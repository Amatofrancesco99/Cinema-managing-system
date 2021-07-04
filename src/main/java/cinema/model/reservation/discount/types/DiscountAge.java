package cinema.model.reservation.discount.types;

import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.types.util.DiscountException;
import cinema.model.reservation.discount.types.util.TypeOfDiscounts;

/**
 * Strategia di sconto sulla prenotazione basata sull'età degli spettatori che
 * guarderanno il film.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class DiscountAge extends Discount {

	/**
	 * Età minima, sotto la quale si ottiene lo sconto per l'età.
	 */
	private int min_age;
	/**
	 * Età massima, sopra la quale si ottiene lo sconto per l'età.
	 */
	private int max_age;
	/**
	 * Percentuale di sconto.
	 */
	private double percentage;

	/**
	 * Costruttore dello sconto in base all'età.
	 * 
	 * @param minAge     Età minima, sotto la quale si ottiene lo sconto per l'età.
	 * @param maxAge     Età massima, sopra la quale si ottiene lo sconto per l'età.
	 * @param percentage Percentuale di sconto.
	 */
	public DiscountAge(int minAge, int maxAge, double percentage) {
		super(TypeOfDiscounts.AGE);
		this.min_age = minAge;
		this.max_age = maxAge;
		this.percentage = percentage;
	}

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
	 * Imposta l'età sotto la quale si ottiene lo sconto.
	 * 
	 * @param min_age Età minima, sotto la quale si ottiene lo sconto per l'età.
	 * @throws DiscountException Eccezione lanciata qualora l'età minima inserita
	 *                           sia inferiore o uguale a zero.
	 */
	public void setMin_Age(int min_age) throws DiscountException {
		if (min_age >= 0) {
			this.min_age = min_age;
		} else
			throw new DiscountException("L'età minima deve essere un numero maggior di zero.");
	}

	/**
	 * Imposta l'età sopra la quale si ottiene lo sconto.
	 * 
	 * @param max_age Età massima, sopra la quale si ottiene lo sconto per l'età.
	 * @throws DiscountException Eccezione lanciata qualora l'età massima inserita
	 *                           sia inferiore o uguale a zero.
	 */
	public void setMax_Age(int max_age) throws DiscountException {
		if (max_age >= 0) {
			this.max_age = max_age;
		} else
			throw new DiscountException("L'età massima deve essere maggiore di zero.");
	}

	public int getMin_age() {
		return min_age;
	}

	public int getMax_age() {
		return max_age;
	}

	@Override
	public String toString() {
		return "[ " + this.getTypeOfDiscount() + " ]" + "\n" + "Età al di sotto della quale lo sconto è valido: "
				+ min_age + "\n" + "Età al di sopra della quale lo sconto è valido: " + max_age + "\n"
				+ "Percentuale di sconto applicata: " + percentage;
	}

	/**
	 * Imposta il nuovo sconto.
	 * 
	 * @param d Valore dello sconto.
	 * @throws DiscountException Eccezione lanciata qualora la percentuale di sconto
	 *                           sia negativa,o superiore al 100%.
	 */
	public void setPercentage(double d) throws DiscountException {
		if (d < 0)
			throw new DiscountException("La percentuale dello sconto deve essere positiva.");
		else if (d > 1)
			throw new DiscountException("La percentuale dello sconto deve essere minore o uguale il 100%");
		this.percentage = d;
	}

}