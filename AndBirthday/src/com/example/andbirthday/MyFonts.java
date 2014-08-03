package com.example.andbirthday;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.view.View;

public class MyFonts extends View{

	Bitmap gball;
	float changingY;
	float changingX = 0;
	Typeface font;
	private String message;
	boolean started = false;
	float[] widths;
	float textLenInPx = 0;
	
	
	public MyFonts(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		//gball = BitmapFactory.decodeResource(getResources(), R.drawable.abc_ab_bottom_solid_dark_holo);
		//changingY = 0;		
		font = Typeface.createFromAsset(context.getAssets(),"AMAZR.TTF" );	
		started = true;
	}
	
	public void setMessage(String message)
	{
		started=true;
		this.message = message;
	}
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);	
		canvas.drawColor(Color.WHITE);
		Paint textPaint = new Paint();
		textPaint.setARGB(200, 33, 165, 37);;
		textPaint.setTextAlign(Align.LEFT);		
		textPaint.setTypeface(font);
		textPaint.setTextSize(50);
		
		if(started == true)
		{
			
			//calc Length in pixels of the message 
			widths = new float[message.length()];
			textPaint.getTextWidths(message, widths);
			for(int i=0;i<widths.length;i++)
			{
				textLenInPx = textLenInPx + widths[i];
			}	
			//start of text 50 px behind the canvas
			changingX = 50 + canvas.getWidth();
		
			started = false;
		}
		canvas.drawText(message, changingX, 50,textPaint);		
		
		if(changingX > -textLenInPx)
		{			
			changingX -= 5;
		}
		else
		{
			//start of text 50 px behind the canvas
			changingX= canvas.getWidth() + 50;
		}
		
		invalidate();
	}

}
