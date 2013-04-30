package com.example.basicphotobrowser;


import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

public class ImageServerService extends Service {

	@Override
	public void onCreate(){

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int id){
		Toast.makeText(getBaseContext(), "Starting Service...", Toast.LENGTH_SHORT).show();
    	LoopingForEver loop = new LoopingForEver(this, 3000);
    	if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
			  loop.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
			else {
			  loop.execute();
			}
		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}
	
	
}

