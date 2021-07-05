package cinema.test.junit;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import cinema.controller.Cinema;
import cinema.controller.handlers.util.HandlerException;
import cinema.controller.util.DiscountNotFoundException;
import cinema.model.spectator.Spectator;
import cinema.model.spectator.util.InvalidSpectatorInfoException;
import cinema.model.cinema.util.RoomException;
import cinema.model.payment.util.PaymentErrorException;
import cinema.model.persistence.PersistenceFacade;
import cinema.model.persistence.util.PersistenceException;
import cinema.model.projection.Projection;
import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.coupon.Coupon;
import cinema.model.reservation.discount.coupon.util.CouponException;
import cinema.model.reservation.discount.types.util.DiscountException;
import cinema.model.reservation.discount.types.util.TypeOfDiscount;
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

	/**
	 * Reservation utilizzata nel test
	 */
	private static Reservation r;

	/**
	 * Controller di dominio utilizzato come interfaccia verso il modello.
	 */
	private static Cinema cinema = new Cinema();

	/**
	 * Proiezioni create per poter effettuare il test
	 */
	private static ArrayList<Projection> projections;

	/**
	 * Impostazione del sistema, creando dati utili per poter effettuare i diversi
	 * test.
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		cinema.setCinemaDiscountStrategy(TypeOfDiscount.AGE);
		r = cinema.getReservation(cinema.createReservation());
		projections = new ArrayList<Projection>();
		projections.add(cinema.getProjection(2));
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
				cinema.createReservation();
			} catch (PersistenceException exception) {
				System.out.println(exception.getMessage());
			}
		}
		try {
			assertEquals(PersistenceFacade.getInstance().getLastReservationId() + STOP,
					cinema.getReservation(PersistenceFacade.getInstance().getLastReservationId()).getProgressive()
							+ STOP);
		} catch (PersistenceException | SQLException | ReservationException exception) {
			System.out.println(exception.getMessage());
		}
	}

	/**
	 * Test data di creazione nuova prenotazione
	 * 
	 * @throws PersistenceException
	 */
	@Test
	public void testPurchaseDate() throws ReservationException, PersistenceException {
		assertEquals(LocalDate.now(), cinema.getReservation(cinema.createReservation()).getDate());
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

	/** Test sui coupon (verifica monouso) */
	@Test
	public void testCoupon() {
		Coupon c1 = null;
		try {
			c1 = new Coupon("PROVA123", 3.5, false);
		} catch (CouponException e1) {
		}
		try {
			cinema.setReservationCoupon(r.getProgressive(), c1.getCode());
			r.setCoupon(c1);
			assertEquals(21.5, r.getTotal(), 0);
		} catch (CouponException | ReservationException e) {
			e.toString();
		}
		try {
			r.setPaymentCard("1234567890123456", "TestOwnerName", "123", YearMonth.of(2022, 01));
			r.setPurchaser(new Spectator("Lorenzo", "Verdi", "prova"));
			cinema.buyReservation(r.getProgressive());
		} catch (NumberFormatException | SeatAvailabilityException | RoomException | ReservationException
				| PaymentErrorException | PersistenceException | InvalidSpectatorInfoException e) {
			e.toString();
		}
		// Cerco di riutilizzare lo stesso coupon in due reservation diverse
		try {
			Reservation newReservation = cinema.getReservation(cinema.createReservation());
			newReservation.setProjection(projections.get(0));
			newReservation.addSeat(2, 2); // occupo un nuovo posto
			cinema.setReservationCoupon(newReservation.getProgressive(), c1.getCode());
			assertEquals(12.50, newReservation.getTotal(), 0);
		} catch (ReservationException | CouponException | SeatAvailabilityException | RoomException
				| PersistenceException e) {
			e.toString();
		}
	}

	/**
	 * Test sui prezzi, usando lo sconto per età
	 * 
	 * @throws PersistenceException      qualora il servizio al database non sia
	 *                                   raggiungibile
	 * @throws DiscountNotFoundException qualora lo sconto che si vuole applicare
	 *                                   non esiste
	 */
	@Test
	public void testPrices() throws CouponException, DiscountNotFoundException, PersistenceException {
		cinema.setCinemaDiscountStrategy(TypeOfDiscount.AGE);
		assertEquals(projections.get(0).getPrice() * 2, r.getFullPrice(), 1);
		// uso lo sconto per età
		try {
			r.setNumberPeopleUnderMinAge(1);
			r.setNumberPeopleOverMaxAge(0);
		} catch (DiscountException e) {
		}
		assertEquals(projections.get(0).getPrice() * 2 - 0.15 * projections.get(0).getPrice(), r.getTotal(), 1);
	}

	/** Test di occupazione dei posti (scegliere un posto non presente in sala) */
	@Test
	public void testRoomSeatNotExists() {
		int error = 0;
		int nCols = r.getProjection().getRoom().getNumberOfCols();
		int nRows = r.getProjection().getRoom().getNumberOfRows();
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
		try {
			r.setPurchaser(new Spectator("Francesco", "Amato", "francesco.amato01@universitadipavia.it"));
			Thread emailThread = cinema.sendReservationEmail(r.getProgressive());
			emailThread.join();
		} catch (HandlerException | InterruptedException | ReservationException exception) {
			System.out.println(exception.getMessage());
		}
	}
}
