package cinema.model.reservation;

import java.io.FileOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import cinema.model.payment.methods.paymentCard.PaymentCard;
import cinema.model.payment.util.PaymentErrorException;
import cinema.model.projection.Projection;
import cinema.model.spectator.Spectator;
import cinema.controller.Cinema;
import cinema.model.cinema.PhysicalSeat;
import cinema.model.cinema.Room;
import cinema.model.cinema.util.InvalidRoomSeatCoordinatesException;
import cinema.model.reservation.discount.coupon.Coupon;
import cinema.model.reservation.discount.types.*;
import cinema.model.reservation.discount.types.util.InvalidNumberPeopleValueException;
import cinema.model.reservation.util.FreeAnotherPersonSeatException;
import cinema.model.reservation.util.ReservationHasNoPaymentCardException;
import cinema.model.reservation.util.ReservationHasNoSeatException;
import cinema.model.reservation.util.SeatAlreadyTakenException;
import cinema.model.reservation.util.SeatTakenTwiceException;


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
	 */
	private static final AtomicInteger count = new AtomicInteger(0); 
	private final long progressive;
	@SuppressWarnings("unused")
	private LocalDate purchaseDate;
	private Spectator purchaser;
	private ArrayList<PhysicalSeat> seats;
	private Projection projection;
	private PaymentCard paymentCard;
	private String reportLocation;
	private Coupon coupon;
	private int numberPeopleUntilMinAge;
	private int numberPeopleOverMaxAge;
	private int countTakenSeat = 0;
	
	/**
	 * COSTRUTTORE della classe, esso una volta invocato genera una prenotazione con un
	 * progressivo che si auto-incrementa e la data di creazione corrisponde alla data di
	 * sistema in cui viene invocato il costruttore stesso
	 */
	public Reservation () {
		progressive = count.incrementAndGet(); 
		purchaseDate = java.time.LocalDate.now();
		seats = new ArrayList<PhysicalSeat>();
		paymentCard = null;
		reportLocation = null;
		numberPeopleUntilMinAge = 0;
		numberPeopleOverMaxAge = 0;
	}
	
	
	/***
	 * METODO per aggiungere un posto alla reservation
	 * @param row, col		Coordinate posto sala da occupare
	 * @throws SeatAlreadyTakenException 
	 * @throws InvalidRoomSeatCoordinatesException 
	 * @throws SeatTakenTwiceException 
	 * @throws FreeAnotherPersonSeatException 
	*/
	public void addSeat(int row, int col) throws SeatAlreadyTakenException, InvalidRoomSeatCoordinatesException, SeatTakenTwiceException, FreeAnotherPersonSeatException {
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
				throw new SeatTakenTwiceException(row,col);
			}
		}
		else throw new SeatAlreadyTakenException(row,col);
	}
	
	
	/**
	 * METODO per rimuovere un posto dalla reservation
	 * @param row, col		Coordinate posto sala da liberare
	 * @throws InvalidRoomSeatCoordinatesException 
	 * @throws FreeAnotherPersonSeatException 
	*/
	public void removeSeat(int row, int col) throws InvalidRoomSeatCoordinatesException, FreeAnotherPersonSeatException {
		try {
			if(!projection.checkIfSeatIsAvailable(row, col)) 
				seats.remove(projection.getPhysicalSeat(row, col));		
			}
		catch (Exception e) {
			// Qualora il posto che sto cercando di rimuovere non fa parte dei posti della
			// mia prenotazione allora il seat.remove(...) lancerà un eccezione, che noi
			// gestiamo stampando una stringa che informi il cliente che non può liberare
			// posti occupati da altre persone
			throw new FreeAnotherPersonSeatException();
		}
	}
	
	
	/**
	 * METODO per farsi dare il totale della reservation, di fronte ad eventuali
	 * sconti effettuati su quest'ultima a seconda degli spettatori che sono intenzionati
	 * a visionare il film.
	 * 
	 * @return total  Ammontare di denaro da pagare
	 */
	public double getTotal() {
		double total = new CinemaDiscount().getTotal(this);
		// Qualora alla prenotazione sia associato un coupon esistente vado a detrarre
		// il totale dell'importo di sconto di questo coupon
		if (getCoupon() != null) {
			total -= getCoupon().getDiscount();
		}
		return Math.round(total * 100.0)/100.0;
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
	 */
	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}
	
	
	/**
	 * METODO per creare un report, in formato .pdf, contenente tutte le informazioni 
	 * inerenti la prenotazione stessa.
	 * @return esitoCreazione   Qualora il file venisse creato senza problemi il valore restituito
	 * 					 		assume valore true, viceversa false (boolean).
	 */
	public boolean createReport() {
		// setting della posizione in cui il report sarà salvato
		String FILE = "./src/main/java/cinema/model/reservation/savedReports/Reservation_"+Long.toString(getProgressive())+".pdf";
		 
		// impostazione dei caratteri che verranno utilizzati nel report
		Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 33,
	            Font.BOLD);
		Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 20,
	            Font.BOLD);
		Font subFont2 = new Font(Font.FontFamily.TIMES_ROMAN, 14,
	            Font.NORMAL);
		Font subFont3 = new Font(Font.FontFamily.TIMES_ROMAN, 18,
	            Font.NORMAL);
		Font smallFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,
	            Font.ITALIC);  
		// tentativo di generazione del report
		 try {
			 	// creazione di un nuovo documento
	            Document document = new Document();
	            PdfWriter.getInstance(document, new FileOutputStream(FILE));
	            document.open();
	            
	            // aggiungere i metadati al documento
	            document.addTitle("PDF Report Reservation n°" + this.getProgressive());
		        document.addSubject("Using iText");
		        document.addKeywords("Java, PDF, iText");
		        document.addAuthor("Screaming Hairy Armadillo Team");
		        document.addCreator("Screaming Hairy Armadillo Team");
		        
		        // creazione di un colore di background per il nostro report (giallognolo)
		        String imageBG = "https://i.pinimg.com/originals/00/25/03/002503946c0a59d4ae800ab05a037fda.jpg";
		        
		        // creare una nuova immagine di quel colore e setting delle sue dimensioni
	            Image bg = Image.getInstance(new URL(imageBG));
	            bg.scalePercent(100f);
	            bg.setAbsolutePosition(-600f, -500f);
	            
	            // creazione di una nuova immagine con il logo del cinema
	            String imageUrl = Cinema.getLogoURL();
	            Image image = Image.getInstance(new URL(imageUrl));
	            image.scalePercent(20f);
	            image.setAbsolutePosition(250f, 715f);
		        
	            // creare un titolo per il report
		        Paragraph titleP = new Paragraph(Cinema.getName() + "\n", catFont);
		        titleP.setSpacingBefore(80);
		        titleP.setAlignment(Element.ALIGN_CENTER);
		        
		        // creare informazioni sul nome del cinema 
		        Paragraph infoCinemaP = new Paragraph(Cinema.getLocation() 
		        		+ "\n" + Cinema.getEmail() + "\n", smallFont);
		        infoCinemaP.setSpacingBefore(10);
		        infoCinemaP.setAlignment(Element.ALIGN_CENTER);
		        
		        // creazione di informazioni sul film che si vuole visionare
		        Paragraph FilmP = new Paragraph(this.getProjection().getMovie().getTitle(), subFont);
		        FilmP.setSpacingBefore(40);
		        
		        // creazione di alcune informazioni riassuntive sul film che si è prenotato
		        Paragraph infoFilmP = new Paragraph("Regista/i:  " + this.getProjection().getMovie().getDirectors().toString()
		        					+ "\t\t\t\t\t\t\tDurata:  " + this.getProjection().getMovie().getDuration() 
		        					+ "\t\t\t\t\t\t\tRating film:  " + this.getProjection().getMovie().getRating(),
		        					subFont2);
		        
		        // creazione di informazioni sulla prenotazione
		        Paragraph infoReservationP = new Paragraph("Sala n°:  " + this.getProjection().getRoom().getProgressive()
		        							+ "\t\t\t\t\t\t\tData:  " + this.getProjection().getDateTime().getDayOfWeek().toString().toLowerCase()
		        							+ " " + this.getProjection().getDateTime().getDayOfMonth()
		        							+ " " + this.getProjection().getDateTime().getMonth().toString().toLowerCase()
		        							+ " " + this.getProjection().getDateTime().getYear()
		        							+ " \t\t\t\t\t\tOra: " + String.format("%02d", this.projection.getDateTime().getHour()) 
		        							+ ":" + String.format("%02d", this.projection.getDateTime().getMinute())
		        							+ " \t\t\t\t\t\tCliente: " + this.purchaser.getName() + " " + this.purchaser.getSurname(),
		        							subFont2);
		        infoReservationP.setSpacingBefore(30);
		        
		        // generazione di una tabella 
		        PdfPTable table = new PdfPTable(1);
		        table.setSpacingBefore(60);

		        // aggiungere alla tabella come primo campo il titolo "Posti riservati"
		        PdfPCell c1 = new PdfPCell(new Phrase("POSTI RISERVATI"));
		        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(c1);
		        
		        //aggiungere alla tabella tutti i posti che si sono aggiunti alla prenotazione
		        for(PhysicalSeat s : seats) {
		        	String seatCoordinates = projection.getSeatCoordinates(s);
		        	if(seatCoordinates != null)
		        		table.addCell("Fila: " + seatCoordinates.replaceAll("\\d","") +
		        				 "\t\tPosto: " + seatCoordinates.replaceAll("[\\D]",""));	
		        }
		        
		        //totale della prenotazione
		        Paragraph totalP = new Paragraph("TOTALE:  " + this.getTotal()
		        				   + "€ ", subFont3);
		        totalP.setSpacingBefore(80);
		        totalP.setAlignment(Element.ALIGN_RIGHT);
		        totalP.setIndentationRight(55);
		        
		        // aggiunte al documento tutte le informazioni precedentemente create
		        document.add(bg);
		        document.add(image);
		        document.add(titleP);
		        document.add(infoCinemaP);
		        document.add(FilmP);
		        document.add(infoFilmP);
		        document.add(infoReservationP);
		        document.add(table);
		        document.add(totalP);
		        
		        // Chiusura del documento
	            document.close();
	            setReportLocation(FILE);
	       
	            // generazione del file andata a buon fine
	            return true;
	        } catch (Exception e) {
	        	e.toString();
	        	return false;
	        }
	}
	
	
	/**
	 * METODO per effettuare l'invio tramite email da parte del cinema, all'utente che 
	 * sta prenotando, del report .pdf della prenotazione stessa.
	 * @return esito invio email
	 */
	public boolean sendEmail() {
		// prima di inviare l'email verifico che il report sia già stato generato, 
		// se non è ancora stato generato lo genero
		while (this.getReportLocation() == null) {
			createReport();
		}
		
		// Stabilire le informazioni sul sender ed il receiver dell'email
		String to = this.getPurchaser().getEmail(); //receiver email
		final String user = Cinema.getEmail(); //sender email (cinema)
		final String password = Cinema.getPassword(); //sender password
		   
		// Stabilire le proprietà dell'email
		Properties properties = System.getProperties();  
		String host = "smtp.gmail.com";
		properties.put("mail.smtp.starttls.enable", "true");
	    properties.put("mail.smtp.host", host);
	    properties.put("mail.smtp.user", user);
	    properties.put("mail.smtp.password", password);
	    properties.put("mail.smtp.port", "587");
	    properties.put("mail.smtp.auth", "true");

	    // Generazione di una nuova sessione mail
	    Session session = Session.getDefaultInstance(properties,  
		new javax.mail.Authenticator() {  
		protected PasswordAuthentication getPasswordAuthentication() {  
				return new PasswordAuthentication(user,password);  
			}  
	    });  
		     
	    //Tentativo di composizione del messaggio ed invio dell'email  
		try{  
			MimeMessage message = new MimeMessage(session);  
			message.setFrom(new InternetAddress(user));  
			message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
			message.setSubject("REPORT PRENOTAZIONE N° " + this.progressive + " | TI ASPETTIAMO!");  
		      
			// Creazione del body della nostra email   
			BodyPart messageBodyPart1 = new MimeBodyPart();  
			messageBodyPart1.setText(
					"SI PREGA DI NON RISPONDERE ALLA SEGUENTE EMAIL.\n\n\n"
					+ "Benvenuto " + this.purchaser.getName() + " " + this.purchaser.getSurname() + ",\n\n"
					+ "In allegato trovi il documento che conferma l'avvenuta prenotazione.\n"
					+ "Stampa l'allegato, o porta una prova della ricevuta quando verrai"
					+ "a visionare il film.\n\n"
					+ "Ti aspettiamo, buona giornata.\n\n\n");  
		      
			// Aggiungere all'email come allegato il report della prenotazione      
			MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
		  
			String filename = getReportLocation();
			DataSource source = new FileDataSource(filename);  
			messageBodyPart2.setDataHandler(new DataHandler(source));  
			messageBodyPart2.setFileName("Reservation_"+Long.toString(getProgressive())+".pdf");  
		     
			// Creazione di un campo multipart comprendente body e allegato    
			Multipart multipart = new MimeMultipart();  
			multipart.addBodyPart(messageBodyPart1);  
			multipart.addBodyPart(messageBodyPart2);  
		  
			// Aggiungere al messaggio da inviare (email) body e allegato
			message.setContent(multipart);  
		     
			// Invio dell'email
			Transport.send(message);   
			
			return true;
			}
			catch (MessagingException ex) {
				ex.toString();
				return false;
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
	public boolean buy() throws SeatAlreadyTakenException, NumberFormatException, InvalidRoomSeatCoordinatesException, ReservationHasNoSeatException, ReservationHasNoPaymentCardException, PaymentErrorException{
		takeSeat();
		return pay();
	}
	
	
	/**
	 * Metodo che permette di occupare i posti selezionati e di gestire le situazioni di concorrenza 
	 * @throws InvalidRoomSeatCoordinatesException
	 * @throws SeatAlreadyTakenException
	 */
	private void takeSeat() throws InvalidRoomSeatCoordinatesException, SeatAlreadyTakenException {		
		for (int i = countTakenSeat; i<seats.size(); i++) {
			String coordinates = projection.getSeatCoordinates(seats.get(i));
			int row = Room.rowLetterToRowIndex(coordinates.replaceAll("\\d",""));
			int col = Integer.valueOf(coordinates.replaceAll("[\\D]", "")) - 1;
			if (projection.takeSeat(row, col))
				countTakenSeat++;
			else {
				seats.remove(seats.get(i));
				throw new SeatAlreadyTakenException(row,col);
			}
		}
		
	}
	
	
	/**
	 * Metodo che permette di pagare la reservation
	 * @return esito pagamento
	 * @throws ReservationHasNoSeatException
	 * @throws ReservationHasNoPaymentCardException
	 * @throws PaymentErrorException
	 */
	private boolean pay() throws ReservationHasNoSeatException, ReservationHasNoPaymentCardException, PaymentErrorException {
		if (getNSeats() > 0)
		{
			if (paymentCard == null) {
				throw new ReservationHasNoPaymentCardException();
			}
			else {
				//Payment simulation
				if (paymentCard.decreaseMoney(getTotal()) == false) {
					throw new PaymentErrorException();
				}
				else {
					if (getCoupon() != null) {
						// se il pagamento va a buon fine dico che il coupon è stato utilizzato
						// chiaramente se un coupon è stato associato alla prenotazione
						this.coupon.setUsed(true);
					}
					return true;
				}
			}
		}
		else throw new ReservationHasNoSeatException();
	}
	
	/**
	 * METODO per settare il numero di persone che hanno un'età inferiore ad un età minima
	 * @param n				Numero di persone
	 * @throws InvalidNumberPeopleValueException 
	 */
	public void setNumberPeopleUntilMinAge(int n) throws InvalidNumberPeopleValueException {
		if ((n + getNumberPeopleOverMaxAge() > this.getNSeats()) || (n < 0)) {
			throw new InvalidNumberPeopleValueException();
		}
		else numberPeopleUntilMinAge = n;
	}
	
	
	/**
	 * METODO per settare il numero di persone che hanno un'età superiore ad un età massima
	 * @param n				Numero di persone
	 * @throws InvalidNumberPeopleValueException 
	 */
	public void setNumberPeopleOverMaxAge(int n) throws InvalidNumberPeopleValueException {
		if ((n + getNumberPeopleUntilMinAge() > this.getNSeats()) || (n < 0)) {
			throw new InvalidNumberPeopleValueException();
		}
		else numberPeopleOverMaxAge = n;
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


	/** METODO per impostare la proiezione della prenotazione*/
	public void setProjection(Projection projection) {
		this.projection = projection;
	}

	
	/** METODO per impostare il compratore della prenotazione*/
	public void setPurchaser(Spectator spectator) {
		this.purchaser = spectator;
	}


	/** METODO per associare la carta di credito alla prenotazione*/
	public void setPaymentCard(PaymentCard p) {
		this.paymentCard = p;
	}
}