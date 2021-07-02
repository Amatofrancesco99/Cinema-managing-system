package cinema.model.payment;

import java.util.Random;

import cinema.model.payment.methods.paymentCard.PaymentCard;

public class GreatNorthernAccountingAdapter implements IPaymentAdapter {

	@SuppressWarnings("unused")
	private PaymentCard p;

	public GreatNorthernAccountingAdapter(PaymentCard p) {
		this.p = p;
	}

	/**
	 * METODO per ridurre il saldo della carta Resituisce sempre true perché non
	 * abbiamo una implementazione vera della carta Ipotizziamo semplicemente che
	 * qualsiasi cifra inserita venga sempre restituito vero Ovviamente bisognerebbe
	 * considerare delle problematiche, ossia se per qualche motivo non si potesse
	 * raggiungere il gestore della carta, o se il saldo da pagare fosse superiore
	 * rispetto al credito residuo, ecc...
	 */
	@Override
	public boolean pay(double amount) {
		double successProbability = 1;
		return new Random().nextDouble() < successProbability;
	}

}
