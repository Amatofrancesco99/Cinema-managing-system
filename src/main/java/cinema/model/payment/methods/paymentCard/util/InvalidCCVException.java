package cinema.model.payment.methods.paymentCard.util;


/** BREVE DESCRIZIONE CLASSE InvalidCCVException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora si inserisca una ccv errato.
 */
@SuppressWarnings("serial")
public class InvalidCCVException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public InvalidCCVException() {
		System.out.println("Il CCV inserito non è valido");
	}
	
}