package cinema.model.reservation.discount.types;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map.Entry;

import cinema.model.money.Money;
import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.ReservationDiscountStrategy;


/** BREVE DESCRIZIONE CLASSE DiscontDay	 (Pattern Strategy)
 * 
 * @author Screaming Hairy Armadillo Team
 *	
 *  Questa classe rappresenta una strategia di sconto sulla prenotazione basata sul giorno
 *  in cui gli spettatori che visioneranno il film
 */
public class DiscountDay implements ReservationDiscountStrategy{
	
	
	/** ATTRIBUTI 
	 *  @param start		giorno da cui parte lo sconto
	 *  @param end			giorno in cui finisce lo sconto
	 *  @param day			giorno in cui vale lo sconto
	 *  @param PERCENTAGE	percentuale di sconto
	 */
	private HashMap<LocalDate, Double> discount = new HashMap<>();
	
	
	/**
	 * METODO utilizzato per poter effettuare lo sconto sulla prenotazione e farsi
	 * restituire il nuovo totale, dato lo sconto
	 */
	@Override
	public Money getTotal(Reservation r) {
		double totalPrice = 0;
		if (discount.size() > 0) {
			for(Entry<LocalDate, Double> entry : discount.entrySet()) {
			    if(entry.getKey().equals(r.getProjection().getDateTime().toLocalDate())){
		            totalPrice += r.getProjection().getPrice().getAmount()*(1 - entry.getValue())*r.getNSeats();
		            return new Money(totalPrice , r.getProjection().getPrice().getCurrency());
		        }
			}
		}
		totalPrice += r.getProjection().getPrice().getAmount()*r.getNSeats();
		return new Money(totalPrice , r.getProjection().getPrice().getCurrency());
	}
	
	/**
	 * METODO per aggiungere una nuova data con uno sconto
	 * @param d				Giorno di validità dello sconto
	 * @param f				Valore dello sconto
	 * @return boolean 		True = aggiunto, False = non aggiunto
	 */
	public boolean addDiscount(LocalDate date, double d) {
		if ((d <= 0) || (d > 1)){
			return false;
		}
		discount.put(date, d);
		return true;
	}
}