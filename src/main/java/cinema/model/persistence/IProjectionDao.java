package cinema.model.persistence;

import java.sql.SQLException;
import java.util.ArrayList;

import cinema.model.persistence.util.PersistenceException;
import cinema.model.projection.Projection;

public interface IProjectionDao {
	public Projection getProjection(int id) throws SQLException, PersistenceException;
	public ArrayList<Projection> getAllProjectionsByMovieId(int movieId) throws SQLException, PersistenceException;
	public ArrayList<Projection> getAllProjections() throws SQLException, PersistenceException;
	public void removeProjection(int id) throws SQLException;
	public void putProjection(Projection newProjection) throws SQLException;
}
