package cinema.model.projection;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Map.Entry;

import cinema.model.money.Money;
import cinema.model.Movie;
import cinema.model.cinema.PhysicalSeat;
import cinema.model.cinema.Room;
import lombok.*;

@Data
@AllArgsConstructor
public class Projection implements Comparable<Projection> {
	
	private int id;
	private Movie movie;
	private Room room;
	private LocalDateTime dateTime;
	private Money price;
	private ArrayList<ArrayList<ProjectionSeat>> seats;
	
	
	public Projection(int id, Movie movie, LocalDateTime dateTime, Money price, Room room) {
		this.id = id;
		this.movie = movie;
		this.dateTime = dateTime;
		this.price = price;
		this.room = room;
		this.seats = new ArrayList<ArrayList<ProjectionSeat>>();
		for(int i = 0; i < room.getNumberRows(); i++) {
			ArrayList<ProjectionSeat> row = new ArrayList<ProjectionSeat>();
			for(int j = 0; j < room.getNumberCols(); j++) {
				row.add(new ProjectionSeat(room.getSeat(i, j), true));
			}
			seats.add(row);
		}
	}
	
	// da testare/controllare conformità, quando cambio la sala siamo sicuri che
	// anche i posti liberi terranno memoria di questo cambiamento o resteranno associati
	// alla prima stanza associata alla proiezione? 
	
	/**
	public void setAllSeatsAsAvailable (Room room) {
		this.room = room;
		
		seatsRow = new ArrayList<ProjectionSeats>();
		seats = new ArrayList<>();
		for () {
			LocalDate localDate = projection.getDateTime().toLocalDate();
			if (!Objects.equals(lastLocalDate, localDate)) {
				schedule.add(new ArrayList<Projection>());
				lastLocalDate = localDate;
			}
			schedule.get(schedule.size() - 1).add(projection);
		}
	}
	
	// farsi restituire i posti liberi della sala in cui sarà proiettato un film
	public ArrayList<PhysicalSeat> getFreeSeats() {
		ArrayList<PhysicalSeat> freeSeats=new ArrayList<PhysicalSeat>();
		// per ogni posto della sala vado a vedere se è libero, ossia se getValue è true.
		// se tale condizione è vera lo aggiungo alla lista dei posti liberi
		for (Entry<PhysicalSeat, Boolean> entry : availableSeats.entrySet()) {
			if (entry.getValue() == true) {
				freeSeats.add(entry.getKey());
			}
		}
		return freeSeats;
	}
	
    // metodo occupa posto di una sala
	public boolean takeSeat(PhysicalSeat seat) {
		boolean takeSeat=false;
		for (PhysicalSeat s : getFreeSeats()) {
			if (seat == s) {
				availableSeats.put(s,false);
				takeSeat=true;
			}
		}
		return takeSeat;
	}
	
	// metodo per liberare il posto di una sala
	public boolean freeSeat(PhysicalSeat seat) {
		boolean freeSeat=false;
		for (PhysicalSeat s : getFreeSeats()) {
			if (seat == s) {
				availableSeats.put(s,true);
				freeSeat=true;
			}
		}
		return freeSeat;
	}
	**/

	@Override
	public int compareTo(Projection p) {
		return this.dateTime.compareTo(p.getDateTime());
	}
}