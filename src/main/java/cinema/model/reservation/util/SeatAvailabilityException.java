package cinema.model.reservation.util;

/**
 * Eccezione lanciata qualora il posto non sia disponibile.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class SeatAvailabilityException extends Exception {

	/**
	 * Costruttore dell'eccezione.
	 * 
	 * @param message Messaggio da riportare.
	 */
	public SeatAvailabilityException(String message) {
		super(message);
	}
}
