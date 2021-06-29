package cinema.model.payment.methods.paymentCard;

import java.time.YearMonth;  

/** BREVE DESCRIZIONE PAYMENTCARD
 * 
 * @author Screaming HairyArmadillo Team
 *
 * Classe che rappresenta la carta di credito utilizzata per effettuare il pagamento della
 * prenotazione
 */
public class PaymentCard{
	
	private String owner;
	private String number;
	private String cvv;
	private YearMonth expirationDate;
	
	public PaymentCard(String number, String owner, String cvv, YearMonth expirationDate) {
		this.owner = owner;
		this.number = number;
		this.cvv = cvv;
		this.expirationDate = expirationDate;
	}
	
	public String getNumber() {
		return number;
	}

	public String getOwner() {
		return owner;
	}

	public String getCvv() {
		return cvv;
	}

	public YearMonth getExpirationDate() {
		return expirationDate;
	}

}