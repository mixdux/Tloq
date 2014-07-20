package com.example.pedagogijadidaktika2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.example.pedagogijadidaktika2.AdapterListe.ViewHolder;

import domen.Pitanje;
import domen.PitanjeStat;
import domen.SetPitanja;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class AdapterProsiriveListe extends BaseExpandableListAdapter{

	private Activity activity;
    private List<SetPitanja> listaHedera;
// HEŠ MAPA SE PRAVI PREMA AUID-U !
    private HashMap<String, List<PitanjeStat>> listaDeteta;	
    
    public AdapterProsiriveListe(Activity act, List<SetPitanja> listaHedera,
            HashMap<String, List<PitanjeStat>> listaDeteta) {
    	activity = act;
        this.listaHedera = listaHedera;
        this.listaDeteta = listaDeteta;
    }


	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return listaDeteta.get(listaHedera.get(groupPosition).getAUIDseta()).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition){
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

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
			viewHolder.onOf = (Button) vi.findViewById(R.id.onOfMarker);
			vi.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) vi.getTag();
		final PitanjeStat pit = (PitanjeStat) getChild(groupPosition, childPosition);
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
		
		return vi;
		
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return listaDeteta.get(listaHedera.get(groupPosition).getAUIDseta()).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return listaHedera.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return listaHedera.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View vi = convertView;
		if (vi == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
				vi = inflater.inflate(R.layout.list_heading_row, null);
			// configure view holder
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.autorSeta = (TextView) vi.findViewById(R.id.autorSeta);
			viewHolder.imeSeta = (TextView) vi.findViewById(R.id.imeSeta);
			vi.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) vi.getTag();
		final SetPitanja set = (SetPitanja) getGroup(groupPosition);
		holder.autorSeta.setText(set.getImeKreatora());
		holder.imeSeta.setText(set.getImeSeta());		
		return vi;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
