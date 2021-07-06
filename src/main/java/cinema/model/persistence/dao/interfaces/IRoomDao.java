package cinema.model.persistence.dao.interfaces;

import java.sql.SQLException;
import java.util.ArrayList;
import cinema.model.cinema.Room;
import cinema.model.cinema.util.RoomException;

/**
 * Contiene i metodi necessari per mantenere la persistenza dei dati riguardanti
 * le sale del cinema.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public interface IRoomDao {
	/**
	 * Restituisce la sala identificata dal proprio numero di sala attarverso il
	 * meccanismo di persisstenza dei dati.
	 * 
	 * @param id identificativo della sala cinematografica che si vuole recuperare
	 *           dal meccanismo di eprsistenza dei dati.
	 * @return la sala identificata da {@code id}.
	 * @throws SQLException  se vengono riscontrati errori nell'interazione con il
	 *                       meccanismo di persistenza.
	 * @throws RoomException se le informazioni della stanza memorizzate nel
	 *                       meccanismo di eprsistenza non sono valide.
	 */
	public Room getRoom(int id) throws SQLException, RoomException;

	/**
	 * Restituisce utte le sale cinematografiche memorizzate dal meccanismo di
	 * perisstenza dei dati.
	 * 
	 * @return tutte le sale memorizzate nel meccanismo di persistenza.
	 * @throws SQLException  se vengono riscontrati errori nell'interazione con il
	 *                       meccanismo di persistenza.
	 * @throws RoomException se le informazioni della stanza memorizzate nel
	 *                       meccanismo di eprsistenza non sono valide.
	 */
	public ArrayList<Room> getAllRooms() throws SQLException, RoomException;
}
