package com.example.lockscreentest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import poslovnaLogika.KolekcijaPitanja;
import poslovnaLogika.Kontroler;

import com.example.pedagogijadidaktika2.R;
import com.example.pedagogijadidaktikarad.GlavnaAktivnost;
import com.example.pedagogijadidaktikarad.UcitajPitanjeAktivnost;

import domen.Pitanje;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public final String TAG = "PedagogijaSaDidaktikom";
	private KolekcijaPitanja kp;
	private Pitanje pitanje;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		startService(new Intent(this, TestService.class));
		Toast.makeText(this, "Pozvan servis", Toast.LENGTH_SHORT).show();
		
		try {
			//zapocinje ucitavanje kolekcije pitanja iz fajla, ukoliko fajl ne postoji
			//vraca null vrednost i ceka da se zavrsi ucitavanje prvog pitanja
			kp = UcitajPitanja();
			if (kp!=null) {
				Kontroler.vratiObjekat().UcitajPitanja(kp);	
			}
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}		
		
	}

	public void SacuvajPitanja(KolekcijaPitanja kp) throws Exception {

		try {
			//TO DO ako ne postoji memorija eksterna, uzmi interni
			File file = new File(Environment.getExternalStorageDirectory(), GlavnaAktivnost.FILENAME);
			Log.i(TAG, "Napravljen fajl");
			if (!file.exists()) {
				Log.i(TAG, "Fajl ne postoji na fajl sistemu");
				file.createNewFile() ;
			}
			FileOutputStream fileout = new FileOutputStream(file);
			Log.i(TAG, "fileOut napravljen");
			ObjectOutputStream out = new ObjectOutputStream(fileout);
			Log.i(TAG, "Out napravljen");
			out.writeObject(kp);
			Log.i(TAG, "Zapoceto ucitavanje");
			out.close();
			Log.i(TAG, "Zavrseno ucitavanje");
			Toast.makeText(this, "Fajl sacuvan", Toast.LENGTH_LONG).show();			
		} catch (Exception ex) {
			throw new Exception("Nesto je poslo po zlu sa cuvanjem fajla");
		}

	}
	
	public KolekcijaPitanja UcitajPitanja() throws Exception{
		try {
			File file = new File(Environment.getExternalStorageDirectory(), GlavnaAktivnost.FILENAME);
			//Pita da li fajl postoji i ukoliko postoji ucitava pitanja iz njega. Ukoliko ne postoji fajl, 
			//poziva aktivnost ucitaj pitanje koja vraca pitanje.
			if (file.exists()) {
				FileInputStream fileIn = new FileInputStream(file);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				KolekcijaPitanja kp = (KolekcijaPitanja) in.readObject();
				in.close();
				return kp;
			} Toast.makeText(this, "Ne postoji fajl", Toast.LENGTH_SHORT).show();
			
			Intent i = new Intent(this, UcitajPitanjeAktivnost.class);
			startActivityForResult(i, 1);
			
			return null;
			
		} catch (Exception ex) {
			throw new Exception("Nesto je poslo po zlu sa ucitavanjem fajla");
		}
	}
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	
    if (requestCode == 1) {
        if(resultCode == RESULT_OK){
        	pitanje = (Pitanje)data.getSerializableExtra("myobj");
			kp = new KolekcijaPitanja();
			kp.DodajPitanje(pitanje);
        	Kontroler.vratiObjekat().UcitajPitanja(kp);  
        	try {
				SacuvajPitanja(kp);
			} catch (Exception e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
				finish();
			}
        }
        if (resultCode == RESULT_CANCELED) {
            //Write your code if there's no result
        }
    }
}
}

