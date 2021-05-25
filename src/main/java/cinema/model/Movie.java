package cinema.model;

import java.util.Date;

public class Movie {

	private String name,cast,producer;
	private int duration, minAgeSee;
	private Date releaseDate;
	
	public Movie (String name, String cast, String producer, int duration, int minAgeSee, Date releaseDate) {
		this.name=name;
		this.cast=cast;
		this.producer=producer;
		this.duration=duration;
		this.minAgeSee=minAgeSee;
		this.releaseDate=releaseDate;
	}
}