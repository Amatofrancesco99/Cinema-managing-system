package cinema.controller.util;

@SuppressWarnings("serial")
public class NoProjectionException extends Exception {

	/**
	 * 
	 */

	public NoProjectionException(int id) {
		System.err.println("La proiezione con id " + id + " non esiste.");
	}
	
}
