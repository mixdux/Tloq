package com.example.pedagogijadidaktika2;

import java.util.List;

import com.example.pedagogijadidaktikarad.PitanjeAktivnost;

import domen.PitanjeStat;
import poslovnaLogika.DatabaseBroker;
import poslovnaLogika.KolekcijaStatPitanja;
import poslovnaLogika.Kontroler;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PregledPitanja extends Activity {

	AdapterListe adapter;
	ListView lista;

	boolean longHoldShield = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pregled_pitanja);

		final List<PitanjeStat> svaPitanjaUBazi = new DatabaseBroker(this)
				.vratiSvaPitanja(false);

		lista = (ListView) this.findViewById(R.id.listaPitanjaMain);
		adapter = new AdapterListe(this, svaPitanjaUBazi, false);
		lista.setAdapter(adapter);

		final Vibrator vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);

		Button opcije = (Button) findViewById(R.id.btnOpcije);

		opcije.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent opcijePitanja = new Intent(getApplicationContext(),
						OpcijePitanja.class);
				opcijePitanja.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				getApplicationContext().startActivity(opcijePitanja);
			}
		});

		lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Button b = (Button) v.findViewById(R.id.onOfMarker);
				if (!longHoldShield) {
					vibrator.vibrate(50);
					ColorDrawable buttonColor = (ColorDrawable) b
							.getBackground();
					int color = buttonColor.getColor();
					PitanjeStat pit = svaPitanjaUBazi.get(position);
					if (color == Color.BLUE) {
						b.setBackgroundColor(Color.RED);
						new DatabaseBroker(getApplicationContext())
								.updateAktivno(false, pit.getPitanje()
										.getJedinstveniIDikada());
						pit.setAktivno(false);
						Kontroler.vratiObjekat().getKolekcijaStatPitanja()
								.izbaciStatPitanje(pit);
					} else {
						b.setBackgroundColor(Color.BLUE);
						new DatabaseBroker(getApplicationContext())
								.updateAktivno(true, pit.getPitanje()
										.getJedinstveniIDikada());
						pit.setAktivno(true);
						Kontroler.vratiObjekat().getKolekcijaStatPitanja()
								.DodajPitanje(pit);
					}
				} else {
					longHoldShield = false;
				}
			}
		});

		/*lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			// http://stackoverflow.com/questions/12244297/how-to-add-multiple-buttons-on-a-single-alertdialog
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				vibrator.vibrate(150);
				// showDialog();
				longHoldShield = true;
				return false;
			}

		});*/

	}

	@Override
	protected void onResume() {
		super.onResume();
		adapter = new AdapterListe(this,
				new DatabaseBroker(this).vratiSvaPitanja(false), false);
		lista.setAdapter(adapter);
	}

}
