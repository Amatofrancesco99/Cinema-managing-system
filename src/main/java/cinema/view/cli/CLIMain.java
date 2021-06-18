package cinema.view.cli;

import java.time.YearMonth;
import java.util.InputMismatchException;
import java.util.Scanner;

import cinema.controller.Cinema;
import cinema.controller.util.NoMovieException;
import cinema.controller.util.NoProjectionException;
import cinema.model.Movie;
import cinema.model.Spectator;
import cinema.model.cinema.Room;
import cinema.model.cinema.util.InvalidRoomSeatCoordinatesException;
import cinema.model.payment.methods.paymentCard.PaymentCard;
import cinema.model.payment.methods.paymentCard.util.ExpiredCreditCardException;
import cinema.model.payment.methods.paymentCard.util.InvalidCCVException;
import cinema.model.payment.methods.paymentCard.util.InvalidCreditCardNumberException;
import cinema.model.payment.util.PaymentErrorException;
import cinema.model.projection.Projection;
import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.coupon.util.CouponAleadyUsedException;
import cinema.model.reservation.discount.coupon.util.CouponNotExistsException;
import cinema.model.reservation.discount.types.util.InvalidNumberPeopleValueException;
import cinema.model.reservation.util.FreeAnotherPersonSeatException;
import cinema.model.reservation.util.ReservationHasNoPaymentCardException;
import cinema.model.reservation.util.ReservationHasNoSeatException;
import cinema.model.reservation.util.SeatAlreadyTakenException;
import cinema.model.reservation.util.SeatTakenTwiceException;


/** BREVE DESCRIZIONE CLASSE CLIMain
 * 
 * @author Screaming Hairy Armadillo Team
 *
 *  Questa classe permette di testare le stesse funzionalità offerte dall'interfaccia
 *  web, ma tramite Command Line Interface (CLI)... chiaramente la resa grafica non sarà
 *  la stessa.
 */
public class CLIMain {

	static Scanner keyboard = new Scanner(System.in);
	
	/**
	 * METODO Main, per eseguire la nostra CLI
	 * @param args   Parametri in ingresso, nel nostro caso non servono, ne tanto meno
	 * 				 vengono utilizzati.
	 */
	public static void main(String[] args) {
		// INFORMAZIONI GENERALI DEL CINEMA E BENVENUTO AL CLIENTE
		printHeader();
				
		// FILM ATTUALMENTE DISPONIBILI/PROIETTATI
		printCurrentlyAvailableMovies();
				
		// FILM DI CUI SI VOGLIONO VEDERE LE PROIEZIONI E MAGGIORI DETTAGLI 
		int movieID = askMovieId();
		printMovieProjections(movieID);
		
		// CREAZIONE DI UNA NUOVA RESERVATION E INSERIMENTO DATI
		printReservationHeader();
		Reservation r = Cinema.getInstance().createReservation();
		int projectionID = selectProjection();	// Selezione di una specifica proiezione
		setReservationProjection(r, projectionID);
		showProjectionSeats(r);	 	// Disposizione posti in sala e posti già occupati
		addSeatsToReservation(r);	// Selezione di uno/più posto/i
		insertPersonalData(r);		// Inserimento dati anagrafici
		insertPaymentCardInfo(r);	// Inserimento dati pagamento
		insertSpectatorsInfo(r);	// Inserimento informazioni persone insieme al compratore del biglietto
		insertCouponInfo(r);		// Aggiungi un eventuale coupon alla prenotazione
		buyAndSendEmail(r); 		// Pagamento e spedizione dell'email al cliente
		
		// SALUTO DEL CLIENTE E TERMINA PROGRAMMA
		sayGoodbye();
	}


	private static void sayGoodbye() {
		System.out.println("\n\nGrazie, a presto!\n");
		System.out.println("-----------------------------------------------------\n");
	}


	private static void buyAndSendEmail(Reservation r) {
		boolean end = false;
		System.out.println("-----------------------------------------------------\n");
		System.out.println("\n4- PAGAMENTO E SPEDIZIONE EMAIL \n");
		while (!end) {
			try {
				r.buy();
				//r.sendEmail();
				System.out.print("\nAbbiamo scalato dalla tua carta inserita un ammontare pari "
						+ "a: ");
				System.out.print(r.getTotal().getAmount() + " " + r.getTotal().getCurrency() + "\n");
				System.out.println("Il prezzo mostrato comprende sia lo sconto" 
					   + " applicato dal nostro cinema, in base alle specifiche inserite, sia"
					  + " lo sconto\ndell'eventuale coupon applicato.");
				System.out.println("\nControlla le tue email ricevute, a breve ne riceverai una "
						+ "con allegato un pdf contenente il resoconto della tua prenotazione.");
				end = true;
			} catch (PaymentErrorException  e) {
				e.toString();
			}
			catch (ReservationHasNoSeatException | ReservationHasNoPaymentCardException | InvalidRoomSeatCoordinatesException e) {
				e.toString();
				end = true;
			} catch (SeatAlreadyTakenException e) {
				System.out.println("\nSiamo spiacenti: ");
				e.toString();
				/* TODO: Scelta se vuole occupare altri posti o se pagare solo i posti
				* che ha prenotato */
				System.out.println("\nInserisci altri posti alla tua prenotazione...");
			}
		}
	}



	private static void insertCouponInfo(Reservation r) {
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
					r.setCoupon(coupon);
					end = true;
				} catch (CouponNotExistsException | CouponAleadyUsedException e) {
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



	private static void insertSpectatorsInfo(Reservation r) {
		boolean end = false;
		System.out.println("\n3.3- INSERIMENTO INFORMAZIONI SPETTATORI \n");
		while (!end) {
			// Aggiungi  informazioni di chi viene con te, per poter effettuare eventuali
			// sconti
			System.out.println("Inserisci il numero di persone che hanno un età inferiore a " + (Cinema.getInstance().getMinDiscountAge()) + " anni: ");
			String n1 = keyboard.next();
			int nMin = Integer.parseInt(n1);
			try {
				r.setNumberPeopleUntilMinAge(nMin);
			} catch (InvalidNumberPeopleValueException | NumberFormatException e) {
				e.toString();
			}
			System.out.println("Inserisci il numero di persone che hanno un età superiore a " + (Cinema.getInstance().getMaxDiscountAge()) + " anni: ");
			String n2 = keyboard.next();
			int nMax = Integer.parseInt(n2);
			try {
				r.setNumberPeopleOverMaxAge(nMax);
				end = true;
			} catch (InvalidNumberPeopleValueException | NumberFormatException e) {
				e.toString();
			}		
		}
	}


	private static void insertPaymentCardInfo(Reservation r) {
		boolean end = false;
		PaymentCard p = new PaymentCard();
		System.out.println("\n\n\n3.2- INSERIMENTO DATI PAGAMENTO \n");
		while (!end) {
			System.out.println("\nInserisci il nome del titolare della carta:  ");
			String owner = keyboard.next();
			System.out.println("\n");
			p.setOwner(owner);
			end = true;
		}
		end = false;
		while (!end) {
			System.out.println("\nInserisci il numero della carta: ");
			String number = keyboard.next();
			System.out.println("\n");
			try {
				p.setNumber(number);
				end = true;
			} catch (InvalidCreditCardNumberException e) {
				e.toString();
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
					p.setExpirationDate(expirationDate);
					end = true;
			} catch (ExpiredCreditCardException e) {
				e.toString();
				System.out.println("...Inserisci una nuova carta...");
				insertPaymentCardInfo(r);
			}
		}
		end = false;
		while (!end) {
			System.out.println("\nInserisci il ccv: ");
			String ccv = keyboard.next();
			System.out.println("\n");
			try {
				p.setCCV(ccv);
				end = true;
			} catch (InvalidCCVException e) {
				e.toString();
			}
		}	
		r.setPaymentCard(p);
	}


	private static void insertPersonalData(Reservation r) {
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
			
			if ((email.equals(""))||(name.equals(""))||(surname.equals(""))) {
				System.err.println("Ops...I tuoi dati anagrafici inseriti sembrano essere mancanti.");
			}
			else {
				r.setPurchaser(new Spectator(name,surname,email));
				end = true;
			}
		}
	}

	private static void addSeatsToReservation(Reservation r) {
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
					r.addSeat(riga, colonna);
					validSeat = true;
				} catch (SeatAlreadyTakenException | InvalidRoomSeatCoordinatesException | SeatTakenTwiceException | FreeAnotherPersonSeatException e) {
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


	private static void showProjectionSeats(Reservation r) {
			System.out.println("\n2- SELEZIONE POSTO/I\n");
			System.out.println("Disposizione sala e posti liberi.");
			System.out.println("I posti segnati con i trattini sono già occupati.\n");
			System.out.println("\n----------------------------------- SCHERMO -----------------------------------");
			for (int i = 0; i < r.getProjection().getRoom().getNumberRows(); i++) {
				for (int j = 0; j < r.getProjection().getRoom().getNumberCols(); j++) {
					try {
						if (!r.getProjection().checkIfSeatIsAvailable(i, j)){
							System.out.print(" ------ ");
						}
						else 
							System.out.print(" [ " + Room.rowIndexToRowLetter(i) + ( j + 1 ) + " ] ");
					} catch (InvalidRoomSeatCoordinatesException e) {
				  }
				}
				System.out.println("");
			}
	}


	private static void setReservationProjection(Reservation r, int projectionId) {
		System.out.println("\n");
		try {
			r.setProjection(Cinema.getInstance().getProjection(projectionId));
		} catch (NoProjectionException e) {
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
					Cinema.getInstance().getProjection(projectionId);
					end = true;
				} catch (NoProjectionException e) {
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
			Cinema.getInstance().getProjections(movieID);
			System.out.println("Maggiori dettagli sul film\n");
			System.out.println(Cinema.getInstance().getProjections(movieID).get(0).getMovie().getDetailedDescription());
			System.out.println("Proiezioni previste\n");
			for (Projection p : Cinema.getInstance().getProjections(movieID)) {
				System.out.println(p.getId() + ")");
				System.out.println(p.toString());
			}
		} catch (NoMovieException e) {
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
					Cinema.getInstance().getProjections(filmId);
					end = true;
				} catch (NoMovieException e) {
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
		System.out.println(Cinema.getInstance().getName().toUpperCase()+"\n");
		System.out.println("Puoi trovarci in: " + Cinema.getInstance().getLocation() + "\n");
		System.out.println("Contattaci: " + Cinema.getInstance().getEmail() + "\n\n");
		System.out.println("Sviluppato da Screaming Hairy Armadillo Team\n");
		System.out.println("-----------------------------------------------------\n");
	}
	
	
	private static void printCurrentlyAvailableMovies() {
		System.out.println("FILM ATTUALMENTE PROIETTATI \n");
		for (Movie m : Cinema.getInstance().getCurrentlyAvailableMovies()) {
			System.out.println(m.getId() + ")");
			System.out.println(m.getDefaultDescription());
		}
		System.out.println("-----------------------------------------------------\n");
	}
}