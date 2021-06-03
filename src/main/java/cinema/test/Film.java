package cinema.test;

import java.util.ArrayList;

import lombok.*;

@AllArgsConstructor
@Data
public class Film {

	private int id;
	private int rating;
	private int duration;
	private String title;
	private String description;
	private String imageURL;
	private String trailerURL;
	private ArrayList<String> directors;
	private ArrayList<String> cast;
	private ArrayList<String> genres;

}
