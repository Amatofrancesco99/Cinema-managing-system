package cinema.test;

public class Film {
	public String titolo, descrizione, urlImmagine, urlTrailer, durata;

	public Film(String titolo) {
		this.titolo = titolo;
		descrizione = "L'incontro tra due mondi apparentemente lontani. Dopo un incidente di parapendio che lo ha reso paraplegico, il ricco aristocratico Philippe assume Driss, ragazzo di periferia appena uscito dalla prigione, come badante personale.";
		urlImmagine = "http://lnx.whipart.it/imagesart9/1382899075-quasi_amici.jpg";
		urlTrailer = "https://www.youtube.com/watch?v=K1buCCC7e48";
		durata = "1h45";
	}

}
