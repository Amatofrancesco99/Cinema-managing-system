package cinema.model;

import java.util.ArrayList; 

import lombok.*;

/** BREVE DESCRIZIONE CLASSE Movie
 * 
 * @author Screaming Hairy Armadillo Team
 *
 *	Questa classe comprende tutti gli attributi che compongono un film
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Movie {

	/** ATTRIBUTI
	 * @param id			Id
	 * @param title			Titolo
	 * @param description	Trama
	 * @param genres		Generi
	 * @param directors		Registi
	 * @param cast			Attori
	 * @param rating		Valutazione (intero da 0 a 5, 5 apprezzato molto dal pubblico)
	 * @param duration		Durata
	 * @param imageURL		Immagine di locandina
	 * @param trailerURL	Link del trailer del film
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
	
	
	/**
	 * METODO per descrivere brevemente le caratteristiche principali di un film
	 * @return caratteristiche
	 */
	public String getDefaultDescription() {
		return "Titolo:\t" + this.getTitle() + "\n"
			 + "Breve trama: " + this.getDescription().substring(0, (this.getDescription().length() <= 240 ? this.getDescription().length() : 240 )).replaceAll("(.{100})", "$1\n             ") 
			 + "...\n"
			 + "Genere/i: " + this.getGenres().toString() + "\n"
			 + "Rating: " + this.getRating() + "\n"
			 + "Durata: " + this.getDuration() + " min.\n\n";
	}
	
	/**
	 * METODO per descrivere tutte le caratteristiche di un film
	 * @return caratteristiche
	 */
	public String getDetailedDescription() {
		return "Titolo:\t" + this.getTitle() + "\n"
			 + "Trama: " + this.getDescription().replaceAll("(.{100})", "$1\n       ") + "\n"
			 + "Genere/i: " + this.getGenres().toString() + "\n"
			 + "Regista/i: " + this.getDirectors().toString() + "\n"
			 + "Cast: " + this.getCast().toString() + "\n"
			 + "Rating: " + this.getRating() + "\n"
			 + "Durata: " + this.getDuration() + " min.\n"
			 + "Trailer (URL): " + this.getTrailerURL() + "\n\n";
	}
	
}