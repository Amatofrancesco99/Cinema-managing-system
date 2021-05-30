package cinema.model.cinema;

import java.util.ArrayList;

import cinema.model.Movie;
import cinema.model.Projection;
import cinema.model.exceptions.*;

//Singleton class
public class Cinema {
	
	private static Cinema single_instance = null;
	private String name, city, country, zipCode, address, logoUrl, email, password;
	private ArrayList<Room> rooms;
	private ArrayList<Projection> cinemaProjections; // ??? (high coupling)
	
	// costruttore di default, contenente le informazioni specifiche del nostro cinema
	private Cinema() {
		this.name="Cinema Armadillo";
		this.city="Pavia (PV)";
		this.country="Italia";
		this.zipCode="27100";
		this.address = "Via A. Ferrata, 5";
		this.email="CinemaArmadillo@gmail.com";
		this.password="CinemaArmadillo@1999";
		this.logoUrl = "https://cdn1.iconfinder.com/data/icons/luchesa-2/128/Movie-512.png";
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
    public ArrayList<Projection> getProjections(){
    	return cinemaProjections;
    }
    
    // farsi dare tutte le proiezioni di uno specifico film
 	// ??? (high coupling)
 	public ArrayList<Projection> getProjections(Movie m) throws NoMovieProjectionsException{
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
 	
 	
    // farsi dare tutti i film proiettati dal cinema
    public ArrayList<Movie> getMovies(){
		ArrayList<Movie> movies = new ArrayList<Movie>();
		for (Projection p: cinemaProjections) {
			for (Movie m:movies) {
				// se il film che si sta proiettando ha nome e data di rilascio
				// diversa tra i film già presenti, allora si aggiunge alla lista di movies
				// proiettati
				if ((p.getMovie().getName()!=m.getName()) || (p.getMovie().getReleaseDate()!=m.getReleaseDate()))
						movies.add(p.getMovie());
			}
		}
		return movies;
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
	// !IMPORTANTE! quando passi un link come parametro, poiché vuoi cambiare il logo
	// del tuo cinema, è importante che il link contenga il riferimento ad un immagine
	// con estensione .png (MUST), altrimenti la resa grafica non sarà ottimale
	public void setLogoUrl(String link) {
		logoUrl=link;
	}
	public void setName(String n) {
		this.name=n;
	}
	public void setLocation(String city,String country, String zipCode, String address) {
		this.city=city;
		this.country=country;
		this.zipCode=zipCode;
		this.address=address;
	}
	public void setEmail(String email) {
		this.email=email;
	}
	public void changePassword(String newPassword) {
		password=newPassword;
	}
	
	
	//Getters per farsi dare le informazioni del cinema
	public String getName() {
		return name;
	}
	public int getNumberOfRooms() {
		return rooms.size();
	}
	public String getLocation() {
		return address + ", " + city + " - " + zipCode + "  " + country;
	}	
	public String getLogoUrl() {
		return logoUrl;
	}
	public String getEmail() {
		return email;
	}
}
