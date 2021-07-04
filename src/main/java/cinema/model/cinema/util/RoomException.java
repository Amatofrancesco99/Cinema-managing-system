package cinema.model.cinema.util;

/**
 * Lanciata in caso di errori legati alla gestione delle sale del cinema.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class RoomException extends Exception {

	/**
	 * Costruttore dell'eccezione.
	 * 
	 * @param message messaggio contenente i dettagli dell'errore riscontrato.
	 */
	public RoomException(String message) {
		super(message);
	}

}
