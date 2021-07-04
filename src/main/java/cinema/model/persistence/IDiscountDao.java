package cinema.model.persistence;

import java.sql.SQLException;

import cinema.model.reservation.discount.types.DiscountAge;
import cinema.model.reservation.discount.types.DiscountDay;
import cinema.model.reservation.discount.types.DiscountNumberSpectators;

/**
 * Contiene i metodi necessari per mantenere la persistenza dei dati riguardanti
 * gli sconti.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public interface IDiscountDao {
	/**
	 * Restituisce tutti gli sconti che applicano una riduzione di prezzo in base
	 * alla data di proiezione presenti nel database.
	 * 
	 * @return gli sconti presenti nel database che applicanola riduzione di prezzo
	 *         sulla base della data di proiezione.
	 * @throws SQLException se vengono riscontrati errori nell'interazione con il
	 *                      meccanismo di persistenza.
	 */
	public DiscountDay getAllDayDiscounts() throws SQLException;

	/**
	 * Restituisce tutti gli sconti che applicano una riduzione di prezzo in base
	 * all'età dello spettaore.
	 * 
	 * @return gli sconti presenti nel database che applicanola riduzione di prezzo
	 *         sulla base dell'età dello spettatore.
	 * @throws SQLException se vengono riscontrati errori nell'interazione con il
	 *                      meccanismo di persistenza.
	 */
	public DiscountAge getAgeDiscounts() throws SQLException;

	/**
	 * Restituisce tutti gli sconti che applicano una riduzione di prezzo in base al
	 * nuemero di biglietti comprati.
	 * 
	 * @return gli sconti presenti nel database che applicanola riduzione di prezzo
	 *         sulla base della al nuemero di biglietti comprati.
	 * @throws SQLException se vengono riscontrati errori nell'interazione con il
	 *                      meccanismo di persistenza.
	 */
	public DiscountNumberSpectators getGroupDiscounts() throws SQLException;
}
