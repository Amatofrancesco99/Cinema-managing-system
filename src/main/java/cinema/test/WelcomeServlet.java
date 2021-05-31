package cinema.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rythmengine.Rythm;

import cinema.model.cinema.Cinema;

@SuppressWarnings("serial")
public class WelcomeServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Cinema myCinema = Cinema.getInstance();
		ArrayList<Film> films = new ArrayList<>();
		
		if (req.getPathInfo().equals("/")) {
			
			// Create myCinema istance

			//films.add(new Film("Quasi amici 1", 0));
			//films.add(new Film("Quasi amici 2", 1));
			//films.add(new Film("Quasi amici 3", 2));
			//films.add(new Film("Quasi amici 4", 3));
			//films.add(new Film("Quasi amici 5", 4));
			//films.add(new Film("Quasi amici 6", 5));
			
			// TODO: search
			if (req.getParameter("query") == null) {
				// Show all films
				films.add(new Film("Quasi amici", 5));
			} else {
				// Filter the films (this is a dumb filter, use it only for debug purposes)
				if ("quasi amici".contains(req.getParameter("query").toLowerCase())) {
					films.add(new Film("Quasi amici", 5));
				}
			}

			resp.getWriter().write(Rythm.render("index.html", myCinema, (List<Film>) films, req.getParameter("query")));
		    return;
		} else if (req.getPathInfo().equals("/movie-details")) {
			Film f = new Film("Quasi amici", 4);
			
			if (Integer.parseInt(req.getParameter("id")) == f.getId()) {
				resp.getWriter().write(Rythm.render("movie-details.html", myCinema, f));
				return;
			}
		}
		
		// Error
		resp.getWriter().write(Rythm.render("index.html", myCinema, (List<Film>) films, null));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.getWriter().write("POST");
	}

}
