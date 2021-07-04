package cinema.model.reservation.discount.coupon.util;

/**
 * Lanciata in caso di errori riscontrati nelle procedure di interazione con gli
 * oggetti che rappresentano i coupon.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class CouponException extends Exception {

	/**
	 * Costruttore dell'eccezione.
	 * 
	 * @param message messaggio contenente i dettagli dell'errore riscontrato.
	 */
	public CouponException(String message) {
		super(message);
	}

}
