package cinema.model.reservation.discount.types;

import cinema.model.reservation.discount.ReservationDiscountStrategy;
import cinema.model.reservation.discount.types.util.DiscountException;
import cinema.model.reservation.discount.types.util.TypeOfDiscounts;


/** BREVE DESCRIZIONE CLASSE DiscontDay	 (Pattern Strategy)
 * 
 * @author Screaming Hairy Armadillo Team
 *	
 *  Questa classe rappresenta padre delle strategia
 */
public abstract class Discount implements ReservationDiscountStrategy{
	
	/**ATTRIBUTI
	 * @param type		Tipo di sconto.
	 */
	private TypeOfDiscounts type;
	private double percentage = 0.15; //TODO: mettere nel costruttore l'assegnazione della percentuale
	
	
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
	
	/**
	 * METODO per settare il nuovo sconto 
	 * @param f		      Percentuale di sconto da applicare
	 * @throws InvalidPercentageValueException 
	 */
	public void setPercentage(double d) throws DiscountException {
		if(d < 0)
			throw new DiscountException("La percentuale dello sconto deve essere positiva.");
		else if(d > 1)
			throw new DiscountException("La percentuale dello sconto deve essere minore o uguale il 100%");
		this.percentage = d;
	}

}