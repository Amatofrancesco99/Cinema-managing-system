package cinema.view.cli.user;

import java.time.YearMonth;
import java.util.InputMismatchException;
import java.util.Scanner;

import cinema.controller.Cinema;
import cinema.controller.util.MovieNoLongerProjectedException;
import cinema.controller.util.NoMovieException;
import cinema.controller.util.NoProjectionException;
import cinema.controller.util.ProjectionIsNoLongerProjectedException;
import cinema.controller.util.ReservationNotExistsException;
import cinema.model.Movie;
import cinema.model.cinema.Room;
import cinema.model.cinema.util.InvalidRoomSeatCoordinatesException;
import cinema.model.payment.methods.paymentCard.PaymentCard;
import cinema.model.payment.methods.paymentCard.util.PaymentCardException;
import cinema.model.payment.util.PaymentErrorException;
import cinema.model.projection.Projection;
import cinema.model.reservation.discount.coupon.util.CouponAleadyUsedException;
import cinema.model.reservation.discount.coupon.util.CouponNotExistsException;
import cinema.model.reservation.discount.types.util.InvalidNumberPeopleValueException;
import cinema.model.reservation.util.FreeAnotherPersonSeatException;
import cinema.model.spectator.util.InvalidSpectatorInfoException;
import cinema.model.reservation.util.ReservationHasNoPaymentCardException;
import cinema.model.reservation.util.ReservationHasNoSeatException;
import cinema.model.reservation.util.SeatAlreadyTakenException;
import cinema.model.reservation.util.SeatTakenTwiceException;


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
	
	/**
	 * METODO Main, per eseguire la nostra CLI
	 * @param args   Parametri in ingresso, nel nostro caso non servono, ne tanto meno
	 * 				 vengono utilizzati.
	 * @throws InvalidNumberPeopleValueException 
	 */
	public static void main(String[] args){
		// INFORMAZIONI GENERALI DEL CINEMA E BENVENUTO AL CLIENTE
		printHeader();
				
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
		
		// EFFETTUA PAGAMENTO
		buy(r); 
		
		// SPEDIZIONE DELL'EMAIL AL CLIENTE, CONTENENTE IL REPORT
		sendEmail(r); 			    
		
		// SALUTO DEL CLIENTE E TERMINA PROGRAMMA
		sayGoodbye();
	}


	private static void sayGoodbye() {
		System.out.println("\n\nGrazie, a presto!\n");
		System.out.println("-----------------------------------------------------");
	}

	
	private static void sendEmail(long r) {
		System.out.println("-----------------------------------------------------\n");
		System.out.println("\nSPEDIZIONE EMAIL \n");
		//myCinema.sendAnEmail(r);
		System.out.println("Controlla le tue email ricevute, a breve ne riceverai una "
				+ "con allegato un pdf contenente il resoconto della tua prenotazione.\n");
	}

	
	private static void buy(long r){
		boolean end = false;
		boolean error = false;
		System.out.println("-----------------------------------------------------\n");
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
			} catch (PaymentErrorException  e) {
				e.toString();
			}
			catch (ReservationHasNoSeatException | ReservationHasNoPaymentCardException | InvalidRoomSeatCoordinatesException e) {
				e.toString();
				end = true;
			} catch (SeatAlreadyTakenException e) {
				e.toString();
				error = true;
			} catch (NumberFormatException | ReservationNotExistsException e) {
				e.toString();
			} 
		}
		if(error) {
			System.out.println("\n\nInserisci altri posti alla tua prenotazione...");
			showProjectionSeats(r);
			addSeatsToReservation(r);
			buy(r);
			insertSpectatorsInfo(r);
		}
	}


	private static void insertCouponInfo(long r) {
		boolean end = false;
		System.out.println("\n\n3.4- INSERIMENTO COUPON \n");
		while (!end) {
			// Aggiungi un coupon alla tua prenotazione
			System.out.println("\nVuoi utilizzare un coupon, ottenuto dal nostro cinema, per scontare il totale? (Y/N)");
			String usaCoupon = keyboard.next();
			if (usaCoupon.equals("Y")) {
				System.out.println("Inserisci il codice del coupon:  ");
				String couponId = keyboard.next();
				long coupon = (long) Integer.valueOf(couponId.replaceAll("[\\D]", ""));
				try {
					myCinema.setReservationCoupon(r, coupon);
					end = true;
				} catch (CouponNotExistsException | CouponAleadyUsedException | ReservationNotExistsException e) {
					e.toString();
				}
			}
			if ((!usaCoupon.equals("Y"))&&(!usaCoupon.equals("N"))){
				System.out.println("Scelta non valida...");
			}
			if (usaCoupon.equals("N")) {
				end = true;
			}
		}
	}


	private static void insertSpectatorsInfo(long r) {
		try {
			myCinema.setReservationNumberPeopleOverMaxAge(r, 0);
			myCinema.setReservationNumberPeopleUntilMinAge(r, 0);
		} catch (InvalidNumberPeopleValueException | ReservationNotExistsException e1){}	
		boolean end = false;
		System.out.println("\n3.3- INSERIMENTO INFORMAZIONI SPETTATORI \n");
		while (!end) {
			// Aggiungi  informazioni di chi viene con te, per poter effettuare eventuali
			// sconti
			System.out.println("Inserisci il numero di persone che hanno un età inferiore a " + (myCinema.getMinDiscountAge()) + " anni: ");
			String n1 = keyboard.next();
			int nMin = Integer.parseInt(n1);
			try {
				myCinema.setReservationNumberPeopleUntilMinAge(r, nMin);
			} catch (InvalidNumberPeopleValueException | NumberFormatException | ReservationNotExistsException e) {
				e.toString();
			}
			System.out.println("Inserisci il numero di persone che hanno un età superiore a " + (myCinema.getMaxDiscountAge()) + " anni: ");
			String n2 = keyboard.next();
			int nMax = Integer.parseInt(n2);
			try {
				myCinema.setReservationNumberPeopleOverMaxAge(r, nMax);
				end = true;
			} catch (InvalidNumberPeopleValueException | NumberFormatException | ReservationNotExistsException e) {
				e.toString();
			}		
		}
	}


	private static void insertPaymentCardInfo(long r) {
		boolean end = false;
		PaymentCard p = new PaymentCard();
		System.out.println("\n\n\n3.2- INSERIMENTO DATI PAGAMENTO \n");
		while (!end) {
			System.out.println("\nInserisci il nome del titolare della carta:  ");
			String owner = keyboard.next();
			System.out.println("\n");
			myCinema.setPaymentCardOwner(p, owner);
			end = true;
		}
		end = false;
		while (!end) {
			System.out.println("\nInserisci il numero della carta: ");
			String number = keyboard.next();
			System.out.println("\n");
			try {
				myCinema.setPaymentCardNumber(p, number);
				end = true;
			} catch (PaymentCardException e) {
				System.out.println(e.getMessage());
			}
		}
		end = false;
		while (!end) {
			System.out.println("\nInserisci la data di scadenza della carta (Anno-Mese): ");
			String expD = keyboard.next();
			System.out.println("\n");
			YearMonth expirationDate = null;
			try {
				expirationDate = YearMonth.parse(expD);
			}
			catch(Exception e) {}
			try {
				if (expirationDate != null)
					myCinema.setPaymentCardExpirationDate(p, expirationDate);
					end = true;
			} catch (PaymentCardException e) {
				System.out.println(e.getMessage());
				insertPaymentCardInfo(r);
			}
		}
		end = false;
		while (!end) {
			System.out.println("\nInserisci il cvv: ");
			String cvv = keyboard.next();
			System.out.println("\n");
			try {
				myCinema.setPaymentCardCvv(p, cvv);
				end = true;
			} catch (PaymentCardException e) {
				System.out.println(e.getMessage());
			}
		}	
		try {
			myCinema.setReservationPaymentCard(r, p);
		} catch (ReservationNotExistsException e) {
			e.toString();
		}
	}


	private static void insertPersonalData(long r) {
		boolean end = false;
		System.out.println("\n3.1- INSERIMENTO DATI PERSONALI \n");
		while (!end) {
			// NOME, COGNOME, DATA DI NASCITA, EMAIL
			System.out.println("Inserisci il tuo nome:  ");
			String name = "";
			name = keyboard.next();
			System.out.println("\n");
			
			System.out.println("Inserisci il tuo cognome:  ");
			String surname = "";
			surname = keyboard.next();
			System.out.println("\n");
			
			System.out.println("Inserisci la tua email:  ");
			String email = "";
			email = keyboard.next();
			
			try {
				myCinema.setReservationPurchaser(r, name, surname, email);
				end = true;
			} catch (InvalidSpectatorInfoException | ReservationNotExistsException e) {
				e.toString();
			}
		}
	}

	
	private static void addSeatsToReservation(long r) {
		boolean end = false;
		do {
			boolean validSeat = false;
			do {
				System.out.println("\nInserisci il posto che vuoi occupare:\n");
				String posto = "";
				posto = keyboard.next();
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
				} catch (SeatAlreadyTakenException | InvalidRoomSeatCoordinatesException | SeatTakenTwiceException | FreeAnotherPersonSeatException | ReservationNotExistsException e) {
					e.toString();
				} 
			} while (!validSeat);
			System.out.println("\nVuoi occupare altri posti? (Y/N):");
			String occupaAltri = keyboard.next();
			if (occupaAltri.contains("N")) {
				System.out.println("\nFase di occupazione posti terminata.\n\n");
				end = true;
			}
			if ((!occupaAltri.equals("Y"))&&(!occupaAltri.equals("N"))){
				System.out.println("Scelta non valida...");
			}
		} while (!end);
	}


	private static void showProjectionSeats(long r) {
			System.out.println("\n2- SELEZIONE POSTO/I\n");
			System.out.println("Disposizione sala e posti liberi.");
			System.out.println("I posti segnati con i trattini sono già occupati.\n");
			System.out.println("\n----------------------------------- SCHERMO -----------------------------------");
			try {
				for (int i = 0; i < myCinema.getNumberRowsReservationProjection(r); i++) {
					for (int j = 0; j < myCinema.getNumberColsReservationProjection(r); j++) {
						try {
							if (!myCinema.checkIfReservationProjectionSeatIsAvailable(r, i, j)){
								System.out.print(" ------ ");
							}
							else 
								System.out.print(" [ " + Room.rowIndexToRowLetter(i) + ( j + 1 ) + " ] ");
						} catch (InvalidRoomSeatCoordinatesException e) {
					  }
					}
					System.out.println("");
				}
			} catch (ReservationNotExistsException e) {}
	}


	private static void setReservationProjection(long r, int projectionId) {
		System.out.println("\n");
		try {
			myCinema.setReservationProjection(r, projectionId);
		} catch (NoProjectionException | ReservationNotExistsException e) {
			e.toString();
		}
	}


	private static int selectProjection() {
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
				} catch (NoProjectionException | ProjectionIsNoLongerProjectedException e) {
					e.toString();
				}
			}
			catch (InputMismatchException | NumberFormatException e){
				System.err.println("\nInserisci solamente caratteri numerici.\n");
			}
		}
		return projectionId;
	}


	private static void printReservationHeader() {
		System.out.println("-----------------------------------------------------\n\n");
		System.out.println("COMPILAZIONE PRENOTAZIONE\n");
	}


	private static void printMovieProjections(int movieID) {
		try {
			myCinema.getCurrentlyAvailableProjections(movieID);
			System.out.println("Maggiori dettagli sul film\n");
			System.out.println(myCinema.getCurrentlyAvailableProjections(movieID).get(0).getMovie().getDetailedDescription());
			System.out.println("Proiezioni previste\n");
			for (Projection p : myCinema.getCurrentlyAvailableProjections(movieID)) {
				System.out.println(p.getId() + ")");
				System.out.println(p.toString());
			}
		} catch (NoMovieException | MovieNoLongerProjectedException e) {
			e.toString(); 
		}		
	}


	private static int askMovieId() {
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
				} catch (NoMovieException | MovieNoLongerProjectedException e) {
					e.toString();
				}
			}
			catch (InputMismatchException | NumberFormatException e){
				System.err.println("\nInserisci solamente caratteri numerici.\n");
			}
			System.out.println("\n");
		}
		return filmId;
	}


	private static void printHeader() {
		System.out.println("-----------------------------------------------------\n");
		System.out.println(Cinema.getName().toUpperCase()+"\n");
		System.out.println("Puoi trovarci in: " + Cinema.getLocation() + "\n");
		System.out.println("Contattaci: " + Cinema.getEmail() + "\n\n");
		System.out.println("Sviluppato da Screaming Hairy Armadillo Team\n");
		System.out.println("-----------------------------------------------------\n");
	}
	
	
	private static void printCurrentlyAvailableMovies() {
		System.out.println("FILM ATTUALMENTE PROIETTATI \n");
		for (Movie m : myCinema.getCurrentlyAvailableMovies()) {
			System.out.println(m.getId() + ")");
			System.out.println(m.getDefaultDescription());
		}
		System.out.println("-----------------------------------------------------\n");
	}
}