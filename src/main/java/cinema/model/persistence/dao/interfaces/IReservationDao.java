package cinema.model.persistence.dao.interfaces;

import java.sql.SQLException;

import cinema.model.cinema.util.RoomException;
import cinema.model.persistence.util.PersistenceException;
import cinema.model.reservation.Reservation;

/**
 * Contiene i metodi necessari per mantenere la persistenza dei dati riguardanti
 * le prenotazioni gestite dal sistema.
 * 
 * <p>
 * Per mantenere la coerenza dei dati persistenti è necessario che in ogni
 * momento ogni reservation abbia un proprio identificativo univoco. Questa
 * classe mette a disposizione un metodo per ottenere l'identificativo
 * dell'utilma prenotazione effettuata e sulla base di questa creare un
 * identificativo valido per qualisasi prenotazione che si vuole creare.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public interface IReservationDao {
	/**
	 * Rende persistente una nuova prenotazione memorizzando l'identificativo che è
	 * reso consistente rispetto a altre possibili prenotazioni.
	 *
	 * <p>
	 * Indipendentemente dal valore degli altri attributi della prenotazione, viene
	 * reso persistente solo l'identificativo. Questo è necessario perchè questo
	 * metodo è chiamato prima che gli altri dati vengano specificati.
	 * 
	 * @param newReservation prenotazione che si vuole rendere persistente.
	 * @throws SQLException se vengono riscontrati errori nell'interazione con il
	 *                      meccanismo di persistenza.
	 */
	public void putEmptyReservation(Reservation newReservation) throws SQLException;

	/**
	 * Rende persistente i dati della prenotazione quando il pagamento di questa è
	 * andato a buon fine.
	 * 
	 * @param reservation prenotazione da rendere persistente.
	 * @throws SQLException         se vengono riscontrati errori nell'interazione
	 *                              con il meccanismo di persistenza.
	 * @throws PersistenceException se i dati riguardanti il film o la proiezione
	 *                              alla quale si riferisce la prenotazione non sono
	 *                              validi.
	 * @throws RoomException        se il numero della sala della proiezione a cui
	 *                              si riferisce la prenotazione non è valida.
	 */
	public void setReservationFields(Reservation reservation) throws SQLException, PersistenceException, RoomException;

	/**
	 * Restituisce l'identificativo dell'ultima prenotazione effettuata e resa
	 * persistente.
	 * 
	 * @return l'identificativo dell'ultima prenotazione memorizzata.
	 * @throws SQLException se vengono riscontrati errori nell'interazione con il
	 *                      meccanismo di persistenza.
	 */
	public long getLastReservationId() throws SQLException;

	/**
	 * Rimuove una prenotazione dal meccanismo di persistenza dei dati quando non va
	 * a buon fine il pagamento di questa.
	 * 
	 * @param reservationId identificativo della proiezione che si vuole eliminare
	 *                      dal meccanismo di perisstenza dei dati.
	 * @throws SQLException se vengono riscontrati errori nell'interazione con il
	 *                      meccanismo di persistenza.
	 */
	public void deleteReservation(long reservationId) throws SQLException;

}
