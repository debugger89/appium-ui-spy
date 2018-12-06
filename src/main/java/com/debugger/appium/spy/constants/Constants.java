package com.debugger.appium.spy.constants;

import org.openqa.selenium.By;

import javafx.scene.paint.Color;

public class Constants {
	
	public final static String[] IGNORING_TAGS_JSOUP = {"script", "meta", "style", "head", "#data", "#comment"};

	public final static double RECTANGLE_WEIGHT = 2;
	
	public final static Color RECTANGLE_COLOR = Color.PURPLE;
	
	public final static String APPIUM_COORDINATE_ATTRIBUTE = "_appium_element_coords";
	
	public final static By FIRST_WEBVIEW_LOCATOR = By.xpath("//android.webkit.WebView");
}
