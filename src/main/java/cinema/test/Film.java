package cinema.test;

import java.util.ArrayList;

public class Film {
	private int id, rating;
	private String title, description, urlImg, urlTrailer, duration;
	private ArrayList<String> directedBy, cast;

	public Film(String title, int rating) {
		this.title = title;
		id = 281688;
		this.rating = rating;
		description = "L'incontro tra due mondi apparentemente lontani. Dopo un incidente di parapendio che lo ha reso paraplegico, il ricco aristocratico Philippe assume Driss, ragazzo di periferia appena uscito dalla prigione, come badante personale.";
		urlImg = "http://lnx.whipart.it/imagesart9/1382899075-quasi_amici.jpg";
		urlTrailer = "https://www.youtube.com/watch?v=K1buCCC7e48";
		duration = "1h45";
		directedBy = new ArrayList<String>();
		directedBy.add("Olivier Nakache");
		directedBy.add("Éric Toledano");
		cast = new ArrayList<String>();
		cast.add("François Cluzet");
		cast.add("Omar Sy");
		cast.add("Anne Le Ny");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrlImg() {
		return urlImg;
	}

	public void setUrlImg(String urlImg) {
		this.urlImg = urlImg;
	}

	public String getUrlTrailer() {
		return urlTrailer;
	}

	public void setUrlTrailer(String urlTrailer) {
		this.urlTrailer = urlTrailer;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public ArrayList<String> getDirectedBy() {
		return directedBy;
	}

	public void setDirectedBy(ArrayList<String> directedBy) {
		this.directedBy = directedBy;
	}

	public ArrayList<String> getCast() {
		return cast;
	}

	public void setCast(ArrayList<String> cast) {
		this.cast = cast;
	}
	
}
