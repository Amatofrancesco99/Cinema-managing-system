package cinema.model.reservation.discount.types;

import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.ReservationDiscountStrategy;
import cinema.model.reservation.discount.types.util.InvalidNumberPeopleValueException;
import cinema.model.reservation.discount.types.util.InvalidPercentageValueException;
import lombok.Getter;


/** BREVE DESCRIZIONE CLASSE DiscountNumberSpectator  (Pattern Strategy)
 * 
 * @author Screaming Hairy Armadillo Team
 *
 *	Questa classe rappresenta una strategia di sconto sulla prenotazione basata sul uno
 *  sconto comitiva, ovvero a seconda di quante persone fanno parte di quella 
 *  specifica prenotazione (si può anche vedere come numero di posti che sono stati occupati).
 */
@Getter
public class DiscountNumberSpectators implements ReservationDiscountStrategy {

	
	/** ATTRIBUTI
	 * @param NUMBER_PEOPLE 	Numero di persone minimo, al di sopra del quale lo sconto
	 * 							comitiva sarà valido
	 * @param PERCENTAGE  		Percentuale di sconto effettuata
	 */
	private int numberPeople = 5;
	private double percentage = 0.15;
	
	
	/**
	 * METODO utilizzato per poter effettuare lo sconto sulla prenotazione e farsi
	 * restituire il nuovo totale, dato lo sconto
	 */
	@Override
	public double getTotal(Reservation r) {
		double totalPrice = 0;
		if(r.getNSeats() >= getNumberPeople()){
			totalPrice+=r.getProjection().getPrice()*(1 - percentage)*r.getNSeats();
		}
		else 
			totalPrice+=r.getProjection().getPrice()*r.getNSeats();
		return totalPrice;
	}
	
	/**
	 * METODO per settare il numero di persone da cui parte lo sconto comitiva
	 * @param n			Numero di persone minimo
	 * @throws InvalidNumberPeopleValueException 
	 */
	public void setNumberPeople(int n) throws InvalidNumberPeopleValueException {
		if (n > 0) {
			this.numberPeople = n;
		}
		else throw new InvalidNumberPeopleValueException();
	}
	
	
	/**
	 * METODO per settare il nuovo sconto in base al numero di persone
	 * @param f		      Percentuale di sconto da applicare
	 * @throws InvalidPercentageValueException 
	 */
	public void setPercentage(double d) throws InvalidPercentageValueException {
		if ((d <= 0) || (d >= 1)){
			throw new InvalidPercentageValueException();
		}
		else percentage = d;
	}
}