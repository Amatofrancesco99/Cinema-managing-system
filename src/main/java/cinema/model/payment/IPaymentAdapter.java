package cinema.model.payment;

/**
 * Contiene i metodi di cui uno strumento di pagamento dovrebbe disporre.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public interface IPaymentAdapter {

	public boolean pay(double amount);

}
