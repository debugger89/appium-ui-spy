package appium.experiments.spy;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class WebViewSurveyor {

	public void markAllFieldsInApp(WebDriver driver) {

		try {

			String js = FileUtils.readFileToString(new File("WebviewSurveyor.js"));
			executeScript(driver, js);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void executeScript(WebDriver driver, String js) {
		
		JavascriptExecutor jsexecutor = (JavascriptExecutor) driver;
		jsexecutor.executeScript(js);

	}

}
