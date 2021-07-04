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
	 * Età minima sotto la quale si ottiene lo sconto per l'età.
	 */
	private int minAge;

	/**
	 * Età massima sopra la quale si ottiene lo sconto per l'età.
	 */
	private int maxAge;

	/**
	 * Percentuale di sconto.
	 */
	private double percentage;

	/**
	 * Costruttore dello sconto in base all'età.
	 * 
	 * @param minAge     età minima sotto la quale si ottiene lo sconto per l'età.
	 * @param maxAge     età massima sopra la quale si ottiene lo sconto per l'età.
	 * @param percentage percentuale di sconto da applicare al costo totale della
	 *                   prenotazione.
	 */
	public DiscountAge(int minAge, int maxAge, double percentage) {
		super(TypeOfDiscounts.AGE);
		this.minAge = minAge;
		this.maxAge = maxAge;
		this.percentage = percentage;
	}

	/**
	 * Restituisce il costo totale scontato della prenotazione dopo l'applicazione
	 * dello sconto definito dalla strategia corrente.
	 *
	 * Se il numero di persone corrispondenti al prezzo scontato non è compatibile
	 * con quello dei posti riservati nella prenotazione non viene applicato alcuno
	 * sconto.
	 */
	@Override
	public double getTotal(Reservation reservation) {
		int nDiscountedSeats = reservation.getNumberPeopleUntilMinAge() + reservation.getNumberPeopleOverMaxAge();
		double totalPrice = reservation.getProjection().getPrice() * reservation.getNSeats();
		double discount = (reservation.getNumberPeopleUntilMinAge() + reservation.getNumberPeopleOverMaxAge())
				* reservation.getProjection().getPrice() * percentage;
		return totalPrice - ((nDiscountedSeats <= reservation.getNSeats()) ? discount : 0.0);
	}

	/**
	 * Imposta l'età sotto la quale si ottiene lo sconto.
	 * 
	 * @param minAge età minima, sotto la quale si ottiene lo sconto per l'età.
	 * @throws DiscountException se l'età minima inserita è minore o uguale a 0.
	 */
	public void setMinAge(int minAge) throws DiscountException {
		if (minAge >= 0) {
			this.minAge = minAge;
		} else
			throw new DiscountException("L'età minima deve essere maggiore di zero.");
	}

	/**
	 * Imposta l'età sopra la quale si ottiene lo sconto.
	 * 
	 * @param maxAge età massima, sopra la quale si ottiene lo sconto per l'età.
	 * @throws DiscountException se l'età massima inserita è minore o uguale a 0.
	 */
	public void setMaxAge(int maxAge) throws DiscountException {
		if (maxAge >= 0) {
			this.maxAge = maxAge;
		} else
			throw new DiscountException("L'età massima deve essere maggiore di zero.");
	}

	/**
	 * Imposta la percentuale di sconto per una strategia di sconto.
	 * 
	 * @param discount valore dello sconto.
	 * @throws DiscountException se la percentuale di sconto è minore di 0 o
	 *                           maggiore di 1.
	 */
	public void setPercentage(double discount) throws DiscountException {
		if (discount < 0) {
			throw new DiscountException("La percentuale dello sconto deve essere positiva.");
		} else if (discount > 1) {
			throw new DiscountException("La percentuale dello sconto deve essere minore o uguale il 100%");
		}
		this.percentage = discount;
	}

	public int getMinAge() {
		return minAge;
	}

	public int getMaxAge() {
		return maxAge;
	}

	@Override
	public String toString() {
		return "[ " + this.getTypeOfDiscount() + " ]" + "\n" + "Età al di sotto della quale lo sconto è valido: "
				+ minAge + "\n" + "Età al di sopra della quale lo sconto è valido: " + maxAge + "\n"
				+ "Percentuale di sconto applicata: " + percentage;
	}

}
