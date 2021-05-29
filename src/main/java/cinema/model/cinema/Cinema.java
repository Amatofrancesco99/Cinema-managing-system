package cinema.model.cinema;

import java.util.ArrayList;

import cinema.model.Movie;
import cinema.model.Projection;
import cinema.model.exceptions.*;

//Singleton class
public class Cinema {
	
	private static Cinema single_instance = null;
	private String name, city, state, zipCode, address, urlLogo;
	private ArrayList<Room> rooms;
	private ArrayList<Projection> cinemaProjections; // ??? (high coupling)
	
	// costruttore di default, contenente le informazioni specifiche del nostro cinema
	private Cinema() {
		this.name="Cinema Armadillo";
		this.city="Pavia (PV)";
		this.state="Italia";
		this.zipCode="27100";
		this.address = "Via A. Ferrata, 5";
		this.urlLogo = "https://www.clipartmax.com/png/middle/310-3105859_film-cinema-icon-png.png";
		rooms=new ArrayList<Room>();
		cinemaProjections=new ArrayList<Projection>(); // ??? (high coupling)
	}

	// metodo statico per creare un istanza di una classe singleton
    public static Cinema getInstance()
    {
        if (single_instance == null)
            single_instance = new Cinema();
  
        return single_instance;
    }
    
    // aggiungere o rimuovere una proiezione da un cinema
    public void addProjection(Projection p) {
    	cinemaProjections.add(p);
    }
    public void removeProjection(Projection p) {
    	cinemaProjections.remove(p);
    }
    
    // farsi dare tutte le proiezioni fatte da un cinema
    public ArrayList<Projection> getCinemaProjections(){
    	return cinemaProjections;
    }
    
	// farsi dare tutte le proiezioni di uno specifico film
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
	
	//Cambiare le proprietà del cinema
	public void addRoom(Room r) {
		rooms.add(r);
	}
	public void removeRoom(Room r) throws NoCinemaRoomsException {
		if (rooms.size()>0)
			rooms.remove(r);
		else throw new NoCinemaRoomsException(this.name,this.city,this.address);
	}
	public void setUrlLogo(String link) {
		urlLogo=link;
	}
	public void setName(String n) {
		this.name=n;
	}
	public void setLocation(String city,String state, String zipCode, String address) {
		this.city=city;
		this.state=state;
		this.zipCode=zipCode;
		this.address=address;
	}
	
	//Getters per farsi dare le informazioni del cinema
	public String getName() {
		return name;
	}
	public int getNumberOfRooms() {
		return rooms.size();
	}
	public String getLocation() {
		return address + ", " + city + " - " + zipCode + "  " + state;
	}	
}
