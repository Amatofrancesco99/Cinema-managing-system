package cinema.model.payment.methods.paymentCard;

import java.time.YearMonth;

/**
 * Carta di credito utilizzata dallo spettatore per effettuare il pagamento
 * della prenotazione.
 *
 * @author Screaming Hairy Armadillo Team
 *
 */
public class PaymentCard {

	/**
	 * Numero della carta.
	 */
	private String number;

	/**
	 * Titolare della carta.
	 */
	private String owner;

	/**
	 * Codice di sicurezza della carta.
	 */
	private String cvv;

	/**
	 * Data di scadenza della carta.
	 */
	private YearMonth expirationDate;

	/**
	 * Costruttore della carta di credito.
	 * 
	 * @param number         numero della carta.
	 * @param owner          titolare della carta.
	 * @param cvv            codice di sicurezza della carta.
	 * @param expirationDate data di scadenza della carta.
	 */
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
