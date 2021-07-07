package cinema.model.persistence.dao.interfaces;

import java.sql.SQLException;
import java.util.ArrayList;

import cinema.model.cinema.util.RoomException;
import cinema.model.persistence.util.PersistenceException;
import cinema.model.projection.Projection;

/**
 * Contiene i metodi necessari per mantenere la persistenza dei dati riguardanti
 * le proiezioni dei film gestiti dal cinema.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public interface IProjectionDao {

	/**
	 * Restituisce una determinata proiezione sulla base del suo identificativo
	 * attraverso il suo identificativo.
	 * 
	 * @param id identificativo della proiezione che si vuole ottenere dal
	 *           meccanismo di persistenza.
	 * @return la proiezione identificata da {@code id} o null se nessuna proiezione
	 *         con l'identificativo specificato è presente.
	 * @throws SQLException         se vengono riscontrati errori nell'interazione
	 *                              con il meccanismo di persistenza.
	 * @throws PersistenceException se il film o la stanza dove era programmata la
	 *                              proiezione non sono più presenti nel meccanismo
	 *                              di persistenza dei dati.
	 * @throws RoomException        se non esiste la sala associata a quella
	 *                              proiezione
	 */
	public Projection getProjection(int id) throws SQLException, PersistenceException, RoomException;

	/**
	 * Restituisce tutte le proiezioni riguardanti un film identificato da
	 * {@code movieId} attraverso il meccanismo di persistenza.
	 * 
	 * @param movieId identificativo del film di cui si vogliono ottenere le
	 *                proiezioni.
	 * @return tutte le proiezioni del film identificato da {@code movieId}
	 * @throws SQLException         se vengono riscontrati errori nell'interazione
	 *                              con il meccanismo di persistenza.
	 * @throws PersistenceException se vi è un problema nel raggiungere il servizio
	 *                              offerto dal database
	 * @throws RoomException        se non esiste la sala in cui il film è
	 *                              proiettato
	 */
	public ArrayList<Projection> getAllProjectionsByMovieId(int movieId)
			throws SQLException, PersistenceException, RoomException;

	/**
	 * Restituisce tutte le proiezioni gestite dal cinema attraverso il meccanismo
	 * di persistenza.
	 * 
	 * @return tutte le proiezioni gestite dal cinema.
	 * @throws SQLException         se vengono riscontrati errori nell'interazione
	 *                              con il meccanismo di persistenza.
	 * @throws PersistenceException se vengono riscontrati errori nella gestione
	 *                              della persistenza.
	 */
	public ArrayList<Projection> getAllProjections() throws SQLException, PersistenceException;

	/**
	 * Elimina dal meccanismo di persistenza una proiezione identificata da
	 * {@code id}.
	 * 
	 * @param id identificativo della proiezione da eliminare dal meccanismo di
	 *           persistenza.
	 * @throws SQLException se vengono riscontrati errori nell'interazione con il
	 *                      meccanismo di persistenza.
	 */
	public void removeProjection(int id) throws SQLException;

	/**
	 * Rende persistente l'aggiunta di una nuova priezione.
	 * 
	 * @param newProjection proiezione la cui aggiunta si vuole rendere persistente.
	 * @throws SQLException se vengono riscontrati errori nell'interazione con il
	 *                      meccanismo di persistenza.
	 */
	public void putProjection(Projection newProjection) throws SQLException;

}
