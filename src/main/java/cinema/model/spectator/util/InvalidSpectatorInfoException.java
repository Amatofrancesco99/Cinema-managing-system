package cinema.model.spectator.util;

/**
 * Lanciata se le informazioni relative ai dati di uno spettatore vengono
 * interpretate come non valide al momento dell'inserimento.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class InvalidSpectatorInfoException extends Exception {

	/**
	 * Costruttore dell'eccezione.
	 * 
	 * @param message messaggio contenente i dettagli dell'errore riscontrato.
	 */
	public InvalidSpectatorInfoException(String message) {
		super(message);
	}

}
