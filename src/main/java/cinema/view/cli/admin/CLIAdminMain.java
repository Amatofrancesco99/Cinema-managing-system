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
 * BREVE DESCRIZIONE CLASSE CLIAdminMain
 * 
 * @author Screaming Hairy Armadillo Team
 *
 *         Questa classe permette all'amministratore del cinema di poter
 *         effettuare le operazioni base principali (login, aggiunta/rimozione
 *         prenotazione, modifica delle politiche di sconto).
 */
public class CLIAdminMain {

	private Scanner keyboard;
	private Cinema cinema;
	private final int MAX_PASSWORD_ATTEMPTS = 3;
	private final String SEPARATOR = "-----------------------------------------------------\n";

	public static void main(String[] args) {
		new CLIAdminMain();
	}

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

	private void printWelcomeMessage() {
		System.out.println(SEPARATOR);
		System.out.println(cinema.getName() + "\n");
		System.out.println(cinema.getLocation());
		System.out.println(cinema.getEmail() + "\n");
		System.out.println("Sviluppato da Screaming Hairy Armadillo Team\n");
		System.out.println("Benvenuto nell'interfaccia amministratore di " + cinema.getName() + "!\n");
	}

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

	private boolean backToMenu() {
		boolean answer = inputBoolean("Vuoi tornare al menu principale (M) o preferisci uscire (U)? ", "M", "U");
		System.out.println();
		return answer;
	}

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

	private void changeNewReservationsDiscountStrategy() {
		System.out.println("\n" + SEPARATOR + "\nGestione sconti:\n");
		System.out.print("Strategie di sconto disponibili: ");
		System.out
				.println(cinema.getAllDiscountStrategy().toString().replaceAll("\\[", "").replaceAll("\\]", "") + "\n");
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
	}

	private void insertOrRemoveProjections() {
		System.out.println("\n" + SEPARATOR + "\nInserimento/rimozione proiezioni:\n");
		insertNewProjections();
		removeProjections();
	}

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