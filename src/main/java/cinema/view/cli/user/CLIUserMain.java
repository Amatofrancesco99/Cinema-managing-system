package cinema.view.cli.user;

import java.time.YearMonth; 
import java.util.InputMismatchException;
import java.util.Scanner;

import cinema.controller.Cinema;
import cinema.controller.handlers.util.HandlerException;
import cinema.controller.util.NoMovieException;
import cinema.model.Movie;
import cinema.model.cinema.Room;
import cinema.model.cinema.util.RoomException;
import cinema.model.payment.util.PaymentErrorException;
import cinema.model.persistence.util.PersistenceException;
import cinema.model.projection.Projection;
import cinema.model.projection.util.ProjectionException;
import cinema.model.reservation.discount.coupon.util.CouponException;
import cinema.model.reservation.discount.types.util.DiscountException;
import cinema.model.reservation.util.ReservationException;
import cinema.model.reservation.util.SeatAvailabilityException;
import cinema.model.spectator.util.InvalidSpectatorInfoException;


/** BREVE DESCRIZIONE CLASSE CLIMain
 * 
 * @author Screaming Hairy Armadillo Team
 *
 *  Questa classe permette allo spettatore di poter effettuare le operazioni principali 
 *  (visualizzare i film attualmente proiettati, visualizzare le proiezioni dei film,
 *  selezione di posti per la prenotazione della visione in sala, inserimento dati per
 *  poter effettuare sconti, pagamento e invio email da parte del cinema dell'avvenuta
 *  prenotazione con tutte le informazioni utili). Chiaramante la resa grafica della 
 *  WEBGui è molto più elevata, anche se le funzionalità sono le stesse.
 */
public class CLIUserMain {

	static Scanner keyboard = new Scanner(System.in);
	static Cinema myCinema = new Cinema();
	private final String separatore = "-----------------------------------------------------";
	
	/**
	 * METODO Main, per eseguire la nostra CLI
	 * @param args   Parametri in ingresso, nel nostro caso non servono, ne tanto meno
	 * 				 vengono utilizzati.
	 * @throws InvalidNumberPeopleValueException 
	 */
	public static void main(String[] args){
		@SuppressWarnings("unused")
		CLIUserMain cli = new CLIUserMain();
	}
	
	public CLIUserMain() {
		// INFORMAZIONI GENERALI DEL CINEMA E BENVENUTO AL CLIENTE
		printHeader();
			
		//MENU CHE ACCOGLIE IL CLIENTE
		menu();		
	}
	
	
	private void menu() {
		
		boolean end = false;
		
		do {
			
			System.out.println("\nMENU");
			System.out.println("\nCosa vuoi fare: \n\n1) Visualizzare i film disponibili \n2) Acquistare un biglietto \n3) Uscire \n");
			System.out.println("Inserire la scelta: ");
			
			int scelta = Integer.parseInt(keyboard.nextLine());
			
			switch(scelta) {
				
				case 1: printCurrentlyAvailableMovies();
						end = backToMenu();
						break;
				
				case 2: buyTicket();
						break;
						
				default:end = true;
			}
			
		}while(!end);
		
		sayGoodbye();
	}
	
	private void buyTicket() {
		
		// FILM ATTUALMENTE DISPONIBILI/PROIETTATI
		printCurrentlyAvailableMovies();
						
		// FILM DI CUI SI VOGLIONO VEDERE LE PROIEZIONI E MAGGIORI DETTAGLI 
		int movieID = askMovieId();
		printMovieProjections(movieID);
						
		// CREAZIONE DI UNA NUOVA RESERVATION E INSERIMENTO DATI
		printReservationHeader();
		long r = myCinema.createReservation();
		int projectionID = selectProjection();	// Selezione di una specifica proiezione
		setReservationProjection(r, projectionID);
		showProjectionSeats(r);	 	// Disposizione posti in sala e posti già occupati
		addSeatsToReservation(r);	// Selezione di uno/più posto/i
		insertPersonalData(r);		// Inserimento dati anagrafici
		insertPaymentCardInfo(r);	// Inserimento dati pagamento
		insertSpectatorsInfo(r);	// Inserimento informazioni persone insieme al compratore del biglietto
		insertCouponInfo(r);		// Aggiungi un eventuale coupon alla prenotazione
						
		// EFFETTUA PAGAMENTO, SE QUALCOSA VA STORTO INSERISCO DI NUOVO 
		while(!buy(r)) {
			showProjectionSeats(r);	 	
			addSeatsToReservation(r);
			insertPaymentCardInfo(r);	
			insertSpectatorsInfo(r);
		}
						
		// SPEDIZIONE DELL'EMAIL AL CLIENTE, CONTENENTE IL REPORT
		sendEmail(r); 
		
	}
	
	private boolean backToMenu() {
		
		boolean end = true;
		
		do {
			
			System.out.println("\n\nVuoi tornare al menu principale (Y) o preferisci uscire (N)? ");
			String conferma = keyboard.nextLine().toUpperCase();
			if (conferma.contains("N")) {
				return true;
			
			} else if ((!conferma.equals("Y"))&&(!conferma.equals("N"))){
				System.out.println("Scelta non valida...");
				end = false;
			}
			end = true;
			
		}while(!end);
		
		return false;
	}

	private void sayGoodbye() {
		System.out.println("\n\nGrazie, a presto!\n");
		System.out.println(separatore);
	}

	
	private void sendEmail(long r) {
		System.out.println(separatore);
		System.out.println("\nSPEDIZIONE EMAIL \n");
		try {
			myCinema.sendReservationEmail(r);
			System.out.println("Controlla le tue email ricevute, a breve ne riceverai una "
					+ "con allegato un pdf contenente il resoconto della tua prenotazione.\n");
		} catch (ReservationException | HandlerException e) {
			System.out.println(e.getMessage());
		}
	}

	
	private boolean buy(long r){
		boolean end = false;
		System.out.println(separatore);
		System.out.println("\nPAGAMENTO \n");
		while (!end) {
			try {
				myCinema.buyReservation(r);
				System.out.print("Abbiamo scalato dalla tua carta inserita un ammontare pari "
						+ "a: ");
				System.out.print(String.format("%.02f", myCinema.getReservationTotalAmount(r)) + "€ " + "\n");
				System.out.println("Il prezzo mostrato comprende sia lo sconto" 
					   + " applicato dal nostro cinema, in base alle specifiche inserite, sia"
					  + " lo sconto\ndell'eventuale coupon applicato.\n");
				end = true;
			} catch (PaymentErrorException | ReservationException | SeatAvailabilityException | RoomException | NumberFormatException | PersistenceException  e) {
				System.out.println(e.getMessage());
				return false;	
			}
		}
		return true;
	}


	private void insertCouponInfo(long r) {
		boolean end = false;
		System.out.println("\n\n3.4- INSERIMENTO COUPON \n");
		while (!end) {
			// Aggiungi un coupon alla tua prenotazione
			System.out.println("\nVuoi utilizzare un coupon, ottenuto dal nostro cinema, per scontare il totale? (Y/N)");
			String usaCoupon = keyboard.nextLine().toUpperCase();
			if (usaCoupon.equals("Y")) {
				System.out.println("Inserisci il codice del coupon:  ");
				String couponCode = keyboard.nextLine();
				try {
					myCinema.setReservationCoupon(r, couponCode);
					end = true;
				} catch (CouponException | ReservationException e) {
					System.out.println(e.getMessage());
				}
			} else if ((!usaCoupon.equals("Y")) && (!usaCoupon.equals("N"))){
				System.out.println("Scelta non valida...");
			} else if (usaCoupon.equals("N")) {
				end = true;
			}
		}
	}


	private void insertSpectatorsInfo(long r) {
		try {
			if (myCinema.getReservationTypeOfDiscount(r).equals("AGE")) {
				try {
					myCinema.setReservationNumberPeopleOverMaxAge(r, 0);
					myCinema.setReservationNumberPeopleUntilMinAge(r, 0);
				} catch (DiscountException | ReservationException e){
					
				}	
				boolean end = false;
				System.out.println("\n3.3- INSERIMENTO INFORMAZIONI SPETTATORI \n");
				while (!end) {
					// Aggiungi  informazioni di chi viene con te, per poter effettuare eventuali
					// sconti
					System.out.println("Inserisci il numero di persone che hanno un età inferiore a " + (myCinema.getMinDiscountAge()) + " anni: ");
					String n1 = keyboard.nextLine();
					int nMin = Integer.parseInt(n1);
					try {
						myCinema.setReservationNumberPeopleUntilMinAge(r, nMin);
					} catch (DiscountException | NumberFormatException | ReservationException e) {
						System.out.println(e.getMessage());
					}
					System.out.println("Inserisci il numero di persone che hanno un età superiore a " + (myCinema.getMaxDiscountAge()) + " anni: ");
					String n2 = keyboard.nextLine();
					int nMax = Integer.parseInt(n2);
					try {
						myCinema.setReservationNumberPeopleOverMaxAge(r, nMax);
						end = true;
					} catch (DiscountException | NumberFormatException | ReservationException e) {
						System.out.println(e.getMessage());
					}		
				}
			}
		} catch (NumberFormatException | ReservationException e) {
			System.out.println(e.getMessage());
		}
	}


	private void insertPaymentCardInfo(long r) {
		boolean end = false;
		while(!end) {
			System.out.println("\n\n\n3.2- INSERIMENTO DATI PAGAMENTO \n");
			System.out.println("\nInserisci il nome del titolare della carta:  ");
			String owner = keyboard.nextLine();
			System.out.println("\n");
			System.out.println("\nInserisci il numero della carta: ");
			String number = keyboard.nextLine();
			System.out.println("\n");
			YearMonth expirationDate = null;
			boolean validDate = false;
			while(!validDate) {
				System.out.println("\nInserisci la data di scadenza della carta (Anno-Mese): ");
				String expD = keyboard.nextLine();
				System.out.println("\n");
				try {
					expirationDate = YearMonth.parse(expD);
					validDate = true;
				}
				catch(Exception e) {
					System.out.println("La data inserita non è valida.");
				}
			}
			System.out.println("\nInserisci il CVV: ");
			String cvv = keyboard.nextLine();
			System.out.println("\n");
			end = true;
			try {
				myCinema.setReservationPaymentCard(r, number, owner, cvv, expirationDate);
			} catch (ReservationException e) {
				System.out.println(e.getMessage());
			}
		}
	}


	private void insertPersonalData(long r) {
		boolean end = false;
		System.out.println("\n3.1- INSERIMENTO DATI PERSONALI \n");
		while (!end) {
			// NOME, COGNOME, DATA DI NASCITA, EMAIL
			System.out.println("Inserisci il tuo nome:  ");
			String name = "";
			name = keyboard.nextLine();
			System.out.println("\n");
			
			System.out.println("Inserisci il tuo cognome:  ");
			String surname = "";
			surname = keyboard.nextLine();
			System.out.println("\n");
			
			System.out.println("Inserisci la tua email:  ");
			String email = "";
			email = keyboard.nextLine();
			
			try {
				myCinema.setReservationPurchaser(r, name, surname, email);
				end = true;
			} catch (InvalidSpectatorInfoException | ReservationException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	
	private void addSeatsToReservation(long r) {
		boolean end = false;
		do {
			boolean validSeat = false;
			do {
				System.out.println("\nInserisci il posto che vuoi occupare:\n");
				String posto = "";
				posto = keyboard.nextLine();
				System.out.println("\n");
				int riga = -1, colonna = -1;
				try {
					riga = Room.rowLetterToRowIndex(posto.replaceAll("\\d",""));
					colonna = Integer.valueOf(posto.replaceAll("[\\D]", "")) - 1;
				}
				catch (Exception e) {
				}
				try {
					myCinema.addSeatToReservation(r, riga, colonna);
					validSeat = true;
				} catch (SeatAvailabilityException | RoomException | ReservationException e) {
					System.out.println(e.getMessage());
				} 
			} while (!validSeat);
			System.out.println("\nVuoi occupare altri posti? (Y/N):");
			String occupaAltri = keyboard.nextLine().toUpperCase();
			if (occupaAltri.contains("N")) {
				System.out.println("\nFase di occupazione posti terminata.\n\n");
				end = true;
			} else if ((!occupaAltri.equals("Y"))&&(!occupaAltri.equals("N"))){
				System.out.println("Scelta non valida...");
			}
		} while (!end);
	}


	private void showProjectionSeats(long r) {
			System.out.println("\n2- SELEZIONE POSTO/I\n");
			System.out.println("Disposizione sala e posti liberi.");
			System.out.println("I posti segnati con i trattini sono già occupati.\n\n");
			try {
				for (int i = 0; i < myCinema.getNumberColsReservationProjection(r); i++) {
					if (i == myCinema.getNumberColsReservationProjection(r)/2 )
						 System.out.print("  SCHERMO   ");
					else System.out.print("--------");
				}
			} catch (ReservationException e) {
				System.out.println(e.getMessage());
			}
			System.out.println();
			try {
				for (int i = 0; i < myCinema.getNumberRowsReservationProjection(r); i++) {
					for (int j = 0; j < myCinema.getNumberColsReservationProjection(r); j++) {
						try {
							if (!myCinema.checkIfProjectionSeatIsAvailable(myCinema.getReservationProjection(r), i, j))
								System.out.print(" ------ ");
							else 
								System.out.print(" [ " + Room.rowIndexToRowLetter(i) + ( j + 1 ) + " ] ");
						} catch (RoomException | ProjectionException e) {
							System.out.println(e.getMessage());
						}
					}
					System.out.println("");
				}
			} catch (ReservationException e) {
				System.out.println(e.getMessage());
			}
	}


	private void setReservationProjection(long r, int projectionId) {
		System.out.println("\n");
		try {
			myCinema.setReservationProjection(r, projectionId);
		} catch (ProjectionException | ReservationException e) {
			System.out.println(e.getMessage());
		}
	}


	private int selectProjection() {
		boolean end = false;
		int projectionId = 0;
		System.out.println("1- SELEZIONE PROIEZIONE \n");
		while (!end) {
			System.out.println("\nInserisci il numero della proiezione che sei interessato visionare:  ");
			try {
				projectionId = Integer.parseInt(keyboard.nextLine());
				try {
					myCinema.getCurrentlyAvailableProjection(projectionId);
					end = true;
				} catch (ProjectionException e) {
					System.out.println(e.getMessage());
				}
			}
			catch (InputMismatchException | NumberFormatException e){
				System.out.println("\nInserisci solamente caratteri numerici.\n");
			}
		}
		return projectionId;
	}


	private void printReservationHeader() {
		System.out.println(separatore + "\n\n");
		System.out.println("COMPILAZIONE PRENOTAZIONE\n");
	}


	private void printMovieProjections(int movieID) {
		try {
			myCinema.getCurrentlyAvailableProjections(movieID);
			System.out.println("Maggiori dettagli sul film\n");
			System.out.println(myCinema.getCurrentlyAvailableProjections(movieID).get(0).getMovie().getDetailedDescription());
			System.out.println("Proiezioni previste\n");
			for (Projection p : myCinema.getCurrentlyAvailableProjections(movieID)) {
				System.out.println(p.getId() + ")");
				System.out.println(p.toString());
			}
		} catch (NoMovieException | ProjectionException e) {
			System.out.println(e.getMessage());
		}		
	}


	private int askMovieId() {
		boolean end = false;
		int filmId = 0;
		System.out.println("MAGGIORI DETTAGLI FILM E PROIEZIONI\n");
		while (!end) {
			System.out.println("Inserisci il numero del film di cui vuoi vedere maggiori dettagli e le sue"
						+ " relative proiezioni:");
			try {
				filmId = Integer.parseInt(keyboard.nextLine());
				try {
					myCinema.getCurrentlyAvailableProjections(filmId);
					end = true;
				} catch (NoMovieException | ProjectionException e) {
					System.out.println(e.getMessage());
				}
			}
			catch (InputMismatchException | NumberFormatException e){
				System.out.println("\nInserisci solamente caratteri numerici.\n");
			}
			System.out.println("\n");
		}
		return filmId;
	}


	private void printHeader() {
		System.out.println(separatore + "\n");
		System.out.println(myCinema.getName().toUpperCase()+"\n");
		System.out.println("Puoi trovarci in: " + myCinema.getLocation() + "\n");
		System.out.println("Contattaci: " + myCinema.getEmail() + "\n\n");
		System.out.println("Sviluppato da Screaming Hairy Armadillo Team\n");
		System.out.println(separatore + "\n");
	}
	
	
	private void printCurrentlyAvailableMovies() {
		System.out.println("FILM ATTUALMENTE PROIETTATI \n");
		for (Movie m : myCinema.getCurrentlyAvailableMovies()) {
			System.out.println(m.getId() + ")");
			System.out.println(m.getDefaultDescription());
		}
		System.out.println(separatore + "\n");
	}
}