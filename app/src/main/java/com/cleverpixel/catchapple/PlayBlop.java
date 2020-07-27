package com.cleverpixel.catchapple;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;


public class PlayBlop extends Service 
{

	MediaPlayer objPlayer;

	public void onCreate()
	{
	    super.onCreate();
	    objPlayer = MediaPlayer.create(this, R.raw.blop);
	}

	public int onStartCommand(Intent intent, int flags, int startId)
	{
		objPlayer.setVolume(.05F, .05F);
		objPlayer.setLooping(false);
		objPlayer.start();
		return START_NOT_STICKY;
	}

	public void onStop()
	{
		objPlayer.stop();
		objPlayer.release();
	}
	
	public void onPause()
	{
		objPlayer.stop();
		objPlayer.release();
	}
	
	public void onDestroy()
	{
		objPlayer.stop();
		objPlayer.release();
	}
	
	@Override
	public IBinder onBind(Intent objIndent) 
	{
	    return null;
	}
	
}

