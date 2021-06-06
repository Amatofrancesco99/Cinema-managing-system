package cinema.controller.util;

/** BREVE DESCRIZIONE CLASSE NoMovieException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora si cerchi un film, il cui Id non esiste
 */
@SuppressWarnings("serial")
public class NoMovieException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public NoMovieException(int id) {
		System.err.println("Il film con id " + id + " non esiste.");
	}
	
}
