package cinema.model.payment.util;

/**
 * Gestisce le eccezioni generate da un pagamento che non è andato a buon fine.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class PaymentErrorException extends Exception {

	/**
	 * Costruttore dell'eccezione.
	 * 
	 * @param message messaggio da riportare.
	 */
	public PaymentErrorException(String message) {
		super(message);
	}

}