package cinema.model.reservation.discount.types;

import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.types.util.DiscountException;
import cinema.model.reservation.discount.types.util.TypeOfDiscount;

/**
 * Strategia di sconto sulla prenotazione basata sull'età degli spettatori che
 * effettuano la prenotazione per una data proiezione.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class DiscountAge extends Discount {

	/**
	 * Età minima sotto la quale si ottiene lo sconto per età.
	 */
	private int minAge;

	/**
	 * Età massima sopra la quale si ottiene lo sconto per età.
	 */
	private int maxAge;

	/**
	 * Percentuale di sconto applicato al costo totale della prenotazione.
	 */
	private double percentage;

	/**
	 * Costruttore dello sconto in base all'età.
	 * 
	 * @param minAge     età minima sotto la quale si ottiene lo sconto per età.
	 * @param maxAge     età massima sopra la quale si ottiene lo sconto per età.
	 * @param percentage percentuale di sconto da applicare al costo totale della
	 *                   prenotazione.
	 * @param id         id della strategia di sconto.
	 */
	public DiscountAge(int minAge, int maxAge, double percentage, int id) {
		super(TypeOfDiscount.AGE, id);
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
	 * Imposta l'età sotto la quale si ottiene lo sconto per età.
	 * 
	 * @param minAge età minima sotto la quale si ottiene lo sconto per età.
	 * @throws DiscountException se l'età minima inserita è minore di zero.
	 */
	public void setMinAge(int minAge) throws DiscountException {
		if (minAge < 0) {
			throw new DiscountException("L'età minima deve essere maggiore o uguale a zero.");
		}
		this.minAge = minAge;
	}

	/**
	 * Imposta l'età sopra la quale si ottiene lo sconto per età.
	 * 
	 * @param maxAge età massima sopra la quale si ottiene lo sconto per l'età.
	 * @throws DiscountException se l'età massima inserita è minore di zero.
	 */
	public void setMaxAge(int maxAge) throws DiscountException {
		if (maxAge < 0) {
			throw new DiscountException("L'età massima deve essere maggiore o uguale a zero.");
		}
		this.maxAge = maxAge;
	}

	/**
	 * Imposta la percentuale di sconto per la strategia di sconto corrente.
	 * 
	 * @param percentage percentuale di sconto da applicare al costo totale della
	 *                   prenotazione.
	 * @throws DiscountException se la percentuale di sconto è minore di 0 o
	 *                           maggiore di 1.
	 */
	public void setPercentage(double percentage) throws DiscountException {
		if (percentage < 0.0 || percentage > 1.0) {
			throw new DiscountException("La percentuale di sconto deve essere compresa tra 0.0 (0%) e 1.0 (100%).");
		}
		this.percentage = percentage;
	}

	public int getMinAge() {
		return minAge;
	}

	public int getMaxAge() {
		return maxAge;
	}

	@Override
	public String toString() {
		return String.format(
				"[%s]\nEtà sotto la quale lo sconto è valido: %d anni\nEtà sopra la quale lo sconto è valido: %d anni\n"
						+ "Percentuale di sconto applicato: %d%%\n",
				getTypeOfDiscount().toString(), minAge, maxAge, (int) (percentage * 100.0));
	}

}
