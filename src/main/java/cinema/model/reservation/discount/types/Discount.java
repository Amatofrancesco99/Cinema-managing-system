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

	private int id;

	/**
	 * Costruttore della strategia di sconto.
	 * 
	 * @param type tipo di sconto.
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
