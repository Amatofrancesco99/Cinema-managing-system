package cinema.model.reservation.discount;

import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.types.util.TypeOfDiscounts;

/**
 * Interfaccia che specifica il metodo da implementare in tutte le strategie,
 * che applicano sconti sulla prenotazione.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 *         (Pattern Strategy + Pattern Composite)
 */
public interface IReservationDiscountStrategy {

	/**
	 * METODO per farsi restituire il totale di una prenotazione, dopo che è stato
	 * verificato se si può calcolare dello sconto su di essa.
	 * 
	 * @param r Prenotazione a cui si vuole verificare se la strategia di sconto è
	 *          applicabile
	 * @return Money Ritorno del prezzo totale scontato
	 */
	public double getTotal(Reservation r);

	public int getDiscountId();

	public TypeOfDiscounts getTypeOfDiscount();

}