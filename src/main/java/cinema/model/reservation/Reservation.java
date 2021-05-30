package cinema.model.reservation;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cinema.model.Projection;
import cinema.model.Spectator;
import cinema.model.Ticket;
import cinema.model.payment.PaymentCard;

public class Reservation {

	private int progressive;
	private Date purchaseDate;
	private ArrayList<Spectator> spectators;
	private ArrayList<Ticket> tickets;
	private Projection projection;
	private PaymentCard paymentCard;
	
	public Reservation (int id, Date purchaseDate) {
		progressive=id;
		this.purchaseDate=purchaseDate;
		spectators=new ArrayList<Spectator>();
		tickets=new ArrayList<Ticket>();
	}
	
	// aggiungi e rimuovi spettatori dalla Reservation
	public void addSpectator(Spectator s) {
		spectators.add(s);
	}
	public void removeSpectator(Spectator s) {
		spectators.remove(s);
	}
	
	// aggiungi e rimuovi tickets dalla Reservation
	public void addTicket(Ticket t) {
		tickets.add(t);
	}
	public void removeTicket(Ticket t) {
		tickets.remove(t);
	}
	
	// imposta o cambia il metodo di pagamento
	public void setPaymentCard(PaymentCard p) {
		paymentCard=p;
	}
	
	// imposta la proiezione di un film 
	public void setProjection(Projection p) {
		projection=p;
	}
	
	// generazione di un documento con tutte le informazioni della reservation
	public boolean createReport() {
		return true;
	}
	
	// invio per email del documento con le informazioni inerenti la reservation
	public String sendEmail(String email, String password) {
		if (createReport()==false) {
			return "La generazione del report non è andata a buon fine.";
		}
		
		String to = email;
        String from = "noreply@"+"CinemaArmadillo.com"; //our cinema email
        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";
        // Get system properties
        Properties properties = System.getProperties();
        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        // Get the Session object and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(to, password);
            }
        });
        // Used to debug SMTP issues
        session.setDebug(true);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            // Set Subject: header field
            message.setSubject("RICEZIONE REPORT PRENOTAZIONE ID: " + String.valueOf(progressive));
            // Now set the actual message
            message.setText("This is actual message");
            System.out.println("sending...");
            // Send message
            Transport.send(message);
            return "Sent message successfully....";
        } catch (MessagingException mex) {
            mex.printStackTrace();
        	return "Error in sending email";
        }
	}
}