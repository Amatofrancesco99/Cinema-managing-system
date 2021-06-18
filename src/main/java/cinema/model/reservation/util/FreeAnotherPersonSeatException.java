package cinema.model.reservation.util;


/** BREVE DESCRIZIONE ECCEZIONE FreeAnotherPersonSeatException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora si cerchi di liberare il posto occupato da un altro utente
 */
@SuppressWarnings("serial")
public class FreeAnotherPersonSeatException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public FreeAnotherPersonSeatException() {
		System.out.println("Non puoi liberare un posto occupato in qualche altra prenotazione.");
	}
	
}