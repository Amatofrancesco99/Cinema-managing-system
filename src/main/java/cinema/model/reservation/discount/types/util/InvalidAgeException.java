package cinema.model.reservation.discount.types.util;


/** BREVE DESCRIZIONE CLASSE InvalidMinAgeException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora si inserisca un età non valida
 */
@SuppressWarnings("serial")
public class InvalidAgeException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public InvalidAgeException() {
		System.err.println("Età inserita non valida.");
	}
	
}