package com.example.andbirthday;



import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class IntroActivity extends Activity implements OnTouchListener{

	Sprite sprite;
	Bitmap blob;
	OurView v;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		v = new OurView(this);
		v.setOnTouchListener(this);
		blob = BitmapFactory.decodeResource(getResources(),R.drawable.davis_sheet);
		
		setContentView(v);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.intro, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		v.resume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		v.pause();
	}
	public class OurView extends SurfaceView implements Runnable
	{
		Thread t = null;
		boolean isItOk = false;
		SurfaceHolder holder;
		boolean spriteLoaded = false;
		
		public OurView(Context context) {
			
			super(context);
			
			// TODO Auto-generated constructor stub
			holder = getHolder();
			
		}

		public void pause() {
			// TODO Auto-generated method stub
			isItOk = false;
			while(true)
			{
				try {
					t.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			t = null;
			
		}

		public void resume() {
			// TODO Auto-generated method stub
			isItOk = true;
			t = new Thread(this);
			t.start();
			
		}

		public void run() {
			// TODO Auto-generated method stub
			while(isItOk == true)
			{
				if(spriteLoaded == false)
				{
					sprite = new Sprite(OurView.this,blob);
				spriteLoaded = true;
				}
				
				if(!holder.getSurface().isValid())
				{
					continue;
				}
				
				Canvas c = holder.lockCanvas();
				onDraw(c);
				holder.unlockCanvasAndPost(c);
				
			}
		}
		
		@Override
		protected void onDraw(Canvas c)
		{
			super.onDraw(c);
			c.drawARGB(255, 155, 155, 10);
			
			sprite.onDraw(c);
		}
		
	}

	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		switch(event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			
			break;
		case MotionEvent.ACTION_UP:
			
			break;
		case MotionEvent.ACTION_MOVE:
			
			break;
			
		}
		return true;
	}

	
}
