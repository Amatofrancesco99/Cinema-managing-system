package cinema.controller.util;


/** BREVE DESCRIZIONE CLASSE ProjectionIsNoLongerProjectedException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora si cerchi una proiezione la cui data di proiezione è inferiore
 * a quella odierna
 */
@SuppressWarnings("serial")
public class ProjectionIsNoLongerProjectedException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public ProjectionIsNoLongerProjectedException(int id) {
		System.out.println("La proiezione " + id + " non è più disponibile.");
	}

}
