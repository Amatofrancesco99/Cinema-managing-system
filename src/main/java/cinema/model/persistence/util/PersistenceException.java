package cinema.model.persistence.util;

/**
 * Lanciata in caso di errori riscontrati durante l'uso di meccanismi di
 * persistenza.
 *
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class PersistenceException extends Exception {

	/**
	 * Costruttore dell'eccezione.
	 * 
	 * @param message messaggio contenente i dettagli dell'errore riscontrato.
	 */
	public PersistenceException(String message) {
		super(message);
	}

}
