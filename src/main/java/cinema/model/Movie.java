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
	// private int minAgeSee;  
	/* oppure si potrebbe ricavare l'età da cui guardare
	* il film dall'attributo genere, anche se forse è troppo "generico" (non tutti i film
	* di un genere possono essere visti da minori e non è detto che un film con quel genere
	* non possa essere visto da minori)*/
}
