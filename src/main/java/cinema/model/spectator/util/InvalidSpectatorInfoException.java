package cinema.model.spectator.util;


/** BREVE DESCRIZIONE CLASSE InvalidPurchaserInfoException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora si inseriscano informazioni sbagliate sul cliente
 * che sta effettuando la prenotazione
 */
@SuppressWarnings("serial")
public class InvalidSpectatorInfoException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public InvalidSpectatorInfoException(String message) {
		super(message);
	}
}
