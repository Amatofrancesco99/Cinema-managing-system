package cinema.controller.util;

import cinema.model.Movie;

/** BREVE DESCRIZIONE CLASSE MovieNoLongerProjectedException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora il film inserito non abbia proiezioni disponibili
 */
@SuppressWarnings("serial")
public class MovieNoLongerProjectedException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public MovieNoLongerProjectedException(Movie movie) {
		System.out.println("Il film " + movie.getTitle() + " non è più proiettato.");
	}

}