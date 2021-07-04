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
	 * Id del film.
	 */
	private int id;

	/**
	 * Titolo del film.
	 */
	private String title;

	/**
	 * Descrizione del film.
	 */
	private String description;

	/**
	 * ArrayList contenente i generi del film.
	 */
	private ArrayList<String> genres;

	/**
	 * ArrayList contenente i direttori del film.
	 */
	private ArrayList<String> directors;

	/**
	 * ArrayList contenente gli attori del film.
	 */
	private ArrayList<String> cast;

	/**
	 * Valutazione del film (1-5 stelle).
	 */
	private int rating;

	/**
	 * Durata del film in minuti.
	 */
	private int duration;

	/**
	 * URL della locandina del film.
	 */
	private String imageURL;

	/**
	 * URL del trailer del film.
	 */
	private String trailerURL;

	/**
	 * Costruttore del film.
	 * 
	 * @param id          id del film.
	 * @param title       titolo del film.
	 * @param description descrizione del film.
	 * @param genres      generi del film.
	 * @param directors   direttori del film.
	 * @param cast        cast del film.
	 * @param rating      valutazione del film (1-5 stelle).
	 * @param duration    durata del film in minuti.
	 * @param imageURL    URL della locandina del film (deve puntare a un'immagine
	 *                    all'interno della cartella
	 *                    src/main/resources/static/img/movie-posters).
	 * @param trailerURL  URL del trailer del film (YouTube).
	 */
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
	 * Restituisce una serializzazione breve dei dati del film.
	 * 
	 * @return la descrizione di default del film (rappresentante i suoi dati
	 *         principali).
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
	 * Restituisce una serializzazione dettagliata dei dati del film.
	 * 
	 * @return la descrizione dettagliata del film (rappresentante il dettaglio dei
	 *         suoi dati).
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

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public ArrayList<String> getGenres() {
		return genres;
	}

	public ArrayList<String> getDirectors() {
		return directors;
	}

	public ArrayList<String> getCast() {
		return cast;
	}

	public int getRating() {
		return rating;
	}

	public int getDuration() {
		return duration;
	}

	public String getImageURL() {
		return imageURL;
	}

	public String getTrailerURL() {
		return trailerURL;
	}

}