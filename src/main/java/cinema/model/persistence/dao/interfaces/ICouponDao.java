package cinema.model.persistence.dao.interfaces;

import java.sql.SQLException;
import java.util.ArrayList;

import cinema.model.reservation.discount.coupon.Coupon;
import cinema.model.reservation.discount.coupon.util.CouponException;

/**
 * Contiene i metodi necessari per mantenere la persistenza dei dati riguardanti
 * i coupon.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public interface ICouponDao {
	/**
	 * Restituisce tutti i coupon presenti nel meccanismo di persistenza.
	 * 
	 * @return i coupon presenti nel meccanismo di persistenza.
	 * @throws SQLException    se vengono riscontrati errori nell'interazione con il
	 *                         meccanismo di persistenza.
	 * @throws CouponException se il codice del coupon non è valido, ovvero non di
	 *                         almeno otto caratteri.
	 */
	public ArrayList<Coupon> getAllCoupons() throws SQLException, CouponException;

	/**
	 * Restituisce un coupon sulla base del suo codice promozionale.
	 * 
	 * @param promoCode codice promozionale del coupon da restituire.
	 * @return un coupon sulla base del suo codice promozionale o null se non è
	 *         presente.
	 * @throws SQLException    se vengono riscontrati errori nell'interazione con il
	 *                         meccanismo di persistenza.
	 * @throws CouponException se il codice del coupon identificato non è valido,
	 *                         ovvero non è di almeno otto caratteri.
	 */
	public Coupon getCoupon(String promoCode) throws SQLException, CouponException;

	/**
	 * Imposta un coupon come usato attraverso il meccanismo di persistenza.
	 * 
	 * @param promoCode codice promozionale del coupon da segnalare come utilizzato.
	 * @throws SQLException se vengono riscontrati errori nell'interazione con il
	 *                      meccanismo di persistenza.
	 */
	public void setCouponUsed(String promoCode) throws SQLException;

}
