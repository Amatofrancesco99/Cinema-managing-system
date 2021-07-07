package cinema.model.persistence.dao.interfaces;

import java.sql.SQLException;

import cinema.model.cinema.util.RoomException;
import cinema.model.projection.Projection;
import cinema.model.reservation.Reservation;

/**
 * Contiene i metodi necessari per mantenere la persistenza dei dati riguardanti
 * i posti occupati per le varie proiezioni rese persistenti.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public interface IOccupiedSeatDao {
	/**
	 * Rende persistente l'occupazione dei posti di una determinata proiezione.
	 * 
	 * @param projection proiezione di cui si vuole rendere persistente
	 *                   l'occupazione dei posti.
	 * @throws SQLException se vengono riscontrati errori nell'interazione col
	 *                      meccanismo di persistenza.
	 */
	public void setOccupiedSeats(Projection projection) throws SQLException;

	/**
	 * Occupa localmente i posti di una proiezioni in modo da sincronizzare i dati
	 * locali con il meccanismo di persistenza dei dati.
	 * 
	 * @param reservation prenotazione di cui si vuole occupare i posti.
	 * @throws SQLException  se vengono riscontrati errori nell'interazione col
	 *                       meccanismo di persistenza.
	 * @throws RoomException se vengono riscontrati errori nell'identificazione dei
	 *                       posti all'interno della sala.
	 */
	public void putOccupiedSeatsFromReservation(Reservation reservation) throws SQLException, RoomException;

	/**
	 * Controlla se il posto della proiezione {@code projectionId} identificato
	 * dalla fila {@code row} e dal posto {@code column} è occupato.
	 * 
	 * @param projectionId identificativo della proiezione alla.
	 * @param row          fila del posto che si vuole verificare se è occupato.
	 * @param column       posto nella fila del posto che si vuole verificare se è
	 *                     occupato.
	 * @return true se il posto è libero, false se è occupato.
	 * @throws SQLException se vengono riscontrati errori nell'interazione col
	 *                      meccanismo di persistenza.
	 */
	public boolean getSeatOccupationStatus(int projectionId, int row, int column) throws SQLException;

}
