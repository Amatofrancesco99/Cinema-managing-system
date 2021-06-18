package cinema.model.reservation.discount.types.util;

/** BREVE DESCRIZIONE CLASSE InvalidNumberPeopleValueException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora si inserisca un numero di persone non valido
 */
@SuppressWarnings("serial")
public class InvalidNumberPeopleValueException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public InvalidNumberPeopleValueException() {
		System.out.println("Numero di persone inserito non valido. ");
	}
	
}
