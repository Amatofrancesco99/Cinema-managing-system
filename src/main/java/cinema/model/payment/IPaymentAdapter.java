package cinema.model.payment;

/**
 * Contiene i metodi di cui uno strumento di pagamento dovrebbe disporre.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public interface IPaymentAdapter {

	/**
	 * Richiede ad un'API di pagamento specificata dalla classe che implementa
	 * questa interfaccia una transazione di denaro attraverso il metodo di
	 * pagamento inserito.
	 * 
	 * @param amount importo da sottrarre.
	 * @return true se il pagamento è andato a buon fine, false nel caso ci siano
	 *         stati dei problemi.
	 */
	public boolean pay(double amount);

}
