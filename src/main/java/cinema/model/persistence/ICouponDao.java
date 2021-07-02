package cinema.model.persistence;

import java.sql.SQLException;
import java.util.ArrayList;

import cinema.model.reservation.discount.coupon.Coupon;
import cinema.model.reservation.discount.coupon.util.CouponException;

public interface ICouponDao {
	public ArrayList<Coupon> getAllCoupons() throws SQLException, CouponException;
	public Coupon getCoupon(String promoCode) throws SQLException, CouponException;
	public void setCouponUsed(String promoCode) throws SQLException;
}
