package cinema.model.cinema;

import java.util.ArrayList;

import cinema.model.Movie;
import cinema.model.Projection;
import cinema.model.exceptions.*;

/** BREVE SPIEGAZIONE CLASSE CINEMA (Pattern Singleton GoF)
 * 
 * @author Screaming Hairy Armadillo Team
 *
 *	Questa classe è stata realizzata seguendo il pattern singleton (GoF), poiché nel nostro
 *  intero progetto ci servirà solamente un istanza di questa classe, la quale potrà essere
 *  raggiunta da altre classi presenti nel nostro dominio, andando a richiamare il metodo
 *  getIstance().
 */
public class Cinema {
	
	/**
	* Qui sotto sono riportati tutti gli ATTRIBUTI della classe cinema e i loro significati
	* @param  single_istance     Poiché una sola istanza della classe potrà essere presente
	* @param  name 			     Nome
	* @param  city  		     Città
	* @param  country 		     Paese 
	* @param  zipCode		     Codice comunale
	* @param  address		     Indirizzo (Via, numero civico)
	* @param  logoURL		     Logo del cinema
	* @param  email			     E-mail, utile per inviare report al cliente con i diversi dati
	* 						     riferiti alla specifica prenotazione, effettuata da quest ultimo
	* @param  password           Password associata all'indirizzo email
	* @param  rooms			     ArrayList: comprende tutte le sale del cinema
	* @param  cinemaProjections  ArrayList: comprende tutte le proiezioni fatte dal cinema
	*/
	private static Cinema single_instance = null;
	private String name, city, country, zipCode, address, logoURL, email, password;
	private ArrayList<Room> rooms;
	private ArrayList<Projection> cinemaProjections;
	
	/**
	 * COSTRUTTORE di default, contenente le informazioni specifiche del nostro cinema
	 */
	private Cinema() {
		this.name = "Cinema Armadillo";
		this.city = "Pavia (PV)";
		this.country = "Italia";
		this.zipCode = "27100";
		this.address = "Via A. Ferrata, 5";
		this.email = "CinemaArmadillo@gmail.com";
		this.password = "CinemaArmadillo@1999";
		this.logoURL = "https://cdn1.iconfinder.com/data/icons/luchesa-2/128/Movie-512.png";
		rooms = new ArrayList<Room>();
		cinemaProjections = new ArrayList<Projection>();
	}
	
	/**
	 * 	METODO STATICO per creare l'istanza della classe
	 * 
	 *  Attenzione! Solamente una istanza potrà essere presente
	 *  
	 * @return Cinema  Istanza della classe Cinema, se il cinema è già stato precedentemente
	 * 				   istanziato allora il metodo restituirà l'oggetto già istanziato,
	 *  			   viceversa procederà con l'istanziamento di quest ultimo
	 */
    public static Cinema getInstance()
    {
        if (single_instance == null)
            single_instance = new Cinema();
  
        return single_instance;
    }
    
    /**
     * METODO per aggiungere una proiezione al cinema
     * @param p   Proiezione da aggiungere alla lista di proiezioni di cui il cinema dispone
     */
    public void addProjection(Projection p) {
    	cinemaProjections.add(p);
    }
    
    /**
     * METODO per rimuovere una proiezione al cinema
     * @param p   Proiezione da rimuovere alla lista di proiezioni di cui il cinema dispone
     */
    public void removeProjection(Projection p) {
    	cinemaProjections.remove(p);
    }
    
    /**
     * METODO per farsi restituire tutte le proiezioni di cui il cinema dispone
     * @return ArrayList<Projection>  Insieme di tutte le proiezioni del cinema
     */
    public ArrayList<Projection> getProjections(){
    	return cinemaProjections;
    }
    
    /**
     * 
     * METODO per restituire le proiezioni di un cinema, inerenti uno specifico film
     * @param m  						    Film di cui si vogliono cercare le proiezioni
     * @return ArrayList<Projection>        Insieme delle proiezioni dello specifico film
     * @throws NoMovieProjectionsException  eccezione lanciata, qualora il cinema non abbia quel 
     * 										film, tra i film proiettati
     */
 	public ArrayList<Projection> getProjections(Movie m) throws NoMovieProjectionsException{
 		ArrayList<Projection> movieProjections = new ArrayList<Projection>();
 		for (Projection p: cinemaProjections) {
 			if (p.getMovie() == m) {
 				movieProjections.add(p);
 			}
 		}
 		if (movieProjections.size() == 0)
 			throw new NoMovieProjectionsException(m);
 		return movieProjections;
 	}
 	
 	/**
 	 * METODO per restituire tutti i film che il cinema sta proiettando
 	 * @return ArrayList<Movie>   Insieme di tutti i film che il cinema sta momentaneamente
 	 * 							  proiettando
 	 */
    public ArrayList<Movie> getMovies(){
		ArrayList<Movie> movies = new ArrayList<Movie>();
		for (Projection p: cinemaProjections) {
			for (Movie m: movies) {
				// a film is valid only if it has all the required fields
				if ((p.getMovie().getTitle()!=m.getTitle()) )
						movies.add(p.getMovie());
			}
		}
		return movies;
	}
    
	/**
	 * METODO per aggiungere una sala del cinema
	 * @param r   Sala del cinema da aggiungere, all'insieme delle sale del cinema stesso
	 */
	public void addRoom(Room r) {
		rooms.add(r);
	}
	
	/**
	 * METODO per rimuovere una sala del cinema
	 * @param r   Sala del cinema da rimuovere, dall'insieme delle sale del cinema stesso
	 */
	public void removeRoom(Room r) throws NoCinemaRoomsException {
		if (rooms.size() > 0)
			rooms.remove(r);
		else throw new NoCinemaRoomsException(this.name,this.city,this.address);
	}
	
	/**
	 * METODO per settare/cambiare il logo del cinema
	 * @param logoURL   Indirizzo URL contentente il logo del cinema ( si suggerisce un immagine
	 * 					con formato .png, per una migliore resa grafica )
	 */
	public void setLogoURL(String logoURL) {
		this.logoURL = logoURL;
	}
	
	/**
	 * METODO per settare/cambiare il nome del cinema
	 * @param n   Nome del cinema
	 */
	public void setName(String n) {
		this.name = n;
	}
	
	/**
	 * METODO per settare/cambiare la "location" in cui si trova il cinema
	 * @param city		Citta
	 * @param country	Paese
	 * @param zipCode	Codice comunale
	 * @param address	Indirizzo (Via, numero civico)
	 */
	public void setLocation(String city,String country, String zipCode, String address) {
		this.city = city;
		this.country = country;
		this.zipCode = zipCode;
		this.address = address;
	}
	
	/**
	 * METODO per settare/cambiare l'email del cinema
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * METODO per settare/cambiare la password associata all'email del cinema
	 * @param newPassword	Password associata all'account email del cinema
	 */
	public void changePassword(String newPassword) {
		password = newPassword;
	}
	
	/**
	 * METODO per farsi dire il nome del cinema
	 * @return name   Nome del cinema
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * METODO per farsi dire il numero di sale di cui il cinema è composto
	 * @return numberOfRooms
	 */
	public int getNumberOfRooms() {
		return rooms.size();
	}
	
	/**
	 * METODO per farsi dire le informazioni del luogo in cui il cinema è situato
	 * @return location
	 */
	public String getLocation() {
		return address + ", " + city + " - " + zipCode + "  " + country;
	}	
	
	/**
	 * METODO per farsi restituire una stringa contentente l'indirizzo web in cui è presente
	 * il logo del cinema
	 * @return logoURL
	 */
	public String getLogoURL() {
		return logoURL;
	}
	
	/**
	 * METODO per farsi dire l'email del cinema
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * METODO per farsi dire la password associata all'email del cinema
	 * Questo metodo viene utilizzato solamente quando deve essere mandata l'email all'utente,
	 * contenente come allegato un .pdf file contenente le informazioni sulla prenotazione
	 * effettuata
	 * @return password
	 */
	public String getPassword() {
		return password;
	}
}