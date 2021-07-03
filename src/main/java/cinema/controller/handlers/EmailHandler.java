package cinema.controller.handlers;

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

import cinema.model.reservation.Reservation;
import cinema.controller.handlers.util.HandlerException;


/** BREVE DESCRIZIONE CLASSE EmailHandler
 * 
 * @author Screaming HairyArmadillo
 * 
 * Questa classe serve per effettuare l'invio tramite email da parte del cinema, all'utente 
 * che sta prenotando, del report .pdf della prenotazione stessa.
 */
public class EmailHandler {
	
	/** 
	 * ATTRIBUTI
	 * @param email
	 * @param password
	 * @param name
	 */
	private String email;
	private String password;
	private String name;
	private String location;
	private String logoURL;
	
	/**
	 * COSTRUTTORE
	 * @param name
	 * @param email
	 * @param password
	 * @param location
	 * @param logo URL
	 */
	public EmailHandler (String name, String email, String password, String location, String logoURL) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.location = location;
		this.logoURL = logoURL;
	}
	
	/**
	 * METODO per effettuare l'invio dell'email
	 * @param r						Prenotazione da inviare
	 * @throws HandlerException 	Eccezione lanciata qualora ci fosse un problema o nella
	 * 								spedizione via email del report, o nella generazione di
	 * 								quest ultimo.
	 */
	public void sendEmail(Reservation r) throws HandlerException {
		ReportHandler reportHandler = new ReportHandler(name, email, location, logoURL);
	    Thread emailThread = new Thread() {
			@Override
			public void run() {
				try {
					// prima di inviare l'email verifico che il report sia già stato generato, 
					// se non è ancora stato generato lo genero
					if (r.getReportLocation() == null) {
						reportHandler.createReport(r);
					}
					
					// Stabilire le informazioni sul sender ed il receiver dell'email
					String to = r.getPurchaser().getEmail(); //receiver email
					String user = email; //sender email (cinema)
					   
					// Configurazione delle proprietà dell'email
					Properties properties = setUpMainProperties(user, password);

				    // Generazione di una nuova sessione mail
				    Session session = startNewSession(user,password,properties);
					   
				    //Tentativo di composizione del messaggio ed invio dell'email  
				    createMessageAndSendEmail(session,user,to,r);
				} catch (HandlerException exception) {
					// If an error occurred during the sending process, it is not handled (the
					// spectator will notify the cinema to fix the issue)
				}
			}
		};
		emailThread.start();
	}

	
	/** METODO per effettuare la creazione del messaggio da inviare e l'invio dell'email
	 * @throws HandlerException */
	private void createMessageAndSendEmail(Session session, String user, String to, Reservation r) throws HandlerException {
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
	private void addBodyAndReportToMail(Message message, BodyPart messageBodyPart1, MimeBodyPart messageBodyPart2) throws MessagingException {
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
	private MimeBodyPart createMailReport(Reservation r) throws MessagingException {
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
	private BodyPart createMailBody(Reservation r) throws MessagingException { 
		BodyPart messageBodyPart1 = new MimeBodyPart();  
		messageBodyPart1.setText(
				"Ciao " + r.getPurchaser().getName() + " " + r.getPurchaser().getSurname() + ",\n\n"
				+ "grazie per aver scelto il nostro Cinema!\n\n\n"
				+ "In allegato trovi la ricevuta di avvenuto pagamento che conferma il tuo acquisto.\n"
				+ "Stampa la prenotazione e presentala quando verrai a guardare il film.\n\n"
			    + "Ti aspettiamo, buona giornata!\n"
				+ name + "\n\n"
			    + "Non rispondere alla presente e-mail. Messaggio generato automaticamente.\n");
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
	private Message createBasicMailProperties(Session session, String user, String to, Reservation r) throws MessagingException {
		MimeMessage message = new MimeMessage(session);  
		message.setFrom(new InternetAddress(user));  
		message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
		message.setSubject("Prenotazione numero " + r.getProgressive() + " - Ti aspettiamo!");  
		return message;
	}


	/** METODO per generare una nuova sessione mail
	 * 
	 * @param user
	 * @param password
	 * @param properties
	 * @return
	 */
	private Session startNewSession(String user, String password, Properties properties) {
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
	private Properties setUpMainProperties(String user, String password) {
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