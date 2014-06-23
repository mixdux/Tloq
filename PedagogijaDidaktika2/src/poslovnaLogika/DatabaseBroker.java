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

	public void dodajPitanje(Pitanje pitanje) {
		ContentValues values = new ContentValues();
		values.put(DatabseCreator.TEXT_PITANJA, pitanje.getmTextPitanja());
		values.put(DatabseCreator.TACAN_ODGOVOR, pitanje.getOdgovori()[0]);
		values.put(DatabseCreator.PRVI_ODGOVOR, pitanje.getOdgovori()[1]);
		values.put(DatabseCreator.DRUGI_ODGOVOR, pitanje.getOdgovori()[2]);
		values.put(DatabseCreator.TRECI_ODGOVOR, pitanje.getOdgovori()[3]);
		values.put(DatabseCreator.CETVRTI_ODGOVOR, pitanje.getOdgovori()[4]);
		values.put(DatabseCreator.KREATOR, pitanje.getKreator());

		database.insert(DatabseCreator.IME_TABELE, null, values);
		/*
		 * int odgovoren = 0; if (pitanje.isOdgovoreno()) odgovoren = 1; String
		 * SQLQuerry = "INSERT INTO "+ DatabseCreator.IME_TABELE + " (" +
		 * DatabseCreator.TEXT_PITANJA + "," + DatabseCreator.TACAN_ODGOVOR +
		 * "," + DatabseCreator.PRVI_ODGOVOR + "," +
		 * DatabseCreator.DRUGI_ODGOVOR + "," + DatabseCreator.TRECI_ODGOVOR +
		 * "," + DatabseCreator.CETVRTI_ODGOVOR + "," +
		 * DatabseCreator.ODGOVORENO +") " + " VALUES" + " ('" +
		 * pitanje.getmTextPitanja() + "','" + pitanje.getOdgovori()[0] + "','"
		 * + pitanje.getOdgovori()[1] + "','" + pitanje.getOdgovori()[2]+ "','"
		 * + pitanje.getOdgovori()[3] + "','" + pitanje.getOdgovori()[4] + "',"
		 * + odgovoren +");"; database.insert(DatabseCreator.IME_TABELE, null,
		 * values)
		 */
		// database.execSQL(SQLQuerry);
		database.close(); // Closing database connection
	}

	public List<PitanjeStat> vratiSvaPitanja() {
		List<PitanjeStat> listaPitanja = new ArrayList<PitanjeStat>();
		String selectQuery = "SELECT  * FROM " + DatabseCreator.IME_TABELE;
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Pitanje pit = new Pitanje();
				pit.setmTextPitanja(cursor.getString(1));
				pit.setOdgovori(new String[] { cursor.getString(2),
						cursor.getString(3), cursor.getString(4),
						cursor.getString(5), cursor.getString(6) });
				pit.setKreator(cursor.getString(8));
				PitanjeStat pitStat = new PitanjeStat(pit);
				pitStat.setBrojTacnihOdgovora(cursor.getInt(9));
				pitStat.setBrojNetacnihOdgovora(cursor.getInt(10));
				int aktivno = cursor.getInt(7);
				if (aktivno == 0)
					pitStat.setAktivno(false);
				else
					pitStat.setAktivno(true);
				pitStat.setVremeZaOdgovor(cursor.getInt(11));
				// listaPitanja.add(pit);

			} while (cursor.moveToNext());
		}
		return listaPitanja;
	}

}
