package com.example.pedagogijadidaktika2;

import java.util.List;

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

	private Activity activity;
	private List<PitanjeStat> listaSvihPitanja;
	
	public AdapterListe(Activity a, List<PitanjeStat> listaPi){
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
		if (vi==null){			
			LayoutInflater inflater = activity.getLayoutInflater();
			vi = inflater.inflate(R.layout.list_row, null);
		      // configure view holder
		      ViewHolder viewHolder = new ViewHolder();
		      viewHolder.kreator = (TextView) vi.findViewById(R.id.kreator);
		      viewHolder.tacanOdgovor = (TextView)vi.findViewById(R.id.tacanOdgovor);
		      viewHolder.tekstPitanja = (TextView)vi.findViewById(R.id.textPitanja);
		      viewHolder.selected = (CheckBox) vi.findViewById(R.id.checkBox);
		      vi.setTag(viewHolder);		      
		}
		
		ViewHolder holder = (ViewHolder) vi.getTag();
		PitanjeStat pit = listaSvihPitanja.get(position);
		Pitanje pi = pit.getPitanje();
		holder.kreator.setText(pi.getKreator());
		holder.tacanOdgovor.setText(pi.getOdgovori()[Integer.parseInt(pi.getOdgovori()[0])]);
		holder.tekstPitanja.setText(pi.getmTextPitanja());;
		holder.selected.setChecked(true);
		
		holder.selected.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				((CompoundButton) arg0.findViewById(R.id.checkBox)).setChecked(!((CompoundButton) arg0.findViewById(R.id.checkBox)).isChecked());
				return true;
			}
		});
		
		return vi;
	}
	
	static class ViewHolder {
		public CheckBox selected;
		public TextView tekstPitanja;
		public TextView tacanOdgovor;
		public TextView kreator;
	  }

}
