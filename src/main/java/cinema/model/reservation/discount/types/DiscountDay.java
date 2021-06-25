package cinema.model.reservation.discount.types;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map.Entry;

import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.types.util.InvalidPercentageValueException;
import cinema.model.reservation.discount.types.util.TypeOfDiscounts;


/** BREVE DESCRIZIONE CLASSE DiscontDay	 (Pattern Strategy)
 * 
 * @author Screaming Hairy Armadillo Team
 *	
 *  Questa classe rappresenta una strategia di sconto sulla prenotazione basata sul giorno
 *  in cui gli spettatori che visioneranno il film
 */
public class DiscountDay extends Discount{
	
	/** ATTRIBUTI 
	 * 
	 */
	private HashMap<LocalDate, Double> discount = new HashMap<>();
	
	/**
	 * COSTRUTTORE 
	 * @param type
	 */
	public DiscountDay() {
		super(TypeOfDiscounts.DAY);
	}

	
	/**
	 * METODO utilizzato per poter effettuare lo sconto sulla prenotazione e farsi
	 * restituire il nuovo totale, dato lo sconto
	 */
	@Override
	public double getTotal(Reservation r) {
		double totalPrice = 0;
		if (discount.size() > 0) {
			for(Entry<LocalDate, Double> entry : discount.entrySet()) {
			    if(entry.getKey().equals(r.getProjection().getDateTime().toLocalDate())){
		            totalPrice += r.getProjection().getPrice()*(1 - entry.getValue())*r.getNSeats();
		            return totalPrice;
		        }
			}
		}
		totalPrice += r.getProjection().getPrice()*r.getNSeats();
		return totalPrice;
	}
	
	/**
	 * METODO per aggiungere una nuova data con uno sconto
	 * @param d				Giorno di validità dello sconto
	 * @param f				Valore dello sconto
	 * @throws InvalidPercentageValueException 
	 */
	public void addDiscount(LocalDate date, double d) throws InvalidPercentageValueException {
		if ((d <= 0) || (d > 1)){
			throw new InvalidPercentageValueException();
		}
		else discount.put(date, d);
	}
}