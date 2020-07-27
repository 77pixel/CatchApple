package com.cleverpixel.catchapple;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;


public class PlayMusic extends Service 
{

	MediaPlayer objPlayer;

	public void onCreate()
	{
	    super.onCreate();
	    objPlayer = MediaPlayer.create(this, R.raw.sound1);
	}

	public int onStartCommand(Intent intent, int flags, int startId)
	{
		objPlayer.setLooping(true);
		objPlayer.start();
		objPlayer.setVolume(1, 1);
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

