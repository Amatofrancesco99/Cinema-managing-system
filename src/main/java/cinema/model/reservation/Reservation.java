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

import cinema.model.money.Money;
import cinema.model.projection.Projection;
import cinema.model.Spectator;
import cinema.controller.Cinema;
import cinema.model.cinema.PhysicalSeat;
import cinema.model.payment.PaymentCard;
import cinema.model.reservation.discount.types.*;
import lombok.Data;

/**BREVE SPIEGAZIONE CLASSE RESERVATION (Facade Controller)
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Questa classe (Facade Controller), rappresenta la prenotazione effettiva che viene fatta
 * una volta selezionato il film che si vuole visionare e l'ora (Proiezione).
 * Tramite questa classe riusciamo a creare la prenotazione e tutte le sue informazioni, 
 * generare un file .pdf contenente tutte le informazioni della prenotazione stessa (sala,
 * film, posti riservati, ecc...) ed inviarlo come allegato tramite email, da parte del cinema
 * al cliente che sta comprando il biglietto, infine possiamo pagare tramite il metodo
 * di pagamento selezionato dall'utente.
 * Oltre la classe Cinema, forse è la seconda per importanza e per responsabilità. 
 */
@Data
public class Reservation {
	
	/**
	 *@param progressive	 Numero di prenotazione
	 *@param purchaseDate	 Data di creazione della prenotazione
	 *@param purchaser		 Spettatore che sta compilando la prenotazione
	 *@param seats			 Posti selezionati/occupati
	 *@param projection		 Proiezione che lo spettatore/gli spettatori vogliono visionare
	 *@param paymentCard	 Metodo di pagamento
	 *@param reportLocation  Area di memoria in cui salviamo il report della prenotazione
	 */
	private static final AtomicInteger count = new AtomicInteger(0); 
	private final long progressive;
	private LocalDate purchaseDate;
	private Spectator purchaser;
	private ArrayList<PhysicalSeat> seats;
	private Projection projection;
	private PaymentCard paymentCard;
	private String reportLocation;
	
	/**
	 * COSTRUTTORE della classe, esso una volta invocato genera una prenotazione con un
	 * progressivo che si auto-incrementa e la data di creazione corrisponde alla data di
	 * sistema in cui viene invocato il costruttore stesso
	 */
	public Reservation () {
		progressive = count.incrementAndGet(); 
		purchaseDate = java.time.LocalDate.now();
		seats = new ArrayList<PhysicalSeat>();
		reportLocation = null;
	}
	
	/***
	 * METODO per aggiungere un posto alla reservation
	 * @param seatR	  Posto da occupare
	 
	public void addSeat(PhysicalSeat seatR) {
		if (takeSeat(seatR) == true ) {
			seats.add(seatR);
		}
	}
	
	/**
	 * METODO per rimuovere un posto dalla reservation
	 * @param seatR		Posto da liberare
	 
	public void removeSeat(PhysicalSeat seatR) {
		if (freeSeat(seatR) == true) {
			seats.remove(seatR);
		}
	}
	**/
	
	
	/**
	 * METODO per farsi dare il totale della reservation, di fronte ad eventuali
	 * sconti effettuati su quest'ultima a seconda degli spettatori che sono intenzionati
	 * a visionare il film.
	 * 
	 * @return total  Ammontare di denaro da pagare
	 */
	public Money getTotal() {
		//TODO: CREARE CLASSE COMPOSITE (FUTURO)!!!
		
		// Al momento viene implementato il metodo che ti fa dare uno sconto a seconda 
		// dell'età degli spettatori
		return new DiscountAge().getTotal(this);
	}  
	
	/**
	 * METODO per creare un report, in formato .pdf, contenente tutte le informazioni 
	 * inerenti la prenotazione stessa.
	 * @return esitoCreazione   Qualora il file venisse creato senza problemi il valore restituito
	 * 					 		assume valore true, viceversa false (boolean).
	 */
	public boolean createReport() {
		String FILE = "./savedReports/Reservation_"+Long.toString(getProgressive())+".pdf";
		 
		//String FILE = Long.toString(getProgressive())+".pdf";
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
		 try {
	            Document document = new Document();
	            PdfWriter.getInstance(document, new FileOutputStream(FILE));
	            document.open();
	            
	            // ADD METADATA
	            document.addTitle("PDF Report Reservation n°" + this.getProgressive());
		        document.addSubject("Using iText");
		        document.addKeywords("Java, PDF, iText");
		        document.addAuthor("Screaming Hairy Armadillo Team");
		        document.addCreator("Screaming Hairy Armadillo Team");
		        
		        String imageBG = "https://i.pinimg.com/originals/00/25/03/002503946c0a59d4ae800ab05a037fda.jpg";
		        
	            Image bg = Image.getInstance(new URL(imageBG));
		        
	            bg.scalePercent(100f);
	            bg.setAbsolutePosition(-600f, -500f);

	            String imageUrl = Cinema.getInstance().getLogoURL();

	            Image image = Image.getInstance(new URL(imageUrl));
		        
	            image.scalePercent(20f);
	            
	            image.setAbsolutePosition(250f, 715f);
		        
		        Paragraph titleP = new Paragraph(Cinema.getInstance().getName() + "\n", catFont);
		        
		        titleP.setSpacingBefore(80);
		        titleP.setAlignment(Element.ALIGN_CENTER);
		        
		        Paragraph infoCinemaP = new Paragraph(Cinema.getInstance().getLocation() 
		        		+ "\n" + Cinema.getInstance().getEmail() + "\n", smallFont);
		        
		        infoCinemaP.setSpacingBefore(10);
		        infoCinemaP.setAlignment(Element.ALIGN_CENTER);
		        
		        Paragraph FilmP = new Paragraph(this.getProjection().getMovie().getTitle(), subFont);

		        FilmP.setSpacingBefore(40);
		        
		        Paragraph infoFilmP = new Paragraph("Regista/i:  " + this.getProjection().getMovie().getDirectors().toString()
		        					+ "\t\t\t\t\t\t\tDurata:  " + this.getProjection().getMovie().getDuration() 
		        					+ "\t\t\t\t\t\t\tRating film:  " + this.getProjection().getMovie().getRating(),
		        					subFont2);
		        
		        Paragraph infoReservationP = new Paragraph("Sala n°:  " + this.getProjection().getRoom().getProgressive()
		        							+ "\t\t\t\t\t\t\tData:  " + this.getProjection().getDateTime().getDayOfWeek()
		        							+ "/" + this.getProjection().getDateTime().getMonth()
		        							+ "/" + this.getProjection().getDateTime().getYear()
		        							+ " \t\t\t\t\t\tOra: " + this.getProjection().getDateTime().getHour()
		        							+ ":" + this.getProjection().getDateTime().getMinute(), 
		        							subFont3);
		        
		        infoReservationP.setSpacingBefore(30);
		        
		        PdfPTable table = new PdfPTable(3);
		        
		        table.setSpacingBefore(60);

		        PdfPCell c1 = new PdfPCell(new Phrase("NOME"));
		        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(c1);

		        c1 = new PdfPCell(new Phrase("COGNOME"));
		        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(c1);

		        c1 = new PdfPCell(new Phrase("POSTO"));
		        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(c1);
		        table.setHeaderRows(1);

		        table.addCell("Mario");
		        table.addCell("Rossi");
		        table.addCell("A1");
		        table.addCell("Giovanni");
		        table.addCell("Rossi");
		        table.addCell("A2");
		        
		        Paragraph totalP = new Paragraph("TOTALE:  " + this.getTotal().getAmount()
		        				   + this.getTotal().getCurrency().toString(), subFont3);
		        
		        totalP.setSpacingBefore(80);
		        totalP.setAlignment(Element.ALIGN_RIGHT);
		        totalP.setIndentationRight(55);
		        
		        document.add(bg);
		        document.add(image);
		        document.add(titleP);
		        document.add(infoCinemaP);
		        document.add(FilmP);
		        document.add(infoFilmP);
		        document.add(infoReservationP);
		        document.add(table);
		        document.add(totalP);
		        
		        // CLOSE DOCUMENT
	            document.close();
	            setReportLocation(FILE);
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	}
	
	/**
	 * METODO per effettuare l'invio tramite email da parte del cinema, all'utente che 
	 * sta prenotando, del report .pdf della prenotazione stessa.
	 * @return esito    Stringa che descrive l'esito della creazione dell'email e 
	 * 					qualora ci siano problemi indica quale sia la problematica specifica
	 * 					che ha impedito un corretto invio dell'email stessa.
	 */
	public String sendEmail() {
		if ((createReport()==false) || (this.getReportLocation()==null)) {
			return "La generazione del report non é andata a buon fine.";
		}
		else {
			String to = this.getPurchaser().getEmail(); //receiver email
			final String user = Cinema.getInstance().getEmail(); //sender email (cinema)
			final String password = Cinema.getInstance().getPassword(); //sender password
		   
			//1) get the session object     
			Properties properties = System.getProperties();  
			properties.setProperty("mail.smtp.host", "mail.javatpoint.com");  
			properties.put("mail.smtp.auth", "true");  
		  
			Session session = Session.getDefaultInstance(properties,  
			new javax.mail.Authenticator() {  
				protected PasswordAuthentication getPasswordAuthentication() {  
					return new PasswordAuthentication(user,password);  
				}  
			});  
		     
			//2) compose message     
			try{  
				MimeMessage message = new MimeMessage(session);  
				message.setFrom(new InternetAddress(user));  
				message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
				message.setSubject("REPORT PRENOTAZIONE N° " + this.progressive + " | TI ASPETTIAMO!");  
		      
				//3) create MimeBodyPart object and set your message text     
				BodyPart messageBodyPart1 = new MimeBodyPart();  
				messageBodyPart1.setText(
						"SI PREGA DI NON RISPONDERE ALLA SEGUENTE EMAIL.\n\n"
						+ "Benvenuto " + to + " ,/n"
						+ "In allegato trovi il documento che conferma l'avvenuta prenotazione.\n"
						+ "Stampa l'allegato, o porta una prova della ricevuta quando verrai"
						+ "a visionare il film.\n"
						+ "Ti aspettiamo, buona giornata.");  
		      
				//4) create new MimeBodyPart object and set DataHandler object to this object      
				MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
		  
				String filename = getReportLocation(); //change accordingly  
				DataSource source = new FileDataSource(filename);  
				messageBodyPart2.setDataHandler(new DataHandler(source));  
				messageBodyPart2.setFileName(filename);  
		     
				//5) create Multipart object and add MimeBodyPart objects to this object      
				Multipart multipart = new MimeMultipart();  
				multipart.addBodyPart(messageBodyPart1);  
				multipart.addBodyPart(messageBodyPart2);  
		  
				//6) set the multiplart object to the message object  
				message.setContent(multipart);  
		     
				//7) send message  
				Transport.send(message);  
		   
				return "Email inviata...";  
			}
			catch (MessagingException ex) {
				ex.printStackTrace();
				return "Processo di invio fallito...Riprova più tardi.";  
				}  
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
	 * METODO per settare l'età degli spettatori che guarderanno il film.
	 * Il numero di spettatori da inserire dipende chiaramente dai posti associati alla
	 * prenotazione.
	 * @return	TakenSeats		Numero di posti occupati
	public void setSpectator(int n, int age) {
		if (n>getNSeats()) {
		}
		else //TODO aggiungi queste informazioni alla prenotazione
	}
	*/
	
	/**
	 *  METODO che consente il pagamento della prenotazione, una volta compilata la prenotazione
	 * @return esito	 Stringa che rappresenta l'esito del pagamento e il verificarsi
	 * 					 di eventuali errori (ad esempio che la prenotazione occupi
	 * 					 almeno un posto, o che il numero di persone ed il numero di posti
	 * 					 occupati dalla reservation siano gli stessi).
	 */
	public String buy(){
		if ((getNSeats()>0) || (getNSeats()>=1))
		{
			//Payment simulation
			if (paymentCard.decreaseMoney(getTotal())==false) {
				return "Il pagamento non è andato a buon fine.";
			}
			else {
				return "Pagamento andato a buon fine.";
			}
		}
		else return "Verifica di aver inserito almeno un posto alla prenotazione.";
	}
	
	/**
	 * METODO per occupare un posto specifico della sala in cui è proiettato il film
	 * che sto prenotando.
	 * @param s		    Posto da occupare
	 * @return esito	Esito dell'occupazione del posto
	 
	public boolean takeSeat(PhysicalSeat s) {
		projection.freeSeat(s);
		return projection.freeSeat(s);
	}
	
	/**
	 * METODO per liberare un posto specifico della sala in cui è proiettato il film che sto
	 * prenotando.
	 * @param s			Posto da liberare
	 * @return esito	Esito della liberazione del posto
	public boolean freeSeat(PhysicalSeat s) {
		projection.freeSeat(s);
		return projection.freeSeat(s);
	}
	
	 */
}