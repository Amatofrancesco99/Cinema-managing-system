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
	 * @param code     Codice del coupon
	 * @param discount Sconto del coupon
	 * @param used     Lo sconto è già stato utilizzato? (True = sì, False = no )
	 */
	private final int MIN_COUPON_CHARACTERS = 8;
	private final String code;
	private double discount;
	private boolean used;

	/**
	 * COSTRUTTORE della classe
	 * 
	 * @param discount
	 * @throws CouponException
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

	/** METODO per farsi dare il progressivo del coupon */
	public String getCode() {
		return code;
	}

	/** METODO per farsi dire l'ammontare di sconto del coupon */
	public double getDiscount() {
		return discount;
	}

	/** METODO per farsi dare se il coupon è usato o meno */
	public boolean isUsed() {
		return used;
	}

	/** METODO per impostare lo stato di un coupon */
	public void setUsed(boolean b) {
		this.used = b;
	}

	@Override
	public String toString() {
		return this.getCode() + " " + this.getDiscount() + " " + this.isUsed();
	}
}