package cinema.model.reservation.util;

/**
 * Lanciata in caso di errori riscontrati nelle procedure di interazione con gli
 * oggetti che rappresentano le prenotazioni.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class ReservationException extends Exception {

	/**
	 * Costruttore dell'eccezione.
	 * 
	 * @param message messaggio contenente i dettagli dell'errore riscontrato.
	 */
	public ReservationException(String message) {
		super(message);
	}

}
