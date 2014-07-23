package com.example.pedagogijadidaktika2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.pedagogijadidaktikarad.PitanjeAktivnost;

import domen.PitanjeStat;
import domen.SetPitanja;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.RelativeLayout;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PregledPitanja extends Activity {

	AdapterProsiriveListe adapter;
	ExpandableListView lista;
	final Activity ovaSama = this;

	boolean longHoldShield = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pregled_pitanja);

		//final List<PitanjeStat> svaPitanjaUBazi = new DatabaseBroker(this).vratiSvaPitanja(false);
		final HashMap<String, List<PitanjeStat>> pitanjaISetovi = new DatabaseBroker(this).vratiSetIPitanja();
		final List<SetPitanja> sviSetovi = new DatabaseBroker(this).vratiSveSetove();		
		lista = (ExpandableListView) findViewById(R.id.listaPitanjaMain);
		adapter = new AdapterProsiriveListe(this, sviSetovi, pitanjaISetovi);
		lista.setAdapter(adapter);
		final List<Integer> expandovani = new ArrayList<Integer>();

		final Vibrator vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);

		Button opcije = (Button) findViewById(R.id.btnOpcije);

		opcije.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent opcijePitanja = new Intent(getApplicationContext(),
						OpcijePitanja.class);
				opcijePitanja.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				getApplicationContext().startActivity(opcijePitanja);
			}
		});

		lista.setOnChildClickListener(new OnChildClickListener() {
 
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
            	Button b = (Button) v.findViewById(R.id.onOfMarker);
            	View group = adapter.getGroupView(groupPosition, true, null, null);
            	Button bh = (Button) group.findViewById(R.id.onOfHeadingMarker);
				if (!longHoldShield) {
					vibrator.vibrate(50);
					ColorDrawable buttonColor = (ColorDrawable) b
							.getBackground();
					int color = buttonColor.getColor();
					PitanjeStat pit = (PitanjeStat) adapter.getChild(groupPosition, childPosition);
					String AUIDSeta = sviSetovi.get(groupPosition).getAUIDseta();
					List<PitanjeStat> dobijenaPitanja = pitanjaISetovi.get(AUIDSeta);
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
					adapter.notifyDataSetChanged();
				} else {
					longHoldShield = false;
				}
                return true;
            }
        });
		
		lista.setOnGroupExpandListener(new OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int groupPosition) {
				if (!expandovani.contains(groupPosition)){
					expandovani.add(groupPosition);	
				}
			}
		});
		lista.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			@Override
			public void onGroupCollapse(int groupPosition) {
				if (expandovani.contains(groupPosition)){
				expandovani.remove(groupPosition);
				}
			}
		});

		lista.setOnItemLongClickListener(new OnItemLongClickListener() {
		      @TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
		      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		          int itemType = ExpandableListView.getPackedPositionType(id);
		          if(itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
		        	  //longHoldShield=true;
		              int groupPosition = ExpandableListView.getPackedPositionGroup(id);
		              View group = adapter.getGroupView(groupPosition, true, null, null);
		              Button bh = (Button) group.findViewById(R.id.onOfHeadingMarker);
		              String AUIDSeta = sviSetovi.get(groupPosition).getAUIDseta();
		              List<PitanjeStat> dobijenaPitanja = pitanjaISetovi.get(AUIDSeta);
		              ColorDrawable buttonColor = (ColorDrawable) bh.getBackground();
		              int color = buttonColor.getColor();
		              DatabaseBroker dbb = new DatabaseBroker(getApplicationContext());
		              if (color == Color.BLUE) {
		            	  for (PitanjeStat pit : dobijenaPitanja){
		            		dbb.updateAktivno(false, pit.getPitanje()
											.getJedinstveniIDikada());
							pit.setAktivno(false);
							Kontroler.vratiObjekat().getKolekcijaStatPitanja()
									.izbaciStatPitanje(pit);
		            	  }
						} else {
							for (PitanjeStat pit : dobijenaPitanja){
							dbb.updateAktivno(true, pit.getPitanje()
											.getJedinstveniIDikada());
							pit.setAktivno(true);
							Kontroler.vratiObjekat().getKolekcijaStatPitanja()
									.DodajPitanje(pit);
							}
						}
		              adapter = new AdapterProsiriveListe(ovaSama, dbb.vratiSveSetove(), dbb.vratiSetIPitanja());
		              lista.setAdapter(adapter);
		              for (Integer i : expandovani){
		            	  int in = i.intValue();
		            	  lista.expandGroup(in);
		              }
		              return true;
		          }
		          return false;
		      }
		  });
		
		/*lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
		});*/

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
		adapter = new AdapterProsiriveListe(this, new DatabaseBroker(this).vratiSveSetove(),  new DatabaseBroker(this).vratiSetIPitanja());
		lista.setAdapter(adapter);
	}

}
