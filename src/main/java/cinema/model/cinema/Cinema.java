package cinema.model.cinema;

import java.util.ArrayList;

public class Cinema {
	
	private String name, city, state, zipCode, address;
	private ArrayList<Room> rooms;
	
	public Cinema(String name, String city, String state, String zipCode, String address) {
		this.name=name;
		this.city=city;
		this.state=state;
		this.zipCode=zipCode;
		this.address = address; 
	}
	
	public String getName() {
		return name;
	}
	public String getCinemaLocation() {
		return city+" "+state+" "+zipCode+" "+address;
	}
	
}