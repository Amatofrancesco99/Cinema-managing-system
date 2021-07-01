package cinema.model.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import cinema.model.Movie;
import cinema.model.cinema.Room;
import cinema.model.cinema.util.RoomException;
import cinema.model.persistence.util.PersistenceException;

public class PersistenceFacade {
    Connection connection;
    IMovieDao iMovieDao;
    IRoomDao iRoomDao;
	
    public PersistenceFacade(String url) throws SQLException {
    	connection = DriverManager.getConnection(url);
    	iMovieDao = new MovieRdbDao(connection);
    	iRoomDao = new RoomRdbDao(connection);
    	
    }
    
    
    public Movie getMovie(int id) throws PersistenceException  {
    	try {
			return iMovieDao.getMovie(id);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine.");
		}
    }
    
    public ArrayList<Movie> getAllMovies() throws PersistenceException {
    	try {
			return iMovieDao.getAllMovies();
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
    }
    
    public Room getRoom(int id) throws PersistenceException {
    	try {
			return iRoomDao.getRoom(id);
		} catch (RoomException | SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine.");
		}
    }
    
    public ArrayList<Room> getAllRooms() throws PersistenceException {
    	try {
			return iRoomDao.getAllRooms();
		} catch (RoomException | SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
    }
    
    
    
}
