package com.example.pedagogijadidaktikarad;

import java.util.List;
import java.util.Random;

import poslovnaLogika.KolekcijaPitanja;
import poslovnaLogika.Kontroler;

import com.example.lockscreentest.TestService;
import com.example.pedagogijadidaktika2.R;

import util.*;
import domen.Pitanje;
import domen.PitanjeStat;
import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PitanjeAktivnost extends Activity {
	TextView tvPrikazPitanja;
	Button bOdgovor1, bOdgovor2, bOdgovor3, bOdgovor4, bpovratak;
	int tacan;
	Pitanje pitanje;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(Konstante.TAG, "Usao u onCreate");
		setContentView(R.layout.activity_pitanje_aktivnost);

		final boolean pozvanIzGlavne = this.getIntent().getExtras()
				.getBoolean(("pozvanIzGlavne"));

		tvPrikazPitanja = (TextView) findViewById(R.id.tvPrikazPitanja);
		bOdgovor1 = (Button) findViewById(R.id.jbOdgovor1);
		bOdgovor2 = (Button) findViewById(R.id.jbOdgovor2);
		bOdgovor3 = (Button) findViewById(R.id.jbOdgovor3);
		bOdgovor4 = (Button) findViewById(R.id.jbOdgovor4);
		bpovratak = (Button) findViewById(R.id.jbPovratak);
		Log.i(Konstante.TAG, "Povezao se sa formom");

		List<PitanjeStat> pitanja = Kontroler.vratiObjekat().getKolekcijaStatPitanja().getPitanja();
		Random rand = new Random();
		Pitanje aktuelnoPitanje = null;
		switch (pitanja.size()) {
		case 0:
			Toast.makeText(this, "Ni jedno pitanje nije odabrano!",
					Toast.LENGTH_LONG).show();
			return;
		case 1:
			aktuelnoPitanje = pitanja.get(0).getPitanje();
			break;
		default:
			int randomPosition = rand.nextInt(pitanja.size());
			aktuelnoPitanje = pitanja.get(randomPosition).getPitanje();
			break;
		}

		tvPrikazPitanja.setText(aktuelnoPitanje.getmTextPitanja().toString());
		bOdgovor1.setText(aktuelnoPitanje.getOdgovori()[1]);
		bOdgovor2.setText(aktuelnoPitanje.getOdgovori()[2]);
		bOdgovor3.setText(aktuelnoPitanje.getOdgovori()[3]);
		bOdgovor4.setText(aktuelnoPitanje.getOdgovori()[4]);
		tacan = Integer.parseInt(aktuelnoPitanje.getOdgovori()[0]);

		final Intent novoPitanje = new Intent(getApplicationContext(),
				PitanjeAktivnost.class);
		novoPitanje.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		novoPitanje.putExtra("pozvanIzGlavne", pozvanIzGlavne);
		
		long now = System.currentTimeMillis();
		
		bOdgovor1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tacan == 1) {
					Toast.makeText(v.getContext(), "Odgovor je tacan",
							Toast.LENGTH_SHORT).show();
					if (pozvanIzGlavne) {
						getApplicationContext().getApplicationContext()
								.startActivity(novoPitanje);
					}
					finish();
				} else {
					Toast.makeText(v.getContext(), "Odgovor je netacan",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		bOdgovor2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tacan == 2) {
					Toast.makeText(v.getContext(), "Odgovor je tacan",
							Toast.LENGTH_SHORT).show();
					if (pozvanIzGlavne) {
						getApplicationContext().getApplicationContext()
								.startActivity(novoPitanje);
					}
					finish();
				} else {
					Toast.makeText(v.getContext(), "Odgovor je netacan",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		bOdgovor3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tacan == 3) {
					Toast.makeText(v.getContext(), "Odgovor je tacan",
							Toast.LENGTH_SHORT).show();
					if (pozvanIzGlavne) {
						getApplicationContext().getApplicationContext()
								.startActivity(novoPitanje);
					}
					finish();
				} else {
					Toast.makeText(v.getContext(), "Odgovor je netacan",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		bOdgovor4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tacan == 4) {
					Toast.makeText(v.getContext(), "Odgovor je tacan",
							Toast.LENGTH_SHORT).show();
					if (pozvanIzGlavne) {
						getApplicationContext().getApplicationContext()
								.startActivity(novoPitanje);
					}
					finish();
				} else {
					Toast.makeText(v.getContext(), "Odgovor je netacan",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		if (pozvanIzGlavne) {
			bpovratak.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setResult(Activity.RESULT_OK);
					finish();
				}
			});
		} else {
			bpovratak.setVisibility(View.INVISIBLE);
		}

	}
}
