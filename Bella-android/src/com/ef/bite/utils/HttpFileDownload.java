package com.ef.bite.utils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeoutException;

import android.util.Log;

public class HttpFileDownload {

	public final static int TimeOut = 20000; // TimeOut 20 second

	String Tag = "HttpFileUpload";
	URL connectURL;

	public HttpFileDownload(String downloadUrl) {
		try {
			connectURL = new URL(downloadUrl);
		} catch (Exception ex) {
			Log.e(Tag, "URL Malformatted");
		}
	}

	/*
	 * 保存到本地存储
	 */
	public void Save(String saveFilePath) {
		try {
			Log.i(Tag, "start to download file to " + saveFilePath);
			URLConnection connection = connectURL.openConnection();
			connection.connect();
			connection.setConnectTimeout(30000);
			// download the file
			InputStream input = new BufferedInputStream(connectURL.openStream());
			OutputStream output = new FileOutputStream(saveFilePath);

			byte data[] = new byte[1024];
			int count;
			while ((count = input.read(data)) != -1) {
				output.write(data, 0, count);
			}

			output.flush();
			output.close();
			input.close();

		}catch (MalformedURLException ex) {
			Log.e(Tag, "URL error: " + ex.getMessage(), ex);
		} catch (IOException ioe) {
			Log.e(Tag, "IO error: " + ioe.getMessage(), ioe);
			ioe.printStackTrace();
		}
	}

	public InputStream Download() {
		if (connectURL == null)
			return null;
		Log.i(Tag,
				"start to download input stream from uri: "
						+ connectURL.toExternalForm());
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) connectURL.openConnection();
			connection.setConnectTimeout(TimeOut);
			connection.connect();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND)
				return null;
			return new BufferedInputStream(connectURL.openStream());
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
			Log.e(Tag, "URL error: " + ex.getMessage(), ex);
			return null;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			Log.e(Tag, "IO error: " + ioe.getMessage(), ioe);
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			Log.e(Tag, "General error: " + ex.getMessage(), ex);
			return null;
		} finally {
			if (connection != null)
				connection.disconnect();
		}
	}
}
