package poslovnaLogika;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import domen.Pitanje;
import domen.PitanjeStat;
import domen.SetPitanja;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseBroker {
	
	private SQLiteDatabase database;
	private static SQLiteDatabase staticDatabase;
	private DatabseCreator dbHelper;
	private String column = DatabseCreator.COLUMN_ID;

	public DatabaseBroker(Context context) {
		dbHelper = new DatabseCreator(context);
		database = dbHelper.getWritableDatabase();
		staticDatabase = dbHelper.getWritableDatabase();
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public void dodajPitanje(PitanjeStat pitanje) {
		ContentValues values = new ContentValues();
		values.put(DatabseCreator.TEXT_PITANJA, pitanje.getPitanje()
				.getmTextPitanja());
		values.put(DatabseCreator.TACAN_ODGOVOR, pitanje.getPitanje()
				.getOdgovori()[0]);
		values.put(DatabseCreator.PRVI_ODGOVOR, pitanje.getPitanje()
				.getOdgovori()[1]);
		values.put(DatabseCreator.DRUGI_ODGOVOR, pitanje.getPitanje()
				.getOdgovori()[2]);
		values.put(DatabseCreator.TRECI_ODGOVOR, pitanje.getPitanje()
				.getOdgovori()[3]);
		values.put(DatabseCreator.CETVRTI_ODGOVOR, pitanje.getPitanje()
				.getOdgovori()[4]);
		values.put(DatabseCreator.KREATOR, pitanje.getPitanje().getKreator());
		values.put(DatabseCreator.POJASNJENJE, pitanje.getPitanje()
				.getPojasnjenje());
		values.put(DatabseCreator.ALLUNIQUE, pitanje.getPitanje()
				.getJedinstveniIDikada());
		if (pitanje.isAktivno()) {
			values.put(DatabseCreator.AKTIVNO, 1);
		} else {
			values.put(DatabseCreator.AKTIVNO, 0);
		}
		values.put(DatabseCreator.TACNI, pitanje.getBrojTacnihOdgovora());
		values.put(DatabseCreator.NETACNI, pitanje.getBrojNetacnihOdgovora());
		values.put(DatabseCreator.VREMEODGOVORA, pitanje.getVremeZaOdgovor());

		long i = database.insert(DatabseCreator.IME_TABELE, null, values);
		database.close();
	}
	
	//Može da se optimizuje ako se iskopira logika od vratiSvaPitanja i da se u toku kreiranja pitanja izvuče i set
	public HashMap<String, List<PitanjeStat>> vratiSetIPitanja(){
		HashMap<String, List<PitanjeStat>> setIPitanja = new HashMap<String, List<PitanjeStat>>();
		List<PitanjeStat> izvucenaPitanja = vratiSvaPitanja(false);
		for (PitanjeStat pist : izvucenaPitanja){
			String selectQuery = "SELECT * FROM " + DatabseCreator.IME_PRIPADA_TABELE
					+ " WHERE " + DatabseCreator.ID_PITANJA + "=?";
			String jedID =  pist.getPitanje().getJedinstveniIDikada();
			Cursor cursor = database.rawQuery(selectQuery, new String[] { jedID });
			if (cursor.moveToFirst()) {
				String idSeta = cursor.getString(2);
				if (setIPitanja.containsKey(idSeta)){
					List<PitanjeStat> uzetaLista = setIPitanja.get(idSeta);
					uzetaLista.add(pist);
				} else {
					final PitanjeStat pistDummy = pist;
					setIPitanja.put(idSeta, new ArrayList<PitanjeStat>(){{add(pistDummy);}});
				}
			}
			}
		return setIPitanja;
	}

	public List<PitanjeStat> vratiSvaPitanja(boolean samoAktivna) {
		List<PitanjeStat> listaPitanja = new ArrayList<PitanjeStat>();
		String selectQuery = "";
		Cursor cursor = null;
		if (samoAktivna) {
			selectQuery = "SELECT * FROM " + DatabseCreator.IME_TABELE
					+ " WHERE " + DatabseCreator.AKTIVNO + "=?";
			cursor = database.rawQuery(selectQuery, new String[] { "" + 1 });
		} else {
			selectQuery = "SELECT  * FROM " + DatabseCreator.IME_TABELE;
			cursor = database.rawQuery(selectQuery, null);
		}
		if (cursor.moveToFirst()) {
			do {
				Pitanje pit = new Pitanje();
				pit.setmTextPitanja(cursor.getString(1));
				pit.setOdgovori(new String[] { cursor.getString(2),
						cursor.getString(3), cursor.getString(4),
						cursor.getString(5), cursor.getString(6) });
				pit.setKreator(cursor.getString(8));
				pit.setPojasnjenje(cursor.getString(12));
				pit.setNotes(cursor.getString(13));
				pit.setJedinstveniIDikada(cursor.getString(14));
				PitanjeStat pitStat = new PitanjeStat(pit);
				pitStat.setBrojTacnihOdgovora(cursor.getInt(9));
				pitStat.setBrojNetacnihOdgovora(cursor.getInt(10));
				int aktivno = cursor.getInt(7);
				if (aktivno == 0)
					pitStat.setAktivno(false);
				else
					pitStat.setAktivno(true);
				pitStat.setVremeZaOdgovor(cursor.getInt(11));
				listaPitanja.add(pitStat);
			} while (cursor.moveToNext());
		}
		database.close();
		return listaPitanja;
	}

	public boolean updateOdgovor(boolean odgovor, String auid) {
		String odgovorSelekcija = DatabseCreator.NETACNI;
		if (odgovor) {
			odgovorSelekcija = DatabseCreator.TACNI;
		}
		String selectQuery = "SELECT " + odgovorSelekcija + " FROM "
				+ DatabseCreator.IME_TABELE + " WHERE "
				+ DatabseCreator.ALLUNIQUE + "=?";
		Cursor cursor = database.rawQuery(selectQuery, new String[] { auid });
		if (cursor.moveToFirst()) {
			int brojOdgovora = cursor.getInt(0);
			brojOdgovora++;
			ContentValues args = new ContentValues();
			args.put(odgovorSelekcija, brojOdgovora);
			long i = database.update(DatabseCreator.IME_TABELE, args,
					DatabseCreator.ALLUNIQUE + "=?", new String[] { auid });
		} else {
			return false;
		}
		return true;
	}

	public boolean updateAktivno(boolean aktivno, String auid) {
		ContentValues args = new ContentValues();
		if (aktivno) {
			args.put(DatabseCreator.AKTIVNO, 1);
		} else {
			args.put(DatabseCreator.AKTIVNO, 0);
		}
		long i = database.update(DatabseCreator.IME_TABELE, args,
				DatabseCreator.ALLUNIQUE + "=?", new String[] { auid });
		return true;
	}

	public boolean updateVremeZaOdgovor(int vreme, String auid) {
		if (vreme > 10000) {
			return false;
		}
		String selectQuery = "SELECT " + DatabseCreator.VREMEODGOVORA
				+ " FROM " + DatabseCreator.IME_TABELE + " WHERE "
				+ DatabseCreator.ALLUNIQUE + "=?";
		Cursor cursor = database.rawQuery(selectQuery, new String[] { auid });
		cursor.moveToFirst();
		int vremeIzBaze = cursor.getInt(0);
		int prosecnovreme = (vremeIzBaze + vreme) / 2;
		if (vremeIzBaze == 0) {
			prosecnovreme = vreme;
		}
		ContentValues args = new ContentValues();
		args.put(DatabseCreator.VREMEODGOVORA, prosecnovreme);
		long i = database.update(DatabseCreator.IME_TABELE, args,
				DatabseCreator.ALLUNIQUE + "=?", new String[] { auid });
		if (i == 1) {
			return true;
		}
		return false;
	}
	
	public boolean obrisiPitanje(String auid) {
		long i = database.delete(DatabseCreator.IME_TABELE,DatabseCreator.ALLUNIQUE + "=?", new String[] { auid });
		return true;
	}
	
	public boolean resetujStatistiku(String auid){
		ContentValues args = new ContentValues();
		args.put(DatabseCreator.TACNI, 0);
		args.put(DatabseCreator.NETACNI, 0);
		args.put(DatabseCreator.VREMEODGOVORA, 0);
		long i = database.update(DatabseCreator.IME_TABELE, args, DatabseCreator.ALLUNIQUE + "=?", new String[] { auid });
		return true;
	}
	
	public boolean promeniPitanje(PitanjeStat pit, String idStarog){
		ContentValues args = new ContentValues();
		args.put(DatabseCreator.TEXT_PITANJA, pit.getPitanje().getmTextPitanja());
		args.put(DatabseCreator.PRVI_ODGOVOR,  pit.getPitanje().getOdgovori()[1]);
		args.put(DatabseCreator.DRUGI_ODGOVOR, pit.getPitanje().getOdgovori()[2]);
		args.put(DatabseCreator.TRECI_ODGOVOR, pit.getPitanje().getOdgovori()[3]);
		args.put(DatabseCreator.CETVRTI_ODGOVOR, pit.getPitanje().getOdgovori()[4]);
		args.put(DatabseCreator.TACAN_ODGOVOR, pit.getPitanje().getOdgovori()[0]);
		args.put(DatabseCreator.POJASNJENJE, pit.getPitanje().getPojasnjenje());
		args.put(DatabseCreator.KREATOR, pit.getPitanje().getKreator());
		args.put(DatabseCreator.ALLUNIQUE, pit.getPitanje().getJedinstveniIDikada());
		long i = database.update(DatabseCreator.IME_TABELE, args, DatabseCreator.ALLUNIQUE + "=?", new String[] { idStarog });
		return true;
	}
	
	public static void dodajGenericSetPitanja(String ime){
		ContentValues values = new ContentValues();
		values.put(DatabseCreator.IME_SETA, ime);
		values.put(DatabseCreator.IME_AUTORA, Kontroler.vratiObjekat().getAktivniKorisnik());
		values.put(DatabseCreator.IME_DOPRINOSIOCA, "");
		values.put(DatabseCreator.NOTES, "");
		values.put(DatabseCreator.ALLUNIQUE, SetPitanja.generisiAUIDSeta());
		long i = staticDatabase.insert(DatabseCreator.IME_SET_TABELE, null, values);
	}
	
	public List<SetPitanja> vratiSveSetove(){
		List<SetPitanja> sviSetovi = new ArrayList<SetPitanja>();
		String selectQuery = "SELECT * FROM" + DatabseCreator.IME_SET_TABELE;
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				SetPitanja sPit = new SetPitanja();
				sPit.setImeSeta(cursor.getString(1));
				sPit.setImeKreatora(cursor.getString(2));
				sPit.setImeDoprinosioca(cursor.getString(3));
				sPit.setNotes(cursor.getString(4));
				sPit.setAUIDseta(cursor.getString(5));
				sviSetovi.add(sPit);
			} while (cursor.moveToNext());
		}
		return sviSetovi;
	}
	
}
