package cinema.controller.util;

public class NoCinemaRoomsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoCinemaRoomsException(String name, String city, String address) {
		System.err.println("Il cinema " + name + " , situato a " + city + " " + address +
				" , non ha stanze da eliminare. ");
	}
	
}
