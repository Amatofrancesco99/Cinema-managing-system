package cinema.model.reservation.discount.coupon;

import cinema.model.reservation.discount.coupon.util.CouponException;

/**
 * Coupon che può essere applicato ad una prenotazione per poter effettuare una
 * detrazione del relativo importo dal costo totale della prenotazione stessa.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class Coupon {

	/**
	 * Numero minimo di caratteri di ogni coupon.
	 */
	private final int MIN_COUPON_CHARACTERS = 8;

	/**
	 * Codice del coupon.
	 */
	private final String code;

	/**
	 * Sconto da applicare all'importo da pagare.
	 */
	private double discount;

	/**
	 * Stato del coupon (true = coupon già utilizzato, false = coupon ancora da
	 * utilizzare).
	 */
	private boolean used;

	/**
	 * Costruttore del coupon.
	 * 
	 * @param code     codice del coupon.
	 * @param discount sconto da applicare.
	 * @param used     stato del coupon (true = coupon già utilizzato, false =
	 *                 coupon ancora da utilizzare).
	 * @throws CouponException se il coupon ha una lunghezza minore di quella
	 *                         consentita.
	 */
	public Coupon(String code, double discount, boolean used) throws CouponException {
		if (code.length() < MIN_COUPON_CHARACTERS) {
			throw new CouponException(
					"La lunghezza del codice del coupon è inferiore a quella consentita (8 caratteri).");
		}
		this.code = code;
		this.discount = Math.round(discount * 100.0) / 100.0;
		this.used = used;
	}

	public String getCode() {
		return code;
	}

	public double getDiscount() {
		return discount;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	@Override
	public String toString() {
		return String.format("%s %f %s", code, discount, ((Boolean) used).toString());
	}

}
