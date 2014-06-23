package com.example.pedagogijadidaktika2;

import domen.Pitanje;
import poslovnaLogika.Kontroler;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterViewFlipper;
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

		lista = (ListView) this.findViewById(R.id.listaPitanjaMain);
		adapter = new AdapterListe(this,
				Kontroler.vratiObjekat().kolekcijaPitanja.getPitanja());
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pregled_pitanja, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
