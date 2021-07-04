package cinema.model.reservation.util;

/**
 * Eccezione lanciata qualora il posto non sia disponibile.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class SeatAvailabilityException extends Exception {
	public SeatAvailabilityException(String message) {
		super(message);
	}
}
