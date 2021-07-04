package cinema.model.reservation.discount;

import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.types.util.TypeOfDiscounts;

/**
 * Interfaccia che specifica il metodo da implementare in tutte le strategie,
 * che applicano sconti sulla prenotazione. (Pattern Strategy + Pattern
 * Composite)
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public interface IReservationDiscountStrategy {

	/**
	 * Restituisce il totale della prenotazione.
	 * 
	 * @param r prenotazione di cui si calcola il totale.
	 * @return restituisce il totale della prenotazione.
	 */
	public double getTotal(Reservation r);

	public TypeOfDiscounts getTypeOfDiscount();

}