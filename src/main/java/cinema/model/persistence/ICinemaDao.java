package cinema.model.persistence;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Contiene i metodi necessari per mantenere la persistenza dei dati riguardanti
 * le informazioni generali sul cinema e del gestore del cinema.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public interface ICinemaDao {
	/**
	 * Restituisce tutte le informazioni riguardanti il cinema e il gesore del
	 * cinema.
	 * 
	 * @param cinemaId codice identificativo del cinema di cui si vogliono
	 *                 recuperare le infromazioni.
	 * @return tutte le informazioni necessarie sul cinema e sul gesotre del cinema.
	 * @throws SQLException se vengono riscontrati errori nell'interazione col
	 *                      meccanismo di persistenza.
	 */
	public HashMap<String, String> getAllCinemaInfo(int cinemaId) throws SQLException;

	/**
	 * Permette rendere persistente il cambio della password dell'account del
	 * gestore del cinema.
	 * 
	 * @param cinemaId    codice identificativo del cinema di cui si vogliono
	 *                    recuperare le infromazioni.
	 * @param newPassword nuova password dell'account del gestore del cinema.
	 * @throws SQLException se vengono riscontrati errori nell'interazione col
	 *                      meccanismo di persistenza.
	 */
	public void setPassword(int cinemaId, String newPassword) throws SQLException;

	/**
	 * Permette di rendere persistente il cambio della strategia di sconto applicata
	 * nel cinema.
	 * 
	 * @param cinemaId             codice identificativo del cinema di cui si vuole
	 *                             cambiare la strategia di sconto.
	 * @param discountStrategyName nome della strategia di sconto che si vuole
	 *                             applicare nel cinema.
	 * @throws SQLException se vengono riscontrati errori nell'interazione col
	 *                      meccanismo di persistenza.
	 */
	public void setDiscountStrategy(int cinemaId, String discountStrategyName) throws SQLException;
}
