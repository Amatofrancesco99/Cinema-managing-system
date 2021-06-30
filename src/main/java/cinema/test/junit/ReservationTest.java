package cinema.test.junit;

import static org.junit.Assert.*;  

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import cinema.controller.Cinema;
import cinema.model.Movie;
import cinema.model.spectator.Spectator;
import cinema.model.spectator.util.InvalidSpectatorInfoException;
import cinema.model.cinema.Room;
import cinema.model.cinema.util.RoomException;
import cinema.model.payment.util.PaymentErrorException;
import cinema.model.projection.Projection;
import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.coupon.util.CouponException;
import cinema.model.reservation.discount.types.util.DiscountException;
import cinema.model.reservation.discount.types.util.TypeOfDiscounts;
import cinema.model.reservation.handlers.ReportHandler;
import cinema.model.reservation.handlers.util.HandlerException;
import cinema.model.reservation.util.SeatAvailabilityException;
import cinema.model.reservation.util.ReservationException;


/** BREVE DESCRIZIONE CLASSE ReservationTest
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Questa classe nasce con lo scopo specifico di effettuare il test di unità (tramite JUnit),
 * ossia test effettuati su una classe specifica e dei suoi metodi, provando differenti
 * input e verificando gli output in maniera automatica (confrontando i risultati attesi
 * con quelli ottenuti), sulla classe Reservation, presente all'interno del model package.
 * Anche se l'implementazione reale prevede l'utilizzo di dati presenti su un DB, per
 * sfruttare il principio di persistenza dei dati (ad esempio dei Film, delle Sale, delle
 * Proiezioni, ecc...) sono utilizzati oggetti istanza "finti" (mock), ossia oggetti
 * che hanno la stessa interfaccia di oggetti esterni realmente utilizzati e che simulano le
 * loro funzionalità. 
 * Questo per evitare di dover effettuare attività preliminari di inserimento dati 
 * all'interno del DB, il che potrebbe comportare eventuali problematiche (perdita dei dati), 
 * un evento non molto gradito e una perdita di tempo, visto che i dati inseriti non sarebbero
 * quelli veri, ma quelli ottenuti di fronte ad errori di inserimento da parte di utenti
 * non particolarmente attenti/istruiti.
 * Chiaramente potrebbe essere utile fare sessioni di "istruzioni" a chi vendiamo il 
 * software, per poter fare in modo tale che inseriscano valori corretti all'interno del DB.
 * 
 */
public class ReservationTest {

	private static Reservation r;
	private static Cinema myCinema = new Cinema();
	private static ArrayList<Projection> projections;
	
	/** 
	 * METODO per poter effettuare l'impostazione del nostro sistema, creando gli input
	 * e gli output previsti.
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		myCinema.setCinemaDiscountStrategy(TypeOfDiscounts.AGE);
		r = myCinema.getReservation(myCinema.createReservation());
		Room room = new Room(3,3);
		
		ArrayList<String> genres, directors, cast;
		genres = new ArrayList<>();
		directors = new ArrayList<>();
		cast = new ArrayList<>();
		
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
		Movie AvengersEndgameMovie = new Movie(2, "Avengers - Endgame",
				"Tutto ovviamente parte dalle vicende di \"Avengers: Infinity War\". Thanos ha distrutto mezzo Universo grazie alle Gemme dell’Infinito (sei pietre e ognuna dona un particolare tipo di potere). La ricerca e la protezione di queste pietre sono state alle base degli altri film, ma ora Thanos le ha tutte ed è praticamente onnipotente. Le gemme dello Spazio, della Mente, del Potere, della Realtà, del Tempo e dell’Anima gli hanno permesso di raggiungere il suo scopo: distruggere l’universo. Tra i sopravvissuti al progetto diabolico del cattivo di turno, ci sono gli Avengers della Fase 1 (Capitan America, Thor, Vedova Nera, Occhio di Falco, Hulk e Iron Man) insieme ad Ant-Man e Captain Marvel. Lo scopo è quello ovviamente di sconfiggere Thanos e di far tornare in vita tutti quelli che non ci sono più come Spider-Man, Black Panther, Doctor Strange, Falcon, Scarlet Witch, Star-Lord, Drax, Groot, Mantis, Bucky Barnes, Nick Fury, Maria Hill, Loki, Visione e Gamora. Un mix di azione, comicità e riflessioni sul genere umano perché il film mostra il lato fragile e vulnerabile presente sia nei buoni sia nei cattivi.  Oltre ad un Robert Downey Jr in stato di grazia, spicca l’interpretazione di Chris Hemsworth che nonostante il cambiamento totale di look riesce ad essere credibile nei momenti comici e in quelli drammatici. ",
				genres, directors, cast, 5, 182,
				"https://images-na.ssl-images-amazon.com/images/I/71HyTegC0SL._AC_SY879_.jpg",
				"https://www.youtube.com/watch?v=vqWz0ZCpYBs");
		
		projections = new ArrayList<Projection>();
		projections.add(new Projection(109, AvengersEndgameMovie, LocalDateTime.parse("2021-06-02T22:30:00"), 12.5,	room));
		r.setProjection(projections.get(0));
		
		r.addSeat(0, 0);
		r.addSeat(0, 1);
		r.addSeat(0, 2);
	}

	
	/** Test sull'assegnamento progressivo di una prenotazione */
	@Test
	public void testProgressiveAssignment() throws ReservationException {
		final int STOP = 3;
		for (int i = 1; i < STOP; i++) {
			myCinema.createReservation();
		}
		assertEquals(STOP, myCinema.getReservation(STOP).getProgressive());
	}
	
	
	/** Test data di creazione nuova prenotazione */
	@Test
	public void testPurchaseDate() throws ReservationException {
		assertEquals(LocalDate.now(),myCinema.getReservation(myCinema.createReservation()).getDate());
	}
	
	
	/** Test di occupazione posti, qualora ce ne fossero alcuni già occupati da qualcun altro */
	@Test
	public void testAlreadyTakenSeat() {
		int error = 0;
		try {
			r.getProjection().takeSeat(0, 0);
			r.takeSeat();
		} catch (RoomException | SeatAvailabilityException e) {
			error = 1;
		}
		assertEquals(1, error);
		
		// Libero il posto usato per test
		try {
			r.getProjection().freeSeat(0, 0);
		} catch (RoomException e) {
		}
	}
	
	
	/** Test di occupazione dei posti (scegliere un posto non presente in sala) */
	@Test
	public void testRoomSeatNotExists() {
		int error = 0;
		int nCols = r.getProjection().getRoom().getNumberCols();
		int nRows = r.getProjection().getRoom().getNumberRows();
		try {
			r.addSeat(nRows + 2 , nCols + 8);
		} catch (RoomException | SeatAvailabilityException e) {
			error ++;
		}
		assertEquals(1,error);
	}
	
	
	/** Test sui coupon */
	@Test
	public void testCoupon() {
		// Coupon utilizzato una sola volta
		long c1 = myCinema.createCoupon(2);
		try {
			r.setCoupon(myCinema.getCoupon(c1));
			assertEquals(23 , r.getFullPrice() - myCinema.getCoupon(c1).getDiscount(),0);
		} catch (CouponException e) {
			e.toString();
		}
		try {
			r.setPaymentCard("1234567890123456", "TestOwnerName", "123", YearMonth.of(2022, 01));
			r.buy();
		} catch (NumberFormatException | SeatAvailabilityException | RoomException
				| ReservationException | PaymentErrorException e) {
			e.toString();
		}
		
		// Cerco di riutilizzare lo stesso coupon in due reservation diverse
		try {
			Reservation newReservation = myCinema.getReservation(myCinema.createReservation());
			newReservation.setProjection(projections.get(0));
			newReservation.addSeat(2, 2); // occupo un nuovo posto
			newReservation.setCoupon(myCinema.getCoupon(c1));
			assertEquals(12.50, newReservation.getTotal(),0);
		} catch (ReservationException | CouponException | SeatAvailabilityException | RoomException e) {
			e.toString();
		}
		
	}
	
	
	/** Test sui prezzi, usando lo sconto per età */
	@Test
	public void testPrices() throws CouponException {
		assertEquals(12.50*2, r.getFullPrice(),0);
		// uso lo sconto per età
		try {
			r.setNumberPeopleUnderMinAge(1);
			r.setNumberPeopleOverMaxAge(0);
		} catch (DiscountException e) {}
		assertEquals(12.50*2 - 1.87 - r.getCoupon().getDiscount(),r.getTotal(),0);
	}
	
	
	/** Test qualora si inserisca un numero di persone che hanno un età inferiore ad un
	* età minima da cui partono gli sconti superiore al numero di persone della prenotazione
	* stessa */
	@Test 
	public void testOnOtherSpectatorInfo() {
		int error = 1;
		try {
			r.setNumberPeopleOverMaxAge(10);
			r.setNumberPeopleUnderMinAge(-2);
		} catch (DiscountException e) {
			error ++;
		}
		assertEquals(2,error);
	}
	
	
	/** Test di creazione del report */
	public void testCreateReport() {
		try {
			ReportHandler.createReport(r);
		} catch (HandlerException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	/** Test invio email 
	 * @throws InvalidSpectatorInfoException */
	@Test
	public void testSendEmail() throws InvalidSpectatorInfoException {
		// cambia i campi qui sotto, specialmente l'email, per poter testare l'invio
		// del report contenente tutte le informazioni sulla prenotazione alla tua casella
		// di posta personale
		r.setPurchaser(new Spectator("Francesco", "Amato" , "francesco.amato01@universitadipavia.it"));
		try {
			r.sendEmail();
		} catch (HandlerException e) {
			System.out.println(e.getMessage());
		}
	}
}