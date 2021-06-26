package cinema.controller.util;


/** BREVE DESCRIZIONE CLASSE RoomNotExistsException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora si cerchi una sala non esistente all'interno del cinema
 */
@SuppressWarnings("serial")
public class RoomNotExistsException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public RoomNotExistsException(long roomId) {
		System.out.println("La sala con id " + roomId + " non è presente all'interno del cinema.");
	}
	
}