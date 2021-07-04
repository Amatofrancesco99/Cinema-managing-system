package cinema.model.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import cinema.model.reservation.Reservation;

public class ReservationRdbDao implements IReservationDao{
	private Connection connection;
	
	
	public ReservationRdbDao(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void putReservation(Reservation newReservation) throws SQLException {
		String sql = "INSERT INTO Reservation(id, date, projection, name, surname, email, paymentcardowner, paymentcard, coupon, discount, numberpeopleunderage, nuberpeopleoverage) VALUES();";
        
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
