package cinema.test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rythmengine.Rythm;

import cinema.model.Money;
import cinema.model.Projection;
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
				films.add(new Film("Druk - Un altro giro", 5));
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
			
			ArrayList<Projection> p = new ArrayList<>();
			
			Projection p1 = new Projection(123, LocalDateTime.parse("2021-06-04T22:30:00"), new Money(12.5f));
			Projection p2 = new Projection(183, LocalDateTime.parse("2021-06-01T20:15:00"), new Money(12.5f));
			Projection p3 = new Projection(193, LocalDateTime.parse("2021-06-01T22:30:00"), new Money(12.5f));
			Projection p4 = new Projection(109, LocalDateTime.parse("2021-06-02T22:30:00"), new Money(12.5f));
			Projection p5 = new Projection(743, LocalDateTime.parse("2021-06-02T23:30:00"), new Money(12.5f));
			Projection p6 = new Projection(233, LocalDateTime.parse("2021-06-02T19:00:00"), new Money(12.5f));
			Projection p7 = new Projection(184, LocalDateTime.parse("2021-06-03T08:05:00"), new Money(12.5f));
			
			p.add(p1);
			p.add(p2);
			p.add(p3);
			p.add(p4);
			p.add(p5);
			p.add(p6);
			p.add(p7);
			
			// Build the data structure used to store the sorted projections
            Collections.sort(p);
            ArrayList<ArrayList<Projection>> schedule = new ArrayList<>();
            LocalDate lastLocalDate = null;
            for(Projection projection : p) {
            	LocalDate localDate = projection.getDateTime().toLocalDate();
            	if (!Objects.equals(lastLocalDate, localDate)) {
            		schedule.add(new ArrayList<Projection>());
            		lastLocalDate = localDate;
            	}
            	schedule.get(schedule.size() - 1).add(projection);
            }
			
			if (Integer.parseInt(req.getParameter("id")) == f.getId()) {
				resp.getWriter().write(Rythm.render("movie-details.html", myCinema, f, schedule));
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
