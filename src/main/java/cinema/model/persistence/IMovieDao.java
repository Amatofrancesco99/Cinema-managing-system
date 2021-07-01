package cinema.model.persistence;

import java.sql.SQLException;
import java.util.ArrayList;
import cinema.model.Movie;

public interface IMovieDao {
	public Movie getMovie(int id) throws SQLException;
	public ArrayList<Movie> getAllMovies() throws SQLException;
}
