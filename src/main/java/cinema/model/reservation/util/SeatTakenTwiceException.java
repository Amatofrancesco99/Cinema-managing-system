package cinema.model.reservation.util;

import cinema.model.cinema.Room;

/** BREVE DESCRIZIONE ECCEZIONE SeatTakenTwiceException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora si inserisca per due volte lo stesso posto
 */
@SuppressWarnings("serial")
public class SeatTakenTwiceException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public SeatTakenTwiceException(int row, int col) {
		System.out.println("Hai inserito per due volte lo stesso posto ( " + Room.rowIndexToRowLetter(row) + (col+1) + " ).");
	}

}