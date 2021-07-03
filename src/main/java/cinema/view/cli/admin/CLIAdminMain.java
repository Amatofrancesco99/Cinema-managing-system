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
	private final int MAX_ATTEMPTS = 3;
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
		System.out.println("\nLista di tutte le proiezioni");
		// TODO: change this method with something like :
		// myCinema.getProjections(int thisYear or a limited range)
		for (Projection p : cinema.getProjections()) {
			System.out.println(p.getId() + ") ");
			System.out.println(p.toString());
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
					int p = selectProjectionID();
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

	private void selectProjectionPrice(int p) {
		boolean end = false;
		while (!end) {
			System.out.println("\nInserisci il prezzo della proiezione: ");
			String n = keyboard.nextLine();
			double price = Double.parseDouble(n);
			try {
				cinema.setProjectionPrice(p, price);
				end = true;
			} catch (ProjectionException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private void selectProjectionRoom(int p) {
		System.out.println("\n\nLista delle sale del cinema: ");
		try {
			for (Room r : cinema.getAllRooms())
				System.out.println(r.toString());
		} catch (PersistenceException e) {
			System.out.println(e.getMessage());
		}
		boolean end = false;
		while (!end) {
			System.out.println("\nInserisci il numero della sala da associare alla proiezione: ");
			String n = keyboard.nextLine();
			int roomId = Integer.parseInt(n);
			try {
				cinema.setProjectionRoom(p, roomId);
				end = true;
			} catch (RoomException | ProjectionException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private void selectProjectionMovie(int p) {
		System.out.println("\n\nLista dei film disponibili: ");
		try {
			for (Movie m : cinema.getAllMovies()) {
				System.out.println((m.getId()) + ") ");
				System.out.println(m.getDetailedDescription());
			}
		} catch (PersistenceException e) {
			System.out.println(e.getMessage());
		}
		boolean end = false;
		while (!end) {
			System.out.println("Inserisci l'id del film da associare alla proiezione: ");
			String n = keyboard.nextLine();
			int movieId = Integer.parseInt(n);
			try {
				cinema.setProjectionMovie(p, movieId);
				end = true;
			} catch (NoMovieException | ProjectionException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private int selectProjectionID() {
		boolean end = false;
		int projectionId = -1;
		while (!end) {
			System.out.print("\nInserisci l'id della proiezione da inserire: ");
			try {
				cinema.createProjectionWithID(Integer.parseInt(keyboard.nextLine()));
				System.out.println();
				end = true;
			} catch (ProjectionException exception) {
				System.out.println(exception.getMessage());
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
			if (attempts == MAX_ATTEMPTS) {
				System.out.println("\nNumero massimo di tentativi raggiunto. Riprova più tardi.");
				System.exit(1);
			}
		}
	}

}
