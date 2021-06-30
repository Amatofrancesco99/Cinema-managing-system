package cinema.view.webgui;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
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
import cinema.model.Movie;
import cinema.model.cinema.util.RoomException;
import cinema.model.projection.Projection;
import cinema.model.projection.util.ProjectionException;
import cinema.model.reservation.discount.coupon.util.CouponException;
import cinema.model.reservation.util.ReservationException;
import cinema.model.reservation.util.SeatAvailabilityException;

@SuppressWarnings("serial")
public class WebGUIServlet extends HttpServlet {

	private Cinema cinema;

	public WebGUIServlet() {
		this.cinema = new Cinema();
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
			} else if (req.getPathInfo().equals("/update-seat-status")) {
				handleUpdateSeatStatus(req, resp);
			} else if (req.getPathInfo().equals("/get-checkout-info")) {
				handleGetCheckoutInfo(req, resp);
			} else if (req.getPathInfo().equals("/apply-coupon")) {
				handleApplyCoupon(req, resp);
			} else if (req.getPathInfo().equals("/buy")) {
				handleBuy(req, resp);
			} else {
				renderError(req, resp);
			}
		} catch (NoMovieException | ProjectionException exception) {
			renderError(req, resp);
		}
	}

	protected void renderCheckout(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, ProjectionException {
		Projection projection = cinema.getProjection(Integer.parseInt(req.getParameter("id")));
		long reservationId = cinema.createReservation();
		try {
			cinema.setReservationProjection(reservationId, projection.getId());
		} catch (ProjectionException | ReservationException e) {
			renderError(req, resp);
			return;
		}
		resp.getWriter().write(Rythm.render("checkout.html", cinema, projection.getMovie(), projection, reservationId));
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

	protected void handleUpdateSeatStatus(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		long reservationId = Integer.parseInt(req.getParameter("reservation-id"));
		String[] splittedSeat = req.getParameter("seat-id").split("-");
		int row = Integer.parseInt(splittedSeat[1]);
		int col = Integer.parseInt(splittedSeat[2]);
		String response = "ok";
		try {
			if (req.getParameter("seat-status").equals("selezionato")) {
				cinema.addSeatToReservation(reservationId, row, col);
			} else if (req.getParameter("seat-status").equals("disponibile")) {
				cinema.removeSeatFromReservation(reservationId, row, col);
			} else {
				response = "invalid parameter";
			}
		} catch (RoomException | SeatAvailabilityException | ReservationException exception) {
			response = "error";
		}
		resp.getWriter().write(Rythm.render(response));
	}

	protected void handleGetCheckoutInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		long reservationId = Integer.parseInt(req.getParameter("reservation-id"));

		ArrayList<String> responseTokens = new ArrayList<>();
		responseTokens.add("ok");

		try {
			double fullPrice = cinema.getReservationFullPrice(reservationId);
			double couponDiscount = cinema.getReservationCouponDiscount(reservationId);
			double totalAmount = cinema.getReservationTotalAmount(reservationId);
			double discountAmount = fullPrice - totalAmount - couponDiscount;
			responseTokens.add(String.valueOf(cinema.getReservationNSeats(reservationId)));
			responseTokens.add(String.format("%.2f", fullPrice));
			responseTokens.add(cinema.getReservationTypeOfDiscount(reservationId));
			responseTokens.add(String.format("%.2f", discountAmount));
			try {
				responseTokens.add(cinema.getReservationCouponCode(reservationId));
			} catch (NullPointerException exception) {
				responseTokens.add("no coupon");
			}
			responseTokens.add(String.format("%.2f", couponDiscount));
			responseTokens.add(String.format("%.2f", totalAmount));
		} catch (ReservationException exception) {
			responseTokens.clear();
			responseTokens.add("error");
		}
		resp.getWriter().write(Rythm.render(String.join("\n", responseTokens)));
	}

	protected void handleApplyCoupon(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		long reservationId = Integer.parseInt(req.getParameter("reservation-id"));
		String couponCode = req.getParameter("coupon-code");

		String response = "ok";

		try {
			cinema.setReservationCoupon(reservationId, couponCode);
		} catch (CouponException | ReservationException exception) {
			response = "error";
		}
		resp.getWriter().write(Rythm.render(String.join("\n", response)));
	}

	protected void handleBuy(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long reservationId = Integer.parseInt(req.getParameter("reservation-id"));

		String name = req.getParameter("name");
		String surname = req.getParameter("surname");
		String email = req.getParameter("e-mail");

		String ccName = req.getParameter("cc-name");
		String ccNumber = req.getParameter("cc-number");
		String ccExpiration = req.getParameter("cc-expiration");
		String ccCvv = req.getParameter("cc-cvv");

		String response = "ok";

		try {
			YearMonth ccExpirationDate = YearMonth.parse(ccExpiration, DateTimeFormatter.ofPattern("yyyy-MM"));
			cinema.setReservationPurchaser(reservationId, name, surname, email);
			cinema.setReservationPaymentCard(reservationId, ccNumber, ccName, ccCvv, ccExpirationDate);
			cinema.buyReservation(reservationId);
			cinema.sendReservationEmail(reservationId);
		} catch (Exception exception) {
			response = "error";
		}
		resp.getWriter().write(Rythm.render(response));
	}

}
