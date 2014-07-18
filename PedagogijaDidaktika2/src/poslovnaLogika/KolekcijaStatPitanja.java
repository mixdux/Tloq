package poslovnaLogika;

import java.util.ArrayList;
import java.util.List;

import domen.Pitanje;
import domen.PitanjeStat;

public class KolekcijaStatPitanja {


	private List<PitanjeStat> pitanjaStat;
	
	public KolekcijaStatPitanja(List<PitanjeStat> pitanja) {
		super();
		this.pitanjaStat = pitanja;
	}
	
	public static List<PitanjeStat> generisiStatListu(List<Pitanje> pitanja){
		List<PitanjeStat> kolekcijaStatPitanja = new ArrayList<PitanjeStat>();
		for (Pitanje pi : pitanja){
			kolekcijaStatPitanja.add(new PitanjeStat(pi));
		}
		return kolekcijaStatPitanja;
	}

	public KolekcijaStatPitanja(){
		pitanjaStat = new ArrayList<PitanjeStat>();
	}
	
	
	public List<PitanjeStat> getPitanja() {
		return pitanjaStat;
	}

	public void DodajPitanje(PitanjeStat pitanje) {
		this.pitanjaStat.add(pitanje);
	}
	
	public void izbaciPitanje(Pitanje pitanje) {
		for (PitanjeStat ps : pitanjaStat){
			if (ps.getPitanje().equals(pitanje)){
				pitanjaStat.remove(ps);
				break;
			}
		}
	}
	
	public void izbaciStatPitanje(PitanjeStat pist){
		for (PitanjeStat ps : pitanjaStat){
			if (ps.getPitanje().getJedinstveniIDikada().equals(pist.getPitanje().getJedinstveniIDikada())){
				pitanjaStat.remove(ps);
				break;
			}
		}
	}
	
	public int BrojPitanja() {
		return pitanjaStat.size();
	}
	
}
