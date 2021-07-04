package cinema.model.persistence;

import java.sql.SQLException;
import java.util.HashMap;

public interface ICinemaDao {
	public HashMap<String, String> getAllCinemaInfo(int cinemaId) throws SQLException;
	public void setPassword(int cinemaId, String newPassword)throws SQLException;
	public void setDiscountStrategy(int cinemaId, String discountStrategyName)throws SQLException;
}
