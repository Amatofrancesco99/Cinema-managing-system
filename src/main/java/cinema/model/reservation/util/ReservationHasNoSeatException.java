package cinema.model.reservation.util;


/** BREVE DESCRIZIONE ECCEZIONE ReservationHasNoSeatException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora la prenotazione non abbia nemmeno un posto associato
 */
@SuppressWarnings("serial")
public class ReservationHasNoSeatException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public ReservationHasNoSeatException() {
		System.out.println("Verifica di aver inserito almeno un posto alla prenotazione. ");
	}
	
}