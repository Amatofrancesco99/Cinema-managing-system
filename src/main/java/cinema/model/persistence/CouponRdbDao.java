package cinema.model.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cinema.model.reservation.discount.coupon.Coupon;
import cinema.model.reservation.discount.coupon.util.CouponException;

public class CouponRdbDao implements ICouponDao{
	private Connection connection;
	
	public CouponRdbDao(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public Coupon getCoupon(String promocode) throws SQLException, CouponException {
		String sql = "SELECT FROM Coupon WHERE promocode = ?";
        PreparedStatement pstatement  = connection.prepareStatement(sql);
        pstatement.setString(1, promocode);
        ResultSet result = pstatement.executeQuery();
        // Il coustruttore di default di coupon setta a true i nuovi coupon, bisogna
        // cambiarlo
        Coupon coupon = new Coupon(promocode, result.getDouble("percentage"));
		return coupon;
	}

	@Override
	public void setCouponUsed(String promocode) throws SQLException {
		String sql = "UPDATE Coupon SET used=1 WHERE promocode = ?";
        PreparedStatement pstatement  = connection.prepareStatement(sql);
        pstatement.setString(1, promocode);
        pstatement.executeQuery();
	}

}
