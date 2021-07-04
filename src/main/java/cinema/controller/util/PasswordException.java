package cinema.controller.util;

/**
 * Gestisce le eccezioni, generate dal Cinema, che rigurardano la password
 * dell'amministratore.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class PasswordException extends Exception {

	public PasswordException(String message) {
		super(message);
	}
}
