package cinema.model.cinema;

import java.util.ArrayList;

import cinema.model.Movie;
import cinema.model.Projection;
import cinema.model.exceptions.NoMovieProjectionsException;

//Singleton class
public class Cinema {
	
	private static Cinema single_instance = null;
	private String name, city, state, zipCode, address, urlLogo;
	private ArrayList<Room> rooms;
	private ArrayList<Projection> cinemaProjections; // ??? (high coupling)
	
	// Costruttore di default, contenente le informazioni specifiche del nostro cinema
	private Cinema() {
		this.name="Armadillo Cinema";
		this.city="Pavia (PV)";
		this.state="Italy";
		this.zipCode="27100";
		this.address = "Via A.Ferrata, 5";
		this.urlLogo = "https://www.clipartmax.com/png/middle/310-3105859_film-cinema-icon-png.png";
		rooms=new ArrayList<Room>();
		cinemaProjections=new ArrayList<Projection>(); // ??? (high coupling)
	}
	
	// Se un'altra persona volesse creare un cinema con informazioni diverse da quelle di 
	// default questo è il costruttore
	private Cinema(String name, String city, String state, String zipCode, String address) {
		this.name=name;
		this.city=city;
		this.state=state;
		this.zipCode=zipCode;
		this.address=address;
		//default logo
		this.urlLogo = "https://www.clipartmax.com/png/middle/310-3105859_film-cinema-icon-png.png";
		rooms=new ArrayList<Room>();
		cinemaProjections=new ArrayList<Projection>(); // ??? (high coupling)
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

	// get all projections of a specific movie
	// ??? (high coupling)
	public ArrayList<Projection> getMovieProjections(Movie m) throws NoMovieProjectionsException{
		ArrayList<Projection> movieProjections = new ArrayList<Projection>();
		for (Projection p: cinemaProjections) {
			if (p.getMovie()==m) {
				movieProjections.add(p);
			}
		}
		if (movieProjections.size()==0)
			throw new NoMovieProjectionsException(m);
		return movieProjections;
	}
	
	public void setUrlLogo(String link) {
		urlLogo=link;
	}
	public String getName() {
		return name;
	}
	public int getNumberOfRooms() {
		return rooms.size();
	}
	public String getCinemaLocation() {
		return city+" "+state+" "+zipCode+" "+address;
	}	
}