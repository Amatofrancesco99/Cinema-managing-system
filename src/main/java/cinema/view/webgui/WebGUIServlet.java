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
import cinema.controller.handlers.util.HandlerException;
import cinema.controller.util.NoMovieException;
import cinema.model.Movie;
import cinema.model.cinema.util.RoomException;
import cinema.model.payment.util.PaymentErrorException;
import cinema.model.persistence.util.PersistenceException;
import cinema.model.projection.util.ProjectionException;
import cinema.model.reservation.discount.coupon.util.CouponException;
import cinema.model.reservation.discount.types.util.DiscountException;
import cinema.model.reservation.util.ReservationException;
import cinema.model.reservation.util.SeatAvailabilityException;
import cinema.model.spectator.util.InvalidSpectatorInfoException;

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
			} else if (req.getPathInfo().equals("/set-age-discount")) {
				handleSetAgeDiscount(req, resp);
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
		int projectionId = Integer.parseInt(req.getParameter("id"));
		long reservationId = cinema.createReservation();
		try {
			cinema.setReservationProjection(reservationId, projectionId);
		} catch (ProjectionException | ReservationException | PersistenceException exception) {
			renderError(req, resp);
			return;
		}
		resp.getWriter().write(Rythm.render("checkout.html", cinema, reservationId));
	}

	protected void renderError(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.getWriter().write(Rythm.render("index.html", cinema, null, null));
	}

	protected void renderIndex(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Movie> resultMovies = null;
		String query = req.getParameter("query");
		try {
			if (query == null) {
				resultMovies = cinema.getCurrentlyAvailableMovies();
			} else {
				resultMovies = cinema.getCurrentlyAvailableMovies(query);
			}
		} catch (PersistenceException exception) {
			renderError(req, resp);
			return;
		}
		resp.getWriter().write(Rythm.render("index.html", cinema, resultMovies, query));
	}

	protected void renderMovieDetails(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, NoMovieException {
		int movieId = Integer.parseInt(req.getParameter("id"));
		Movie movie = cinema.getMovie(movieId);

		// Build the data structure used to store the sorted projections
		ArrayList<Integer> sortedProjections = null;
		try {
			sortedProjections = new ArrayList<>(cinema.getMovieProjections(movieId));
		} catch (NoMovieException | PersistenceException exception) {
			renderError(req, resp);
			return;
		}
		Collections.sort(sortedProjections);
		ArrayList<ArrayList<Integer>> schedule = new ArrayList<>();
		LocalDate lastLocalDate = null;
		for (int projection : sortedProjections) {
			LocalDate localDate = null;
			try {
				localDate = cinema.getProjectionDateTime(projection).toLocalDate();
			} catch (ProjectionException | PersistenceException exception) {
				// No need to handle this exception
			}
			if (!Objects.equals(lastLocalDate, localDate)) {
				schedule.add(new ArrayList<Integer>());
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
			// Reset the age discount parameters if seats get modified
			if (cinema.getReservationTypeOfDiscount(reservationId).contentEquals("AGE")) {
				cinema.setReservationNumberPeopleUntilMinAge(reservationId, 0);
				cinema.setReservationNumberPeopleOverMaxAge(reservationId, 0);
			}
			if (req.getParameter("seat-status").equals("selezionato")) {
				cinema.addSeatToReservation(reservationId, row, col);
			} else if (req.getParameter("seat-status").equals("disponibile")) {
				cinema.removeSeatFromReservation(reservationId, row, col);
			} else {
				response = "invalid parameter";
			}
		} catch (DiscountException | RoomException | SeatAvailabilityException | ReservationException exception) {
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

	protected void handleSetAgeDiscount(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		long reservationId = Integer.parseInt(req.getParameter("reservation-id"));

		int numberPeopleUnderMinAge = Integer.parseInt(req.getParameter("age-under"));
		int numberPeopleOverMaxAge = Integer.parseInt(req.getParameter("age-over"));

		String response = "ok";

		try {
			cinema.setReservationNumberPeopleUntilMinAge(reservationId, numberPeopleUnderMinAge);
			cinema.setReservationNumberPeopleOverMaxAge(reservationId, numberPeopleOverMaxAge);
		} catch (DiscountException | ReservationException exception) {
			response = "error";
		}
		resp.getWriter().write(Rythm.render(response));
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
		} catch (HandlerException | InvalidSpectatorInfoException | PaymentErrorException | PersistenceException
				| ReservationException | RoomException | SeatAvailabilityException exception) {
			response = "error";
		}
		resp.getWriter().write(Rythm.render(response));
	}

}
