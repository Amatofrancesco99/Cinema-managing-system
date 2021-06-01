package cinema.model.reservation;

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

import cinema.model.Money;
import cinema.model.Projection;
import cinema.model.Spectator;
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
		return true;
	}
	
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