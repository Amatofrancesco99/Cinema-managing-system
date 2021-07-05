package cinema.model.reservation.discount.types;

import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.types.util.TypeOfDiscount;

/**
 * Strategia di sconto sulla prenotazione basata sul numero delle persone
 * partecipanti alla proiezione per la prenotazione stessa.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class DiscountNumberSpectators extends Discount {

	/**
	 * Numero di spettatori all'interno della prenotazione sopra il quale viene
	 * applicato lo sconto.
	 */
	private int numberOfPeople;

	/**
	 * Percentuale di sconto da applicare al costo totale della reservation.
	 */
	private double percentage;

	/**
	 * Costruttore dello sconto.
	 * 
	 * @param numberOfPeople numero di spettatori all'interno della prenotazione
	 *                       sopra il quale viene applicato lo sconto.
	 * @param percentage     percentuale di sconto da applicare al costo totale
	 *                       della prenotazione.
	 * @param id             id della strategia di sconto.
	 */
	public DiscountNumberSpectators(int numberOfPeople, double percentage, int id) {
		super(TypeOfDiscount.NUMBER, id);
		this.numberOfPeople = numberOfPeople;
		this.percentage = percentage;
	}

	/**
	 * Restituisce il costo totale scontato della prenotazione dopo l'applicazione
	 * dello sconto definito dalla strategia corrente.
	 *
	 * Se la prenotazione riguarda un numero di spettatori uguale o superiore al
	 * minimo preimpostato per la strategia di sconto corrente viene applicata la
	 * detrazione della percentuale di sconto dal costo totale della prenotazione.
	 */
	@Override
	public double getTotal(Reservation reservation) {
		if (reservation.getNSeats() >= numberOfPeople) {
			return reservation.getProjection().getPrice() * (1.0 - percentage) * reservation.getNSeats();
		}
		return reservation.getProjection().getPrice() * reservation.getNSeats();
	}

	@Override
	public String toString() {
		return String.format(
				"[%s]\nNumero di persone a partire al quale lo sconto è valido: %d\nPercentuale di sconto applicato: %d%%\n",
				getTypeOfDiscount(), numberOfPeople, (int) (percentage * 100.0));
	}

}
