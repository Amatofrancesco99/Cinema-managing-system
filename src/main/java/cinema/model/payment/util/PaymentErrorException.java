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
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public PaymentErrorException(String message) {
		super(message);
	}

}