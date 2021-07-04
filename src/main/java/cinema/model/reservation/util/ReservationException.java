package cinema.model.reservation.util;

/**
 * Eccezione lanciata qualora la reservation non rispetti determinate
 * condizioni.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class ReservationException extends Exception {

	/**
	 * Costruttore dell'eccezione.
	 * 
	 * @param message Messaggio da riportare.
	 */
	public ReservationException(String message) {
		super(message);
	}
}
