package cinema.model;

import java.util.Date;
import cinema.model.cinema.Room;
import cinema.model.enumerations.TypeOfCurrency;

public class Projection {
	private Movie movie;
	private Room room;
	private Date date;
	private String time;
	private Money price;
	
	public Projection (Date date, String time, Money price) {
		this.date=date;
		this.time=time;
		this.price=price;
	}

	// setta quale film e dove, il film sarà proiettato
	public void setMovie (Movie movie) {
		this.movie=movie;
	}
	public void setRoom (Room room) {
		this.room=room;
	}
	
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