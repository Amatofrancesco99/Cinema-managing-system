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
 *         prenotazione, modificate politiche scontistiche).
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
		System.out.println(SEPARATOR);
		System.out.println(cinema.getName());
		System.out.println("Sviluppato da Screaming Hairy Armadillo Team\n");
		welcomeAndLogin();

		// Cambio della password
		changePassword();

		// Selezione della strategia di sconto da applicare alle prossime prenotazioni
		changeNewReservationsDiscountStrategy();

		// Inserimento di nuove proiezioni
		insertOrRemoveProjections();

		// Terminazione dell'applicazione
		System.out.println("\n\nA presto!");
	}

	private void insertOrRemoveProjections() {
		System.out.println(SEPARATOR);
		System.out.println("Inserimento/rimozione proiezioni\n");
		insertNewProjections();
		removeProjections();
	}

	private void removeProjections() {
		System.out.println("\nVuoi rimuovere delle proiezioni esistenti? (Y/N)");
		boolean end = false;
		while (!end) {
			String choice = keyboard.nextLine();
			if (choice.toUpperCase().equals("N")) {
				end = true;
			}
			if (!choice.toUpperCase().equals("N") && (!choice.toUpperCase().equals("Y"))) {
				System.out.println("Scelta non valida...\n");
			}
			if (choice.toUpperCase().equals("Y")) {
				boolean removeEnd = false;
				while (!removeEnd) {
					showAllProjections();
					System.out.println("Inserisci il numero della proiezione che vuoi rimuovere: ");
					int projectionId = Integer.parseInt(keyboard.nextLine());
					try {
						cinema.removeProjection(projectionId);
					} catch (ProjectionException e) {
						System.out.println(e.getMessage());
					}
					System.out.println("\n\nVuoi rimuovere altre proiezioni? (Y/N) ");
					String c = keyboard.nextLine();
					if (c.toUpperCase().equals("N")) {
						removeEnd = true;
						end = true;
					}
					if (!c.toUpperCase().equals("N") && (!choice.toUpperCase().equals("Y"))) {
						System.out.println("Scelta non valida...\n");
					}
					if (c.toUpperCase().equals("Y")) {
					}
				}
			}
		}
	}

	private void showAllProjections() {
		System.out.println("Lista di tutte le proiezioni esistenti:");
		for (Projection projection : cinema.getProjections()) {
			System.out.println(projection.getId() + ") ");
			System.out.println(projection.toString() + "\n");
		}
	}

	private void insertNewProjections() {
		System.out.println("Vuoi inserire nuove proiezioni? (Y/N)");
		boolean end = false;
		while (!end) {
			String choice = keyboard.nextLine();
			if (choice.toUpperCase().equals("N")) {
				end = true;
			}
			if (!choice.toUpperCase().equals("N") && (!choice.toUpperCase().equals("Y"))) {
				System.out.println("Scelta non valida...\n");
			}
			if (choice.toUpperCase().equals("Y")) {
				boolean insertingEnd = false;
				while (!insertingEnd) {
					int p = insertProjectionID();
					selectProjectionMovie(p);
					selectProjectionRoom(p);
					selectProjectionDateTime(p);
					selectProjectionPrice(p);
					insertingEnd = true;
					System.out.println("\n\nVuoi inserire nuove proiezioni? (Y/N) ");
					String c = keyboard.nextLine();
					if (c.toUpperCase().equals("N")) {
						insertingEnd = true;
						end = true;
					}
					if (!c.toUpperCase().equals("N") && (!choice.toUpperCase().equals("Y"))) {
						System.out.println("Scelta non valida...\n");
					}
					if (c.toUpperCase().equals("Y")) {
					}
				}
			}
		}
	}

	private void selectProjectionDateTime(int p) {
		boolean end = false;
		LocalDateTime projectionDateTime = null;
		while (!end) {
			System.out.println("\nInserisci la data della proiezione: (DD-MM-YYYY)");
			String date = keyboard.nextLine();
			String[] parts1 = date.split("-");
			int year = Integer.parseInt(parts1[2]);
			int month = Integer.parseInt(parts1[1]);
			int dayOfMonth = Integer.parseInt(parts1[0]);
			System.out.println("\nInserisci l'ora della proiezione: (HH:MM)");
			String time = keyboard.nextLine();
			String[] parts2 = time.split(":");
			int hour = Integer.parseInt(parts2[0]);
			int minute = Integer.parseInt(parts2[1]);
			try {
				projectionDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute);
			} catch (Exception e) {

			}
			try {
				if (projectionDateTime != null) {
					cinema.setProjectionDateTime(p, projectionDateTime);
					end = true;
				}
			} catch (ProjectionException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private void selectProjectionPrice(int projection) {
		boolean end = false;
		while (!end) {
			System.out.println("Inserisci il prezzo della proiezione: ");
			try {
				cinema.setProjectionPrice(projection, Double.parseDouble(keyboard.nextLine()));
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
				System.out.println(room.toString() + "\n");
			}
		} catch (PersistenceException exception) {
			System.out.println(exception.getMessage() + "\n");
		}
		boolean end = false;
		while (!end) {
			System.out.println("Inserisci il numero della sala da associare alla proiezione: ");
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
				System.out.println(movie.getDetailedDescription() + "\n");
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

	private int insertProjectionID() {
		boolean end = false;
		int projectionId = -1;
		while (!end) {
			System.out.print("\nInserisci l'ID della proiezione da inserire: ");
			try {
				cinema.createProjectionWithID(Integer.parseInt(keyboard.nextLine()));
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
			System.out.print("Vuoi cambiare la strategia di sconto delle prossime prenotazioni? (Y/N): ");
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
			System.out.print("Vuoi cambiare la password? (Y/N): ");
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
