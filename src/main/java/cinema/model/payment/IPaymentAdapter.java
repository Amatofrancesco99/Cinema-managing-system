package cinema.model.payment;

/** BREVE DESCRIZIONE PAYMENT
 * 
 * @author Screaming HairyArmadillo Team
 *
 * Interfaccia che rappresenta i metodi che uno strumento
 * di pagamento dovrebbero avere
 */
public interface IPaymentAdapter {
	
	public boolean pay(double amount);

}
