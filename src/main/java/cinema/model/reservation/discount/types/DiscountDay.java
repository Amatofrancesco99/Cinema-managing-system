package cinema.model.reservation.discount.types;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map.Entry;

import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.types.util.TypeOfDiscount;

/**
 * Strategia di sconto sulla prenotazione basata sul giorno in cui gli
 * spettatori visioneranno il film.
 * 
 * @author Screaming Hairy Armadillo Team
 * 
 */
public class DiscountDay extends Discount {

	/**
	 * HashMap che associa ad ogni data, uno sconto da sottrarre al totale della
	 * prenotazione.
	 */
	private HashMap<LocalDate, Double> discount;

	/**
	 * Costruttore dello sconto.
	 */
	public DiscountDay(int id) {
		super(TypeOfDiscount.DAY, id);
		discount = new HashMap<>();
	}

	@Override
	public double getTotal(Reservation reservation) {
		double totalPrice = 0;
		if (discount.size() > 0) {
			for (Entry<LocalDate, Double> entry : discount.entrySet()) {
				if (entry.getKey().equals(reservation.getProjection().getDateTime().toLocalDate())) {
					totalPrice += reservation.getProjection().getPrice() * (1 - entry.getValue())
							* reservation.getNSeats();
					return totalPrice;
				}
			}
		}
		totalPrice += reservation.getProjection().getPrice() * reservation.getNSeats();
		return totalPrice;
	}

	/**
	 * Restituisce, sottoforma di stringa, lo sconto associato al giorno.
	 * 
	 * @param discount HashMap che associa ad ogni data, uno sconto da sottrarre al
	 *                 totale della prenotazione.
	 * @return lo sconto associato al giorno
	 */
	private String discountsToString(HashMap<LocalDate, Double> discount) {
		String output = "";
		for (Entry<LocalDate, Double> entry : discount.entrySet()) {
			output += "Giorno: " + entry.getKey();
			output += "  Sconto: " + entry.getValue() * 100 + "%\n";
		}
		return output;
	}

	public void addDayDiscount(LocalDate day, double percentage) {
		this.discount.put(day, percentage);
	}

	@Override
	public String toString() {
		return String.format("[%s]\nGiorni e relativa percentuale di sconto nelle seguenti date:\n%s",
				getTypeOfDiscount().toString(), discountsToString(discount));
	}

}
