package cinema.view.cli.admin;

import java.time.LocalDateTime;
import java.util.Scanner;

import cinema.controller.Cinema;
import cinema.controller.util.DiscountNotFoundException;
import cinema.controller.util.NoMovieException;
import cinema.controller.util.PasswordException;
import cinema.model.Movie;
import cinema.model.cinema.Room;
import cinema.model.cinema.util.RoomException;
import cinema.model.persistence.util.PersistenceException;
import cinema.model.projection.Projection;
import cinema.model.projection.util.ProjectionException;

/**
 * Permette all'amministratore del cinema di poter effettuare le operazioni base
 * principali (login, aggiunta/rimozione di prenotazioni, modifica delle
 * politiche di sconto).
 * 
 * @author Screaming Hairy Armadillo Team
 * 
 */
public class CLIAdminMain {

	/**
	 * Scanner utilizzato per leggere gli input dal terminale.
	 */
	private Scanner keyboard;

	/**
	 * Controller di dominio utilizzato come interfaccia verso il modello.
	 */
	private Cinema cinema;

	/**
	 * Massimo numero di tentativi di login errati permessi prima di chiudere
	 * l'applicazione.
	 */
	private final int MAX_PASSWORD_ATTEMPTS = 3;

	/**
	 * Separatore delle sezioni dell'output sul terminale.
	 */
	private final String SEPARATOR = "-----------------------------------------------------\n";

	/**
	 * Avvia l'interfaccia testuale riservata all'amministratore del sistema.
	 *
	 * @param args Parametri dell'applicazione (non utilizzati).
	 */
	public static void main(String[] args) {
		new CLIAdminMain();
	}

	/**
	 * Costruttore dell'interfaccia utente da riga di comando.
	 * 
	 * Viene proposto un menu numerato contenente varie scelte che l'amministratore
	 * può selezionare per agire sui dati gestiti dall'applicazione.
	 */
	public CLIAdminMain() {
		keyboard = new Scanner(System.in);
		cinema = new Cinema();

		// Informazioni generali sul cinema, messaggio di benvenuto e login
		printWelcomeMessage();
		login();

		// Menu di scelta delle opzioni disponibili
		boolean end = true;
		do {
			System.out.println(SEPARATOR + "\nMenu\n");
			System.out.println("Inserisci il numero corrispondente all'azione che vuoi effettuare:\n\n"
					+ "1) Reimpostare la password\n2) Cambiare il tipo di discount applicato alle prenotazioni future\n"
					+ "3) Inserire/rimuovere proiezioni\n4) Uscire dall'applicazione\n");
			switch (inputInt("Scelta: ")) {
			case 1:
				changePassword();
				end = !backToMenu();
				break;
			case 2:
				changeNewReservationsDiscountStrategy();
				end = !backToMenu();
				break;
			case 3:
				insertOrRemoveProjections();
				end = !backToMenu();
				break;
			case 4:
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
	 * Stampa un messaggio di benvenuto all'amministratore sul terminale.
	 */
	private void printWelcomeMessage() {
		System.out.println(SEPARATOR);
		System.out.println(cinema.getName() + "\n");
		System.out.println(cinema.getLocation());
		System.out.println(cinema.getEmail() + "\n");
		System.out.println("Sviluppato da Screaming Hairy Armadillo Team\n");
		System.out.println("Benvenuto nell'interfaccia amministratore di " + cinema.getName() + "!\n");
	}

	/**
	 * Permette all'amministratore di autenticarsi attraverso la passowrd.
	 * 
	 * Se viene raggiunto il numero massimo ti tentativi di login falliti
	 * l'applicazione viene chiusa.
	 */
	private void login() {
		System.out.println(SEPARATOR);
		boolean end = false;
		int attempts = 0;
		do {
			System.out.print("Inserisci la password (admin): ");
			try {
				cinema.login(keyboard.nextLine());
				System.out.println();
				end = true;
			} catch (PasswordException exception) {
				attempts++;
				System.out.println(exception.getMessage() + "\n");
			}
			if (attempts == MAX_PASSWORD_ATTEMPTS) {
				System.out.println("\nNumero massimo di tentativi raggiunto. Riprova più tardi.");
				System.exit(1);
			}
		} while (!end);
	}

	/**
	 * Chiede all'amministratore se vuole tornare al menu principale o uscire
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
	 * Permette all'amministratore di cambiare la passowrd di login.
	 *
	 * Se la password non rispetta i requisiti minimi di lunghezza essa viene
	 * richiesta.
	 */
	private void changePassword() {
		System.out.println("\n" + SEPARATOR + "\nGestione password:\n");
		do {
			System.out.print("Inserisci la nuova password: ");
			try {
				cinema.setPassword(keyboard.nextLine());
				System.out.println("\nPassword aggiornata con successo.\n");
				return;
			} catch (PasswordException exception) {
				System.out.println(exception.getMessage() + "\n");
			}
		} while (true);
	}

	/**
	 * Permette all'amministratore di cambiare la strategia di discount applicata
	 * alle prenotazioni future tra quelle disponibili.
	 * 
	 * Se viene inserita una strategia inesistente viene chiesto di effettuare una
	 * nuova selezione della scelta.
	 */
	private void changeNewReservationsDiscountStrategy() {
		System.out.println("\n" + SEPARATOR + "\nGestione sconti:\n");
		System.out.print("Strategie di sconto disponibili: ");
		try {
			System.out.println(
					cinema.getAllDiscountStrategy().toString().replaceAll("\\[", "").replaceAll("\\]", "") + "\n");
			do {
				System.out.print("Inserisci una tra le strategie disponibili: ");
				String newStrategyName = keyboard.nextLine().toUpperCase();
				for (int i = 0; i < cinema.getAllDiscountStrategy().size(); i++) {
					if (newStrategyName.equals(cinema.getAllDiscountStrategy().get(i).toString())) {
						try {
							cinema.setCinemaDiscountStrategy(cinema.getAllDiscountStrategy().get(i));
							System.out.println("\nStrategia aggiornata con successo.\n");
						} catch (DiscountNotFoundException exception) {
							System.out.println(exception.getMessage() + "\n");
						}
						return;
					}
				}
				System.out.println("Scelta non valida.\n");
			} while (true);
		} catch (PersistenceException exception) {
			System.out.println(exception.getMessage());
		}
	}

	/**
	 * Permette all'amministratore di inserire o rimuovere proiezioni.
	 */
	private void insertOrRemoveProjections() {
		System.out.println("\n" + SEPARATOR + "\nInserimento/rimozione proiezioni:\n");
		insertNewProjections();
		removeProjections();
	}

	/**
	 * Permette all'amministratore di inserire nuove proiezioni per uno specifico
	 * film.
	 */
	private void insertNewProjections() {
		while (inputBoolean("Vuoi inserire nuove proiezioni? (S/N) ", "S", "N")) {
			int projection = insertProjectionId();
			selectProjectionMovie(projection);
			selectProjectionRoom(projection);
			selectProjectionDateTime(projection);
			selectProjectionPrice(projection);
			try {
				cinema.putNewProjectionIntoDb();
			} catch (PersistenceException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * Permette all'amministratore di inserire l'ID di una nuova proiezione da
	 * aggiungere al programma.
	 *
	 * @return l'ID della nuova proiezione.
	 */
	private int insertProjectionId() {
		System.out.println();
		int projectionId = -1;
		do {
			projectionId = inputInt("Inserisci l'ID della proiezione da aggiungere: ");
			try {
				cinema.createProjectionWithID(projectionId);
				System.out.println();
				return projectionId;
			} catch (ProjectionException | PersistenceException exception) {
				System.out.println(exception.getMessage() + "\n");
			}
		} while (true);
	}

	/**
	 * Permette all'amministratore di scegliere a quale film associare la nuova
	 * proiezione.
	 *
	 * @param projection proiezione di cui impostare il film.
	 */
	private void selectProjectionMovie(int projection) {
		System.out.println("Lista dei film disponibili:\n");
		try {
			for (Movie movie : cinema.getAllMovies()) {
				System.out.println((movie.getId()) + ")");
				System.out.println(movie.getDetailedDescription());
			}
		} catch (PersistenceException exception) {
			System.out.println(exception.getMessage() + "\n");
		}
		do {
			try {
				cinema.setProjectionMovie(projection,
						inputInt("Inserisci l'ID del film da associare alla proiezione: "));
				System.out.println();
				return;
			} catch (NoMovieException exception) {
				System.out.println(exception.getMessage() + "\n");
			}
		} while (true);
	}

	/**
	 * Permette all'amministratore di scegliere in quale sala programmare la nuova
	 * proiezione.
	 *
	 * @param projection proiezione di cui impostare la sala.
	 */
	private void selectProjectionRoom(int projection) {
		System.out.println("Lista delle sale del cinema:\n");
		try {
			for (Room room : cinema.getAllRooms()) {
				System.out.println(room.toString());
			}
		} catch (PersistenceException exception) {
			System.out.println(exception.getMessage() + "\n");
		}
		System.out.println();
		do {
			try {
				cinema.setProjectionRoom(projection,
						inputInt("Inserisci il numero della sala da associare alla proiezione: "));
				return;
			} catch (RoomException exception) {
				System.out.println(exception.getMessage() + "\n");
			}
		} while (true);
	}

	/**
	 * Permette all'amministratore di scegliere in quale data e a quale ora
	 * programmare la nuova proiezione.
	 *
	 * @param projection proiezione di cui impostare la data e l'ora.
	 */
	private void selectProjectionDateTime(int projection) {
		System.out.println();
		LocalDateTime projectionDateTime;
		do {
			projectionDateTime = null;
			try {
				System.out.print("Inserisci la data della proiezione (DD-MM-YYYY): ");
				String date = keyboard.nextLine();
				String[] tokens1 = date.split("-");
				int year = Integer.parseInt(tokens1[2]);
				int month = Integer.parseInt(tokens1[1]);
				int dayOfMonth = Integer.parseInt(tokens1[0]);
				System.out.print("Inserisci l'ora della proiezione (HH:MM): ");
				String time = keyboard.nextLine();
				String[] tokens2 = time.split(":");
				int hour = Integer.parseInt(tokens2[0]);
				int minute = Integer.parseInt(tokens2[1]);
				projectionDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute);
			} catch (Exception exception) {
				// Nessuna eccezione da gestire
			}
			try {
				if (projectionDateTime != null) {
					cinema.setProjectionDateTime(projection, projectionDateTime);
					System.out.println();
					return;
				} else {
					throw new ProjectionException("Data non valida.");
				}
			} catch (ProjectionException exception) {
				System.out.println(exception.getMessage() + "\n");
			}
		} while (true);
	}

	/**
	 * Permette all'amministratore di scegliere il costo della nuova proiezione.
	 *
	 * @param projection proiezione di cui impostare il costo.
	 */
	private void selectProjectionPrice(int projection) {
		do {
			System.out.print("Inserisci il prezzo della proiezione (EUR): ");
			try {
				try {
					cinema.setProjectionPrice(projection, Double.parseDouble(keyboard.nextLine().replaceAll(",", ".")));
				} catch (NumberFormatException exception) {
					throw new ProjectionException("Prezzo non valido.");
				}
				System.out.println();
				return;
			} catch (ProjectionException exception) {
				System.out.println(exception.getMessage() + "\n");
			}
		} while (true);
	}

	/**
	 * Permette all'amministratore di rimuovere una o più proiezioni in base all'ID.
	 */
	private void removeProjections() {
		while (inputBoolean("Vuoi rimuovere delle proiezioni esistenti? (S/N) ", "S", "N")) {
			showAllProjections();
			boolean end = false;
			do {
				int projectionId = inputInt("Inserisci il numero della proiezione che vuoi rimuovere: ");
				try {
					cinema.removeProjection(projectionId);
					System.out.println();
					end = true;
				} catch (ProjectionException | PersistenceException exception) {
					System.out.println(exception.getMessage() + "\n");
				}
			} while (!end);
		}
		System.out.println();
	}

	/**
	 * Stampa sul terminale tutte le proiezioni esistenti.
	 */
	private void showAllProjections() {
		System.out.println("\nLista di tutte le proiezioni esistenti:\n");
		try {
			for (Projection projection : cinema.getProjections()) {
				System.out.println(projection.getId() + ") ");
				System.out.println(projection.toString());
			}
		} catch (PersistenceException exception) {
			System.out.println(exception.getMessage());
		}
	}

	/**
	 * Stampa sul terminale il messaggio di chiusura dell'applicazione.
	 */
	private void sayGoodbye() {
		System.out.println(SEPARATOR + "\nGrazie, a presto!");
	}

	/**
	 * Permette all'amministratore di inserire un numero intero da terminale.
	 *
	 * Viene effettuato un controllo per permettere solamente l'inserimento di un
	 * numero intero (e non altri tipi di dato).
	 *
	 * @param question messaggio da stampare sul terminale prima dell'input
	 *                 dell'amministratore.
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
	 * Permette all'amministratore di effettuare una scelta binaria.
	 *
	 * Viene effettuato un controllo per permettere solamente l'inserimento di una
	 * scelta binaria (S/N, A/B, ...) da parte dell'amministratore.
	 *
	 * @param question messaggio da stampare sul terminale prima dell'input
	 *                 dell'amministratore.
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
