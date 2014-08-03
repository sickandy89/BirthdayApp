package com.example.andbirthday;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	MediaPlayer mySong;
	
	private String message;
	private TextView versionTextView; // Shows Current Version of App
	private String AppVersion;
	private String CurrentAppVersion;
	private int CurrentID;
	MyFonts mf;

	final String PHP_DB_TABLE = "phpDBTable";

	String currentTable = "";

	// Service Codes:
	final String SERVICE_CODE = "serviceCode";
	final int UPDATE_SERVICE_CODE = 0;
	final int PHP_DATABASE_SERVICE_CODE = 1;

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				int intServiceCode = bundle.getInt(SERVICE_CODE);
				switch (intServiceCode) {
				case UPDATE_SERVICE_CODE:
					setAppVersion(bundle.getString(UpdateService.RESULT));

					int resultCode = bundle.getInt("code");
					if (resultCode == -1) {
						Toast.makeText(context.getApplicationContext(),
								"Keine neuen Updates verfügbar",
								Toast.LENGTH_LONG).show();
					}
					if (resultCode == 1) {
						Intent updateIntent = new Intent(
								Intent.ACTION_VIEW,
								Uri.parse("http://sickandy.bplaced.net/BirthdayAppTest/AndBirthday.apk"));
						startActivity(updateIntent);
					}
					break;

				case PHP_DATABASE_SERVICE_CODE:
					String msg = bundle.getString(PhpDatabaseService.RESULT);
					setMessage(msg);
					mf.setMessage(msg);
					break;
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		
		setCurrentID();
		setCurrentAppVersion(getVersionCode(this));
		setMessage("");
		setContentView(R.layout.activity_main);
		mySong = MediaPlayer.create(MainActivity.this,R.raw.hipsterhass );
		mySong.start();
		mf = new MyFonts(this);
		mf.setMessage(message);

		LinearLayout mainLayout = (LinearLayout) findViewById(R.id.myLinearLayout);
		mainLayout.addView(mf);

		versionTextView = (TextView) findViewById(R.id.VersionTextView);
		versionTextView.setText("Version:" + getCurrentAppVersion());
		
		
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(receiver, new IntentFilter(UpdateService.NOTIFICATION));
	}

	@Override
	protected void onPause() {
		super.onPause();
		mySong.release();
		unregisterReceiver(receiver);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.action_settings:
			return true;

		case R.id.menu_msgUpdate:
			if (isOnline() == true) {
				Intent GetMsgIntent = new Intent(this, PhpDatabaseService.class);
				GetMsgIntent.putExtra("currentID", Integer.toString(CurrentID));
				// Which DB Table to get Message
				GetMsgIntent.putExtra(PHP_DB_TABLE, getCurrentTable());
				startService(GetMsgIntent);
				return true;
			} else {
				Toast.makeText(getApplicationContext(), "Keine Internetverbindung...",
						Toast.LENGTH_LONG).show();
				return true;
			}

		case R.id.menu_appUpdate:// Get AppVersion on Server to compare with
									// current version
			Intent intent = new Intent(this, UpdateService.class);
			// add infos for the service which file to download and where to
			// store
			intent.putExtra(UpdateService.URL,
					"http://sickandy.bplaced.net/BirthdayApp/version.txt");
			intent.putExtra("CurrentAppVersion", CurrentAppVersion);
			startService(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_1:

			Intent HasiIntent = new Intent(MainActivity.this,
					HasiActivity.class);
			startActivity(HasiIntent);
			
//			Intent IntroIntent = new Intent(MainActivity.this,
//					IntroActivity.class);
//			startActivity(IntroIntent);
			break;
		}

	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			System.out.println("online!!!");
			return true;
		}
		return false;
	}

	public static String getVersionCode(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException ex) {
		}
		return null;
	}

	private void setCurrentID() {

		// TODO Auto-generated method stub
		/** The date at the end of the last century */

		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);

		// Morning 6:00 to 11:59
		if (hour < 12 && hour >= 6) {
			this.CurrentID = 0;
			setCurrentTable("Morning");
		}
		// Day 12:00 to 17:59
		if (hour < 18 && hour >= 12) {
			Date Bday = new GregorianCalendar(2014, 06, 26, 00, 00).getTime();
			/** Today's date */
			Date today = new Date();

			// Get msec from each, and subtract.
			long diff = today.getTime() - Bday.getTime();

			diff = diff / (1000 * 60 * 60 * 24);
			this.CurrentID = (int) diff;
			setCurrentTable("Day");
		}
		// Evening 18:00 to 24
		if (hour < 24 && hour >= 18) {
			this.CurrentID = 0;
			setCurrentTable("Evening");
		}
		// Night 0:00 to 5:59
		if (hour < 6 && hour >= 0) {
			this.CurrentID = 0;
			setCurrentTable("Night");
		}
	}

	// Set Version of running App
	public void setCurrentAppVersion(String CurrentAppVersion) {
		this.CurrentAppVersion = CurrentAppVersion;
	}

	// get Version of Running App
	public String getCurrentAppVersion() {
		return CurrentAppVersion;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setAppVersion(String AppVersion) {
		this.AppVersion = AppVersion;
	}

	public String getAppVersion() {
		return AppVersion;
	}

	public String getCurrentTable() {
		return this.currentTable;
	}

	public void setCurrentTable(String currentTable) {
		this.currentTable = currentTable;
	}
}
