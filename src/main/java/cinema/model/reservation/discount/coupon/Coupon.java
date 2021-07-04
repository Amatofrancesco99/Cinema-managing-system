package cinema.model.reservation.discount.coupon;

import cinema.model.reservation.discount.coupon.util.CouponException;

/**
 * Rappresenta un coupon che pu� essere applicato per poter effettuare una
 * detrazione dal totale della prenotazione, pari all'importo del coupon stesso.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class Coupon {

	/**
	 * Numero minimo di caratteri del coupon.
	 */
	private final int MIN_COUPON_CHARACTERS = 8;
	/**
	 * Codice coupon.
	 */
	private final String code;
	/**
	 * Sconto da applicare.
	 */
	private double discount;
	/**
	 * Indica se il coupon è gia stato usato o meno.
	 */
	private boolean used;

	/**
	 * Costruttore del coupon.
	 * 
	 * @param code     Codice coupon.
	 * @param discount Sconto da applicare.
	 * @param used     Indica se il coupon è gia stato usato o meno.
	 * @throws CouponException Eccezione lanciata qualora il coupon sia troppo
	 *                         corto.
	 */
	public Coupon(String code, double discount, boolean used) throws CouponException {
		if (code.length() < MIN_COUPON_CHARACTERS) {
			throw new CouponException("Il coupon che si sta cercando di creare è troppo corto (almeno 8 cifre).");
		} else {
			this.code = code;
			this.discount = Math.round(discount * 100.0) / 100.0;
			this.used = used;
		}
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

	public void setUsed(boolean b) {
		this.used = b;
	}

	@Override
	public String toString() {
		return this.getCode() + " " + this.getDiscount() + " " + this.isUsed();
	}
}
