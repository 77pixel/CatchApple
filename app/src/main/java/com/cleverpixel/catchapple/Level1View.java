package com.cleverpixel.catchapple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class Level1View extends View 
{	
    Bitmap[] bmp = null;
    Bitmap cyfry = null;
    Bitmap bmpBee = null;
    Bitmap[] lapple = null;
    Bitmap bmpScore = null;
    Bitmap bmpBon = null;
    
    ArrayList<Owoc> owoce = new ArrayList<Owoc>();
    
    Bee bee = null;
    
    public int w;
    public int h;
    public int w10;
    public int h10;
   
    private int aw = 200;
    private int ah = 200;
    
    public int score = 0;
    public int lost = 3;
    public Boolean endGame = false;
    
    public int bonus = 0;
    
    private Context con;
    
    public Level1View(Context context, AttributeSet attrs) 
    {
        super(context, attrs);
        con = context;
    }
    
    public void makeBmp()
    {
        bmp = new Bitmap[3];
        Bitmap bitmap = null;
        
        bitmap = BitmapFactory.decodeResource(con.getResources(), R.drawable.j1);
        bmp[0] = Bitmap.createScaledBitmap(bitmap, aw, ah, false); 
        bitmap.recycle();
        bitmap = null;
        
        bitmap = BitmapFactory.decodeResource(con.getResources(), R.drawable.j2);
        bmp[1] = Bitmap.createScaledBitmap(bitmap, aw, ah, false); 
        bitmap.recycle();
        bitmap = null;

        bitmap = BitmapFactory.decodeResource(con.getResources(), R.drawable.j3);
        bmp[2] = Bitmap.createScaledBitmap(bitmap, aw, ah, false); 
        bitmap.recycle();
        bitmap = null;
        
        lapple = new Bitmap[2];
        bitmap = BitmapFactory.decodeResource(con.getResources(), R.drawable.jl1);
        lapple[0] = Bitmap.createScaledBitmap(bitmap, 40, 40, false); 
        bitmap.recycle();
        bitmap = null;
        
        bitmap = BitmapFactory.decodeResource(con.getResources(), R.drawable.jl2);
        lapple[1] = Bitmap.createScaledBitmap(bitmap, 40, 40, false); 
        bitmap.recycle();
        bitmap = null;
        
        cyfry = BitmapFactory.decodeResource(con.getResources(), R.drawable.cyferki);
        cyfry = Bitmap.createScaledBitmap(cyfry, 900, 100, false);  
        
        bmpBee = BitmapFactory.decodeResource(con.getResources(), R.drawable.bee);
        bmpBee = Bitmap.createScaledBitmap(bmpBee, 200, 200, false);
        
        bee = new Bee();   
        
        bmpScore = BitmapFactory.decodeResource(con.getResources(), R.drawable.score);
        bmpScore = Bitmap.createScaledBitmap(bmpScore, 280, 100, false);
        
        bmpBon = BitmapFactory.decodeResource(con.getResources(), R.drawable.bon);
        bmpBon = Bitmap.createScaledBitmap(bmpBon, (int) (aw + aw*0.3) , (int) (aw + aw*0.3), false);
    }
    
    public void makeSize(int wi, int hi)
    {
    	aw = (int) (wi * 0.1);
    	ah = (int) (hi * 0.1);
    	
    }

    public static Bitmap rotateBmp(Bitmap source, float angle)
    {
          Matrix matrix = new Matrix();
          matrix.postRotate(angle);
          return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    
    public Owoc owoc()
    {
    	Random r = new Random();
    	
    	int y = r.nextInt(3 * h10) + h10;
    	int x = r.nextInt(w - (2*w10)) + w10;
    	int s = r.nextInt(12)+8;
    	
    	int b = r.nextInt(3);
    	
    	Owoc o = new Owoc();
    	
    	o.posY = y; 
    	o.posX = x;
    	o.speed = s;
    	o.bmp = bmp[b];
    	o.w = aw; 
    	o.h = ah;
    	
    	return o;
    }
    
    
    @SuppressLint("DrawAllocation")
	@Override
    protected void onDraw(Canvas canvas) 
    {
        super.onDraw(canvas);
        
        if(!endGame)
        {
	        //stracone jab�ka__________________________________________________
        	
        	int[] pic = new int[3]; 
        	switch (lost)
        	{
	        	case 0:
	        		pic[0] = 1; pic[1] = 1; pic[2] = 1;
	        	break;	        	
	        	case 1:
	        		pic[0] = 0; pic[1] = 1; pic[2] = 1;
	        	break;
	        	case 2:
	        		pic[0] = 0; pic[1] = 0; pic[2] = 1;
	        	break;
	        	case 3:
	        		pic[0] = 0; pic[1] = 0; pic[2] = 0;
        		break;	
        	}
        	
        	for(int i = 1; i < 4; i++)
        	{
        		canvas.drawBitmap(lapple[pic[i-1]], w - (i*50), 10, null);
        	}
        	
        	
	        //punktacja________________________________________________________
	        int number = score;
	        String n = Integer.toString(number);
	        int pos = n.length();
	        
	        canvas.drawBitmap(bmpScore, 10, 20, null);
	        
	        while ( number > 0) 
	        {
	        	int srcX = (number % 10) * 90;
	            int srcY = 0;
	            
	            Rect src = new Rect(srcX,       srcY, 
	            					srcX + 90,  100);
	            //skala - taka b�dzie wielko�� bitmapy
	            Rect dst = new Rect(pos*80 + 280 , 20, 
	            					(pos*80) + 80 + 280, 100);
	            canvas.drawBitmap(cyfry, src, dst, null);
	            
	            number = number / 10;  
	            pos--;
	            
	        }

	        //rysuj jab�ka_____________________________________________________        
	        Iterator<Owoc> oi = owoce.iterator();
	        while(oi.hasNext())
	        {
	            Owoc o = oi.next();    
	            if (o.active) 
	            {

	            	//bonusowe
	            	if(o.bonus == 1)
	            	{	
	            		canvas.drawBitmap(bmpBon, (float) (o.posX - (aw*0.15)) ,(float) (o.posY - (ah*0.15)), null);   
	            	}	 
	            	
	            	//jab�ko
	            	canvas.drawBitmap(rotateBmp(o.bmp, o.angle) , o.posX, o.posY, null);
	            
	            }
	        }
        
	        //pszcz�ka________________________________________________________
	        canvas.drawBitmap(bmpBee, bee.px, h/2 + bee.py, null);
	        bee.move(w);
        }
    }
}
