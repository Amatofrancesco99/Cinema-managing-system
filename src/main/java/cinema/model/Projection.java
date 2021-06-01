package cinema.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import cinema.model.cinema.PhisicalSeat;
import cinema.model.cinema.Room;
import cinema.model.enumerations.TypeOfCurrency;

public class Projection implements Comparable<Projection> {
	private int id;
	private Movie movie;
	private Room room;
	private LocalDateTime dateTime;
	private Money price;
	private HashMap<PhisicalSeat,Boolean> availableSeats;
	
	public Projection (int id, LocalDateTime dateTime, Money price) {
		this.dateTime= dateTime;
		this.price = price;
		this.id = id;
	}

	// setta quale film e dove, il film sarà proiettato
	public void setMovie (Movie movie) {
		this.movie = movie;
	}
	
	//da testare/controllare conformità
	public void setRoom (Room room) {
		this.room = room;
		availableSeats = new HashMap<PhisicalSeat,Boolean>(room.getNumberSeats());
		for(int i = 0; i < room.getNumberSeats(); i++) 
			availableSeats.put(room.getSeat(i), true);	
	}
	
	//(DA FARE!!!!!) METODO OCCUPA POSTO E METODO PER VERIFICARE POSTO LIBERO + RELATIVO METODO IN RESERVATION
	
	// getters informazioni proiezioni
	public Movie getMovie() {
		return movie;
	}
	public Room getRoom() {
		return room;
	}
	public Money getPrice() {
		return price;
	}
	// getters per ricavare le informazioni sul prezzo di queste ultime
	public float getAmount() {
		return price.getAmount();
	}
	public TypeOfCurrency getCurrency() {
		return price.getCurrency();
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public int getId() {
		return id;
	}

	@Override
	public int compareTo(Projection p) {
		return this.dateTime.compareTo(p.getDateTime());
	}
}
