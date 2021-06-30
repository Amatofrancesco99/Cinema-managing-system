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
		   
		// Configurazione delle proprietà dell'email
		Properties properties = setUpMainProperties(user, password);

	    // Generazione di una nuova sessione mail
	    Session session = startNewSession(user,password,properties);
		   
	    //Tentativo di composizione del messaggio ed invio dell'email  
	    createMessageAndSendEmail(session,user,to,r);
	}

	
	/** METODO per effettuare la creazione del messaggio da inviare e l'invio dell'email
	 * @throws HandlerException */
	private static void createMessageAndSendEmail(Session session, String user, String to, Reservation r) throws HandlerException {
		try{  
			// Configurazione proprietà basilari dell'email
			Message message = createBasicMailProperties(session,user,to,r);
		     
			// Creazione del body dell'email
			BodyPart messageBodyPart1 = createMailBody(r);
		      
			// Aggiungere all'email come allegato il report della prenotazione      
			MimeBodyPart messageBodyPart2 = createMailReport(r);
		     
			// Creazione di un campo multipart comprendente body e allegato    
			addBodyAndReportToMail(message, messageBodyPart1, messageBodyPart2);  
		     
			// Invio dell'email
			Transport.send(message);   
			}
			catch (MessagingException ex) {
				throw new HandlerException("Si è verificato un problema nella spedizione via email del tuo report.");
			}  
	}

	
	/** METODO per aggiungere all'email il body ed il report in allegato
	 * 
	 * @param message
	 * @param messageBodyPart1
	 * @param messageBodyPart2
	 * @throws MessagingException
	 */
	private static void addBodyAndReportToMail(Message message, BodyPart messageBodyPart1, MimeBodyPart messageBodyPart2) throws MessagingException {
		Multipart multipart = new MimeMultipart();  
		multipart.addBodyPart(messageBodyPart1);  
		multipart.addBodyPart(messageBodyPart2);  
		message.setContent(multipart);
	}


	/** METODO per creare l'allegato per l'email
	 * 
	 * @param r
	 * @return
	 * @throws MessagingException
	 */
	private static MimeBodyPart createMailReport(Reservation r) throws MessagingException {
		MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
		String filename = r.getReportLocation();
		DataSource source = new FileDataSource(filename);  
		messageBodyPart2.setDataHandler(new DataHandler(source));  
		messageBodyPart2.setFileName("Reservation_"+Long.toString(r.getProgressive())+".pdf"); 
		return messageBodyPart2;
	}


	/** METODO per creare il testo principale (body) dell'email
	 * 
	 * @param r
	 * @throws MessagingException
	 */
	private static BodyPart createMailBody(Reservation r) throws MessagingException { 
		BodyPart messageBodyPart1 = new MimeBodyPart();  
		messageBodyPart1.setText(
				"SI PREGA DI NON RISPONDERE ALLA SEGUENTE EMAIL.\n\n\n"
				+ "Benvenuto " + r.getPurchaser().getName() + " " + r.getPurchaser().getSurname() + ",\n\n"
				+ "In allegato trovi il documento che conferma l'avvenuta prenotazione.\n"
				+ "Stampa l'allegato, o porta una prova della ricevuta quando verrai"
				+ " a visionare il film.\n\n"
			    + "Ti aspettiamo, buona giornata.\n\n\n");
		return messageBodyPart1;
	}


	/** METODO per impostare le proprietà basilari dell'email
	 * 
	 * @param session
	 * @param user
	 * @param to
	 * @param r
	 * @return
	 * @throws MessagingException
	 */
	private static Message createBasicMailProperties(Session session, String user, String to, Reservation r) throws MessagingException {
		MimeMessage message = new MimeMessage(session);  
		message.setFrom(new InternetAddress(user));  
		message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
		message.setSubject("REPORT PRENOTAZIONE N° " + r.getProgressive() + " | TI ASPETTIAMO!");  
		return message;
	}


	/** METODO per generare una nuova sessione mail
	 * 
	 * @param user
	 * @param password
	 * @param properties
	 * @return
	 */
	private static Session startNewSession(String user, String password, Properties properties) {
		 Session session = Session.getDefaultInstance(properties,  
				new javax.mail.Authenticator() {  
				protected PasswordAuthentication getPasswordAuthentication() {  
						return new PasswordAuthentication(user,password);  
					}  
				});
		 return session;
	}

	
	/** METODO per impostare le proprietà dell'email
	 * 
	 * @param user
	 * @param password
	 * @return
	 */
	private static Properties setUpMainProperties(String user, String password) {
		Properties properties = System.getProperties();  
		String host = "smtp.gmail.com";
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.user", user);
		properties.put("mail.smtp.password", password);
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		return properties;
	}
}