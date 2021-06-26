package cinema.model.projection.util;


/** BREVE DESCRIZIONE CLASSE InvalidPriceException
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Eccezione lanciata qualora si voglia inserire un prezzo negativo o pari a zero ad una 
 * proiezione
 */
@SuppressWarnings("serial")
public class InvalidPriceException extends Exception {

	/** 
	 * METODO utilizzato per stampare le informazioni dell'eccezione
	 */
	public InvalidPriceException() {
		System.out.println("Il prezzo di una proiezione deve essere maggiore di 0€.");
	}
	
}