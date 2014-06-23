	package poslovnaLogika;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import domen.Pitanje;
import domen.PitanjeStat;

public class KolekcijaPitanja implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<Pitanje> pitanja;

	public KolekcijaPitanja(List<Pitanje> pitanja) {
		super();
		this.pitanja = pitanja;
	}

	public KolekcijaPitanja(){
		pitanja = new ArrayList<Pitanje>();
	}
	
	
	public List<Pitanje> getPitanja() {
		return pitanja;
	}

	public void DodajPitanje(Pitanje pitanja) {
		this.pitanja.add(pitanja);
	}
	
	public int BrojPitanja() {
		return pitanja.size();
	}
}
