package com.example.pedagogijadidaktika2;

import java.security.interfaces.ECKey;
import java.util.List;

import poslovnaLogika.DatabaseBroker;
import poslovnaLogika.KolekcijaStatPitanja;
import poslovnaLogika.Kontroler;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import domen.Pitanje;
import domen.PitanjeStat;

public class AdapterListe extends BaseAdapter {

	private final Activity activity;
	private List<PitanjeStat> listaSvihPitanja;

	public AdapterListe(Activity a, List<PitanjeStat> listaPi) {
		activity = a;
		listaSvihPitanja = listaPi;
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
			vi = inflater.inflate(R.layout.list_row, null);
			// configure view holder
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.kreator = (TextView) vi.findViewById(R.id.kreator);
			viewHolder.tacanOdgovor = (TextView) vi
					.findViewById(R.id.tacanOdgovor);
			viewHolder.tekstPitanja = (TextView) vi
					.findViewById(R.id.textPitanja);
			viewHolder.selected = (CheckBox) vi.findViewById(R.id.checkBox);
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
			holder.selected.setChecked(true);
		} else {
			holder.selected.setChecked(false);
		}

		/*holder.selected.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent mevt) {
				KolekcijaStatPitanja ksp = Kontroler.vratiObjekat().kolekcijaStatPitanja;
				if (mevt.getAction() == MotionEvent.ACTION_UP) {
					CheckBox cb = (CheckBox) v.findViewById(R.id.checkBox);
					boolean isChecked = cb.isChecked();

					if (isChecked) {
						cb.setChecked(false);
						new DatabaseBroker(activity).updateAktivno(false, pit
								.getPitanje().getJedinstveniIDikada());
						pit.setAktivno(false);
						Kontroler.vratiObjekat().kolekcijaStatPitanja
								.izbaciStatPitanje(pit);
						ksp = Kontroler.vratiObjekat().kolekcijaStatPitanja;
						return true;
					} else {
						cb.setChecked(true);
						new DatabaseBroker(activity).updateAktivno(true, pit
								.getPitanje().getJedinstveniIDikada());
						pit.setAktivno(true);
						Kontroler.vratiObjekat().kolekcijaStatPitanja
								.DodajPitanje(pit);
						ksp = Kontroler.vratiObjekat().kolekcijaStatPitanja;
						return true;
					}
				}
				return true;
			}
		});*/

		return vi;
	}

	static class ViewHolder {
		public CheckBox selected;
		public TextView tekstPitanja;
		public TextView tacanOdgovor;
		public TextView kreator;
	}

}
