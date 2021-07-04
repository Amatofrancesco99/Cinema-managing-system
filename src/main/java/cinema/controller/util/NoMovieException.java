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
	 * Costruttore dell'eccezione.
	 * 
	 * @param message messaggio da riportare.
	 */
	public NoMovieException(String message) {
		super(message);
	}

}
