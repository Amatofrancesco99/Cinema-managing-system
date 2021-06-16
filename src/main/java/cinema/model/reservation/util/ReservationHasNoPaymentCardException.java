package cinema.model.reservation.util;


/** BREVE DESCRIZIONE ECCEZIONE ReservationHasNoPaymentCardException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora la prenotazione non abbia associata una carta di credito
 */
@SuppressWarnings("serial")
public class ReservationHasNoPaymentCardException extends Exception {
	
	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public ReservationHasNoPaymentCardException() {
		System.err.println("Verifica di aver inserito un metodo di pagamento.");
	}
	
}