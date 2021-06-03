package cinema.model;

import cinema.model.enumerations.TypeOfCurrency;
import lombok.Data;

@Data
public class Money {
	
	private double amount;
	private TypeOfCurrency currency;
	
	//default
	public Money() {
		amount = 0;
		currency = TypeOfCurrency.EUR;
	}
	
	//if you do not insert currency value the default currency are euros
	public Money(double amount) {
		this.amount = amount;
		currency = TypeOfCurrency.EUR;
	}
	
	public Money (double amount, TypeOfCurrency currency) {
		this.amount = amount;
		this.currency = currency;
	}
}