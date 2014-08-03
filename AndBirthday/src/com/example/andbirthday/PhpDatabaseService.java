package com.example.andbirthday;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class PhpDatabaseService extends IntentService {

	JSONParser jParser = new JSONParser();

	// URI back to App
	public static final String NOTIFICATION = "com.example.andbirthday";
	public static final String RESULT = "result";
	private static String url_message = "http://sickandy.bplaced.net/BirthdayAppTest/get_Message.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_Message = "messages";
	private static final String TAG_PID = "id";
	
	final int PHP_DATABASE_SERVICE_CODE = 1;
	final String SERVICE_CODE = "serviceCode";

	// products JSONArray
	JSONArray messages = null;

	public PhpDatabaseService() {
		super("PhpDatabaseService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		String MsgID = intent.getStringExtra("currentID");
		String currentTab = intent.getStringExtra("phpDBTable");
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", MsgID));
		params.add(new  BasicNameValuePair("messageTab", currentTab));
		// getting JSON string from URL
		JSONObject json = jParser.makeHttpRequest(url_message, "POST", params);

		// Check your log cat for JSON reponse
		//Log.d("All messages: ", json.toString());

		try {
			// Checking for SUCCESS TAG
			int success = json.getInt(TAG_SUCCESS);

			if (success == 1) {
			
				messages = json.getJSONArray(TAG_Message);

				JSONObject c = messages.getJSONObject(0);

				// Storing each json item in variable
				String id = c.getString(TAG_PID);
				String msg = c.getString("message");

				

				publishResults(msg, 0);

			} else {

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void publishResults(String resultString, int result) {
		Intent intent = new Intent(NOTIFICATION);
		intent.putExtra(SERVICE_CODE, PHP_DATABASE_SERVICE_CODE);
		intent.putExtra(RESULT, resultString);
		sendBroadcast(intent);
	}

}
