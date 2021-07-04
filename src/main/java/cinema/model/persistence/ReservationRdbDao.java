package cinema.model.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import cinema.model.reservation.Reservation;

public class ReservationRdbDao implements IReservationDao{
	private Connection connection;
	
	
	public ReservationRdbDao(Connection connection) {
		this.connection = connection;
	}
	


	@Override
	public void setReservationFields(Reservation reservation) throws SQLException {
		String sql = "UPDATE Reservation SET date = ?, projection = ?, name = ?, surname = ?, email = ?, paymentcardowner = ?, paymentcard = ?, coupon = ?, discount = ?, numberpeopleunderage = ?, nuberpeopleoverage = ? WHERE id = ?;";
		PreparedStatement pstatement  = connection.prepareStatement(sql);
        
		pstatement.setString(1, new SimpleDateFormat("yyyy-MM-dd").format(reservation.getDate()));
        pstatement.setLong(2, reservation.getProjection().getId());
        pstatement.setString(3, reservation.getPurchaser().getName());
        pstatement.setString(4, reservation.getPurchaser().getSurname());
        pstatement.setString(5, reservation.getPurchaser().getEmail());
        pstatement.setString(6, reservation.getPaymentCard().getOwner());
        pstatement.setString(7, reservation.getPaymentCard().getNumber());
        pstatement.setString(8, reservation.getCoupon().getCode());
        pstatement.setInt(9, reservation.getDiscountId());
        pstatement.setInt(10, reservation.getNumberPeopleUntilMinAge());
        pstatement.setLong(11, reservation.getNumberPeopleOverMaxAge());
        pstatement.setLong(12, reservation.getProgressive());
        pstatement.executeUpdate();
	}
	
	
	@Override
	public void putEmptyReservation(Reservation newReservation) throws SQLException {
		String sql = "INSERT INTO Reservation(id) VALUES(?);";
		PreparedStatement pstatement  = connection.prepareStatement(sql);
        pstatement.setLong(1, newReservation.getProgressive());
        pstatement.executeUpdate();
	}

	@Override
	public long getLastReservationId() throws SQLException {
		String sql = "SELECT MAX(id) AS maxid FROM Reservation;";
        PreparedStatement pstatement  = connection.prepareStatement(sql);
        return pstatement.executeQuery().getLong("maxid");
	}
}
