package com.example.pedagogijadidaktika2;

import poslovnaLogika.Kontroler;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class PregledPitanja extends Activity {

	AdapterListe adapter;
	ListView lista;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pregled_pitanja);

		lista = (ListView) this.findViewById(R.id.listaPitanjaMain);
		adapter = new AdapterListe(this,
				Kontroler.vratiObjekat().kolekcijaStatPitanja.getPitanja());
		lista.setAdapter(adapter);

		lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			TextView tv = null;
			@Override
			public void onItemClick(AdapterView<?> parent, View item,
					int position, long id) {
				if (tv != null) {
					if (tv == (TextView) item.findViewById(R.id.textPitanja)) {
						if (tv.isSelected()) {
							tv.setSelected(false);
						} else {
							tv.setSelected(true);
						}
					} else {
						tv.setSelected(false);
						tv = (TextView) item.findViewById(R.id.textPitanja);
						tv.setSelected(true);
					}
				} else {
					tv = (TextView) item.findViewById(R.id.textPitanja);
					tv.setSelected(true);
				}
			}
		});

	}
}
