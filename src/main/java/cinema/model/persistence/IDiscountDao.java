package cinema.model.persistence;

import java.sql.SQLException;

import cinema.model.reservation.discount.types.DiscountAge;
import cinema.model.reservation.discount.types.DiscountDay;
import cinema.model.reservation.discount.types.DiscountNumberSpectators;

public interface IDiscountDao {
	public DiscountDay getAllDayDiscounts() throws SQLException;
	public DiscountAge getAgeDiscounts() throws SQLException;
	public DiscountNumberSpectators getGroupDiscounts() throws SQLException;
}
