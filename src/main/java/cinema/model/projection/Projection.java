package cinema.model.projection;

import java.time.LocalDateTime;
import java.util.ArrayList;

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
	
	public boolean verifyIfSeatAvailable(int row, int col) {
		return seats.get(row).get(col).isAvailable();
	}
	
    // metodo occupa posto della sala in cui è fatta la proiezione
	public boolean takeSeat(int row, int col) {
			if(verifyIfSeatAvailable(row, col)) {
				seats.get(row).get(col).setAvailable(false);
				return true;
			}
			return false;		
	}
	

	// metodo per liberare il posto di una sala
	public boolean freeSeat(int row, int col) {
		if(!verifyIfSeatAvailable(row, col)) {
			seats.get(row).get(col).setAvailable(true);
			return true;
		}
		return false;
	}
	
	
	public PhysicalSeat getPhysicalSeat(int row, int col) {
		return this.getSeats().get(row).get(col).getPhysicalSeat();
	}

	
	@Override
	public int compareTo(Projection p) {
		return this.dateTime.compareTo(p.getDateTime());
	}
}