package cinema.model.payment;

import java.util.Random;

import cinema.model.payment.methods.paymentCard.PaymentCard;

/**
 * Rappresenta una possibile metodologia di pagamento digitale messa a
 * disposizione dal cinema.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class GreatNorthernAccountingAdapter implements IPaymentAdapter {

	/**
	 * Carta di credito.
	 */
	@SuppressWarnings("unused")
	private PaymentCard p;

	/**
	 * Costruttore del servizio di pagamento digitale GreatNorthernAccounting.
	 * 
	 * @param p carta di credito che sfrutta il servizio.
	 */
	public GreatNorthernAccountingAdapter(PaymentCard p) {
		this.p = p;
	}

	/**
	 * Permette di simulare un pagamento. Il valore di succesProbability rappresenta
	 * la probabilità di successo del pagamento (1 sempre successo, 0 sempre
	 * insuccesso).
	 * 
	 * @param amount quantita di denaro da sottrarre dal saldo.
	 * @return true se il pagamento è andato a buon fine, false nel caso ci siano
	 *         stati dei problemi.
	 */
	@Override
	public boolean pay(double amount) {
		double successProbability = 1;
		return new Random().nextDouble() < successProbability;
	}

}
