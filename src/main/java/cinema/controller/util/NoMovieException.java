package cinema.controller.util;

/**
 * Gestisce le eccezioni, generate dal Cinema, che segnalano la mancanza di uno
 * specifico film.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class NoMovieException extends Exception {

	/**
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public NoMovieException(String message) {
		super(message);
	}

}
