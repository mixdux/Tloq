package com.example.pedagogijadidaktikarad;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.example.lockscreentest.TestService;
import com.example.pedagogijadidaktika2.AdapterListe;
import com.example.pedagogijadidaktika2.PregledPitanja;
import com.example.pedagogijadidaktika2.R;

import util.*;
import domen.Pitanje;
import domen.PitanjeStat;
import poslovnaLogika.DatabaseBroker;
import poslovnaLogika.KolekcijaPitanja;
import poslovnaLogika.KolekcijaStatPitanja;
import poslovnaLogika.Kontroler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class GlavnaAktivnost extends Activity {

	public Button bSnimiNovoPitanje;
	public Button bOdgovarajNaPitanja;
	public Button bKaListiPitanja;
	public static final String FILENAME = "SaveFileForMyApp.bin";
	private KolekcijaStatPitanja ksp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_glavna_aktivnost);
		Kontroler.vratiObjekat().setAktivniKorisnik("ADMIN<dsc>_M</dsc>");
		
		startService(new Intent(this, TestService.class));
		Toast.makeText(this, "Pozvan servis", Toast.LENGTH_SHORT).show();

		bOdgovarajNaPitanja = (Button) findViewById(R.id.jbOdgovor3);
		bSnimiNovoPitanje = (Button) findViewById(R.id.jbOdgovor4);
		bKaListiPitanja = (Button) findViewById(R.id.jbListaPitanja);

		try {
			// zapocinje ucitavanje kolekcije pitanja iz fajla, ukoliko fajl ne
			// postoji
			// vraca null vrednost i ceka da se zavrsi ucitavanje prvog pitanja
			ksp = UcitajPitanja();
			if (ksp.BrojPitanja()!=0) {
				Kontroler.vratiObjekat().UcitajStatPitanja(ksp);
				Toast.makeText(this, "Pitanja uspešno učitana iz baze",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Pitanja nisu učitana iz baze",
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}

		bSnimiNovoPitanje.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent ucitajPitanje = new Intent(v.getContext(),
						UcitajPitanjeAktivnost.class);
				ucitajPitanje.putExtra("promeni", false);
				startActivityForResult(ucitajPitanje, 1);

			}
		});

		bOdgovarajNaPitanja.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Kontroler.vratiObjekat().getKolekcijaStatPitanja()
						.BrojPitanja() == 0) {
					Toast.makeText(getApplicationContext(),
							"Pitanja nisu učitana, molimo učitajte.",
							Toast.LENGTH_SHORT).show();
					return;
				}
				Log.i(Konstante.TAG, "Dugme stisnuto");
				Intent odgovarajPitanje = new Intent(v.getContext(),
						PitanjeAktivnost.class);
				odgovarajPitanje.putExtra("pozvanIzGlavne", true);
				/*
				 * Log.i(Konstante.TAG, "Intent napravljen");
				 * odgovarajPitanje.putExtra("myobj", Kontroler.vratiObjekat()
				 * .vratiPitanje()); Log.i(Konstante.TAG, "Pitanje dodato");
				 */
				startActivityForResult(odgovarajPitanje, 2);
				/* Log.i(Konstante.TAG, "pozvan activiti za rezultat"); */
			}
		});
		
		bKaListiPitanja.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DatabaseBroker dbb = new DatabaseBroker(getApplicationContext());
				List<PitanjeStat> pist = dbb.vratiSvaPitanja(false);
				String imeOpstegSeta = "Opšta pitanja";
//Optimizuj kad se stvara generic set
				if (!dbb.daLiPostojiSetSaImenom(imeOpstegSeta)){
					dbb.dodajGenericSetPitanja(imeOpstegSeta);
				}
				if (pist.size() == 0) {
					Toast.makeText(getApplicationContext(),
							"Ne postoje pitanja u bazi, molimo učitajte.",
							Toast.LENGTH_SHORT).show();
					return;
				}
				Intent listaPitanja = new Intent(v.getContext(),
						PregledPitanja.class);
				startActivityForResult(listaPitanja, 3);
			}
		});
	}

	public KolekcijaStatPitanja UcitajPitanja() throws Exception {
		try {
			DatabaseBroker dbb = new DatabaseBroker(this);
			return new KolekcijaStatPitanja(dbb.vratiSvaPitanja(true));
		} catch (Exception ex) {
			throw new Exception("Nesto je poslo po zlu sa čitanjem iz baze");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode){
		case 1:
			if (resultCode == RESULT_OK) {
				PitanjeStat pitanje = (PitanjeStat) data.getSerializableExtra("myobj");
				Kontroler.vratiObjekat().dodajStatPitanje(pitanje);
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		break;

		case 2:
			Toast.makeText(this, "Sesija odgovaranja uspešno završena",
					Toast.LENGTH_SHORT).show();
		break;
		
		case 3:
			//Kontroler.vratiObjekat().UcitajStatPitanja(new KolekcijaStatPitanja(new DatabaseBroker(getApplicationContext()).vratiSvaPitanja(true)));
		break;
		}
	}
	
}
