package cinema.model.projection.util;

/**
 * Lanciata in caso di errori riscontrati nelle procedure di interazione con gli
 * oggetti che rappresentano le proiezioni.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class ProjectionException extends Exception {

	/**
	 * Costruttore dell'eccezione.
	 * 
	 * @param message messaggio contenente i dettagli dell'errore riscontrato.
	 */
	public ProjectionException(String message) {
		super(message);
	}

}
