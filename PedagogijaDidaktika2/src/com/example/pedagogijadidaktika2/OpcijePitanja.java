package com.example.pedagogijadidaktika2;

import java.util.ArrayList;
import java.util.List;

import com.example.pedagogijadidaktikarad.UcitajPitanjeAktivnost;

import poslovnaLogika.DatabaseBroker;
import poslovnaLogika.Kontroler;
import domen.PitanjeStat;
import android.support.v4.app.Fragment;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
	
	//UPDATE PITANjA U KONTROLERU

	AdapterListe adapter;
	ListView lista;
	final List<PitanjeStat> selektovanaPitanja = new ArrayList<PitanjeStat>();
	final List<PitanjeStat> svaPitanjaUBazi = new ArrayList<PitanjeStat>();
	Activity act = this;

	boolean longHoldShield = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opcije_pitanja);
		svaPitanjaUBazi.addAll(DatabaseBroker.vratiInstancu(this).vratiSvaPitanja(false));
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
					int position, long arg3) {
				vibrator.vibrate(150);
				prikaziPromeniDijalog(svaPitanjaUBazi.get(position));
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

	private void prikaziPromeniDijalog(PitanjeStat pitanje) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final PitanjeStat pit = pitanje;
		builder.setMessage("Da li �elite da izmenite pitanje?")
				.setCancelable(false)
				.setPositiveButton("Da", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent ucitajPitanje = new Intent(getApplicationContext(),UcitajPitanjeAktivnost.class);
						ucitajPitanje.putExtra("promeni", true);
						ucitajPitanje.putExtra("pitanje", pit);
						startActivityForResult(ucitajPitanje, 2);
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
						for (PitanjeStat pitanje : selektovanaPitanja){
							DatabaseBroker.vratiInstancu().resetujStatistiku(pitanje.getPitanje().getJedinstveniIDikada());
						}
						adapter = new AdapterListe(act, DatabaseBroker.vratiInstancu().vratiSvaPitanja(false), true);
						lista.setAdapter(adapter);
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
		String poruka = "Da li ste sigurni da želite potpuno obrisati odabrana pitanja?";
		final boolean visePitanja;
		if (selektovanaPitanja.size()==1){
			poruka = "Da li ste sigurni da želite potpuno obrisati odabrano pitanje?";
			visePitanja=false;
		} else {
			visePitanja = true;
		}
		builder.setMessage(poruka)
				.setCancelable(false)
				.setPositiveButton("Da", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						for (PitanjeStat pitanje : selektovanaPitanja){
							DatabaseBroker.vratiInstancu().obrisiPitanje(pitanje.getPitanje().getJedinstveniIDikada());
							selektovanaPitanja.remove(pitanje);
						}
						adapter = new AdapterListe(act, DatabaseBroker.vratiInstancu().vratiSvaPitanja(false), true);
						lista.setAdapter(adapter);
						if (visePitanja){
							Toast.makeText(getApplicationContext(), "Pitanja su uspešno obrisana!", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(), "Pitanje je uspešno obrisano!", Toast.LENGTH_SHORT).show();
						}
						setResult(Activity.RESULT_OK);
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
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 11) {
			svaPitanjaUBazi.clear();
			svaPitanjaUBazi.addAll(DatabaseBroker.vratiInstancu().vratiSvaPitanja(false));
			adapter = new AdapterListe(this,svaPitanjaUBazi, true);
			lista.setAdapter(adapter);
			setResult(Activity.RESULT_OK);
		} else {
			// Pitanje nije menjano
		}
}

	

}
