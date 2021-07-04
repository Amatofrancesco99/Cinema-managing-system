package cinema.model.reservation.discount.types;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map.Entry;

import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.types.util.TypeOfDiscounts;

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
		super(TypeOfDiscounts.DAY, id);
		discount = new HashMap<>();
	}

	@Override
	public double getTotal(Reservation r) {
		double totalPrice = 0;
		if (discount.size() > 0) {
			for (Entry<LocalDate, Double> entry : discount.entrySet()) {
				if (entry.getKey().equals(r.getProjection().getDateTime().toLocalDate())) {
					totalPrice += r.getProjection().getPrice() * (1 - entry.getValue()) * r.getNSeats();
					return totalPrice;
				}
			}
		}
		totalPrice += r.getProjection().getPrice() * r.getNSeats();
		return totalPrice;
	}

	@Override
	public String toString() {
		return "[ " + this.getTypeOfDiscount() + " ]" + "\n"
				+ "Giorni e percentuale di sconti nelle specifiche giornate: \n" + discountsToString(discount);
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
}
