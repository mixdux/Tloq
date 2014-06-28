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

	boolean longHoldShield = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opcije_pitanja);

		final List<PitanjeStat> svaPitanjaUBazi = new DatabaseBroker(this)
				.vratiSvaPitanja(false);

		lista = (ListView) this.findViewById(R.id.listaPitanjaOpcije);
		adapter = new AdapterListe(this, svaPitanjaUBazi, true);
		lista.setAdapter(adapter);
		final Vibrator vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
		final List<PitanjeStat> selektovanaPitanja = new ArrayList<PitanjeStat>();

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
				showDialog();
				longHoldShield = true;
				return false;
			}

		});

	}

	private void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Da li želite da izmenite pitanje?")
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


}
