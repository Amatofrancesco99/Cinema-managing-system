package cinema.model.persistence;

import java.sql.SQLException;

import cinema.model.cinema.util.RoomException;
import cinema.model.projection.Projection;
import cinema.model.reservation.Reservation;

public interface IOccupiedSeatDao {
	public void setOccupiedSeats(Projection projection) throws SQLException;
	public void putOccupiedSeatsFromReservation(Reservation reservation) throws SQLException, RoomException;
	public boolean getOccupiedSeat(int projectionId, int row, int column) throws SQLException;

}
