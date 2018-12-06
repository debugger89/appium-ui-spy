package com.debugger.appium.spy.driver;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.debugger.appium.spy.constants.MobileOS;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class DriverBase {

	private AppiumDriver<WebElement> driver;
	private static DriverBase driverBaseInstance = null;

	private DriverBase() {
	}

	public static DriverBase getInstance() {
		if (driverBaseInstance == null)
			driverBaseInstance = new DriverBase();

		return driverBaseInstance;
	}

	public void initializeAppiumDriver(DesiredCapabilities capabilities, MobileOS os)
			throws MalformedURLException, ConfigurationException {

		String appiumURL = getAppiumProperties("APPIUM_URL");
		String appiumTimeout = getAppiumProperties("APPIUM_TIMEOUT_SECONDS");
		int appiumTimeoutInt = Integer.parseInt(appiumTimeout);

		URL url = new URL(appiumURL);

		if (os.equals(MobileOS.ANDROID)) {
			driver = new AndroidDriver<WebElement>(url, capabilities);
			driver.manage().timeouts().implicitlyWait(appiumTimeoutInt, TimeUnit.SECONDS);
		} else if (os.equals(MobileOS.IOS)) {
			driver = new IOSDriver<WebElement>(url, capabilities);
			driver.manage().timeouts().implicitlyWait(appiumTimeoutInt, TimeUnit.SECONDS);
		}

		// REMOVE LATER
		driver.get("https://www.google.com");
		executeScript("document.documentElement.webkitRequestFullscreen()");

	}
	
	public Object executeScript(String script, Object... args) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return js.executeScript(script, args);
	}

	public String getScreeenshotBase64() {

		String imageString = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
		return imageString;

	}

	public void getScreeenshotToFile(File destination) throws IOException {

		File image = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(image, destination);

	}

	private String getAppiumProperties(String key) throws ConfigurationException {
		Configuration config = new PropertiesConfiguration("appium.properties");
		return (String) config.getProperty(key);
	}

	public AppiumDriver<WebElement> getDriver() {
		return driver;
	}

	public void setDriver(AppiumDriver<WebElement> driver) {
		this.driver = driver;
	}

	public String getCordinatedPageSource() {

		WebViewSurveyor surveyor = new WebViewSurveyor();
		surveyor.markAllFieldsInApp(driver);
		String pagesource = driver.getPageSource();
		return pagesource;
	}

	public Dimension getPageSize() {

		Dimension windowSize;

		if (driver.getContext().equals("NATIVE_APP")) {

			windowSize = driver.manage().window().getSize();

		} else {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			int width = ((Long) js.executeScript("return window.innerWidth")).intValue();
			int height = ((Long) js.executeScript("return window.innerHeight")).intValue();
			windowSize = new Dimension(width, height);

		}

		return windowSize;

	}
	
	
	public String getWebViewNativeScreenshot(By webviewLocator) throws IOException {

		String currentContext = driver.getContext();
		try {
			driver.context("NATIVE_APP");
			byte[] imgarr = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

			WebElement webview = driver.findElement(webviewLocator);
			Dimension webviewSize = webview.getSize();
			Point webviewLocation = webview.getLocation();

			System.out.println(
					webviewSize.width + "," + webviewSize.height + " | " + webviewLocation.x + "," + webviewLocation.y);

			BufferedImage original = createImageFromBytes(imgarr);
			System.out.println(original.getWidth() + "," + original.getHeight());

			Rectangle rect = new Rectangle(webview.getLocation().x, webview.getLocation().y, original.getWidth(),
					webviewSize.height);

			BufferedImage cropped = cropImage(original, rect);
			ImageIO.write(cropped, "png", new File("CroppedWebView.png"));
			
			return imgToBase64String(cropped, "PNG");
			
		} finally {
			driver.context(currentContext);
		}

	}
	
	private BufferedImage cropImage(BufferedImage src, Rectangle rect) {
		BufferedImage dest = src.getSubimage(rect.x, rect.y, rect.width, rect.height);
		return dest;
	}

	private BufferedImage createImageFromBytes(byte[] imageData) {
		ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
		try {
			return ImageIO.read(bais);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private String imgToBase64String(final RenderedImage img, final String formatName) {
	    final ByteArrayOutputStream os = new ByteArrayOutputStream();
	    try {
	        ImageIO.write(img, formatName, Base64.getEncoder().wrap(os));
	        return os.toString(StandardCharsets.ISO_8859_1.name());
	    } catch (final IOException ioe) {
	        throw new UncheckedIOException(ioe);
	    }
	}

}
