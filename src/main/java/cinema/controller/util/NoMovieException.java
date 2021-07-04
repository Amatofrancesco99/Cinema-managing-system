package cinema.controller.util;

/**
 * Lanciata in caso in cui il film non sia valido.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class NoMovieException extends Exception {

	/**
	 * Costruttore dell'eccezione.
	 * 
	 * @param message messaggio contenente i dettagli dell'errore riscontrato.
	 */
	public NoMovieException(String message) {
		super(message);
	}

}
