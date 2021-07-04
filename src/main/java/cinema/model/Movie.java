package cinema.model;

import java.util.ArrayList;

/**
 * Comprende tutti gli attributi che compongono un film.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class Movie {

	/**
	 * ATTRIBUTI
	 * 
	 * @param id          Id
	 * @param title       Titolo
	 * @param description Trama
	 * @param genres      Generi
	 * @param directors   Registi
	 * @param cast        Attori
	 * @param rating      Valutazione (intero da 0 a 5, 5 apprezzato molto dal
	 *                    pubblico)
	 * @param duration    Durata
	 * @param imageURL    Immagine di locandina
	 * @param trailerURL  Link del trailer del film
	 */
	private int id;
	private String title;
	private String description;
	private ArrayList<String> genres;
	private ArrayList<String> directors;
	private ArrayList<String> cast;
	private int rating;
	private int duration;
	private String imageURL;
	private String trailerURL;

	/** COSTRUTTORE comprendente tutti gli argomenti */
	public Movie(int id, String title, String description, ArrayList<String> genres, ArrayList<String> directors,
			ArrayList<String> cast, int rating, int duration, String imageURL, String trailerURL) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.genres = genres;
		this.directors = directors;
		this.cast = cast;
		this.rating = rating;
		this.duration = duration;
		this.imageURL = imageURL;
		this.trailerURL = trailerURL;
	}

	/**
	 * METODO per descrivere brevemente le caratteristiche principali di un film
	 * 
	 * @return caratteristiche
	 */
	public String getDefaultDescription() {
		return "Titolo:\t" + this.getTitle() + "\n" + "Breve trama: "
				+ this.getDescription()
						.substring(0, (this.getDescription().length() <= 240 ? this.getDescription().length() : 240))
						.replaceAll("(.{100})", "$1\n             ")
				+ "...\n" + "Genere/i: " + this.getGenres().toString().replaceAll("\\[", "").replaceAll("\\]", "")
				+ "\n" + "Rating: " + this.getRating() + "\n" + "Durata: " + this.getDuration() + " min.\n";
	}

	/**
	 * METODO per descrivere tutte le caratteristiche di un film
	 * 
	 * @return caratteristiche
	 */
	public String getDetailedDescription() {
		return "Titolo:\t" + this.getTitle() + "\n" + "Trama: "
				+ this.getDescription().replaceAll("(.{100})", "$1\n       ") + "\n" + "Genere/i: "
				+ this.getGenres().toString().replaceAll("\\[", "").replaceAll("\\]", "") + "\n" + "Regista/i: "
				+ this.getDirectors().toString().replaceAll("\\[", "").replaceAll("\\]", "") + "\n" + "Cast: "
				+ this.getCast().toString().replaceAll("\\[", "").replaceAll("\\]", "") + "\n" + "Rating: "
				+ this.getRating() + "/5\n" + "Durata: " + this.getDuration() + " min.\n" + "Trailer (URL): "
				+ this.getTrailerURL() + "\n";
	}

	/** METODO per farsi dire l'id di un film */
	public int getId() {
		return id;
	}

	/** METODO per farsi dire il titolo di un film */
	public String getTitle() {
		return title;
	}

	/** METODO per farsi dire la trama di un film */
	public String getDescription() {
		return description;
	}

	/** METODO per farsi dire i generi di un film */
	public ArrayList<String> getGenres() {
		return genres;
	}

	/** METODO per farsi dire i direttori di un film */
	public ArrayList<String> getDirectors() {
		return directors;
	}

	/** METODO per farsi dire il cast di un film */
	public ArrayList<String> getCast() {
		return cast;
	}

	/** METODO per farsi dire il rating di un film */
	public int getRating() {
		return rating;
	}

	/** METODO per farsi dire la durata di un film */
	public int getDuration() {
		return duration;
	}

	/** METODO per farsi dire l'url dell'immagine di copertina del film */
	public String getImageURL() {
		return imageURL;
	}

	/** METODO per farsi dire l'url del trailer del film */
	public String getTrailerURL() {
		return trailerURL;
	}
}