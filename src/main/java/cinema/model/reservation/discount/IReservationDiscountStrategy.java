package cinema.model.reservation.discount;

import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.types.util.TypeOfDiscount;

/**
 * Specifica l'interfaccia minima implementata in ogni strategia per
 * l'applicazione di uno sconto sul costo totale di una determinata
 * prenotazione.
 *
 * Ogni strategia di sconto possiede un id univoco.
 *
 * @author Screaming Hairy Armadillo Team
 *
 */
public interface IReservationDiscountStrategy {

	/**
	 * Restituisce il costo totale della prenotazione una volta applicato lo sconto
	 * relativo alla strategia corrente.
	 * 
	 * @param reservation prenotazione a cui si vuole applicare lo sconto.
	 * @return il costo totale scontato della prenotazione.
	 */
	public double getTotal(Reservation reservation);

	/**
	 * Restituisce il tipo della strategia di sconto.
	 *
	 * @return il tipo della strategia di sconto.
	 */
	public TypeOfDiscount getTypeOfDiscount();

	/**
	 * Restituisce l'id della strategia di sconto.
	 *
	 * @return l'id della strategia di sconto.
	 */
	public int getDiscountId();

}
