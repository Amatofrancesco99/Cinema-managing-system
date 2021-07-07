package cinema.model.persistence.dao.rdbClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import cinema.model.persistence.dao.interfaces.ICinemaDao;

/**
 * Si interfaccia con un database relazionale per implementare la persistenza
 * delle informazioni sul cinema e sul gestore del cinema.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class CinemaRdbDao implements ICinemaDao {
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
	public CinemaRdbDao(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Esegue la query per recuperare tutte le informazioni del cinema e del gestore
	 * del cinema.
	 */
	@Override
	public HashMap<String, String> getAllCinemaInfo(int cinemaId) throws SQLException {
		String sql = "SELECT * FROM Cinema WHERE id = ?;";
		PreparedStatement pstatement = connection.prepareStatement(sql);
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

	/**
	 * Esegue la query che implementa la persistenza del cambio di password per il
	 * login del gestore del cinema su un database relazionale.
	 */
	@Override
	public void setPassword(int cinemaId, String newPassword) throws SQLException {
		String sql = "UPDATE Cinema SET adminPassword = ? WHERE id = ?;";
		PreparedStatement pstatement = connection.prepareStatement(sql);
		pstatement.setString(1, newPassword);
		pstatement.setInt(2, cinemaId);
		pstatement.executeUpdate();
	}

	/**
	 * Esegue la query che implemeta la persistenza del cambio della strategia degli
	 * sconti applicati nel cinema su un database relazionale.
	 */
	@Override
	public void setDiscountStrategy(int cinemaId, String discountStrategyName) throws SQLException {
		String sql = "UPDATE Cinema SET discountstrategy = ? WHERE id = ?;";
		PreparedStatement pstatement = connection.prepareStatement(sql);
		pstatement.setString(1, discountStrategyName);
		pstatement.setInt(2, cinemaId);
		pstatement.executeUpdate();
	}

}
