package com.example.andbirthday;

import com.example.andbirthday.IntroActivity.OurView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {

	int x,y,x_speed,y_speed;
	int height,width;
	Bitmap b;
	OurView ov;
	int currentFrame = 0;
	int direction = 0;
	
	
	public Sprite(OurView ourView, Bitmap blob) {
		// TODO Auto-generated constructor stub
		b = blob;
		ov = ourView;
		//rows
		height = b.getHeight()/7;
		//columns
		width = b.getWidth()/10;
		x = y = 0;
		x_speed = 5;
		y_speed = 0;
	}

	public void onDraw(Canvas c) {
		// TODO Auto-generated method stub
		update();
		int sourceX = currentFrame * width;
		int sourceY = direction * height;
		Rect src = new Rect(sourceX,sourceY,sourceX +width,sourceY+height);
		Rect dst = new Rect(x,y,x+width,y+height);
		c.drawBitmap(b, src,dst , null);
		
	}

	private void update()  {
		// TODO Auto-generated method stub
		
		//up = 0
		//down = 1 
		//left = 2
		//right = 3
 		
		//facing down
		if(x> ov.getWidth() - width - x_speed)
		{
			x_speed = 0;
			y_speed = 5;
			direction = 1;
		}
		
		//going left
		if(y> ov.getHeight() - height - y_speed)
		{
			x_speed = -5;
			y_speed = 0;
			direction = 2;
		}
		
		//going up
		if(x + x_speed < 0)
		{
			x = 0;
			x_speed = 0;
			y_speed = -5;
			direction = 0;
		}
		//facing right
		if(y + y_speed < 0 )
		{
			y = 0;
			x_speed = 5;
			y_speed = 0;
			direction = 3;
		}
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentFrame = ++currentFrame % 10;
		x += x_speed;
		y += y_speed;
		
	}

}
