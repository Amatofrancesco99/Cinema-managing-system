package cinema.controller.util;

/** BREVE DESCRIZIONE CLASSE NoProjectionException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora si cerchi una proiezione, il cui Id non esiste.
 */
@SuppressWarnings("serial")
public class NoProjectionException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public NoProjectionException(int id) {
		System.err.println("La proiezione con id " + id + " non esiste.");
	}
	
}
