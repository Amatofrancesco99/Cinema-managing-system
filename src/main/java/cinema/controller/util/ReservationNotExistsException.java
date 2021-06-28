package cinema.controller.util;


/** BREVE DESCRIZIONE CLASSE ReservationNotExistsException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora si cerchi una prenotazione per id, inesistente
 */
@SuppressWarnings("serial")
public class ReservationNotExistsException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public ReservationNotExistsException(long progressive) {
		System.out.println("La prenotazione n° " + progressive + " non esiste.");
	}
	
}