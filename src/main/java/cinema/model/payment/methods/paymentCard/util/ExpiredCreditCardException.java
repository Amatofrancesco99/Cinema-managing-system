package cinema.model.payment.methods.paymentCard.util;


/** BREVE DESCRIZIONE CLASSE ExpiredCreditCardException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora si inserisca una carta di credito scaduta
 */
@SuppressWarnings("serial")
public class ExpiredCreditCardException extends Exception {
	
	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public ExpiredCreditCardException() {
		System.out.println("La carta di credito inserita è scaduta. ");
	}
	
}