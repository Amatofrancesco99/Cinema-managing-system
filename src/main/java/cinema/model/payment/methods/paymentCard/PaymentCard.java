package cinema.model.payment.methods.paymentCard;

import java.time.YearMonth;

/**
 * Contiene le informazioni che riguardano la carta di credito, utilizzata dallo
 * spettatore, per effettuare il pagamento della prenotazione.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class PaymentCard {

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