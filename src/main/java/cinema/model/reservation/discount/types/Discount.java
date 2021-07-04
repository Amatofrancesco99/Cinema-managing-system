package cinema.model.reservation.discount.types;

import cinema.model.reservation.discount.IReservationDiscountStrategy;
import cinema.model.reservation.discount.types.util.TypeOfDiscounts;


/** BREVE DESCRIZIONE CLASSE DiscontDay	 (Pattern Strategy)
 * 
 * @author Screaming Hairy Armadillo Team
 *	
 *  Questa classe rappresenta padre delle strategia
 */
public abstract class Discount implements IReservationDiscountStrategy{
	
	/**ATTRIBUTI
	 * @param type		Tipo di sconto.
	 */
	private TypeOfDiscounts type;
	private int id;
	
	/**
	 * COSTRUTTORE 
	 * @param type
	 */
	public Discount(TypeOfDiscounts type, int id) {
		this.type = type;
		this.id = id;
	}
	
	
	/**METODO per farsi restituire il tipo di sconto*/
	public TypeOfDiscounts getTypeOfDiscount(){
		return type;
	}
	
	public int getDiscountId() {
		return id;
	}
	
	/**METODO per farsi restituire le caratteristiche di uno sconto*/
	public abstract String toString();

}