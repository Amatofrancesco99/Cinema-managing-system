package cinema.controller.util;

/**
 * Gestisce le eccezioni, generate da Cinema, che segnalano la mancanza di uno
 * specifico sconto.
 * 
 * @author Screaming Hairy Armadillo Team
 *
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