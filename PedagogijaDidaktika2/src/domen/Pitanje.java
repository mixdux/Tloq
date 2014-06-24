package domen;

import java.io.Serializable;



public class Pitanje implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String mTextPitanja;
	private String[] odgovori;
	private String kreator;
	private String pojasnjenje;
	private String notes;
	
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
		return kreator;
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

	
	
	public Pitanje(String mTextPitanja, String[] odgovori, String kreator) {
		super();
		this.mTextPitanja = mTextPitanja;
		this.odgovori = odgovori;
		this.kreator = kreator;
		this.pojasnjenje = "";
		this.notes = "";
	}
	
	public Pitanje(){
		odgovori = new  String[5];
	}
	
	/*public Pitanje(Pitanje pit){
		kreator = pit.kreator;
		mTextPitanja = pit.mTextPitanja;
		odgovoreno = pit.odgovoreno;
		odgovori = pit.odgovori;
	}*/
	
	
}
