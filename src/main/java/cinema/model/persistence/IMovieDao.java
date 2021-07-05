package cinema.model.persistence;

import java.sql.SQLException;
import java.util.ArrayList;
import cinema.model.Movie;

/**
 * Contiene i metodi necessari per mantenere la persistenza dei dati riguardanti
 * i film gestiti dall'applicazione.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public interface IMovieDao {
	/**
	 * Restituice un film sulla base del suo identificativo.
	 * 
	 * @param id identificativo del film che si vuole ottenere.
	 * @return un film sulla base del suo identificativo o null se non è presente.
	 * @throws SQLException se vengono riscontrati errori nell'interazione col
	 *                      meccanismo di persistenza.
	 */
	public Movie getMovie(int id) throws SQLException;

	/**
	 * Restituisce tutti i film mantenuti dal meccanismo di persistenza.
	 * 
	 * @return tutti i film del meccanismo di persistenza.
	 * @throws SQLException se vengono riscontrati errori nell'interazione col
	 *                      meccanismo di persistenza.
	 */
	public ArrayList<Movie> getAllMovies() throws SQLException;
}
