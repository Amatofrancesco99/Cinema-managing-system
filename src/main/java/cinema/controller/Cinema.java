package cinema.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
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
import cinema.model.reservation.discount.coupon.Coupon;
import cinema.model.reservation.discount.coupon.util.CouponException;
import cinema.model.reservation.discount.types.Discount;
import cinema.model.reservation.discount.types.DiscountAge;
import cinema.model.reservation.discount.types.DiscountDay;
import cinema.model.reservation.discount.types.DiscountNumberSpectators;
import cinema.model.reservation.discount.types.util.DiscountException;
import cinema.model.reservation.discount.types.util.TypeOfDiscounts;
import cinema.model.reservation.Reservation;
import cinema.model.reservation.util.ReservationException;
import cinema.model.reservation.util.SeatAvailabilityException;
import cinema.model.spectator.Spectator;
import cinema.model.spectator.util.InvalidSpectatorInfoException;

/**
 * BREVE SPIEGAZIONE CLASSE CINEMA ( Pattern Controller)
 * 
 * @author Screaming Hairy Armadillo Team
 *
 *         Questa classe rappresenta il controller del nostro progetto, ossia la
 *         classe he consente alla vista di poter eseguire operazioni utili,
 *         senza dover conoscere la logica di dominio. Essa è molto utile poiché
 *         qualora si usino interfacce diverse, come nel nostro caso (CLI e GUI)
 *         gli stessi metodi della CLI saranno presenti nella GUI, chiaramente
 *         cambierà il modo di gestire situazioni particolari, ma comunque
 *         permette di rendere migliore, tra le altre cose, la modificabilità
 *         del codice, l'high coesion ed il low coupling.
 */
public class Cinema {

	/**
	 * ATTRIBUTI
	 * 
	 * @param name               	Nome
	 * @param city               	Città
	 * @param country            	Paese
	 * @param zipCode            	Codice comunale
	 * @param address            	Indirizzo (Via, numero civico)
	 * @param logoURL            	Logo del cinema
	 * @param email              	E-mail, utile per inviare report al cliente con i
	 *                           	diversi dati riferiti alla specifica prenotazione,
	 *                           	effettuata da quest ultimo
	 * @param password           	Password associata all'indirizzo email
	 * @param adminPassword      	Password dell'amministratore del cinema
	 * @param rooms              	List: comprende tutte le sale del cinema
	 * @param cinemaProjections  	List: comprende tutte le proiezioni fatte dal
	 *                           	cinema
	 * @param cinemaReservations 	List: comprende tutte le prenotazioni del cinema
	 * @param cinemaDiscount     	Sconto attivo
	 * @param allDiscounts       	Tutti gli sconti applicabili
	 * @param persistenceFacade
	 */
	private String name;
	private String city;
	private String country;
	private String zipCode;
	private String address;
	private String logoURL;
	private String email;
	private String password;
	private String adminPassword;
	private EmailHandler emailHandler;
	private List<Room> rooms;
	private List<Projection> cinemaProjections;
	private List<Reservation> cinemaReservations;
	private Discount cinemaDiscount;
	private ArrayList<Discount> allDiscounts;
	private PersistenceFacade persistenceFacade;

	/**
	 * COSTRUTTORE di default, contenente le informazioni specifiche del nostro
	 * cinema
	 */
	public Cinema() {
		name = "Cinema Armadillo";
		city = "Pavia (PV)";
		country = "Italia";
		zipCode = "27100";
		address = "Via A. Ferrata, 5";
		email = "cinemaarmadillo@gmail.com";
		password = "CinemaArmadillo@1999";
		adminPassword = "admin";
		logoURL = "https://cdn1.iconfinder.com/data/icons/luchesa-2/128/Movie-512.png";
		emailHandler = new EmailHandler(name,email,password,getLocation(),logoURL);
		try {
			persistenceFacade = PersistenceFacade.getInstance();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		rooms = new ArrayList<Room>();
		cinemaProjections = new ArrayList<Projection>();
		cinemaReservations = new ArrayList<Reservation>();
		cinemaDiscount = new DiscountAge();
		allDiscounts = new ArrayList<Discount>();
		addDiscount(new DiscountAge());
		addDiscount(new DiscountDay());
		addDiscount(new DiscountNumberSpectators());

		// ********* TEMPORARY DATA USED FOR TESTING *********
		// Test projections
		try {
			rooms = getAllRooms();
			cinemaProjections = persistenceFacade.getAllProjections();
			/*
			// occupare il primo posto della seconda proiezione
			try {
				p2.takeSeat(0, 0);
			} catch (RoomException e) {
			}
			*/
		} catch (PersistenceException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * METODO per creare una nuova prenotazione (vuota), a partire dalla classe
	 * cinema
	 * 
	 * @return reservation Nuova prenotazione creata
	 */
	public long createReservation() {
		Reservation r = new Reservation(cinemaDiscount);
		cinemaReservations.add(r);
		return r.getProgressive();
	}

	/**
	 * METODO per farsi dare una prenotazione, dato il suo id
	 * 
	 * @param progressive
	 * @return
	 * @throws ReservationNotExistsException
	 */
	public Reservation getReservation(long progressive) throws ReservationException {
		for (Reservation r : cinemaReservations) {
			if (r.getProgressive() == progressive)
				return r;
		}
		throw new ReservationException("La prenotazione " + progressive + " non esiste.");
	}

	/**
	 * METODO per rimuovere una proiezione al cinema
	 * 
	 * @param p Proiezione da rimuovere alla lista di proiezioni di cui il cinema
	 *          dispone
	 * @throws NoProjectionException
	 */
	public void removeProjection(int p) throws ProjectionException {
		cinemaProjections.remove(getProjection(p));
	}

	/**
	 * METODO per farsi restituire tutte le proiezioni di cui il cinema dispone
	 * 
	 * @return ArrayList<Projection> Insieme di tutte le proiezioni del cinema
	 */
	public List<Projection> getProjections() {
		return cinemaProjections;
	}

	/**
	 * METODO per impostare l'id di una proiezione
	 * 
	 * @param newProjection
	 * @param id
	 * @throws ProjectionIDAlreadyUsedException
	 * @throws InvalidProjectionIdException
	 */
	public void createProjectionWithID(int id) throws ProjectionException {
		for (Projection p : cinemaProjections) {
			if (p.getId() == id)
				throw new ProjectionException("La proiezione con id " + id + " non esiste.");
		}
		Projection p = new Projection();
		p.setId(id);
		cinemaProjections.add(p);
	}

	/**
	 * METODO per associare ad una proiezione un film
	 * 
	 * @param p
	 * @param movie
	 * @throws NoMovieException
	 * @throws NoProjectionException
	 */
	public void setProjectionMovie(int p, int movieId) throws NoMovieException, ProjectionException {
		getProjection(p).setMovie(getMovie(movieId));
	}

	/**
	 * METODO per associare alla proiezione una sala
	 * 
	 * @param p
	 * @param roomId
	 * @throws RoomNotExistsException
	 * @throws NoProjectionException
	 */
	public void setProjectionRoom(int p, int roomId) throws RoomException, ProjectionException {
		getProjection(p).setRoom(getRoom(roomId));
	}

	/**
	 * METODO per impostare la data e l'ora di una proiezione
	 * 
	 * @param p
	 * @param projectionDateTime
	 * @throws InvalidProjectionDateTimeException
	 * @throws NoProjectionException
	 */
	public void setProjectionDateTime(int p, LocalDateTime projectionDateTime)
			throws ProjectionException, ProjectionException {
		getProjection(p).setDateTime(projectionDateTime);
	}

	/**
	 * METODO per associare alla proiezione un prezzo
	 * 
	 * @param p
	 * @param price
	 * @throws InvalidPriceException
	 * @throws NoProjectionException
	 */
	public void setProjectionPrice(int p, double price) throws ProjectionException, ProjectionException {
		getProjection(p).setPrice(price);
	}

	/**
	 * 
	 * METODO per restituire le proiezioni di un cinema, inerenti uno specifico film
	 * tramite l'id
	 * 
	 * @param movieId Id del film di cui si vogliono cercare le proiezioni
	 * @return ArrayList<Projection> Insieme delle proiezioni dello specifico film
	 * @throws NoMovieException
	 * @throws NoMovieProjectionsException Eccezione lanciata, qualora il cinema non
	 *                                     abbia quel film, tra i film proiettati
	 * 
	 *                                     DA ELIMINARE (RIDURRE L'ACCOPPIAMENTO CON
	 *                                     LE INTERFACCE)
	 */
	public List<Projection> getProjections(int movieId) throws NoMovieException {
		List<Projection> movieProjections = new ArrayList<Projection>();
		Movie m = getMovie(movieId);
		if (m != null) {
			for (Projection p : cinemaProjections) {
				if (p.getMovie().getId() == movieId) {
					movieProjections.add(p);
				}
			}
		}
		return movieProjections;
	}

	/**
	 * 
	 * METODO per restituire gli id delle pproiezioni di un cinema, inerenti uno
	 * specifico film tramite l'id
	 * 
	 * @param movieId Id del film di cui si vogliono cercare le proiezioni
	 * @return ArrayList<Integer> Id delle proiezioni dello specifico film
	 * @throws NoMovieException
	 * @throws NoMovieProjectionsException Eccezione lanciata, qualora il cinema non
	 *                                     abbia quel film, tra i film proiettati
	 * 
	 *                                     DA ELIMINARE (RIDURRE L'ACCOPPIAMENTO CON
	 *                                     LE INTERFACCE)
	 */
	public List<Integer> getMovieProjections(int movieId) throws NoMovieException {
		List<Integer> movieProjections = new ArrayList<>();
		Movie m = getMovie(movieId);
		if (m != null) {
			for (Projection p : cinemaProjections) {
				if (p.getMovie().getId() == movieId) {
					movieProjections.add(p.getId());
				}
			}
		}
		return movieProjections;
	}

	/**
	 * 
	 * METODO per restituire le proiezioni attualmente realizzate da un cinema,
	 * inerenti uno specifico film tramite l'id
	 * 
	 * @param movieId Id del film di cui si vogliono cercare le proiezioni
	 *                attualmente disponibili
	 * @return ArrayList<Projection> Insieme delle proiezioni attualmente
	 *         disponibili dello specifico film
	 * @throws NoMovieException
	 * @throws MovieNoLongerProjectedException Eccezione lanciata qualora il film
	 *                                         inserito non abbia proiezioni
	 *                                         attualmente disponibili
	 * @throws NoMovieProjectionsException     Eccezione lanciata, qualora il cinema
	 *                                         non abbia quel film, tra i film
	 *                                         proiettati
	 */
	public List<Projection> getCurrentlyAvailableProjections(int movieId) throws NoMovieException, ProjectionException {
		List<Projection> movieProjections = new ArrayList<Projection>();
		Movie m = getMovie(movieId);
		if (m != null) {
			for (Projection p : cinemaProjections) {
				if ((p.getMovie().getId() == movieId) && (p.getDateTime().isAfter(LocalDateTime.now()))) {
					movieProjections.add(p);
				}
			}
		}
		if (movieProjections.size() != 0)
			return movieProjections;
		else
			throw new ProjectionException(
					"Il film \"" + this.getMovie(movieId).getTitle() + "\" non è più in programmazione.");
	}

	/** METODO per farci dare tutte i film inerenti ad un anno specifico */
	public ArrayList<Movie> getAllMovies() throws PersistenceException {
		return persistenceFacade.getAllMovies();
	}

	/**
	 * 
	 * METODO per restituire tutti i film che il cinema sta attualmente proiettando
	 * 
	 * @return List<Movie> Insieme di tutti i film che il cinema sta momentaneamente
	 *         proiettando
	 */
	public List<Movie> getCurrentlyAvailableMovies() {
		List<Movie> movies = new ArrayList<Movie>();
		for (Projection p : cinemaProjections) {
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
	 * METODO per restituire la lista di film che il cinema proietta, dato il titolo
	 * di un film (o una parte di esso)
	 * 
	 * @param query Titolo del film che si vuole cercare tra le proiezioni del
	 *              cinema (o una parte di esso)
	 * @return ArrayList<Movie> Lista dei film
	 */
	public List<Movie> getCurrentlyAvailableMovies(String query) {
		List<Movie> movies = new ArrayList<Movie>();
		for (Movie m : getCurrentlyAvailableMovies()) {
			if (m.getTitle().toLowerCase().contains(query.toLowerCase())) {
				movies.add(m);
			}
		}
		return movies;
	}

	/**
	 * METODO per restituire un film, dato il suo Id
	 * 
	 * @param id Id del film
	 * @return Movie Film trovato
	 * @throws NoMovieException Eccezione lanciata qualora non si trovi nessun film
	 *                          con quell'Id
	 */
	public Movie getMovie(int id) throws NoMovieException {
		try {
			return persistenceFacade.getMovie(id);
		} catch (PersistenceException e) {
			throw new NoMovieException("Il film con ID " + id + " non esiste.");
		}
	}

	/**
	 * METODO per resituire una proiezione, dato il suo Id
	 * 
	 * @param id Id della proiezione
	 * @return Projection Proiezione con quello specifico Id
	 * @throws NoProjectionException Eccezione lanciata qualora non ci sia nessuna
	 *                               proiezione con quell'Id
	 */
	public Projection getProjection(int id) throws ProjectionException {
		for (Projection p : cinemaProjections) {
			if (p.getId() == id) {
				return p;
			}
		}
		throw new ProjectionException("La proiezione con id " + id + " non esiste.");
	}

	/**
	 * METODO per resituire una proiezione, se attualmente proiettata, dato il suo
	 * Id
	 * 
	 * @param id Id della proiezione che si vuole verificare se sia disponibile
	 * @return Projection Proiezione con quello specifico Id, attualmente proiettata
	 * @throws NoProjectionException                  Eccezione lanciata qualora non
	 *                                                ci sia nessuna proiezione con
	 *                                                quell'Id
	 * @throws ProjectionIsNoLongerProjectedException Eccezione lanciata qualora la
	 *                                                data della proiezione inserita
	 *                                                sia inferiore alla data
	 *                                                odierna
	 */
	public Projection getCurrentlyAvailableProjection(int id) throws ProjectionException {
		for (Projection p : cinemaProjections) {
			if ((p.getId() == id) && (p.getDateTime().isAfter(LocalDateTime.now()))) {
				return p;
			}
			if ((p.getId() == id) && (p.getDateTime().isBefore(LocalDateTime.now()))) {
				throw new ProjectionException("La proiezione selezionata con ID " + id + " non è più disponibile.");
			}
		}
		throw new ProjectionException("La proiezione con id " + id + " non esiste.");
	}

	/**
	 * METODO per resituire un coupon, dato il suo id (progressivo)
	 * 
	 * @param progressive Id del coupon
	 * @return Coupon Coupon con quello specifico progressivo
	 * @throws CouponNotExistsException Eccezione lanciata qualora non ci sia nessun
	 *                                  coupon con quell'Id progressivo
	 */
	public Coupon getCoupon(String code) throws CouponException {
		try {
			return persistenceFacade.getCoupon(code);
		} catch (PersistenceException e) {
			throw new CouponException("Il coupon " + code + " non esiste.");
		}
	}
	

	/**
	 * METODO per settare/cambiare la "location" in cui si trova il cinema
	 * 
	 * @param city    Citta
	 * @param country Paese
	 * @param zipCode Codice comunale
	 * @param address Indirizzo (Via, numero civico)
	 */
	public void setLocation(String city, String country, String zipCode, String address) {
		this.city = city;
		this.country = country;
		this.zipCode = zipCode;
		this.address = address;
	}

	/**
	 * METODO per farsi dire l'età più elevata da cui il cinema effettua uno sconto
	 * sul totale
	 * 
	 * @return max_age Età massima
	 */
	public int getMaxDiscountAge() {
		return new DiscountAge().getMax_age();
	}

	/**
	 * METODO per farsi dire l'età più bassa da cui il cinema effettua uno sconto
	 * sul totale
	 * 
	 * @return min_age Età minima
	 */
	public int getMinDiscountAge() {
		return new DiscountAge().getMin_age();
	}

	/**
	 * METODO per farsi dire il numero di sale di cui il cinema è composto
	 * 
	 * @return numberOfRooms
	 */
	public int getNumberOfRooms() {
		return rooms.size();
	}

	/**
	 * METODO per farsi dire tutte le sale del cinema
	 * 
	 * @return rooms
	 * @throws PersistenceException
	 */
	public List<Room> getAllRooms() throws PersistenceException {
		return persistenceFacade.getAllRooms();
	}

	/**
	 * METODO per farsi dire dal cinema la sala, dato l'id
	 * 
	 * @return
	 * @throws RoomNotExistsException
	 */
	public Room getRoom(int roomId) throws RoomException {
		try {
			return persistenceFacade.getRoom(roomId);
		} catch (PersistenceException e) {
			throw new RoomException("La sala con id " + roomId + " non è presente all'interno del cinema.");
		}
	}

	/**
	 * METODO per farsi dire le informazioni del luogo in cui il cinema è situato
	 * 
	 * @return location
	 */
	public String getLocation() {
		return address + ", " + city + " - " + zipCode + " " + country;
	}

	/**
	 * METODO per impostare la proiezione di una prenotazione
	 * 
	 * @param r
	 * @param projectionId
	 * @throws NoProjectionException
	 * @throws ReservationNotExistsException
	 */
	public void setReservationProjection(long r, int projectionId) throws ProjectionException, ReservationException {
		getReservation(r).setProjection(getProjection(projectionId));
	}

	/**
	 * METODO per farsi dire il numero di colonne della sala in cui è proiettato il
	 * film della prenotazione
	 * 
	 * @param r
	 * @return
	 * @throws ReservationNotExistsException
	 */
	public int getNumberColsReservationProjection(long r) throws ReservationException {
		return getReservation(r).getProjection().getRoom().getNumberCols();
	}

	/**
	 * METODO per farsi dire il numero di righe della sala in cui è proiettato il
	 * film della prenotazione
	 * 
	 * @param r
	 * @return
	 * @throws ReservationNotExistsException
	 */
	public int getNumberRowsReservationProjection(long r) throws ReservationException {
		return getReservation(r).getProjection().getRoom().getNumberRows();
	}

	/**
	 * METODO per farsi dire se il posto della sala selezionata dalla prenotazione è
	 * libero o meno
	 * 
	 * @param r
	 * @param row
	 * @param col
	 * @return
	 * @throws ProjectionException
	 * @throws InvalidRoomSeatCoordinatesException
	 * @throws ReservationNotExistsException
	 */
	public boolean checkIfProjectionSeatIsAvailable(int p, int row, int col)
			throws RoomException, ReservationException, ProjectionException {
		return getProjection(p).checkIfSeatIsAvailable(row, col);
	}

	public int getReservationProjection(long r) throws ReservationException {
		for (Reservation reservation : cinemaReservations) {
			if (reservation.getProgressive() == r)
				return reservation.getProjection().getId();
		}
		throw new ReservationException("La prenotazione " + r + " non esiste.");
	}

	/**
	 * METODO per aggiungere un posto alla reservation
	 * 
	 * @param r
	 * @param row
	 * @param col
	 * @throws SeatAlreadyTakenException
	 * @throws InvalidRoomSeatCoordinatesException
	 * @throws SeatTakenTwiceException
	 * @throws FreeAnotherPersonSeatException
	 * @throws ReservationNotExistsException
	 */
	public void addSeatToReservation(long r, int row, int col)
			throws RoomException, SeatAvailabilityException, ReservationException {
		getReservation(r).addSeat(row, col);
	}

	/**
	 * METODO per rimuovere un posto dalla reservation
	 * 
	 * @param r
	 * @param row
	 * @param col
	 * @throws ReservationException
	 * @throws SeatAvailabilityException
	 * @throws RoomException
	 * @throws SeatAlreadyTakenException
	 * @throws InvalidRoomSeatCoordinatesException
	 * @throws SeatTakenTwiceException
	 * @throws FreeAnotherPersonSeatException
	 * @throws ReservationNotExistsException
	 */
	public void removeSeatFromReservation(long r, int row, int col) throws RoomException, ReservationException {
		getReservation(r).removeSeat(row, col);
	}

	/**
	 * METODO per aggiungere informazioni sul cliente che effettua la prenotazione
	 * 
	 * @param r
	 * @param name
	 * @param surname
	 * @param email
	 * @throws InvalidSpectatorInfoException
	 * @throws ReservationNotExistsException
	 */
	public void setReservationPurchaser(long r, String name, String surname, String email)
			throws InvalidSpectatorInfoException, ReservationException {
		getReservation(r).setPurchaser(new Spectator(name, surname, email));
	}

	/**
	 * METODO per impostare il ccv di una carta di credito
	 * 
	 * @param p
	 * @param ccv
	 * @throws ReservationException
	 * @throws InvalidCCVException
	 */
	public void setReservationPaymentCard(long r, String number, String owner, String cvv, YearMonth expirationDate)
			throws ReservationException {
		getReservation(r).setPaymentCard(number, owner, cvv, expirationDate);
	}

	/**
	 * METODO per impostare il numero di persone che hanno un età inferiore ad un
	 * età minima da cui parte lo sconto per la proiezione indicata (per età)
	 * 
	 * @param r
	 * @param n
	 * @throws InvalidNumberPeopleValueException
	 * @throws ReservationNotExistsException
	 */
	public void setReservationNumberPeopleUntilMinAge(long r, int n) throws DiscountException, ReservationException {
		getReservation(r).setNumberPeopleUnderMinAge(n);
	}

	/**
	 * METODO per impostare il numero di persone che hanno un età superiore ad un
	 * età a partire dalla quale parte lo sconto per la proiezione indicata (per
	 * età)
	 * 
	 * @param r
	 * @param n
	 * @throws InvalidNumberPeopleValueException
	 * @throws ReservationNotExistsException
	 */
	public void setReservationNumberPeopleOverMaxAge(long r, int n) throws DiscountException, ReservationException {
		getReservation(r).setNumberPeopleOverMaxAge(n);
	}

	/**
	 * METODO per aggiungere alla prenotazione un eventuale coupon per un ulteriore
	 * sconto sul totale
	 * 
	 * @param r
	 * @param coupon
	 * @throws CouponNotExistsException
	 * @throws CouponAleadyUsedException
	 * @throws ReservationNotExistsException
	 */
	public void setReservationCoupon(long r, String code) throws CouponException, ReservationException {
		Coupon coupon = getCoupon(code);
		if (coupon.isUsed() == true) {
			throw new CouponException("Il coupon " + code + " è già stato usato.");
		} else
			getReservation(r).setCoupon(coupon);
	}

	/**
	 * METODO per comprare una prenotazione, una volta inseriti tutti i dati
	 * 
	 * @param r
	 * @throws NumberFormatException
	 * @throws SeatAlreadyTakenException
	 * @throws InvalidRoomSeatCoordinatesException
	 * @throws ReservationHasNoSeatException
	 * @throws ReservationHasNoPaymentCardException
	 * @throws PaymentErrorException
	 * @throws PersistenceException 
	 * @throws ReservationNotExistsException
	 */
	public void buyReservation(long r) throws NumberFormatException, SeatAvailabilityException, RoomException,
			ReservationException, PaymentErrorException, ReservationException, PersistenceException {
		getReservation(r).buy();
		/* Se la reservation è associata ad un coupon una volta terminato il metodo buy
		* dico che quel coupon è stato già utilizzato, in modo tale da impedirne il riutilizzo */
		Coupon coupon = getReservation(r).getCoupon();
		if (coupon != null) {
			persistenceFacade.setCouponUsed(coupon.getCode());
		}
	}

	/**
	 * METODO per farsi dire il totale di una prenotazione
	 * 
	 * @param r
	 * @return
	 * @throws ReservationNotExistsException
	 */
	public double getReservationTotalAmount(long r) throws ReservationException {
		return getReservation(r).getTotal();
	}

	/**
	 * METODO per inviare un email al cliente che ha compilato la prenotazione
	 * comprendente il report (documento comprendente le varie informazioni sulla
	 * sua prenotazione: film, posti prenotati, ora, ecc...)
	 * 
	 * @param r
	 * @throws HandlerException
	 * @throws ReservationNotExistsException
	 */
	public void sendReservationEmail(long r) throws ReservationException, HandlerException {
		emailHandler.sendEmail(getReservation(r));
	}

	/**
	 * METODO per farsi dire il nome del cinema
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * METODO per farsi restituire l'email del cinema
	 * 
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * METODO per farsi dare il logo del cinema
	 * 
	 * @return
	 */
	public String getLogoURL() {
		return logoURL;
	}

	/**
	 * METODO per farsi restituire la password del cinema
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * METODO per settare la strategia
	 * 
	 * @param td
	 * @throws DiscountNotFoundException
	 */
	public void setCinemaDiscountStrategy(TypeOfDiscounts td) throws DiscountNotFoundException {
		cinemaDiscount = this.getDiscountByStrategy(td);
	}

	/**
	 * METODO per aggiungere una strategia di sconto alla lista Non si può
	 * aggiungere uno sconto di una tipologià già esistente
	 * 
	 * @param d
	 */
	public void addDiscount(Discount d) {
		boolean alreadyExists = false;
		for (TypeOfDiscounts td : getAllDiscountStrategy()) {
			if (d.getTypeOfDiscount().equals(td))
				alreadyExists = true;
		}
		if (!alreadyExists)
			allDiscounts.add(d);
	}

	/**
	 * METODO per rimuovere una strategia di sconto dalla lista, dato il suo tipo
	 * 
	 * @param td
	 * @throws DiscountNotFoundException
	 */
	public void removeDiscount(TypeOfDiscounts td) throws DiscountNotFoundException {
		allDiscounts.remove(getDiscountByStrategy(td));
	}

	/**
	 * METODO per farsi dire tutte le tipologie di sconto applicabili
	 * 
	 * @param td
	 */
	public ArrayList<TypeOfDiscounts> getAllDiscountStrategy() {
		ArrayList<TypeOfDiscounts> allTypeOfDiscounts = new ArrayList<TypeOfDiscounts>();
		for (Discount d : this.allDiscounts) {
			if (!allTypeOfDiscounts.contains(d.getTypeOfDiscount())) {
				allTypeOfDiscounts.add(d.getTypeOfDiscount());
			}
		}
		return allTypeOfDiscounts;
	}

	/**
	 * METODO per farsi dare uno sconto data la sua strategia
	 * 
	 * @param t
	 * @return
	 * @throws DiscountNotFoundException
	 */
	public Discount getDiscountByStrategy(TypeOfDiscounts t) throws DiscountNotFoundException {
		Discount discount = null;
		for (Discount d : allDiscounts) {
			if (d.getTypeOfDiscount() == t) {
				discount = d;
			}
		}
		if (discount == null)
			throw new DiscountNotFoundException("Non è stato trovato nessuno sconto che applica la strategia " + t);
		else
			return discount;
	}

	public String getDiscountStrategyDescription(TypeOfDiscounts t) throws DiscountNotFoundException {
		return getDiscountByStrategy(t).toString();
	}

	/** METODO per farsi dire la password dell'admin */
	public String getAdminPassword() {
		return adminPassword;
	}

	/* METODO per cambiare la password dell'admin */
	public void setPassword(String newAdminPassword) throws PasswordException {
		if (newAdminPassword.length() < 5) {
			throw new PasswordException("La password inserita è troppo corta");
		}
		adminPassword = newAdminPassword;
	}

	/**
	 * METODO per effettuare il login per l'amministratore
	 * 
	 * @param password Password inserita
	 * @return Esito del login
	 * @throws WrongAdminPasswordException Eccezione lanciata qualora si inserisca
	 *                                     una password errata nella fase di login
	 */
	public void login(String password) throws PasswordException {
		if (!getAdminPassword().equals(password))
			throw new PasswordException("La password inserita è errata.");
	}

	public int getReservationNSeats(long r) throws ReservationException {
		return getReservation(r).getNSeats();
	}

	public String getReservationTypeOfDiscount(long r) throws ReservationException {
		return getReservation(r).getTypeOfDiscount().name();
	}

	public double getReservationFullPrice(long r) throws ReservationException {
		return getReservation(r).getFullPrice();
	}

	public double getReservationCouponDiscount(long r) throws ReservationException {
		try {
			return getReservation(r).getCoupon().getDiscount();
		} catch (NullPointerException exception) {
			return 0.0;
		}
	}

	public String getReservationCouponCode(long r) throws ReservationException {
		return getReservation(r).getCoupon().getCode();
	}

	public Movie getProjectionMovie(int p) throws ProjectionException {
		for (Projection projection : cinemaProjections) {
			if (projection.getId() == p) {
				return projection.getMovie();
			}
		}
		throw new ProjectionException("La proiezione con id " + p + " non esiste.");
	}

	public LocalDateTime getProjectionDateTime(int p) throws ProjectionException {
		for (Projection projection : cinemaProjections) {
			if (projection.getId() == p) {
				return projection.getDateTime();
			}
		}
		throw new ProjectionException("La proiezione con id " + p + " non esiste.");
	}

	public Room getProjectionRoom(int p) throws ProjectionException {
		for (Projection projection : cinemaProjections) {
			if (projection.getId() == p) {
				return projection.getRoom();
			}
		}
		throw new ProjectionException("La proiezione con id " + p + " non esiste.");
	}
}