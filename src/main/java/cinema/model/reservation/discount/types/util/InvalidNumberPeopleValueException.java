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
		System.err.println("Numero di persone (da cui parte lo sconto comitiva) inserito non valido. ");
	}
	
}
