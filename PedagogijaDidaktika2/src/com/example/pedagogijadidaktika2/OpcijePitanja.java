package com.example.pedagogijadidaktika2;

import java.util.ArrayList;
import java.util.List;

import poslovnaLogika.DatabaseBroker;
import poslovnaLogika.Kontroler;
import domen.PitanjeStat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Build;

public class OpcijePitanja extends Activity {

	AdapterListe adapter;
	ListView lista;
	final List<PitanjeStat> selektovanaPitanja = new ArrayList<PitanjeStat>();
	Activity act = this;

	boolean longHoldShield = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opcije_pitanja);

		final List<PitanjeStat> svaPitanjaUBazi = new DatabaseBroker(this)
				.vratiSvaPitanja(false);
		Button obrisi = (Button) findViewById(R.id.btnObrisi);
		Button resetuj = (Button) findViewById(R.id.btnResetujStatistiku);
		lista = (ListView) this.findViewById(R.id.listaPitanjaOpcije);
		adapter = new AdapterListe(this, svaPitanjaUBazi, true);
		lista.setAdapter(adapter);
		final Vibrator vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);

		lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				CheckBox cb = (CheckBox) v.findViewById(R.id.checkBox);
				boolean isChecked = cb.isChecked();
				if (!longHoldShield) {
					vibrator.vibrate(50);
					PitanjeStat pit = svaPitanjaUBazi.get(position);
					if (isChecked) {
						cb.setChecked(false);
						selektovanaPitanja.remove(pit);
					} else {
						cb.setChecked(true);
						selektovanaPitanja.add(pit);
					}
				} else {
					longHoldShield = false;
				}
			}
		});

		lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			// http://stackoverflow.com/questions/12244297/how-to-add-multiple-buttons-on-a-single-alertdialog
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				vibrator.vibrate(150);
				prikaziPromeniDijalog();
				longHoldShield = true;
				return false;
			}

		});
		
		obrisi.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (selektovanaPitanja.size()==0){
					Toast.makeText(getApplicationContext(), "Molim odaberite bar jedno pitanje", Toast.LENGTH_SHORT).show();
				} else {
					prikaziObrisiDijalog();
				}
			}
		});
		
		resetuj.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (selektovanaPitanja.size()==0){
					Toast.makeText(getApplicationContext(), "Molim odaberite bar jedno pitanje", Toast.LENGTH_SHORT).show();
				} else {
					prikaziResetujDijalog();
				}
			}
		});

	}

	private void prikaziPromeniDijalog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Da li �elite da izmenite pitanje?")
				.setCancelable(false)
				.setPositiveButton("Da", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Toast.makeText(getApplicationContext(), "Promeni", Toast.LENGTH_SHORT).show();
					}
				});
		AlertDialog alert = builder.create();
		alert.setButton(android.content.DialogInterface.BUTTON_NEGATIVE, "Ne", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Toast.makeText(getApplicationContext(), "Nazad", Toast.LENGTH_SHORT).show();
					}
				});		

		alert.show();
	}
	
	private void prikaziResetujDijalog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String poruka = "Da li ste sigurni da �elite resetovati statistiku odabranih pitanja?";
		if (selektovanaPitanja.size()==1){
			poruka = "Da li ste sigurni da �elite resetovati statistiku odabranog pitanja?";
		}
		builder.setMessage(poruka)
				.setCancelable(false)
				.setPositiveButton("Da", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Toast.makeText(getApplicationContext(), "Resetuj", Toast.LENGTH_SHORT).show();
					}
				});
		AlertDialog alert = builder.create();
		alert.setButton(android.content.DialogInterface.BUTTON_NEGATIVE, "Ne", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Toast.makeText(getApplicationContext(), "Ne resetuj", Toast.LENGTH_SHORT).show();
					}
				});		

		alert.show();
	}
	
	private void prikaziObrisiDijalog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String poruka = "Da li ste sigurni da �elite potpuno obrisati odabrana pitanja?";
		final boolean visePitanja;
		if (selektovanaPitanja.size()==1){
			poruka = "Da li ste sigurni da �elite potpuno obrisati odabrano pitanje?";
			visePitanja=false;
		} else {
			visePitanja = true;
		}
		builder.setMessage(poruka)
				.setCancelable(false)
				.setPositiveButton("Da", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						DatabaseBroker dbb = new DatabaseBroker(getApplicationContext());
						for (PitanjeStat pitanje : selektovanaPitanja){
							dbb.obrisiPitanje(pitanje.getPitanje().getJedinstveniIDikada());
							selektovanaPitanja.remove(pitanje);
						}
						adapter = new AdapterListe(act, dbb.vratiSvaPitanja(false), true);
						lista.setAdapter(adapter);
						if (visePitanja){
							Toast.makeText(getApplicationContext(), "Pitanja su uspe�no obrisana!", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(), "Pitanje je uspe�no obrisano!", Toast.LENGTH_SHORT).show();
						}
					}
				});
		AlertDialog alert = builder.create();
		alert.setButton(android.content.DialogInterface.BUTTON_NEGATIVE, "Ne", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Toast.makeText(getApplicationContext(), "Ne bri�i", Toast.LENGTH_SHORT).show();
					}
				});		

		alert.show();
	}


}
