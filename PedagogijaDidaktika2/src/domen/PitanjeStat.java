package domen;

import java.io.Serializable;

public class PitanjeStat implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int brojTacnihOdgovora;
	private int brojNetacnihOdgovora;
	private boolean aktivno;
	private int vremeZaOdgovor;
	private Pitanje pitanje;

	public int getVremeZaOdgovor() {
		return vremeZaOdgovor;
	}

	public void setVremeZaOdgovor(int vremeZaOdgovor) {
		this.vremeZaOdgovor = vremeZaOdgovor;
	}

	public PitanjeStat(Pitanje pitanje) {
		brojTacnihOdgovora = 0;
		brojNetacnihOdgovora = 0;
		aktivno = true;
		vremeZaOdgovor = 0;
		this.pitanje = pitanje;
	}
	
	public PitanjeStat(){
		brojTacnihOdgovora = 0;
		brojNetacnihOdgovora = 0;
		aktivno = true;
		vremeZaOdgovor = 0;
		this.pitanje = new Pitanje();
	}

	public int getBrojTacnihOdgovora() {
		return brojTacnihOdgovora;
	}

	public void setBrojTacnihOdgovora(int brojTacnihOdgovora) {
		this.brojTacnihOdgovora = brojTacnihOdgovora;
	}

	public int getBrojNetacnihOdgovora() {
		return brojNetacnihOdgovora;
	}

	public void setBrojNetacnihOdgovora(int brojNetacnihOdgovora) {
		this.brojNetacnihOdgovora = brojNetacnihOdgovora;
	}

	public boolean isAktivno() {
		return aktivno;
	}

	public void setAktivno(boolean aktivno) {
		this.aktivno = aktivno;
	}

	public Pitanje getPitanje() {
		return pitanje;
	}

	public void setPitanje(Pitanje pitanje) {
		this.pitanje = pitanje;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof PitanjeStat)){
			return false;
		}
		PitanjeStat sp = (PitanjeStat) o;
		return this.pitanje.getJedinstveniIDikada().equals(sp.getPitanje().getJedinstveniIDikada());
	}

}
