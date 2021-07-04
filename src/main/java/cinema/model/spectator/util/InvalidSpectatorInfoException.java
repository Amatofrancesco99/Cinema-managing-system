package cinema.model.spectator.util;

/**
 * Eccezione lanciata qualora si inseriscano informazioni sbagliate sul cliente
 * che sta effettuando la prenotazione.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class InvalidSpectatorInfoException extends Exception {

	/**
	 * Costruttore dell'eccezione.
	 * 
	 * @param message Messaggio da riportare.
	 */
	public InvalidSpectatorInfoException(String message) {
		super(message);
	}
}
