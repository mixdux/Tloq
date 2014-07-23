package domen;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import poslovnaLogika.Kontroler;

public class SetPitanja implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String AUIDseta;
	private String imeKreatora;
	private String ImeSeta;
	private String imeDoprinosioca;
	private String notes;
	
	public String getAUIDseta() {
		return AUIDseta;
	}
	public void setAUIDseta(String aUIDseta) {
		AUIDseta = aUIDseta;
	}
	public String getImeKreatora() {
		return imeKreatora;
	}
	public void setImeKreatora(String imeKreatora) {
		this.imeKreatora = imeKreatora;
	}
	public String getImeSeta() {
		return ImeSeta;
	}
	public void setImeSeta(String imeSeta) {
		ImeSeta = imeSeta;
	}
	
	public String getImeDoprinosioca() {
		return imeDoprinosioca;
	}
	
	public void setImeDoprinosioca(String imeDoprinosioca) {
		this.imeDoprinosioca = imeDoprinosioca;
	}
		
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public SetPitanja() {
		super();
	}
	
	public SetPitanja(String aUIDseta, String imeKreatora, String imeSeta) {
		super();
		AUIDseta = aUIDseta;
		this.imeKreatora = imeKreatora;
		ImeSeta = imeSeta;
	}

	public static String generisiAUIDSeta(){
			String auid = "";
			auid += new SimpleDateFormat("ddMMyyyy").format(new Date()) + "";
			auid += Pitanje.dajSekundeOdPocetkaDana() + "-";
			auid += Kontroler.vratiObjekat().getAktivniKorisnik();
			Random rand = new Random();
			auid += "-" + (10000 + rand.nextInt(86400-10000));
			return auid;
	}
	
	@Override
	public String toString() {
		return ImeSeta;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SetPitanja)){
			return false;
		}
		SetPitanja sp = (SetPitanja) o;
		return this.AUIDseta.equals(sp.AUIDseta);
	}
	
}
