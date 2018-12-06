package com.debugger.appium.spy.ui.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.configuration.ConfigurationException;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.debugger.appium.spy.constants.Constants;
import com.debugger.appium.spy.constants.MobileOS;
import com.debugger.appium.spy.driver.DriverBase;
import com.debugger.appium.spy.driver.ElementCoordinates;
import com.debugger.appium.spy.utils.DialogHandler;
import com.google.gson.Gson;

import io.appium.java_client.remote.MobileBrowserType;
import io.appium.java_client.remote.MobileCapabilityType;
import javafx.fxml.FXML;
import javafx.geometry.Dimension2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import sun.misc.BASE64Decoder;

public class FxFXMLController {

	@FXML
	private ImageView screenshotMirror;

	@FXML
	private ToggleButton serverStartStop;

	@FXML
	private TreeView<NodeTag> elementTree;

	@FXML
	private Canvas mirrorCanvas;

	@FXML
	private AnchorPane mirrorRootAnchorPane;
	
	@FXML
	private ComboBox<?> osSelection;

	private Dimension currentPageSize;

	public FxFXMLController() {
	}
	
	private String getBase64Screenshot() throws IOException {
		
		DriverBase driverbase = DriverBase.getInstance();
		if(Session.currentOS.equals(MobileOS.IOS)) {
			return driverbase.getScreeenshotBase64();
		}else {
			return driverbase.getWebViewNativeScreenshot(Constants.ANDROID_FIRST_WEBVIEW_LOCATOR);
		}
	}

	@FXML
	private void refreshScreenshot() throws IOException {
		System.out.println("hoorah");
		DriverBase driverbase = DriverBase.getInstance();

		driverbase.getScreeenshotToFile(new File("DeviceScreenshot_Crurent.png"));
		
		String rocketImgStr = getBase64Screenshot();
		
		BASE64Decoder base64Decoder = new BASE64Decoder();
		ByteArrayInputStream rocketInputStream = new ByteArrayInputStream(base64Decoder.decodeBuffer(rocketImgStr));
		Image img = new Image(rocketInputStream);
		

		
		String pagesource = driverbase.getCordinatedPageSource();
		currentPageSize = driverbase.getPageSize();

		System.out.println(pagesource);

		// resize the canvas to fit anchor pane
		mirrorCanvas.setWidth(mirrorRootAnchorPane.getWidth());
		mirrorCanvas.setHeight(mirrorRootAnchorPane.getHeight());
		
		GraphicsContext gc = mirrorCanvas.getGraphicsContext2D();
		gc.drawImage(img, 0, 0, mirrorCanvas.getWidth(), mirrorCanvas.getHeight());

		drawRectangleOutline();

		JsoupParser htmlparser = new JsoupParser();
		TreeItem<NodeTag> htmltree = htmlparser.createHTMLTreeNode(pagesource);
		elementTree.setRoot(htmltree);
		expandTreeView(htmltree);

	}

	private void drawRectangleForNodeElement(ElementCoordinates tagCoordinates) {
		double widthFactor = getResizedFactor(currentPageSize.width, mirrorCanvas.getWidth());
		double heightFactor = getResizedFactor(currentPageSize.height, mirrorCanvas.getHeight());

		GraphicsContext gc = mirrorCanvas.getGraphicsContext2D();
		gc.setLineWidth(Constants.RECTANGLE_WEIGHT);
		gc.setStroke(Constants.RECTANGLE_COLOR);

		gc.strokeRect((tagCoordinates.getX() * widthFactor), (tagCoordinates.getY() * heightFactor),
				(tagCoordinates.getWidth() * widthFactor), (tagCoordinates.getHeight() * heightFactor));

	}

	private void drawRectangleOutline() {
		GraphicsContext gc = mirrorCanvas.getGraphicsContext2D();
		gc.setLineWidth(2);
		gc.setStroke(Color.GREEN);

		gc.strokeRect(0, 0, mirrorCanvas.getWidth(), mirrorCanvas.getHeight());
	}

	private double getResizedFactor(double original, double resized) {

		double factor = resized / original;

		return factor;
	}

	@FXML
	private void treeNodeClickHandler() {

		TreeItem<NodeTag> item = (TreeItem<NodeTag>) elementTree.getSelectionModel().getSelectedItem();
		NodeTag tag = item.getValue();
		String coordinateJson = tag.getAttributes().get(Constants.APPIUM_COORDINATE_ATTRIBUTE);
		ElementCoordinates coordinates = new Gson().fromJson(coordinateJson, ElementCoordinates.class);
		drawRectangleForNodeElement(coordinates);
		System.out.println("clicked : " + tag.getTagName() + " " + coordinateJson  + " | " + tag);
	}

	private void expandTreeView(TreeItem<?> item) {
		if (item != null && !item.isLeaf()) {
			item.setExpanded(true);
			for (TreeItem<?> child : item.getChildren()) {
				expandTreeView(child);
			}
		}
	}
	
	
	private void setCurrentOS() {
		String selectedOS = osSelection.getSelectionModel().getSelectedItem().toString();
		if("IOS".equals(selectedOS)) {
			Session.currentOS = MobileOS.IOS;
		}else if("ANDROID".equals(selectedOS)) {
			Session.currentOS = MobileOS.ANDROID;
		}
	}

	@FXML
	private void startStopServer() {
		
		setCurrentOS();
		
		if (serverStartStop.isSelected()) {
			serverStartStop.setText("Stop Service");

			DriverBase driverbase = DriverBase.getInstance();
			DesiredCapabilities desiredCapabilities = getDesiredCapabilitiesFRomProperty();
			try {
				
				driverbase.initializeAppiumDriver(desiredCapabilities, Session.currentOS);
				
				osSelection.setDisable(true);

			} catch (ConfigurationException e) {

				e.printStackTrace();
				new DialogHandler().showExceptionDialog("Config File Error", "Appium Config File Read Error",
						"Cannot read the appium.properties configuration file", e);
			} catch (MalformedURLException e) {

				e.printStackTrace();
				new DialogHandler().showExceptionDialog("Appium Config Error", "Appium Config Error",
						"Incorrect appium server url. Correct url example : http://127.0.0.1:4723/wd/hub", e);
			} catch (WebDriverException e) {

				e.printStackTrace();
				new DialogHandler().showExceptionDialog("Appium Driver Error", "Appium Driver Error",
						"Error occured while performing an action with the appium driver", e);
			} catch (Exception e) {

				e.printStackTrace();
				new DialogHandler().showExceptionDialog("Appium Initialization Error", "Appium Initialization Error",
						"Error occured while initializing the appium server", e);
			}

		} else {
			osSelection.setDisable(false);
			serverStartStop.setText("Start Service");
			DriverBase driverbase = DriverBase.getInstance();
			if (driverbase.getDriver() != null) {
				driverbase.getDriver().quit();
			}
		}

	}

	// To be removed later when the config window comes in
	private DesiredCapabilities getDesiredCapabilitiesFRomProperty() {
		
		if (Session.currentOS.equals(MobileOS.IOS)) {

			DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
			desiredCapabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Safari");
			desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
			desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "10.3");
			desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "300");
			desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 5");
			return desiredCapabilities;
		} else {
			DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
			desiredCapabilities.setCapability(MobileCapabilityType.BROWSER_NAME, MobileBrowserType.CHROME);
			desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
			desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "7");
			desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "300");
			desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
			desiredCapabilities.setCapability(ChromeOptions.CAPABILITY,
					new ChromeOptions().addArguments("no-first-run", "show_on_first_run_allowed=false"));
			return desiredCapabilities;
		}
		

	}

}