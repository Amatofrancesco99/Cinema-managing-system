package cinema.model.cinema.util;

/** BREVE DESCRIZIONE CLASSE InvalidRoomDimensionsException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Come il nome della classe lascia intuire, essa serve per gestire il caso in cui
 * si voglia istanziare una stanza (Room) con dimensioni invalide, ossia un numero di 
 * righe/colonne pari a zero.
 */
@SuppressWarnings("serial")
public class InvalidRoomDimensionsException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public InvalidRoomDimensionsException() {
		System.out.println("Dimensioni della stanza invalide: devono essere maggiori di zero.");
	}
	
}