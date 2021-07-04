package cinema.controller.util;

/**
 * Gestisce le eccezioni, generate da Cinema, che segnalano la mancanza di uno
 * specifico sconto.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class DiscountNotFoundException extends Exception {

	/**
	 * Costruttore dell'eccezione.
	 * 
	 * @param message messaggio da riportare.
	 */
	public DiscountNotFoundException(String message) {
		super(message);
	}

}