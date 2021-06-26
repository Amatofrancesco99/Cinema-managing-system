package cinema.controller.util;

import cinema.model.reservation.discount.types.util.TypeOfDiscounts;

/** BREVE DESCRIZIONE CLASSE MovieNoLongerProjectedException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora non si trovi uno sconto con una determinata strategia
 */
@SuppressWarnings("serial")
public class DiscountNotFoundException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public DiscountNotFoundException(TypeOfDiscounts td) {
		System.out.println("Non è stato trovato nessuno sconto che applica la strategia " + td);
	}

}