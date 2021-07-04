package cinema.model.reservation.discount.coupon.util;

/**
 * Eccezione lanciata qualora il Coupon non rispetti i parametri fissati.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class CouponException extends Exception {

	/**
	 * Costruttore dell'eccezione.
	 * 
	 * @param message Messaggio da riportare.
	 */
	public CouponException(String message) {
		super(message);
	}

}
