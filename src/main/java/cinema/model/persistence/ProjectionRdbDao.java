package cinema.model.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import cinema.model.Movie;
import cinema.model.cinema.Room;
import cinema.model.cinema.util.RoomException;
import cinema.model.projection.Projection;

public class ProjectionRdbDao implements IProjectionDao{
	private Connection connection;
	
	public ProjectionRdbDao(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Projection getProjection(int id) throws SQLException {
		String sql = "SELECT * FROM Projection WHERE id = ?;";
        PreparedStatement pstatement  = connection.prepareStatement(sql);
        pstatement.setInt(1, id);
        ResultSet result = pstatement.executeQuery();
        
        //(int id, Movie movie, LocalDateTime dateTime, double price, Room room)
        Projection projection = new Projection();
        return projection;
	}

	@Override
	public ArrayList<Projection> getAllProjectionsByMovieId(int movieId) throws SQLException {
		String sql = "SELECT * FROM Projection WHERE movie = ?;";
        PreparedStatement pstatement  = connection.prepareStatement(sql);
        pstatement.setInt(1, movieId);
        ResultSet result = pstatement.executeQuery();
        
        
        
        
		return null;
	}

	@Override
	public ArrayList<Projection> getAllProjections() throws SQLException {
		String sql = "SELECT * FROM Projection;";
        PreparedStatement pstatement  = connection.prepareStatement(sql);
        ResultSet result = pstatement.executeQuery();
		
		return null;
	}

	@Override
	public void removeProjection(int id) throws SQLException {
		String sql = "DELETE FROM Projection WHERE id = ?;";
        PreparedStatement pstatement  = connection.prepareStatement(sql);
        pstatement.setInt(1, id);
        pstatement.executeQuery();
	}

	@Override
	public void putProjection(Projection newProjection) throws SQLException {
		String sql = "INSERT INTO Projection(id, datetime, price, Movie, room)VALUES(?, ?, ?, ?, ?);";
        PreparedStatement pstatement  = connection.prepareStatement(sql);
        pstatement.setInt(1, newProjection.getId());
        pstatement.setString(2, newProjection.getDateTime().toString());
        pstatement.setDouble(3, newProjection.getPrice());
        pstatement.setInt(4, newProjection.getMovie().getId());
        pstatement.setInt(5, newProjection.getRoom().getNumber());
        pstatement.executeQuery();
	}
}
