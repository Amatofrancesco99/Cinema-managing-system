package cinema.model.reservation.discount.types;

import cinema.model.Money;
import cinema.model.Spectator;
import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.ReservationDiscountStrategy;

public class DiscountAge implements ReservationDiscountStrategy{

	private final int MIN_AGE = 5;
	private final int MAX_AGE = 80;
	private final float PERCENTAGE = (float) 0.85;
	
	@Override
	public Money getTotal(Reservation r) {
		float totalPrice = 0;
		for(Spectator s: r.getSpectators()) {
			if((s.getAge()<=MIN_AGE)||(s.getAge()>=MAX_AGE)){
				totalPrice+=r.getProjection().getAmount()*PERCENTAGE;
			}
			else totalPrice+=r.getProjection().getAmount();
		}
		return new Money(totalPrice,r.getProjection().getCurrency());
	}

}