package cinema.model.payment.util;


/** BREVE DESCRIZIONE CLASSE PaymentErrorException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora il pagamento non vada a buon fine
 */
@SuppressWarnings("serial")
public class PaymentErrorException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public PaymentErrorException() {
		System.out.println("Il pagamento non è andato a buon fine.");
	}
	
}