package cinema.model.exceptions;

import cinema.model.Movie;

public class NoMovieProjectionsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoMovieProjectionsException(Movie m) {
		System.err.println("Il film " + m.getName() + " non ha proiezioni.");
	}

}
