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
	
	/**
	 * COSTRUTTORE 
	 * @param type
	 */
	public Discount(TypeOfDiscounts type) {
		this.type = type;
	}
	
	
	/**METODO per farsi restituire il tipo di sconto*/
	public TypeOfDiscounts getTypeOfDiscount(){
		return type;
	}
	
	
	/**METODO per farsi restituire le caratteristiche di uno sconto*/
	public abstract String toString();

}