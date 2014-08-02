package com.example.pedagogijadidaktika2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import util.SelectedHolder;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AdapterProsiriveListe extends BaseExpandableListAdapter{

	private Activity activity;
    private List<SetPitanja> listaHedera;
// HEŠ MAPA SE PRAVI PREMA AUID-U !
    private HashMap<String, List<PitanjeStat>> listaDeteta;
    HashMap<Integer, HashMap<Integer, Boolean>> selektovani;
    private boolean listaOpcija;
    
    public AdapterProsiriveListe(Activity act, List<SetPitanja> listaHedera,
            HashMap<String, List<PitanjeStat>> listaDeteta, boolean opcije, HashMap<Integer, HashMap<Integer, Boolean>> selektovani) {
    	activity = act;
        this.listaHedera = listaHedera;
        this.listaDeteta = listaDeteta;
        listaOpcija = opcije;
        this.selektovani = selektovani;
    }

    public void promenjeniPodaci(List<SetPitanja> listaHedera, HashMap<String, List<PitanjeStat>> listaDeteta){
    	this.listaDeteta.clear();
    	this.listaDeteta.putAll(listaDeteta);
    	this.listaHedera.clear();
    	this.listaHedera.addAll(listaHedera);
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
		if (getChildrenCount(groupPosition)==0){
			return null;
		}
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
				viewHolder.sekundeOznaka = (TextView) vi.findViewById(R.id.txtSekunde);
			}
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
		if (listaOpcija) {
			holder.selected.setChecked(false);
			if (selektovani.containsKey(groupPosition)){
				if (selektovani.get(groupPosition).containsKey(childPosition)){
					holder.selected.setChecked(selektovani.get(groupPosition).get(childPosition));
				}
			}
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
				holder.sekundeOznaka.setVisibility(View.INVISIBLE);
			}
			//holder.rejting = (TextView) vi.findViewById(R.id.txtRejtingZvezdice);
		}
		return vi;
		
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(!listaDeteta.containsKey(listaHedera.get(groupPosition).getAUIDseta())){
			return 0;
		}
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
			if (!listaOpcija) {
				vi = inflater.inflate(R.layout.list_heading_row, null);
			} else {
				vi = inflater.inflate(R.layout.list_options_heading_row, null);
			}
				
			// configure view holder
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.autorSeta = (TextView) vi.findViewById(R.id.autorSeta);
			viewHolder.imeSeta = (TextView) vi.findViewById(R.id.imeSeta);
			viewHolder.onOfSet = (Button) vi.findViewById(R.id.onOfHeadingMarker);
			if (listaOpcija){
				viewHolder.selectedSet = (CheckBox) vi.findViewById(R.id.checkBoxHeading);
				viewHolder.doprinosioci = (TextView) vi.findViewById(R.id.txtDoprinosioci);
				viewHolder.onOfSet = (Button) vi.findViewById(R.id.onOfHeadingOptionsMarker);
			}
			vi.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) vi.getTag();
		final SetPitanja set = (SetPitanja) getGroup(groupPosition);
		podesiSetSelekijeDugme(holder.onOfSet, listaDeteta.get(set.getAUIDseta()));
		holder.autorSeta.setText(Pitanje.userFriendlyKreator(set.getImeKreatora()));
		holder.imeSeta.setText(set.getImeSeta());
		if (listaOpcija){
			holder.selectedSet.setChecked(false);
			if (selektovani.containsKey(groupPosition)){
				if (!selektovani.get(groupPosition).containsKey(-1)){
					holder.selectedSet.setChecked(true);
				}
			}
			if (set.getImeDoprinosioca().equals("")){
				holder.doprinosioci.setVisibility(View.GONE);
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.autorSeta.getLayoutParams();
				params.addRule(RelativeLayout.BELOW, R.id.imeSeta);
			} else {
				holder.doprinosioci.setText("Doprinosioci: "+ set.getImeDoprinosioca().replace(";", "; ").replace(",", ", "));
			}
		}
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
	
	public static boolean podesiSetSelekijeDugme(Button dugme, List<PitanjeStat> svaPitanjaZaSet){
		boolean celiSetSelektovan = true;
		if (svaPitanjaZaSet==null){
			dugme.setBackgroundColor(Color.GRAY);
		} else {
			for (PitanjeStat pist : svaPitanjaZaSet){
				celiSetSelektovan &= pist.isAktivno();
			}
			if (celiSetSelektovan) {
				dugme.setBackgroundColor(Color.BLUE);
			} else {
				dugme.setBackgroundColor(Color.RED);
			}
		}
		return celiSetSelektovan;
	}

}
