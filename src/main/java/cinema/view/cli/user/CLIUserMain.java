package cinema.view.cli.user;

import java.time.LocalDate;
import java.time.YearMonth;
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

/**
 * Permette allo spettatore di poter effettuare le operazioni principali
 * (visualizzare i film proiettati, visualizzare le proiezioni dei film,
 * selezionare i posti nella sala, inserire i dati per poter effettuare sconti,
 * pagamento e invio e-mail contenente la ricevuta di prenotazione).
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class CLIUserMain {

	/**
	 * Scanner utilizzato per leggere gli input dal terminale.
	 */
	private Scanner keyboard;

	/**
	 * Controller di dominio utilizzato come interfaccia verso il modello.
	 */
	private Cinema cinema;

	/**
	 * Massimo numero di tentativi di pagamento errati permessi prima di rinunciare.
	 */
	private final int MAX_PAYMENT_ATTEMPTS = 3;

	/**
	 * Separatore delle sezioni dell'output sul terminale.
	 */
	private final String SEPARATOR = "-----------------------------------------------------\n";

	/**
	 * Punto di avvio dell'applicazione di interfaccia con lo spettatore.
	 * 
	 * @param args parametri dell'applicazione (non utilizzati).
	 */
	public static void main(String[] args) {
		new CLIUserMain();
	}

	/**
	 * Costruttore dell'interfaccia utente da riga di comando.
	 * 
	 * Viene proposto un menu numerato contenente varie scelte che lo spettatore può
	 * selezionare per agire sui dati gestiti dall'applicazione.
	 */
	public CLIUserMain() {
		keyboard = new Scanner(System.in);
		cinema = new Cinema();

		// Informazioni generali sul cinema, e messaggio di benvenuto
		printWelcomeMessage();

		// Menu di scelta delle opzioni disponibili
		boolean end = false;
		do {
			System.out.println(SEPARATOR + "\nMenu\n");
			System.out.println("Inserisci il numero corrispondente all'azione che vuoi effettuare:\n\n"
					+ "1) Visualizzare i film disponibili\n2) Acquistare un biglietto\n3) Uscire dall'applicazione\n");
			switch (inputInt("Scelta: ")) {
			case 1:
				printCurrentlyAvailableMovies();
				end = !backToMenu();
				break;
			case 2:
				createReservation();
				end = !backToMenu();
				break;
			case 3:
				System.out.println();
				end = true;
				break;
			default:
				System.out.println("Inserisci il numero corrispondente a una scelta valida.\n");
				end = false;
				break;
			}
		} while (!end);

		sayGoodbye();
	}

	/**
	 * Stampa un messaggio di benvenuto allo spettatore sul terminale.
	 */
	private void printWelcomeMessage() {
		System.out.println(SEPARATOR);
		System.out.println(cinema.getName() + "\n");
		System.out.println(cinema.getLocation());
		System.out.println(cinema.getEmail() + "\n");
		System.out.println("Sviluppato da Screaming Hairy Armadillo Team\n");
		System.out.println("Benvenuto!\n");
	}

	/**
	 * Chiede allo spettatore se vuole tornare al menu principale o uscire
	 * dall'applicazione.
	 *
	 * @return true se l'utente vuole tornare al menu principale, false se vuole
	 *         uscire dall'applicazione.
	 */
	private boolean backToMenu() {
		boolean answer = inputBoolean("Vuoi tornare al menu principale (M) o preferisci uscire (U)? ", "M", "U");
		System.out.println();
		return answer;
	}

	/**
	 * Mostra allo spettatore l'elenco dei film associati ad almeno una proiezione
	 * prevista in futuro e la relativa descrizione breve.
	 */
	private void printCurrentlyAvailableMovies() {
		System.out.println("\n" + SEPARATOR + "\nFilm attualmente proiettati:\n");
		try {
			for (Movie movie : cinema.getCurrentlyAvailableMovies()) {
				System.out.println(movie.getId() + ")");
				System.out.println(movie.getDefaultDescription());
			}
		} catch (PersistenceException exception) {
			System.out.println(exception.getMessage() + "\n");
		}
	}

	/**
	 * Gestisce la procedura di creazione di una nuova prenotazione da parte dello
	 * spettatore.
	 *
	 * Viene mostrato l'elenco dei film disponibili e viene chiesto a quale film lo
	 * spettatore è interessato; a questo punto viene istanziata una nuova
	 * prenotazione e vengono richiesti i relativi dati allo spettatore.
	 *
	 * Una volta terminata la fase di inserimento dati viene avviato il processo di
	 * pagamento e, se quest'ultimo va a buon fine, viene generata la ricevuta di
	 * avvenuta prenotazione e inviata allo spettatore per e-mail.
	 */
	private void createReservation() {
		printCurrentlyAvailableMovies();

		int movie = askMovieId();
		printMovieProjections(movie);

		// Creazione di una nuova prenotazione e inserimento dei relativi dati
		long reservation = -1;
		try {
			reservation = cinema.createReservation();

			// Selezione di una specifica proiezione
			int projection = askProjectionId(movie);
			cinema.setReservationProjection(reservation, projection);

			// Inserimento dati, pagamento e invio della ricevuta allo spettatore
			showProjectionSeats(reservation);
			addSeatsToReservation(reservation);
			insertSpectatorData(reservation);
			insertPaymentCardInfo(reservation);
			insertDiscountData(reservation);
			insertCouponInfo(reservation);
			if (buy(reservation)) {
				sendEmail(reservation);
			}
		} catch (ProjectionException | ReservationException | PersistenceException exception) {
			System.out.println(exception.getMessage() + "\n");
		}
	}

	/**
	 * Chiede allo spettatore di inserire un id valido di un film per il quale c'è
	 * almeno una proiezione disponibile attualmente.
	 *
	 * Se l'id inserito non è valido esso viene chiesto nuovamente.
	 *
	 * @return l'id del film inserito dallo spettatore.
	 */
	private int askMovieId() {
		do {
			int movieId = inputInt("Inserisci il numero del film da guardare: ");
			try {
				cinema.getCurrentlyAvailableProjections(movieId);
				return movieId;
			} catch (NoMovieException | ProjectionException | PersistenceException exception) {
				System.out.println(exception.getMessage() + "\n");
			}
		} while (true);
	}

	/**
	 * Mostra allo spettatore l'elenco di tutte le proiezioni disponibili per il
	 * film con id {@code movieId}.
	 * 
	 * @param movieId id del film di cui mostrare le proiezioni.
	 */
	private void printMovieProjections(int movieId) {
		try {
			cinema.getCurrentlyAvailableProjections(movieId);
			System.out.println("\nMaggiori dettagli sul film selezionato:\n");
			System.out.println(
					cinema.getCurrentlyAvailableProjections(movieId).get(0).getMovie().getDetailedDescription());
			System.out.println("Proiezioni programmate per questo film:\n");
			for (Projection projection : cinema.getCurrentlyAvailableProjections(movieId)) {
				System.out.println(projection.getId() + ")");
				System.out.println(projection.toString());
			}
		} catch (NoMovieException | ProjectionException | PersistenceException exception) {
			System.out.println(exception.getMessage() + "\n");
		}
	}

	/**
	 * Chiede allo spettatore l'id della proiezione per la quale desidera prenotare
	 * dei posti (per il movie con id {@code movieId}).
	 * 
	 * @param movieId id del film associato alle proiezioni possibili tra le quali
	 *                lo spettatore può scegliere.
	 * @return l'id della proiezione scelta dallo spettatore.
	 */
	private int askProjectionId(long movieId) {
		do {
			int projectionId = inputInt("Inserisci il numero della proiezione da prenotare: ");
			try {
				cinema.getCurrentlyAvailableProjection(projectionId);
				if (cinema.getProjectionMovie(projectionId).getId() != movieId) {
					throw new ProjectionException("Inserisci il numero di una proiezione per il film scelto.");
				}
				return projectionId;
			} catch (ProjectionException | PersistenceException exception) {
				System.out.println(exception.getMessage() + "\n");
			}
		} while (true);
	}

	/**
	 * Mostra i posti disponibili all'interno della sala del cinema associata alla
	 * proiezione collegata ad una specifica prenotazione.
	 *
	 * @param reservation id della prenotazione dalla quale ricavare la sala della
	 *                    quale mostrare i posti.
	 */
	private void showProjectionSeats(long reservation) {
		System.out.println("\nDi seguito viene mostrata la disposizione dei posti in sala.");
		System.out.println("I posti segnati con i trattini sono già stati occupati.\n");
		try {
			for (int i = 0; i < cinema.getNumberColsReservationProjection(reservation); i++) {
				if (i == cinema.getNumberColsReservationProjection(reservation) / 2) {
					System.out.print(" SCHERMO ");
				} else {
					System.out.print("---------");
				}
			}
		} catch (ReservationException exception) {
			System.out.println(exception.getMessage() + "\n");
		}
		System.out.println();
		try {
			for (int i = 0; i < cinema.getNumberRowsReservationProjection(reservation); i++) {
				for (int j = 0; j < cinema.getNumberColsReservationProjection(reservation); j++) {
					try {
						if (!cinema.checkIfProjectionSeatIsAvailable(cinema.getReservationProjection(reservation), i,
								j))
							System.out.print(" ------- ");
						else
							System.out.print(String.format(" [ %s%-2d ] ", Room.rowIndexToRowLetter(i), j + 1));
					} catch (RoomException | ProjectionException | PersistenceException exception) {
						System.out.println(exception.getMessage() + "\n");
					}
				}
				System.out.println();
			}
		} catch (ReservationException exception) {
			System.out.println(exception.getMessage() + "\n");
		}
	}

	/**
	 * Permette allo spettatore di effettuare la selezione dei posti della sala
	 * all'interno di una prenotazione e li aggiunge alla prenotazione stessa.
	 *
	 * @param reservation id della prenotazione alla quale associare i posti
	 *                    selezionati dallo spettatore.
	 */
	private void addSeatsToReservation(long reservation) {
		do {
			boolean validSeat = false;
			System.out.println();
			do {
				System.out.print("Inserisci un posto da occupare: ");
				String seat = keyboard.nextLine().toUpperCase();
				int riga = -1, colonna = -1;
				try {
					riga = Room.rowLetterToRowIndex(seat.replaceAll("\\d", ""));
					colonna = Integer.valueOf(seat.replaceAll("[\\D]", "")) - 1;
				} catch (Exception exception) {

				}
				try {
					cinema.addSeatToReservation(reservation, riga, colonna);
					System.out.println();
					validSeat = true;
				} catch (SeatAvailabilityException | RoomException | ReservationException exception) {
					System.out.println("Il posto selezionato risulta occupato o non esiste.\n");
					validSeat = false;
				}
			} while (!validSeat);
		} while (inputBoolean("Vuoi occupare altri posti? (S/N) ", "S", "N"));
	}

	/**
	 * Permette allo spettatore di inserire i suoi dati personali al fine di
	 * associarli alla prenotazione in corso, prima di effettuare l'acquisto.
	 *
	 * @param reservation id della prenotazione nella quale inserire i dati dello
	 *                    spettatore che effettua l'acquisto (un singolo spettatore
	 *                    effettua l'acquisto per ogni prenotazione, anche in caso
	 *                    di gruppi).
	 */
	private void insertSpectatorData(long reservation) {
		System.out.println();
		do {
			System.out.print("Inserisci il tuo nome: ");
			String name = keyboard.nextLine();
			System.out.print("Inserisci il tuo cognome: ");
			String surname = keyboard.nextLine();
			System.out.print("Inserisci la tua email: ");
			String email = keyboard.nextLine();
			try {
				cinema.setReservationPurchaser(reservation, name, surname, email);
				return;
			} catch (InvalidSpectatorInfoException | ReservationException exception) {
				System.out.println(exception.getMessage() + "\n");
			}
		} while (true);
	}

	/**
	 * Permette allo spettatore di inserire i dati di una carta di credito da
	 * utilizzare nella procedura di acquisto della prenotazione.
	 *
	 * @param reservation id della prenotazione nella quale inserire i dati della
	 *                    carta di credito da utilizzare nella procedura di
	 *                    acquisto.
	 */
	private void insertPaymentCardInfo(long reservation) {
		System.out.println();
		String owner = null;
		String number = null;
		YearMonth expirationDate = null;
		boolean end = false;
		do {
			boolean validDate = false;
			do {
				System.out.print("Inserisci nome e cognome del titolare della carta di credito: ");
				owner = keyboard.nextLine();
				System.out.print("Inserisci il numero della carta di credito: ");
				number = keyboard.nextLine();
				expirationDate = null;
				YearMonth currentYearMonth = YearMonth.parse(LocalDate.now().toString().substring(0, 7));
				validDate = false;
				System.out.print("Inserisci la data di scadenza della carta di credito (YYYY-MM): ");
				try {
					expirationDate = YearMonth.parse(keyboard.nextLine());
					if (expirationDate.compareTo(currentYearMonth) >= 0) {
						validDate = true;
					} else {
						throw new Exception();
					}
				} catch (Exception exception) {
					System.out
							.println("La carta di credito inserita è scaduta. Inserisci una nuova carta di credito.\n");
				}
			} while (!validDate);
			System.out.print("Inserisci il CVV della carta di credito: ");
			String cvv = keyboard.nextLine();
			try {
				cinema.setReservationPaymentCard(reservation, number, owner, cvv, expirationDate);
				end = true;
			} catch (ReservationException exception) {
				System.out.println(exception.getMessage() + "\n");
			}
		} while (!end);
	}

	/**
	 * Permette allo spettatore di inserire i dati relativi allo sconto da applicare
	 * alla prenotazione nel caso la tipologia di sconto correntemente applicata dal
	 * cinema sia {@code AGE} (i dati richiesti sono il numero di persone sotto la
	 * soglia minima di età per avere lo sconto e quello delle persone sopra la
	 * soglia massima).
	 *
	 * @param reservation id della prenotazione nella quale inserire i dati relativi
	 *                    allo sconto da applicare alla prenotazione.
	 */
	private void insertDiscountData(long reservation) {
		try {
			if (cinema.getReservationTypeOfDiscount(reservation).equals("AGE")) {
				System.out.println();
				try {
					cinema.setReservationNumberPeopleOverMaxAge(reservation, 0);
					cinema.setReservationNumberPeopleUntilMinAge(reservation, 0);
				} catch (DiscountException | ReservationException exception) {
					System.out.println(exception);
				}
				boolean end = false;
				do {
					int nMin = inputInt("Inserisci il numero di persone di età inferiore a "
							+ cinema.getMinDiscountAge() + " anni: ");
					int nMax = inputInt("Inserisci il numero di persone di età superiore a "
							+ cinema.getMaxDiscountAge() + " anni: ");
					try {
						cinema.setReservationNumberPeopleUntilMinAge(reservation, nMin);
						cinema.setReservationNumberPeopleOverMaxAge(reservation, nMax);
						end = true;
					} catch (DiscountException | NumberFormatException | ReservationException exception) {
						System.out.println(exception.getMessage() + "\n");
					}
				} while (!end);
			}
		} catch (NumberFormatException | ReservationException | PersistenceException exception) {
			System.out.println(exception.getMessage() + "\n");
		}
	}

	/**
	 * Permette allo spettatore di inserire i il codice del coupon (se lo spettatore
	 * ne ha uno) da applicare alla prenotazione.
	 *
	 * Viene controllata la validità del coupon al momento dell'inserimento e viene
	 * richiesto un altro copuon se quello inserito non è valido o è già stato
	 * utilizzato.
	 *
	 * @param reservation id della prenotazione nella quale inserire il codice del
	 *                    coupon da applicare alla prenotazione.
	 */
	private void insertCouponInfo(long reservation) {
		System.out.println();
		if (inputBoolean("Hai un codice coupon erogato dal nostro cinema da applicare al totale? (S/N) ", "S", "N")) {
			do {
				System.out.print("Inserisci il codice coupon: ");
				try {
					cinema.setReservationCoupon(reservation, keyboard.nextLine());
					return;
				} catch (CouponException | ReservationException exception) {
					System.out.println(exception.getMessage() + "\n");
				}
			} while (true);
		}
	}

	/**
	 * Avvia il processo di pagamento per una data prenotazione.
	 *
	 * Vengono effettuati al massimo {@code MAX_PAYMENT_ATTEMPTS} tentativi di
	 * pagamento (in caso i pagamenti fallissero), dopo i quali la funzione termina
	 * segnalando l'errore al chiamante attraverso il valore restituito.
	 *
	 * @param reservation id della prenotazione della quale effettuare il pagamento.
	 * @return true se l'acquisto è stato concluso con successo, false altrimenti.
	 */
	private boolean buy(long reservation) {
		int attempts = 0;
		System.out.println("\n" + SEPARATOR);
		System.out.println("Pagamento:\n");
		do {
			try {
				attempts++;
				System.out.println("Tentativo di transazione in corso...");
				cinema.buyReservation(reservation);
				System.out.print("\nAbbiamo scalato dalla tua carta di credito l'importo di ");
				System.out.print(String.format("%.02f €\n", cinema.getReservationTotalAmount(reservation)));
				System.out.println(
						"Il prezzo comprende sia lo sconto applicato dal nostro cinema in base ai dati inseriti,\n"
								+ "sia lo sconto dell'eventuale coupon applicato (se inserito precedentemente).");
				return true;
			} catch (PaymentErrorException | ReservationException | SeatAvailabilityException | RoomException
					| NumberFormatException | PersistenceException exception) {
				System.out.println(exception.getMessage() + "\n");
			}
		} while (attempts < MAX_PAYMENT_ATTEMPTS);
		System.out.println("Impossibile completare la transazione. Riprova più tardi.");
		return false;
	}

	/**
	 * Invia allo spettatore che ha effettuato l'acquisto l'e-mail contenente la
	 * ricevuta di avvenuta prenotazione e pagamento completato.
	 *
	 * @param reservation id della prenotazione della quale inviare una ricevuta
	 *                    tramite e-mail allo spettatore che ha effettuato
	 *                    l'acquisto.
	 */
	private void sendEmail(long reservation) {
		System.out.println("\n" + SEPARATOR);
		System.out.println("Invio prenotazione per e-mail:\n");
		try {
			cinema.sendReservationEmail(reservation);
			System.out.println("Controlla la tua casella di posta.\n"
					+ "A breve riceverai un'e-mail contenente la ricevuta della tua prenotazione.\n\n"
					+ "Grazie di averci scelto!\n");
		} catch (ReservationException | HandlerException exception) {
			System.out.println(exception.getMessage() + "\n");
		}
	}

	/**
	 * Stampa sul terminale il messaggio di chiusura dell'applicazione.
	 */
	private void sayGoodbye() {
		System.out.println(SEPARATOR + "\nGrazie, a presto!");
	}

	/**
	 * Permette allo spettatore di inserire un numero intero da terminale.
	 *
	 * Viene effettuato un controllo per permettere solamente l'inserimento di un
	 * numero intero (e non altri tipi di dato).
	 *
	 * @param question messaggio da stampare sul terminale prima dell'input dello
	 *                 spettatore.
	 * @return il valore letto.
	 */
	private int inputInt(String question) {
		do {
			System.out.print(question);
			try {
				return Integer.parseInt(keyboard.nextLine());
			} catch (NumberFormatException exception) {
				System.out.println("Inserisci un numero.\n");
			}
		} while (true);
	}

	/**
	 * Permette allo spettatore di effettuare una scelta binaria.
	 *
	 * Viene effettuato un controllo per permettere solamente l'inserimento di una
	 * scelta binaria (S/N, A/B, ...) da parte dello spettatore.
	 *
	 * @param question messaggio da stampare sul terminale prima dell'input dello
	 *                 spettatore.
	 * @param onTrue   stringa corrispondente al valore restituito true.
	 * @param onFalse  stringa corrispondente al valore restituito false.
	 * @return true se l'input utente è onTrue, false se è onFalse.
	 */
	private boolean inputBoolean(String question, String onTrue, String onFalse) {
		do {
			System.out.print(question);
			String choice = keyboard.nextLine().toUpperCase();
			if (choice.equals(onTrue)) {
				return true;
			} else if (choice.equals(onFalse)) {
				return false;
			}
			System.out.println(String.format("Inserisci %s o %s.\n", onTrue, onFalse));
		} while (true);
	}

}
