package cinema.model.reservation.discount.types;

import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.types.util.InvalidAgeException;
import cinema.model.reservation.discount.types.util.InvalidPercentageValueException;
import cinema.model.reservation.discount.types.util.TypeOfDiscounts;

/** BREVE DESCRIZIONE CLASSE DiscountAge  (Pattern Strategy)
 * 
 * @author Screaming Hairy Armadillo Team
 *
 *	Questa classe rappresenta una strategia di sconto sulla prenotazione basata sull'età
 *  degli spettatori che visioneranno il film
 */
public class DiscountAge extends Discount{


	/** ATTRIBUTI
	 * @param MIN_AGE	  Età al di sotto della quale lo sconto è valido
	 * @param MAX_AGE	  Età al di sopra della quale lo sconto è valido
	 * @param PERCENTAGE  Percentuale di sconto effettuata
	 */
	private int min_age = 5;
	private int max_age = 80;
	private double percentage = 0.15;
	
	
	/**
	 * COSTRUTTORE 
	 * @param type
	 */
	public DiscountAge() {
		super(TypeOfDiscounts.AGE);
	}

	
	/**
	 * METODO utilizzato per poter effettuare lo sconto sulla prenotazione e farsi
	 * restituire il nuovo totale, dato lo sconto
	 */
	@Override
	public double getTotal(Reservation r) {
		double totalPrice = 0;
		if (r.getNumberPeopleUntilMinAge() == 0) {
			if (r.getNumberPeopleOverMaxAge() == 0) {
				totalPrice += r.getProjection().getPrice()*r.getNSeats();
				return totalPrice;
			}
			else {
				totalPrice += r.getProjection().getPrice()*r.getNumberPeopleOverMaxAge()*(1 - percentage);
			}
		}
		else {
			totalPrice += r.getProjection().getPrice()*r.getNumberPeopleUntilMinAge()*(1 - percentage);
		}
		if (r.getNumberPeopleUntilMinAge() != 0) {
			if (r.getNumberPeopleOverMaxAge() != 0) {
				totalPrice += r.getProjection().getPrice()*r.getNumberPeopleOverMaxAge()*(1 - percentage);
			}
		}
		totalPrice += (r.getNSeats() - r.getNumberPeopleOverMaxAge() - r.getNumberPeopleUntilMinAge()) * r.getProjection().getPrice();
		return totalPrice;
	}
	
	
	/**
	 * METODO per settare l'età minima di validità dello sconto
	 * @param min_age
	 * @throws InvalidMinAgeException 
	 */
	public void setMin_Age(int min_age) throws InvalidAgeException {
		if (min_age >= 0) {
			this.min_age = min_age;
		}
		else throw new InvalidAgeException();
	}
	
	/**
	 * METODO per settare l'età minima di validità dello sconto
	 * @param max_age
	 * @throws InvalidMaxAgeException 
	 */
	public void setMax_Age(int max_age) throws InvalidAgeException {
		if (max_age >= 0) {
			this.max_age = max_age;
		}
		else throw new InvalidAgeException();
	}
	
	
	/**
	 * METODO per settare il nuovo sconto 
	 * @param f		      Percentuale di sconto da applicare
	 * @throws InvalidPercentageValueException 
	 */
	public void setPercentage(double d) throws InvalidPercentageValueException {
		if ((d <= 0) || (d >= 1)){
			throw new InvalidPercentageValueException();
		}
		else percentage = d;
	}

	
	/**METODO per farsi dire l'età al di sotto della quale parte lo sconto per età */
	public int getMin_age() {
		return min_age;
	}

	
	/**METODO per farsi dire l'età al di sopra della quale parte lo sconto per età */
	public int getMax_age() {
		return max_age;
	}
}