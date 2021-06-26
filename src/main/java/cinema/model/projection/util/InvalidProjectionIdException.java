package cinema.model.projection.util;


/** BREVE DESCRIZIONE CLASSE InvalidProjectionIdException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora si voglia inserire una proiezione con id negativo
 */
@SuppressWarnings("serial")
public class InvalidProjectionIdException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public InvalidProjectionIdException() {
		System.out.println("Non si possono inserire proiezioni con un id negativo.");
	}
	
}