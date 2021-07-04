package cinema.model.reservation;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

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
import cinema.model.reservation.discount.types.util.TypeOfDiscounts;
import cinema.model.reservation.util.SeatAvailabilityException;
import cinema.model.reservation.util.ReservationException;


/**BREVE SPIEGAZIONE CLASSE RESERVATION
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Questa classe, rappresenta la prenotazione effettiva che viene fatta
 * una volta selezionato il film che si vuole visionare e l'ora (Proiezione).
 * Tramite questa classe riusciamo a creare la prenotazione e tutte le sue informazioni, 
 * generare un file .pdf contenente tutte le informazioni della prenotazione stessa (sala,
 * film, posti riservati, ecc...) ed inviarlo come allegato tramite email, da parte del cinema
 * al cliente che sta comprando il biglietto, infine possiamo pagare tramite il metodo
 * di pagamento selezionato dall'utente.
 * Oltre la classe Cinema, forse è la seconda per importanza e per responsabilità. 
 */
public class Reservation {
	
	
	/**
	 *@param progressive	 Numero di prenotazione
	 *@param purchaseDate	 Data di creazione della prenotazione
	 *@param purchaser		 Spettatore che sta compilando la prenotazione
	 *@param seats			 Posti selezionati/occupati
	 *@param projection		 Proiezione che lo spettatore/gli spettatori vogliono visionare
	 *@param paymentCard	 Metodo di pagamento
	 *@param reportLocation  Area di memoria in cui salviamo il report della prenotazione
	 *@param coupon			 Coupon che può essere usato nella prenotazione
	 *@param numberPeopleUntilMinAge   Numero di persone che hanno un'età inferiore ad un età min
	 *@param numberPeopleOverMaxAge    Numero di persone che hanno un'età superiore ad un età max
	 *@param rd				 Strategia di sconto applicata
	 */
	private static final AtomicInteger count = new AtomicInteger(0); 
	private final long progressive;
	private LocalDate purchaseDate;
	private Spectator purchaser;
	private ArrayList<PhysicalSeat> seats;
	private Projection projection;
	private PaymentCard paymentCard;
	private String reportLocation;
	private Coupon coupon; 
	private int numberPeopleUntilMinAge;
	private int numberPeopleOverMaxAge;
	private IReservationDiscountStrategy rd;
	
	
	/**
	 * COSTRUTTORE della classe, esso una volta invocato genera una prenotazione con un
	 * progressivo che si auto-incrementa e la data di creazione corrisponde alla data di
	 * sistema in cui viene invocato il costruttore stesso
	 */
	public Reservation(IReservationDiscountStrategy rd) {
		progressive = count.incrementAndGet(); 
		purchaseDate = java.time.LocalDate.now();
		seats = new ArrayList<PhysicalSeat>();
		paymentCard = null;
		reportLocation = null;
		coupon = null;
		numberPeopleUntilMinAge = 0;
		numberPeopleOverMaxAge = 0;
		this.rd = rd;
	}
	
	
	/***
	 * METODO per aggiungere un posto alla reservation
	 * @param row, col		Coordinate posto sala da occupare
	 * @throws SeatAlreadyTakenException 
	 * @throws InvalidRoomSeatCoordinatesException 
	 * @throws SeatTakenTwiceException 
	 * @throws FreeAnotherPersonSeatException 
	*/
	public void addSeat(int row, int col) throws SeatAvailabilityException, RoomException{
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
				throw new SeatAvailabilityException("Il posto " + Room.rowIndexToRowLetter(row) + "-" + ( col + 1 ) + " è già stato selezionato.");
			}
		}
		else throw new SeatAvailabilityException("Il posto " + Room.rowIndexToRowLetter(row) + "-" + ( col + 1 ) + " è già occupato.");
	}
	
	
	/**
	 * METODO per rimuovere un posto dalla reservation
	 * @param row, col		Coordinate posto sala da liberare
	 * @throws RoomException 
	 * @throws InvalidRoomSeatCoordinatesException 
	 * @throws FreeAnotherPersonSeatException 
	*/
	public void removeSeat(int row, int col) throws RoomException {
		seats.remove(projection.getPhysicalSeat(row, col));
	}
	
	
	/**
	 * METODO per farsi dare il totale della reservation, di fronte ad eventuali
	 * sconti effettuati su quest'ultima a seconda degli spettatori che sono intenzionati
	 * a visionare il film.
	 * 
	 * @return total  Ammontare di denaro da pagare
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
		return  Math.round(total * 100.0)/100.0;
	}  
	

	/**
	 * METODO per farsi dare il totale della prenotazione, senza sconti
	 * @return total  Ammontare di denaro
	 */
	public double getFullPrice() {
		return Math.round(getNSeats() * this.projection.getPrice() * 100.0)/100.0;
	}  
	
	
	/**
	 * METODO per aggiungere un coupon dato il suo progressivo
	 * @param coupon				 Coupon
	 * @throws CouponAleadyUsedException 
	 */
	public void setCoupon(Coupon coupon) throws CouponException {
		if (coupon == null) ;
		else {
			if (!coupon.isUsed())
				this.coupon = coupon;
			else throw new CouponException("Il coupon " + coupon.getCode() + " è già stato usato.");
		}
	}
	
	
	/**
	 * METODO per farsi dire quanti siano stati i posti occupati dalla prenotazione
	 * @return	TakenSeats		Numero di posti occupati
	 */
	public int getNSeats() {
		return seats.size();
	}
	
	
	/**
	 *  METODO che consente il pagamento della prenotazione, una volta compilata la prenotazione
	 * @return esito acquisto
	 * @throws PaymentErrorException 
	 * @throws ReservationHasNoSeatException 
	 * @throws ReservationHasNoPaymentCardException 
	 * @throws InvalidRoomSeatCoordinatesException 
	 * @throws SeatAlreadyTakenException 
	 * @throws NumberFormatException 
	 */
	public void buy() throws SeatAvailabilityException, NumberFormatException, RoomException, ReservationException, PaymentErrorException, PersistenceException{
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
	 * Metodo che permette di occupare i posti selezionati e di gestire le situazioni di concorrenza 
	 * @throws InvalidRoomSeatCoordinatesException
	 * @throws SeatAlreadyTakenException
	 */
	public void takeSeat() throws RoomException, SeatAvailabilityException {		
		for (int i = 0; i<seats.size(); i++) {
			String coordinates = projection.getSeatCoordinates(seats.get(i));
			int row = Room.rowLetterToRowIndex(coordinates.replaceAll("\\d",""));
			int col = Integer.valueOf(coordinates.replaceAll("[\\D]", "")) - 1;
			if (projection.takeSeat(row, col));	
			else {
				freeAllSeats();
				seats.removeAll(seats);
				throw new SeatAvailabilityException("Uno dei posti selezionati è già stato occupato.");
			}
		}
	}
	
	
	/**
	 * Metodo che permette di pagare la reservation
	 * @return esito pagamento
	 * @throws ReservationHasNoSeatException
	 * @throws ReservationHasNoPaymentCardException
	 * @throws PaymentErrorException
	 * @throws PersistenceException 
	 */
	public void pay() throws ReservationException, PaymentErrorException, PersistenceException {
		if (getNSeats() > 0)
		{
			if (paymentCard == null) {
				throw new ReservationException("La prenotazione non ha associata nessuna carta di credito.");
			}
			else {
				//Payment simulation
				if (new GreatNorthernAccountingAdapter(paymentCard).pay(getTotal()) == false) {
					throw new PaymentErrorException("Il pagamento non è andato a buon fine.");
				}
			}
		}
		else throw new ReservationException("Nessun posto è stato selezionato.");
	}
	
	
	/**
	 * METODO per liberare tutti i posti
	 * @throws RoomException
	 */
	public void freeAllSeats() throws RoomException {
		for (int i = 0; i<seats.size(); i++) {
			String coordinates = projection.getSeatCoordinates(seats.get(i));
			int row = Room.rowLetterToRowIndex(coordinates.replaceAll("\\d",""));
			int col = Integer.valueOf(coordinates.replaceAll("[\\D]", "")) - 1;
			if (projection.checkIfSeatIsAvailable(row, col)) 
				projection.freeSeat(row, col);		
		}
	}
	
	
	/**
	 * METODO per settare il numero di persone che hanno un'età inferiore ad un età minima
	 * @param n				Numero di persone
	 * @throws InvalidNumberPeopleValueException 
	 */
	public void setNumberPeopleUnderMinAge(int n) throws DiscountException {
		if(n < 0)
			throw new DiscountException("Il numero di persone non può essere negativo.");
		if (n + getNumberPeopleOverMaxAge() > this.getNSeats())
			throw new DiscountException("Il numero di persone sotto l'età minima supera il massimo consentito dai posti correntemente selezionati.");
		numberPeopleUntilMinAge = n;
	}
	
	
	/**
	 * METODO per settare il numero di persone che hanno un'età superiore ad un età massima
	 * @param n				Numero di persone
	 * @throws InvalidNumberPeopleValueException 
	 */
	public void setNumberPeopleOverMaxAge(int n) throws DiscountException {
		if(n < 0)
			throw new DiscountException("Il numero di persone non può essere negativo.");
		if (n + getNumberPeopleUntilMinAge() > this.getNSeats())
			throw new DiscountException("Il numero di persone sotto l'età massima supera il massimo consentito dai posti correntemente selezionati.");
		numberPeopleOverMaxAge = n;
	}
	
	
	/** METODO per farsi dire il coupon associato alla prenotazione*/
	public Coupon getCoupon() {
		return this.coupon;
	}
	
	
	/** METODO per farsi dire il numero di persone aventi un età inferiore ad un determinato
	 * valore sono state inserite*/
	public int getNumberPeopleUntilMinAge() {
		return numberPeopleUntilMinAge;
	}

	
	/** METODO per farsi dire il numero di persone aventi un età superiore ad un determinato
	 * valore sono state inserite*/
	public int getNumberPeopleOverMaxAge() {
		return numberPeopleOverMaxAge;
	}
	
	
	/** METODO per impostare la locazione di memoria in cui è salvata la prenotazione*/
	public void setReportLocation(String path) {
		reportLocation = path;
	}
	
	
	/** METODO per farsi dire la posizione in memoria (percorso) in cui è salvata la prenotazione*/
	public String getReportLocation() {
		return reportLocation;
	}
	
	
	/** METODO per farsi dire il progressivo della prenotazione*/
	public long getProgressive() {
		return progressive;
	}
	
	
	/** METODO per farsi dire il compratore della prenotazione*/
	public Spectator getPurchaser() {
		return purchaser;
	}
	
	
	/** METODO per farsi dire la proiezione della prenotazione*/
	public Projection getProjection() {
		return projection;
	}


	/** METODO per restituire la data in cui è stata creata la prenotazione */
	public LocalDate getDate() {
		return purchaseDate;
	}
	
	
	/** METODO per farsi dare i posti che compongono la prenotazione */
	public ArrayList<PhysicalSeat> getSeats(){
		return seats;
	}
	
	
	/**METODO per farsi dire la strategia della reservation*/
	public IReservationDiscountStrategy getStrategy() {
		return rd;
	}
	
	
	/**METODO per restituire la carta di credito associata alla prenotazione*/
	public PaymentCard getPaymentCard() {
		return this.paymentCard;
	}
	
	
	/** METODO per impostare la proiezione della prenotazione*/
	public void setProjection(Projection projection) {
		this.projection = projection;
	}

	
	/** METODO per impostare il compratore della prenotazione*/
	public void setPurchaser(Spectator spectator) {
		this.purchaser = spectator;
	}


	/** METODO per associare la carta di credito alla prenotazione*/
	public void setPaymentCard(String number, String owner, String cvv, YearMonth expirationDate) {
		this.paymentCard = new PaymentCard(number, owner, cvv, expirationDate);
	}
	
	public TypeOfDiscounts getTypeOfDiscount() {
		return rd.getTypeOfDiscount();
	}
}
