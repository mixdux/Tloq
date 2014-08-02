package com.example.pedagogijadidaktika2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.pedagogijadidaktikarad.UcitajPitanjeAktivnost;

import poslovnaLogika.DatabaseBroker;
import poslovnaLogika.Kontroler;
import util.SelectedHolder;
import domen.PitanjeStat;
import domen.SetPitanja;
import android.support.v4.app.Fragment;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class OpcijePitanja extends Activity {
	
	//UPDATE PITANjA U KONTROLERU

	AdapterProsiriveListe adapter;
	ExpandableListView lista;
	final List<PitanjeStat> selektovanaPitanja = new ArrayList<PitanjeStat>();
	Activity act = this;
	final HashMap<Integer, HashMap<Integer, Boolean>> selektovani = new HashMap<Integer, HashMap<Integer, Boolean>>();
	float touchX;
	float touchY;
	final List<SetPitanja> selektovaniSetovi = new ArrayList<SetPitanja>();
	final List<Integer> expandovani = new ArrayList<Integer>();
	boolean longClickShield;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opcije_pitanja);
		Button obrisi = (Button) findViewById(R.id.btnObrisi);
		Button resetuj = (Button) findViewById(R.id.btnResetujStatistiku);
		HashMap<String, List<PitanjeStat>> pitanjaISetovi = DatabaseBroker.vratiInstancu(this).vratiSetIPitanja();
		List<SetPitanja> sviSetovi = DatabaseBroker.vratiInstancu().vratiSveSetove();
		lista = (ExpandableListView) this.findViewById(R.id.listaPitanjaOpcije);
		adapter = new AdapterProsiriveListe(this, sviSetovi, pitanjaISetovi, true, selektovani);
		lista.setAdapter(adapter);
		final Vibrator vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
		
		lista.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				if (!longClickShield){
				CheckBox cb = (CheckBox) v.findViewById(R.id.checkBox);
				//Toast.makeText(getApplicationContext(), ((TextView) v.findViewById(R.id.textPitanja)).getText().toString(), Toast.LENGTH_SHORT).show();
				List<PitanjeStat> deca = DatabaseBroker.vratiInstancu().vratiPitanjaZaSet(((SetPitanja) adapter.getGroup(groupPosition)).getAUIDseta());
				boolean isChecked = cb.isChecked();
					vibrator.vibrate(50);
					PitanjeStat pit = (PitanjeStat) adapter.getChild(groupPosition, childPosition);
					if (isChecked) {
						if (selektovani.containsKey(groupPosition)){
							if (selektovani.get(groupPosition).containsKey(childPosition)){
								selektovani.get(groupPosition).put(childPosition, false);
							}
							
							selektovani.get(groupPosition).put(-1, true);
						}
						boolean i = selektovanaPitanja.remove(pit);
						selektovaniSetovi.remove((SetPitanja) adapter.getGroup(groupPosition));
						for (Integer dete : selektovani.get(groupPosition).keySet()){
							if (selektovani.get(groupPosition).get(dete) && dete!=-1){
								PitanjeStat selDet = deca.get(dete);
								if (!selektovanaPitanja.contains(selDet)){
									selektovanaPitanja.add(selDet);
								}
							}
						}
					} else {
						selektovanaPitanja.add(pit);
						int decaV = deca.size();
						handleCekiranje(decaV, groupPosition, childPosition);
					}
					cb.toggle();
					refreshListe();
				return true;
			} else {
				longClickShield=false;
				return true;
			}
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
				expandovani.remove((Integer)groupPosition);
				}
			}
		});
		
		lista.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				touchX = event.getX();
				touchY = event.getY();
				return false;
			}
		});
		lista.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				CheckBox cb = (CheckBox) v.findViewById(R.id.checkBoxHeading);
				float absY = v.getTop();
				if ((touchX>=cb.getLeft() && touchX<=(cb.getLeft()+cb.getWidth())) && (touchY>=(cb.getTop()+absY) && touchY<=(cb.getTop()+cb.getHeight()+absY))){
					List<PitanjeStat> deca = DatabaseBroker.vratiInstancu().vratiPitanjaZaSet(((SetPitanja) adapter.getGroup(groupPosition)).getAUIDseta());
					if (deca.size()==0){
						return false;
					}
					if(cb.isChecked()){
						selektovaniSetovi.remove((SetPitanja) adapter.getGroup(groupPosition));
						List<PitanjeStat> zaIzbaciti = new ArrayList<PitanjeStat>();
						for (PitanjeStat pist : selektovanaPitanja){
							if (pist.getPitanje().getIdSeta().equals(((SetPitanja) adapter.getGroup(groupPosition)).getAUIDseta())){
								zaIzbaciti.add(pist);
							}
						}
						if (selektovani.containsKey(groupPosition)){
							for (int i = 0 ; i < deca.size(); i++){
								selektovani.get(groupPosition).put(i, false);
							}
						}
						selektovani.get(groupPosition).put(-1, true);
						selektovanaPitanja.removeAll(zaIzbaciti);
					} else {
						selektovaniSetovi.add((SetPitanja) adapter.getGroup(groupPosition));
						List<PitanjeStat> zaIzbaciti = new ArrayList<PitanjeStat>();
						for (PitanjeStat pist : selektovanaPitanja){
							if (pist.getPitanje().getIdSeta().equals(((SetPitanja) adapter.getGroup(groupPosition)).getAUIDseta())){
								zaIzbaciti.add(pist);
							}
						}
						if (selektovani.containsKey(groupPosition)){
							for (int i = 0 ; i < deca.size(); i++){
								selektovani.get(groupPosition).put(i, true);
							}
						} else {
							selektovani.put(groupPosition, new HashMap<Integer, Boolean>());
							for(int i = 0 ; i < deca.size(); i++){
								selektovani.get(groupPosition).put(i, true);
							}
						}
						selektovani.get(groupPosition).remove(-1);
						selektovanaPitanja.removeAll(zaIzbaciti);
					}
					cb.toggle();
					refreshListe();
					return true;
				}
				return false;
			}
			
		});

		lista.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				int itemType = ExpandableListView.getPackedPositionType(id);
				int groupPosition = ExpandableListView.getPackedPositionGroup(id);
				int childPosition = ExpandableListView.getPackedPositionChild(id);
		        if(itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
		        	//Dodaj verifikaciju korisničkog imena!!!
		        	
		        	if (((SetPitanja) adapter.getGroup(groupPosition)).getImeSeta().equals(Kontroler.vratiObjekat().getImeGenericSeta())){
		        		Toast.makeText(getApplicationContext(), "Osnovni set se ne može menjati", Toast.LENGTH_SHORT).show();
		        		vibrator.vibrate(new long[]{0,150,50,150,50,150}, -1);
		        	} else {
		        		prikaziPromeniSetDijalog((SetPitanja) adapter.getGroup(groupPosition));
		        		vibrator.vibrate(new long[]{0,150,50,150}, -1);
		        	}
		        	return false;
		        } else {
		        	longClickShield=true;
					vibrator.vibrate(150);
					List<PitanjeStat> deca = DatabaseBroker.vratiInstancu().vratiPitanjaZaSet(((SetPitanja) adapter.getGroup(groupPosition)).getAUIDseta());
					if (!selektovanaPitanja.contains((PitanjeStat) adapter.getChild(groupPosition, childPosition))){
						selektovanaPitanja.add((PitanjeStat) adapter.getChild(groupPosition, childPosition));
					}
					handleCekiranje(deca.size(), groupPosition, childPosition);
					refreshListe();
					if (DatabaseBroker.vratiInstancu().vratiSveSetove().size()<2 && (selektovanaPitanja.size()>1 || selektovaniSetovi.size()==1)){
						Toast.makeText(getApplicationContext(), "Potrebno je imati najmanje dva seta da radi premeštanja", Toast.LENGTH_SHORT).show();
					} else {
						boolean posebnaDozvola = deca.size()==1 && selektovaniSetovi.size()==1;
						prikaziPromeniDijalog((PitanjeStat) adapter.getChild(groupPosition, childPosition), posebnaDozvola);
					}
					return false;
		        }
		        
			}
		});
		
		
		obrisi.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (selektovanaPitanja.size()==0 && selektovaniSetovi.size()==0){
					Toast.makeText(getApplicationContext(), "Morate odabrati barem jedno pitanje ili set", Toast.LENGTH_SHORT).show();
				} else {
					prikaziObrisiDijalog();
				}
			}
		});
		
		resetuj.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (selektovanaPitanja.size()==0 && selektovaniSetovi.size()==0){
					Toast.makeText(getApplicationContext(), "Morate odabrati barem jedno pitanje ili set", Toast.LENGTH_SHORT).show();
				} else {
					prikaziResetujDijalog();
				}
			}
		});

	}

	private void prikaziPromeniDijalog(PitanjeStat pitanje, boolean jedinoDete) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final PitanjeStat pit = pitanje;
		String poruka = "Da li želite da premestite odabrana pitanja?";
		builder.setCancelable(false);
		AlertDialog alert = builder.create();
		int i = selektovanaPitanja.size();
		int j = selektovaniSetovi.size();
		if ((selektovanaPitanja.size()==1 && selektovaniSetovi.size()==0) || jedinoDete){
			poruka = "Da li želite da izmenite pitanje?";
			alert.setButton(android.content.DialogInterface.BUTTON_NEUTRAL, "Izmeni", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					selektovanaPitanja.clear();
					selektovani.clear();
					Intent ucitajPitanje = new Intent(getApplicationContext(),UcitajPitanjeAktivnost.class);
					ucitajPitanje.putExtra("promeni", true);
					ucitajPitanje.putExtra("pitanje", pit);
					startActivityForResult(ucitajPitanje, 2);
				}
			});
		}
		alert.setMessage(poruka);
		if (DatabaseBroker.vratiInstancu().vratiSveSetove().size()>1){
		alert.setButton(android.content.DialogInterface.BUTTON_POSITIVE, "Premesti", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				prikaziPomeriDialog();
			}
		});
		}
		alert.setButton(android.content.DialogInterface.BUTTON_NEGATIVE, "Nazad", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Toast.makeText(getApplicationContext(), "Nazad", Toast.LENGTH_SHORT).show();
					}
				});		

		alert.show();
	}
	
	private void prikaziResetujDijalog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String poruka = "Da li ste sigurni da želite resetovati statistiku"; 
		if (selektovaniSetovi.size()==0){
			if (selektovanaPitanja.size()==1){
				poruka += " odabranog pitanja?";
			} else {
				poruka += " odabranih pitanja?";
			}
		} else {
			if (selektovaniSetovi.size()==1){
				poruka += " odabranog seta";
			} else {
				poruka += " odabranih setova";
			}
			if (selektovanaPitanja.size()==0){
				poruka += "?";
			} else {
				if (selektovanaPitanja.size()==1){
					poruka += " i odabranog pitanja?";
				} else {
					poruka += " i odabranih pitanja?";
				}
			}
		}
		builder.setMessage(poruka)
				.setCancelable(false)
				.setPositiveButton("Da", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {					
						for (PitanjeStat pitanje : selektovanaPitanja){
							DatabaseBroker.vratiInstancu().resetujStatistiku(pitanje.getPitanje().getJedinstveniIDikada());
						}
						potpuniRefreshListe();
					}
				});
		AlertDialog alert = builder.create();
		alert.setButton(android.content.DialogInterface.BUTTON_NEGATIVE, "Ne", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Toast.makeText(getApplicationContext(), "Ne resetuj", Toast.LENGTH_SHORT).show();
					}
				});		

		alert.show();
	}
	
	private void prikaziObrisiDijalog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		int visePitanja = 0;
		int viseSetova = 0;
		String poruka = "Da li ste sigurni da želite potpuno"; 
		if (selektovaniSetovi.size()==0){
			viseSetova = 0;
			visePitanja = 0;
			if (selektovanaPitanja.size()==1){
				poruka += " obrisati odabrano pitanje?";
				visePitanja = 1;
			} else {
				poruka += " obrisati odabrana pitanja?";
				visePitanja = 2;
			}
		} else {
			viseSetova = 1;
			visePitanja = 0;
			if (selektovaniSetovi.size()==1){
				poruka += " isprazniti odabrani set";
			} else {
				viseSetova = 2;
				poruka += " isprazniti odabrane setove";
			}
			if (selektovanaPitanja.size()==0){
				poruka += "?";
			} else {
				if (selektovanaPitanja.size()==1){
					visePitanja = 1;
					poruka += " i obrisati odabrano pitanje?";
				} else {
					visePitanja = 2;
					poruka += " i obrisati odabrana pitanja?";
				}
			}
		}
		final int brSetova = viseSetova;
		final int brPitanja = visePitanja;
		builder.setMessage(poruka)
				.setCancelable(false)
				.setPositiveButton("Da", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						for (PitanjeStat pitanje : vratiSelektovanaPitanja()){
							DatabaseBroker.vratiInstancu().obrisiPitanje(pitanje.getPitanje().getJedinstveniIDikada());
						}
						potpuniRefreshListe();
						String poruka = "Uspešno"; 
						if (brSetova==0){
							if (brPitanja==1){
								poruka += " je obriasno pitanje!";
							} else {
								poruka += " su obriasna pitanja!";
							}
						} else {
							if (brSetova==1){
								poruka += " je ispražnjen set";
							} else {
								poruka += " su ispražnjeni setovi";
							}
							if (selektovanaPitanja.size()==0){
								poruka += "!";
							} else {
								if (selektovanaPitanja.size()==1){
									poruka += " i obrisano pitanje!";
								} else {
									poruka += " i obrisana pitanja!";
								}
							}
						}
						Toast.makeText(getApplicationContext(), poruka, Toast.LENGTH_SHORT).show();
						if (DatabaseBroker.vratiInstancu().vratiSvaPitanja(false).size()==0){
							setResult(-1);
							Toast.makeText(getApplicationContext(), "Nema više pitanja u bazi", Toast.LENGTH_SHORT).show();
						}
					}
				});
		AlertDialog alert = builder.create();
		alert.setButton(android.content.DialogInterface.BUTTON_NEGATIVE, "Ne", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});		

		alert.show();
	}
	
	private void prikaziPromeniSetDijalog(SetPitanja set) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final SetPitanja setFinal = set;
		builder.setMessage("Da li želite da izmenite set?")
				.setCancelable(false)
				.setPositiveButton("Da", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent promeniSet = new Intent(getApplicationContext(),PromeniSetPitanja.class);
						promeniSet.putExtra("set", setFinal);
						startActivityForResult(promeniSet, 1);
					}
				});
		AlertDialog alert = builder.create();
		alert.setButton(android.content.DialogInterface.BUTTON_NEGATIVE, "Ne", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});		
		alert.setButton(android.content.DialogInterface.BUTTON_NEUTRAL, "Obriši", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				potvrdaBrisanjaSetaDijalog(setFinal);
			}
		});	
		alert.show();
	}
	
	private void potvrdaBrisanjaSetaDijalog(SetPitanja set) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final SetPitanja setFinal = set;
		final List<PitanjeStat> pitanjaSeta = DatabaseBroker.vratiInstancu().vratiPitanjaZaSet(setFinal.getAUIDseta());
		String poruka = "Da li ste sigurni da želite potpuno obrisati set";
		switch (pitanjaSeta.size()){
		case 0:
			poruka+="?";
			break;
		case 1:
			poruka+=", zajedno sa njegovim pitanjem?";
		break;
		default:
			poruka+=", zajedno sa njegovim pitanjima?";
			break;
		}
		builder.setMessage(poruka)
				.setCancelable(false)
				.setPositiveButton("Da", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						for (PitanjeStat pist : pitanjaSeta){
							DatabaseBroker.vratiInstancu().obrisiPitanje(pist.getPitanje().getJedinstveniIDikada());
						}
						DatabaseBroker.vratiInstancu().obrisiSet(setFinal.getAUIDseta());
						potpuniRefreshListe();
					}
				});
		AlertDialog alert = builder.create();
		alert.setButton(android.content.DialogInterface.BUTTON_NEGATIVE, "Nazad", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});	
		alert.show();
	}
	
	private void prikaziPomeriDialog(){
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(
				OpcijePitanja.this);
		String poruka = "Odaberite set u koji želite da premestite pitanja:";
		if (selektovanaPitanja.size()==1){
			poruka = "Odaberite set u koji želite da premestite pitanje:";
		}
        builderSingle.setTitle(poruka);
        final ArrayAdapter<SetPitanja> arrayAdapter = new ArrayAdapter<SetPitanja>(
        		OpcijePitanja.this,
                android.R.layout.select_dialog_singlechoice);
        final List<SetPitanja> setovi = DatabaseBroker.vratiInstancu().vratiSveSetove();
        List<String> auidSetova = new ArrayList<String>();
        for (PitanjeStat pist : selektovanaPitanja){
        	if (!auidSetova.contains(pist.getPitanje().getIdSeta())){
        		auidSetova.add(pist.getPitanje().getIdSeta());
        	}
        }
        for (SetPitanja setp : selektovaniSetovi){
        	if (!auidSetova.contains(setp.getAUIDseta())){
        		auidSetova.add(setp.getAUIDseta());
        	}
        }
        if (auidSetova.size()==1){
        	SetPitanja sp = DatabaseBroker.vratiInstancu().vratiSetSaAUID(auidSetova.get(0));
        	setovi.remove(sp);
        }
        for (SetPitanja sp : setovi){
        	arrayAdapter.add(sp);
        }
        builderSingle.setNegativeButton("Nazad",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SetPitanja strName = arrayAdapter.getItem(which);
                        for (PitanjeStat pist : vratiSelektovanaPitanja()){
                        	 DatabaseBroker.vratiInstancu().premestiPitanje(strName.getAUIDseta(), pist.getPitanje().getJedinstveniIDikada());
                        }
                        potpuniRefreshListe();
                        Toast.makeText(getApplicationContext(), "Pitana su uspešno premeštena!", Toast.LENGTH_SHORT).show();
                    }
                });
        builderSingle.show();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode){
			case 11:
				adapter.promenjeniPodaci(DatabaseBroker.vratiInstancu().vratiSveSetove(), DatabaseBroker.vratiInstancu().vratiSetIPitanja());
				setResult(Activity.RESULT_OK);
				break;
			case RESULT_OK:
				adapter.promenjeniPodaci(DatabaseBroker.vratiInstancu().vratiSveSetove(), DatabaseBroker.vratiInstancu().vratiSetIPitanja());
				setResult(Activity.RESULT_OK);
				break;
			default:
				// Ništa menjano
				break;
		}
		selektovani.clear();
		selektovanaPitanja.clear();
		selektovaniSetovi.clear();
		refreshListe();

	}
	
	private void refreshListe(){
		adapter.notifyDataSetChanged();
        for (Integer i : expandovani){
          int in = i.intValue();
       	  lista.expandGroup(in);
        }
	}
	
	private void potpuniRefreshListe(){
		adapter.promenjeniPodaci(DatabaseBroker.vratiInstancu().vratiSveSetove(), DatabaseBroker.vratiInstancu().vratiSetIPitanja());
        selektovani.clear();
		selektovanaPitanja.clear();
		selektovaniSetovi.clear();
        setResult(Activity.RESULT_OK);
		refreshListe();
	}
	
	private List<PitanjeStat> vratiSelektovanaPitanja(){
		List<PitanjeStat> selPit = new ArrayList<PitanjeStat>();
		selPit.addAll(selektovanaPitanja);
		for (SetPitanja setp : selektovaniSetovi){
			selPit.addAll(DatabaseBroker.vratiInstancu().vratiPitanjaZaSet(setp.getAUIDseta()));
		}
		return selPit;
	}
	
	private void handleCekiranje(int decaV, int groupPosition, int childPosition){
		if (!selektovani.containsKey(groupPosition)){
		HashMap<Integer, Boolean> zaStavljanje = new HashMap<Integer, Boolean>();
		zaStavljanje.put(childPosition, true);
		selektovani.put(groupPosition, zaStavljanje);
		} else {
			selektovani.get(groupPosition).put(childPosition, true);
		}
			if (selektovani.get(groupPosition).containsKey(-1)){
				decaV++;
			}
			if (selektovani.get(groupPosition).size()==decaV){
				boolean cekiraj = true;
				for (Integer dete : selektovani.get(groupPosition).keySet()){
					if (!selektovani.get(groupPosition).get(dete)){
						cekiraj=false;
					}
				}
				if (cekiraj){
					SetPitanja spCek = (SetPitanja) adapter.getGroup(groupPosition);
					selektovani.get(groupPosition).remove(-1);
					if (!selektovaniSetovi.contains(spCek)){
						selektovaniSetovi.add(spCek);
					}
					List<PitanjeStat> pitanjaIzCekiranogSeta = new ArrayList<PitanjeStat>();
					for (PitanjeStat pist : selektovanaPitanja){
						if (pist.getPitanje().getIdSeta().equals(spCek.getAUIDseta())){
							pitanjaIzCekiranogSeta.add(pist);
						}
					}
					selektovanaPitanja.removeAll(pitanjaIzCekiranogSeta);
				}
			} else {
				selektovani.get(groupPosition).put(-1, true);
			}
	}

	

}
