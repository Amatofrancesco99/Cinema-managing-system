package cinema.model.payment.methods.paymentCard;

import java.time.YearMonth;
import java.util.Random;

import cinema.model.payment.Payment;
import cinema.model.payment.methods.paymentCard.util.ExpiredCreditCardException;
import cinema.model.payment.methods.paymentCard.util.InvalidCCVException;
import cinema.model.payment.methods.paymentCard.util.InvalidCreditCardNumberException;
import lombok.*;

/** BREVE DESCRIZIONE PAYMENTCARD
 * 
 * @author Screaming HairyArmadillo Team
 *
 * Classe che rappresenta la carta di credito utilizzata per effettuare il pagamento della
 * prenotazione
 */
@NoArgsConstructor
@Data
public class PaymentCard implements Payment{
	
	
	/**ATTRIBUTI
	 * @param MIN_DIGIT  	    Numero minimo di numeri della carta
	 * @param owner		 		Nome del proprietario della carta
	 * @param number	 		Numero della carta	  
	 * @param ccv		 		Codice
	 * @param expirationDate	Data di scadenza (Anno/Mese)
	 */
	private final int MIN_DIGIT = 16;
	private String owner;
	private String number;
	private String ccv;
	private YearMonth expirationDate;
	
	
	/** 
	 * METODO per impostare il numero della carta
	 * @param number
	 * @throws InvalidCreditCardNumberException
	 */
	public void setNumber(String number) throws InvalidCreditCardNumberException {
		if (number.length() != MIN_DIGIT)
			throw new InvalidCreditCardNumberException();
		else this.number = number.replaceAll("[\\D]", "");
	}
	
	
	/**
	 * METODO per inserire il CCV della carta
	 * @param ccv
	 * @throws InvalidCCVException
	 */
	public void setCCV(String ccv) throws InvalidCCVException {
		if (ccv.length() != 3)
			throw new InvalidCCVException();
		else this.ccv = ccv.replaceAll("[\\D]", "");
	}
	
	
	/**
	 * METODO per impostare la data di scadenza (Anno/Mese) della carta di credito
	 * @param expirationDate
	 * @throws ExpiredCreditCardException
	 */
	public void setExpirationDate(YearMonth expirationDate) throws ExpiredCreditCardException {
		if (!(expirationDate.isAfter(YearMonth.now())))
			throw new ExpiredCreditCardException();
		else this.expirationDate = expirationDate;
	}
	
	
	/**
	 * METODO per ridurre il saldo della carta
	 * Resituisce sempre true perché non abbiamo una implementazione vera della carta
	 * Ipotizziamo semplicemente che qualsiasi cifra inserita venga sempre restituito vero
	 * Ovviamente bisognerebbe considerare delle problematiche, ossia se per qualche motivo
	 * non si potesse raggiungere il gestore della carta, o se il saldo da pagare fosse superiore
	 * rispetto al credito residuo, ecc...
	 */
	public boolean decreaseMoney(double amount) {
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
}