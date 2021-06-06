package cinema.model.money;

import cinema.model.money.util.TypeOfCurrency;
import lombok.Data;

/** BREVE DESCRIZIONE CLASSE Money
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Come lascia intuire questa classe viene utilizzata per rappresentare i soldi, ossia 
 * una coppia ammontare di soldi, valuta.
 */
@Data
public class Money {
	
	/**
	 * ATTRIBUTI
	 * @param amount	Ammontare di soldi
	 * @param currency	Valuta
	 */
	private double amount;
	private TypeOfCurrency currency;
	
	/**
	 * COSTRUTTORE di default, setta a zero l'ammontare di soldi, e la valuta ad euro
	 */
	public Money() {
		amount = 0;
		currency = TypeOfCurrency.EUR;
	}
	
	/**
	 * COSTRUTTORE, inserito un ammontare di soldi setta la valuta di default a euro
	 * @param amount Ammontare di soldi
	 */
	public Money(double amount) {
		this.amount = amount;
		currency = TypeOfCurrency.EUR;
	}
	
	/**
	 * COSTRUTTORE, esso prende come parametro sia l'ammontare di soldi, sia la valuta specifica
	 * @param amount	Ammontare di soldi
	 * @param currency	Valuta
	 */
	public Money (double amount, TypeOfCurrency currency) {
		this.amount = amount;
		this.currency = currency;
	}
}