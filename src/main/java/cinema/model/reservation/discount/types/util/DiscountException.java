package cinema.model.reservation.discount.types.util;

/**
 * Eccezione lanciata qualora via sia un problema nell'utilizzo degli sconti.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class DiscountException extends Exception {

	/**
	 * Costruttore dell'eccezione.
	 * 
	 * @param message Messaggio da riportare.
	 */
	public DiscountException(String message) {
		super(message);
	}
}
