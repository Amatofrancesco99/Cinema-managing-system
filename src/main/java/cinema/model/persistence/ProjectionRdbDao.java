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

/**
 * Si interfaccia con un database relazionale per implementare la persistenza
 * delle informazioni sulle proiezioni gestite dall'applicazione.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class ProjectionRdbDao implements IProjectionDao {
	/**
	 * Connessione al database.
	 */
	private Connection connection;

	/**
	 * Costruttore dell'interfaccia verso il database relazionale.
	 * 
	 * @param connection connessione al database relazionale che impelemta la
	 *                   persistenza delle informazioni.
	 */
	public ProjectionRdbDao(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Esegue la query sul database relazionale per recuperare le informazioni sulla
	 * proiezione identificata da {@code id}.
	 */
	@Override
	public Projection getProjection(int id) throws SQLException, PersistenceException {
		String sql = "SELECT * FROM Projection WHERE id = ?;";
		PreparedStatement pstatement = connection.prepareStatement(sql);
		pstatement.setInt(1, id);
		ResultSet result = pstatement.executeQuery();
		Movie movie = PersistenceFacade.getInstance().getMovie(result.getInt("movie"));
		Room room = PersistenceFacade.getInstance().getRoom(result.getInt("room"));
		Projection projection = new Projection(id, movie,
				LocalDateTime.parse(result.getString("datetime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
				result.getDouble("price"), room);
		PersistenceFacade.getInstance().setOccupiedSeats(projection);
		return projection;
	}

	/**
	 * Esegue la query sul database relazionale per recuperare le informazioni sulle
	 * proiezioni riguardanti un determinato film identificato da {@code movieId}.
	 */
	@Override
	public ArrayList<Projection> getAllProjectionsByMovieId(int movieId) throws SQLException, PersistenceException {
		String sql = "SELECT * FROM Projection WHERE movie = ?;";
		PreparedStatement pstatement = connection.prepareStatement(sql);
		pstatement.setInt(1, movieId);
		ResultSet result = pstatement.executeQuery();
		ArrayList<Projection> projections = new ArrayList<Projection>();
		while (result.next()) {
			Movie movie = PersistenceFacade.getInstance().getMovie(result.getInt("movie"));
			Room room = PersistenceFacade.getInstance().getRoom(result.getInt("room"));
			Projection projection = new Projection(movieId, movie, LocalDateTime.parse(result.getString("datetime"),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), result.getDouble("price"), room);
			PersistenceFacade.getInstance().setOccupiedSeats(projection);
			projections.add(projection);
		}
		return projections;
	}

	/**
	 * Esegue la query sul database relazionale per recuperare le informazioni su
	 * tutte le proiezioni gestite dall'applicazione.
	 */
	@Override
	public ArrayList<Projection> getAllProjections() throws SQLException, PersistenceException {
		String sql = "SELECT * FROM Projection;";
		PreparedStatement pstatement = connection.prepareStatement(sql);
		ResultSet result = pstatement.executeQuery();
		ArrayList<Projection> projections = new ArrayList<Projection>();
		while (result.next()) {
			Movie movie = PersistenceFacade.getInstance().getMovie(result.getInt("movie"));
			Room room = PersistenceFacade.getInstance().getRoom(result.getInt("room"));
			Projection projection = new Projection(result.getInt("id"), movie, LocalDateTime
					.parse(result.getString("datetime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
					result.getDouble("price"), room);
			PersistenceFacade.getInstance().setOccupiedSeats(projection);
			projections.add(projection);
		}
		return projections;
	}

	/**
	 * Esegue la query sul database relazionale per implementare l'eliminazione da
	 * parte del gestore del cinema di una proiezione identificata da {@code id}.
	 */
	@Override
	public void removeProjection(int id) throws SQLException {
		String sql = "DELETE FROM Projection WHERE id = ?;";
		PreparedStatement pstatement = connection.prepareStatement(sql);
		pstatement.setInt(1, id);
		pstatement.executeUpdate();
	}

	/**
	 * Esegue la query sul database relazionale per implementare la persistenza
	 * delle informazioni di una proiezione quando questa viene creata dal gestore
	 * del cinema.
	 */
	@Override
	public void putProjection(Projection newProjection) throws SQLException {
		String sql = "INSERT INTO Projection(id, datetime, price, movie, room) VALUES(?, ?, ?, ?, ?);";
		PreparedStatement pstatement = connection.prepareStatement(sql);
		pstatement.setInt(1, newProjection.getId());
		pstatement.setString(2, newProjection.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		pstatement.setDouble(3, newProjection.getPrice());
		pstatement.setInt(4, newProjection.getMovie().getId());
		pstatement.setInt(5, newProjection.getRoom().getNumber());
		pstatement.executeUpdate();
	}
}
