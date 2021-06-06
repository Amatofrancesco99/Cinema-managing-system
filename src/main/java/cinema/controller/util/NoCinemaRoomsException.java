package cinema.controller.util;

/** BREVE DESCRIZIONE CLASSE NoCinemaRoomsException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class NoCinemaRoomsException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public NoCinemaRoomsException(String name, String city, String address) {
		System.err.println("Il cinema " + name + " , situato a " + city + " " + address +
				" , non ha stanze da eliminare. ");
	}
	
}
