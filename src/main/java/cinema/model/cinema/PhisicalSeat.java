package cinema.model.cinema;

import cinema.model.enumerations.TypeOfSeat;

public class PhisicalSeat {
	
	private char row;
	private int number;
	private TypeOfSeat type;
	
	public PhisicalSeat(char row, int number, TypeOfSeat type) {
		this.row=row;
		this.number=number;
		this.type=type;
	}
	
}