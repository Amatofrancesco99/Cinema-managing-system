package cinema.model.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import cinema.model.Movie;
import cinema.model.cinema.Room;
import cinema.model.cinema.util.RoomException;
import cinema.model.persistence.util.PersistenceException;
import cinema.model.projection.Projection;
import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.coupon.Coupon;
import cinema.model.reservation.discount.coupon.util.CouponException;
import cinema.model.reservation.discount.types.DiscountAge;
import cinema.model.reservation.discount.types.DiscountDay;
import cinema.model.reservation.discount.types.DiscountNumberSpectators;

public class PersistenceFacade {
    Connection connection;
    IMovieDao iMovieDao;
    IRoomDao iRoomDao;
    IProjectionDao iProjectionDao;
    ICouponDao iCouponDao;
    IDiscountDao iDiscountDao;
    IOccupiedSeatDao iOccupiedSeatDao;
    IReservationDao iReservationDao;
    ICinemaDao iCinemaDao;
	private static PersistenceFacade singleInstance;
    
    private PersistenceFacade(String url) throws SQLException {
    	connection = DriverManager.getConnection(url);
    	iMovieDao = new MovieRdbDao(connection);
    	iRoomDao = new RoomRdbDao(connection);
    	iProjectionDao = new ProjectionRdbDao(connection);
    	iCouponDao = new CouponRdbDao(connection);
    	iDiscountDao = new DiscountRdbDao(connection);
    	iOccupiedSeatDao = new OccupiedSeatRdbDao(connection);
    	iReservationDao = new ReservationRdbDao(connection);
    	iCinemaDao = new CinemaRdbDao(connection);
    }
    
    public static PersistenceFacade getInstance() throws SQLException {
    	if (singleInstance == null) {
    		singleInstance = new PersistenceFacade("jdbc:sqlite:persistence/cinemaDb.db");
    	}
    	return singleInstance;
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
			e.printStackTrace();
			//throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}
	
	public ArrayList<Coupon> getAllCoupons() throws PersistenceException {
		try {
			return this.iCouponDao.getAllCoupons();
		} catch (SQLException | CouponException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}
	
	
	public Coupon getCoupon(String promocode) throws PersistenceException {
		try {
			return this.iCouponDao.getCoupon(promocode);
		} catch (SQLException | CouponException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}
	
	
	public void setCouponUsed(String promocode) throws PersistenceException {
		try {
			this.iCouponDao.setCouponUsed(promocode);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}
	

	public DiscountDay getAllDayDiscounts() throws PersistenceException{
		try {
			return this.iDiscountDao.getAllDayDiscounts();
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}
	
	public DiscountAge getAgeDiscounts() throws PersistenceException{
		try {
			return this.iDiscountDao.getAgeDiscounts();
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}
	
	public DiscountNumberSpectators getGroupDiscounts() throws PersistenceException{
		try {
			return this.iDiscountDao.getGroupDiscounts();
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}
	
	
	public void setOccupiedSeats(Projection projection) throws PersistenceException{
		try {
			this.iOccupiedSeatDao.setOccupiedSeats(projection);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}
	
	public boolean getOccupiedSeat(int projectionId, int row, int column) throws PersistenceException {
		try {
			return this.iOccupiedSeatDao.getOccupiedSeat(projectionId, row, column);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}
	
	public long getLastReservationId() throws PersistenceException{
		try {
			return this.iReservationDao.getLastReservationId();
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}
	
	public void deleteReservation(long reservationId) throws PersistenceException{
		try {
			iReservationDao.deleteReservation(reservationId);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}
	
	
	public void putEmptyReservation(Reservation newReservation) throws PersistenceException{
		try {
			iReservationDao.putEmptyReservation(newReservation);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}
	
	public void setReservationFields(Reservation reservation) throws PersistenceException, RoomException{
		try {
			iReservationDao.setReservationFields(reservation);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}
	
	public void putOccupiedSeatsFromReservation(Reservation reservation) throws PersistenceException, RoomException{
		try {
			iOccupiedSeatDao.putOccupiedSeatsFromReservation(reservation);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}
	
	
	public HashMap<String, String> getAllCinemaInfo(int cinemaId) throws PersistenceException{
		try {
			return iCinemaDao.getAllCinemaInfo(cinemaId);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}
	
	
	public void setPassword(int cinemaId, String newPassword) throws PersistenceException {
		try {
			iCinemaDao.setPassword(cinemaId, newPassword);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}
	
	
	public void setDiscountStrategy(int cinemaId, String discountStrategyName) throws PersistenceException {
		try {
			iCinemaDao.setDiscountStrategy(cinemaId, discountStrategyName);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}
}