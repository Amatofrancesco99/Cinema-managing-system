package cinema.model.reservation.discount.types.util;

/** BREVE DESCRIZIONE CLASSE InvalidPercentageValueException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora si inserisca una percentuale di sconto non valida
 */
@SuppressWarnings("serial")
public class InvalidPercentageValueException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public InvalidPercentageValueException() {
		System.err.println("La percentuale di sconto inserita non ha un valore valido.");
	}
	
}