package com.example.andbirthday;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class HasiActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hasi_layout);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	public void onClick(View v)
	{
		switch(v.getId())
		{
		case R.id.button1:
			Intent waIntent = new Intent(Intent.ACTION_SEND);
			waIntent.setType("text/plain");
			String text = "Hallo Hase";
			waIntent.setPackage("com.whatsapp");
			waIntent.putExtra(Intent.EXTRA_TEXT, text);
			startActivity(Intent.createChooser(waIntent, "Hallo Hase"));
			break;
			
		case R.id.button2:
			startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+Uri.encode("+4916091076026"))));
			break;
			
		case R.id.button3:
			
			break;
		}
		
	}

}
