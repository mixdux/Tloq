package domen;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class Pitanje implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String mTextPitanja;
	private String[] odgovori;
	private String kreator;
	private String pojasnjenje;
	private String notes;
	private String jedinstveniIDikada;
	
	public void setJedinstveniIDikada(String jedinstveniIDikada) {
		if (this.jedinstveniIDikada.equals("")){
		this.jedinstveniIDikada = jedinstveniIDikada;
		}
	}

	public String getJedinstveniIDikada() {
		return jedinstveniIDikada;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getPojasnjenje() {
		return pojasnjenje;
	}

	public void setPojasnjenje(String pojasnjenje) {
		this.pojasnjenje = pojasnjenje;
	}

	public String getKreator() {
		return userFriendlyKreator(kreator);
	}

	public void setKreator(String kreator) {
		this.kreator = kreator;
	}

	public String getmTextPitanja() {
		return mTextPitanja;
	}

	public void setmTextPitanja(String mTextPitanja) {
		this.mTextPitanja = mTextPitanja;
	}

	public String[] getOdgovori() {
		return odgovori;
	}

	public void setOdgovori(String[] odgovori) {
		this.odgovori = odgovori;
	}

	
	
	public Pitanje(String mTextPitanja, String[] odgovori, String kreator, String auid) {
		super();
		this.mTextPitanja = mTextPitanja;
		this.odgovori = odgovori;
		this.kreator = kreator;
		this.pojasnjenje = "";
		this.notes = "";
		jedinstveniIDikada = auid;
	}
	
	public Pitanje(){
		odgovori = new  String[5];
		jedinstveniIDikada = "";
	}
	
	public static String userFriendlyKreator(String kreator){
		String [] kreatorUF = kreator.split("<dsc>(.+?)</dsc>");
		String kuf = "";
		for (String kre : kreatorUF){
			kuf+=kre;
		}
		return kuf;
	}
	
	public static String napraviAUID() {
		String auid = "";
		auid += new SimpleDateFormat("ddMMyyyy").format(new Date()) + "";
		auid += dajSekundeOdPocetkaDana() + "-";
		auid += java.util.UUID.randomUUID();
		return auid;
	}
	
	public static long dajSekundeOdPocetkaDana(){
		Calendar c = Calendar.getInstance();
		long now = c.getTimeInMillis();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		long passed = now - c.getTimeInMillis();
		long secondsPassed = passed / 1000;
		return secondsPassed;
	}
	
}
