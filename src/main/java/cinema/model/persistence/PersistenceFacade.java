package cinema.model.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import cinema.model.Movie;
import cinema.model.cinema.Room;
import cinema.model.cinema.util.RoomException;
import cinema.model.persistence.util.PersistenceException;
import cinema.model.projection.Projection;

public class PersistenceFacade {
    Connection connection;
    IMovieDao iMovieDao;
    IRoomDao iRoomDao;
    IProjectionDao iProjectionDao;
	
    public PersistenceFacade(String url) throws SQLException {
    	connection = DriverManager.getConnection(url);
    	iMovieDao = new MovieRdbDao(connection);
    	iRoomDao = new RoomRdbDao(connection);
    	iProjectionDao = new ProjectionRdbDao(connection);
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
    
    
	public Projection getProjection(int projectionId) throws PersistenceException {
		try {
			return this.iProjectionDao.getProjection(projectionId);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}
	
	
	public ArrayList<Projection> getAllProjectionsByMovieId(int movieId) throws PersistenceException {
		try {
			return this.iProjectionDao.getAllProjectionsByMovieId(movieId);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}
	
	
	public ArrayList<Projection> getAllProjections() throws PersistenceException {
		try {
			return this.iProjectionDao.getAllProjections();
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}
	
	
	public void removeProjection(int projectionId) throws PersistenceException {
		try {
			this.iProjectionDao.removeProjection(projectionId);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}
	
	
	public void putProjection(Projection newProjection) throws PersistenceException {
		try {
			this.iProjectionDao.putProjection(newProjection);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}
}
