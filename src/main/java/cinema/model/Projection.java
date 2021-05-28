package cinema.model;

import java.util.Date;
import cinema.model.cinema.Room;

public class Projection {
	private Movie movie;
	private Room room;
	private Date date;
	private String time;
	
	public Projection (Date date, String time) {
		this.date=date;
		this.time=time;
	}

	// set which movie, and where (room), the film will be projected
	public void setMovie (Movie movie) {
		this.movie=movie;
	}
	public void setRoom (Room room) {
		this.room=room;
	}
	
	// Projection info
	public Movie getMovie() {
		return movie;
	}
	public Room getRoom() {
		return room;
	}
}