package cinema.model.reservation.discount.coupon.util;

/**
 * Eccezione lanciata qualora il Coupon non rispetti i parametri fissati.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class CouponException extends Exception {
	public CouponException(String message) {
		super(message);
	}

}
