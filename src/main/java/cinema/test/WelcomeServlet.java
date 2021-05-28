package cinema.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rythmengine.Rythm;

@SuppressWarnings("serial")
public class WelcomeServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getPathInfo().equals("/")) {

			ArrayList<Film> films = new ArrayList<>();

			films.add(new Film("Quasi amici 1", 0));
			films.add(new Film("Quasi amici 2", 1));
			films.add(new Film("Quasi amici 3", 2));
			films.add(new Film("Quasi amici 4", 3));
			films.add(new Film("Quasi amici 5", 4));
			films.add(new Film("Quasi amici 6", 5));
			films.add(new Film("Quasi amici 7", 5));

			resp.getWriter().write(Rythm.render("index.html", (List<Film>) (films)));
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.getWriter().write("POST");
	}

}
