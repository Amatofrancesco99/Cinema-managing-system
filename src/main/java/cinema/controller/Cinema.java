package cinema.controller;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import cinema.model.Movie;
import cinema.model.spectator.Spectator;
import cinema.model.cinema.Room;
import cinema.model.cinema.util.InvalidRoomDimensionsException;
import cinema.model.cinema.util.InvalidRoomSeatCoordinatesException;
import cinema.model.payment.methods.paymentCard.PaymentCard;
import cinema.model.payment.methods.paymentCard.util.ExpiredCreditCardException;
import cinema.model.payment.methods.paymentCard.util.InvalidCCVException;
import cinema.model.payment.methods.paymentCard.util.InvalidCreditCardNumberException;
import cinema.model.payment.util.PaymentErrorException;
import cinema.controller.util.*;
import cinema.model.projection.Projection;
import cinema.model.projection.util.InvalidPriceException;
import cinema.model.projection.util.InvalidProjectionIdException;
import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.coupon.Coupon;
import cinema.model.reservation.discount.coupon.util.CouponAleadyUsedException;
import cinema.model.reservation.discount.coupon.util.CouponNotExistsException;
import cinema.model.reservation.discount.types.Discount;
import cinema.model.reservation.discount.types.DiscountAge;
import cinema.model.reservation.discount.types.DiscountDay;
import cinema.model.reservation.discount.types.DiscountNumberSpectators;
import cinema.model.reservation.discount.types.util.InvalidNumberPeopleValueException;
import cinema.model.reservation.discount.types.util.TypeOfDiscounts;
import cinema.model.reservation.util.FreeAnotherPersonSeatException;
import cinema.model.spectator.util.InvalidSpectatorInfoException;
import cinema.model.reservation.util.ReservationHasNoPaymentCardException;
import cinema.model.reservation.util.ReservationHasNoSeatException;
import cinema.model.reservation.util.SeatAlreadyTakenException;
import cinema.model.reservation.util.SeatTakenTwiceException;

/**
 * BREVE SPIEGAZIONE CLASSE CINEMA ( Pattern Controller)
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Questa classe rappresenta il controller del nostro progetto, ossia la classe che consente 
 * alla vista di poter eseguire operazioni utili, senza dover conoscere la logica di dominio.
 * Essa è molto utile poiché qualora si usino interfacce diverse, come nel nostro caso
 * (CLI e GUI) gli stessi metodi della CLI saranno presenti nella GUI, chiaramente cambierà
 * il modo di gestire situazioni particolari, ma comunque permette di rendere
 * migliore, tra le altre cose, la modificabilità del codice, l'high coesion ed
 * il low coupling. 
 */
public class Cinema {
	
	
	/**
	 * ATTRIBUTI
	 * 
	 * @param name              Nome
	 * @param city              Città
	 * @param country           Paese
	 * @param zipCode           Codice comunale
	 * @param address           Indirizzo (Via, numero civico)
	 * @param logoURL           Logo del cinema
	 * @param email             E-mail, utile per inviare report al cliente con i
	 *                          diversi dati riferiti alla specifica prenotazione,
	 *                          effettuata da quest ultimo
	 * @param password          Password associata all'indirizzo email
	 * @param adminPassword     Password dell'amministratore del cinema
	 * @param rooms             List: comprende tutte le sale del cinema
	 * @param cinemaProjections List: comprende tutte le proiezioni fatte dal cinema
	 * @param coupon            List: comprende tutti i coupon emessi dal cinema
	 * @param cinemaDiscount    Sconto attivo
	 * @param allDiscounts      Tutti gli sconti applicabili
	 */
	private static String name;
	private static String city;
	private static String country;
	private static String zipCode;
	private static String address;
	private static String logoURL;
	private static String email;
	private static String password;
	private static String adminPassword;
	private List<Room> rooms;
	private List<Projection> cinemaProjections;
	private List<Coupon> coupons;
	private Discount cinemaDiscount;
	private ArrayList<Discount> allDiscounts;
	
	
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
		rooms = new ArrayList<Room>();
		cinemaProjections = new ArrayList<Projection>();
		coupons = new ArrayList<Coupon>();
		cinemaDiscount = new DiscountAge();
		allDiscounts = new ArrayList<Discount>();
		addDiscount(new DiscountAge());
		addDiscount(new DiscountDay());
		addDiscount(new DiscountNumberSpectators());


		// ********* TEMPORARY DATA USED FOR TESTING *********
		
		// Test movie
		ArrayList<String> genres, directors, cast;
		genres = new ArrayList<>();
		directors = new ArrayList<>();
		cast = new ArrayList<>();

		// DRUK
		genres.add("Drammatico");
		genres.add("Commedia");
		directors.add("Thomas Vinterberg");
		cast.add("Mads Mikkelsen");
		cast.add("Thomas Bo Larsen");
		cast.add("Lars Ranthe");
		cast.add("Magnus Millang");
		Movie drukMovie = new Movie(1, "Druk - Un altro giro",
				"C'è una teoria secondo la quale tutti noi siamo nati con una piccola quantità di alcool già presente nel sangue e che, pertanto, una piccola ebbrezza possa aprire le nostre menti al mondo che ci circonda, diminuendo la nostra percezione dei problemi e aumentando la nostra creatività. Rincuorati da questa teoria, Martin e tre suoi amici, tutti annoiati insegnanti delle superiori, intraprendono un esperimento per mantenere un livello costante di ubriachezza durante tutta la giornata lavorativa. Se Churchill vinse la seconda guerra mondiale in preda a un pesante stordimento da alcool, chissà cosa potrebbero fare pochi bicchieri per loro e per i loro studenti?",
				genres, directors, cast, 4, 117, "druk-un-altro-giro.jpg",
				"https://www.youtube.com/watch?v=hFbDh58QHzw");

		genres = new ArrayList<>();
		directors = new ArrayList<>();
		cast = new ArrayList<>();

		// PULP FICTION
		genres.add("Drammatico");
		genres.add("Thriller");
		directors.add("Quentin Tarantino");
		cast.add("Uma Thurman");
		cast.add("John Travolta");
		cast.add("Samuel L. Jackson");
		cast.add("Bruce Willis");
		cast.add("Steve Buscemi");
		Movie pulpFictionMovie = new Movie(3, "Pulp Fiction",
				"Un killer si innamora della moglie del suo capo, un pugile rinnega la sua promessa e una coppia tenta una rapina che va rapidamente fuori controllo.",
				genres, directors, cast, 5, 154, "pulp-fiction.jpg", "https://www.youtube.com/watch?v=s7EdQ4FqbhY");

		genres = new ArrayList<>();
		directors = new ArrayList<>();
		cast = new ArrayList<>();

		// NO COUNTRY FOR OLD MEN
		genres.add("Thriller");
		genres.add("Crime");
		genres.add("Neo-Western");
		directors.add("Joel Coen");
		directors.add("Ethan Coen");
		cast.add("Javier Bardem");
		cast.add("Tommy Lee Jones");
		cast.add("Josh Brolin");
		cast.add("Kelly Mcdonald");
		Movie noCountryForOldMenMovie = new Movie(4, "Non è un paese per vecchi",
				"Texas, 1980. Mentre è a caccia nei territori selvaggi al confine con il Messico, Llewelyn Moss, un saldatore texano reduce dalla guerra del Vietnam, si imbatte in quel che resta di un regolamento di conti tra bande locali per una partita di droga. In mezzo ai numerosi cadaveri, Moss trova un'ingente somma di denaro che si porta a casa, con l'intento di assicurarsi un futuro migliore per sé e la giovane moglie, Carla Jean.",
				genres, directors, cast, 4, 122, "non-e-un-paese-per-vecchi.jpg",
				"https://www.youtube.com/watch?v=38A__WT3-o0");

		genres = new ArrayList<>();
		directors = new ArrayList<>();
		cast = new ArrayList<>();

		// SKYFALL
		genres.add("Drammatico");
		genres.add("Action");
		genres.add("Spy");
		directors.add("Sam Mendes");
		cast.add("Daniel Craig");
		cast.add("Judi Dench");
		cast.add("Javier Bardem");
		cast.add("Ben Whishaw");
		Movie skyfallMovie = new Movie(5, "Skyfall",
				"In seguito al fallimento di una missione recente, Il celebre agente segreto britannico è costretto ad essere testimone di una serie terribile di eventi: la MI6 deve trasferirsi al più presto mentre i dipendenti sotto copertura vedono le proprie identità venire rivelate. M è disperata e si rivolge a James Bond in cerca di un aiuto immediato.",
				genres, directors, cast, 4, 143, "skyfall.jpg", "https://www.youtube.com/watch?v=OnlSRBTG5Tw");

		genres = new ArrayList<>();
		directors = new ArrayList<>();
		cast = new ArrayList<>();

		// AVENGERS - ENDGAME
		genres.add("Azione");
		genres.add("Fantascienza");
		genres.add("Avventura");
		directors.add("Anthony Russo");
		directors.add("Joe Russo");
		cast.add("Robert Downey Jr.");
		cast.add("Chris Evans");
		cast.add("Mark Ruffalo");
		cast.add("Chris Hemsworth");
		cast.add("Scarlett Johansson");
		cast.add("Jeremy Renner");
		Movie avengersEndgameMovie = new Movie(2, "Avengers - Endgame",
				"Tutto riparte dalla terra devastata da Thanos e le sue gemme dell'infinito, dopo aver visto svanire in particelle infinitesimali di polvere i Guardiani della Galassia e personaggi come Spider-Man, Black Panther e Dr. Strange. Questa seconda parte evidenzia una situazione altamente drammatica, con i superstiti che non sanno darsi pace, dilaniati dal senso di colpa e dal vuoto ed incapaci di ripartire. I legami rimasti continuano a rafforzarsi, seppur in sordina, quelli che avevano portato a scontri mutano in fiducia, le amicizie si cementano, i rapporti di vecchia data si consolidano ed alcuni cercano di costruirsi una famiglia fino a cinque anni dopo, quando una \"microscopica\" particella ritrovata rimette in moto lo spirito di gruppo. Un paese in ginocchio che prova a cambiare le cose e ripristinare l'ordine, quantomeno apparente e in mezzo al prevedibile e oramai noto affiorano sorprese inaspettate. Il finale non si tinge di dark, ma in alcuni tratti è caratterizzato da toni distesi, dialoghi divertenti, personaggi quasi caricaturali senza i muscoli di \"Avengers: Infinity War\", ma tanto cervello e costruzione dell'azione come del pensiero.",
				genres, directors, cast, 5, 182, "avengers-endgame.jpg", "https://www.youtube.com/watch?v=vqWz0ZCpYBs");

		// Test room
		try {
			addRoom(new Room(5, 10));
			addRoom(new Room(7, 10));
		} catch (InvalidRoomDimensionsException e) {
			e.printStackTrace();
		}

		// Test projections
		Projection p1 = new Projection(100, drukMovie, LocalDateTime.parse("2021-06-04T22:30:00"), 12.5,
				rooms.get(0));
		Projection p2 = new Projection(101, drukMovie, LocalDateTime.parse("2021-06-01T20:15:00"), 12.5,
				rooms.get(0));
		Projection p3 = new Projection(102, drukMovie, LocalDateTime.parse("2021-06-01T22:30:00"), 12.5,
				rooms.get(0));
		Projection p4 = new Projection(103, drukMovie, LocalDateTime.parse("2021-06-02T22:30:00"), 12.5,
				rooms.get(0));
		Projection p5 = new Projection(104, drukMovie, LocalDateTime.parse("2021-06-02T23:30:00"), 12.5,
				rooms.get(0));
		Projection p6 = new Projection(105, drukMovie, LocalDateTime.parse("2021-06-02T19:00:00"), 12.5,
				rooms.get(0));
		Projection p7 = new Projection(106, drukMovie, LocalDateTime.parse("2021-06-03T08:05:00"), 12.5,
				rooms.get(0));
		Projection p8 = new Projection(200, avengersEndgameMovie, LocalDateTime.parse("2021-06-02T22:30:00"),
				12.5, rooms.get(1));
		Projection p9 = new Projection(201, avengersEndgameMovie, LocalDateTime.parse("2021-06-02T23:30:00"),
				12.5, rooms.get(1));
		Projection p10 = new Projection(202, avengersEndgameMovie, LocalDateTime.parse("2021-06-02T19:00:00"),
				12.5, rooms.get(1));
		Projection p11 = new Projection(203, avengersEndgameMovie, LocalDateTime.parse("2021-06-03T08:05:00"),
				12.5, rooms.get(1));
		Projection p12 = new Projection(300, pulpFictionMovie, LocalDateTime.parse("2021-06-03T08:05:00"),
				8.5, rooms.get(1));
		Projection p13 = new Projection(301, pulpFictionMovie, LocalDateTime.parse("2021-06-06T22:30:00"),
				8.5, rooms.get(0));
		Projection p14 = new Projection(302, pulpFictionMovie, LocalDateTime.parse("2021-06-02T23:30:00"),
				8.5, rooms.get(0));
		Projection p15 = new Projection(303, pulpFictionMovie, LocalDateTime.parse("2021-06-01T19:00:00"),
				8.5, rooms.get(0));
		Projection p16 = new Projection(304, pulpFictionMovie, LocalDateTime.parse("2021-06-03T08:05:00"),
				8.5, rooms.get(0));
		Projection p17 = new Projection(400, noCountryForOldMenMovie, LocalDateTime.parse("2021-06-22T23:30:00"),
				8.5, rooms.get(0));
		Projection p18 = new Projection(401, noCountryForOldMenMovie, LocalDateTime.parse("2021-06-21T19:00:00"),
				8.5, rooms.get(0));
		Projection p19 = new Projection(402, noCountryForOldMenMovie, LocalDateTime.parse("2021-06-23T08:05:00"),
				8.5, rooms.get(0));
		Projection p20 = new Projection(500, skyfallMovie, LocalDateTime.parse("2021-07-01T08:05:00"), 8.5,
				rooms.get(0));
		Projection p21 = new Projection(501, skyfallMovie, LocalDateTime.parse("2021-07-02T23:30:00"), 8.5,
				rooms.get(0));
		Projection p22 = new Projection(502, skyfallMovie, LocalDateTime.parse("2021-07-01T19:00:00"), 8.5,
				rooms.get(0));
		Projection p23 = new Projection(503, skyfallMovie, LocalDateTime.parse("2021-07-02T08:05:00"), 8.5,
				rooms.get(0));

		this.cinemaProjections.add(p1);
		this.cinemaProjections.add(p2);
		this.cinemaProjections.add(p3);
		this.cinemaProjections.add(p4);
		this.cinemaProjections.add(p5);
		this.cinemaProjections.add(p6);
		this.cinemaProjections.add(p7);
		this.cinemaProjections.add(p8);
		this.cinemaProjections.add(p9);
		this.cinemaProjections.add(p10);
		this.cinemaProjections.add(p11);
		this.cinemaProjections.add(p12);
		this.cinemaProjections.add(p13);
		this.cinemaProjections.add(p14);
		this.cinemaProjections.add(p15);
		this.cinemaProjections.add(p16);
		this.cinemaProjections.add(p17);
		this.cinemaProjections.add(p18);
		this.cinemaProjections.add(p19);
		this.cinemaProjections.add(p20);
		this.cinemaProjections.add(p21);
		this.cinemaProjections.add(p22);
		this.cinemaProjections.add(p23);

		// occupare il primo posto della seconda proiezione
		try {
			p2.takeSeat(0, 0);
		} catch (InvalidRoomSeatCoordinatesException e) {
		}

		// Aggiunti due coupon di prova emessi dal cinema
		coupons.add(new Coupon(5));
		coupons.get(0).setUsed(true); // Coupon ID: 1 già utilizzato (Prova)
		coupons.add(new Coupon(6));
		coupons.add(new Coupon(3.5));
	}


	/**
	 * METODO per creare una nuova prenotazione (vuota), a partire dalla classe
	 * cinema
	 * 
	 * @return reservation Nuova prenotazione creata
	 */
	public Reservation createReservation() {
		return new Reservation(cinemaDiscount);
	}

	
	/**
	 * METODO per aggiungere una proiezione al cinema
	 * 
	 * @param p Proiezione da aggiungere alla lista di proiezioni di cui il cinema
	 *          dispone
	 */
	public void addProjection(Projection p) {
		cinemaProjections.add(p);
	}

	
	/**
	 * METODO per rimuovere una proiezione al cinema
	 * 
	 * @param p Proiezione da rimuovere alla lista di proiezioni di cui il cinema
	 *          dispone
	 */
	public void removeProjection(Projection p) {
		cinemaProjections.remove(p);
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
	 * @param newProjection
	 * @param id
	 * @throws ProjectionIDAlreadyUsedException
	 * @throws InvalidProjectionIdException
	 */
	public void setProjectionID(Projection newProjection, int id) throws ProjectionIDAlreadyUsedException, InvalidProjectionIdException {
		for (Projection p: cinemaProjections) {
			if (p.getId() == id) throw new ProjectionIDAlreadyUsedException(id);
		}
		newProjection.setId(id);
	}
	
	
	/** METODO per associare ad una proiezione un film
	 * 
	 * @param p
	 * @param movie
	 * @throws NoMovieException 
	 */
	public void setProjectionMovie(Projection p, int movieId) throws NoMovieException {
		p.setMovie(getMovie(movieId));
	}
	
	
	/** METODO per associare alla proiezione una sala 
	 * 
	 * @param p
	 * @param roomId
	 * @throws RoomNotExistsException
	 */
	public void setProjectionRoom(Projection p, long roomId) throws RoomNotExistsException {
		p.setRoom(getRoom(roomId));
	}
	
	
	/** METODO per associare alla proiezione un prezzo
	 * 
	 * @param p
	 * @param price
	 * @throws InvalidPriceException
	 */
	public void setProjectionPrice(Projection p, double price) throws InvalidPriceException {
		p.setPrice(price);
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
	 * METODO per restituire le proiezioni attualmente realizzate da un cinema, inerenti uno specifico film
	 * tramite l'id
	 * 
	 * @param movieId Id del film di cui si vogliono cercare le proiezioni attualmente disponibili
	 * @return ArrayList<Projection> Insieme delle proiezioni attualmente disponibili dello specifico film
	 * @throws NoMovieException
	 * @throws MovieNoLongerProjectedException  Eccezione lanciata qualora il film inserito
	 * 											non abbia proiezioni attualmente disponibili
	 * @throws NoMovieProjectionsException Eccezione lanciata, qualora il cinema non
	 *                                     abbia quel film, tra i film proiettati
	 */
	public List<Projection> getCurrentlyAvailableProjections(int movieId) throws NoMovieException, MovieNoLongerProjectedException {
		List<Projection> movieProjections = new ArrayList<Projection>();
		Movie m = getMovie(movieId);
		if (m != null) {
			for (Projection p : cinemaProjections) {
				if ((p.getMovie().getId() == movieId) && 
				   (p.getDateTime().isAfter(LocalDateTime.now()))){
					movieProjections.add(p);
				}
			}
		}
		if (movieProjections.size() != 0) return movieProjections;
		else throw new MovieNoLongerProjectedException(this.getMovie(movieId));
	}

	
	/*METODO per farci dare tutte i film inerenti ad un anno specifico*/
	public ArrayList<Movie> getAllMovies(int year){
		// TODO with persistence
		return null;
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
			if (p.getDateTime().isAfter(LocalDateTime.now()))
			{
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
		for (Projection p : cinemaProjections) {
			if (p.getMovie().getId() == id) {
				return p.getMovie();
			}
		}
		throw new NoMovieException(id);
	}

	
	/**
	 * METODO per resituire una proiezione, dato il suo Id
	 * 
	 * @param id Id della proiezione
	 * @return Projection Proiezione con quello specifico Id
	 * @throws NoProjectionException Eccezione lanciata qualora non ci sia nessuna
	 *                               proiezione con quell'Id
	 */
	public Projection getProjection(int id) throws NoProjectionException {
		for (Projection p : cinemaProjections) {
			if (p.getId() == id) {
				return p;
			}
		}
		throw new NoProjectionException(id);
	}

	
	/**
	 * METODO per resituire una proiezione, se attualmente proiettata, dato il suo Id
	 * 
	 * @param id Id della proiezione che si vuole verificare se sia disponibile
	 * @return Projection Proiezione con quello specifico Id, attualmente proiettata
	 * @throws NoProjectionException Eccezione lanciata qualora non ci sia nessuna
	 *                               proiezione con quell'Id
	 * @throws ProjectionIsNoLongerProjectedException   Eccezione lanciata qualora la data
	 * 													della proiezione inserita sia 
	 * 													inferiore alla data odierna
 	 */
	public Projection getCurrentlyAvailableProjection(int id) throws NoProjectionException, ProjectionIsNoLongerProjectedException {
		for (Projection p : cinemaProjections) {
			if ((p.getId() == id) && (p.getDateTime().isAfter(LocalDateTime.now()))) {
				return p;
			}
			if ((p.getId() == id) && (p.getDateTime().isBefore(LocalDateTime.now()))) {
				throw new ProjectionIsNoLongerProjectedException(id);
			}
		}
		throw new NoProjectionException(id);
	}
	
	
	/**
	 * METODO per resituire un coupon, dato il suo id (progressivo)
	 * 
	 * @param progressive Id del coupon
	 * @return Coupon Coupon con quello specifico progressivo
	 * @throws CouponNotExistsException Eccezione lanciata qualora non ci sia nessun
	 *                                  coupon con quell'Id progressivo
	 */
	public Coupon getCoupon(long progressive) throws CouponNotExistsException {
		for (Coupon c : coupons) {
			if (c.getProgressive() == progressive) {
				return c;
			}
		}
		throw new CouponNotExistsException(progressive);
	}
	
	
	/**
	 * METODO per aggiungere una sala del cinema
	 * 
	 * @param r Sala del cinema da aggiungere, all'insieme delle sale del cinema
	 *          stesso
	 */
	public void addRoom(Room r) {
		rooms.add(r);
	}

	
	/**
	 * METODO per rimuovere una sala del cinema
	 * 
	 * @param r Sala del cinema da rimuovere, dall'insieme delle sale del cinema
	 *          stesso
	 */
	public void removeRoom(Room r) throws NoCinemaRoomsException {
		if (rooms.size() > 0)
			rooms.remove(r);
		else
			throw new NoCinemaRoomsException(name, city, address);
	}

	
	/**
	 * METODO per settare/cambiare la "location" in cui si trova il cinema
	 * 
	 * @param city    Citta
	 * @param country Paese
	 * @param zipCode Codice comunale
	 * @param address Indirizzo (Via, numero civico)
	 */
	@SuppressWarnings("static-access")
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
	 */
	public List<Room> getAllRooms(){
		ArrayList<Room> allCinemaRooms = new ArrayList<Room>();
		for (int i=0; i< rooms.size();i++) {
			allCinemaRooms.add(rooms.get(i));
		}
		return allCinemaRooms;
	}
	
	
	/** METODO per farsi dire dal cinema la sala, dato l'id
	 * 
	 * @return
	 * @throws RoomNotExistsException 
	 */
	public Room getRoom(long roomId) throws RoomNotExistsException {
		for(Room r: rooms) {
			if (r.getProgressive() == roomId)
				return r;
		}
		throw new RoomNotExistsException(roomId);
	}
	
	/**
	 * METODO per farsi dire le informazioni del luogo in cui il cinema è situato
	 * 
	 * @return location
	 */
	public static String getLocation() {
		return address + ", " + city + " - " + zipCode + " " + country;
	}

	
	/**
	 * METODO per impostare la proiezione di una prenotazione
	 * 
	 * @param r
	 * @param projectionId
	 * @throws NoProjectionException
	 */
	public void setReservationProjection(Reservation r, int projectionId) throws NoProjectionException {
		r.setProjection(getProjection(projectionId));
	}

	
	/**
	 * METODO per farsi dire il numero di colonne della sala in cui è proiettato il
	 * film della prenotazione
	 * 
	 * @param r
	 * @return
	 */
	public int getNumberColsReservationProjection(Reservation r) {
		return r.getProjection().getRoom().getNumberCols();
	}

	
	/**
	 * METODO per farsi dire il numero di righe della sala in cui è proiettato il
	 * film della prenotazione
	 * 
	 * @param r
	 * @return
	 */
	public int getNumberRowsReservationProjection(Reservation r) {
		return r.getProjection().getRoom().getNumberRows();
	}

	
	/**
	 * METODO per farsi dire se il posto della sala selezionata dalla prenotazione è
	 * libero o meno
	 * 
	 * @param r
	 * @param row
	 * @param col
	 * @return
	 * @throws InvalidRoomSeatCoordinatesException
	 */
	public boolean checkIfReservationProjectionSeatIsAvailable(Reservation r, int row, int col)
			throws InvalidRoomSeatCoordinatesException {
		return r.getProjection().checkIfSeatIsAvailable(row, col);
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
	 */
	public void addSeatToReservation(Reservation r, int row, int col) throws SeatAlreadyTakenException,
			InvalidRoomSeatCoordinatesException, SeatTakenTwiceException, FreeAnotherPersonSeatException {
		r.addSeat(row, col);
	}

	
	/**
	 * METODO per aggiungere informazioni sul cliente che effettua la prenotazione
	 * 
	 * @param r
	 * @param name
	 * @param surname
	 * @param email
	 * @throws InvalidSpectatorInfoException
	 */
	public void setReservationPurchaser(Reservation r, String name, String surname, String email)
			throws InvalidSpectatorInfoException {
		r.setPurchaser(new Spectator(name, surname, email));
	}

	
	/**
	 * METODO per impostare il nome di una carta di credito
	 * 
	 * @param p
	 * @param owner
	 */
	public void setPaymentCardOwner(PaymentCard p, String owner) {
		p.setOwner(owner);
	}

	
	/**
	 * METODO per impostare il numero di una carta di credito
	 * 
	 * @param p
	 * @param number
	 * @throws InvalidCreditCardNumberException
	 */
	public void setPaymentCardNumber(PaymentCard p, String number) throws InvalidCreditCardNumberException {
		p.setNumber(number);
	}

	
	/**
	 * METODO per impostare la data di scadenza di una carta di credito
	 * 
	 * @param p
	 * @param expirationDate
	 * @throws ExpiredCreditCardException
	 */
	public void setPaymentCardExpirationDate(PaymentCard p, YearMonth expirationDate)
			throws ExpiredCreditCardException {
		p.setExpirationDate(expirationDate);
	}

	
	/**
	 * METODO per impostare il ccv di una carta di credito
	 * 
	 * @param p
	 * @param ccv
	 * @throws InvalidCCVException
	 */
	public void setPaymentCardCCV(PaymentCard p, String ccv) throws InvalidCCVException {
		p.setCCV(ccv);
	}

	
	/**
	 * METODO per aggiungere alla prenotazione la carta di credito
	 * 
	 * @param r
	 * @param p
	 */
	public void setReservationPaymentCard(Reservation r, PaymentCard p) {
		r.setPaymentCard(p);
	}

	
	/**
	 * METODO per impostare il numero di persone che hanno un età inferiore ad un
	 * età minima da cui parte lo sconto per la proiezione indicata (per età)
	 * 
	 * @param r
	 * @param n
	 * @throws InvalidNumberPeopleValueException
	 */
	public void setReservationNumberPeopleUntilMinAge(Reservation r, int n) throws InvalidNumberPeopleValueException {
		r.setNumberPeopleUntilMinAge(n);
	}

	
	/**
	 * METODO per impostare il numero di persone che hanno un età superiore ad un
	 * età a partire dalla quale parte lo sconto per la proiezione indicata (per
	 * età)
	 * 
	 * @param r
	 * @param n
	 * @throws InvalidNumberPeopleValueException
	 */
	public void setReservationNumberPeopleOverMaxAge(Reservation r, int n) throws InvalidNumberPeopleValueException {
		r.setNumberPeopleOverMaxAge(n);
	}

	
	/**
	 * METODO per aggiungere alla prenotazione un eventuale coupon per un ulteriore
	 * sconto sul totale
	 * 
	 * @param r
	 * @param coupon
	 * @throws CouponNotExistsException
	 * @throws CouponAleadyUsedException
	 */
	public void setReservationCoupon(Reservation r, long progressive)
			throws CouponNotExistsException, CouponAleadyUsedException {
		Coupon coupon = getCoupon(progressive);
		if (coupon.isUsed() == true) {
			throw new CouponAleadyUsedException(progressive);
		}
		else r.setCoupon(coupon);
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
	 */
	public void buyReservation(Reservation r)
			throws NumberFormatException, SeatAlreadyTakenException, InvalidRoomSeatCoordinatesException,
			ReservationHasNoSeatException, ReservationHasNoPaymentCardException, PaymentErrorException {
		r.buy();
	}

	
	/**
	 * METODO per farsi dire il totale di una prenotazione
	 * 
	 * @param r
	 * @return
	 */
	public double getReservationTotalAmount(Reservation r) {
		return r.getTotal();
	}


	/**
	 * METODO per inviare un email al cliente che ha compilato la prenotazione
	 * comprendente il report (documento comprendente le varie informazioni sulla
	 * sua prenotazione: film, posti prenotati, ora, ecc...)
	 * 
	 * @param r
	 */
	public void sendAnEmail(Reservation r) {
		r.sendEmail();
	}

	
	/**
	 * METODO per farsi dire il nome del cinema
	 * @return
	 */
	public static String getName() {
		return name;
	}

	
	/**
	 * METODO per farsi restituire l'email del cinema
	 * @return
	 */
	public static String getEmail() {
		return email;
	}

	
	/**
	 * METODO per farsi dare il logo del cinema
	 * @return
	 */
	public static String getLogoURL() {
		return logoURL;
	}


	/**
	 * METODO per farsi restituire la password del cinema
	 * @return
	 */
	public static String getPassword() {
		return password;
	}
	
	
	/**
	 * METODO per settare la strategia
	 * @param td
	 * @throws DiscountNotFoundException 
	 */
	public void setCinemaDiscountStrategy(TypeOfDiscounts td) throws DiscountNotFoundException {
		cinemaDiscount = this.getDiscountByStrategy(td);
	}
	
	
	/**
	 * METODO per aggiungere una strategia di sconto alla lista 
	 * Non si può aggiungere uno sconto di una tipologià già esistente
	 * @param d
	 */
	public void addDiscount(Discount d) {
		boolean alreadyExists = false;
		for (TypeOfDiscounts td : getAllDiscountStrategy()) {
			if (d.getTypeOfDiscount().equals(td)) alreadyExists = true;
		}
		if (!alreadyExists) allDiscounts.add(d);
	}
	
	
	/**
	 * METODO per rimuovere una strategia di sconto dalla lista, dato il suo tipo
	 * @param td
	 * @throws DiscountNotFoundException 
	 */
	public void removeDiscount(TypeOfDiscounts td) throws DiscountNotFoundException {
		allDiscounts.remove(getDiscountByStrategy(td));
	}
	
	
	/**
	 * METODO per farsi dire tutte le tipologie di sconto applicabili
	 * @param td
	 */
	public ArrayList<TypeOfDiscounts> getAllDiscountStrategy() {
		ArrayList<TypeOfDiscounts> allTypeOfDiscounts = new ArrayList<TypeOfDiscounts>();
		for (Discount d: this.allDiscounts) {
			if (!allTypeOfDiscounts.contains(d.getTypeOfDiscount())) {
				allTypeOfDiscounts.add(d.getTypeOfDiscount());
			}
		}
		return allTypeOfDiscounts;
	}

	
	/**
	 * METODO per farsi dare uno sconto data la sua strategia
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
		if (discount == null) throw new DiscountNotFoundException(t);
		else return discount;
	}
	
	
	public String getDiscountStrategyDescription(TypeOfDiscounts t) throws DiscountNotFoundException {
		return getDiscountByStrategy(t).toString();
	}
	
	
	/** METODO per farsi dire la password dell'admin */
	public String getAdminPassword() {
		return adminPassword;
	}
	
	
	/* METODO per cambiare la password dell'admin */
	public void setPassword(String newAdminPassword) throws PasswordTooShortException {
		if (newAdminPassword.length() < 5) {
			throw new PasswordTooShortException();
		}
		adminPassword = newAdminPassword;
	}
	
	
	/** METODO per effettuare il login per l'amministratore
	 * 
	 * @param password   Password inserita
	 * @return			 Esito del login
	 * @throws WrongAdminPasswordException	  Eccezione lanciata qualora si inserisca una
	 * 										  password errata nella fase di login
	 */
	public void login(String password) throws WrongAdminPasswordException {
		if (getAdminPassword().equals(password)) ;
		else throw new WrongAdminPasswordException();
	}
}