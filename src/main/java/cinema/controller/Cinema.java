package cinema.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import cinema.model.Movie;
import cinema.model.cinema.Room;
import cinema.model.cinema.util.InvalidRoomDimensionsException;
import cinema.model.money.Money;
import cinema.controller.util.*;
import cinema.model.projection.Projection;
import cinema.model.reservation.Reservation;
import lombok.Data;

/**
 * BREVE SPIEGAZIONE CLASSE CINEMA (Pattern Singleton GoF - Pattern Controller)
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Questa classe è stata realizzata seguendo il pattern singleton (GoF),
 * poiché nel nostro intero progetto ci servirà solamente un istanza di
 * questa classe, la quale potrà essere raggiunta da altre classi
 * presenti nel nostro dominio, andando a richiamare il metodo
 * getIstance().
 */
@Data
public class Cinema {

	/**
	 * ATTRIBUTI
	 * 
	 * @param single_istance    Poiché una sola istanza della classe potrà essere
	 *                          presente
	 * @param name              Nome
	 * @param city              Città
	 * @param country           Paese
	 * @param zipCode           Codice comunale
	 * @param address           Indirizzo (Via, numero civico)
	 * @param logoURL           Logo del cinema
	 * @param email             E-mail, utile per inviare report al cliente con i
	 *                          diversi dati riferiti alla specifica prenotazione,
	 *                          effettuata da quest ultimo
	 * @param password          Password associata all'indirizzo email
	 * @param rooms             List: comprende tutte le sale del cinema
	 * @param cinemaProjections List: comprende tutte le proiezioni fatte dal cinema
	 */
	private static Cinema single_instance = null;
	private String name, city, country, zipCode, address, logoURL, email, password;
	private List<Room> rooms;
	private List<Projection> cinemaProjections;

	/**
	 * COSTRUTTORE di default, contenente le informazioni specifiche del nostro
	 * cinema
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

		// ********* TEMPORARY DATA USED FOR TESTING *********
		// Test movie
		ArrayList<String> genres, directors, cast;
		genres = new ArrayList<>();
		directors = new ArrayList<>();
		cast = new ArrayList<>();
		genres.add("Drammatico");
		genres.add("Commedia");
		directors.add("Thomas Vinterberg");
		cast.add("Mads Mikkelsen");
		cast.add("Thomas Bo Larsen");
		cast.add("Lars Ranthe");
		cast.add("Magnus Millang");
		Movie movie = new Movie(1, "Druk - Un altro giro",
				"C'è una teoria secondo la quale tutti noi siamo nati con una piccola quantità di alcool già presente nel sangue e che, pertanto, una piccola ebbrezza possa aprire le nostre menti al mondo che ci circonda, diminuendo la nostra percezione dei problemi e aumentando la nostra creatività. Rincuorati da questa teoria, Martin e tre suoi amici, tutti annoiati insegnanti delle superiori, intraprendono un esperimento per mantenere un livello costante di ubriachezza durante tutta la giornata lavorativa. Se Churchill vinse la seconda guerra mondiale in preda a un pesante stordimento da alcool, chissà cosa potrebbero fare pochi bicchieri per loro e per i loro studenti?",
				genres, directors, cast, 4, 117,
				"https://200mghercianos.files.wordpress.com/2020/12/another-round-druk-thomas-vinteberg-filme-critica-mostra-sp-poster-1.jpg",
				"https://www.youtube.com/watch?v=hFbDh58QHzw");

		// Test room
		try {
			rooms.add(new Room(5, 10));
		} catch (InvalidRoomDimensionsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Test projections
		Projection p1 = new Projection(123, movie, LocalDateTime.parse("2021-06-04T22:30:00"), new Money(12.5f),
				rooms.get(0));
		Projection p2 = new Projection(183, movie, LocalDateTime.parse("2021-06-01T20:15:00"), new Money(12.5f),
				rooms.get(0));
		Projection p3 = new Projection(193, movie, LocalDateTime.parse("2021-06-01T22:30:00"), new Money(12.5f),
				rooms.get(0));
		Projection p4 = new Projection(109, movie, LocalDateTime.parse("2021-06-02T22:30:00"), new Money(12.5f),
				rooms.get(0));
		Projection p5 = new Projection(743, movie, LocalDateTime.parse("2021-06-02T23:30:00"), new Money(12.5f),
				rooms.get(0));
		Projection p6 = new Projection(233, movie, LocalDateTime.parse("2021-06-02T19:00:00"), new Money(12.5f),
				rooms.get(0));
		Projection p7 = new Projection(184, movie, LocalDateTime.parse("2021-06-03T08:05:00"), new Money(12.5f),
				rooms.get(0));
		this.cinemaProjections.add(p1);
		this.cinemaProjections.add(p2);
		this.cinemaProjections.add(p3);
		this.cinemaProjections.add(p4);
		this.cinemaProjections.add(p5);
		this.cinemaProjections.add(p6);
		this.cinemaProjections.add(p7);
		// ********* END *********
	}

	/**
	 * METODO STATICO per creare l'istanza della classe
	 * 
	 * Attenzione! Solamente una istanza potrà essere presente
	 * La parola chiave synchronized indica che solo un thread per volta può accedere al
	 * seguente metodo.
	 * @return Cinema 	 Istanza della classe Cinema, se il cinema è già stato
	 *         			 precedentemente istanziato allora il metodo restituirà l'oggetto già
	 *        			 istanziato, viceversa procederà con l'istanziamento di quest ultimo
	 *
	 */
	public static synchronized Cinema getInstance() {
		if (single_instance == null)
			single_instance = new Cinema();

		return single_instance;
	}

	/**
	 * METODO per creare una nuova prenotazione, a partire dalla classe cinema
	 */
	public void createReservation() {
		new Reservation();
	}
	
	/**
	 * METODO per aggiungere una proiezione al cinema
	 * 
	 * @param p 	Proiezione da aggiungere alla lista di proiezioni di cui il cinema
	 *          	dispone
	 */
	public void addProjection(Projection p) {
		cinemaProjections.add(p);
	}

	/**
	 * METODO per rimuovere una proiezione al cinema
	 * 
	 * @param p 	Proiezione da rimuovere alla lista di proiezioni di cui il cinema
	 *          	dispone
	 */
	public void removeProjection(Projection p) {
		cinemaProjections.remove(p);
	}

	/**
	 * METODO per farsi restituire tutte le proiezioni di cui il cinema dispone
	 * 
	 * @return ArrayList<Projection>	 Insieme di tutte le proiezioni del cinema
	 */
	public List<Projection> getProjections() {
		return cinemaProjections;
	}

	/**
	 * 
	 * METODO per restituire le proiezioni di un cinema, inerenti uno specifico film
	 * tramite l'id
	 * 
	 * @param movieId 						 Id del film di cui si vogliono cercare le proiezioni
	 * @return ArrayList<Projection> 		 Insieme delle proiezioni dello specifico film
	 * @throws NoMovieProjectionsException   Eccezione lanciata, qualora il cinema non
	 *                                       abbia quel film, tra i film proiettati
	 */
	public List<Projection> getProjections(int movieId) {
		List<Projection> movieProjections = new ArrayList<Projection>();
		// TODO: Check that movieId is valid
		for (Projection p : cinemaProjections) {
			if (p.getMovie().getId() == movieId) {
				movieProjections.add(p);
			}
		}
		return movieProjections;
	}

	/**
	 * 
	 * METODO per restituire tutti i film che il cinema sta attualmente proiettando
	 * 
	 * @return List<Movie>  Insieme di tutti i film che il cinema sta momentaneamente
	 *         				proiettando
	 */
	public List<Movie> getCurrentlyAvailableMovies() {
		List<Movie> movies = new ArrayList<Movie>();
		for (Projection p : cinemaProjections) {
			boolean alreadyExists = false;
			for (Movie m : movies) {
				if (p.getMovie().getId() == m.getId()) {
					alreadyExists = true;
					break;
				}
			}
			if (!alreadyExists) {
				movies.add(p.getMovie());
			}
		}
		return movies;
	}

	/**
	 * METODO per restituire la lista di film che il cinema proietta, dato il titolo
	 * di un film (o una parte di esso)
	 * 
	 * @param query 			  Titolo del film che si vuole cercare tra le proiezioni del
	 *              			  cinema (o una parte di esso)
	 * @return ArrayList<Movie>   Lista dei film
	 */
	public List<Movie> getCurrentlyAvailableMovies(String query) {
		List<Movie> movies = new ArrayList<Movie>();
		for (Movie m : getCurrentlyAvailableMovies()) {
			if (m.getTitle().toLowerCase().contains(query.toLowerCase())) {
				movies.add(m);
			}
		}
		return movies;
	}

	/**
	 * METODO per restituire un film, dato il suo Id
	 * @param id					Id del film
	 * @return Movie				Film trovato
	 * @throws NoMovieException		Eccezione lanciata qualora non si trovi nessun film
	 * 								con quell'Id
	 */
	public Movie getMovie(int id) throws NoMovieException {
		for (Projection p : cinemaProjections) {
			if (p.getMovie().getId() == id) {
				return p.getMovie();
			}
		}
		throw new NoMovieException(id);
	}

	/**
	 * METODO per resituire una proiezione, dato il suo Id
	 * @param id						Id della proiezione
	 * @return Projection				Proiezione con quello specifico Id
	 * @throws NoProjectionException	Eccezione lanciata qualora non ci sia nessuna proiezione
	 * 									con quell'Id
	 */
	public Projection getProjection(int id) throws NoProjectionException {
		for (Projection p : cinemaProjections) {
			if (p.getId() == id) {
				return p;
			}
		}
		throw new NoProjectionException(id);
	}

	/**
	 * METODO per aggiungere una sala del cinema
	 * 
	 * @param r 	Sala del cinema da aggiungere, all'insieme delle sale del cinema
	 *          	stesso
	 */
	public void addRoom(Room r) {
		rooms.add(r);
	}

	/**
	 * METODO per rimuovere una sala del cinema
	 * 
	 * @param r		 Sala del cinema da rimuovere, dall'insieme delle sale del cinema
	 *          	 stesso
	 */
	public void removeRoom(Room r) throws NoCinemaRoomsException {
		if (rooms.size() > 0)
			rooms.remove(r);
		else
			throw new NoCinemaRoomsException(this.name, this.city, this.address);
	}
	
	/**
	 * METODO per settare/cambiare la "location" in cui si trova il cinema
	 * 
	 * @param city      Citta
	 * @param country   Paese
	 * @param zipCode   Codice comunale
	 * @param address   Indirizzo (Via, numero civico)
	 */
	public void setLocation(String city, String country, String zipCode, String address) {
		this.city = city;
		this.country = country;
		this.zipCode = zipCode;
		this.address = address;
	}

	/**
	 * METODO per farsi dire il numero di sale di cui il cinema è composto
	 * 
	 * @return numberOfRooms
	 */
	public int getNumberOfRooms() {
		return rooms.size();
	}

	/**
	 * METODO per farsi dire le informazioni del luogo in cui il cinema è situato
	 * 
	 * @return location
	 */
	public String getLocation() {
		return address + ", " + city + " - " + zipCode + "  " + country;
	}
}