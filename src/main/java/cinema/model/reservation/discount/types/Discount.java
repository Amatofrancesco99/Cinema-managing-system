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

	private int id;

	/**
	 * Costruttore del discount.
	 * 
	 * @param type Indica il tipo di sconto.
	 */
	public Discount(TypeOfDiscounts type, int id) {
		this.type = type;
		this.id = id;
	}

	public TypeOfDiscounts getTypeOfDiscount() {
		return type;
	}
	
	public int getDiscountId() {
		return id;
	}
	
	/**METODO per farsi restituire le caratteristiche di uno sconto*/
	public abstract String toString();

}
