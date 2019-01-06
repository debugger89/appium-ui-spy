package com.debugger.appium.spy.constants;

import org.openqa.selenium.By;

import javafx.scene.paint.Color;

public class Constants {
	
	public final static String[] IGNORING_TAGS_JSOUP = {"script", "meta", "style", "head", "#data", "#comment"};

	public final static double RECTANGLE_WEIGHT = 2;
	
	public final static Color RECTANGLE_COLOR = Color.PURPLE;
	
	public final static String APPIUM_COORDINATE_ATTRIBUTE = "_appium_element_coords";
	
	public final static By ANDROID_FIRST_WEBVIEW_LOCATOR = By.xpath("//android.webkit.WebView");
	
	public final static By IOS_FIRST_WEBVIEW_LOCATOR = By.xpath("//XCUIElementTypeWebView");
	
	public final static int IOS_DEBUG_PROXY_PORT = 4747;
	
	public final static long IOS_DEBUG_PROXY_STARTUP_WAIT_TIME_MILLIS = 5000;
}
