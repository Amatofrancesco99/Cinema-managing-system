package cinema.controller.util;


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
	public DiscountNotFoundException(String message) {
		super(message);
	}

}