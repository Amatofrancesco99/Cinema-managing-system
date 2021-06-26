package cinema.controller.util;


/** BREVE DESCRIZIONE CLASSE PasswordTooShortException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora si inserisca una nuova password troppo corta
 */
@SuppressWarnings("serial")
public class PasswordTooShortException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public PasswordTooShortException() {
		System.out.println("La nuova password inserita è troppo corta. ");
	}
	
}