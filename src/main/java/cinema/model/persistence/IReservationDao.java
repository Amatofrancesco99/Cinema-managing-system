package cinema.model.persistence;

import java.sql.SQLException;

import cinema.model.cinema.util.RoomException;
import cinema.model.persistence.util.PersistenceException;
import cinema.model.reservation.Reservation;

public interface IReservationDao {
	public void putEmptyReservation(Reservation newReservation) throws SQLException;
	public void setReservationFields(Reservation reservation) throws SQLException, PersistenceException, RoomException;
	public long getLastReservationId() throws SQLException;
}
