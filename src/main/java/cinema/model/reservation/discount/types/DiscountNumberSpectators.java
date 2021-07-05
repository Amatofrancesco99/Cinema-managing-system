package cinema.model.reservation.discount.types;

import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.types.util.DiscountException;
import cinema.model.reservation.discount.types.util.TypeOfDiscount;

/**
 * Strategia di sconto sulla prenotazione basata sul numero delle persone
 * facenti parte della prenotazione.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class DiscountNumberSpectators extends Discount {

	/**
	 * Numero di persone associate alla prenotazione.
	 */
	private int numberPeople;
	/**
	 * Percentuale di sconto da applicare.
	 */
	private double percentage;

	/**
	 * Costruttore dello sconto.
	 * 
	 * @param percentage Percentuale di sconto da applicare.
	 */
	public DiscountNumberSpectators(int numberPeople, double percentage, int id) {
		super(TypeOfDiscount.NUMBER, id);
		this.numberPeople = numberPeople;
		this.percentage = percentage;

	}

	@Override
	public double getTotal(Reservation reservation) {
		double totalPrice = 0;
		if (reservation.getNSeats() >= getNumberPeople()) {
			totalPrice += reservation.getProjection().getPrice() * (1 - percentage) * reservation.getNSeats();
		} else
			totalPrice += reservation.getProjection().getPrice() * reservation.getNSeats();
		return totalPrice;
	}

	/**
	 * Imposta il numero di persone oltre al quale si applica lo sconto comitiva.
	 * 
	 * @param n numero di persone associate alla prenotazione.
	 * @throws DiscountException eccezione lanciata qualora il numero di persone
	 *                           oltre il quale si applica lo sconto è inferiore,o
	 *                           uguale, a zero.
	 */
	public void setNumberPeople(int n) throws DiscountException {
		if (n > 0) {
			this.numberPeople = n;
		} else
			throw new DiscountException(
					"Il numero di persone sopra il quale applicare lo sconto deve essere maggiore di zero.");
	}

	public int getNumberPeople() {
		return numberPeople;
	}

	@Override
	public String toString() {
		return "[ " + this.getTypeOfDiscount() + " ]" + "\n"
				+ "Numero di persone al di sopra del quale parte lo sconto è valido: " + numberPeople + "\n"
				+ "Percentuale di sconto applicata: " + percentage;
	}
}