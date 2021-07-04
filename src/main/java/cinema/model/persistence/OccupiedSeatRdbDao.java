package cinema.model.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cinema.model.cinema.util.RoomException;
import cinema.model.projection.Projection;
import cinema.model.reservation.Reservation;

public class OccupiedSeatRdbDao implements IOccupiedSeatDao{
	private Connection connection;
	
	public OccupiedSeatRdbDao(Connection connection) {
		this.connection = connection;
	}
	
	public void setOccupiedSeats(Projection projection) throws SQLException {
		String sql = "SELECT * FROM OccupiedSeat WHERE projection = ?";
        PreparedStatement pstatement  = connection.prepareStatement(sql);
        pstatement.setInt(1, projection.getId());
        ResultSet result = pstatement.executeQuery();
        while(result.next()) {
        	int row = result.getInt("row");
        	int column = result.getInt("column");
        	try {
				projection.takeSeat(row, column);
			} catch (RoomException e) {
				// Trattando dati sul database non si entra mai in questo catch essendo che non si scatena
				// mai l'eccezione siccome i dati sono validi se sono nel database 
				System.out.println(e.getMessage());
			}
        }
        
	}


	@Override
	public boolean getOccupiedSeat(int projectionId, int row, int column) throws SQLException{
		String sql = "SELECT * FROM OccupiedSeat WHERE projection = ? AND row = ? AND column = ?;";
        	PreparedStatement pstatement  = connection.prepareStatement(sql);
	        pstatement.setInt(1, projectionId);
	        pstatement.setInt(2, row);
	        pstatement.setInt(3, column);
			ResultSet result = pstatement.executeQuery();
			if(result.next())
				return false;
			return true;
	}
}
