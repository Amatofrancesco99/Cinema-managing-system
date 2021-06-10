package cinema.model.reservation.discount.coupon.util;


/** BREVE DESCRIZIONE CLASSE CouponNotExistsException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora si cerchi un coupon per progressivo, esso non venga trovato
 */
@SuppressWarnings("serial")
public class CouponNotExistsException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public CouponNotExistsException(long progressive) {
		System.err.println("Il coupon " + progressive + " non esiste.");
	}

}