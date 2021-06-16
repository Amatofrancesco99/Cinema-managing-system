package cinema.model.reservation.discount.coupon.util;


/** BREVE DESCRIZIONE CLASSE CouponNotExistsException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora si inserisca nella prenotazione un coupon già utilizzato
 */
@SuppressWarnings("serial")
public class CouponAleadyUsedException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public CouponAleadyUsedException(long progressive) {
		 System.err.println("Il coupon " + progressive + " è già stato utilizzato.");
	}

}