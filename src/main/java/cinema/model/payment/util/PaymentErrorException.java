package cinema.model.payment.util;

/**
 * Lanciata in caso di errori riscontrati nelle procedure di pagamento.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class PaymentErrorException extends Exception {

	/**
	 * Costruttore dell'eccezione.
	 * 
	 * @param message messaggio contenente i dettagli dell'errore riscontrato.
	 */
	public PaymentErrorException(String message) {
		super(message);
	}

}
