package cinema.controller.handlers.util;

/**
 * Lanciata in caso di errori riscontrati durante le procedure di generazione
 * dei report delle prenotazioni e conseguente invio di e-mail allo spettatore.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class HandlerException extends Exception {

	/**
	 * Costruttore dell'eccezione.
	 * 
	 * @param message messaggio contenente i dettagli dell'errore riscontrato.
	 */
	public HandlerException(String message) {
		super(message);
	}

}
