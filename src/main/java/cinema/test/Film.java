package cinema.test;

import java.util.ArrayList;

public class Film {
	public int id, rating;
	public String titolo, descrizione, urlImmagine, urlTrailer, durata;
	public ArrayList<String> regia, attori;

	public Film(String titolo, int rating) {
		this.titolo = titolo;
		id = 281688;
		this.rating = rating;
		descrizione = "L'incontro tra due mondi apparentemente lontani. Dopo un incidente di parapendio che lo ha reso paraplegico, il ricco aristocratico Philippe assume Driss, ragazzo di periferia appena uscito dalla prigione, come badante personale.";
		urlImmagine = "http://lnx.whipart.it/imagesart9/1382899075-quasi_amici.jpg";
		urlTrailer = "https://www.youtube.com/watch?v=K1buCCC7e48";
		durata = "1h45";
		regia = new ArrayList<String>();
		regia.add("Olivier Nakache");
		regia.add("Éric Toledano");
		attori = new ArrayList<String>();
		attori.add("François Cluzet");
		attori.add("Omar Sy");
		attori.add("Anne Le Ny");
	}

}
