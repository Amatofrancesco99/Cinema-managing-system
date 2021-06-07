package cinema.model.payment;

import cinema.model.money.Money;

/** BREVE DESCRIZIONE PAYMENT
 * 
 * @author Screaming HairyArmadillo Team
 *
 * Interfaccia che rappresenta i metodi che uno strumento
 * di pagamento dovrebbero avere
 */

public interface Payment {
	
	public boolean decreaseMoney(Money amount);
}
