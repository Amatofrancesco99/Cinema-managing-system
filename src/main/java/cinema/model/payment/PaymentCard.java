package cinema.model.payment;

import java.util.Date;

import cinema.model.Money;


public class PaymentCard {
	
	private String IBAN, accountHolder;
	private int securityCode;
	private Date expirationDate;
	
	public PaymentCard(String IBAN, String accountHolder, int securityCode, Date expirationDate) {
		this.IBAN = IBAN;
		this.accountHolder = accountHolder;
		this.securityCode = securityCode;
		this.expirationDate = expirationDate;
	}
		
	//to simplify, we do not ask to out credit card manager
	public boolean decreaseMoney(Money amount) {
		return true;
	}
}