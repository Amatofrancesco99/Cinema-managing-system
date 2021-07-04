package cinema.test.junit;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import cinema.controller.Cinema;
import cinema.controller.handlers.util.HandlerException;
import cinema.model.Movie;
import cinema.model.spectator.Spectator;
import cinema.model.spectator.util.InvalidSpectatorInfoException;
import cinema.model.cinema.Room;
import cinema.model.cinema.util.RoomException;
import cinema.model.payment.util.PaymentErrorException;
import cinema.model.persistence.util.PersistenceException;
import cinema.model.projection.Projection;
import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.coupon.Coupon;
import cinema.model.reservation.discount.coupon.util.CouponException;
import cinema.model.reservation.discount.types.util.DiscountException;
import cinema.model.reservation.discount.types.util.TypeOfDiscounts;
import cinema.model.reservation.util.SeatAvailabilityException;
import cinema.model.reservation.util.ReservationException;

/**
 * Effettua il test di unità (tramite JUnit), sulla classe Reservation presente
 * all'interno del model package.
 * 
 * @author Screaming Hairy Armadillo Team
 * 
 */
public class ReservationTest {

	private static Reservation r;
	private static Cinema myCinema = new Cinema();
	private static ArrayList<Projection> projections;

	/**
	 * METODO per poter effettuare l'impostazione del nostro sistema, creando gli
	 * input e gli output previsti.
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		myCinema.setCinemaDiscountStrategy(TypeOfDiscounts.AGE);
		r = myCinema.getReservation(myCinema.createReservation());
		Room room = new Room(999, 3, 3);

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
		projections
				.add(new Projection(109, AvengersEndgameMovie, LocalDateTime.parse("2021-06-02T22:30:00"), 12.5, room));
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
			try {
				myCinema.createReservation();
			} catch (PersistenceException e) {
				System.out.println(e.getMessage());
			}
		}
		assertEquals(STOP, myCinema.getReservation(STOP).getProgressive());
	}

	/**
	 * Test data di creazione nuova prenotazione
	 * 
	 * @throws PersistenceException
	 */
	@Test
	public void testPurchaseDate() throws ReservationException, PersistenceException {
		assertEquals(LocalDate.now(), myCinema.getReservation(myCinema.createReservation()).getDate());
	}

	/**
	 * Test di occupazione posti, qualora ce ne fossero alcuni già occupati da
	 * qualcun altro
	 */
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
		// Libero il posto usato per test e ritorno alla situazione di prima (rioccupo i
		// posti)
		try {
			r.getProjection().freeSeat(0, 0);
			r.addSeat(0, 1);
			r.addSeat(0, 2);
		} catch (RoomException | SeatAvailabilityException e) {
		}
	}

	/** Test sui coupon */
	@Test
	public void testCoupon() {
		Coupon c1 = null;
		// Coupon utilizzato una sola volta
		try {
			c1 = new Coupon("PROVA123", 3.5, false);
		} catch (CouponException e1) {
		}
		try {
			myCinema.setReservationCoupon(r.getProgressive(), c1.getCode());
			r.setCoupon(c1);
			assertEquals(21.5, r.getTotal(), 0);
		} catch (CouponException | ReservationException e) {
			e.toString();
		}
		try {
			r.setPaymentCard("1234567890123456", "TestOwnerName", "123", YearMonth.of(2022, 01));
			myCinema.buyReservation(r.getProgressive());
		} catch (NumberFormatException | SeatAvailabilityException | RoomException | ReservationException
				| PaymentErrorException | PersistenceException e) {
			e.toString();
		}
		// Cerco di riutilizzare lo stesso coupon in due reservation diverse
		try {
			Reservation newReservation = myCinema.getReservation(myCinema.createReservation());
			newReservation.setProjection(projections.get(0));
			newReservation.addSeat(2, 2); // occupo un nuovo posto
			myCinema.setReservationCoupon(newReservation.getProgressive(), c1.getCode());
			assertEquals(12.50, newReservation.getTotal(), 0);
		} catch (ReservationException | CouponException | SeatAvailabilityException | RoomException
				| PersistenceException e) {
			e.toString();
		}
	}

	/** Test sui prezzi, usando lo sconto per età */
	@Test
	public void testPrices() throws CouponException {
		assertEquals(12.50 * 2, r.getFullPrice(), 0);
		// uso lo sconto per età
		try {
			r.setNumberPeopleUnderMinAge(1);
			r.setNumberPeopleOverMaxAge(0);
		} catch (DiscountException e) {
		}
		assertEquals(12.50 * 2 - 1.87, r.getTotal(), 0);
	}

	/** Test di occupazione dei posti (scegliere un posto non presente in sala) */
	@Test
	public void testRoomSeatNotExists() {
		int error = 0;
		int nCols = r.getProjection().getRoom().getNumberCols();
		int nRows = r.getProjection().getRoom().getNumberRows();
		try {
			r.addSeat(nRows + 2, nCols + 8);
		} catch (RoomException | SeatAvailabilityException e) {
			error++;
		}
		assertEquals(1, error);
	}

	/**
	 * Test qualora si inserisca un numero di persone che hanno un età inferiore ad
	 * un età minima da cui partono gli sconti superiore al numero di persone della
	 * prenotazione stessa
	 */
	@Test
	public void testOnOtherSpectatorInfo() {
		int error = 0;
		try {
			r.setNumberPeopleOverMaxAge(10);
		} catch (DiscountException e) {
			error++;
		}
		assertEquals(1, error);
	}

	/**
	 * Test invio email
	 * 
	 * @throws InvalidSpectatorInfoException
	 */
	@Test
	public void testSendEmail() throws InvalidSpectatorInfoException {
		// cambia i campi qui sotto, specialmente l'email, per poter testare l'invio
		// del report contenente tutte le informazioni sulla prenotazione alla tua
		// casella
		// di posta personale
		Reservation r2 = null;
		try {
			r2 = myCinema.getReservation(myCinema.createReservation());
			r2.setProjection(projections.get(0));
			r2.addSeat(1, 1);
			r2.addSeat(1, 2);
			r2.setPaymentCard("1234567890123456", "Francesco Amato", "212", YearMonth.of(2024, 02));
			r2.setPurchaser(new Spectator("Francesco", "Amato", "francesco.amato01@universitadipavia.it"));
		} catch (ReservationException | SeatAvailabilityException | RoomException | PersistenceException exception) {
			System.out.println(exception.getMessage());
		}
		try {
			/*
			 * notare ci sono tre posti nell'email inviata (ogni classe di test chiama
			 * setUpBeforeClass(), prima di iniziare)
			 */
			myCinema.sendReservationEmail(r2.getProgressive());
		} catch (HandlerException | ReservationException e) {
			System.out.println(e.getMessage());
		}
	}
}