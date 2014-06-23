package com.example.lockscreentest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UnstoppableReciever extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d("Service", "Primljen signal!");
		context.startService(new Intent(context, TestService.class));
	}

}
