package cinema.model.reservation.discount.types.util;

/**
 * Lanciata in caso di errori riscontrati nelle procedure di interazione con gli
 * oggetti che rappresentano i gli sconti.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class DiscountException extends Exception {

	/**
	 * Costruttore dell'eccezione.
	 * 
	 * @param message messaggio contenente i dettagli dell'errore riscontrato.
	 */
	public DiscountException(String message) {
		super(message);
	}

}
