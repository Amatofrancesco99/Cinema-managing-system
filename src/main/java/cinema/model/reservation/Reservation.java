package cinema.model.reservation;

import java.io.FileOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import cinema.model.Money;
import cinema.model.Projection;
import cinema.model.Spectator;
import cinema.model.cinema.Cinema;
import cinema.model.cinema.PhisicalSeat;
import cinema.model.payment.PaymentCard;
import cinema.model.reservation.discount.types.*;

public class Reservation {

	private static final AtomicInteger count = new AtomicInteger(0); 
	private final long progressive;
	private LocalDate purchaseDate;
	private ArrayList<Spectator> spectators;
	private ArrayList<PhisicalSeat> seats;
	private Projection projection;
	private PaymentCard paymentCard;
	
	public Reservation () {
		progressive = count.incrementAndGet(); 
		purchaseDate = java.time.LocalDate.now();
		spectators = new ArrayList<Spectator>();
		seats = new ArrayList<PhisicalSeat>();
	}
	
	// aggiungi e rimuovi spettatori dalla Reservation
	public void addSpectator(Spectator s) {
		spectators.add(s);
	}
	public void removeSpectator(Spectator s) {
		spectators.remove(s);
	}
	
	// aggiungi e rimuovi posti alla Reservation
	public void addSeat(PhisicalSeat seatR) {
		if (takeSeat(seatR) == true ) {
			seats.add(seatR);
		}
	}
	public void removeSeat(PhisicalSeat seatR) {
		if (freeSeat(seatR) == true) {
			seats.remove(seatR);
		}
	}
	//it depends on the implementation
	/*public void addMultipleSeats(ArrayList<PhisicalSeat> seatsR) {
		for(PhisicalSeat seat:seatsR) 
			this.seats.add(seat);	
	}
	public void removeMultipleSeats(ArrayList<PhisicalSeat> seatsR) {
		for(PhisicalSeat seat:seatsR) 
			this.seats.remove(seat);	
	}*/
	
	// imposta o cambia il metodo di pagamento
	public void setPaymentCard(PaymentCard p) {
		paymentCard = p;
	}
	
	// imposta la proiezione di un film 
	public void setProjection(Projection p) {
		projection = p;
	}
	
	// farsi restituire l'ammontare di soldi da pagare 
	public Money getTotal() {
		// Cambia la discount strategy per farti dare lo sconto
		// CREARE CLASSE COMPOSITE (FUTURO)!!!
		return new DiscountAge().getTotal(this);
	}  
	
	// generazione di un documento con tutte le informazioni della reservation
	public boolean createReport() {   
		String FILE = "./savedReports/"+Long.toString(getProgressive())+".pdf";
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
	            document.addTitle("My first PDF");
		        document.addSubject("Using iText");
		        document.addKeywords("Java, PDF, iText");
		        document.addAuthor("Lars Vogel");
		        document.addCreator("Lars Vogel");
		        
		        String imageBG = "https://i.pinimg.com/originals/00/25/03/002503946c0a59d4ae800ab05a037fda.jpg";
		        
	            Image bg = Image.getInstance(new URL(imageBG));
		        
	            bg.scalePercent(100f);
	            bg.setAbsolutePosition(-600f, -500f);

	            String imageUrl = Cinema.getInstance().getLogoURL();

	            Image image = Image.getInstance(new URL(imageUrl));
		        
	            image.scalePercent(20f);
	            
	            image.setAbsolutePosition(250f, 715f);
		        
		        Paragraph titleP = new Paragraph("NOME CINEMA", catFont);
		        
		        titleP.setSpacingBefore(80);
		        titleP.setAlignment(Element.ALIGN_CENTER);
		        
		        Paragraph infoCinemaP = new Paragraph("Location cinema\nInfo cinema\nAltre info", smallFont);
		        
		        infoCinemaP.setSpacingBefore(10);
		        infoCinemaP.setAlignment(Element.ALIGN_CENTER);
		        
		        Paragraph FilmP = new Paragraph("TITOLO FILM", subFont);

		        FilmP.setSpacingBefore(40);
		        
		        Paragraph infoFilmP = new Paragraph("Autore:  ?,	\t\t\t\t\t\t\tDurata:  ? h,	\t\t\t\t\t\t\tInfo:  ?", subFont2);
		        
		        Paragraph infoReservationP = new Paragraph("ROOM:  ?,	\t\t\t\t\t\t\tDATA:  ?/?/?,	\t\t\t\t\t\tORA:  ?:?", subFont3);
		        
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
		        
		        Paragraph totalP = new Paragraph("TOTALE:  ? ?", subFont3);
		        
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
		        return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	}

	   /* private static void createTable(Section subCatPart)
	            throws BadElementException {
	        PdfPTable table = new PdfPTable(3);

	        // t.setBorderColor(BaseColor.GRAY);
	        // t.setPadding(4);
	        // t.setSpacing(4);
	        // t.setBorderWidth(1);

	        PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);

	        c1 = new PdfPCell(new Phrase("Table Header 2"));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);

	        c1 = new PdfPCell(new Phrase("Table Header 3"));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);
	        table.setHeaderRows(1);

	        table.addCell("1.0");
	        table.addCell("1.1");
	        table.addCell("1.2");
	        table.addCell("2.1");
	        table.addCell("2.2");
	        table.addCell("2.3");

	        subCatPart.add(table);

	    }

	    private static void createList(Section subCatPart) {
	        List list = new List(true, false, 10);
	        list.add(new ListItem("First point"));
	        list.add(new ListItem("Second point"));
	        list.add(new ListItem("Third point"));
	        subCatPart.add(list);
	    }

	    private static void addEmptyLine(Paragraph paragraph, int number) {
	        for (int i = 0; i < number; i++) {
	            paragraph.add(new Paragraph(" "));
	        }
	    }*/
	    
	// invio per email del documento con le informazioni inerenti la reservation
	public String sendEmail(String email) {
		if (createReport()==false) {
			return "La generazione del report non è andata a buon fine.";
		}
		String to = email;//receiver email
		final String user = "ArmadilloCinema@gmail.com";//sender email (cinema)
		final String password = "xxxxx";//sender password
		   
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
			message.setSubject("RESERVATION N° "+this.progressive+" | TI ASPETTIAMO!");  
		      
			//3) create MimeBodyPart object and set your message text     
			BodyPart messageBodyPart1 = new MimeBodyPart();  
			messageBodyPart1.setText("Benvenuto "+to+ " ,/n"
					+ "In allegato trova il documento che conferma l'avvenuta prenotazione.\n"
					+ "Stampi l'allegato, o porti una prova della ricevuta quando verrà"
					+ "a visionare il film.\n"
					+ "La aspettiamo, buona giornata.");  
		      
			//4) create new MimeBodyPart object and set DataHandler object to this object      
			MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
		  
			String filename = "SendAttachment.java";//change accordingly  
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
	
	//getters della classe
	public ArrayList<Spectator> getSpectators(){
		return spectators;
	}
	
	public Projection getProjection() {
		return projection;
	}
	
	public long getProgressive() {
		return progressive;
	}
	
	public LocalDate getDate() {
		return purchaseDate;
	}
	
	//payment method
	public boolean buy(){
		//Payment simulation
		return paymentCard.decreaseMoney(getTotal());
	}
	
	// IMPLEMENTARE UN METODO PER OCCUPARE/LIBERARE IL POSTO DELLA SALA IN CUI IL FILM CHE HO SCELTO
	// SARA' PROIETTATO
	public boolean takeSeat(PhisicalSeat s) {
		projection.freeSeat(s);
		return projection.freeSeat(s);
	}
	
	public boolean freeSeat(PhisicalSeat s) {
		projection.freeSeat(s);
		return projection.freeSeat(s);
	}
	
}