package cinema.model.reservation.discount.types;

import cinema.model.money.Money;
import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.ReservationDiscountStrategy;

/** BREVE DESCRIZIONE CLASSE DiscountAge  (Pattern Strategy)
 * 
 * @author Screaming Hairy Armadillo Team
 *
 *	Questa classe rappresenta una strategia di sconto sulla prenotazione basata sull'età
 *  degli spettatori che visioneranno il film
 */
public class DiscountAge implements ReservationDiscountStrategy{

	/** ATTRIBUTI
	 * @param MIN_AGE	  Età al di sotto della quale lo sconto è valido
	 * @param MAX_AGE	  Età al di sopra della quale lo sconto è valido
	 * @param PERCENTAGE  Percentuale di sconto effettuata
	 */
	private final int MIN_AGE = 5;
	private final int MAX_AGE = 80;
	private final float PERCENTAGE = (float) 0.85;
	
	/**
	 * METODO utilizzato per poter effettuare lo sconto sulla prenotazione e farsi
	 * restituire il nuovo totale, dato lo sconto
	 */
	@Override
	public Money getTotal(Reservation r) {
		float totalPrice = 0;
			if((r.getPurchaser().getAge()<=MIN_AGE)||(r.getPurchaser().getAge()>=MAX_AGE)){
				totalPrice+=r.getProjection().getPrice().getAmount()*PERCENTAGE;
			}
			else totalPrice+=r.getProjection().getPrice().getAmount();
		return new Money(totalPrice,r.getProjection().getPrice().getCurrency());
	}

}