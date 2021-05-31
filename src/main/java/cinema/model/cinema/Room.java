package cinema.model.cinema;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Room {
	
	private static final AtomicInteger count = new AtomicInteger(0); 
	private final long progressive;
	private ArrayList<PhisicalSeat> seats;
	
	public Room() {
		progressive = count.incrementAndGet(); 
		seats=new ArrayList<PhisicalSeat>();
	}
	
	// aggiungi e rimuovi posti alla Room
	public void addSeat(PhisicalSeat s) {
		seats.add(s);
	}
	public void removeSeat(PhisicalSeat s) {
		seats.remove(s);
	}
	
	public long getProgressive() {
		return progressive;
	}
	
	public int getNumberSeats() {
		return seats.size();
	}
	
	public PhisicalSeat getSeat(int index) {
		if(index < seats.size())
			return seats.get(index);
		else
			return null;
			
	}
}