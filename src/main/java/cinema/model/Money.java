package cinema.model;

import cinema.model.enumerations.TypeOfCurrency;

public class Money {
	private float amount;
	private TypeOfCurrency currency;
	
	//default
	public Money() {
		amount=0;
		currency=TypeOfCurrency.EUR;
	}
	
	//if you do not insert currency value the default currency are euros
	public Money(float amount) {
		this.amount=amount;
		currency=TypeOfCurrency.EUR;
	}
	
	public Money (float amount, TypeOfCurrency currency) {
		this.amount=amount;
		this.currency=currency;
	}
}