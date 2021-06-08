package cinema.model.reservation.discount.types;

import cinema.model.money.Money;
import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.ReservationDiscountStrategy;


/** BREVE DESCRIZIONE CLASSE DiscountNumberSpectator  (Pattern Strategy)
 * 
 * @author Screaming Hairy Armadillo Team
 *
 *	Questa classe rappresenta una strategia di sconto sulla prenotazione basata sul uno
 *  sconto comitiva, ovvero a seconda di quante persone fanno parte di quella 
 *  specifica prenotazione (si può anche vedere come numero di posti che sono stati occupati).
 */
public class DiscountNumberSpectators implements ReservationDiscountStrategy {

	
	/** ATTRIBUTI
	 * @param NUMBER_PEOPLE 	Numero di persone minimo, al di sopra del quale lo sconto
	 * 							comitiva sarà valido
	 * @param PERCENTAGE  		Percentuale di sconto effettuata
	 */
	private final int NUMBER_PEOPLE = 10;
	private final float PERCENTAGE = (float) 0.85;
	
	
	/**
	 * METODO utilizzato per poter effettuare lo sconto sulla prenotazione e farsi
	 * restituire il nuovo totale, dato lo sconto
	 */
	@Override
	public Money getTotal(Reservation r) {
		float totalPrice = 0;
		if(r.getNSeats() >= NUMBER_PEOPLE){
			totalPrice+=r.getProjection().getPrice().getAmount()*PERCENTAGE*r.getNSeats();
		}
		else 
			totalPrice+=r.getProjection().getPrice().getAmount()*r.getNSeats();
		return new Money(totalPrice,r.getProjection().getPrice().getCurrency());
	}

}
