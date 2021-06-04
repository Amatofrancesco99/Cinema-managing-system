package cinema.view.webgui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rythmengine.Rythm;

import cinema.controller.Cinema;
import cinema.controller.util.NoMovieException;
import cinema.controller.util.NoProjectionException;
import cinema.model.Movie;
import cinema.model.projection.Projection;

@SuppressWarnings("serial")
public class WebGUIServlet extends HttpServlet {

	private Cinema cinema;

	public WebGUIServlet() {
		this.cinema = Cinema.getInstance();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handleRequest(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handleRequest(req, resp);
	}

	protected void handleRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			if (req.getPathInfo().equals("/")) {
				renderIndex(req, resp);
			} else if (req.getPathInfo().equals("/movie-details")) {
				renderMovieDetails(req, resp);
			} else if (req.getPathInfo().equals("/checkout")) {
				renderCheckout(req, resp);
			} else {
				renderError(req, resp);
			}
		} catch (NoMovieException | NoProjectionException e) {
			renderError(req, resp);
		}
	}

	protected void renderCheckout(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, NoProjectionException {
		Projection projection = cinema.getProjection(Integer.parseInt(req.getParameter("id")));
		resp.getWriter().write(Rythm.render("checkout.html", cinema, projection.getMovie(), projection));
	}

	protected void renderError(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.getWriter().write(Rythm.render("index.html", cinema, null, null));
	}

	protected void renderIndex(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Movie> resultMovies;
		String query = req.getParameter("query");
		if (query == null) {
			resultMovies = cinema.getCurrentlyAvailableMovies();
		} else {
			resultMovies = cinema.getCurrentlyAvailableMovies(query);
		}
		resp.getWriter().write(Rythm.render("index.html", cinema, resultMovies, query));
	}

	protected void renderMovieDetails(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, NoMovieException {
		int movieId = Integer.parseInt(req.getParameter("id"));
		Movie movie = cinema.getMovie(movieId);

		// Build the data structure used to store the sorted projections
		ArrayList<Projection> sortedProjections = new ArrayList<>(cinema.getProjections(movieId));
		Collections.sort(sortedProjections);
		ArrayList<ArrayList<Projection>> schedule = new ArrayList<>();
		LocalDate lastLocalDate = null;
		for (Projection projection : sortedProjections) {
			LocalDate localDate = projection.getDateTime().toLocalDate();
			if (!Objects.equals(lastLocalDate, localDate)) {
				schedule.add(new ArrayList<Projection>());
				lastLocalDate = localDate;
			}
			schedule.get(schedule.size() - 1).add(projection);
		}
		resp.getWriter().write(Rythm.render("movie-details.html", cinema, movie, schedule));
	}

}
