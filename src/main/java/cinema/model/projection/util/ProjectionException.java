package cinema.model.projection.util;

/**
 * Gestisce le eccezioni generate dalla classe Projection.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class ProjectionException extends Exception {

	/**
	 * Costruttore dell'eccezione.
	 * 
	 * @param message messaggio da riportare.
	 */
	public ProjectionException(String message) {
		super(message);
	}
}
