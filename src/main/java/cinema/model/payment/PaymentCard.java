package cinema.model.payment;

import java.util.Date;

import cinema.model.money.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** BREVE DESCRIZIONE PAYMENTCARD
 * 
 * @author Screaming HairyArmadillo Team
 *
 * Classe che rappresenta la carta di credito utilizzata per effettuare il pagamento della
 * prenotazione
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class PaymentCard {
	
	private String IBAN, accountHolder;
	private int securityCode;
	private Date expirationDate;
		
	//to simplify, we do not ask to out credit card manager
	public boolean decreaseMoney(Money amount) {
		return true;
	}
}