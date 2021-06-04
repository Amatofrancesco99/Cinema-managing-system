package cinema.model.cinema.util;

@SuppressWarnings("serial")
public class InvalidRoomDimensionsException extends Exception {

	/**
	 * 
	 */

	public InvalidRoomDimensionsException() {
		System.err.println("Dimensioni della stanza invalide: devono essere maggiori di zero.");
	}
	
}