package cinema.model.persistence;

import java.sql.SQLException;
import java.util.ArrayList;
import cinema.model.projection.Projection;

public interface IProjectionDao {
	public Projection getProjection(int id) throws SQLException;
	public ArrayList<Projection> getAllProjectionsByMovieId(int movieId) throws SQLException;
	public ArrayList<Projection> getAllProjections() throws SQLException;
	public void removeProjection(int id) throws SQLException;
	public void putProjection(Projection newProjection) throws SQLException;
}
