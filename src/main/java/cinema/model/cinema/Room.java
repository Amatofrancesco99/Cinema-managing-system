package cinema.model.cinema;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import cinema.model.Movie;
import lombok.Data;

@Data
public class Room {
	
	private static final AtomicInteger count = new AtomicInteger(0); 
	private final long progressive;
	private ArrayList<PhysicalSeat> seats;
	
	public Room() {
		progressive = count.incrementAndGet(); 
		seats = new ArrayList<PhysicalSeat>();
	}
	
	// aggiungi e rimuovi posti alla Room
	public void addSeat(PhysicalSeat s) {
		seats.add(s);
	}
	public void removeSeat(PhysicalSeat s) {
		seats.remove(s);
	}
	
	public int getNumberSeats() {
		return seats.size();
	}
	
	public PhysicalSeat getSeat(int index) {
		if(index < seats.size())
			return seats.get(index);
		else
			return null;
	}
}