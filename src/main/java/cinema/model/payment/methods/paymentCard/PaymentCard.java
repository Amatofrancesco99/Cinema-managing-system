package cinema.model.payment.methods.paymentCard;

import java.time.YearMonth; 
import java.util.Random;

import cinema.model.payment.Payment;
import cinema.model.payment.methods.paymentCard.util.PaymentCardException;

/** BREVE DESCRIZIONE PAYMENTCARD
 * 
 * @author Screaming HairyArmadillo Team
 *
 * Classe che rappresenta la carta di credito utilizzata per effettuare il pagamento della
 * prenotazione
 */
public class PaymentCard implements Payment{
	
	
	/**ATTRIBUTI
	 * @param MIN_DIGIT  	    Numero minimo di numeri della carta
	 * @param owner		 		Nome del proprietario della carta
	 * @param number	 		Numero della carta	  
	 * @param ccv		 		Codice
	 * @param expirationDate	Data di scadenza (Anno/Mese)
	 */
	private static final int CREDIT_CARD_NUMBER_OF_DIGITS = 16;
	private static final int CVV_NUMBER_OF_DIGITS = 3;
	@SuppressWarnings("unused")
	private String owner;
	@SuppressWarnings("unused")
	private String number;
	@SuppressWarnings("unused")
	private String cvv;
	@SuppressWarnings("unused")
	private YearMonth expirationDate;
	
	/** 
	 * METODO per impostare il numero della carta
	 * @param number
	 * @throws PaymentCardException
	 */
	public void setNumber(String number) throws PaymentCardException {
		if (number.length() != CREDIT_CARD_NUMBER_OF_DIGITS)
			throw new PaymentCardException("Il numero della carta di credito inserito non è valido, sono richieste " + CREDIT_CARD_NUMBER_OF_DIGITS + " cifre.");
		else this.number = number;
	}
	
	
	/**
	 * METODO per inserire il CCV della carta
	 * @param ccv
	 * @throws PaymentCardException
	 */
	public void setCvv(String cvv) throws PaymentCardException {
		if(cvv.length() != CVV_NUMBER_OF_DIGITS)
			throw new PaymentCardException("Il CVV inserito non è valido, sono richieste " + CVV_NUMBER_OF_DIGITS + " cifre.");
		this.cvv = cvv;	
	}
	
	
	/**
	 * METODO per impostare la data di scadenza (Anno/Mese) della carta di credito
	 * @param expirationDate
	 * @throws PaymentCardException
	 */
	public void setExpirationDate(YearMonth expirationDate) throws PaymentCardException {
		if (!(expirationDate.isAfter(YearMonth.now())))
			throw new PaymentCardException("La carta di credito inserita è scaduta.");
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


	/** METODO per impostare il nome del proprietario della carta
	 * @param owner
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
}