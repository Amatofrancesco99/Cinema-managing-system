package cinema.model;

import java.util.ArrayList;

import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Movie {

	private int id;
	private String title;
	private String description;
	private ArrayList<String> genres;
	private ArrayList<String> directors;
	private ArrayList<String> cast;
	private int rating;
	private int duration;
	private String imageURL;
	private String trailerURL;

}
