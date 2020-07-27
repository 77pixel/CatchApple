package com.cleverpixel.catchapple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class MainMenu extends Activity 
{

	private ImageButton bt1;
	private ImageButton bt2;
	
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_menu);
	
        bt1 = (ImageButton) findViewById(R.id.bt1);
        bt2 = (ImageButton) findViewById(R.id.bt2);
        
        
        bt1.setOnClickListener(new View.OnClickListener() 
	    {
            @Override
            public void onClick(View v) 
            {
            	 Intent intent = new Intent( MainMenu.this, Level1Activity.class);
            	 MainMenu.this.startActivity(intent);
            }
	    }); 
     
        bt2.setOnClickListener(new View.OnClickListener() 
	    {
            @Override
            public void onClick(View v) 
            {
            	 Intent intent = new Intent( MainMenu.this, Level2Activity.class);
            	 MainMenu.this.startActivity(intent);
            }
	    });       
        
        
    }
	
	
	
	
	
}
