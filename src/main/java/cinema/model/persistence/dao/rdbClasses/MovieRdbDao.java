package cinema.model.persistence.dao.rdbClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import cinema.model.Movie;
import cinema.model.persistence.dao.interfaces.IMovieDao;

/**
 * Si interfaccia con un database relazionale per implementare la persistenza
 * dei dati dei film gestiti dall'applicazione.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class MovieRdbDao implements IMovieDao {

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
	public MovieRdbDao(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Esegue la query sul database relazionale per recuperare le informazioni sul
	 * film identificato da {@code id}.
	 */
	@Override
	public Movie getMovie(int id) throws SQLException {
		String sql = "SELECT * FROM Movie WHERE id = ?;";
		PreparedStatement pstatement = connection.prepareStatement(sql);
		pstatement.setInt(1, id);
		ResultSet result = pstatement.executeQuery();
		ArrayList<String> genres = new ArrayList<String>(Arrays.asList(result.getString("genres").split(",")));
		ArrayList<String> cast = new ArrayList<String>(Arrays.asList(result.getString("cast").split(",")));
		ArrayList<String> directors = new ArrayList<String>(Arrays.asList(result.getString("directors").split(",")));
		Movie movie = new Movie(id, result.getString("title"), result.getString("description"), genres, directors, cast,
				result.getInt("rating"), result.getInt("duration"), result.getString("imageurl"),
				result.getString("trailerurl"));
		return movie;
	}

	/**
	 * Esegue la query per recuperare tutti i film presenti sul database
	 * relazionale.
	 */
	@Override
	public ArrayList<Movie> getAllMovies() throws SQLException {
		String sql = "SELECT * FROM Movie;";
		PreparedStatement pstatement = connection.prepareStatement(sql);
		ResultSet result = pstatement.executeQuery();
		ArrayList<Movie> movies = new ArrayList<Movie>();
		while (result.next()) {
			ArrayList<String> genres = new ArrayList<String>(Arrays.asList(result.getString("genres").split(",")));
			ArrayList<String> cast = new ArrayList<String>(Arrays.asList(result.getString("cast").split(",")));
			ArrayList<String> directors = new ArrayList<String>(
					Arrays.asList(result.getString("directors").split(",")));
			movies.add(new Movie(result.getInt("id"), result.getString("title"), result.getString("description"),
					genres, directors, cast, result.getInt("rating"), result.getInt("duration"),
					result.getString("imageurl"), result.getString("trailerurl")));
		}
		return movies;
	}

}
