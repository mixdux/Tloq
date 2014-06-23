package com.example.lockscreentest;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class TestService extends Service{

	BroadcastReceiver mReceiver;
	IntentFilter filter;	
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mReceiver = new ScreenReceiver();
		filter = new IntentFilter(Intent.ACTION_SCREEN_ON); 
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// Pali se za 2.2 i ostale starce
		super.onStart(intent, startId);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("Service", "Pokrenuta je!");
		Toast.makeText(getApplicationContext(), "Pokrenut je servis", Toast.LENGTH_SHORT).show();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);
        
		return android.app.Service.START_STICKY;
		//Toast.makeText(context.getApplicationContext(), "Otkljucan!", Toast.LENGTH_SHORT).show();
	}
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onTaskRemoved(Intent rootIntent) {
		Intent intent = new Intent("lockServiceStopped");
        //intent.putExtra("yourvalue", "torestore");
        sendBroadcast(intent);
		Log.d("Service", "Uklonjena je!");
	}
	
	@Override
	public void onDestroy() {
		Log.d("Service", "Unistena je!");
		mReceiver = null;
		filter = null;
		/*Intent intent = new Intent("lockServiceStopped");
        //intent.putExtra("yourvalue", "torestore");
        sendBroadcast(intent);*/
		super.onDestroy();
	}	
	
}
