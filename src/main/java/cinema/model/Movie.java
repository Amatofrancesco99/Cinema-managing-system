package cinema.model;

import java.util.Date;

public class Movie {

	private String name,cast,producer;
	private int duration, minAgeSee;
	private String urlImage, urlTrailer; 
	private Date releaseDate;
	
	public Movie (String name, String cast, String producer, int duration, int minAgeSee, String urlImage, String urlTrailer,  Date releaseDate) {
		this.name = name;
		this.cast = cast;
		this.producer = producer;
		this.duration = duration;
		this.minAgeSee = minAgeSee;
		this.releaseDate = releaseDate;
		this.urlImage = urlImage;
		this.urlTrailer = urlTrailer;
	}
	
	// Getters
	public String getName() {
		return name;
	}
	public int getDuration() {
		return duration;
	}
	public Date getReleaseDate() {
		return releaseDate;
	}
	public String getCast() {
		return cast;
	}
	public String getProducer() {
		return producer;
	}
	public int getMinAgeSee() {
		return minAgeSee;
	}
	public String getUrlImage() {
		return urlImage;
	}
}