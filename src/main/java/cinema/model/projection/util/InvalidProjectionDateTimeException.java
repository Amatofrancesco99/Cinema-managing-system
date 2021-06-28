package cinema.model.projection.util;


/** BREVE DESCRIZIONE CLASSE InvalidProjectionDateTimeException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora si voglia inserire una proiezione la cui data sia prima di oggi
 */
@SuppressWarnings("serial")
public class InvalidProjectionDateTimeException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public InvalidProjectionDateTimeException() {
		System.out.println("Non puoi inserire una proiezione la cui data è prima di oggi.");
	}
	
}