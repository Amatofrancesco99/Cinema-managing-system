package cinema.model.reservation;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

import cinema.model.payment.GreatNorthernAccountingAdapter;
import cinema.model.payment.methods.paymentCard.PaymentCard;
import cinema.model.payment.util.PaymentErrorException;
import cinema.model.persistence.util.PersistenceException;
import cinema.model.projection.Projection;
import cinema.model.spectator.Spectator;
import cinema.model.cinema.PhysicalSeat;
import cinema.model.cinema.Room;
import cinema.model.cinema.util.RoomException;
import cinema.model.reservation.discount.IReservationDiscountStrategy;
import cinema.model.reservation.discount.coupon.Coupon;
import cinema.model.reservation.discount.coupon.util.CouponException;
import cinema.model.reservation.discount.types.util.DiscountException;
import cinema.model.reservation.discount.types.util.TypeOfDiscount;
import cinema.model.reservation.util.SeatAvailabilityException;
import cinema.model.reservation.util.ReservationException;

/**
 * Rappresenta la prenotazione effettuata dallo spettatore una volta selezionato
 * il film desiderato e una relativa proiezione.
 * 
 * <p>
 * Gestisce la prenotazione e tutte le sue informazioni, genera un file PDF
 * contenente tutte le informazioni della prenotazione stessa (sala, film, posti
 * riservati...) e lo invia come allegato tramite e-mail da parte del cinema al
 * cliente una volta terminato e convalidato il pagamento.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class Reservation {

	/**
	 * Identificativo della prenotazione.
	 */
	private final long progressive;

	/**
	 * Data di creazione della prenotazione.
	 */
	private LocalDate purchaseDate;

	/**
	 * Spettatore che sta compilando la prenotazione.
	 */
	private Spectator purchaser;

	/**
	 * Posti selezionati associati alla prenotazione.
	 */
	private ArrayList<PhysicalSeat> seats;

	/**
	 * Proiezione per la quale lo spettatore desidera prenotare dei posti.
	 */
	private Projection projection;

	/**
	 * Carta di credito usata per il pagamento.
	 */
	private PaymentCard paymentCard;

	/**
	 * URI che identifica il file contenente la ricevuta di prenotazione una volta
	 * avviato il processo di acquisto.
	 */
	private String reportLocation;

	/**
	 * Coupon utilizzato nella prenotazione (se lo spettatore ne ha a disposizione
	 * uno).
	 */
	private Coupon coupon;

	/**
	 * Numero di persone la cui età è minore o uguale ad una determinata età minima
	 * utilizzata per il calcolo dello sconto se la strategia di sconto applicata
	 * correntemente dal cinema è {@code AGE}.
	 */
	private int numberPeopleUntilMinAge;

	/**
	 * Numero di persone la cui età è maggiore o uguale ad una determinata età
	 * massima utilizzata per il calcolo dello sconto se la strategia di sconto
	 * applicata correntemente dal cinema è {@code AGE}.
	 */
	private int numberPeopleOverMaxAge;

	/**
	 * Strategia di sconto applicata correntemente dal cinema.
	 */
	private IReservationDiscountStrategy rd;

	/**
	 * Costruttore della prenotazione.
	 * 
	 * @param strategy strategia di sconto applicata correntemente dal cinema.
	 * @param id       identificativo della prenotazione.
	 */
	public Reservation(IReservationDiscountStrategy strategy, long id) {
		progressive = id;
		purchaseDate = java.time.LocalDate.now();
		seats = new ArrayList<PhysicalSeat>();
		paymentCard = null;
		reportLocation = null;
		coupon = null;
		numberPeopleUntilMinAge = 0;
		numberPeopleOverMaxAge = 0;
		rd = strategy;
	}

	/**
	 * Aggiunge un posto alla prenotazione.
	 * 
	 * @param row fila del posto da occupare.
	 * @param col posto all'interno della fila {@code row} da occupare.
	 * @throws SeatAvailabilityException se il posto scelto è già stato selezionato
	 *                                   o occupato precedentemente.
	 * @throws RoomException             se il posto selezionato non esiste
	 *                                   all'interno della sala associata alla
	 *                                   prenotazione.
	 */
	public void addSeat(int row, int col) throws SeatAvailabilityException, RoomException {
		boolean findDuplicate = false;
		if (projection.checkIfSeatIsAvailable(row, col)) {
			for (PhysicalSeat s : seats) {
				if (s == projection.getPhysicalSeat(row, col)) {
					findDuplicate = true;
				}
			}
			if (!findDuplicate) {
				seats.add(projection.getPhysicalSeat(row, col));
			} else {
				throw new SeatAvailabilityException(
						"Il posto " + Room.rowIndexToRowLetter(row) + "-" + (col + 1) + " è già stato selezionato.");
			}
		} else
			throw new SeatAvailabilityException(
					"Il posto " + Room.rowIndexToRowLetter(row) + "-" + (col + 1) + " è già occupato.");
	}

	/**
	 * Rimuove un posto dalla prenotazione.
	 * 
	 * @param row fila del posto da rimuovere.
	 * @param col posto all'interno della fila {@code row} da rimuovere.
	 * @throws RoomException se il posto selezionato non esiste all'interno della
	 *                       sala associata alla prenotazione.
	 */
	public void removeSeat(int row, int col) throws RoomException {
		seats.remove(projection.getPhysicalSeat(row, col));
	}

	/**
	 * Restituisce il costo totale della prenotazione, una volta applicato un
	 * eventuale coupon aggiunto a quest'ultima dallo spettatore.
	 * 
	 * @return costo totale della prenotazione una volta applicati eventuali coupon.
	 */
	public double getTotal() {
		double total = rd.getTotal(this);

		// Se alla prenotazione è associato un coupon esistente si sottrae
		// al costo totale il valore del coupon stesso
		if (getCoupon() != null) {
			if (total > getCoupon().getDiscount()) {
				total -= getCoupon().getDiscount();
			} else {
				total = 0.0;
			}
		}
		return Math.round(total * 100.0) / 100.0;
	}

	/**
	 * Restituisce il costo totale della prenotazione senza l'applicazione di alcuno
	 * sconto o coupon.
	 * 
	 * @return il costo totale della prenotazione senza l'applicazione di alcuno
	 *         sconto o coupon.
	 */
	public double getFullPrice() {
		return Math.round(getNSeats() * this.projection.getPrice() * 100.0) / 100.0;
	}

	/**
	 * Aggiunge un coupon alla prenotazione dato il suo identificatore.
	 * 
	 * @param coupon codice del coupon da aggiungere alla prenotazione.
	 * @throws CouponException se il coupon inserito è già stato usato.
	 */
	public void setCoupon(Coupon coupon) throws CouponException {
		if (coupon != null) {
			if (!coupon.isUsed()) {
				this.coupon = coupon;
			} else {
				throw new CouponException("Il coupon " + coupon.getCode() + " è già stato usato.");
			}
		}
	}

	/**
	 * Returns the number of currently occupied seats for the reservation.
	 *
	 * @return the number of currently occupied seats for the reservation.
	 */
	public int getNSeats() {
		return seats.size();
	}

	/**
	 * Consente il pagamento della prenotazione, una volta compilata.
	 *
	 * <p>
	 * All'inizio della procedura i posti vengono segnati come occupati per la
	 * proiezione corrente. Una volta avviato il processo effettivo di pagamento, se
	 * questo non va a buon fine viene reimpostato lo stato precedente dei posti in
	 * modo da non mantenere occupati posti inseriti in prenotazioni non concluse.
	 *
	 * @throws SeatAvailabilityException se il posto richiesto non è disponibile.
	 * @throws NumberFormatException     se si tanta di convertire una stringa con
	 *                                   formato non corretto in un valore numerico.
	 * @throws RoomException             se ci sono errori legati alla gestione
	 *                                   della sala del cinema.
	 * @throws ReservationException      se ci sono errori riscontrati nelle
	 *                                   procedure di interazione con gli oggetti
	 *                                   che rappresentano le prenotazioni.
	 * @throws PaymentErrorException     se ci sono errori riscontrati nella
	 *                                   procedura di pagamento.
	 * @throws PersistenceException      se ci sono errori riscontrati durante l'uso
	 *                                   dei meccanismi di persistenza.
	 */
	public void buy() throws SeatAvailabilityException, NumberFormatException, RoomException, ReservationException,
			PaymentErrorException, PersistenceException {
		takeSeat();
		try {
			pay();
		} catch (ReservationException | PaymentErrorException | PersistenceException exception) {
			freeAllSeats();
			seats.removeAll(seats);
			throw exception;
		}
	}

	/**
	 * Permette di occupare effettivamente i posti selezionati e di gestire le
	 * situazioni di concorrenza tra prenotazioni simultanee.
	 *
	 * <p>
	 * Viene effettuata la prenotazione effettiva dei posti aggiunti alla
	 * prenotazione in modo tale che essi risultino occupati per altri spettatori
	 * concorrenti o futuri. Se un posto non può essere riservato al momento della
	 * chiamata tutti i posti già occupati vengono liberati e viene lanciata
	 * un'eccezione {@code SeatAvailabilityException} per notificare il chiamante
	 * dell'errore riscontrato.
	 *
	 * @throws RoomException             se un posto non può essere considerato
	 *                                   parte della sala della quale dovrebbe fare
	 *                                   parte.
	 * @throws SeatAvailabilityException se un posto richiesto è già stato
	 *                                   effettivamente prenotato da un altro
	 *                                   spettatore concorrente.
	 */
	public void takeSeat() throws RoomException, SeatAvailabilityException {
		for (int i = 0; i < seats.size(); i++) {
			String coordinates = projection.getSeatCoordinates(seats.get(i));
			int row = Room.rowLetterToRowIndex(coordinates.replaceAll("\\d", ""));
			int col = Integer.valueOf(coordinates.replaceAll("[\\D]", "")) - 1;
			if (!projection.takeSeat(row, col)) {
				freeAllSeats();
				seats.removeAll(seats);
				throw new SeatAvailabilityException("Uno dei posti selezionati è già stato occupato.");
			}
		}
	}

	/**
	 * Permette di avviare la procedura di pagamento della prenotazione.
	 *
	 * @throws ReservationException  se la prenotazione non è associata ad alcuna
	 *                               carta di credito, oppure non è stato
	 *                               selezionato alcun posto.
	 * @throws PaymentErrorException se il pagamento non è andato a buon fine.
	 * @throws PersistenceException  se ci sono errori riscontrati durante l'uso dei
	 *                               meccanismi di persistenza.
	 */
	public void pay() throws ReservationException, PaymentErrorException, PersistenceException {
		if (getNSeats() > 0) {
			if (paymentCard == null) {
				throw new ReservationException("La prenotazione non ha associata nessuna carta di credito.");
			} else {
				// Payment simulation
				if (new GreatNorthernAccountingAdapter(paymentCard).pay(getTotal()) == false) {
					throw new PaymentErrorException("Il pagamento non è andato a buon fine.");
				}
			}
		} else
			throw new ReservationException("Nessun posto è stato selezionato.");
	}

	/**
	 * Libera tutti i posti della prenotazione.
	 * 
	 * @throws RoomException se un posto non può essere considerato parte della sala
	 *                       della quale dovrebbe fare parte.
	 */
	public void freeAllSeats() throws RoomException {
		for (int i = 0; i < seats.size(); i++) {
			String coordinates = projection.getSeatCoordinates(seats.get(i));
			int row = Room.rowLetterToRowIndex(coordinates.replaceAll("\\d", ""));
			int col = Integer.valueOf(coordinates.replaceAll("[\\D]", "")) - 1;
			if (projection.checkIfSeatIsAvailable(row, col))
				projection.freeSeat(row, col);
		}
	}

	/**
	 * Imposta il numero di persone che hanno un'età minore o uguale all'età minima.
	 *
	 * @param n numero di persone di età minore o uguale all'età minima per lo
	 *          sconto di tipo {@code AGE}.
	 * @throws DiscountException se il numero di persone da impostare non è valido.
	 */
	public void setNumberPeopleUnderMinAge(int n) throws DiscountException {
		if (n < 0)
			throw new DiscountException("Il numero di persone non può essere negativo.");
		if (n + getNumberPeopleOverMaxAge() > this.getNSeats())
			throw new DiscountException(
					"Il numero di persone sotto l'età minima supera il massimo consentito dai posti correntemente selezionati.");
		numberPeopleUntilMinAge = n;
	}

	/**
	 * Imposta il numero di persone che hanno un'età maggiore o uguale all'età
	 * massima.
	 *
	 * @param n numero di persone di età maggiore o uguale all'età massima per lo
	 *          sconto di tipo {@code AGE}.
	 * @throws DiscountException se il numero di persone da impostare non è valido.
	 */
	public void setNumberPeopleOverMaxAge(int n) throws DiscountException {
		if (n < 0)
			throw new DiscountException("Il numero di persone non può essere negativo.");
		if (n + getNumberPeopleUntilMinAge() > this.getNSeats())
			throw new DiscountException(
					"Il numero di persone sotto l'età massima supera il massimo consentito dai posti correntemente selezionati.");
		numberPeopleOverMaxAge = n;
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public int getNumberPeopleUntilMinAge() {
		return numberPeopleUntilMinAge;
	}

	public int getNumberPeopleOverMaxAge() {
		return numberPeopleOverMaxAge;
	}

	public void setReportLocation(String path) {
		reportLocation = path;
	}

	public String getReportLocation() {
		return reportLocation;
	}

	public long getProgressive() {
		return progressive;
	}

	public Spectator getPurchaser() {
		return purchaser;
	}

	public Projection getProjection() {
		return projection;
	}

	public LocalDate getDate() {
		return purchaseDate;
	}

	public ArrayList<PhysicalSeat> getSeats() {
		return seats;
	}

	public IReservationDiscountStrategy getStrategy() {
		return rd;
	}

	public PaymentCard getPaymentCard() {
		return paymentCard;
	}

	public void setProjection(Projection projection) {
		this.projection = projection;
	}

	public void setPurchaser(Spectator spectator) {
		this.purchaser = spectator;
	}

	public void setPaymentCard(String number, String owner, String cvv, YearMonth expirationDate) {
		this.paymentCard = new PaymentCard(number, owner, cvv, expirationDate);
	}

	public TypeOfDiscount getTypeOfDiscount() {
		return rd.getTypeOfDiscount();
	}

	public int getDiscountId() {
		return rd.getDiscountId();
	}

}
