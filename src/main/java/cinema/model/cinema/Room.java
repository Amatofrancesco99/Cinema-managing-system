package cinema.model.cinema;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import cinema.model.cinema.util.InvalidRoomDimensionsException;
import lombok.Data;

@Data
public class Room {
	
	private static final AtomicInteger count = new AtomicInteger(0); 
	private final long progressive;
	private ArrayList<ArrayList<PhysicalSeat>> seats;
	
	public Room(int rows, int cols) throws InvalidRoomDimensionsException{

		if(rows <= 0 || cols <= 0)
			throw new InvalidRoomDimensionsException();

		seats = new ArrayList<ArrayList<PhysicalSeat>>();
		for(int i = 0; i < rows; i++) {
			ArrayList<PhysicalSeat> row = new ArrayList<PhysicalSeat>();
			for(int j = 0; j < cols; j++) {
				row.add(new PhysicalSeat());
			}
			seats.add(row);
		}
		this.progressive = count.incrementAndGet();
	}
	
	public int getNumberSeats() {
		return seats.size()*seats.get(0).size();
	}
	
	public int getNumberRows() {
		return seats.get(0).size();
	}
	
	public int getNumberCols() {
		return seats.size();
	}
	
	
	
	public PhysicalSeat getSeat(int row, int col) {
		return seats.get(row).get(col);
	}
}