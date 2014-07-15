package com.example.pedagogijadidaktikarad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.UUID;

import com.example.pedagogijadidaktika2.R;

import poslovnaLogika.DatabaseBroker;
import poslovnaLogika.Kontroler;
import domen.Pitanje;
import domen.PitanjeStat;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

		final boolean promeniPitanje = this.getIntent().getExtras()
				.getBoolean(("promeni"));
		final List<PitanjeStat> pitanjeKontejner = new ArrayList<PitanjeStat>();
		if (promeniPitanje) {
			PitanjeStat pitanjePromena = (PitanjeStat) this.getIntent()
					.getExtras().getSerializable("pitanje");
			pitanjeKontejner.add(pitanjePromena);
			popuniPromeni(pitanjePromena);
		}

		rootView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager inputMethodManager = (InputMethodManager) trenutniActivity
						.getSystemService(Activity.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(trenutniActivity
						.getCurrentFocus().getWindowToken(), 0);
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
					int idSelektovanog = rgTacanOdgovor
							.getCheckedRadioButtonId();
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
					if (promeniPitanje) {
						PitanjeStat pist = proveriPromene(pitanjeKontejner.get(0));
						if(pist!=null){
						if ((new DatabaseBroker(getApplicationContext())).promeniPitanje(pist));
						Toast.makeText(getApplicationContext(), "Pitanje je uspešno promenjeno!",
								Toast.LENGTH_SHORT).show();
						finish();
						}
						else {
							Toast.makeText(getApplicationContext(), "Sve je isto!",
									Toast.LENGTH_SHORT).show();
						}
					} else {
						SacuvajPitanje();
					}
				}

			}

			private void SacuvajPitanje() {
				Intent data = new Intent();
				String[] odgovori = { tac, odg1, odg2, odg3, odg4 };
				Log.e("XYZ", mTekstPitanje + odgovori[0] + odgovori[1]
						+ odgovori[2] + odgovori[3] + odgovori[4]);
				String kreator = Kontroler.vratiObjekat().getAktivniKorisnik();
				String auid = napraviAUID();
				Pitanje pitanje = new Pitanje(mTekstPitanje, odgovori, kreator,
						auid);
				pitanje.setPojasnjenje(razrada);

				/*
				 * if (!databas.exists()) { // Database does not exist so copy
				 * it from assets here Log.i("Database", "Not Found"); } else {
				 * Log.i("Database", "Found"); }
				 */
				PitanjeStat novoPitanje = new PitanjeStat(pitanje);
				try {
					DatabaseBroker dbb = new DatabaseBroker(
							getApplicationContext());
					dbb.dodajPitanje(novoPitanje);
				} catch (Exception ex) {
					Toast.makeText(getApplicationContext(),
							"Nesto je poslo po zlu sa cuvanjem fajla",
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

	private void popuniPromeni(PitanjeStat pitanjePromena) {
		((EditText) findViewById(R.id.etTekstPitanja)).setText(pitanjePromena
				.getPitanje().getmTextPitanja());
		((EditText) findViewById(R.id.etOdgovor1)).setText(pitanjePromena
				.getPitanje().getOdgovori()[1]);
		((EditText) findViewById(R.id.etOdgovor2)).setText(pitanjePromena
				.getPitanje().getOdgovori()[2]);
		((EditText) findViewById(R.id.etOdgovor3)).setText(pitanjePromena
				.getPitanje().getOdgovori()[3]);
		((EditText) findViewById(R.id.etOdgovor4)).setText(pitanjePromena
				.getPitanje().getOdgovori()[4]);
		((EditText) findViewById(R.id.etRazrada)).setText(pitanjePromena
				.getPitanje().getPojasnjenje());
		switch (Integer.parseInt(pitanjePromena.getPitanje().getOdgovori()[0])) {
		case 1:
			((RadioButton) findViewById(R.id.radioButton1)).setChecked(true);
			break;
		case 2:
			((RadioButton) findViewById(R.id.radioButton2)).setChecked(true);
			break;
		case 3:
			((RadioButton) findViewById(R.id.RadioButton3)).setChecked(true);
			break;
		case 4:
			((RadioButton) findViewById(R.id.RadioButton4)).setChecked(true);
			break;
		}
	}

	private PitanjeStat proveriPromene(PitanjeStat pitanjeStaro) {
		int idSelektovanog = ((RadioGroup) findViewById(R.id.rgTacanOdgovor)).getCheckedRadioButtonId();
		RadioButton selektovanoDugme = (RadioButton) findViewById(idSelektovanog);
		String selText = selektovanoDugme.getText().toString();
		if (((EditText) findViewById(R.id.etTekstPitanja)).getText().toString().equals(pitanjeStaro.getPitanje().getmTextPitanja())
				&& ((EditText) findViewById(R.id.etOdgovor1)).getText().toString().equals(pitanjeStaro.getPitanje().getOdgovori()[1])
				&& ((EditText) findViewById(R.id.etOdgovor2)).getText().toString().equals(pitanjeStaro.getPitanje().getOdgovori()[2])
				&& ((EditText) findViewById(R.id.etOdgovor3)).getText().toString().equals(pitanjeStaro.getPitanje().getOdgovori()[3])
				&& ((EditText) findViewById(R.id.etOdgovor4)).getText().toString().equals(pitanjeStaro.getPitanje().getOdgovori()[4])
				&& ((EditText) findViewById(R.id.etRazrada)).getText().toString().equals(pitanjeStaro.getPitanje().getPojasnjenje())){
				if (selText.equals(pitanjeStaro.getPitanje().getOdgovori()[0])){
					return null;
				}
		}
		String[] odg = new String[]{selText,
				((EditText) findViewById(R.id.etOdgovor1)).getText().toString(),
				((EditText) findViewById(R.id.etOdgovor2)).getText().toString(),
				((EditText) findViewById(R.id.etOdgovor3)).getText().toString(),
				((EditText) findViewById(R.id.etOdgovor4)).getText().toString()};
		String aktivniKorisnik = Kontroler.vratiObjekat().getAktivniKorisnik();
		PitanjeStat pitStat = new PitanjeStat(new Pitanje(((EditText) findViewById(R.id.etTekstPitanja)).getText().toString(), odg, aktivniKorisnik, pitanjeStaro.getPitanje().getJedinstveniIDikada()));
		return pitStat;
	}

	private String napraviAUID() {
		String auid = "";
		auid += new SimpleDateFormat("ddMMyyyy").format(new Date()) + "-";
		auid += java.util.UUID.randomUUID();
		return auid;

	}

}
