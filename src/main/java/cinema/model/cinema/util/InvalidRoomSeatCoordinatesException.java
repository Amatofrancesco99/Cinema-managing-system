package cinema.model.cinema.util;

import cinema.model.cinema.Room;

/** BREVE DESCRIZIONE CLASSE InvalidSeatCoordinates
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora si vogliano occupare, quando si chiama la proiezione, 
 * posti le cui coordinate non sono valide
 */
@SuppressWarnings("serial")
public class InvalidRoomSeatCoordinatesException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public InvalidRoomSeatCoordinatesException(int row, int col) {
		System.out.println("La sala non ha al suo interno il posto " + Room.rowIndexToRowLetter(row) + (col+1));
	}

}