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

/**
 * Contiene i metodi necessari per interfacciare l'applicazione con un
 * meccanismo di persistenza dei dati.
 * 
 * La classe implementa tutti i metodi per implementare la persistenza dei dati
 * interfacciando l'applicazione col meccanismo di persistenza dei dati.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class PersistenceFacade {

	/**
	 * Connessione al meccanismo di persistenza dei dati.
	 */
	Connection connection;

	/**
	 * Interfaccia con la persistenza dei dati del film.
	 */
	IMovieDao iMovieDao;

	/**
	 * Interfaccia con la persistenza dei dati delle sale.
	 */
	IRoomDao iRoomDao;

	/**
	 * Interfaccia con la persistenza dei dati delle proiezioni.
	 */
	IProjectionDao iProjectionDao;

	/**
	 * Interfaccia con la persistenza dei coupon.
	 */
	ICouponDao iCouponDao;

	/**
	 * Interfaccia con la persistenza degli sconti.
	 */
	IDiscountDao iDiscountDao;

	/**
	 * Interfaccia con la persistenza dei posti occupati per ciascuna proiezione.
	 */
	IOccupiedSeatDao iOccupiedSeatDao;

	/**
	 * Interfaccia con la persistenza delle prenotazioni.
	 */
	IReservationDao iReservationDao;

	/**
	 * Interfaccia con la persistenza delle informazioni generali del cinema.
	 */
	ICinemaDao iCinemaDao;

	/**
	 * Unica istanza della classe necessaria a implementare il pattern singleton.
	 */
	private static PersistenceFacade singleInstance;

	/**
	 * Costruttore del facade controller che gestisce la persistenza dei dati.
	 * 
	 * @param url URI del meccanismo di persistenza dei dati.
	 * @throws SQLException se occorrono degli errori nella connessione al
	 *                      meccanismo di persistenza dei dati.
	 */
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

	/**
	 * Restituisce l'unico oggetto della classe che è permesso avere implementando
	 * il pattern singleton.
	 * 
	 * @return l'unica istanza della calsse permassa dal pattern singleton.
	 * @throws SQLException se occorrono degli errori nella connessione al
	 *                      meccanismo di persistenza dei dati.
	 */
	public static PersistenceFacade getInstance() throws SQLException {
		if (singleInstance == null) {
			singleInstance = new PersistenceFacade("jdbc:sqlite:persistence/cinemaDb.db");
		}
		return singleInstance;
	}

	/**
	 * Restituisce un film dato il suo identificativo recuperandolo dal meccanismo
	 * di persistenza dei dati.
	 * 
	 * @param id identificativo del film di sui si vogliono ottenere le
	 *           informazioni.
	 * @return il film identificato da {@code id} o null se non è presente.
	 * @throws PersistenceException se la richiesta al meccanismo di persistenza dei
	 *                              dati fallisce.
	 */
	public Movie getMovie(int id) throws PersistenceException {
		try {
			return iMovieDao.getMovie(id);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine.");
		}
	}

	/**
	 * Restituisce tutti i film che sono mantenuti dal meccanismo di persistenza dei
	 * dati.
	 * 
	 * @return tutti i film tenuti nel meccanismo di persistenza dei dati.
	 * @throws PersistenceException se la richiesta al meccanismo di persistenza dei
	 *                              dati fallisce.
	 */
	public ArrayList<Movie> getAllMovies() throws PersistenceException {
		try {
			return iMovieDao.getAllMovies();
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}

	/**
	 * Restituisce una sala dato il suo numero identificativo.
	 * 
	 * @param id identificativo della sala.
	 * @return la sala identificata da {@code id} o null se non è presente.
	 * @throws PersistenceException se la richiesta al meccanismo di persistenza dei
	 *                              dati fallisce.
	 */
	public Room getRoom(int id) throws PersistenceException {
		try {
			return iRoomDao.getRoom(id);
		} catch (RoomException | SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine.");
		}
	}

	/**
	 * Restituisce tutte le sale che sono mantenute dal meccanismo di persistenza
	 * dei dati.
	 * 
	 * @return tutte le sale mantenute nel meccanismo di persistenza dei dati.
	 * @throws PersistenceException se la richiesta al meccanismo di persistenza dei
	 *                              dati fallisce.
	 */
	public ArrayList<Room> getAllRooms() throws PersistenceException {
		try {
			return iRoomDao.getAllRooms();
		} catch (RoomException | SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}

	/**
	 * Restituisce una proiezione dato il suo identificativo tra quelle mantenute
	 * dal meccanismo di persistenza dei dati.
	 * 
	 * @param projectionId identificativo della proiezione che si vuole.
	 * @return la proiezione con identificativo {@code projectionId} o null se non
	 *         la proiezione non è presente.
	 * @throws PersistenceException se la richiesta al meccanismo di persistenza dei
	 *                              dati fallisce.
	 */
	public Projection getProjection(int projectionId) throws PersistenceException {
		try {
			return this.iProjectionDao.getProjection(projectionId);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}

	/**
	 * Restituisce tutte le proiezioni riguardanti un determinato film dato il suo
	 * identificativo.
	 * 
	 * @param movieId l'identificativo del film di cui si vogliono ottenere tutte le
	 *                proiezioni.
	 * @return le proiezioni riguardanti il film con identificativo {@code movieId}.
	 * @throws PersistenceException se la richiesta al meccanismo di persistenza dei
	 *                              dati fallisce.
	 */
	public ArrayList<Projection> getAllProjectionsByMovieId(int movieId) throws PersistenceException {
		try {
			return this.iProjectionDao.getAllProjectionsByMovieId(movieId);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}

	/**
	 * Restituisce tutte le proiezioni mantenute dal meccanismo di persistenza dei
	 * dati.
	 * 
	 * @return tutte le proiezioni mantenute dal meccanismo di persistenza.
	 * @throws PersistenceException se la richiesta al meccanismo di persistenza dei
	 *                              dati fallisce.
	 */
	public ArrayList<Projection> getAllProjections() throws PersistenceException {
		try {
			return this.iProjectionDao.getAllProjections();
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}

	/**
	 * Elimina una determinata proiezione sulla base del suo identificativo.
	 * 
	 * @param projectionId identificativo della proiezione da eliminare.
	 * @throws PersistenceException se la richiesta al meccanismo di persistenza dei
	 *                              dati fallisce.
	 */
	public void removeProjection(int projectionId) throws PersistenceException {
		try {
			this.iProjectionDao.removeProjection(projectionId);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}

	/**
	 * Rende persistente i dati di una proiezione.
	 * 
	 * @param newProjection proiezione da rendere persistente.
	 * @throws PersistenceException se la richiesta al meccanismo di persistenza dei
	 *                              dati fallisce.
	 */
	public void putProjection(Projection newProjection) throws PersistenceException {
		try {
			this.iProjectionDao.putProjection(newProjection);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buonfine");
		}
	}

	/**
	 * Restituisce tutte i coupon mantenuti dal meccanismo di persistenza dei dati.
	 * 
	 * @return tutti i coupon mantenuti dal meccanismo di persistenza.
	 * @throws PersistenceException se la richiesta al meccanismo di persistenza dei
	 *                              dati fallisce.
	 */
	public ArrayList<Coupon> getAllCoupons() throws PersistenceException {
		try {
			return this.iCouponDao.getAllCoupons();
		} catch (SQLException | CouponException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}

	/**
	 * Restituisce un coupon sulla base del suo codice promozionale.
	 * 
	 * @param promocode codice promozionale del coupon da restituire.
	 * @return un coupon con identificativo {@code promocode} o null se questo non è
	 *         presente.
	 * @throws PersistenceException se la richiesta al meccanismo di persistenza dei
	 *                              dati fallisce.
	 */
	public Coupon getCoupon(String promocode) throws PersistenceException {
		try {
			return this.iCouponDao.getCoupon(promocode);
		} catch (SQLException | CouponException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}

	/**
	 * Rende persistente la proprietà di un coupon di essere stato utilizzato.
	 * 
	 * @param promocode codice promozionale del coupon che è stato usato.
	 * @throws PersistenceException se la richiesta al meccanismo di persistenza dei
	 *                              dati fallisce.
	 */
	public void setCouponUsed(String promocode) throws PersistenceException {
		try {
			this.iCouponDao.setCouponUsed(promocode);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}

	/**
	 * Restituisce tutti gli sconti fatti sulla base del giorno della proiezione che
	 * sono mantenuti dal meccanismo di persistenza dei dati.
	 * 
	 * @return tutti gli sconti fatti sulla base del giorno che sono mantenuti dal
	 *         meccanismo di persistenza dei dati o null se non ne è presente
	 *         nessuno.
	 * @throws PersistenceException se la richiesta al meccanismo di persistenza dei
	 *                              dati fallisce.
	 */
	public DiscountDay getAllDayDiscounts() throws PersistenceException {
		try {
			return this.iDiscountDao.getAllDayDiscounts();
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}

	/**
	 * Restituisce tutti gli sconti fatti sulla base dell'età degli spettatori che
	 * sono mantenuti dal meccanismo di persistenza dei dati.
	 * 
	 * @return tutti gli sconti fatti sulla base dell'età degli spettatori che sono
	 *         mantenuti dal meccanismo di persistenza dei dati o null se non ne è
	 *         presente nessuno.
	 * @throws PersistenceException se la richiesta al meccanismo di persistenza dei
	 *                              dati fallisce.
	 */
	public DiscountAge getAgeDiscounts() throws PersistenceException {
		try {
			return this.iDiscountDao.getAgeDiscounts();
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}

	/**
	 * Restituisce tutti gli sconti fatti sulla base del numero di biglietti
	 * acquistati che sono mantenuti dal meccanismo di persistenza dei dati.
	 * 
	 * @return tutti gli sconti fatti sulla base del numero di biglietti acquistati
	 *         che sono mantenuti dal meccanismo di persistenza dei dati o null se
	 *         non ne è presente nessuno.
	 * @throws PersistenceException se la richiesta al meccanismo di persistenza dei
	 *                              dati fallisce.
	 */
	public DiscountNumberSpectators getGroupDiscounts() throws PersistenceException {
		try {
			return this.iDiscountDao.getGroupDiscounts();
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}

	/**
	 * Occupa i posti di una proiezione in modo da sincronizzare i dati locali con i
	 * dati mantenuti dal meccanismo di persistenza dei dati.
	 * 
	 * @param projection proiezione di cui si vuole occupare i posti.
	 * @throws PersistenceException se la richiesta al meccanismo di persistenza dei
	 *                              dati fallisce.
	 */
	public void setOccupiedSeats(Projection projection) throws PersistenceException {
		try {
			this.iOccupiedSeatDao.setOccupiedSeats(projection);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}

	public boolean getOccupiedSeat(int projectionId, int row, int column) throws PersistenceException {
		try {
			return this.iOccupiedSeatDao.getSeatOccupationStatus(projectionId, row, column);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}

	public long getLastReservationId() throws PersistenceException {
		try {
			return this.iReservationDao.getLastReservationId();
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}

	public void deleteReservation(long reservationId) throws PersistenceException {
		try {
			iReservationDao.deleteReservation(reservationId);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}

	public void putEmptyReservation(Reservation newReservation) throws PersistenceException {
		try {
			iReservationDao.putEmptyReservation(newReservation);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}

	public void setReservationFields(Reservation reservation) throws PersistenceException, RoomException {
		try {
			iReservationDao.setReservationFields(reservation);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}

	public void putOccupiedSeatsFromReservation(Reservation reservation) throws PersistenceException, RoomException {
		try {
			iOccupiedSeatDao.putOccupiedSeatsFromReservation(reservation);
		} catch (SQLException e) {
			throw new PersistenceException("La richiesta al database non è andata a buon fine");
		}
	}

	public HashMap<String, String> getAllCinemaInfo(int cinemaId) throws PersistenceException {
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