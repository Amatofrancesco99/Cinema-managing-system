package cinema.view.cli;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

import cinema.controller.Cinema;
import cinema.controller.util.NoProjectionException;
import cinema.model.Movie;
import cinema.model.Spectator;
import cinema.model.cinema.Room;
import cinema.model.payment.methods.PaymentCard;
import cinema.model.projection.Projection;
import cinema.model.reservation.Reservation;

public class CLIMain {

	public static void main(String[] args) {
		//****************************** CLI GUI START *********************************
		
		
		// INFORMAZIONI GENERALI
		System.out.println("-----------------------------------------------------\n");
		System.err.println(Cinema.getInstance().getName().toUpperCase()+"\n");
		System.out.println("Puoi trovarci in: " + Cinema.getInstance().getLocation() + "\n");
		System.out.println("Contattaci: " + Cinema.getInstance().getEmail() + "\n\n");
		System.out.println("-----------------------------------------------------\n");
		
		
		
		// FILM ATTUALMENTE DISPONIBILI
		System.out.println("FILM ATTUALMENTE PROIETTATI \n");
		for (Movie m : Cinema.getInstance().getCurrentlyAvailableMovies()) {
			System.out.println(m.getId() + ")");
			System.out.println(m.getDefaultDescription());
		}
		System.out.println("-----------------------------------------------------\n");
		
		
		
		// FILM DI CUI SI VOGLIONO VEDERE LE PROIEZIONI
		System.out.println("MAGGIORI DETTAGLI FILM E PROIEZIONI \n");
		System.out.println("Inserisci il numero del film di cui vuoi vedere maggiori dettagli e le sue"
				+ " relative proiezioni.");
		@SuppressWarnings("resource")
		Scanner keyboard = new Scanner(System.in);
		int filmId = 0;
		try {
			filmId = keyboard.nextInt();
			if ((filmId <= 0) || (filmId > Cinema.getInstance().getCurrentlyAvailableMovies().size())) {
				System.err.println("\nSelezione non valida. \n");
				System.exit(1);
			}
		}
		catch (InputMismatchException e){
			System.err.println("\nInserisci un numero, non una lettera.\n");
			System.exit(1);
		}
		System.out.println("\n");
		
		System.out.println("Maggiori dettagli sul film: \n");
		System.out.println(Cinema.getInstance().getProjections(filmId).get(0).getMovie().getDetailedDescription());
		System.out.println("Proiezioni previste\n");
		for (Projection p : Cinema.getInstance().getProjections(filmId)) {
			System.out.println(p.getId() + ")");
			System.out.println(p.toString());
		}
		
		
		
		// COMPILAZIONE DELLA PRENOTAZIONE
		
		// 0) Creazione di una nuova prenotazione
		System.out.println("-----------------------------------------------------\n\n");
		System.out.println("COMPILAZIONE PRENOTAZIONE \n");
		Reservation r = Cinema.getInstance().createReservation();
		
		
		// 1) Selezione di una specifica proiezione
		System.out.println("1- SELEZIONE PROIEZIONE \n");
		System.out.println("Inserisci il numero della proiezione che sei interessato visionare:  ");
		int projectionId = 0;
		try {
			projectionId = keyboard.nextInt();
			if ((projectionId <= 0)) { 
				System.err.println("\nSelezione non valida. \n");
				System.exit(1);
			}
		}
		catch (InputMismatchException e){
			System.err.println("\nInserisci un numero, non una lettera.\n");
			System.exit(1);
		}
		System.out.println("\n");
		try {
			r.setProjection(Cinema.getInstance().getProjection(projectionId));
		} catch (NoProjectionException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		
		// 2) Selezione del posto/i
		System.out.println("\n2- SELEZIONE POSTO/I \n");
		System.out.println("Disposizione sala e posti liberi.");
		System.out.println("I posti segnati con XX sono quelli già stati occupati. \n");
		System.out.println("\n----------------------------------- SCHERMO -----------------------------------");
		for (int i = 0 ; i < r.getProjection().getRoom().getNumberRows() ; i++) {
			for (int j = 0 ; j < r.getProjection().getRoom().getNumberCols() ; j++) {
				if (!r.getProjection().verifyIfSeatAvailable(i, j)) {
					System.out.print(" [ XX ] ");
				}
				else 
					System.out.print(" [ " + Room.rowIndexToRowLetter(i) + (j+1) + " ] ");
			}
			System.out.println("");
		}
		System.out.println("\nInserisci i posti che vuoi occupare:  \n");
		// TODO: Occupazione posto/i
		System.out.println(r.addSeat(0, 0)); // prova -- Posto già occupato (Film 1 - Proiezione 183)
		System.out.println(r.addSeat(0, 1)); // prova
		
		
		// 3) Inserimento dei dati personali (Anagrafici + Pagamento)
		System.out.println("\n3.1- INSERIMENTO DATI PERSONALI \n");
		// NOME, COGNOME, DATA DI NASCITA, EMAIL
		System.out.println("Inserisci il tuo nome:  ");
		String name = "";
		name = keyboard.next();
		System.out.println("\n");
		
		System.out.println("Inserisci il tuo cognome:  ");
		String surname = "";
		surname = keyboard.next();
		System.out.println("\n");
		
		System.out.println("Inserisci la tua data di nascita (yyyy-mm-dd):  ");
		String data = keyboard.next();
		// di default, bisogna poi cambiare l'assegnazione per gestire il caso di valori nulli
		LocalDate birthDate = null; 
		try {
			birthDate = LocalDate.parse(data);
		}
		catch(DateTimeParseException e) {
			System.err.println("\nLa data di nascita che hai inserito non è valida. ");
			System.exit(1);
		}
		System.out.println("\n");
		
		System.out.println("Inserisci la tua email:  ");
		String email = "";
		email = keyboard.next();
		System.out.println("\n");
		
		//TODO: aggiungere NOME TITOLARE CARTA, NUMERO CARTA, SCADENZA, CVV
		System.out.println("\n\n3.2- INSERIMENTO DATI PAGAMENTO \n");
		
		
		
		if ((birthDate == null)||(email.equals(""))||(name.equals(""))||(surname.equals(""))) {
			System.err.println("Ops...I tuoi dati anagrafici inseriti sembrano essere mancanti.");
			System.exit(1);
		}
		r.setPurchaser(new Spectator(name,surname,email,birthDate));
		r.setPaymentCard(new PaymentCard());
		// TODO: aggiungere informazioni sugli altri spettatori che parteciperanno alla 
		// proiezione, in modo tale da applicare sconti (comitiva/età/giorno)
		
		
		
		// 4) Pagamento e spedizione dell'email al cliente
		System.out.println("\n4- PAGAMENTO E SPEDIZIONE EMAIL \n");
		String esitoPagamento = r.buy();
		if (!esitoPagamento.equals("Pagamento andato a buon fine.")) {
			System.err.println(esitoPagamento);
			System.exit(1);
		}
		// Vi assicuro che l'invio dell'email, una volta inseriti i parametri corretti funziona.
		// ... Qualora vogliate comunque provare, disabilitate questo commento.
		// r.sendEmail();
		System.out.print("Abbiamo scalato dalla tua carta inserita un ammontare pari "
				+ "a: ");
		System.err.print(r.getTotal().getAmount() + " " + r.getTotal().getCurrency() + "\n");
		System.out.println("Il prezzo scalato comprende già lo sconto" 
				+ " applicato dal nostro cinema, in base alle specifiche inserite.");
		System.out.println("\nControlla le tue email ricevute, a breve ne riceverai una "
				+ "con allegato un pdf contenente il resoconto della tua prenotazione.");
		
		
		
		// SALUTO CLIENTE E CHIUSURA CLI
		System.out.println("\n\nGrazie, a presto!\n");
		System.out.println("-----------------------------------------------------\n");
		System.err.println("Sviluppato da Screaming Hairy Armadillo Team\n");
		System.exit(0);
		
		
		
		//******************************* CLI GUI END **********************************
	}
}