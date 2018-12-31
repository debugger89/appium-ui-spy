package com.debugger.appium.spy.webkit;

public class WebKitConnector {
	
	private String devtoolsFrontendUrl;
	
	private String title;
	
	private String url;
	
	private String webSocketDebuggerUrl;
	
	private String appId;
	
	private String id;
	
	private String adapterType;
	
	private WebKitConnectorMetaData metadata;

	public String getDevtoolsFrontendUrl() {
		return devtoolsFrontendUrl;
	}

	public void setDevtoolsFrontendUrl(String devtoolsFrontendUrl) {
		this.devtoolsFrontendUrl = devtoolsFrontendUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWebSocketDebuggerUrl() {
		return webSocketDebuggerUrl;
	}

	public void setWebSocketDebuggerUrl(String webSocketDebuggerUrl) {
		this.webSocketDebuggerUrl = webSocketDebuggerUrl;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAdapterType() {
		return adapterType;
	}

	public void setAdapterType(String adapterType) {
		this.adapterType = adapterType;
	}

	public WebKitConnectorMetaData getMetadata() {
		return metadata;
	}

	public void setMetadata(WebKitConnectorMetaData metadata) {
		this.metadata = metadata;
	}
	
	

}
