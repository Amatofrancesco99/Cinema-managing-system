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
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public InvalidSpectatorInfoException(String message) {
		super(message);
	}
}
