package cinema.controller.util;


/** BREVE DESCRIZIONE CLASSE WrongAdminPasswordException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora si cerchi una proiezione la cui data di proiezione è inferiore
 * a quella odierna
 */
@SuppressWarnings("serial")
public class WrongAdminPasswordException extends Exception {
	
	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public WrongAdminPasswordException() {
		System.out.println("La password inserita è sbagliata. ");
	}
	
}