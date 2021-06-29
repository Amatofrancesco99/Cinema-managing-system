package cinema.model.payment.methods.paymentCard;

import java.time.YearMonth;  
import java.util.Random;

import cinema.model.payment.Payment;

/** BREVE DESCRIZIONE PAYMENTCARD
 * 
 * @author Screaming HairyArmadillo Team
 *
 * Classe che rappresenta la carta di credito utilizzata per effettuare il pagamento della
 * prenotazione
 */
public class PaymentCard implements Payment{
	
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
	
	/**
	 * METODO per ridurre il saldo della carta
	 * Resituisce sempre true perché non abbiamo una implementazione vera della carta
	 * Ipotizziamo semplicemente che qualsiasi cifra inserita venga sempre restituito vero
	 * Ovviamente bisognerebbe considerare delle problematiche, ossia se per qualche motivo
	 * non si potesse raggiungere il gestore della carta, o se il saldo da pagare fosse superiore
	 * rispetto al credito residuo, ecc...
	 */
	public boolean decreaseMoney(double amount) { //POSSIBILE DA SPOSTARE IN PAY
		int mode = 0;
		switch(mode) {
			case 0:
				return true;
			case 1:
				return false;
			default:
				return new Random().nextBoolean();
		}
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