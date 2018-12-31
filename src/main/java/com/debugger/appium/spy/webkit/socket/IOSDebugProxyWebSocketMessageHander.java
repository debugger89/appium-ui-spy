package com.debugger.appium.spy.webkit.socket;

import java.util.HashMap;

import com.debugger.appium.spy.webkit.IOSDebugProxyMessage;
import com.debugger.appium.spy.webkit.WebKitConnector;
import com.google.gson.Gson;

public class IOSDebugProxyWebSocketMessageHander {

	private WebKitConnector connector;
	
	public IOSDebugProxyWebSocketMessageHander(WebKitConnector connector) {
		this.connector = connector;
	}
	
	public String getScreenCastMessageJson() {
		
		IOSDebugProxyMessage message = new IOSDebugProxyMessage();
		message.setId(connector.getId());
		message.setMethod("Page.startScreencast");
		HashMap<Object, Object> params = new HashMap<Object, Object>();
		params.put("format", "jpeg");
		params.put("quality", 80);
		message.setParams(params);
		
		return new Gson().toJson(message);
		
	}
}
