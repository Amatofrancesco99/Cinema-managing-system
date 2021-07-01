package cinema.model.persistence;

import java.sql.SQLException;
import java.util.ArrayList;
import cinema.model.cinema.Room;
import cinema.model.cinema.util.RoomException;

public interface IRoomDao {
	public Room getRoom(int id) throws SQLException, RoomException;
	public ArrayList<Room> getAllRooms() throws SQLException, RoomException;
}
