package cinema.model.reservation.discount.coupon;

import java.util.concurrent.atomic.AtomicInteger;


/** BREVE DESCRIZIONE CLASSE Coupon
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Questa classe rappresenta un coupon che può essere applicato per poter effettuare
 * una detrazione del totale della prenotazione, dell'importo definito nel coupon stesso
 */
public class Coupon {
	
	/**
	 * @param number     Numero di coupon, univoco (reso tale grazie al progressivo)
	 * @param discount   Sconto del coupon
	 * @param used		 Lo sconto è già stato utilizzato? (True = sì, False = no )
	 */
	private static final AtomicInteger count = new AtomicInteger(0);
	private final long progressive;
	private double discount;
	private boolean used; 
	
	/**
	 * COSTRUTTORE della classe
	 * @param discount
	 */
	public Coupon (double discount) {
		progressive = count.incrementAndGet(); 
		this.discount = Math.round(discount * 100.0)/100.0;
		used = false;
	}

	/**METODO per farsi dare il progressivo del coupon */
	public long getProgressive() {
		return progressive;
	}

	/**METODO per farsi dire l'ammontare di sconto del coupon */
	public double getDiscount() {
		return discount;
	}
	
	/**METODO per farsi dare se il coupon è usato o meno */
	public boolean isUsed() {
		return used;
	}

	/**METODO per impostare lo stato di un coupon */
	public void setUsed(boolean b) {
		this.used = b;
	}
	
}