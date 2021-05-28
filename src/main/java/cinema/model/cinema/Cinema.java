package cinema.model.cinema;

import java.util.ArrayList;

//Singleton class
public class Cinema {
	
	private static Cinema single_instance = null;
	private String name, city, state, zipCode, address;
	private ArrayList<Room> rooms;
	
	// Our cinema class
	private Cinema() {
		this.name="Armadillo Cinema";
		this.city="Pavia (PV)";
		this.state="Italy";
		this.zipCode="27100";
		this.address = "Via A.Ferrata, 5";
		rooms=new ArrayList<Room>();
	}
	
	// static method to create instance of Singleton class
    public static Cinema getInstance()
    {
        if (single_instance == null)
            single_instance = new Cinema();
  
        return single_instance;
    }
    
	public void addRoom(Room r) {
		rooms.add(r);
	}
	public void removeRoom(Room r) {
		rooms.remove(r);
	}
	
	public String getName() {
		return name;
	}
	public String getCinemaLocation() {
		return city+" "+state+" "+zipCode+" "+address;
	}
	
}