package poslovnaLogika;

import java.util.List;
import java.util.Random;

import android.util.Log;

import domen.Pitanje;
import domen.PitanjeStat;

public class Kontroler {
	private static final String TAG = "PedagogijaSaDidaktikom";

	public static Kontroler kontroler;

	public KolekcijaPitanja kolekcijaPitanja;
	public KolekcijaStatPitanja kolekcijaStatPitanja;

	public Kontroler() {
		kolekcijaPitanja = new KolekcijaPitanja();
		kolekcijaStatPitanja = new KolekcijaStatPitanja();
	}

	public void UcitajPitanja(KolekcijaPitanja kolekcijaPitanja) {
		this.kolekcijaPitanja = kolekcijaPitanja;
	}
	
	public void UcitajStatPitanja(KolekcijaStatPitanja kolekcijaPitanja) {
		this.kolekcijaStatPitanja = kolekcijaPitanja;
	}

	public KolekcijaPitanja getKolekcijaPitanja() {
		return kolekcijaPitanja;
	}
	
	public KolekcijaStatPitanja getKolekcijaStatPitanja() {
		return kolekcijaStatPitanja;
	}

	public static Kontroler vratiObjekat() {
		if (kontroler == null) {
			kontroler = new Kontroler();
		}
		return kontroler;
	}

	public void dodajPitanje(Pitanje p) {
		kolekcijaPitanja.DodajPitanje(p);
	}
	
	public void dodajStatPitanje(PitanjeStat p) {
		kolekcijaStatPitanja.DodajPitanje(p);
	}

	public Pitanje vratiPitanje() throws RuntimeException {
		Random rand = new Random();
		List<Pitanje> nizPitanja = kolekcijaPitanja.getPitanja();
		if (nizPitanja == null) {
			Log.i(TAG, "Nema pitanja u bazi, unesite pitanja");
			throw new RuntimeException("Nema pitanja u bazi, unesite pitanja");
		}
		return nizPitanja.get(3);
	}
}
