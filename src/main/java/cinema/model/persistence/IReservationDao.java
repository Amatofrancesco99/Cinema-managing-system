package cinema.model.persistence;

import java.sql.SQLException;

import cinema.model.reservation.Reservation;

public interface IReservationDao {
	public void putReservation(Reservation newReservation) throws SQLException;
	public long getLastReservationId() throws SQLException;
}
