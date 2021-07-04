package cinema.model.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class CinemaRdbDao implements ICinemaDao{
	private Connection connection;
	
	public CinemaRdbDao(Connection connection) {
		this.connection = connection;
	}

	@Override
	public HashMap<String, String> getAllCinemaInfo(int cinemaId) throws SQLException {
		String sql = "SELECT * FROM Cinema WHERE id = ?;";
        PreparedStatement pstatement  = connection.prepareStatement(sql);
        pstatement.setInt(1, cinemaId);
        ResultSet result = pstatement.executeQuery();
        HashMap<String, String> cinemaInfo = new HashMap<String, String>();
        cinemaInfo.put("name", result.getString("name"));
        cinemaInfo.put("city", result.getString("city"));
        cinemaInfo.put("country", result.getString("country"));
        cinemaInfo.put("zipCode", result.getString("zipCode"));
        cinemaInfo.put("address", result.getString("address"));
        cinemaInfo.put("email", result.getString("email"));
        cinemaInfo.put("mailPassword", result.getString("mailPassword"));
        cinemaInfo.put("adminPassword", result.getString("adminPassword"));
        cinemaInfo.put("logoURL", result.getString("logoURL"));
        cinemaInfo.put("discountStrategy", result.getString("discountstrategy"));
        return cinemaInfo;
	}

	
	@Override
	public void setPassword(int cinemaId, String newPassword) throws SQLException {
		String sql = "UPDATE Cinema SET adminPassword = ? WHERE id = ?;";
        PreparedStatement pstatement  = connection.prepareStatement(sql);
        pstatement.setString(1, newPassword);
        pstatement.setInt(2, cinemaId);
        pstatement.executeUpdate();
	}

	
	@Override
	public void setDiscountStrategy(int cinemaId, String discountStrategyName) throws SQLException {
		String sql = "UPDATE Cinema SET discountstrategy = ? WHERE id = ?;";
        PreparedStatement pstatement  = connection.prepareStatement(sql);
        pstatement.setString(1, discountStrategyName);
        pstatement.setInt(2, cinemaId);
        pstatement.executeUpdate();
	}

}
