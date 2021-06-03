package cinema.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import cinema.model.cinema.PhisicalSeat;
import cinema.model.cinema.Room;
import cinema.model.enumerations.TypeOfCurrency;
import lombok.Data;

@Data
public class Projection implements Comparable<Projection> {
	
	private int id;
	private Movie movie;
	private Room room;
	private LocalDateTime dateTime;
	private Money price;
	// I posti della sala in cui sarà proiettato il film possono essere liberi o occupati,
	// per questo motivo ad ogni posto fisico della sala è associato un valore booleano.
	// Se true allora il posto è libero, se falso il posto è occupato.
	private HashMap<PhisicalSeat,Boolean> availableSeats;
	
	public Projection (int id, LocalDateTime dateTime, Money price) {
		this.dateTime = dateTime;
		this.price = price;
		this.id = id;
	}
	
	// da testare/controllare conformità, quando cambio la sala siamo sicuri che
	// anche i posti liberi terranno memoria di questo cambiamento o resteranno associati
	// alla prima stanza associata alla proiezione? 
	public void setRoom (Room room) {
		this.room = room;
		availableSeats = new HashMap<PhisicalSeat,Boolean>(room.getNumberSeats());
		for(int i = 0; i < room.getNumberSeats(); i++) 
			availableSeats.put(room.getSeat(i), true);	
	}
	
	// farsi restituire i posti liberi della sala in cui sarà proiettato un film
	public ArrayList<PhisicalSeat> getFreeSeats() {
		ArrayList<PhisicalSeat> freeSeats=new ArrayList<PhisicalSeat>();
		// per ogni posto della sala vado a vedere se è libero, ossia se getValue è true.
		// se tale condizione è vera lo aggiungo alla lista dei posti liberi
		for (Entry<PhisicalSeat, Boolean> entry : availableSeats.entrySet()) {
			if (entry.getValue() == true) {
				freeSeats.add(entry.getKey());
			}
		}
		return freeSeats;
	}
	
    // metodo occupa posto di una sala
	public boolean takeSeat(PhisicalSeat seat) {
		boolean takeSeat=false;
		for (PhisicalSeat s : getFreeSeats()) {
			if (seat == s) {
				availableSeats.put(s,false);
				takeSeat=true;
			}
		}
		return takeSeat;
	}
	
	// metodo per liberare il posto di una sala
	public boolean freeSeat(PhisicalSeat seat) {
		boolean freeSeat=false;
		for (PhisicalSeat s : getFreeSeats()) {
			if (seat == s) {
				availableSeats.put(s,true);
				freeSeat=true;
			}
		}
		return freeSeat;
	}

	@Override
	public int compareTo(Projection p) {
		return this.dateTime.compareTo(p.getDateTime());
	}
}