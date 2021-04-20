package it.unipv.ingsw.cinema.model.cinema;

import java.util.ArrayList;

public class Room {
	
	private int progressive,seatsNumber;
	private ArrayList<PhisicalSeat> seats;
	
	public Room(int progressive, int seatsNumber) {
		this.progressive=progressive;
		this.seatsNumber=seatsNumber;
		seats=new ArrayList<PhisicalSeat>();
	}
	
}