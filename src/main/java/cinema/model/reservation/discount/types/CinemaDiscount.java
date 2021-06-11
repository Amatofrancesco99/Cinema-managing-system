package cinema.model.reservation.discount.types;

import cinema.model.money.Money;
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
	public Money getTotal(Reservation r) {
		Money total = new DiscountAge().getTotal(r);
		return total;
	}

}