package cinema.view.cli.admin;

import java.util.Scanner;

import cinema.controller.Cinema;
import cinema.controller.util.DiscountNotFoundException;
import cinema.controller.util.PasswordTooShortException;
import cinema.controller.util.WrongAdminPasswordException;


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
	final static int MAX_ATTEMPT = 3;
	
	public static void main(String[] args) {
		// INFORMAZIONI GENERALI SUL CINEMA
		printHeader();
		
		// SALUTO ALL'ADMIN E LOGIN
		welcomeAndLogin();
		
		// CAMBIO PASSWORD (OPZIONALE)
		changePassword();
		
		// CAMBIO PROPRIETA' STRATEGIA / SELEZIONE STRATEGIA APPLICATA
		handleDiscounts();
		
		// INSERIMENTO NUOVE PROIEZIONI (OPZIONALE) TODO
		insertNewProjections();
		
		// SALUTO DELL'ADMIN E TERMINA PROGRAMMA
		sayGoodbye();
	}

	private static void handleDiscounts() {
		System.out.println("-----------------------------------------------------\n");
		System.out.println("GESTIONE SCONTI\n");
		// ???????  CAMBIO PROPRIETA' STRATEGIA/E (OPZIONALE)  ??????
		// changeDiscountsProperties();
		// CAMBIO STRATEGIA SCONTISTICA (OPZIONALE)
		changeNewReservationsDiscountStrategy();
	}

	/*
	private static void changeDiscountsProperties() {
		System.out.println("Vuoi cambiare le proprietà di una strategia già esistente? (Y/N)");
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
								System.out.println(myCinema.getDiscountStrategyDescription(myCinema.getAllDiscountStrategy().get(i)));
								changeEnd = true;
								end = true;
							} catch (DiscountNotFoundException e) {
								e.toString();
							}
						}
					}
					if (!changeEnd) {
						System.out.println("Sembra che la scelta selezionata non sia valida...\n");
					}
				}
			}
		}
	} */

	private static void sayGoodbye() {
		System.out.println("\n\nGrazie, a presto!");
		System.out.println("-----------------------------------------------------");
	}

	private static void insertNewProjections() {
		
	}

	private static void changeNewReservationsDiscountStrategy() {
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
		int attempts = 0;
		while (!end) {
			System.out.println("\nInserisci la password: ");
			String password = keyboard.nextLine();
			try {
				myCinema.login(password);
				end = true;
			} catch (WrongAdminPasswordException e) {
				attempts++;
				e.toString();
			}
			/* Se non ricordo la password per più di N volte allora la cambio
			* e faccio il login nuovamente 
			* */
			if (attempts == MAX_ATTEMPT) {
				attempts = 0;
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
