package cinema.model.reservation.discount.types;

import cinema.model.reservation.discount.IReservationDiscountStrategy;
import cinema.model.reservation.discount.types.util.TypeOfDiscounts;

/**
 * Rappresenta le strategie di sconti da applicare al costo totale delle
 * prenotazioni.
 * 
 * @author Screaming Hairy Armadillo Team
 * 
 */
public abstract class Discount implements IReservationDiscountStrategy {

	/**
	 * Tipo di sconto.
	 */
	private TypeOfDiscounts type;

	/**
	 * Costruttore della strategia di sconto.
	 * 
	 * @param type tipo di sconto.
	 */
	public Discount(TypeOfDiscounts type) {
		this.type = type;
	}

	public TypeOfDiscounts getTypeOfDiscount() {
		return type;
	}

	public abstract String toString();

}
