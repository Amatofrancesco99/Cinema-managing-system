package cinema.model.persistence.dao.rdbClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import cinema.model.persistence.dao.interfaces.IDiscountDao;
import cinema.model.reservation.discount.types.DiscountAge;
import cinema.model.reservation.discount.types.DiscountDay;
import cinema.model.reservation.discount.types.DiscountNumberSpectators;

/**
 * Si interfaccia con un database relazionale per implementare la persistenza
 * dei dati degli sconti gestiti dall'applicazione.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class DiscountRdbDao implements IDiscountDao {
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
	public DiscountRdbDao(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Esegue la query per recuperare tutti gli sconti basati sul giorno della
	 * proiezione presenti sul database relazionale.
	 */
	@Override
	public DiscountDay getAllDayDiscounts() throws SQLException {
		String sql = "SELECT * FROM Discount WHERE type = \"DAY\";";
		PreparedStatement pstatement = connection.prepareStatement(sql);
		ResultSet result = pstatement.executeQuery();
		DiscountDay discounts = new DiscountDay(result.getInt("id"));
		while (result.next()) {
			discounts.addDayDiscount(
					LocalDate.parse(result.getString("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
					result.getDouble("percentage"));
		}
		return discounts;
	}

	/**
	 * Esegue la query per recuperare tutti gli sconti basati sull'età dello
	 * spettatore presenti sul database relazionale.
	 */
	@Override
	public DiscountAge getAgeDiscounts() throws SQLException {
		String sql = "SELECT * FROM Discount WHERE type = \"AGE\";";
		PreparedStatement pstatement = connection.prepareStatement(sql);
		ResultSet result = pstatement.executeQuery();
		return new DiscountAge(result.getInt("minage"), result.getInt("maxage"), result.getDouble("percentage"),
				result.getInt("id"));
	}

	/**
	 * Esegue la query per recuperare tutti gli sconti basati sul numero di posti
	 * prenotati presenti sul database relazionale.
	 */
	@Override
	public DiscountNumberSpectators getGroupDiscounts() throws SQLException {
		String sql = "SELECT * FROM Discount WHERE type = \"NUMBER\";";
		PreparedStatement pstatement = connection.prepareStatement(sql);
		ResultSet result = pstatement.executeQuery();
		return new DiscountNumberSpectators(result.getInt("numberpeople"), result.getDouble("percentage"),
				result.getInt("id"));
	}

}
