package cinema.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cinema.controller.util.*;
import cinema.controller.handlers.EmailHandler;
import cinema.controller.handlers.util.HandlerException;
import cinema.model.cinema.Room;
import cinema.model.cinema.util.RoomException;
import cinema.model.Movie;
import cinema.model.payment.util.PaymentErrorException;
import cinema.model.persistence.PersistenceFacade;
import cinema.model.persistence.util.PersistenceException;
import cinema.model.projection.Projection;
import cinema.model.projection.util.ProjectionException;
import cinema.model.reservation.discount.IReservationDiscountStrategy;
import cinema.model.reservation.discount.coupon.Coupon;
import cinema.model.reservation.discount.coupon.util.CouponException;
import cinema.model.reservation.discount.types.Discount;
import cinema.model.reservation.discount.types.util.DiscountException;
import cinema.model.reservation.discount.types.util.TypeOfDiscount;
import cinema.model.reservation.Reservation;
import cinema.model.reservation.util.ReservationException;
import cinema.model.reservation.util.SeatAvailabilityException;
import cinema.model.spectator.Spectator;
import cinema.model.spectator.util.InvalidSpectatorInfoException;

/**
 * Rappresenta il controllore che consente alle viste di poter eseguire
 * operazioni utili, senza dover conoscere la logica di dominio.
 * 
 * 
 * @author Screaming Hairy Armadillo Team
 * 
 */
public class Cinema {

	/**
	 * HashMap contenente le informazioni generali del cinema.
	 */
	private HashMap<String, String> cinemaInfo;

	/**
	 * Gestisce l'invio dell'e-mail da parte del cinema.
	 */
	private EmailHandler emailHandler;

	/**
	 * HashMap contenente le informazioni della proiezione associata all'id.
	 */
	private HashMap<Integer, Projection> newProjections;

	/**
	 * Lista contenente tutte le prenotazioni generate dal cinema.
	 */
	private List<Reservation> cinemaReservations;

	/**
	 * Corrente strategia di sconto applicata dal cinema.
	 */
	private IReservationDiscountStrategy cinemaDiscount;

	/**
	 * Permette la gestione della persistenza dei dati.
	 */
	private PersistenceFacade persistenceFacade;

	/**
	 * Costruttore del cinema.
	 */
	public Cinema() {
		try {
			persistenceFacade = new PersistenceFacade("jdbc:sqlite:persistence/cinemaDb.db");
			cinemaInfo = persistenceFacade.getAllCinemaInfo(1);
			cinemaDiscount = getDiscountByStrategy(TypeOfDiscount.valueOf(cinemaInfo.get("discountStrategy")));
		} catch (SQLException | PersistenceException | DiscountNotFoundException e) {
			System.out.println(e.getMessage());
		}
		emailHandler = new EmailHandler(cinemaInfo.get("name"), cinemaInfo.get("email"), cinemaInfo.get("mailPassword"),
				getLocation(), cinemaInfo.get("logoURL"));
		cinemaReservations = new ArrayList<Reservation>();
		newProjections = new HashMap<>();
	}

	/**
	 * Crea una nuova prenotazione (vuota), a partire dalla classe cinema.
	 * 
	 * @return la nuova prenotazione creata.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public long createReservation() throws PersistenceException {
		Reservation r = new Reservation(cinemaDiscount, persistenceFacade.getLastReservationId() + 1);
		cinemaReservations.add(r);
		persistenceFacade.putEmptyReservation(r);
		return r.getProgressive();
	}

	/**
	 * Restituisce una prenotazione, dato il suo id.
	 * 
	 * @param progressive numero di prenotazione(id).
	 * @return la prenotazione corrispondente all'id inserito.
	 * @throws ReservationException qualora l'id della prenotazione inserita non
	 *                              esista.
	 */
	public Reservation getReservation(long progressive) throws ReservationException {
		for (Reservation r : cinemaReservations) {
			if (r.getProgressive() == progressive)
				return r;
		}
		throw new ReservationException("La prenotazione " + progressive + " non esiste.");
	}

	/**
	 * Rimuove una proiezione dal cinema, dato il suo id.
	 * 
	 * @param projectionId identificativo della proiezione.
	 * @throws ProjectionException  qualora vi siano errori riscontrati nelle
	 *                              procedure di interazione con gli oggetti che
	 *                              rappresentano le proiezioni.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public void removeProjection(int projectionId) throws ProjectionException, PersistenceException {
		persistenceFacade.removeProjection(projectionId);
	}

	/**
	 * Restituisce tutte le proiezioni di cui il cinema dispone.
	 * 
	 * @return la lista di tutte le proiezioni del cinema.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public List<Projection> getProjections() throws PersistenceException {
		return persistenceFacade.getAllProjections();
	}

	/**
	 * Crea una proiezione e gli imposta l'id.
	 * 
	 * @param projectionId identificativo della proiezione.
	 * @throws ProjectionException  qualora la proiezione con l'id inserito sia già
	 *                              esistente.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public void createProjectionWithID(int projectionId) throws ProjectionException, PersistenceException {
		for (Projection projection : getProjections()) {
			if (projection.getId() == projectionId)
				throw new ProjectionException("La proiezione con id " + projectionId + " è già esistente.");
		}
		Projection newProjection = new Projection();
		newProjection.setId(projectionId);
		newProjections.put(projectionId, newProjection);
	}

	/**
	 * Associa un film ad una proiezione.
	 * 
	 * @param projectionId codice identificativo della proiezione.
	 * @param movieId      codice identificativo del film.
	 * @throws NoMovieException    qualora non si trovi nessun film, dato l'id.
	 * @throws ProjectionException qualora la proiezione inserita non esista.
	 */
	public void setProjectionMovie(int projectionId, int movieId) throws NoMovieException, ProjectionException {
		Projection projection = null;
		if ((projection = newProjections.get(projectionId)) == null) {
			throw new ProjectionException("La proiezione " + projectionId + " non esiste tra le nuove proiezioni.");
		}
		projection.setMovie(getMovie(movieId));
	}

	/**
	 * Associa alla proiezione una sala.
	 * 
	 * @param projectionId codice identificativo della proiezione.
	 * @param roomId       codice identificativo della sala.
	 * @throws RoomException       qualora vi siano errori legati alla gestione
	 *                             della sala del cinema.
	 * @throws ProjectionException qualora la proiezione inserita non esista.
	 */
	public void setProjectionRoom(int projectionId, int roomId) throws RoomException, ProjectionException {
		Projection projection = null;
		if ((projection = newProjections.get(projectionId)) == null) {
			throw new ProjectionException("La proiezione " + projectionId + " non esiste tra le nuove proiezioni.");
		}
		projection.setRoom(getRoom(roomId));
	}

	/**
	 * Imposta la data e l'ora di una proiezione.
	 * 
	 * @param projectionId       codice identificativo della proiezione.
	 * @param projectionDateTime data e ora di una proiezione.
	 * @throws ProjectionException qualora la proiezione inserita non esista.
	 */
	public void setProjectionDateTime(int projectionId, LocalDateTime projectionDateTime) throws ProjectionException {
		Projection projection = null;
		if ((projection = newProjections.get(projectionId)) == null) {
			throw new ProjectionException("La proiezione " + projectionId + " non esiste tra le nuove proiezioni.");
		}
		projection.setDateTime(projectionDateTime);
	}

	/**
	 * Associa alla proiezione un prezzo.
	 * 
	 * @param projectionId codice identificativo della proiezione.
	 * @param price        prezzo della proiezione.
	 * @throws ProjectionException qualora la proiezione inserita non esista.
	 */
	public void setProjectionPrice(int projectionId, double price) throws ProjectionException {
		Projection projection = null;
		if ((projection = newProjections.get(projectionId)) == null) {
			throw new ProjectionException("La proiezione " + projectionId + " non esiste tra le nuove proiezioni.");
		}
		projection.setPrice(price);
	}

	/**
	 * Inserisce la nuova proiezione creata dall'admin all'interno del database.
	 * 
	 * @param projectionId codice identificativo della proiezione.
	 * @throws ProjectionException  qualora la proiezione inserita non esista.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public void saveNewProjection(int projectionId) throws ProjectionException, PersistenceException {
		Projection projection = null;
		if ((projection = newProjections.get(projectionId)) == null) {
			throw new ProjectionException("La proiezione " + projectionId + " non esiste tra le nuove proiezioni.");
		}
		persistenceFacade.putProjection(projection);
	}

	/**
	 * Restituisce le proiezioni di un cinema, inerenti uno specifico film.
	 * 
	 * @param movieId codice identificativo del film.
	 * @return la lista di proiezioni inerenti al film inserito.
	 * @throws NoMovieException     qualora non si trovi nessun film, dato l'id.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public List<Projection> getProjections(int movieId) throws NoMovieException, PersistenceException {
		List<Projection> movieProjections = new ArrayList<Projection>();
		Movie m = getMovie(movieId);
		if (m != null) {
			for (Projection p : getProjections()) {
				if (p.getMovie().getId() == movieId) {
					movieProjections.add(p);
				}
			}
		}
		return movieProjections;
	}

	/**
	 * Restituisce gli id delle proiezioni, inerenti ad uno specifico film.
	 * 
	 * @param movieId codice identificativo del film.
	 * @return la lista degli id delle proiezioni inerenti al film inserito.
	 * @throws NoMovieException     qualora non si trovi nessun film, dato l'id.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public List<Integer> getMovieProjections(int movieId) throws NoMovieException, PersistenceException {
		List<Integer> movieProjections = new ArrayList<>();
		Movie m = getMovie(movieId);
		if (m != null) {
			for (Projection p : getProjections()) {
				if (p.getMovie().getId() == movieId) {
					movieProjections.add(p.getId());
				}
			}
		}
		return movieProjections;
	}

	/**
	 * Restituisce le proiezioni attualmente proiettate da un cinema, inerenti uno
	 * specifico film.
	 * 
	 * @param movieId codice identificativo del film.
	 * @return la lista di proiezioni inerenti al film inserito.
	 * @throws NoMovieException     qualora non si trovi nessun film, dato l'id.
	 * @throws ProjectionException  qualora il film inserito non sia più in
	 *                              programmazione.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public List<Projection> getCurrentlyAvailableProjections(int movieId)
			throws NoMovieException, ProjectionException, PersistenceException {
		List<Projection> movieProjections = new ArrayList<Projection>();
		Movie m = getMovie(movieId);
		if (m != null) {
			for (Projection p : getProjections()) {
				if ((p.getMovie().getId() == movieId) && (p.getDateTime().isAfter(LocalDateTime.now()))) {
					movieProjections.add(p);
				}
			}
		}
		if (movieProjections.size() != 0)
			return movieProjections;
		else
			throw new ProjectionException(
					"Il film \"" + this.getMovie(movieId).getTitle() + "\" non è attualmente in programmazione.");
	}

	/**
	 * Restituisce tutti i film presenti nel database.
	 * 
	 * @return la lista dei film presenti nel database.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public ArrayList<Movie> getAllMovies() throws PersistenceException {
		return persistenceFacade.getAllMovies();
	}

	/**
	 * Restituisce tutti i film attualmente proiettati.
	 * 
	 * @return la lista dei film attualmente proiettati.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public List<Movie> getCurrentlyAvailableMovies() throws PersistenceException {
		List<Movie> movies = new ArrayList<Movie>();
		for (Projection p : getProjections()) {
			if ((p.getDateTime() != null) && (p.getDateTime().isAfter(LocalDateTime.now()))) {
				boolean alreadyExists = false;
				for (Movie m : movies) {
					if (p.getMovie().getId() == m.getId()) {
						alreadyExists = true;
						break;
					}
				}
				if (!alreadyExists) {
					movies.add(p.getMovie());
				}
			}
		}
		return movies;
	}

	/**
	 * Restituire la lista di film attualmente proiettati, dato il titolo di un film
	 * (o parte di esso).
	 * 
	 * @param title titolo del film.
	 * @return la lista di film attualmente proiettati.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public List<Movie> getCurrentlyAvailableMovies(String title) throws PersistenceException {
		List<Movie> movies = new ArrayList<Movie>();
		for (Movie m : getCurrentlyAvailableMovies()) {
			if (m.getTitle().toLowerCase().contains(title.toLowerCase())) {
				movies.add(m);
			}
		}
		return movies;
	}

	/**
	 * Restituisce un film, dato il suo Id.
	 * 
	 * @param movieId codice identificativo del film.
	 * @return il film cercato.
	 * @throws NoMovieException qualora non si trovi nessun film, dato l'id.
	 */
	public Movie getMovie(int movieId) throws NoMovieException {
		try {
			return persistenceFacade.getMovie(movieId);
		} catch (PersistenceException e) {
			throw new NoMovieException("Il film con ID " + movieId + " non esiste.");
		}
	}

	/**
	 * Resituisce una proiezione, dato il suo Id.
	 * 
	 * @param projectionId codice identificativo della proiezione.
	 * @return la proiezione cercata.
	 * @throws ProjectionException  qualora il film inserito non sia più in
	 *                              programmazione.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public Projection getProjection(int projectionId) throws ProjectionException, PersistenceException {
		for (Projection p : getProjections()) {
			if (p.getId() == projectionId) {
				return p;
			}
		}
		throw new ProjectionException("La proiezione con id " + projectionId + " non esiste.");
	}

	/**
	 * Resituisce una proiezione, se attualmente proiettata, dato il suo id.
	 * 
	 * @param projectionId codice identificativo della proiezione.
	 * @return la proiezione cercata.
	 * @throws ProjectionException  qualora il film inserito non sia più
	 *                              disponiobile, o non esista.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public Projection getCurrentlyAvailableProjection(int projectionId)
			throws ProjectionException, PersistenceException {
		for (Projection p : getProjections()) {
			if ((p.getId() == projectionId) && (p.getDateTime().isAfter(LocalDateTime.now()))) {
				return p;
			}
			if ((p.getId() == projectionId) && (p.getDateTime().isBefore(LocalDateTime.now()))) {
				throw new ProjectionException(
						"La proiezione selezionata con ID " + projectionId + " non è più disponibile.");
			}
		}
		throw new ProjectionException("La proiezione con id " + projectionId + " non esiste.");
	}

	/**
	 * Resituisce un coupon, dato il suo id.
	 * 
	 * @param code codice coupon.
	 * @return il coupon cercato.
	 * @throws CouponException qualora non esista il coupon.
	 */
	public Coupon getCoupon(String code) throws CouponException {
		try {
			return persistenceFacade.getCoupon(code);
		} catch (PersistenceException e) {
			throw new CouponException("Il coupon " + code + " non esiste.");
		}
	}

	/**
	 * Restituisce l'età massima sopra la quale il cinema inizia ad effettuare uno
	 * sconto.
	 * 
	 * @return l'età massima.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public int getMaxDiscountAge() throws PersistenceException {
		return this.persistenceFacade.getAgeDiscounts().getMaxAge();
	}

	/**
	 * Restituisce l'età minima sotto la quale il cinema inizia ad effettuare uno
	 * sconto.
	 * 
	 * @return l'età minima.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public int getMinDiscountAge() throws PersistenceException {
		return this.persistenceFacade.getAgeDiscounts().getMinAge();
	}

	/**
	 * Restituisce tutte le sale del cinema.
	 * 
	 * @return la lista delle sale del cinema.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public List<Room> getAllRooms() throws PersistenceException {
		return persistenceFacade.getAllRooms();
	}

	/**
	 * Restituisce la sala del cinema, dato l'id.
	 * 
	 * @param roomId codice identifictivo della sala.
	 * @return la sala cercata.
	 * @throws RoomException qualora la sala cercata non sia presente all'interno
	 *                       del cinema.
	 */
	public Room getRoom(int roomId) throws RoomException {
		try {
			return persistenceFacade.getRoom(roomId);
		} catch (PersistenceException e) {
			throw new RoomException("La sala con id " + roomId + " non è presente all'interno del cinema.");
		}
	}

	/**
	 * Restituisce l'ubicazione del cinema.
	 * 
	 * @return l'ubicazione del cinema (indirizzo, città, cap, stato).
	 */
	public String getLocation() {
		return cinemaInfo.get("address") + ", " + cinemaInfo.get("city") + " - " + cinemaInfo.get("zipCode") + " "
				+ cinemaInfo.get("country");
	}

	/**
	 * Resituisce il persistenceFacade creato nel costruttore del cinema.
	 * @return PersistenceFacade istanza del PersistenceFacade
	 * @throws SQLException	qualora vi siano problemi nel collegarsi al
	 * 						database.
	 */
	public PersistenceFacade getPersistenceFacade() throws SQLException {
		return persistenceFacade;
	}
	
	/**
	 * Imposta la proiezione di una prenotazione.
	 * 
	 * @param reservationId codice identificativo della prenotazione.
	 * @param projectionId  codice identificativo della proiezione..
	 * @throws ProjectionException  qualora vi siano errori riscontrati nelle
	 *                              procedure di interazione con gli oggetti che
	 *                              rappresentano le proiezioni..
	 * @throws ReservationException qualora l'id della prenotazione inserita non
	 *                              esista.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public void setReservationProjection(long reservationId, int projectionId)
			throws ProjectionException, ReservationException, PersistenceException {
		getReservation(reservationId).setProjection(getProjection(projectionId));
	}

	/**
	 * Restituisce il numero di colonne della sala in cui è proiettato il film della
	 * prenotazione.
	 * 
	 * @param reservationId codice identificativo della prenotazione.
	 * @return il numero di colonne della sala.
	 * @throws ReservationException qualora l'id della prenotazione inserita non
	 *                              esista.
	 */
	public int getNumberColsReservationProjection(long reservationId) throws ReservationException {
		return getReservation(reservationId).getProjection().getRoom().getNumberOfCols();
	}

	/**
	 * Restituisce il numero di righe della sala in cui è proiettato il film della
	 * prenotazione.
	 * 
	 * @param reservationId codice identificativo della prenotazione.
	 * @return il numero di righe della sala.
	 * @throws ReservationException qualora l'id della prenotazione inserita non
	 *                              esista.
	 */
	public int getNumberRowsReservationProjection(long reservationId) throws ReservationException {
		return getReservation(reservationId).getProjection().getRoom().getNumberOfRows();
	}

	/**
	 * Controlla se il posto della sala selezionata dalla prenotazione è libero o
	 * meno.
	 * 
	 * @param projectionId codice identificativo della proiezione.
	 * @param row          coordinata riga posto.
	 * @param col          coordinata colonna posto.
	 * @return True: libero, False: occupato.
	 * @throws RoomException        qualora la sala cercata non sia presente
	 *                              all'interno del cinema.
	 * @throws ReservationException qualora l'id della prenotazione inserita non
	 *                              esista.
	 * @throws ProjectionException  qualora vi siano errori riscontrati nelle
	 *                              procedure di interazione con gli oggetti che
	 *                              rappresentano le proiezioni.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public boolean checkIfProjectionSeatIsAvailable(int projectionId, int row, int col)
			throws RoomException, ReservationException, ProjectionException, PersistenceException {
		return persistenceFacade.getOccupiedSeat(projectionId, row, col);
	}

	/**
	 * Restituisce la proiezione associata alla prenotazione.
	 * 
	 * @param reservationId codice identificativo della prenotazione.
	 * @return l'id della proiezione associata alla prenotazione inderita.
	 * @throws ReservationException qualora la prenotazine inserita non esista.
	 */
	public int getReservationProjection(long reservationId) throws ReservationException {
		for (Reservation reservation : cinemaReservations) {
			if (reservation.getProgressive() == reservationId)
				return reservation.getProjection().getId();
		}
		throw new ReservationException("La prenotazione " + reservationId + " non esiste.");
	}

	/**
	 * Aggiunge un posto alla reservation.
	 * 
	 * @param reservationId codice identificativo della prenotazione.
	 * @param row           coordinata riga posto.
	 * @param col           coordinata colonna posto.
	 * @throws RoomException             qualora la sala cercata non sia presente
	 *                                   all'interno del cinema.
	 * @throws SeatAvailabilityException qualora il posto richiesto non sia
	 *                                   disponibile.
	 * @throws ReservationException      qualora l'id della prenotazione inserita
	 *                                   non esista.
	 */
	public void addSeatToReservation(long reservationId, int row, int col)
			throws RoomException, SeatAvailabilityException, ReservationException {
		getReservation(reservationId).addSeat(row, col);
	}

	/**
	 * Rimuove un posto dalla reservation.
	 * 
	 * @param reservationId codice identificativo della prenotazione.
	 * @param row           coordinata riga posto.
	 * @param col           coordinata colonna posto.
	 * @throws RoomException        qualora la sala cercata non sia presente
	 *                              all'interno del cinema.
	 * @throws ReservationException qualora l'id della prenotazione inserita non
	 *                              esista.
	 */
	public void removeSeatFromReservation(long reservationId, int row, int col)
			throws RoomException, ReservationException {
		getReservation(reservationId).removeSeat(row, col);
	}

	/**
	 * Aggiunge le informazioni dello spettatore alla prenotazione.
	 * 
	 * @param reservationId codice identificativo della prenotazione.
	 * @param name          nome dello spettatore.
	 * @param surname       cognome dello spettatore.
	 * @param email         email dello spettatore.
	 * @throws InvalidSpectatorInfoException qualora le informazioni relative ai
	 *                                       dati di uno spettatore vengano
	 *                                       interpretate come non valide al momento
	 *                                       dell'inserimento.
	 * @throws ReservationException          qualora l'id della prenotazione
	 *                                       inserita non esista.
	 */
	public void setReservationPurchaser(long reservationId, String name, String surname, String email)
			throws InvalidSpectatorInfoException, ReservationException {
		getReservation(reservationId).setPurchaser(new Spectator(name, surname, email));
	}

	/**
	 * Imposta i dati della carta di credito dello spettatore.
	 * 
	 * @param reservationId  codice identificativo della prenotazione.
	 * @param number         numero della carta di credito
	 * @param owner          titolare della carta di credito.
	 * @param cvv            codice di sicurezza della carta di credito.
	 * @param expirationDate data di scadenza della carta di cradito.
	 * @throws ReservationException qualora l'id della prenotazione inserita non
	 *                              esista.
	 */
	public void setReservationPaymentCard(long reservationId, String number, String owner, String cvv,
			YearMonth expirationDate) throws ReservationException {
		getReservation(reservationId).setPaymentCard(number, owner, cvv, expirationDate);
	}

	/**
	 * Imposta il numero di persone che hanno un'età inferiore all'età minima per la
	 * quale viene applicato lo sconto.
	 * 
	 * @param reservationId codice identificativo della prenotazione.
	 * @param number        numero di persone con l'età inferiore all'età minima.
	 * @throws DiscountException    qualora vi siano errori riscontrati nelle
	 *                              procedure di interazione con gli oggetti che
	 *                              rappresentano gli sconti.
	 * @throws ReservationException qualora l'id della prenotazione inserita non
	 *                              esista.
	 */
	public void setReservationNumberPeopleUntilMinAge(long reservationId, int number)
			throws DiscountException, ReservationException {
		getReservation(reservationId).setNumberPeopleUnderMinAge(number);
	}

	/**
	 * Imposta il numero di persone che hanno un'età superiore all'età massima per
	 * la quale viene applicato lo sconto.
	 * 
	 * @param reservationId codice identificativo della prenotazione.
	 * @param number        numero di persone con l'età superiore all'età massima.
	 * @throws DiscountException    qualora vi siano errori riscontrati nelle
	 *                              procedure di interazione con gli oggetti che
	 *                              rappresentano gli sconti.
	 * @throws ReservationException qualora l'id della prenotazione inserita non
	 *                              esista.
	 */
	public void setReservationNumberPeopleOverMaxAge(long reservationId, int number)
			throws DiscountException, ReservationException {
		getReservation(reservationId).setNumberPeopleOverMaxAge(number);
	}

	/**
	 * Aggiunge alla prenotazione un eventuale coupon per un ulteriore sconto sul
	 * totale.
	 * 
	 * @param reservationId codice identificativo della prenotazione.
	 * @param code          codice del coupon.
	 * @throws CouponException      qualora il coupon sia già stato usato.
	 * @throws ReservationException qualora l'id della prenotazione inserita non
	 *                              esista.
	 */
	public void setReservationCoupon(long reservationId, String code) throws CouponException, ReservationException {
		Coupon coupon = getCoupon(code);
		if (coupon.isUsed() == true) {
			throw new CouponException("Il coupon " + code + " è già stato usato.");
		} else
			getReservation(reservationId).setCoupon(coupon);
	}

	/**
	 * Avvia il processo di pagamento per una prenotazione, una volta inseriti tutti
	 * i dati.
	 * 
	 * @param reservationId codice identificativo della prenotazione.
	 * @throws NumberFormatException     qualora si tenti di convertire una stringa
	 *                                   con formato non corretto in un valore
	 *                                   numerico.
	 * @throws SeatAvailabilityException qualora il posto richiesto non sia
	 *                                   disponibile.
	 * @throws RoomException             qualora la sala cercata non sia presente
	 *                                   all'interno del cinema.
	 * @throws ReservationException      qualora l'id della prenotazione inserita
	 *                                   non esista.
	 * @throws PaymentErrorException     qualora vi siano errori riscontrati nelle
	 *                                   procedure di pagamento.
	 * @throws ReservationException      qualora l'id della prenotazione inserita
	 *                                   non esista.
	 * @throws PersistenceException      qualora vi siano errori riscontrati durante
	 *                                   l'uso di meccanismi di persistenza.
	 */
	public void buyReservation(long reservationId) throws NumberFormatException, SeatAvailabilityException,
			RoomException, ReservationException, PaymentErrorException, ReservationException, PersistenceException {
		try {
			getReservation(reservationId).buy();
		} catch (PaymentErrorException e) {
			persistenceFacade.deleteReservation(reservationId);
			throw new PaymentErrorException(e.getMessage());
		}
		/*
		 * Se la reservation è associata ad un coupon, una volta terminato il metodo
		 * buy, il coupon viene segnato come già utilizzato in modo da impedirne il
		 * riutilizzo.
		 */
		Coupon coupon = getReservation(reservationId).getCoupon();
		if (coupon != null) {
			persistenceFacade.setCouponUsed(coupon.getCode());
		}
		persistenceFacade.setReservationFields(getReservation(reservationId));
	}

	/**
	 * Restituisce il totale di una prenotazione.
	 * 
	 * @param reservationId codice identificativo della prenotazione.
	 * @return il totale della prenotazione inserita.
	 * @throws ReservationException qualora l'id della prenotazione inserita non
	 *                              esista.
	 */
	public double getReservationTotalAmount(long reservationId) throws ReservationException {
		return getReservation(reservationId).getTotal();
	}

	/**
	 * Invia un e-mail allo spettatore che ha completato la compilazione della
	 * prenotazione (comprende il report).
	 * 
	 * @param reservationId codice identificativo della prenotazione.
	 * @return il thread che gestisce l'invio dell'e-mail.
	 * @throws ReservationException qualora l'id della prenotazione inserita non
	 *                              esista.
	 * @throws HandlerException     qualora ci siano errori riscontrati durante le
	 *                              procedure di generazione dei report delle
	 *                              prenotazioni e conseguente invio di e-mail allo
	 *                              spettatore.
	 */
	public Thread sendReservationEmail(long reservationId) throws ReservationException, HandlerException {
		return emailHandler.sendEmail(getReservation(reservationId));
	}

	public String getName() {
		return cinemaInfo.get("name");
	}

	public String getEmail() {
		return cinemaInfo.get("email");
	}
	
	public String getLogoURL() {
		return cinemaInfo.get("logoURL");
	}

	/**
	 * Imposta la strategia di sconto applicata dal cinema.
	 * 
	 * @param typeOfDiscount tipo di sconto.
	 * @throws DiscountNotFoundException qualora la strategia di sconto richiesta
	 *                                   non sia valida.
	 * @throws PersistenceException      qualora vi siano errori riscontrati durante
	 *                                   l'uso di meccanismi di persistenza.
	 */
	public void setCinemaDiscountStrategy(TypeOfDiscount typeOfDiscount)
			throws DiscountNotFoundException, PersistenceException {
		cinemaDiscount = this.getDiscountByStrategy(typeOfDiscount);
		persistenceFacade.setDiscountStrategy(1, typeOfDiscount.name().toString().toUpperCase());
	}

	/**
	 * Restituisce tutte le strategie del cinema presenti nel database.
	 * 
	 * @return lista di tutte le strategie di sconto possibili.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public ArrayList<Discount> getAllCinemaDiscounts() throws PersistenceException {
		ArrayList<Discount> allDiscounts = new ArrayList<Discount>();
		allDiscounts.add(persistenceFacade.getAgeDiscounts());
		allDiscounts.add(persistenceFacade.getAllDayDiscounts());
		allDiscounts.add(persistenceFacade.getGroupDiscounts());
		return allDiscounts;
	}

	/**
	 * Restituisce tutte le tipologie di sconto applicabili.
	 * 
	 * @return la lista di tutte le tipologie di sconto a disposizione del cinema.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public ArrayList<TypeOfDiscount> getAllDiscountStrategies() throws PersistenceException {
		ArrayList<TypeOfDiscount> allTypesOfDiscounts = new ArrayList<>();
		for (Discount discount : getAllCinemaDiscounts()) {
			allTypesOfDiscounts.add(discount.getTypeOfDiscount());
		}
		return allTypesOfDiscounts;
	}

	/**
	 * Restituisce la descrizione di tutte le strategia di sconto.
	 * 
	 * @return la lista delle descrizioni di tutte le strategie.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public ArrayList<String> getAllDiscountStrategiesDescription() throws PersistenceException {
		ArrayList<String> allTypesOfDiscountsDescription = new ArrayList<>();
		for (Discount discount : getAllCinemaDiscounts()) {
			allTypesOfDiscountsDescription.add(discount.toString());
		}
		return allTypesOfDiscountsDescription;
	}

	/**
	 * Restituisce uno sconto dato il tipo di sconto inserito.
	 * 
	 * @param typeOfDiscount tipo di sconto.
	 * @return lo sconto dato il tipo di sconto inserito.
	 * @throws DiscountNotFoundException qualoro non ci sia nessuno sconto associato
	 *                                   al tipo di sconto inserito.
	 * @throws PersistenceException      qualora vi siano errori riscontrati durante
	 *                                   l'uso di meccanismi di persistenza.
	 */
	public Discount getDiscountByStrategy(TypeOfDiscount typeOfDiscount)
			throws DiscountNotFoundException, PersistenceException {
		Discount discount = null;
		for (Discount d : getAllCinemaDiscounts()) {
			if (d.getTypeOfDiscount() == typeOfDiscount) {
				discount = d;
			}
		}
		if (discount == null)
			throw new DiscountNotFoundException(
					"Non è stato trovato nessuno sconto che applica la strategia " + typeOfDiscount);
		else
			return discount;
	}

	/**
	 * 0 References da eliminare.
	 */
	public String getDiscountStrategyDescription(TypeOfDiscount t)
			throws DiscountNotFoundException, PersistenceException {
		return getDiscountByStrategy(t).toString();
	}

	public String getAdminPassword() {
		return cinemaInfo.get("adminPassword");
	}

	/**
	 * Imposta la password dell'amministratore.
	 * 
	 * @param newAdminPassword nuova password dell'amministratore.
	 * @throws PasswordException    qualorala password inserita non rispetti il
	 *                              requisito di validità.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public void setPassword(String newAdminPassword) throws PasswordException, PersistenceException {
		if (newAdminPassword.length() < 5) {
			throw new PasswordException("La password inserita è troppo corta.");
		}
		persistenceFacade.setPassword(1, newAdminPassword);
	}

	/**
	 * Effettua il login per l'amministratore.
	 * 
	 * @param password password dell'amministratore.
	 * @throws PasswordException Eccezione lanciata qualora si inserisca una
	 *                           password errata nella fase di login.
	 */
	public void login(String password) throws PasswordException {
		if (!getAdminPassword().equals(password))
			throw new PasswordException("La password inserita è errata.");
	}

	/**
	 * Restistuisce il numero di posti della prenotazione.
	 * 
	 * @param reservationId codice identificativo della prenotazione.
	 * @return il numero di posti della prenotazione.
	 * @throws ReservationException qualora l'id della prenotazione inserita non
	 *                              esista.
	 */
	public int getReservationNSeats(long reservationId) throws ReservationException {
		return getReservation(reservationId).getNSeats();
	}

	/**
	 * Restistuisce il tipo di sconto applicato dalla prenotazione stessa.
	 * 
	 * @param reservationId codice identificativo della prenotazione.
	 * @return il tipo di sconto applicato dalla prenotazione stessa.
	 * @throws ReservationException qualora l'id della prenotazione inserita non
	 *                              esista.
	 */
	public String getReservationTypeOfDiscount(long reservationId) throws ReservationException {
		return getReservation(reservationId).getTypeOfDiscount().name();
	}

	/**
	 * Restistuisce il totale della prenotazione, a meno degli sconti.
	 * 
	 * @param reservationId codice identificativo della prenotazione.
	 * @return il totale della prenotazione, a meno degli sconti.
	 * @throws ReservationException qualora l'id della prenotazione inserita non
	 *                              esista.
	 */
	public double getReservationFullPrice(long reservationId) throws ReservationException {
		return getReservation(reservationId).getFullPrice();
	}

	/**
	 * Restistuisce lo sconto del coupon, se esistente, associato alla prenotazione.
	 * 
	 * @param reservationId codice identificativo della prenotazione.
	 * @return lo sconto del coupon associato alla prenotazione.
	 * @throws ReservationException qualora l'id della prenotazione inserita non
	 *                              esista.
	 */
	public double getReservationCouponDiscount(long reservationId) throws ReservationException {
		try {
			return getReservation(reservationId).getCoupon().getDiscount();
		} catch (NullPointerException exception) {
			return 0.0;
		}
	}

	/**
	 * Restistuisce il codice del coupon, se esistente, associato alla prenotazione.
	 * 
	 * @param reservationId codice identificativo della prenotazione.
	 * @return il codice del coupon associato alla prenotazione.
	 * @throws ReservationException qualora l'id della prenotazione inserita non
	 *                              esista.
	 */
	public String getReservationCouponCode(long reservationId) throws ReservationException {
		return getReservation(reservationId).getCoupon().getCode();
	}

	/**
	 * Restituisce il film associato all'id della proiezione inserito.
	 * 
	 * @param projectionId codice identificativo della proiezione.
	 * @return il film associato all'id della proiezione cercato.
	 * @throws ProjectionException  qualora la proiezione con l'id inserito non
	 *                              esista.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public Movie getProjectionMovie(int projectionId) throws ProjectionException, PersistenceException {
		for (Projection projection : getProjections()) {
			if (projection.getId() == projectionId) {
				return projection.getMovie();
			}
		}
		throw new ProjectionException("La proiezione con id " + projectionId + " non esiste.");
	}

	/**
	 * Restituisce data e ora della proiezione.
	 * 
	 * @param projectionId codice identificativo della proiezione.
	 * @return la data e ora della proiezione.
	 * @throws ProjectionException  qualora la proiezione con l'id inserito non
	 *                              esista.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public LocalDateTime getProjectionDateTime(int projectionId) throws ProjectionException, PersistenceException {
		for (Projection projection : getProjections()) {
			if (projection.getId() == projectionId) {
				return projection.getDateTime();
			}
		}
		throw new ProjectionException("La proiezione con id " + projectionId + " non esiste.");
	}

	/**
	 * Restituisce la sala della proiezione.
	 * 
	 * @param projectionId codice identificativo della proiezione.
	 * @return la sala della proiezione.
	 * @throws ProjectionException  qualora la proiezione con l'id inserito non
	 *                              esista.
	 * @throws PersistenceException qualora vi siano errori riscontrati durante
	 *                              l'uso di meccanismi di persistenza.
	 */
	public Room getProjectionRoom(int projectionId) throws ProjectionException, PersistenceException {
		for (Projection projection : getProjections()) {
			if (projection.getId() == projectionId) {
				return projection.getRoom();
			}
		}
		throw new ProjectionException("La proiezione con id " + projectionId + " non esiste.");
	}

}
