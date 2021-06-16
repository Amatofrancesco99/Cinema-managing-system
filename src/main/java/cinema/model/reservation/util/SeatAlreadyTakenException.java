package cinema.model.reservation.util;

import cinema.model.cinema.Room;


/**BREVE DESCRIZIONE CLASSE SeatAlreadyTakenException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora si cerchi di occupare un posto già occupato
 */
@SuppressWarnings("serial")
public class SeatAlreadyTakenException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public SeatAlreadyTakenException(int row, int col) {
		System.err.println("Il posto " + Room.rowIndexToRowLetter(row) + (col+1) + " è già stato occupato.");
	}

}
