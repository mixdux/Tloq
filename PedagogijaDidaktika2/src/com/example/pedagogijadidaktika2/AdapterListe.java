package com.example.pedagogijadidaktika2;

import java.security.interfaces.ECKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import poslovnaLogika.DatabaseBroker;
import poslovnaLogika.KolekcijaStatPitanja;
import poslovnaLogika.Kontroler;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import domen.Pitanje;
import domen.PitanjeStat;

public class AdapterListe extends BaseAdapter {

	private final Activity activity;
	private List<PitanjeStat> listaSvihPitanja;
	private boolean listaOpcija;

	public AdapterListe(Activity a, List<PitanjeStat> listaPi,
			boolean listaOpcija) {
		activity = a;
		listaSvihPitanja = listaPi;
		this.listaOpcija = listaOpcija;
	}

	@Override
	public int getCount() {
		return listaSvihPitanja.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listaSvihPitanja.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		if (vi == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			if (!listaOpcija) {
				vi = inflater.inflate(R.layout.list_row, null);
			} else {
				vi = inflater.inflate(R.layout.list_options_row, null);
			}
			// configure view holder
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.kreator = (TextView) vi.findViewById(R.id.kreator);
			viewHolder.tacanOdgovor = (TextView) vi
					.findViewById(R.id.tacanOdgovor);
			viewHolder.tekstPitanja = (TextView) vi
					.findViewById(R.id.textPitanja);
			if (listaOpcija) {
				viewHolder.selected = (CheckBox) vi.findViewById(R.id.checkBox);
				viewHolder.tacni = (TextView) vi.findViewById(R.id.txtBrojTacnihOdgovora);
				viewHolder.netacni = (TextView) vi.findViewById(R.id.txtBrojNetacnihOdgovora);
				viewHolder.vreme = (TextView) vi.findViewById(R.id.txtPotrebneSekunde);
				viewHolder.rejting = (TextView) vi.findViewById(R.id.txtRejtingZvezdice);
				viewHolder.drugiOdgovor = (TextView) vi.findViewById(R.id.drugiOdgovor);
				viewHolder.treciOdgovor = (TextView) vi.findViewById(R.id.treciOdgovor);
				viewHolder.cetvrtiOdgovor = (TextView) vi.findViewById(R.id.cetvrtiOdgovor);
			}
			viewHolder.onOf = (Button) vi.findViewById(R.id.onOfMarker);
			vi.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) vi.getTag();
		final PitanjeStat pit = listaSvihPitanja.get(position);
		Pitanje pi = pit.getPitanje();
		holder.kreator.setText(pi.getKreator());
		holder.tacanOdgovor.setText(pi.getOdgovori()[Integer.parseInt(pi
				.getOdgovori()[0])]);
		holder.tekstPitanja.setText(pi.getmTextPitanja());
		if (pit.isAktivno()) {
			holder.onOf.setBackgroundColor(Color.BLUE);
		} else {
			holder.onOf.setBackgroundColor(Color.RED);
		}
		if (listaOpcija) {
			holder.tacni.setText(""+pit.getBrojTacnihOdgovora());
			holder.netacni.setText(""+pit.getBrojNetacnihOdgovora());
			List<String> sviOdgovori = new ArrayList<String>(Arrays.asList(pi.getOdgovori()));
			sviOdgovori.remove(0);
			sviOdgovori.remove(holder.tacanOdgovor.getText());
			holder.drugiOdgovor.setText(sviOdgovori.get(0));
			holder.treciOdgovor.setText(sviOdgovori.get(1));
			holder.cetvrtiOdgovor.setText(sviOdgovori.get(2));
			int milisekunde = pit.getVremeZaOdgovor();
			double sekunde = (double)milisekunde/1000;
			String sekundeIsecene = String.valueOf(sekunde).substring(0, 3);
			holder.vreme.setText(sekundeIsecene);
			if (milisekunde==0){
				holder.vreme.setText("N/A");
			}
			//holder.rejting = (TextView) vi.findViewById(R.id.txtRejtingZvezdice);
		}

		return vi;
	}

	static class ViewHolder {
		public CheckBox selected;
		public TextView tekstPitanja;
		public TextView tacanOdgovor;
		public TextView drugiOdgovor;
		public TextView treciOdgovor;
		public TextView cetvrtiOdgovor;
		public TextView kreator;
		public Button onOf;
		public TextView tacni;
		public TextView netacni;
		public TextView vreme;
		public TextView rejting;
		
	}

}
