package cinema.model.persistence.dao.rdbClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import cinema.model.cinema.Room;
import cinema.model.cinema.util.RoomException;
import cinema.model.persistence.dao.interfaces.IRoomDao;

/**
 * Si interfaccia con un database relazionale per implementare la persistenza
 * dei dati delle sale cinematografiche.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class RoomRdbDao implements IRoomDao {

	/**
	 * Connessione al database.
	 */
	private Connection connection;

	/**
	 * Costruttore dell'interfaccia verso il database relazionale.
	 * 
	 * @param connection connessione al database relazionale che impelemta la
	 *                   persistenza delle informazioni.
	 */
	public RoomRdbDao(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Esegue la query al database relazionale per recuperare le informazioni sulla
	 * sala identificata da {@code id}.
	 */
	@Override
	public Room getRoom(int id) throws SQLException, RoomException {
		String sql = "SELECT * FROM Room WHERE id = ?;";
		PreparedStatement pstatement = connection.prepareStatement(sql);
		pstatement.setInt(1, id);
		ResultSet result = pstatement.executeQuery();
		Room room = new Room(id, result.getInt("rows"), result.getInt("columns"));
		return room;
	}

	/**
	 * Esegue la query per recuperare tutte le sale presenti sul database
	 * relazionale.
	 */
	@Override
	public ArrayList<Room> getAllRooms() throws SQLException, RoomException {
		String sql = "SELECT * FROM Room;";
		PreparedStatement pstatement = connection.prepareStatement(sql);
		ResultSet result = pstatement.executeQuery();
		ArrayList<Room> rooms = new ArrayList<Room>();
		while (result.next()) {
			rooms.add(new Room(result.getInt("id"), result.getInt("rows"), result.getInt("columns")));
		}
		return rooms;
	}

}
