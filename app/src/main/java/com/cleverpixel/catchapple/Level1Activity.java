package com.cleverpixel.catchapple;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Level1Activity extends Activity 
{
	private Level1View aView = null;
	private Timer myTimer = null;
	
	private RelativeLayout layEnd = null;
	private ImageButton imbRestart = null;
	private ImageButton imbStart = null;
	private ImageView imGame = null;
	
	private TextView tvHiscore;
	private TextView tvScore;
	private int secTic = 0;
	
	private int bonScore = 1;
	private long lastTic = 0;
	private long nextTic = 0;
	private int bonusTime = 400; 
	private Context con = null;
	
	private int hiscore = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);	
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.level1activity);
		
		con = this.getApplicationContext();
		
		aView = (Level1View) findViewById(R.id.appleView1); 
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		    
		aView.w = size.x;
		aView.h = size.y;
		aView.w10 = (int) (size.x * 0.1F);
		aView.h10 = (int) (size.y * 0.1F);
		aView.makeSize(aView.w, aView.w);
		aView.makeBmp();
		aView.owoce.add(aView.owoc());
		
		layEnd = (RelativeLayout) findViewById(R.id.LayGameOver);
		imGame = (ImageView) findViewById(R.id.imageGame);
		
		tvHiscore = (TextView) findViewById(R.id.tvHscoreL1);
		tvScore = (TextView) findViewById(R.id.tvScoreL1);
		
		imbRestart = (ImageButton) findViewById(R.id.imbRestart);
		imbRestart.setVisibility(ImageButton.GONE);
		imbRestart.setOnClickListener(new View.OnClickListener() 
	    {
            @Override
            public void onClick(View v) 
            {
            	startGame();
            }
	    }); 
		
		imbStart = (ImageButton) findViewById(R.id.imbStart);
		imbStart.setOnClickListener(new View.OnClickListener() 
	    {
            @Override
            public void onClick(View v) 
            {
            	startGame();
            }
	    }); 
		
	    aView.setOnTouchListener(new Level1View.OnTouchListener() 
		{				
	    	@Override
			public boolean onTouch(View arg0, MotionEvent arg1) 
			{								
	    		nextTic = System.currentTimeMillis();
	    		
	    		//owoce dotyk
	    		Iterator<Owoc> oi = aView.owoce.iterator();
		        while(oi.hasNext())
		        {
		            Owoc o = oi.next();
		            
		            if(arg1.getX() - o.w < o.posX  & arg1.getX() > o.posX )
			    	{	
			    		if(arg1.getY() - o.h < o.posY  & arg1.getY() > o.posY )
			    		{
				    		o.touched = true;
				    		
			 				//bonus____________________________________________________________
					    	if(nextTic - bonusTime < lastTic) 
					    	{
					    		//Play BONUS______________________________________________________
					    		stopService(new Intent(con, PlayBonus.class));
					    		Intent intent = new Intent(con, PlayBonus.class);
					    		con.startService(intent);
					    		//nie min¹³ czas
					    		bonScore += bonScore;
					    		aView.bonus = 1;
					    		o.bonus = 1;
					    	}
					    	else
					    	{	
					    		//Play BOING______________________________________________________
					    		stopService(new Intent(con, PlayBoing.class));
					    		Intent intent = new Intent(con, PlayBoing.class);
					    		con.startService(intent);
					    		///czas min¹³
					    		if (aView.bonus == 1)
					    		{
						    		aView.score += (2 * bonScore);
						    		bonScore = 0;
						    		aView.bonus = 0;
						    	}
					    	}
				    	}
			    	}
		        }
		     
		        //pszczola_________________________________________________________________________
		        if(arg1.getX() - 200 < aView.bee.px  & arg1.getX() > aView.bee.px )
		    	{	
		    		
		        	if(arg1.getY() - aView.h < aView.bee.py + aView.h/2  & arg1.getY() > aView.bee.py + aView.h/2 )
		    		{
			    		aView.bee.status = 1;
			    		//Play LOOSE______________________________________________________
			    		Intent intent = new Intent(con, PlayPac.class);
			    		con.startService(intent);
			    	}
		    	}
		     
		        lastTic = System.currentTimeMillis();
		        return false;
			}
		});
	    
	    getHiscore();
	    layEnd.setVisibility(RelativeLayout.VISIBLE);
	}
	
	@Override
	public void onDestroy() 
	{
	    super.onDestroy();
		turnOffTimer();
	}
	
	@Override
	public void onPause() 
	{
	    super.onPause();
		gameOver();
	}
	
	public void getHiscore()
	{
		SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(con);
        hiscore = prefs1.getInt("hiscoreL1", 0);
        tvHiscore.setText("HI SCORE: " + String.valueOf(hiscore));
        tvScore.setText("SCORE: " + String.valueOf(aView.score));
	}
	
	public void setHiscore()
	{
		if (aView.score >= hiscore)
		{
			hiscore = aView.score;
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(con);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("hiscoreL1", hiscore);
			editor.commit();
		}
		tvHiscore.setText("HI SCORE: " + String.valueOf(hiscore));
		tvScore.setText("SCORE: " + String.valueOf(aView.score));
	}
	
	private void startGame()
	{
		aView.endGame = false;
        aView.owoce.clear();
        aView.score = 0;
        aView.lost = 3;
        
        turnOnTimer();
        lastTic = System.currentTimeMillis();
        
        //Play MUSIC______________________________________________________
		//stopService(new Intent(con, PlayMusic.class));
		Intent intent = new Intent(con, PlayMusic.class);
		con.startService(intent);
	}
	
	private void gameOver()
	{
		setHiscore();	

		turnOffTimer();
		layEnd.setVisibility(RelativeLayout.VISIBLE);
		imbRestart.setVisibility(ImageButton.VISIBLE);
		imbStart.setVisibility(ImageButton.GONE);
		imGame.setImageResource(R.drawable.gameovertxt);
		stopService(new Intent(con, PlayMusic.class));
	}
	
	private void turnOnTimer()
	{
		layEnd.setVisibility(RelativeLayout.GONE);
		if(myTimer == null){
		myTimer = new Timer();
		myTimer.schedule(new TimerTask() 
		{          
			@Override
		    public void run() 
			{
				TimerMethod();
		    }
		}, 0, 50); 
		}
	}
	
	private void turnOffTimer()
	{
		if(myTimer != null)
		{
			myTimer.cancel();
			myTimer.purge();
			myTimer = null;
		}
	}
	
	private void TimerMethod()
	{
	    this.runOnUiThread(Timer_Tick);
	}
	
	private Runnable Timer_Tick = new Runnable() 
	{
	    public void run() 
	    {   	
	    	//bonus____________________________________________________________
	    	if(System.currentTimeMillis() - bonusTime > lastTic)
	    	{
	    		///czas min¹³
	    		if (aView.bonus == 1)
	    		{
		    		aView.score += (2 * bonScore);
		    		bonScore = 1;
		    		aView.bonus = 0;
		    	}
	    	}
	    	
	    	//pszczo³a_________________________________________________________
	    	if(aView.bee.status > 0)
	    	{
	    		
	    		switch (aView.bee.status) 
        		{
	    			case 1:
	    				aView.bee.status = 2;
	    				aView.lost--;
	    				if (aView.lost == 0)gameOver();
	    	    	break;
        		
        			case 2:
	    				aView.bee.py -= 80;
	    				if (aView.h/2 + aView.bee.py < 0) aView.bee.status = 3;
	    			break;
	    			
	    			case 3:
	    				aView.bee.px = -200;
	    				aView.bee.py = 20;
	    				aView.bee.pyflag = true;
	    				aView.bee.status = 0;
	    			break;
        		}	
	    	}
	    	
	    	//nowe jab³ko______________________________________________________
	    	secTic += 1;
	    	if (secTic > 10)
	    	{
	    		aView.owoce.add(aView.owoc());
	    		//stopService(new Intent(con, PlayBlop.class));
	    		Intent intent = new Intent(con, PlayBlop.class);
	    		con.startService(intent);
	    		
	    		//sekunda
	    		secTic = 0;
	    	}

	    	Iterator<Owoc> oi = aView.owoce.iterator();
	        
	    	while(oi.hasNext())
	        {
	            Owoc o = oi.next();
	            
	            if(o.posY < aView.h - aView.h10+100) //sprawdz koniec planszy
	    		{ 
	            	if (o.status > 0) o.angle += 70;
	            	
	            	if (!o.touched) //jesli nie dotkniêta spada dalej
	            	{
	            		o.posY += o.speed;
	            	}
	            	else //dotkniêta i spada do koszyka
	            	{
	            		//dotkniêty	            		
	            		switch (o.status) 
	            		{
	            			//ustal pozycjê
	            			case 0:
	            				o.goBasket();
				    			o.status = 1;
				    			aView.score ++;
				    		break;
	            			//leci do góry	
	            			case 1:
				    			
	            				o.posY -= (50);
	            				o.posX += (40);
	            				if (o.posY < (o.lposY-100))
	            				{	
	            					o.status = 2;
	            				}
	            			
	            			break;
	            			 //zaczyna leciec w dó³
	            			case 2:
	            				
	            				o.posY += (50);
	            				o.posX += (100);
	            				if (o.posY > (o.lposY))
	            				{	
	            					o.status = 3;
	            				}
	            			
	            			break;
	            			//opadanie do koszyka
	            			case 3:
	            				
	            				o.posY += (50);
	            				o.posX += (20);
	            				
	            				if (o.posY > aView.h - aView.h10)
	            				{	
	            					o.active = false;		            				
					    			o.status = 4;
	            				}
	            				
	            			break;	
	            			
	            			default:
	            				
	            			break;
	            		}
	            	}
	            }	
	    		else //spad³o na ziemie
	    		{
	    			if(o.active & o.status == 0)
	    			{	
	    				//Play LOOSE______________________________________________________
			    		Intent intent = new Intent(con, PlayPac.class);
			    		con.startService(intent);
	    				aView.lost--;
	    				o.active = false;
	    				if (aView.lost == 0) gameOver();
	    			}
	    		}
	        }
	        aView.invalidate();	    	
	    }
	};
	
	
}
