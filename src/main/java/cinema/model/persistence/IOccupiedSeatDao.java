package cinema.model.persistence;

import java.sql.SQLException;

import cinema.model.projection.Projection;

public interface IOccupiedSeatDao {
	public void setOccupiedSeats(Projection projection) throws SQLException;
	//public void putOccupiedSeatsFromReservation(ARGOMENTI) throws SQLException;
	public boolean getOccupiedSeat(int projectionId, int row, int column) throws SQLException;

}
