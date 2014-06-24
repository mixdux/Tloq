package com.example.pedagogijadidaktikarad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import com.example.pedagogijadidaktika2.R;

import poslovnaLogika.DatabaseBroker;
import poslovnaLogika.DatabseCreator;
import poslovnaLogika.KolekcijaPitanja;






import domen.Pitanje;
import domen.PitanjeStat;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class UcitajPitanjeAktivnost extends Activity {
	private Button bSacuvaj;
	private Button bNazad;
	private String tac, odg1, odg2, odg3, odg4, mTekstPitanje, razrada;
	final String TAG = "PedagogijaSaDidaktikom";
	private Activity trenutniActivity;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		trenutniActivity = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ucitaj_pitanje_aktivnost);
		final View rootView = (View) findViewById(R.id.container);
		final EditText tvPitanje = (EditText) findViewById(R.id.etTekstPitanja);
		final EditText tvOdg1 = (EditText) findViewById(R.id.etOdgovor1);
		final EditText tvOdg2 = (EditText) findViewById(R.id.etOdgovor2);
		final EditText tvOdg3 = (EditText) findViewById(R.id.etOdgovor3);
		final EditText tvOdg4 = (EditText) findViewById(R.id.etOdgovor4);
		final EditText tvRazrada = (EditText) findViewById(R.id.etRazrada);
		
		rootView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager inputMethodManager = (InputMethodManager)  trenutniActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
			    inputMethodManager.hideSoftInputFromWindow(trenutniActivity.getCurrentFocus().getWindowToken(), 0);
			    skiniFocusSaSvih();
				return false;
			}

			private void skiniFocusSaSvih() {
				rootView.requestFocus();
				tvPitanje.clearFocus();
				tvOdg1.clearFocus();
				tvOdg2.clearFocus();
				tvOdg3.clearFocus();
				tvOdg4.clearFocus();
				tvRazrada.clearFocus();
			}
		});
		
		bSacuvaj = (Button) findViewById(R.id.bSacuvajPitanje);
		bSacuvaj.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				boolean tacno = false;
				
				mTekstPitanje = tvPitanje.getText().toString();
				odg1 = tvOdg1.getText().toString();				
				odg2 = tvOdg2.getText().toString();
				odg3 = tvOdg3.getText().toString();
				odg4 = tvOdg4.getText().toString();
				razrada = tvRazrada.getText().toString();
				tac = "";
				

				RadioGroup rgTacanOdgovor = (RadioGroup) findViewById(R.id.rgTacanOdgovor);
				if (rgTacanOdgovor.getCheckedRadioButtonId() != -1) {
					int idSelektovanog = rgTacanOdgovor.getCheckedRadioButtonId();
					RadioButton selektovanoDugme = (RadioButton) findViewById(idSelektovanog);
					tac = selektovanoDugme.getText().toString();					
					tacno = true;
				} else
					Toast.makeText(getApplicationContext(),
							"Morate obeleziti tacan odgovor", Toast.LENGTH_LONG)
							.show();

				if (!tacno
						|| (mTekstPitanje.equals("") || odg1.equals("")
								|| odg2.equals("") || odg3.equals("") || odg4
									.equals(""))) {
					Toast.makeText(getApplicationContext(),
							"Ni jedno polje ne sme biti prazno",
							Toast.LENGTH_LONG).show();

				} else {
				SacuvajPitanje();
				}

			}

			private void SacuvajPitanje() {
				Intent data = new Intent();
				String[] odgovori = { tac, odg1, odg2, odg3, odg4 };
				Log.e("XYZ",mTekstPitanje
						+ odgovori[0] + odgovori[1]+ odgovori[2]+ odgovori[3]+ odgovori[4]);
				Pitanje pitanje = new Pitanje(mTekstPitanje, odgovori, "ADMIN_M");
				pitanje.setPojasnjenje(razrada);

				
				/*if (!databas.exists()) {
					// Database does not exist so copy it from assets here
					Log.i("Database", "Not Found");
				} else {
					Log.i("Database", "Found");
				}*/
				PitanjeStat novoPitanje = new PitanjeStat(pitanje);
				try {
					DatabaseBroker dbb = new DatabaseBroker(getApplicationContext());
					dbb.dodajPitanje(novoPitanje);
				} catch (Exception ex) {
					Toast.makeText(getApplicationContext(), "Nesto je poslo po zlu sa cuvanjem fajla",
							Toast.LENGTH_LONG).show();
				}
				data.putExtra("myobj", novoPitanje);
				setResult(Activity.RESULT_OK, data);
				finish();
			}

		});

		bNazad = (Button) findViewById(R.id.bNazad);
		bNazad.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Toast.makeText(v.getContext(), "Ovo dugme ne radi nista :)",
						Toast.LENGTH_LONG).show();
			}
		});

	}

}
