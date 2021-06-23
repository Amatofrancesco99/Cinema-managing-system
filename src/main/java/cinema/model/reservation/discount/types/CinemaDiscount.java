package cinema.model.reservation.discount.types;

import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.ReservationDiscountStrategy;


/** BREVE DESCRIZIONE CLASSE CompositeDiscount (Pattern Strategy - GoF)
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Questa classe rappresenta una strategia scontistica, implementata dal cinema stesso.
 * Qualora l'amministratore di sistema voglia cambiare strategia basta che cambi il metodo
 * richiamato.
 */
public class CinemaDiscount implements ReservationDiscountStrategy {

	@Override
	public double getTotal(Reservation r) {
		double total = new DiscountAge().getTotal(r);
		return Math.round(total * 100.0)/100.0;
	}

}