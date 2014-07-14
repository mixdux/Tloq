package poslovnaLogika;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import domen.Pitanje;
import domen.PitanjeStat;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseBroker {
	private SQLiteDatabase database;
	private DatabseCreator dbHelper;
	private String column = DatabseCreator.COLUMN_ID;

	public DatabaseBroker(Context context) {
		dbHelper = new DatabseCreator(context);
		database = dbHelper.getWritableDatabase();
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
	
}
