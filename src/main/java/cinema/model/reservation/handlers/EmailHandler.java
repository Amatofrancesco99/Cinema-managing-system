package cinema.model.reservation.handlers;

import java.util.Properties;

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

import cinema.controller.Cinema;
import cinema.model.reservation.Reservation;
import cinema.model.reservation.handlers.util.HandlerException;

/** BREVE DESCRIZIONE CLASSE EmailHandler
 * 
 * @author Screaming HairyArmadillo
 * 
 * Questa classe serve per effettuare l'invio tramite email da parte del cinema, all'utente 
 * che sta prenotando, del report .pdf della prenotazione stessa.
 */
public class EmailHandler {
	
	/**
	 * METODO per effettuare l'invio dell'email
	 * @param r						Prenotazione da inviare
	 * @throws HandlerException 	Eccezione lanciata qualora ci fosse un problema o nella
	 * 								spedizione via email del report, o nella generazione di
	 * 								quest ultimo.
	 */
	public static void sendEmail(Reservation r) throws HandlerException {
		// prima di inviare l'email verifico che il report sia già stato generato, 
		// se non è ancora stato generato lo genero
		if (r.getReportLocation() == null) {
			ReportHandler.createReport(r);
		}
		
		// Stabilire le informazioni sul sender ed il receiver dell'email
		String to = r.getPurchaser().getEmail(); //receiver email
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
			message.setSubject("REPORT PRENOTAZIONE N° " + r.getProgressive() + " | TI ASPETTIAMO!");  
		      
			// Creazione del body della nostra email   
			BodyPart messageBodyPart1 = new MimeBodyPart();  
			messageBodyPart1.setText(
					"SI PREGA DI NON RISPONDERE ALLA SEGUENTE EMAIL.\n\n\n"
					+ "Benvenuto " + r.getPurchaser().getName() + " " + r.getPurchaser().getSurname() + ",\n\n"
					+ "In allegato trovi il documento che conferma l'avvenuta prenotazione.\n"
					+ "Stampa l'allegato, o porta una prova della ricevuta quando verrai"
					+ "a visionare il film.\n\n"
					+ "Ti aspettiamo, buona giornata.\n\n\n");  
		      
			// Aggiungere all'email come allegato il report della prenotazione      
			MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
		  
			String filename = r.getReportLocation();
			DataSource source = new FileDataSource(filename);  
			messageBodyPart2.setDataHandler(new DataHandler(source));  
			messageBodyPart2.setFileName("Reservation_"+Long.toString(r.getProgressive())+".pdf");  
		     
			// Creazione di un campo multipart comprendente body e allegato    
			Multipart multipart = new MimeMultipart();  
			multipart.addBodyPart(messageBodyPart1);  
			multipart.addBodyPart(messageBodyPart2);  
		  
			// Aggiungere al messaggio da inviare (email) body e allegato
			message.setContent(multipart);  
		     
			// Invio dell'email
			Transport.send(message);   
			}
			catch (MessagingException ex) {
				throw new HandlerException("Si è verificato un problema nella spedizione via email del tuo report.");
			}  
		}

}
