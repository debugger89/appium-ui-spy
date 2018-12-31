package com.debugger.appium.spy.webkit;

import java.util.HashMap;

public class IOSDebugProxyMessage {
	
	private String id;
	
	private String method;
	
	private HashMap<Object, Object> params;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public HashMap<Object, Object> getParams() {
		return params;
	}

	public void setParams(HashMap<Object, Object> params) {
		this.params = params;
	}

	

	
}
