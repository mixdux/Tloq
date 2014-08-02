package com.example.pedagogijadidaktika2;

import java.util.Arrays;

import poslovnaLogika.DatabaseBroker;
import poslovnaLogika.Kontroler;

import domen.PitanjeStat;
import domen.SetPitanja;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PromeniSetPitanja extends Activity {
	
	final Activity ovaSama = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_promeni_set_pitanja);
		final EditText imeSeta = (EditText) findViewById(R.id.editImeSeta);
		final EditText doprinosiociSeta = (EditText) findViewById(R.id.editDodajDoprinosioce);
		final TextView autorSeta = (TextView) findViewById(R.id.txtAutorSeta);
		final TextView brojPitanjaTxt = (TextView) findViewById(R.id.txtBrojPitanja);
		
		final SetPitanja setPromena = (SetPitanja) this.getIntent().getExtras().getSerializable("set");
		
		imeSeta.setText(setPromena.getImeSeta());
		doprinosiociSeta.setText(setPromena.getImeDoprinosioca().split(";")[0]);
		autorSeta.setText(setPromena.getImeKreatora());
		int brojPitanja = DatabaseBroker.vratiInstancu().vratiPitanjaZaSet(setPromena.getAUIDseta()).size();
		if (brojPitanja==0){
			brojPitanjaTxt.setText("Set je prazan.");
		} else {
			brojPitanjaTxt.setText("Broj pitanja: "+brojPitanja);
		}
		
		(findViewById(R.id.scrollViewPromeniSet)).setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager inputMethodManager = (InputMethodManager) ovaSama
						.getSystemService(Activity.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(ovaSama
						.getCurrentFocus().getWindowToken(), 0);
				skiniFocusSaSvih();
				return false;
			}
			private void skiniFocusSaSvih() {
				(findViewById(R.id.scrollViewPromeniSet)).requestFocus();
				imeSeta.clearFocus();
				doprinosiociSeta.clearFocus();
			}
		});
		
		((Button) findViewById(R.id.usnimiPromenljeniSet)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String [] doprinosioci = doprinosiociSeta.getText().toString().split(",");
				String [] originalniDopr = setPromena.getImeDoprinosioca().split(";")[0].split(",");
				boolean istiDopr = true;
				if (doprinosioci.length!=originalniDopr.length){
					istiDopr = false;
				}
				String doprString = "";
				for (String dopr : doprinosioci){
					dopr=dopr.trim();
					doprString+=dopr+",";
					for (String dop : originalniDopr){
						if (!dop.trim().equals(dopr)) {
							istiDopr = false;
						}
					}
				}
				if (!imeSeta.getText().toString().equals(setPromena.getImeSeta()) || !istiDopr){
					if (imeSeta.getText().toString().equals(Kontroler.vratiObjekat().getImeGenericSeta())){
						Toast.makeText(getApplicationContext(), "Unesite drugačije ime seta", Toast.LENGTH_SHORT).show();
						return;
					}
					DatabaseBroker.vratiInstancu(getApplicationContext()).promeniSet(setPromena, imeSeta.getText().toString(), doprString.substring(0, doprString.length()-1));
					setResult(RESULT_OK);
					Toast.makeText(getApplicationContext(), "Set uspešno promenjen", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		});	
		
		((Button) findViewById(R.id.nazadIzSeta)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();				
			}
		});	
		
	}
	
	
}
