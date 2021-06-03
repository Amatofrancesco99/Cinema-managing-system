package cinema.model.reservation.discount.types;

import java.time.LocalDate;

import cinema.model.Money;
import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.ReservationDiscountStrategy;

public class DiscountDay implements ReservationDiscountStrategy{

	private LocalDate start, end, day;
	private final float PERCENTAGE = (float) 0.90;
	
	@Override
	public Money getTotal(Reservation r) {
		float totalPrice = 0;
		if(((r.getDate().compareTo(start)>=0) && (r.getDate().compareTo(end)<=0)) 
		|| (r.getDate().compareTo(day)==0)) {
			totalPrice+=r.getProjection().getPrice().getAmount()*PERCENTAGE;
		}
		else totalPrice+=r.getProjection().getPrice().getAmount();
		return new Money(totalPrice,r.getProjection().getPrice().getCurrency());
	}
	
	public void setDateDiscount(LocalDate start, LocalDate end) {
		this.start = start;
		this.end = end;
	}
	public void setDayDiscount(LocalDate day) {
		this.day = day;
	}
}
