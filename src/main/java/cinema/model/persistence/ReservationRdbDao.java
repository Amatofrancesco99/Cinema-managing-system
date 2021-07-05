package cinema.model.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import cinema.model.cinema.util.RoomException;
import cinema.model.persistence.util.PersistenceException;
import cinema.model.reservation.Reservation;

/**
 * Si interfaccia con un database relazionale per implementare la persistenza
 * delle informazioni sulle prenotazioni gestite dall'applicazione.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class ReservationRdbDao implements IReservationDao {
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
	public ReservationRdbDao(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Esegue la query sul database relazionale per implementare la persistenza
	 * delle informazioni di una prenotazinoe una volta che il relativo pagamento è
	 * andato a buon fine.
	 */
	@Override
	public void setReservationFields(Reservation reservation) throws SQLException, PersistenceException, RoomException {
		String sql = "UPDATE Reservation SET date = ?, projection = ?, name = ?, surname = ?, email = ?, paymentcardowner = ?, paymentcard = ?, coupon = ?, discount = ?, numberpeopleunderage = ?, numberpeopleoverage = ? WHERE id = ?;";
		PreparedStatement pstatement = connection.prepareStatement(sql);
		pstatement.setString(1, reservation.getDate().toString());
		pstatement.setLong(2, reservation.getProjection().getId());
		pstatement.setString(3, reservation.getPurchaser().getName());
		pstatement.setString(4, reservation.getPurchaser().getSurname());
		pstatement.setString(5, reservation.getPurchaser().getEmail());
		pstatement.setString(6, reservation.getPaymentCard().getOwner());
		pstatement.setString(7, reservation.getPaymentCard().getNumber());
		if (reservation.getCoupon() != null)
			pstatement.setString(8, reservation.getCoupon().getCode());
		pstatement.setInt(9, reservation.getDiscountId());
		pstatement.setInt(10, reservation.getNumberPeopleUntilMinAge());
		pstatement.setLong(11, reservation.getNumberPeopleOverMaxAge());
		pstatement.setLong(12, reservation.getProgressive());
		pstatement.executeUpdate();
		new OccupiedSeatRdbDao(connection).putOccupiedSeatsFromReservation(reservation);
	}

	/**
	 * Esegue la query sul database relazionale per implementare la persistenza
	 * della creazione della prenotazione.
	 */
	@Override
	public void putEmptyReservation(Reservation newReservation) throws SQLException {
		String sql = "INSERT INTO Reservation(id) VALUES(?);";
		PreparedStatement pstatement = connection.prepareStatement(sql);
		pstatement.setLong(1, newReservation.getProgressive());
		pstatement.executeUpdate();
	}

	/**
	 * Esegue la query sul database relazionale per recuperare l'identificativo
	 * dell'ultima prenotazione effettuata.
	 */
	@Override
	public long getLastReservationId() throws SQLException {
		String sql = "SELECT MAX(id) AS maxid FROM Reservation;";
		PreparedStatement pstatement = connection.prepareStatement(sql);
		return pstatement.executeQuery().getLong("maxid");
	}

	/**
	 * Esegue la query sul database relazionale per eliminare una prenotazione.
	 * Questo metodo viene chiamato quando il pagamento di un aprenotazione non va a
	 * buon fine.
	 */
	@Override
	public void deleteReservation(long reservationId) throws SQLException {
		String sql = "DELETE FROM Reservation WHERE id = ?;";
		PreparedStatement pstatement = connection.prepareStatement(sql);
		pstatement.setLong(1, reservationId);
		pstatement.executeUpdate();
	}

}
