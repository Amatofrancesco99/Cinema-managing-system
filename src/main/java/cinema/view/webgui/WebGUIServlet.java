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

/**
 * Gestisce le richieste dei client web via HTTP interfacciandosi con il
 * controller.
 *
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class WebGUIServlet extends HttpServlet {

	/**
	 * Controller di dominio utilizzato come interfaccia verso il modello.
	 */
	private Cinema cinema;

	/**
	 * Costruttore dell'interfaccia utente web.
	 */
	public WebGUIServlet() {
		this.cinema = new Cinema();
	}

	/**
	 * Inoltra le richieste GET a {@code handleRequest()}.
	 *
	 * Ogni richiesta viene inviata a {@code handleRequest()} che restituisce la
	 * pagina voluta indipendentemente dal metodo HTTP utilizzato per richiederla
	 * (permettendo sia richieste GET che POST in modo da poter testare comodamente
	 * l'applicazione variando i parametri della richiesta direttamente attraverso
	 * la query string dell'URL, senza il bisogno di proxy di debug o comunque altri
	 * software esterni ad un browser standard (es. Chrome)).
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handleRequest(req, resp);
	}

	/**
	 * Inoltra le richieste POST a {@code handleRequest()}.
	 *
	 * Ogni richiesta viene inviata a {@code handleRequest()} che restituisce la
	 * pagina voluta indipendentemente dal metodo HTTP utilizzato per richiederla
	 * (permettendo sia richieste GET che POST in modo da poter testare comodamente
	 * l'applicazione variando i parametri della richiesta direttamente attraverso
	 * la query string dell'URL, senza il bisogno di proxy di debug o comunque altri
	 * software esterni ad un browser standard (es. Chrome)).
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		handleRequest(req, resp);
	}

	/**
	 * Gestisce le richiete provenienti dai client degli spettatori.
	 *
	 * Ogni richiesta viene smistata in base al percorso dell'URL e viene inoltrata
	 * al metodo specifico per quella data pagina.
	 *
	 * Se la pagina richiesta non esiste viene mostrato un messaggio di errore.
	 *
	 * @param req  parametri della richiesta.
	 * @param resp risposta del server alla richiesta.
	 * @throws ServletException se si verificano errori durante la gestione della
	 *                          richiesta.
	 * @throws IOException      se non risulta possibile scrivere nel buffer di
	 *                          risposta alla richiesta.
	 */
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

	/**
	 * Gestisce le richieste corrispondenti alla pagina di checkout (/checkout).
	 *
	 * Viene creata una nuova prenotazione (vuota) per la proiezione con id
	 * {@code id} e mostrata la pagina di checkout che permette di selezionare i
	 * posti desiderati all'interno della sala e di concludere l'acquisto
	 * successivamente.
	 *
	 * Se si verificano errori durante la lettura dei dati dalla persistenza o
	 * comunque in una qualsiasi fase dell'inizializzazione di una prenotazione
	 * viene mostrato un messaggio di errore.
	 *
	 * @param req  parametri della richiesta.
	 * @param resp risposta del server alla richiesta.
	 * @throws ServletException se si verificano errori durante la gestione della
	 *                          richiesta.
	 * @throws IOException      se non risulta possibile scrivere nel buffer di
	 *                          risposta alla richiesta.
	 */
	protected void renderCheckout(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, ProjectionException {
		int projectionId = Integer.parseInt(req.getParameter("id"));
		long reservationId;
		try {
			reservationId = cinema.createReservation();
			cinema.setReservationProjection(reservationId, projectionId);
		} catch (ProjectionException | ReservationException | PersistenceException exception) {
			renderError(req, resp);
			return;
		}
		resp.getWriter().write(Rythm.render("checkout.html", cinema, reservationId));
	}

	/**
	 * Mostra una pagina di errore.
	 *
	 * @param req  parametri della richiesta.
	 * @param resp risposta del server alla richiesta.
	 * @throws ServletException se si verificano errori durante la gestione della
	 *                          richiesta.
	 * @throws IOException      se non risulta possibile scrivere nel buffer di
	 *                          risposta alla richiesta.
	 */
	protected void renderError(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.getWriter().write(Rythm.render("index.html", cinema, null, null));
	}

	/**
	 * Gestisce le richieste corrispondenti alla pagina principale (/).
	 *
	 * Se non viene specificata nessuna {@code query} corrispondente a un film, la
	 * pagina mostra tutti i film per i quali sono presenti delle proiezioni
	 * programmate in futuro. Altrimenti viene mostrato l'elenco dei film che
	 * contengono nel titolo la stringa {@code query}.
	 *
	 * Se si verificano errori durante la lettura dei dati dalla persistenza viene
	 * mostrato un messaggio di errore.
	 *
	 * @param req  parametri della richiesta.
	 * @param resp risposta del server alla richiesta.
	 * @throws ServletException se si verificano errori durante la gestione della
	 *                          richiesta.
	 * @throws IOException      se non risulta possibile scrivere nel buffer di
	 *                          risposta alla richiesta.
	 */
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

	/**
	 * Gestisce le richieste corrispondenti alla pagina che mostra i dettagli di un
	 * determinato film (/movie-details).
	 *
	 * Se non viene specificato nessun {@code id} di un film viene mostrato un
	 * messaggio di errore, altrimenti la pagina mostra i dettagli del film e le
	 * relative proiezioni in ordine cronologico, raggruppate per data.
	 *
	 * Se si verificano errori durante la lettura dei dati dalla persistenza viene
	 * mostrato un messaggio di errore.
	 *
	 * @param req  parametri della richiesta.
	 * @param resp risposta del server alla richiesta.
	 * @throws ServletException se si verificano errori durante la gestione della
	 *                          richiesta.
	 * @throws IOException      se non risulta possibile scrivere nel buffer di
	 *                          risposta alla richiesta.
	 */
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

	/**
	 * Gestisce le richieste corrispondenti all'aggiornamento dello stato dei posti
	 * per una determinata proiezione (/update-seat-status).
	 *
	 * La richiesta deve contenere nel parametro {@code seat-id} una stringa nel
	 * formato seat-0-1 (posto 1 della fila 0) e un parametro {@code seat-status}
	 * che può assumere i valori {@code selezionato} o {@code disponibile}. Il posto
	 * verrà aggiornato per la prenotazione corrente (con id {@code reservation-id})
	 * a seconda del valore del parametro di stato.
	 *
	 * Se i valori dei parametri non sono validi viene inviato un messaggio di
	 * errore.
	 *
	 * Se si verificano errori durante la lettura dei dati dalla persistenza viene
	 * inviato un messaggio di errore.
	 *
	 * @param req  parametri della richiesta.
	 * @param resp risposta del server alla richiesta.
	 * @throws ServletException se si verificano errori durante la gestione della
	 *                          richiesta.
	 * @throws IOException      se non risulta possibile scrivere nel buffer di
	 *                          risposta alla richiesta.
	 */
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

	/**
	 * Gestisce le richieste corrispondenti all'aggiornamento del carrello di una
	 * prenotazione (/get-checkout-info).
	 *
	 * La richiesta deve contenere nel parametro {@code reservation-id} l'id di una
	 * prenotazione. La risposta contiene al suo interno i valori richiesti per
	 * aggiornare lo stato del carrello mostrato allo spettatore (si veda il
	 * corrispondente script JavaScript per maggiori informazioni).
	 *
	 * Se il valore del parametro non è valido viene inviato un messaggio di errore.
	 *
	 * Se si verificano errori durante la lettura dei dati dalla persistenza viene
	 * inviato un messaggio di errore.
	 *
	 * @param req  parametri della richiesta.
	 * @param resp risposta del server alla richiesta.
	 * @throws ServletException se si verificano errori durante la gestione della
	 *                          richiesta.
	 * @throws IOException      se non risulta possibile scrivere nel buffer di
	 *                          risposta alla richiesta.
	 */
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

	/**
	 * Gestisce le richieste corrispondenti all'aggiornamento dello stato dei
	 * parametri relativi allo sconto di tipo {@code AGE} (/set-age-discount).
	 *
	 * La richiesta deve contenere nel parametro {@code reservation-id} l'id di una
	 * prenotazione. I parametri {@code age-under} e {@code age-over} sono usati per
	 * impostare il numero di persone sotto l'età minima e sopra l'età massima per
	 * avere lo sconto di tipo {@code AGE}.
	 *
	 * Se i valori dei parametri non sono validi viene inviato un messaggio di
	 * errore.
	 *
	 * Se si verificano errori durante la lettura dei dati dalla persistenza viene
	 * inviato un messaggio di errore.
	 *
	 * @param req  parametri della richiesta.
	 * @param resp risposta del server alla richiesta.
	 * @throws ServletException se si verificano errori durante la gestione della
	 *                          richiesta.
	 * @throws IOException      se non risulta possibile scrivere nel buffer di
	 *                          risposta alla richiesta.
	 */
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

	/**
	 * Gestisce le richieste corrispondenti all'inserimento di un codice coupon per
	 * la prenotazione corrente (/apply-coupon).
	 *
	 * La richiesta deve contenere nel parametro {@code reservation-id} l'id di una
	 * prenotazione. Il parametro {@code coupon-code} è usato per inserire un coupon
	 * nella prenotazione corrente e ottenerne il relativo sconto sull'importo
	 * totale.
	 *
	 * Se i valori dei parametri non sono validi (es. il coupon non esiste o è già
	 * stato utilizzato) viene inviato un messaggio di errore.
	 *
	 * Se si verificano errori durante la lettura dei dati dalla persistenza viene
	 * inviato un messaggio di errore.
	 *
	 * @param req  parametri della richiesta.
	 * @param resp risposta del server alla richiesta.
	 * @throws ServletException se si verificano errori durante la gestione della
	 *                          richiesta.
	 * @throws IOException      se non risulta possibile scrivere nel buffer di
	 *                          risposta alla richiesta.
	 */
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

	/**
	 * Gestisce le richieste corrispondenti all'avvio del processo di completamento
	 * della prenotazione, pagamento e invio dell'e-mail allo spettatore contenente
	 * la ricevuta di avvenuta prenotazione (/buy).
	 *
	 * La richiesta deve contenere nel parametro {@code reservation-id} l'id di una
	 * prenotazione. Vengono impostati i vari campi necessari al corretto
	 * completamento di una prenotazione in corso (si veda il corrispondente script
	 * JavaScript per maggiori informazioni); viene poi avviato il processo di
	 * pagamento e l'invio (asincrono) dell'e-mail.
	 *
	 * Se i valori dei parametri non sono validi o si riscontra un errore nel
	 * processo di pagamento viene inviato un messaggio di errore.
	 *
	 * Se si verificano errori durante la lettura dei dati dalla persistenza viene
	 * inviato un messaggio di errore.
	 *
	 * @param req  parametri della richiesta.
	 * @param resp risposta del server alla richiesta.
	 * @throws ServletException se si verificano errori durante la gestione della
	 *                          richiesta.
	 * @throws IOException      se non risulta possibile scrivere nel buffer di
	 *                          risposta alla richiesta.
	 */
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
