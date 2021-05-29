package cinema.model.reservation.discount;

import cinema.model.Money;
import cinema.model.reservation.Reservation;

public interface ReservationDiscountStrategy {
	public Money getTotal(Reservation r);
}