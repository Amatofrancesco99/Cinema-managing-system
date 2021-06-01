package cinema.test;

import java.util.ArrayList;

public class Film {
	private int id, rating;
	private String title, description, imageURL, trailerURL, duration;
	private ArrayList<String> directors, cast, genres;

	public Film(String title, int rating) {
		this.title = title;
		id = 281688;
		this.rating = rating;
		description = "C'è una teoria secondo la quale tutti noi siamo nati con una piccolaquantità di alcool già presente nel sangue e che, pertanto, una piccola ebbrezza possa aprire le nostre menti al mondo che ci circonda, diminuendo la nostra percezione dei problemi e aumentando la nostra creatività. Rincuorati da questa teoria, Martin e tre suoi amici, tutti annoiati insegnanti delle superiori, intraprendono un esperimento per mantenere un livello costante di ubriachezza durante tutta la giornata lavorativa. Se Churchill vinse la seconda guerra mondiale in preda a un pesante stordimento da alcool, chissà cosa potrebbero fare pochi bicchieri per loro e per i loro studenti?";
		imageURL = "https://200mghercianos.files.wordpress.com/2020/12/another-round-druk-thomas-vinteberg-filme-critica-mostra-sp-poster-1.jpg";
		trailerURL = "https://www.youtube.com/watch?v=K1buCCC7e48";
		duration = "1h45";
		directors = new ArrayList<String>();
		directors.add(" Thomas Vinterberg");
		cast = new ArrayList<String>();
		cast.add("Mads Mikkelsen");
		cast.add("Thomas Bo Larsen");
		cast.add("Lars Ranthe");
		cast.add("Magnus Millang");
		genres = new ArrayList<String>();
		genres.add("Drammatico");
		genres.add("Commedia");
		
	}

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
