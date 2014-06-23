package com.example.pedagogijadidaktikarad;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.example.lockscreentest.TestService;
import com.example.pedagogijadidaktika2.PregledPitanja;
import com.example.pedagogijadidaktika2.R;

import util.*;
import domen.Pitanje;
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
			if (ksp != null) {
				Kontroler.vratiObjekat().UcitajStatPitanja(ksp);
				Toast.makeText(this, "Pitanja uspešno učitana iz fajla",
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
				startActivityForResult(ucitajPitanje, 1);

			}
		});

		bOdgovarajNaPitanja.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Kontroler.vratiObjekat().getKolekcijaPitanja()
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
				if (Kontroler.vratiObjekat().getKolekcijaPitanja()
						.BrojPitanja() == 0) {
					Toast.makeText(getApplicationContext(),
							"Pitanja nisu učitana, molimo učitajte.",
							Toast.LENGTH_SHORT).show();
					return;
				}
				Intent listaPitanja = new Intent(v.getContext(),
						PregledPitanja.class);
				startActivityForResult(listaPitanja, 1);
			}
		});
	}

	public KolekcijaStatPitanja UcitajPitanja() throws Exception {
		try {
			DatabaseBroker dbb = new DatabaseBroker(this);
			return new KolekcijaStatPitanja(dbb.vratiSvaPitanja());
		} catch (Exception ex) {
			throw new Exception("Nesto je poslo po zlu sa ucitavanjem fajla");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Pitanje pitanje = (Pitanje) data.getSerializableExtra("myobj");
				Kontroler.vratiObjekat().dodajPitanje(pitanje);
			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}
		if (requestCode == 2) {
			Toast.makeText(this, "Sesija odgovaranja uspešno završena",
					Toast.LENGTH_SHORT).show();
		}
	}
}
