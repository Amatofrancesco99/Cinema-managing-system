package cinema.model.reservation.discount.types;

import cinema.model.money.Money;
import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.ReservationDiscountStrategy;
import lombok.Data;


/** BREVE DESCRIZIONE CLASSE DiscountNumberSpectator  (Pattern Strategy)
 * 
 * @author Screaming Hairy Armadillo Team
 *
 *	Questa classe rappresenta una strategia di sconto sulla prenotazione basata sul uno
 *  sconto comitiva, ovvero a seconda di quante persone fanno parte di quella 
 *  specifica prenotazione (si può anche vedere come numero di posti che sono stati occupati).
 */
@Data
public class DiscountNumberSpectators implements ReservationDiscountStrategy {

	
	/** ATTRIBUTI
	 * @param NUMBER_PEOPLE 	Numero di persone minimo, al di sopra del quale lo sconto
	 * 							comitiva sarà valido
	 * @param PERCENTAGE  		Percentuale di sconto effettuata
	 */
	private int numberPeople = 5;
	private float percentage = (float) 0.15;
	
	
	/**
	 * METODO utilizzato per poter effettuare lo sconto sulla prenotazione e farsi
	 * restituire il nuovo totale, dato lo sconto
	 */
	@Override
	public Money getTotal(Reservation r) {
		float totalPrice = 0;
		if(r.getNSeats() >= getNumberPeople()){
			totalPrice+=r.getProjection().getPrice().getAmount()*(1 - percentage)*r.getNSeats();
		}
		else 
			totalPrice+=r.getProjection().getPrice().getAmount()*r.getNSeats();
		return new Money(totalPrice,r.getProjection().getPrice().getCurrency());
	}
	
	
	/**
	 * METODO per settare il nuovo sconto in base al numero di persone
	 * @param f		      Percentuale di sconto da applicare
	 * @return boolean	  Esito assegnazione percentuale di sconto
	 */
	public boolean setPercentage(float f) {
		if ((f <= 0f) || (f >= 1f)){
			return false;
		}
		percentage = f;
		return true;
	}
}