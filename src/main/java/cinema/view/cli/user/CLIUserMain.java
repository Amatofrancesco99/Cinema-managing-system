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
 * BREVE DESCRIZIONE CLASSE CLIMain
 * 
 * @author Screaming Hairy Armadillo Team
 *
 *         Questa classe permette allo spettatore di poter effettuare le
 *         operazioni principali (visualizzare i film attualmente proiettati,
 *         visualizzare le proiezioni dei film, selezione di posti per la
 *         prenotazione della visione in sala, inserimento dati per poter
 *         effettuare sconti, pagamento e invio email da parte del cinema
 *         dell'avvenuta prenotazione con tutte le informazioni utili).
 *         Chiaramante la resa grafica della WEBGui è molto più elevata, anche
 *         se le funzionalità sono le stesse.
 */
public class CLIUserMain {

	private Scanner keyboard;
	private Cinema cinema;
	private final int MAX_PAYMENT_ATTEMPTS = 3;
	private final String SEPARATOR = "-----------------------------------------------------\n";

	/**
	 * METODO Main, per avviare la CLI
	 * 
	 * @param args Parametri in ingresso, nel nostro caso non servono, ne tanto meno
	 *             vengono utilizzati.
	 * @throws InvalidNumberPeopleValueException
	 */
	public static void main(String[] args) {
		new CLIUserMain();
	}

	public CLIUserMain() {
		keyboard = new Scanner(System.in);
		cinema = new Cinema();

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

	private void printWelcomeMessage() {
		System.out.println(SEPARATOR);
		System.out.println(cinema.getName() + "\n");
		System.out.println(cinema.getLocation());
		System.out.println(cinema.getEmail() + "\n");
		System.out.println("Sviluppato da Screaming Hairy Armadillo Team\n");
		System.out.println("Benvenuto!\n");
	}

	private boolean backToMenu() {
		boolean answer = inputBoolean("Vuoi tornare al menu principale (M) o preferisci uscire (U)? ", "M", "U");
		System.out.println();
		return answer;
	}

	private void printCurrentlyAvailableMovies() {
		System.out.println("\n" + SEPARATOR + "\nFilm attualmente proiettati:\n");
		try {
			for (Movie movie : cinema.getCurrentlyAvailableMovies()) {
				System.out.println(movie.getId() + ")");
				System.out.println(movie.getDefaultDescription());
			}
		} catch (PersistenceException exception) {
			System.out.println(exception.getMessage());
		}
	}

	private void createReservation() {
		printCurrentlyAvailableMovies();

		int movie = askMovieId();
		printMovieProjections(movie);

		// Creazione di una nuova prenotazione e inserimento dei relativi dati
		long reservation = cinema.createReservation();

		// Selezione di una specifica proiezione
		int projection = askProjectionId(movie);
		try {
			cinema.setReservationProjection(reservation, projection);
		} catch (ProjectionException | ReservationException | PersistenceException exception) {
			System.out.println(exception.getMessage() + "\n");
		}

		// Inserimento dati, pagamento e invio della ricevuta allo spettatore
		showProjectionSeats(reservation);
		addSeatsToReservation(reservation);
		insertSpectatorData(reservation);
		insertPaymentCardInfo(reservation);
		insertSpectatorsInfo(reservation);
		insertCouponInfo(reservation);
		if (buy(reservation)) {
			sendEmail(reservation);
		}
	}

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
					System.out.println("La carta di credito inserita è scaduta.\nInserisci una nuova carta di credito.\n");
				}
			} while (!validDate);
			System.out.print("Inserisci il CVV della carta di credito: ");
			String cvv = keyboard.nextLine();
			end = true;
			try {
				cinema.setReservationPaymentCard(reservation, number, owner, cvv, expirationDate);
			} catch (ReservationException exception) {
				System.out.println(exception.getMessage() + "\n");
			}
		} while (!end);
	}

	private void insertSpectatorsInfo(long reservation) {
		System.out.println();
		try {
			if (cinema.getReservationTypeOfDiscount(reservation).equals("AGE")) {
				try {
					cinema.setReservationNumberPeopleOverMaxAge(reservation, 0);
					cinema.setReservationNumberPeopleUntilMinAge(reservation, 0);
				} catch (DiscountException | ReservationException exception) {
					// Nessuna eccezione da gestire qui
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

	private void sayGoodbye() {
		System.out.println(SEPARATOR + "\nGrazie, a presto!");
	}

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