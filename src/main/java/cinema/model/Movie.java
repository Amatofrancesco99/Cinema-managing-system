package cinema.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Movie {

	private int id, rating, minAgeSee;
	private long numberPeopleInsertRating;
	private String title, description, duration, imageURL, trailerURL;
	private ArrayList<String> directors, cast, genres;
	private LocalDate releaseDate;
	
	public Movie (int id, String title, String duration, int minAgeSee, LocalDate releaseDate) {
		this.id = id;
		this.title = title;
		this.rating = 0;
		this.numberPeopleInsertRating = 0;
		this.duration = duration;
		this.minAgeSee = minAgeSee;
		this.releaseDate = releaseDate;
		directors = new ArrayList<String>();
		cast = new ArrayList<String>();
		genres = new ArrayList<String>();
	}
	
	//setters
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void addDirector(String director) {
		directors.add(director);
	}
	
	public void addCast(String member) {
		cast.add(member);
	}
	
	public void addGenres(String genre) {
		genres.add(genre);
	}
	
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	public void setTrailerURL(String trailerURL) {
		this.trailerURL = trailerURL;
	}
	
	public void setRating(int rating) {
		this.rating=rating;
	}
	
	// Metodo per poter permettere di aggiornare il rating del film, magari da un utente
	// questa prima implementazione non richiede nessuna verifica su chi stia votando 
	// (un utente non molto sveglio però potrebbe votare infinite volte facendo alzare o abbassare
	// il ranking)...chiaramente ci potrebbero essere tecniche migliori 
	// ( ad esempio andando a prendere il voto del film da diversi siti di 
	// "ranking" dei film, sfruttando così l'ETL e i DWH per elaborare queste
	// informazioni e aggiornare il rating del film ogni tot tempo ).
	// Più semplicemente, ma più costoso (hw) un display che a fine film ti faccia dare 
	// un parere e qualcuno che verifichi che non venga fatta n volte dallo stesso utente 
	// una votazione.
	public void updateRating(int vote) {
		this.numberPeopleInsertRating++;
		//approssimazione del voto
		this.rating = (int) ((this.rating+vote)/numberPeopleInsertRating);
	}
	
	//getters
	public int getId() {
		return id;
	}

	public int getRating() {
		return rating;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getImageURL() {
		return imageURL;
	}

	public String getTrailerURL() {
		return trailerURL;
	}

	public String getDuration() {
		return duration;
	}

	public int getMinAgeSee() {
		return minAgeSee;
	}
	
	public LocalDate getReleaseDate() {
		return releaseDate;
	}
	
	public ArrayList<String> getDirectors() {
		return directors;
	}

	public ArrayList<String> getCast() {
		return cast;
	}

	public ArrayList<String> getGenres() {
		return genres;
	}
	
}