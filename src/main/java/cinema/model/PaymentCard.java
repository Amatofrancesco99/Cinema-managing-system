package cinema.model;

import java.util.Date;

public class PaymentCard {
	
	private String IBAN, accountHolder;
	private int securityCode;
	private Date expirationDate;
	
	public PaymentCard(String IBAN, String accountHolder, int securityCode, Date expirationDate) {
		this.IBAN=IBAN;
		this.accountHolder=accountHolder;
		this.securityCode=securityCode;
		this.expirationDate=expirationDate;
	}
}