package com.debugger.appium.spy.webkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.debugger.appium.spy.constants.Constants;
import com.debugger.appium.spy.utils.OsUtils;

public class WebKitProxySession {

	private Process sessionProcess;

	private static WebKitProxySession instance = null;

	private WebKitProxySession() {
	}

	public static WebKitProxySession getInstance() throws IOException {
		if (instance == null)
			instance = new WebKitProxySession();

		return instance;
	}

	public void initiateSession() throws IOException {

		if (sessionProcess == null || !sessionProcess.isAlive()) {

			if (OsUtils.isUnix()) {

				String scriptPath = new File("scripts" + File.separator + "RunDebugProxy.sh").getAbsolutePath();

				String cmd = "sh " + scriptPath;

				try {
					sessionProcess = Runtime.getRuntime().exec(cmd);
					Thread.sleep(Constants.IOS_DEBUG_PROXY_STARTUP_WAIT_TIME_MILLIS);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			} else {
				throw new UnsupportedOperationException(
						"Unsupported operating system. Only Mac OS is supported for IOS automation.");
			}
		}
	}

	public void closeSession() {
		if (sessionProcess != null && sessionProcess.isAlive()) {

			sessionProcess.destroy();
		}
	}

}
