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
 * Rappresenta la prenotazione effettuata una volta selezionato il film
 * desiderato e l'orario.
 * 
 * Crea la prenotazione e tutte le sue informazioni, genera un file .pdf
 * contenente tutte le informazioni della prenotazione stessa (sala, film, posti
 * riservati, ecc...) e lo invia come allegato tramite email, da parte del
 * cinema al cliente. Infine permette di pagare tramite il metodo di pagamento
 * selezionato dall'utente.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class Reservation {

	/**
	 * Numero di prenotazione.
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
	 * Posti selezionati/occupati.
	 */
	private ArrayList<PhysicalSeat> seats;
	/**
	 * Proiezione che si desidera visionare.
	 */
	private Projection projection;
	/**
	 * Metodo di pagamento.
	 */
	private PaymentCard paymentCard;
	/**
	 * Area di memoria in cui salviamo il report della prenotazione.
	 */
	private String reportLocation;
	/**
	 * Coupon utilizzabile nella prenotazione.
	 */
	private Coupon coupon;
	/**
	 * Numero di persone la cui un'età è inferiore ad una determinata età minima.
	 */
	private int numberPeopleUntilMinAge;
	/**
	 * Numero di persone la cui un'età è superiore ad una determinata età massima.
	 */
	private int numberPeopleOverMaxAge;
	/**
	 * Strategia di sconto applicata.
	 */
	private IReservationDiscountStrategy rd;

	/**
	 * Costruttore della prenotazione.
	 * 
	 * @param rd strategia di sconto applicata.
	 * @param id numero di prenotazione.
	 */
	public Reservation(IReservationDiscountStrategy rd, long id) {
		progressive = id;
		purchaseDate = java.time.LocalDate.now();
		seats = new ArrayList<PhysicalSeat>();
		paymentCard = null;
		reportLocation = null;
		coupon = null;
		numberPeopleUntilMinAge = 0;
		numberPeopleOverMaxAge = 0;
		this.rd = rd;
	}

	/**
	 * Aggiunge un posto alla reservation.
	 * 
	 * @param row coordinata riga del posto.
	 * @param col coordinata colonna del posto.
	 * @throws SeatAvailabilityException eccezione lanciata qualora il posto scelto
	 *                                   sia già stato selezionato,o occupato.
	 * @throws RoomException             eccezione lanciata qualora vi siano errori
	 *                                   legati alla gestione della sala del cinema.
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
	 * Rimuove un posto dalla reservation.
	 * 
	 * @param row coordinata riga del posto.
	 * @param col coordinata colonna del posto.
	 * @throws RoomException eccezione lanciata qualora vi siano errori legati alla
	 *                       gestione della sala del cinema.
	 */
	public void removeSeat(int row, int col) throws RoomException {
		seats.remove(projection.getPhysicalSeat(row, col));
	}

	/**
	 * Restituisce il totale della reservation, a fronte di eventuali sconti
	 * effettuati su quest'ultima.
	 * 
	 * @return ammontare di denaro da pagare.
	 * 
	 */
	public double getTotal() {
		double total = rd.getTotal(this);
		// Qualora alla prenotazione sia associato un coupon esistente vado a detrarre
		// il totale dell'importo di sconto di questo coupon
		if (getCoupon() != null) {
			if (total > getCoupon().getDiscount()) {
				total -= getCoupon().getDiscount();
			} else {
				total = 0.0;
			}
		}
		return Math.round(total * 100.0) / 100.0;
	}

	public double getFullPrice() {
		return Math.round(getNSeats() * this.projection.getPrice() * 100.0) / 100.0;
	}

	/**
	 * Aggiunge un coupon dato il suo progressivo.
	 * 
	 * @param coupon coupon
	 * @throws CouponException eccezione lanciata qualora il coupon inserito sia gia
	 *                         stato usato.
	 */
	public void setCoupon(Coupon coupon) throws CouponException {
		if (coupon == null)
			;
		else {
			if (!coupon.isUsed())
				this.coupon = coupon;
			else
				throw new CouponException("Il coupon " + coupon.getCode() + " è già stato usato.");
		}
	}

	public int getNSeats() {
		return seats.size();
	}

	/**
	 * Consente il pagamento della prenotazione, una volta compilata la
	 * prenotazione.
	 * 
	 * @throws SeatAvailabilityException eccezione lanciata qualora il posto
	 *                                   richiesto non sia disponibile.
	 * @throws NumberFormatException     eccezione lanciata qualora si tenti di
	 *                                   convertire una stringa con formato non
	 *                                   corretto in un valore numerico.
	 * @throws RoomException             eccezione lanciata qualora vi siano errori
	 *                                   legati alla gestione della sala del cinema.
	 * @throws ReservationException      eccezione lanciata qualora vi siano errori
	 *                                   riscontrati nelle procedure di interazione
	 *                                   con gli oggetti che rappresentano le
	 *                                   prenotazioni.
	 * @throws PaymentErrorException     eccezione lanciata qualora vi siano errori
	 *                                   riscontrati nelle procedure di pagamento.
	 * @throws PersistenceException      eccezione lanciata qualora vi siano errori
	 *                                   riscontrati durante l'uso di meccanismi di
	 *                                   persistenza.
	 */
	public void buy() throws SeatAvailabilityException, NumberFormatException, RoomException, ReservationException,
			PaymentErrorException, PersistenceException {
		takeSeat();
		try {
			pay();
		} catch (ReservationException | PaymentErrorException | PersistenceException e) {
			freeAllSeats();
			seats.removeAll(seats);
			throw e;
		}
	}

	/**
	 * Permette di occupare i posti selezionati e di gestire le situazioni di
	 * concorrenza.
	 * 
	 * @throws RoomException             eccezione lanciata qualora vi siano errori
	 *                                   legati alla gestione della sala del cinema.
	 * @throws SeatAvailabilityException eccezione lanciata qualora il posto
	 *                                   richiesto sia già satto occupato.
	 */
	public void takeSeat() throws RoomException, SeatAvailabilityException {
		for (int i = 0; i < seats.size(); i++) {
			String coordinates = projection.getSeatCoordinates(seats.get(i));
			int row = Room.rowLetterToRowIndex(coordinates.replaceAll("\\d", ""));
			int col = Integer.valueOf(coordinates.replaceAll("[\\D]", "")) - 1;
			if (projection.takeSeat(row, col))
				;
			else {
				freeAllSeats();
				seats.removeAll(seats);
				throw new SeatAvailabilityException("Uno dei posti selezionati è già stato occupato.");
			}
		}
	}

	/**
	 * Permette di pagare la prenotazione.
	 * 
	 * @throws ReservationException  eccezione lanciata qualora la prenotazione non
	 *                               sia associata ad alcuna carta di credito,
	 *                               oppure non si sia selezionato alcun posto.
	 * @throws PaymentErrorException eccezione lanciata qualora il pagamento non sia
	 *                               andato a buon fine.
	 * @throws PersistenceException  eccezione lanciata qualora vi siano errori
	 *                               riscontrati durante l'uso di meccanismi di
	 *                               persistenza.
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
	 * Libera tutti i posti.
	 * 
	 * @throws RoomException eccezione lanciata qualora vi siano errori legati alla
	 *                       gestione della sala del cinema.
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
	 * METODO per settare il numero di persone che hanno un'età inferiore ad un età
	 * minima
	 * 
	 * @param n Numero di persone
	 * @throws InvalidNumberPeopleValueException
	 */
	/**
	 * Imposta il numero di persone che hanno un'età inferiore ad un età minima.
	 * 
	 * @param n numero di persone.
	 * @throws DiscountException
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
	 * METODO per settare il numero di persone che hanno un'età superiore ad un età
	 * massima
	 * 
	 * @param n Numero di persone
	 * @throws InvalidNumberPeopleValueException eccezione lanciata qualora il
	 *                                           numero di persone inserito sia
	 *                                           negativo, oppure il numero di
	 *                                           persone sotto l'età massima supera
	 *                                           il massimo consentito dai posti
	 *                                           correntemente selezionati.
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
		return this.coupon;
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
		return this.paymentCard;
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
