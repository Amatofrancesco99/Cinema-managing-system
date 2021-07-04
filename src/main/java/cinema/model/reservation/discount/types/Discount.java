package cinema.model.reservation.discount.types;

import cinema.model.reservation.discount.IReservationDiscountStrategy;
import cinema.model.reservation.discount.types.util.TypeOfDiscounts;

/**
 * Rappresenta la classe padre delle strategie.
 * 
 * @author Screaming Hairy Armadillo Team
 * 
 */
public abstract class Discount implements IReservationDiscountStrategy {

	/**
	 * Indica il tipo di sconto.
	 */
	private TypeOfDiscounts type;

	/**
	 * Costruttore del discount.
	 * 
	 * @param type Indica il tipo di sconto.
	 */
	public Discount(TypeOfDiscounts type) {
		this.type = type;
	}

	public TypeOfDiscounts getTypeOfDiscount() {
		return type;
	}

	public abstract String toString();

}
