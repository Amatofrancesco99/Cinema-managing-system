package cinema.model.reservation.discount.types;

import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.types.util.DiscountException;
import cinema.model.reservation.discount.types.util.TypeOfDiscounts;

/**
 * Strategia di sconto sulla prenotazione basata sull'et�degli spettatori che
 * visioneranno il film.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class DiscountAge extends Discount {

	/**
	 * ATTRIBUTI
	 * 
	 * @param MIN_AGE    Età al di sotto della quale lo sconto è valido
	 * @param MAX_AGE    Età al di sopra della quale lo sconto è valido
	 * @param PERCENTAGE Percentuale di sconto effettuata
	 */
	private int min_age;
	private int max_age;
	private double percentage;

	/**
	 * COSTRUTTORE
	 * 
	 * @param type
	 */
	public DiscountAge(int minAge, int maxAge, double percentage) {
		super(TypeOfDiscounts.AGE);
		this.min_age = minAge;
		this.max_age = maxAge;
		this.percentage = percentage;
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
				totalPrice += r.getProjection().getPrice() * r.getNSeats();
				return totalPrice;
			} else {
				totalPrice += r.getProjection().getPrice() * r.getNumberPeopleOverMaxAge() * (1 - percentage);
			}
		} else {
			totalPrice += r.getProjection().getPrice() * r.getNumberPeopleUntilMinAge() * (1 - percentage);
		}
		if (r.getNumberPeopleUntilMinAge() != 0) {
			if (r.getNumberPeopleOverMaxAge() != 0) {
				totalPrice += r.getProjection().getPrice() * r.getNumberPeopleOverMaxAge() * (1 - percentage);
			}
		}
		totalPrice += (r.getNSeats() - r.getNumberPeopleOverMaxAge() - r.getNumberPeopleUntilMinAge())
				* r.getProjection().getPrice();
		return totalPrice;
	}

	/**
	 * METODO per settare l'età minima di validità dello sconto
	 * 
	 * @param min_age
	 * @throws InvalidMinAgeException
	 */
	public void setMin_Age(int min_age) throws DiscountException {
		if (min_age >= 0) {
			this.min_age = min_age;
		} else
			throw new DiscountException("L'età minima deve essere un numero maggior di zero.");
	}

	/**
	 * METODO per settare l'età minima di validità dello sconto
	 * 
	 * @param max_age
	 * @throws InvalidMaxAgeException
	 */
	public void setMax_Age(int max_age) throws DiscountException {
		if (max_age >= 0) {
			this.max_age = max_age;
		} else
			throw new DiscountException("L'età massima deve essere maggiore di zero.");
	}

	/**
	 * METODO per farsi dire l'età al di sotto della quale parte lo sconto per età
	 */
	public int getMin_age() {
		return min_age;
	}

	/**
	 * METODO per farsi dire l'età al di sopra della quale parte lo sconto per età
	 */
	public int getMax_age() {
		return max_age;
	}

	/** METODO per farsi restituire le caratteristiche dello sconto per età */
	@Override
	public String toString() {
		return "[ " + this.getTypeOfDiscount() + " ]" + "\n" + "Età al di sotto della quale lo sconto è valido: "
				+ min_age + "\n" + "Età al di sopra della quale lo sconto è valido: " + max_age + "\n"
				+ "Percentuale di sconto applicata: " + percentage;
	}

	/**
	 * METODO per settare il nuovo sconto
	 * 
	 * @param f Percentuale di sconto da applicare
	 * @throws InvalidPercentageValueException
	 */
	public void setPercentage(double d) throws DiscountException {
		if (d < 0)
			throw new DiscountException("La percentuale dello sconto deve essere positiva.");
		else if (d > 1)
			throw new DiscountException("La percentuale dello sconto deve essere minore o uguale il 100%");
		this.percentage = d;
	}

}