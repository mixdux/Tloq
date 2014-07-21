package poslovnaLogika;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabseCreator extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 2;
	public static final String DATABASE_NAME = "PitanjaDB";
	
	public static final String COLUMN_ID = "_id";
	public static final String NOTES= "notes";
	public static final String ALLUNIQUE= "auid";
	
	public static final String IME_TABELE = "pitanja";
	public static final String TEXT_PITANJA = "tekst_pitanja";
	public static final String PRVI_ODGOVOR = "odgovor_1";
	public static final String DRUGI_ODGOVOR = "odgovor_2";
	public static final String TRECI_ODGOVOR = "odgovor_3";
	public static final String CETVRTI_ODGOVOR = "odgovor_4";
	public static final String TACAN_ODGOVOR = "tacan_odgovor";
	public static final String AKTIVNO= "aktivno";
	public static final String KREATOR= "kreator";
	public static final String TACNI= "broj_tacnih_odgovora";
	public static final String NETACNI= "broj_netacnih_odgovora";
	public static final String VREMEODGOVORA= "vreme_za_odgovor";
	public static final String POJASNJENJE= "dodatne_informacije";
	
	public static final String IME_SET_TABELE = "setPitanja";
	public static final String IME_SETA = "ime_seta";
	public static final String IME_AUTORA = "ime_autora";
	public static final String IME_DOPRINOSIOCA = "ime_dopr";
	
	public static final String IME_PRIPADA_TABELE = "pitanjePripada";
	public static final String ID_PITANJA = "id_pitanje";
	public static final String ID_SETA = "id_set";
	
	private static final String CREATE_PITANJA = "CREATE TABLE "
			+ IME_TABELE + "( "
/*0*/		+ COLUMN_ID + " integer primary key autoincrement,"
/*1*/		+ TEXT_PITANJA +" TEXT, "
/*2*/		+ TACAN_ODGOVOR +" TEXT, "
			+ PRVI_ODGOVOR +" TEXT, "
			+ DRUGI_ODGOVOR +" TEXT, "
			+ TRECI_ODGOVOR +" TEXT, "
			+ CETVRTI_ODGOVOR+ " TEXT, "
/*7*/		+ AKTIVNO + " INTEGER, "
			+ KREATOR + " TEXT, "
			+ TACNI + " INTEGER, "
/*10*/		+ NETACNI + " INTEGER, "
			+ VREMEODGOVORA + " INTEGER, "
			+ POJASNJENJE + " TEXT, "
/*13*/		+ NOTES + " TEXT, "
			+ ALLUNIQUE + " TEXT, "
			+ ID_SETA + " TEXT"
			+ ");";

	private static final String CREATE_SET = "CREATE TABLE "
			+ IME_SET_TABELE + "( "
/*0*/		+ COLUMN_ID + " integer primary key autoincrement,"
/*1*/		+ IME_SETA +" TEXT, "
/*2*/		+ IME_AUTORA +" TEXT, "
			+ IME_DOPRINOSIOCA +" TEXT, "
			+ NOTES +" TEXT, "
/*5*/		+ ALLUNIQUE +" TEXT "
			+ ");";
	
	private static final String CREATE_PRIPADA = "CREATE TABLE "
			+ IME_PRIPADA_TABELE + "( "
/*0*/		+ COLUMN_ID + " integer primary key autoincrement,"
/*1*/		+ ID_PITANJA +" TEXT, "
			+ ID_SETA +" TEXT "
			+ ");";
	
	DatabseCreator(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PITANJA);
		db.execSQL(CREATE_SET);
		//db.execSQL(CREATE_PRIPADA);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
}
