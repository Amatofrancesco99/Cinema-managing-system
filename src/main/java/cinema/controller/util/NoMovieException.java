package cinema.controller.util;

@SuppressWarnings("serial")
public class NoMovieException extends Exception {

	/**
	 * 
	 */

	public NoMovieException(int id) {
		System.err.println("Il film con id " + id + " non esiste.");
	}
	
}
