package com.debugger.appium.spy.webkit.socket;

/*
 * Copyright (c) 2010-2018 Nathan Rajlich
 *
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use,
 *  copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following
 *  conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 */

import java.net.URI;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import com.debugger.appium.spy.webkit.IOSDebugProxyMessage;
import com.google.gson.Gson;

/**
 * This example demonstrates how to create a websocket connection to a server.
 * Only the most important callbacks are overloaded.
 */
public class WebKitSocketClient extends WebSocketClient {

	CountDownLatch latch = new CountDownLatch(1);
	private IOSDebugProxyMessage replyMessage;

	public WebKitSocketClient(URI serverUri, Draft draft) {
		super(serverUri, draft);
	}

	public WebKitSocketClient(URI serverURI) {
		super(serverURI);
	}

	public WebKitSocketClient(URI serverUri, Map<String, String> httpHeaders) {
		super(serverUri, httpHeaders);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		// send("Hello, it is me. Mario :)");
		System.out.println("opened connection");
		// if you plan to refuse connection based on ip or httpfields overload:
		// onWebsocketHandshakeReceivedAsClient
	}

	@Override
	public void onMessage(String message) {

		System.out.println("received: " + message);
		
		try {
			IOSDebugProxyMessage reply = new Gson().fromJson(message, IOSDebugProxyMessage.class);
			if ("Page.screencastFrame".equals(reply.getMethod())) {
				
				this.replyMessage = reply;
				latch.countDown();
			}
			System.out.println(reply);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onError(Exception ex) {
		ex.printStackTrace();
		// if the error is fatal then onClose will be called additionally
	}

	public IOSDebugProxyMessage requestScreenshot(String requestMessage) {

		send(requestMessage);
		try {
			// wait for the threads to be done
			latch.await();
			return this.replyMessage;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void closeConnection() {
		close();
	}

	public static void main(String[] args) throws Exception {
		WebKitSocketClient c = new WebKitSocketClient(
				new URI("ws://localhost:4747/ios_SIMULATOR/ws://127.0.0.1:4848/devtools/page/1")); // more about drafts
																									// here:
																									// http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
		c.connectBlocking();
		
		IOSDebugProxyMessage reply = c.requestScreenshot("{\"id\": \"ws://127.0.0.1:4848/devtools/page/1\",\"method\":\"Page.startScreencast\",\"params\":{\"format\":\"jpeg\",\"quality\":80}}");
		System.out.println("***********");
		System.out.println(reply.getParams().get("data"));
		
		System.out.println("################## END #####################");
		/*
		c.send("{\"id\": \"ws://127.0.0.1:4848/devtools/page/1\",\"method\":\"Page.startScreencast\",\"params\":{\"format\":\"jpeg\",\"quality\":80}}");

		Thread.sleep(3000);
		c.send("{\"id\": \"ws://127.0.0.1:4848/devtools/page/1\",\"method\":\"Page.startScreencast\",\"params\":{\"format\":\"jpeg\",\"quality\":80}}");

		Thread.sleep(3000);
		c.send("{\"id\": \"ws://127.0.0.1:4848/devtools/page/1\",\"method\":\"Page.startScreencast\",\"params\":{\"format\":\"jpeg\",\"quality\":80}}");
		*/
		c.close();
		
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		// TODO Auto-generated method stub
		
	}

}