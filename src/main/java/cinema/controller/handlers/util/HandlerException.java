package cinema.controller.handlers.util;


/** BREVE DESCRIZIONE CLASSE HandlerException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Questa classe raccoglie tutte le eccezioni dei diversi handlers
 */
@SuppressWarnings("serial")
public class HandlerException extends Exception {

	public HandlerException(String message) {
		super(message);
	}
	
}