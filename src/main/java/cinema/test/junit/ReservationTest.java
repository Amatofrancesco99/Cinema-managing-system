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
 * Effettua il test di unità (tramite JUnit) sulla classe Reservation.
 *
 * Se necessario, reimpostare il database
 * ({@code git restore persistence/cinemaDb.db}) prima di avviare il test.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class ReservationTest {

	/**
	 * Prenotazione utilizzata nel test.
	 */
	private static Reservation r;

	/**
	 * Controller di dominio utilizzato come interfaccia verso il modello.
	 */
	private static Cinema cinema = new Cinema();

	/**
	 * Proiezioni create per poter effettuare il test.
	 */
	private static ArrayList<Projection> projections;

	/**
	 * Impostazione del test, creando dati utili per poter effettuare i test stessi.
	 * 
	 * @throws Exception se vengono riscontrati errori nella creazione dei dati
	 *                   necessari allo svolgimento dei test.
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

	/**
	 * Test sull'assegnazione del progressivo di una prenotazione.
	 *
	 * @throws ReservationException se vengono riscontrati errori nella gestione
	 *                              della prenotazione.
	 */
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
			assertEquals(cinema.getPersistenceFacade().getLastReservationId() + STOP,
					cinema.getReservation(cinema.getPersistenceFacade().getLastReservationId()).getProgressive()
							+ STOP);
		} catch (PersistenceException | SQLException | ReservationException exception) {
			System.out.println(exception.getMessage());
		}
	}

	/**
	 * Test sulla data di creazione di una nuova prenotazione.
	 * 
	 * @throws ReservationException se si riscontrano errori nel gestire i dati
	 *                              della prenotazione.
	 * @throws PersistenceException se vengono riscontrati errori nell'interazione
	 *                              con il modulo di persistenza.
	 */
	@Test
	public void testPurchaseDate() throws ReservationException, PersistenceException {
		assertEquals(LocalDate.now(), cinema.getReservation(cinema.createReservation()).getDate());
	}

	/**
	 * Test di occupazione posti (verifica il comportamento se ci sono alcuni posti
	 * già occupati da altre prenotazioni).
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
		// Viene liberato il posto usato per il test tornando poi alla situazione
		// iniziale
		try {
			r.getProjection().freeSeat(0, 0);
			r.addSeat(0, 1);
			r.addSeat(0, 2);
		} catch (RoomException | SeatAvailabilityException e) {
			// Nessuna eccezione da gestire qui
		}
	}

	/**
	 * Test dei coupon (verifica il vincolo che essi siano monouso).
	 */
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
			System.out.println(e.getMessage());
		}
		try {
			r.setPaymentCard("1234567890123456", "TestOwnerName", "123", YearMonth.of(2022, 01));
			r.setPurchaser(new Spectator("Lorenzo", "Verdi", "prova"));
			cinema.buyReservation(r.getProgressive());
		} catch (NumberFormatException | SeatAvailabilityException | RoomException | ReservationException
				| PaymentErrorException | PersistenceException | InvalidSpectatorInfoException e) {
			System.out.println(e.getMessage());
		}
		// Viene tentato di riutilizzare lo stesso coupon in due prenotazioni diverse
		try {
			Reservation newReservation = cinema.getReservation(cinema.createReservation());
			newReservation.setProjection(projections.get(0));
			newReservation.addSeat(2, 2); // occupo un nuovo posto
			cinema.setReservationCoupon(newReservation.getProgressive(), c1.getCode());
			assertEquals(12.50, newReservation.getTotal(), 0);
		} catch (ReservationException | CouponException | SeatAvailabilityException | RoomException
				| PersistenceException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Test sui prezzi usando lo sconto di tipo {@code AGE}.
	 *
	 * @throws DiscountNotFoundException se lo sconto da applicare non è valido.
	 * @throws PersistenceException      se il servizio di persistenza riscontra
	 *                                   errori.
	 */
	@Test
	public void testPrices() throws DiscountNotFoundException, PersistenceException {
		assertEquals(projections.get(0).getPrice() * 2, r.getFullPrice(), 1);
		// Viene usato lo sconto per età
		try {
			r.setNumberPeopleUnderMinAge(1);
			r.setNumberPeopleOverMaxAge(0);
		} catch (DiscountException exception) {
		}
		assertEquals(projections.get(0).getPrice() * 2 - 0.15 * projections.get(0).getPrice(), r.getTotal(), 1);
	}

	/**
	 * Test di occupazione dei posti (viene tentato di selezionare un posto non
	 * presente in sala).
	 */
	@Test
	public void testRoomSeatNotExists() {
		int error = 0;
		int nCols = r.getProjection().getRoom().getNumberOfCols();
		int nRows = r.getProjection().getRoom().getNumberOfRows();
		try {
			r.addSeat(nRows + 2, nCols + 8);
		} catch (RoomException | SeatAvailabilityException exception) {
			error++;
		}
		assertEquals(1, error);
	}

	/**
	 * Test sui vincoli numerici del numero di persone sotto/sopra l'età
	 * minima/massima quando è attivo lo sconto di tipo {@code AGE}.
	 */
	@Test
	public void testOnOtherSpectatorInfo() {
		int error = 0;
		try {
			r.setNumberPeopleOverMaxAge(10);
		} catch (DiscountException exception) {
			error++;
		}
		assertEquals(1, error);
	}

	/**
	 * Test dell'invio di e-mail allo spettatore.
	 * 
	 * @throws InvalidSpectatorInfoException se le informazioni relative allo
	 *                                       spettatore non sono valide.
	 */
	@Test
	public void testSendEmail() throws InvalidSpectatorInfoException {
		try {
			// Modificare i dati seguenti per testare il corretto invio dell'e-mail
			r.setPurchaser(new Spectator("Francesco", "Amato", "francesco.amato01@universitadipavia.it"));
			Thread emailThread = cinema.sendReservationEmail(r.getProgressive());
			emailThread.join();
		} catch (HandlerException | InterruptedException | ReservationException exception) {
			System.out.println(exception.getMessage());
		}
	}

}
