package cinema.model.payment;

/**
 * Contiene i metodi di cui uno strumento di pagamento dovrebbe disporre.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public interface IPaymentAdapter {

	/**
	 * Permette di gestire il pagamento.
	 * 
	 * @param amount quantita di denaro da sottrarre dal saldo.
	 * @return true se il pagamento è andato a buon fine, false nel caso ci siano
	 *         stati dei problemi.
	 */
	public boolean pay(double amount);

}
