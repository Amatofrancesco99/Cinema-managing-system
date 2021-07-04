package cinema.controller.util;

/**
 * Lanciata in caso in cui la strategia di sconto richiesta non sia valida.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class DiscountNotFoundException extends Exception {

	/**
	 * Costruttore dell'eccezione.
	 * 
	 * @param message messaggio contenente i dettagli dell'errore riscontrato.
	 */
	public DiscountNotFoundException(String message) {
		super(message);
	}

}
