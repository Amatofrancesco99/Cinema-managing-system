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
	// private int minAgeSee;  
	/* oppure si potrebbe ricavare l'età da cui guardare
	* il film dall'attributo genere, anche se forse è troppo "generico" (non tutti i film
	* di un genere possono essere visti da minori e non è detto che un film con quel genere
	* non possa essere visto da minori)*/
	
	
	/**
	 * METODO per stampare le caratteristiche principali della classe
	 * @return caratteristiche
	 */
	@Override
	public String toString() {
		return "Titolo:\t" + this.getTitle() + "\n"
			 + "Descrizione: " + this.getDescription().substring(0, 80) + "\n"
			 + "\t    " + this.getDescription().substring(80, 150) + "...\n"
			 + "Genere/i: " + this.getGenres().toString() + "\n"
			 + "Rating: " + this.getRating() + "\n"
			 + "Durata: " + this.getDuration() + " min.\n\n";
	}
	
}