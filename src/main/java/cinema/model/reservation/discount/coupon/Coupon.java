package cinema.model.reservation.discount.coupon;

import java.util.concurrent.atomic.AtomicInteger;

import cinema.model.money.Money;
import lombok.*;


/** BREVE DESCRIZIONE CLASSE Coupon
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Questa classe rappresenta un coupon che può essere applicato per poter effettuare
 * una detrazione del totale della prenotazione, dell'importo definito nel coupon stesso
 */
@Data
public class Coupon {
	
	/**
	 * @param number     Numero di coupon, univoco (reso tale grazie al progressivo)
	 * @param discount   Sconto del coupon
	 */
	private static final AtomicInteger count = new AtomicInteger(0);
	private final long progressive;
	private Money discount;
	
	public Coupon (Money discount) {
		progressive = count.incrementAndGet(); 
		this.discount = discount;
	}
	
}