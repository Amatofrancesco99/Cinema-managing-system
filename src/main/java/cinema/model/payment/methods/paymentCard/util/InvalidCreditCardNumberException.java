package cinema.model.payment.methods.paymentCard.util;


/** BREVE DESCRIZIONE CLASSE InvalidCreditCardNumberException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora si inserisca una carta di credito con un numero di cifre
 * errato
 */
@SuppressWarnings("serial")
public class InvalidCreditCardNumberException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public InvalidCreditCardNumberException() {
		System.out.println("Il numero di cifre inserito per la carta di credito non è corretto.");
	}
	
}