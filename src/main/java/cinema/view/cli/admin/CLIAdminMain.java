package cinema.view.cli.admin;

import java.util.Scanner;

import cinema.controller.Cinema;
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
	
	public static void main(String[] args) {
		//TODO: Aggiungere proiezioni e modificare sconti.
		
		// INFORMAZIONI GENERALI SUL CINEMA
		printHeader();
		
		// SALUTO ALL'ADMIN E LOGIN
		welcomeAndLogin();
	}

	private static void welcomeAndLogin() {
		System.out.println("BENVENUTO ADMIN");
		boolean end = false;
		while (!end) {
			System.out.println("\nInserisci la password: ");
			String password = keyboard.nextLine();
			try {
				myCinema.login(password);
				end = true;
			} catch (WrongAdminPasswordException e) {
				e.toString();
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
