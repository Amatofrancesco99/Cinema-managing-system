package cinema.view.cligui;

import java.util.InputMismatchException;
import java.util.Scanner;

import cinema.controller.Cinema;
import cinema.model.Movie;
import cinema.model.projection.Projection;
import cinema.model.reservation.Reservation;

public class CliGUIMain {

	public static void main(String[] args) {
		
		
		// INFORMAZIONI GENERALI
		System.out.println("-----------------------------------------------------\n");
		System.err.println(Cinema.getInstance().getName()+"\n");
		System.out.println("Puoi trovarci in: " + Cinema.getInstance().getLocation() + "\n");
		System.out.println("Contattaci: " + Cinema.getInstance().getEmail() + "\n\n");
		System.err.println("Sviluppato da Screaming Hairy Armadillo Team\n");
		System.out.println("-----------------------------------------------------\n");
		
		
		
		// FILM ATTUALMENTE DISPONIBILI
		System.out.println("FILM ATTUALMENTE PROIETTATI \n");
		for (Movie m : Cinema.getInstance().getCurrentlyAvailableMovies()) {
			System.out.println(m.getId() + ")");
			System.out.println(m.toString());
		}
		System.out.println("-----------------------------------------------------\n");
		
		
		
		// FILM DI CUI SI VOGLIONO VEDERE LE PROIEZIONI
		System.out.println("MAGGIORI DETTAGLI PROIEZIONE FILM \n");
		System.out.println("Inserisci il numero del film di cui sei interessatto a vedere le proiezioni:  ");
		Scanner keyboard = new Scanner(System.in);
		int filmId = 0;
		try {
			filmId = keyboard.nextInt();
			if ((filmId <= 0) || (filmId > Cinema.getInstance().getCurrentlyAvailableMovies().size())) {
				System.out.println("\nSelezione non valida. \n");
			}
		}
		catch (InputMismatchException e){
			System.out.println("\nInserisci un numero, non una lettera.\n");
		}
		System.out.println("\n");
		
		for (Projection p : Cinema.getInstance().getProjections(filmId)) {
			System.out.println(p.getId() + ")");
			System.out.println(p.toString());
		}
		
		
		
		// COMPILAZIONE DELLA PRENOTAZIONE, AGGIUNTA POSTI UNA VOLTA SELEZIONATA 
		// UNA PROIEZIONE SPECIFICA
		System.out.println("-----------------------------------------------------\n\n");
		System.out.println("SELEZIONE PROIEZIONE \n");
		Reservation r = Cinema.getInstance().createReservation();
		
	}
}