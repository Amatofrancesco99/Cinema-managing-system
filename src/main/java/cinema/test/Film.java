package cinema.test;

import java.util.ArrayList;

public class Film {
	private int id, rating;
	private String title, description, imageURL, trailerURL, duration;
	private ArrayList<String> directors, cast;

	public Film(String title, int rating) {
		this.title = title;
		id = 281688;
		this.rating = rating;
		description = "L'incontro tra due mondi apparentemente lontani. Dopo un incidente di parapendio che lo ha reso paraplegico, il ricco aristocratico Philippe assume Driss, ragazzo di periferia appena uscito dalla prigione, come badante personale.";
		imageURL = "http://lnx.whipart.it/imagesart9/1382899075-quasi_amici.jpg";
		trailerURL = "https://www.youtube.com/watch?v=K1buCCC7e48";
		duration = "1h45";
		directors = new ArrayList<String>();
		directors.add("Olivier Nakache");
		directors.add("�ric Toledano");
		cast = new ArrayList<String>();
		cast.add("Fran�ois Cluzet");
		cast.add("Omar Sy");
		cast.add("Anne Le Ny");
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
	
}
