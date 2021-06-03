package cinema.view.webgui;

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
import cinema.model.Movie;
import cinema.model.Projection;
import cinema.model.cinema.Cinema;

@SuppressWarnings("serial")
public class WebGUIServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handleRequest(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handleRequest(req, resp);
	}

	protected void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Cinema myCinema = Cinema.getInstance();
		ArrayList<Movie> films = new ArrayList<>();

		ArrayList<String> directors, cast, genres;
		directors = new ArrayList<>();
		directors.add("Thomas Vinterberg");
		cast = new ArrayList<>();
		cast.add("Mads Mikkelsen");
		cast.add("Thomas Bo Larsen");
		cast.add("Lars Ranthe");
		cast.add("Magnus Millang");
		genres = new ArrayList<>();
		genres.add("Drammatico");
		genres.add("Commedia");
		Movie f = new Movie(1, "Druk - Un altro giro", "C'è una teoria secondo la quale tutti noi siamo nati con una piccola quantità di alcool già presente nel sangue e che, pertanto, una piccola ebbrezza possa aprire le nostre menti al mondo che ci circonda, diminuendo la nostra percezione dei problemi e aumentando la nostra creatività. Rincuorati da questa teoria, Martin e tre suoi amici, tutti annoiati insegnanti delle superiori, intraprendono un esperimento per mantenere un livello costante di ubriachezza durante tutta la giornata lavorativa. Se Churchill vinse la seconda guerra mondiale in preda a un pesante stordimento da alcool, chissà cosa potrebbero fare pochi bicchieri per loro e per i loro studenti?", genres, directors, cast, 4, 117, "https://200mghercianos.files.wordpress.com/2020/12/another-round-druk-thomas-vinteberg-filme-critica-mostra-sp-poster-1.jpg", "https://www.youtube.com/watch?v=hFbDh58QHzw");

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
		
		if (req.getPathInfo().equals("/")) {

			// TODO: search
			if (req.getParameter("query") == null) {
				// Show all films
				films.add(f);
			} else {
				// Filter the films (this is a dumb filter, use it only for debug purposes)
				if (f.getTitle().toLowerCase().contains(req.getParameter("query").toLowerCase())) {
					films.add(f);
				}
			}

			resp.getWriter().write(Rythm.render("index.html", myCinema, (List<Movie>) films, req.getParameter("query")));
		    return;
		} else if (req.getPathInfo().equals("/movie-details")) {
			
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
		} else if (req.getPathInfo().equals("/checkout")) {
			if (Integer.parseInt(req.getParameter("id")) == p2.getId()) {
				resp.getWriter().write(Rythm.render("checkout.html", myCinema, f, p2));
				return;
			}
		}
		
		// Error
		resp.getWriter().write(Rythm.render("index.html", myCinema, (List<Movie>) films, null));
	}

}
