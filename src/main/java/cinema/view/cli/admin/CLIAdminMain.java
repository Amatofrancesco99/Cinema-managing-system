package cinema.view.cli.admin;

import java.time.LocalDateTime;
import java.util.Scanner;

import cinema.controller.Cinema;
import cinema.controller.util.DiscountNotFoundException;
import cinema.controller.util.NoMovieException;
import cinema.controller.util.NoProjectionException;
import cinema.controller.util.PasswordTooShortException;
import cinema.controller.util.ProjectionIDAlreadyUsedException;
import cinema.controller.util.RoomNotExistsException;
import cinema.controller.util.WrongAdminPasswordException;
import cinema.model.Movie;
import cinema.model.cinema.Room;
import cinema.model.projection.Projection;
import cinema.model.projection.util.InvalidPriceException;
import cinema.model.projection.util.InvalidProjectionDateTimeException;
import cinema.model.projection.util.InvalidProjectionIdException;


/** BREVE DESCRIZIONE CLASSE CLIAdminMain
 * 
 * @author Screaming Hairy Armadillo Team
 *
 *  Questa classe permette all'amministratore del cinema di poter effettuare le 
 *  operazioni base principali (login, aggiunta/rimozione prenotazione, modificate politiche
 *  scontistiche).
 */
public class CLIAdminMain {

	static Scanner keyboard = new Scanner(System.in);
	static Cinema myCinema = new Cinema();
	final static int MAX_ATTEMPTS = 3;
	
	public static void main(String[] args) {
		// INFORMAZIONI GENERALI SUL CINEMA
		printHeader();
		
		// SALUTO ALL'ADMIN E LOGIN
		welcomeAndLogin();
		
		// CAMBIO PASSWORD (OPZIONALE)
		changePassword();
		
		// SELEZIONE STRATEGIA APPLICABILE (OPZIONALE)
		changeNewReservationsDiscountStrategy();
		
		// INSERIMENTO NUOVE PROIEZIONI (OPZIONALE)
		insertOrRemoveProjections();
		
		// SALUTO DELL'ADMIN E TERMINA PROGRAMMA
		sayGoodbye();
	}

	
	private static void sayGoodbye() {
		System.out.println("\n\nGrazie, a presto!");
		System.out.println("-----------------------------------------------------");
	}

	
	private static void insertOrRemoveProjections() {
		System.out.println("-----------------------------------------------------\n");
		System.out.println("INSERIMENTO/RIMOZIONE PROIEZIONI\n");
		insertNewProjections();
		removeProjections();
	}

	
	private static void removeProjections() {
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
						myCinema.removeProjection(myCinema.getProjection(projectionId));
					} catch (NoProjectionException e) {
						e.toString();
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


	private static void showAllProjections() {
		System.out.println("\nLista di tutte le proiezioni");
		// TODO: change this method with something like :   
		//    	 myCinema.getProjections(int thisYear or a limited range)
		for (Projection p : myCinema.getProjections()) {
			System.out.println(p.getId() + ") ");
			System.out.println(p.toString());
		}
	}


	private static void insertNewProjections() {
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
					Projection p = new Projection();
					selectProjectionID(p);
					selectProjectionMovie(p);
					selectProjectionRoom(p);
					selectProjectionDateTime(p);
					selectProjectionPrice(p);
					myCinema.addProjection(p);
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

	
	private static void selectProjectionDateTime(Projection p) {
		boolean end = false;
		LocalDateTime projectionDateTime = null;
		while (!end) {
			System.out.println("\nInserisci la data della proiezione: (DD-MM-YYYY)");
			String date = keyboard.next();
			String[] parts1 = date.split("-");
			int year = Integer.parseInt(parts1[2]);
			int month = Integer.parseInt(parts1[1]);
			int dayOfMonth = Integer.parseInt(parts1[0]);
			System.out.println("\nInserisci l'ora della proiezione: (HH:MM)");
			String time = keyboard.next();
			String[] parts2 = time.split(":");
			int hour = Integer.parseInt(parts2[0]);
			int minute = Integer.parseInt(parts2[1]);
			try {
				projectionDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute);
			} catch (Exception e) {}
			try {
				if (projectionDateTime != null) {
					myCinema.setProjectionDateTime(p,projectionDateTime);
					end = true;
				}
			} catch (InvalidProjectionDateTimeException e) {
				e.toString();
			}
		}
	}


	private static void selectProjectionPrice(Projection p) {
		boolean end = false;
		while(!end) {
			System.out.println("\nInserisci il prezzo della proiezione: ");
			String n = keyboard.next();
			double price = Double.parseDouble(n);
			try {
				myCinema.setProjectionPrice(p,price);
				end = true;
			} catch (InvalidPriceException e) {
				e.toString();
			}
		}
	}


	private static void selectProjectionRoom(Projection p) {
		System.out.println("\n\nLista delle sale del cinema: ");
		for (Room r : myCinema.getAllRooms())
			System.out.println(r.toString());
		boolean end = false;
		while(!end) {
			System.out.println("\nInserisci il numero della sala da associare alla proiezione: ");
			String n = keyboard.next();
			int roomId = Integer.parseInt(n);
			try {
				myCinema.setProjectionRoom(p,roomId);
				end = true;
			} catch (RoomNotExistsException e) {
				e.toString();
			}
		}
	}


	private static void selectProjectionMovie(Projection p) {
		System.out.println("\n\nLista dei film disponibili: ");
		// TODO: change this method with something like:
		// 		 myCinema.getAllMovies(int thisYear or a limited range)
		for (Movie m: myCinema.getCurrentlyAvailableMovies()) {
			System.out.println((m.getId()) + ") ");
			System.out.println(m.getDetailedDescription());
		}
		boolean end = false;
		while(!end) {
			System.out.println("Inserisci l'id del film da associare alla proiezione: ");
			String n = keyboard.next();
			int movieId = Integer.parseInt(n);
			try {
				myCinema.setProjectionMovie(p,movieId);
				end = true;
			} catch (NoMovieException e) {
				e.toString();
			}
		}
	}

	
	private static void selectProjectionID(Projection p) {
		boolean end = false;
		while (!end) {
			System.out.println("\nInserisci l'id della proiezione da inserire: ");
			String n = keyboard.next();
			int projectionId = Integer.parseInt(n);
			try {
				myCinema.setProjectionID(p, projectionId);
				end = true;
			} catch (ProjectionIDAlreadyUsedException | InvalidProjectionIdException e) {
				e.toString();
			}
		}
	}

	
	private static void changeNewReservationsDiscountStrategy() {
		System.out.println("-----------------------------------------------------\n");
		System.out.println("GESTIONE SCONTI\n");
		System.out.println("Vuoi cambiare la strategia di sconto delle prossime\nprenotazioni"
				+ " effettuate? (Y/N)");
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
				System.out.println("Selezione disponibile: ");
				System.out.println(myCinema.getAllDiscountStrategy());
				boolean changeEnd = false;
				while (!changeEnd) {
					System.out.println("Inserisci una tra queste strategie: ");
					String newStrategyName = keyboard.nextLine();
					for (int i = 0; i < myCinema.getAllDiscountStrategy().size(); i++) {
						if (newStrategyName.toUpperCase().equals(myCinema.getAllDiscountStrategy().get(i).toString())) {
							try {
								myCinema.setCinemaDiscountStrategy(myCinema.getAllDiscountStrategy().get(i));
							} catch (DiscountNotFoundException e) {
								e.toString();
							}
							changeEnd = true;
							end = true;
						}
					}
					if (!changeEnd) {
						System.out.println("Sembra che la scelta selezionata non sia valida...\n");
					}
				}
			}
		}
	}

	
	private static void changePassword() {
		System.out.println("-----------------------------------------------------\n");
		System.out.println("GESTIONE PASSWORD\n");
		System.out.println("Vuoi cambiare la password? (Y/N): ");
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
				boolean changePasswordEnd = false;
				while (!changePasswordEnd) {
					System.out.println("Inserisci la nuova password: ");
					String newPassword = keyboard.nextLine();
					try {
						myCinema.setPassword(newPassword);
						changePasswordEnd = true;
						end = true;
					} catch (PasswordTooShortException e) {
						e.toString();
						System.out.println();
					}
				}
			}
		}
	}

	
	private static void welcomeAndLogin() {
		System.out.println("BENVENUTO ADMIN");
		boolean end = false;
		int attempt = 0;
		while (!end) {
			System.out.println("\nInserisci la password: ");
			String password = keyboard.nextLine();
			try {
				myCinema.login(password);
				end = true;
			} catch (WrongAdminPasswordException e) {
				attempt++;
				e.toString();
			}
			/* Se non ricordo la password per più di N volte allora la cambio
			* e faccio il login nuovamente 
			* */
			if (attempt == MAX_ATTEMPTS) {
				attempt = 0;
				System.out.println("\nSembra che non ricordi più la password...\n");
				changePassword();
			}
		}
	}

	
	private static void printHeader() {
		System.out.println("-----------------------------------------------------\n");
		System.out.println(Cinema.getName().toUpperCase()+"\n");
		System.out.println("Sviluppato da Screaming Hairy Armadillo Team\n");
		System.out.println("-----------------------------------------------------\n");
	}
}
