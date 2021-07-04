package cinema.model.persistence.util;

/**
 * Utilizzata per segnalare errori riscontrati nell'utilizzo di un qualsiasi
 * meccanismo di persistenza.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class PersistenceException extends Exception {

	/**
	 * Costruttore dell'eccezione.
	 * 
	 * @param message messaggio da riportare.
	 */
	public PersistenceException(String message) {
		super(message);
	}
	
}
