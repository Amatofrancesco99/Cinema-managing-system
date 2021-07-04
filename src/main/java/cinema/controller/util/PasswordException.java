package cinema.controller.util;

/**
 * Lanciata in caso in cui la password inserita non rispetti il requisito di
 * validità.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class PasswordException extends Exception {

	/**
	 * Costruttore dell'eccezione.
	 * 
	 * @param message messaggio contenente i dettagli dell'errore riscontrato.
	 */
	public PasswordException(String message) {
		super(message);
	}

}
