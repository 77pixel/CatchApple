package com.cleverpixel.catchapple;

import android.graphics.Bitmap;

public class Owoc 
{
	
	public int posX;
	public int posY;
	public int speed;
	
	public Bitmap bmp;

	public Boolean touched = false;
	
	public int lposX = 0;
	public int lposY = 0;
	
	public int angle = 0;
	
	public int status = 0;
	
	public Boolean active = true;
	
	public int w;
	public int h;
	
	public int bonus = 0;
	
	
	
	public Owoc()
	{	
	}
	
	public void goBasket()
	{ 
		lposX = posX;
		lposY = posY; 
	}
	
}
