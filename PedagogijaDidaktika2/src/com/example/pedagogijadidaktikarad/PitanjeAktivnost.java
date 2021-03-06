package com.example.pedagogijadidaktikarad;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import poslovnaLogika.DatabaseBroker;
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
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
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

		final boolean pozvanIzGlavne = this.getIntent().getExtras().getBoolean(("pozvanIzGlavne"));

		tvPrikazPitanja = (TextView) findViewById(R.id.tvPrikazPitanja);
		bOdgovor1 = (Button) findViewById(R.id.jbOdgovor1);
		bOdgovor2 = (Button) findViewById(R.id.jbOdgovor2);
		bOdgovor3 = (Button) findViewById(R.id.jbOdgovor3);
		bOdgovor4 = (Button) findViewById(R.id.jbOdgovor4);
		bpovratak = (Button) findViewById(R.id.jbPovratak);
		Log.i(Konstante.TAG, "Povezao se sa formom");

		List<PitanjeStat> pitanja = DatabaseBroker.vratiInstancu(this).vratiSvaPitanja(true);

		final List<PitanjeStat> statPitanja = new ArrayList<PitanjeStat>();
		Pitanje aktuelnoPitanje = null;
		switch (pitanja.size()) {
		case 0:
			Toast.makeText(this, "Ni jedno pitanje nije odabrano!",
					Toast.LENGTH_LONG).show();
			this.finish();
			return;
		case 1:
			aktuelnoPitanje = pitanja.get(0).getPitanje();
			statPitanja.add(pitanja.get(0));
			break;
		default:
			PitanjeStat statPitanje = dajRandomPitanje(pitanja);
			aktuelnoPitanje = statPitanje.getPitanje();
			statPitanja.add(statPitanje);
			break;
		}
		
		final Vibrator vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
		
		String tacanOdgovor = aktuelnoPitanje.getOdgovori()[Integer.parseInt(aktuelnoPitanje.getOdgovori()[0])];
		List<String> odgovori = new ArrayList<String>(Arrays.asList(aktuelnoPitanje.getOdgovori()));
		odgovori.remove(0);
		Collections.shuffle(odgovori);
		 
		tvPrikazPitanja.setText(aktuelnoPitanje.getmTextPitanja().toString());
		bOdgovor1.setText(odgovori.get(0));
		bOdgovor2.setText(odgovori.get(1));
		bOdgovor3.setText(odgovori.get(2));
		bOdgovor4.setText(odgovori.get(3));
		tacan = odgovori.indexOf(tacanOdgovor)+1;
		
		//podesiVisinu();
		
		final int vremeUcitavanja = (int) System.currentTimeMillis();

		final String auid = aktuelnoPitanje.getJedinstveniIDikada();

		final Intent novoPitanje = new Intent(getApplicationContext(),
				PitanjeAktivnost.class);
		novoPitanje.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		novoPitanje.putExtra("pozvanIzGlavne", pozvanIzGlavne);

		final List<Integer> pushedAnswers = new ArrayList<Integer>();

		bOdgovor1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tacan == 1) {
					int vremeOdgovora = (int) System.currentTimeMillis();
					DatabaseBroker.vratiInstancu().updateVremeZaOdgovor(vremeOdgovora-vremeUcitavanja, auid);
					Toast.makeText(v.getContext(), "Odgovor je tacan",
							Toast.LENGTH_SHORT).show();
					if (pozvanIzGlavne) {
						getApplicationContext().getApplicationContext()
								.startActivity(novoPitanje);
					}
					zavibriraj(true, vibrator);
					DatabaseBroker.vratiInstancu().updateOdgovor(true, auid);
					statPitanja.get(0).setBrojTacnihOdgovora(
							statPitanja.get(0).getBrojTacnihOdgovora() + 1);
					finish();
				} else {
					zavibriraj(false, vibrator);
					if (!pushedAnswers.contains(1)) {
						DatabaseBroker.vratiInstancu().updateOdgovor(false, auid);
						statPitanja.get(0)
								.setBrojNetacnihOdgovora(
										statPitanja.get(0)
												.getBrojNetacnihOdgovora() + 1);
						pushedAnswers.add(1);
					}
					Toast.makeText(v.getContext(), "Odgovor je netacan",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		bOdgovor2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tacan == 2) {
					int vremeOdgovora = (int) System.currentTimeMillis();
					DatabaseBroker.vratiInstancu().updateVremeZaOdgovor(vremeOdgovora-vremeUcitavanja, auid);
					Toast.makeText(v.getContext(), "Odgovor je tacan",
							Toast.LENGTH_SHORT).show();
					if (pozvanIzGlavne) {
						getApplicationContext().getApplicationContext()
								.startActivity(novoPitanje);
					}
					zavibriraj(true, vibrator);
					DatabaseBroker.vratiInstancu().updateOdgovor(true, auid);
					statPitanja.get(0).setBrojTacnihOdgovora(
							statPitanja.get(0).getBrojTacnihOdgovora() + 1);
					finish();
				} else {
					zavibriraj(false, vibrator);
					if (!pushedAnswers.contains(2)) {
						DatabaseBroker.vratiInstancu().updateOdgovor(false, auid);
						statPitanja.get(0)
								.setBrojNetacnihOdgovora(
										statPitanja.get(0)
												.getBrojNetacnihOdgovora() + 1);
						pushedAnswers.add(2);
					}
					Toast.makeText(v.getContext(), "Odgovor je netacan",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		bOdgovor3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tacan == 3) {
					int vremeOdgovora = (int) System.currentTimeMillis();
					DatabaseBroker.vratiInstancu().updateVremeZaOdgovor(vremeOdgovora-vremeUcitavanja, auid);
					Toast.makeText(v.getContext(), "Odgovor je tacan",
							Toast.LENGTH_SHORT).show();
					if (pozvanIzGlavne) {
						getApplicationContext().getApplicationContext()
								.startActivity(novoPitanje);
					}
					zavibriraj(true, vibrator);
					DatabaseBroker.vratiInstancu().updateOdgovor(true, auid);
					statPitanja.get(0).setBrojTacnihOdgovora(
							statPitanja.get(0).getBrojTacnihOdgovora() + 1);
					finish();
				} else {
					zavibriraj(false, vibrator);
					if (!pushedAnswers.contains(3)) {
						DatabaseBroker.vratiInstancu().updateOdgovor(false, auid);
						statPitanja.get(0)
								.setBrojNetacnihOdgovora(
										statPitanja.get(0)
												.getBrojNetacnihOdgovora() + 1);
						pushedAnswers.add(3);
					}
					Toast.makeText(v.getContext(), "Odgovor je netacan",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		bOdgovor4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tacan == 4) {
					int vremeOdgovora = (int) System.currentTimeMillis();
					DatabaseBroker.vratiInstancu().updateVremeZaOdgovor(vremeOdgovora-vremeUcitavanja, auid);
					Toast.makeText(v.getContext(), "Odgovor je tacan",
							Toast.LENGTH_SHORT).show();
					if (pozvanIzGlavne) {
						getApplicationContext().getApplicationContext()
								.startActivity(novoPitanje);
					}
					zavibriraj(true, vibrator);
					DatabaseBroker.vratiInstancu().updateOdgovor(true, auid);
					statPitanja.get(0).setBrojTacnihOdgovora(
							statPitanja.get(0).getBrojTacnihOdgovora() + 1);
					finish();
				} else {
					zavibriraj(false, vibrator);
					if (!pushedAnswers.contains(4)) {
						DatabaseBroker.vratiInstancu().updateOdgovor(false, auid);
						statPitanja.get(0)
								.setBrojNetacnihOdgovora(
										statPitanja.get(0)
												.getBrojNetacnihOdgovora() + 1);
						pushedAnswers.add(4);
					}
					Toast.makeText(v.getContext(), "Odgovor je netacan",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		if (pozvanIzGlavne) {
			bpovratak.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					DatabaseBroker.vratiInstancu().getDatabase().close();
					finish();
				}
			});
		} else {
			bpovratak.setVisibility(View.INVISIBLE);
		}

	}
	 

	private PitanjeStat dajRandomPitanje(List<PitanjeStat> pitanja) {
		while (true) {
			Random rand = new Random();
			int randomPosition = rand.nextInt(pitanja.size());
			PitanjeStat kandidat = pitanja.get(randomPosition);
			int[] odgovori = new int[] { kandidat.getBrojTacnihOdgovora(),
					kandidat.getBrojNetacnihOdgovora() };
			if (odgovori[0] == 0 && odgovori[1] == 0) {
				if (Math.random() > 0.5d)
					return kandidat;
			} else if (odgovori[0] == 0){
				if (Math.random() > 0.2d)
					return kandidat;
			} else {
				double sansaZaNEDolazenjem = (double) odgovori[0]
						/ (odgovori[0] + odgovori[1]);
				double pogodak = Math.random();
				if ((sansaZaNEDolazenjem - pogodak) <= 0)
					return kandidat;
			}
		}
	}
	
	private void zavibriraj(boolean tacno, Vibrator vibrator){
		if (tacno){
			vibrator.vibrate(400);
		} else {
			vibrator.vibrate(new long[]{0,100,50,100,50,100}, -1);
		}
	}
	
	/*private void podesiVisinu() {
		LinearLayout layoutGornji = (LinearLayout) findViewById(R.id.linearGornji);
		LinearLayout layoutDonji = (LinearLayout) findViewById(R.id.linearDonji);
		
		RelativeLayout.LayoutParams paramsG =  (RelativeLayout.LayoutParams) layoutGornji.getLayoutParams();
		RelativeLayout.LayoutParams paramsD = (RelativeLayout.LayoutParams) layoutDonji.getLayoutParams();
		
		int razlika = paramsD.height-paramsG.height;
		if(razlika>0){
			paramsG.height=paramsD.height;
		} else {
			paramsD.height=paramsG.height;
		}
	}*/

}
