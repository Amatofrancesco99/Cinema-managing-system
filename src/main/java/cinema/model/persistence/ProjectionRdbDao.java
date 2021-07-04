package cinema.model.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import cinema.model.Movie;
import cinema.model.cinema.Room;
import cinema.model.persistence.util.PersistenceException;
import cinema.model.projection.Projection;

public class ProjectionRdbDao implements IProjectionDao{
	private Connection connection;
	
	public ProjectionRdbDao(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Projection getProjection(int id) throws SQLException, PersistenceException {
		String sql = "SELECT * FROM Projection WHERE id = ?;";
        PreparedStatement pstatement  = connection.prepareStatement(sql);
        pstatement.setInt(1, id);
        ResultSet result = pstatement.executeQuery();
        Movie movie = PersistenceFacade.getInstance().getMovie(result.getInt("movie"));
        Room room = PersistenceFacade.getInstance().getRoom(result.getInt("room"));
        Projection projection = new Projection(id, movie, LocalDateTime.parse(result.getString("datetime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), result.getDouble("price"), room );
        return projection;
	}

	@Override
	public ArrayList<Projection> getAllProjectionsByMovieId(int movieId) throws SQLException, PersistenceException {
		String sql = "SELECT * FROM Projection WHERE movie = ?;";
        PreparedStatement pstatement  = connection.prepareStatement(sql);
        pstatement.setInt(1, movieId);
        ResultSet result = pstatement.executeQuery();
        ArrayList<Projection> projections = new ArrayList<Projection>();
        while(result.next()) {
            Movie movie = PersistenceFacade.getInstance().getMovie(result.getInt("movie"));
            Room room = PersistenceFacade.getInstance().getRoom(result.getInt("room"));
            Projection projection = new Projection(movieId, movie, LocalDateTime.parse(result.getString("datetime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), result.getDouble("price"), room );
            projections.add(projection);
        }
		return null;
	}

	@Override
	public ArrayList<Projection> getAllProjections() throws SQLException, PersistenceException {
		String sql = "SELECT * FROM Projection;";
        PreparedStatement pstatement  = connection.prepareStatement(sql);
        ResultSet result = pstatement.executeQuery();
        ArrayList<Projection> projections = new ArrayList<Projection>();
        while(result.next()) {
            Movie movie = PersistenceFacade.getInstance().getMovie(result.getInt("movie"));
            Room room = PersistenceFacade.getInstance().getRoom(result.getInt("room"));
            Projection projection = new Projection(result.getInt("id"), movie, LocalDateTime.parse(result.getString("datetime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), result.getDouble("price"), room );
            projections.add(projection);
        }
		return projections;
	}

	@Override
	public void removeProjection(int id) throws SQLException {
		String sql = "DELETE FROM Projection WHERE id = ?;";
        PreparedStatement pstatement  = connection.prepareStatement(sql);
        pstatement.setInt(1, id);
        pstatement.executeUpdate();
	}

	@Override
	public void putProjection(Projection newProjection) throws SQLException {
		String sql = "INSERT INTO Projection(id, datetime, price, movie, room) VALUES(?, ?, ?, ?, ?);";
        PreparedStatement pstatement  = connection.prepareStatement(sql);
        pstatement.setInt(1, newProjection.getId());
        pstatement.setString(2, newProjection.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        pstatement.setDouble(3, newProjection.getPrice());
        pstatement.setInt(4, newProjection.getMovie().getId());
        pstatement.setInt(5, newProjection.getRoom().getNumber());
        pstatement.executeUpdate();
	}
}
