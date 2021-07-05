package cinema.model.payment;

import java.util.Random;

import cinema.model.payment.methods.paymentCard.PaymentCard;

/**
 * Adattatore specifico per l'API (fittizia) di un ipotetico servizio di
 * pagamento del gestore Great Northern Accounting.
 *
 * In un contesto reale questo adattatore dovrebbe esporre al dominio attraverso
 * un'interfaccia comune le funzionalità dell'API remota; in questo contesto
 * invece il metodo di pagamento simula il comportamento reale ma non esegue
 * nessuna transazione effettiva.
 *
 * @author Screaming Hairy Armadillo Team
 *
 */
public class GreatNorthernAccountingAdapter implements IPaymentAdapter {

	/**
	 * Carta di credito utilizzata per il pagamento.
	 */
	@SuppressWarnings("unused")
	private PaymentCard paymentCard;

	/**
	 * Probabilità di successo della transazione di pagamento.
	 *
	 * Poiché l'adattatore simula il comportamento reale di un'API di pagamento,
	 * questa costante imposta la percentuale di successo (0.0 = 0%, 1.0 = 100%) di
	 * ogni chiamata al metodo che effettua il pagamento.
	 */
	private final double SUCCESS_PROBABILITY = 1.0;

	/**
	 * Costruttore dell'adattatore del servizio di pagamento digitale
	 * GreatNorthernAccounting.
	 * 
	 * @param paymentCard carta di credito che sfrutta il servizio.
	 */
	public GreatNorthernAccountingAdapter(PaymentCard paymentCard) {
		this.paymentCard = paymentCard;
	}

	/**
	 * Effettua il pagamento mediante la carta di credito specificata.
	 * 
	 * Il pagamento simulato ha probabilità di successo
	 * {@code} SUCCESS_PROBABILITY}.
	 */
	@Override
	public boolean pay(double amount) {
		return new Random().nextDouble() < SUCCESS_PROBABILITY;
	}

}
