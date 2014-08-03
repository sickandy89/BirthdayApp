package com.example.andbirthday;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.IntentService;
import android.content.Intent;

public class UpdateService extends IntentService {
	
	public static final String URL = "http://sickandy.bplaced.net/BirthdayApp/version.txt";	
	public static final String RESULT = "result";
	public static final String NOTIFICATION = "com.example.andbirthday";
	private String CurrAppVersion;
	final int UPDATE_SERVICE_CODE = 0;
	String SERVICE_CODE = "serviceCode";
	public UpdateService() {
		super("UpdateService");
		
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String resultdonwnload = null;
		try {
			resultdonwnload = downloadText();
		} catch (IOException e) {
			e.printStackTrace();
		}
		CurrAppVersion = intent.getStringExtra("CurrentAppVersion");
		if(CurrAppVersion.equals(resultdonwnload))
		{
			publishResults(resultdonwnload, -1);
		}
		else
		{
			publishResults(resultdonwnload, 1);
		}
		}

	private void publishResults(String resultString, int result) {
		Intent intent = new Intent(NOTIFICATION);
		intent.putExtra(SERVICE_CODE, UPDATE_SERVICE_CODE);
		intent.putExtra("code", result);
		intent.putExtra(RESULT, resultString);
		sendBroadcast(intent);
	}

	private String downloadText() throws IOException {
		int BUFFER_SIZE = 2000;
		InputStream in = null;
		in = openHttpConnection();

		String str = "";
		if (in != null) {
			InputStreamReader isr = new InputStreamReader(in);
			int charRead;
			char[] inputBuffer = new char[BUFFER_SIZE];
			try {
				while ((charRead = isr.read(inputBuffer)) > 0) {
					// ---convert the chars to a String---
					String readString = String.copyValueOf(inputBuffer, 0,
							charRead);
					str += readString;
					inputBuffer = new char[BUFFER_SIZE];
				}
				in.close();
			} catch (IOException e) {
				return "";
			}
		}
		return str;
	}

	private InputStream openHttpConnection() {
		InputStream in = null;
		int response = -1;

		URL url = null;
		try {
			url = new URL(URL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		URLConnection conn = null;
		try {
			conn = url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!(conn instanceof HttpURLConnection))
			try {
				throw new IOException("Not an HTTP connection");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
			}
		} catch (Exception ex) {
			try {
				throw new IOException("Error connecting");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return in;

	}
}
