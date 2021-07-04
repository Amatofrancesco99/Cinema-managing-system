package cinema.model.reservation.util;

/**
 * Eccezione lanciata se il posto richiesto non è disponibile.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class SeatAvailabilityException extends Exception {

	/**
	 * Costruttore dell'eccezione.
	 * 
	 * @param message messaggio contenente i dettagli dell'errore riscontrato.
	 */
	public SeatAvailabilityException(String message) {
		super(message);
	}

}
