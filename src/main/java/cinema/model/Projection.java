package cinema.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cinema.model.cinema.PhisicalSeat;
import cinema.model.cinema.Room;
import cinema.model.enumerations.TypeOfCurrency;

public class Projection {
	
	private Movie movie;
	private Room room;
	private Date date;
	private String time;
	private Money price;
	private HashMap<PhisicalSeat,Boolean> availableSeats;
	
	public Projection (Date date, String time, Money price) {
		this.date=date;
		this.time=time;
		this.price=price;
	}

	// setta quale film e dove, il film sarà proiettato
	public void setMovie (Movie movie) {
		this.movie=movie;
	}
	//da testare/controllare conformità
	public void setRoom (Room room) {
		this.room=room;
		availableSeats = new HashMap<PhisicalSeat,Boolean>(room.getNumberSeats());
		for(int i=0; i<room.getNumberSeats(); i++) 
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
		
}