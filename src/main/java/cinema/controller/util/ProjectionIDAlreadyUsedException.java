package cinema.controller.util;


/** BREVE DESCRIZIONE CLASSE ProjectionIDAlreadyUsedException 
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora il film inserito non abbia proiezioni disponibili
 */
@SuppressWarnings("serial")
public class ProjectionIDAlreadyUsedException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public ProjectionIDAlreadyUsedException(int id) {
		System.out.println("La proiezione con id " + id + " è già presente.");
	}
	
}