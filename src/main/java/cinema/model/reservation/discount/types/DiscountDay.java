package cinema.model.reservation.discount.types;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map.Entry;

import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.types.util.TypeOfDiscount;

/**
 * Strategia di sconto sulla prenotazione basata sulla data della proiezione
 * associata alla prenotazione.
 * 
 * @author Screaming Hairy Armadillo Team
 * 
 */
public class DiscountDay extends Discount {

	/**
	 * Associa ad ogni data scontata una percentuale di sconto da applicare al costo
	 * totale della prenotazione.
	 */
	private HashMap<LocalDate, Double> discount;

	/**
	 * Costruttore dello sconto.
	 *
	 * @param id id della strategia di sconto.
	 */
	public DiscountDay(int id) {
		super(TypeOfDiscount.DAY, id);
		discount = new HashMap<>();
	}

	/**
	 * Restituisce il costo totale scontato della prenotazione dopo l'applicazione
	 * dello sconto definito dalla strategia corrente.
	 *
	 * <p>
	 * Se la data della proiezione coincide con una data per la quale è stato
	 * preimpostato uno sconto esso viene applicato al costo totale della
	 * prenotazione.
	 */
	@Override
	public double getTotal(Reservation reservation) {
		if (discount.size() > 0) {
			for (Entry<LocalDate, Double> entry : discount.entrySet()) {
				if (entry.getKey().equals(reservation.getProjection().getDateTime().toLocalDate())) {
					return reservation.getProjection().getPrice() * (1.0 - entry.getValue()) * reservation.getNSeats();
				}
			}
		}
		return reservation.getProjection().getPrice() * reservation.getNSeats();
	}

	/**
	 * Imposta la percentuale di sconto {@code percentage} per le proiezioni del
	 * giorno {@code day}.
	 * 
	 * @param day        giorno della proiezione per il quale impostare lo sconto.
	 * @param percentage percentuale di sconto da applicare.
	 */
	public void addDayDiscount(LocalDate day, double percentage) {
		discount.put(day, percentage);
	}

	@Override
	public String toString() {
		String serialization = "";
		for (Entry<LocalDate, Double> entry : discount.entrySet()) {
			serialization += String.format("%s: sconto del %d%%\n", entry.getKey().toString(),
					(int) (entry.getValue() * 100.0));
		}
		return String.format("[%s]\nDate scontante e relativa percentuale di sconto:\n%s",
				getTypeOfDiscount().toString(), serialization);
	}

}
