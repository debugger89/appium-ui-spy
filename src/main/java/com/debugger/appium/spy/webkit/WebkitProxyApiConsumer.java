package com.debugger.appium.spy.webkit;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.debugger.appium.spy.constants.Constants;
import com.debugger.appium.spy.exceptions.NoDeviceConnectedException;
import com.google.gson.Gson;

public class WebkitProxyApiConsumer {
	
	private final String proxyURL = "http://localhost:"+Constants.IOS_DEBUG_PROXY_PORT+"/json";
	
	private CloseableHttpClient httpclient;
	
	
	public WebkitProxyApiConsumer() {
		httpclient = HttpClients.createDefault();
	}

	public WebKitConnector getWebViewComponentConnector() throws IOException, HttpException, NoDeviceConnectedException {
		
		HttpGet httpGet = new HttpGet(proxyURL);
		CloseableHttpResponse response1 = httpclient.execute(httpGet);
		
		if(response1.getStatusLine().getStatusCode() != 200) {
			throw new HttpException("Webkit debug proxy throws an error. HTTP code : "+response1.getStatusLine().getStatusCode());
		}
		
		try {
		    HttpEntity entity1 = response1.getEntity();
		    String responseString = EntityUtils.toString(entity1, "UTF-8");
		    WebKitConnector[] connectors = new Gson().fromJson(responseString, WebKitConnector[].class);
		    EntityUtils.consume(entity1);
		    
		    if(connectors.length == 0) {
		    	throw new NoDeviceConnectedException();
		    }
		    
		    // TO DO : For now, the first webview is taken. Change to parameterize this and get from user.
		    
		    return connectors[0];
		    
		} finally {
		    response1.close();
		}
		
		
	}
	


}
