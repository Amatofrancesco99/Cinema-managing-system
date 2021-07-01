package cinema.model.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import cinema.model.cinema.Room;
import cinema.model.cinema.util.RoomException;

public class RoomRdbDao implements IRoomDao{
	private Connection connection;
	
	public RoomRdbDao(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public Room getRoom(int id) throws SQLException, RoomException {
		String sql = "SELECT * FROM Room WHERE id = ?;";
        PreparedStatement pstatement  = connection.prepareStatement(sql);
        pstatement.setInt(1, id);
        ResultSet result = pstatement.executeQuery();
        Room room = new Room(id, result.getInt("rows"), result.getInt("columns"));
		return room;
	}

	@Override
	public ArrayList<Room> getAllRooms() throws SQLException, RoomException {
		String sql = "SELECT * FROM Room;";
        PreparedStatement pstatement  = connection.prepareStatement(sql);
        ResultSet result = pstatement.executeQuery();
        ArrayList<Room> rooms = new ArrayList<Room>();
        while (result.next()) {
        	rooms.add(new Room(result.getInt("id"), result.getInt("rows"), result.getInt("columns")));
        }
        return rooms;
	}

}
