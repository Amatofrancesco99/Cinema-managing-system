package cinema.model.payment;

/**
 * Adattatore che espone uniformemente agli altri oggetti del dominio le
 * funzionalità di una determinata API di pagamento fornita da un gestore
 * esterno.
 *
 * @author Screaming Hairy Armadillo Team
 *
 */
public interface IPaymentAdapter {

	/**
	 * Richiede ad un'API di pagamento specificata dalla classe che implementa
	 * questa interfaccia una transazione di pagamento relativa ad un importo
	 * specifico attraverso il relativo metodo di pagamento.
	 * 
	 * @param amount importo da scalare attraverso il metodo di pagamento.
	 * @return true se il pagamento è andato a buon fine, false se ci sono problemi
	 *         nell'effettuare la transazione.
	 */
	public boolean pay(double amount);

}
