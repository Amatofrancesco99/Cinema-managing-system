package cinema.model.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import cinema.model.reservation.discount.types.DiscountAge;
import cinema.model.reservation.discount.types.DiscountDay;
import cinema.model.reservation.discount.types.DiscountNumberSpectators;

public class DiscountRdbDao implements IDiscountDao{
	private Connection connection;

	public DiscountRdbDao(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public DiscountDay getAllDayDiscounts() throws SQLException {
		String sql = "SELECT * FROM Discount WHERE type = \"DAY\";";
        PreparedStatement pstatement  = connection.prepareStatement(sql);
        ResultSet result = pstatement.executeQuery();
        DiscountDay discounts = new DiscountDay(result.getInt("id"));
        while (result.next()) {
        	discounts.addDayDiscount(LocalDate.parse(result.getString("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd")), result.getDouble("percentage"));
        }
        return discounts;
	}

	@Override
	public DiscountAge getAgeDiscounts() throws SQLException {
		String sql = "SELECT * FROM Discount WHERE type = \"AGE\";";
        PreparedStatement pstatement  = connection.prepareStatement(sql);
        ResultSet result = pstatement.executeQuery();
        return new DiscountAge(result.getInt("minage"), result.getInt("maxage"), result.getDouble("percentage"), result.getInt("id"));
	}

	@Override
	public DiscountNumberSpectators getGroupDiscounts() throws SQLException {
		String sql = "SELECT * FROM Discount WHERE type = \"NUMBER\";";
        PreparedStatement pstatement  = connection.prepareStatement(sql);
        ResultSet result = pstatement.executeQuery();
        return new DiscountNumberSpectators(result.getInt("numberpeople"), result.getDouble("percentage"), result.getInt("id"));
	}

}
