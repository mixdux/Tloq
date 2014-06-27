package com.example.pedagogijadidaktika2;

import java.util.List;

import domen.PitanjeStat;
import poslovnaLogika.DatabaseBroker;
import poslovnaLogika.KolekcijaStatPitanja;
import poslovnaLogika.Kontroler;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class PregledPitanja extends Activity {

	AdapterListe adapter;
	ListView lista;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pregled_pitanja);
		
		final List<PitanjeStat> svaPitanjaUBazi = new DatabaseBroker(this).vratiSvaPitanja(false);
		
		lista = (ListView) this.findViewById(R.id.listaPitanjaMain);
		adapter = new AdapterListe(this,svaPitanjaUBazi);
		lista.setAdapter(adapter);

		lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				KolekcijaStatPitanja ksp = Kontroler.vratiObjekat().kolekcijaStatPitanja;
					CheckBox cb = (CheckBox) v.findViewById(R.id.checkBox);
					boolean isChecked = cb.isChecked();
					PitanjeStat pit = svaPitanjaUBazi.get(position);
					if (isChecked) {
						cb.setChecked(false);
						new DatabaseBroker(getApplicationContext()).updateAktivno(false, pit
								.getPitanje().getJedinstveniIDikada());
						pit.setAktivno(false);
						Kontroler.vratiObjekat().kolekcijaStatPitanja
								.izbaciStatPitanje(pit);
						ksp = Kontroler.vratiObjekat().kolekcijaStatPitanja;

					} else {
						cb.setChecked(true);
						new DatabaseBroker(getApplicationContext()).updateAktivno(true, pit
								.getPitanje().getJedinstveniIDikada());
						pit.setAktivno(true);
						Kontroler.vratiObjekat().kolekcijaStatPitanja
								.DodajPitanje(pit);
						ksp = Kontroler.vratiObjekat().kolekcijaStatPitanja;
					}
				}
		});

	}
}
