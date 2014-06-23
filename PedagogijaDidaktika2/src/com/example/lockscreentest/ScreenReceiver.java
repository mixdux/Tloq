package com.example.lockscreentest;

import java.io.Serializable;

import com.example.pedagogijadidaktikarad.GlavnaAktivnost;
import com.example.pedagogijadidaktikarad.PitanjeAktivnost;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ScreenReceiver extends BroadcastReceiver implements Serializable {

	private static final long serialVersionUID = 1L;
	public static boolean wasScreenOn;
	
	public ScreenReceiver() {
		wasScreenOn = true;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            wasScreenOn = false;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
        	Intent lockEkran = new Intent(context, PitanjeAktivnost.class);
        	//Toast.makeText(context.getApplicationContext(), "Otkljucan!", Toast.LENGTH_SHORT).show();
        	lockEkran.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        	lockEkran.putExtra("pozvanIzGlavne", false);
        	context.getApplicationContext().startActivity(lockEkran);
            wasScreenOn = true;
        }
    }


}
