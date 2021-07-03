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
		welcomeAndLogin();

		// Cambio della password
		changePassword();

		// Selezione della strategia di sconto da applicare alle prossime prenotazioni
		changeNewReservationsDiscountStrategy();

		// Inserimento di nuove proiezioni
		insertOrRemoveProjections();

		// Terminazione dell'applicazione
		System.out.println("\nA presto!");
	}

	private void printWelcomeMessage() {
		System.out.println(SEPARATOR);
		System.out.println(cinema.getName() + "\n");
		System.out.println(cinema.getLocation());
		System.out.println(cinema.getEmail() + "\n");
		System.out.println("Sviluppato da Screaming Hairy Armadillo Team\n");
		System.out.println(SEPARATOR);
	}

	private void insertOrRemoveProjections() {
		System.out.println(SEPARATOR);
		System.out.println("Inserimento/rimozione proiezioni\n");
		insertNewProjections();
		removeProjections();
	}

	private void removeProjections() {
		boolean end = false;
		while (!end) {
			System.out.print("Vuoi rimuovere delle proiezioni esistenti? (Y/N) ");
			String choice = keyboard.nextLine();
			if (choice.toUpperCase().equals("N")) {
				end = true;
			} else if (choice.toUpperCase().equals("Y")) {
				boolean removeEnd = false;
				while (!removeEnd) {
					showAllProjections();
					System.out.print("Inserisci il numero della proiezione che vuoi rimuovere: ");
					int projectionId = Integer.parseInt(keyboard.nextLine());
					try {
						cinema.removeProjection(projectionId);
					} catch (ProjectionException exception) {
						System.out.println(exception.getMessage() + "\n");
					}
					System.out.println("\nVuoi rimuovere altre proiezioni? (Y/N) ");
					String c = keyboard.nextLine();
					if (c.toUpperCase().equals("N")) {
						removeEnd = true;
						end = true;
					} else if (!c.toUpperCase().equals("N") && !choice.toUpperCase().equals("Y")) {
						System.out.println("Scelta non valida.\n");
					}
				}
			} else {
				System.out.println("Scelta non valida.\n");
			}
		}
	}

	private void showAllProjections() {
		System.out.println("\nLista di tutte le proiezioni esistenti:");
		for (Projection projection : cinema.getProjections()) {
			System.out.println(projection.getId() + ") ");
			System.out.println(projection.toString());
		}
	}

	private void insertNewProjections() {
		boolean end = false;
		while (!end) {
			System.out.print("Vuoi inserire nuove proiezioni? (Y/N) ");
			String choice = keyboard.nextLine();
			if (choice.toUpperCase().equals("N")) {
				end = true;
			} else if (choice.toUpperCase().equals("Y")) {
				int projection = insertProjectionId();
				selectProjectionMovie(projection);
				selectProjectionRoom(projection);
				selectProjectionDateTime(projection);
				selectProjectionPrice(projection);
			} else {
				System.out.println("Scelta non valida.\n");
			}
		}
	}

	private void selectProjectionDateTime(int projection) {
		boolean end = false;
		LocalDateTime projectionDateTime = null;
		while (!end) {
			System.out.print("\nInserisci la data della proiezione (DD-MM-YYYY): ");
			String date = keyboard.nextLine();
			String[] tokens1 = date.split("-");
			int year = Integer.parseInt(tokens1[2]);
			int month = Integer.parseInt(tokens1[1]);
			int dayOfMonth = Integer.parseInt(tokens1[0]);
			System.out.print("\nInserisci l'ora della proiezione (HH:MM): ");
			String time = keyboard.nextLine();
			String[] tokens2 = time.split(":");
			int hour = Integer.parseInt(tokens2[0]);
			int minute = Integer.parseInt(tokens2[1]);
			try {
				projectionDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute);
			} catch (Exception exception) {

			}
			try {
				if (projectionDateTime != null) {
					cinema.setProjectionDateTime(projection, projectionDateTime);
					System.out.println();
					end = true;
				}
			} catch (ProjectionException exception) {
				System.out.println(exception.getMessage() + "\n");
			}
		}
	}

	private void selectProjectionPrice(int projection) {
		boolean end = false;
		while (!end) {
			System.out.print("Inserisci il prezzo della proiezione (EUR): ");
			try {
				cinema.setProjectionPrice(projection, Double.parseDouble(keyboard.nextLine().replaceAll(",", ".")));
				System.out.println();
				end = true;
			} catch (ProjectionException exception) {
				System.out.println(exception.getMessage() + "\n");
			}
		}
	}

	private void selectProjectionRoom(int projection) {
		System.out.println("Lista delle sale del cinema:");
		try {
			for (Room room : cinema.getAllRooms()) {
				System.out.println(room.toString());
			}
		} catch (PersistenceException exception) {
			System.out.println(exception.getMessage() + "\n");
		}
		boolean end = false;
		while (!end) {
			System.out.print("\nInserisci il numero della sala da associare alla proiezione: ");
			try {
				cinema.setProjectionRoom(projection, Integer.parseInt(keyboard.nextLine()));
				end = true;
			} catch (RoomException | ProjectionException exception) {
				System.out.println(exception.getMessage() + "\n");
			}
		}
	}

	private void selectProjectionMovie(int projection) {
		System.out.println("Lista dei film disponibili:");
		try {
			for (Movie movie : cinema.getAllMovies()) {
				System.out.println((movie.getId()) + ")");
				System.out.println(movie.getDetailedDescription());
			}
		} catch (PersistenceException exception) {
			System.out.println(exception.getMessage() + "\n");
		}
		boolean end = false;
		while (!end) {
			System.out.print("Inserisci l'ID del film da associare alla proiezione: ");
			try {
				cinema.setProjectionMovie(projection, Integer.parseInt(keyboard.nextLine()));
				System.out.println();
				end = true;
			} catch (NoMovieException | ProjectionException exception) {
				System.out.println(exception.getMessage() + "\n");
			}
		}
	}

	private int insertProjectionId() {
		boolean end = false;
		int projectionId = -1;
		while (!end) {
			System.out.print("\nInserisci l'ID della proiezione da aggiungere: ");
			projectionId = Integer.parseInt(keyboard.nextLine());
			try {
				cinema.createProjectionWithID(projectionId);
				System.out.println();
				end = true;
			} catch (ProjectionException exception) {
				System.out.println(exception.getMessage() + "\n");
			}
		}
		return projectionId;
	}

	private void changeNewReservationsDiscountStrategy() {
		System.out.println(SEPARATOR);
		System.out.println("Gestione degli sconti\n");
		boolean end = false;
		while (!end) {
			System.out.print("Vuoi cambiare la strategia di sconto delle prossime prenotazioni? (Y/N) ");
			String choice = keyboard.nextLine().toUpperCase();
			if (choice.equals("N")) {
				System.out.println();
				end = true;
			} else if (choice.equals("Y")) {
				System.out.print("\nStrategie disponibili: ");
				System.out.println(cinema.getAllDiscountStrategy());
				boolean changeEnd = false;
				while (!changeEnd) {
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
							changeEnd = true;
							end = true;
						}
					}
					if (!changeEnd) {
						System.out.println("Scelta non valida.\n");
					}
				}
			} else {
				System.out.println("Scelta non valida.\n");
			}
		}
	}

	private void changePassword() {
		System.out.println(SEPARATOR);
		System.out.println("Gestione password\n");
		boolean end = false;
		while (!end) {
			System.out.print("Vuoi cambiare la password? (Y/N) ");
			String choice = keyboard.nextLine().toUpperCase();
			if (choice.equals("N")) {
				System.out.println();
				end = true;
			} else if (choice.equals("Y")) {
				System.out.println();
				boolean changePasswordEnd = false;
				while (!changePasswordEnd) {
					System.out.print("Inserisci la nuova password: ");
					String newPassword = keyboard.nextLine();
					try {
						cinema.setPassword(newPassword);
						System.out.println("\nPassword aggiornata con successo.\n");
						changePasswordEnd = true;
						end = true;
					} catch (PasswordException exception) {
						System.out.println(exception.getMessage() + "\n");
					}
				}
			} else {
				System.out.println("Scelta non valida.\n");
			}
		}
	}

	private void welcomeAndLogin() {
		System.out.println(SEPARATOR);
		System.out.println("Benvenuto nell'interfaccia amministratore di " + cinema.getName());
		boolean end = false;
		int attempts = 0;
		while (!end) {
			System.out.print("\nInserisci la password (admin): ");
			String password = keyboard.nextLine();
			try {
				cinema.login(password);
				System.out.println();
				end = true;
			} catch (PasswordException exception) {
				attempts++;
				System.out.println(exception.getMessage());
			}
			if (attempts == MAX_PASSWORD_ATTEMPTS) {
				System.out.println("\nNumero massimo di tentativi raggiunto. Riprova più tardi.");
				System.exit(1);
			}
		}
	}

}
