package cinema.model.projection;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

import cinema.model.cinema.util.RoomException;
import cinema.model.projection.util.ProjectionException;
import cinema.model.Movie;
import cinema.model.cinema.PhysicalSeat;
import cinema.model.cinema.Room;

/**
 * Comprende tutte le informazioni, e metodi, necessari per rappresentare una
 * proiezione effettuata dal cinema.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class Projection implements Comparable<Projection> {
	/**
	 * Codice identificativo del film.
	 */
	private int id;
	/**
	 * Film associato.
	 */
	private Movie movie;
	/**
	 * Sala in cui verrà proiettato il film.
	 */
	private Room room;
	/**
	 * Data e ora.
	 */
	private LocalDateTime dateTime;
	/**
	 * Prezzo del film per persona.
	 */
	private double price;
	/**
	 * Posti della sala in cui il film è proiettato.
	 */
	private ArrayList<ArrayList<ProjectionSeat>> seats;

	/**
	 * Costruttore della Proiezione.
	 * 
	 * @param id       codice identificativo del film.
	 * @param movie    film associato.
	 * @param dateTime data e ora.
	 * @param price    prezzo del film per persona.
	 * @param room     sala in cui verrà proiettato il film.
	 */
	public Projection(int id, Movie movie, LocalDateTime dateTime, double price, Room room) {
		this.id = id;
		this.movie = movie;
		this.dateTime = dateTime;
		this.price = Math.round(price * 100.0) / 100.0;
		this.room = room;
		this.seats = new ArrayList<ArrayList<ProjectionSeat>>();
		for (int i = 0; i < room.getNumberOfRows(); i++) {
			ArrayList<ProjectionSeat> row = new ArrayList<ProjectionSeat>();
			for (int j = 0; j < room.getNumberOfCols(); j++) {
				row.add(new ProjectionSeat(room.getSeat(i, j), true));
			}
			seats.add(row);
		}
	}

	/**
	 * Costruttore di default.
	 */
	public Projection() {
		this.seats = new ArrayList<ArrayList<ProjectionSeat>>();
	}

	/**
	 * Imposta l'id di una proiezione.
	 * 
	 * @param id codice identificativo del film.
	 * @throws ProjectionException qualora l'id inserito sia negativo.
	 */
	public void setId(int id) throws ProjectionException {
		if (id < 0)
			throw new ProjectionException("L'ID della proiezione deve essere non negativo");
		this.id = id;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public void setRoom(Room room) {
		this.room = room;
		if (seats.size() != 0) {
			seats.removeAll(seats);
		}
		for (int i = 0; i < room.getNumberOfRows(); i++) {
			ArrayList<ProjectionSeat> row = new ArrayList<ProjectionSeat>();
			for (int j = 0; j < room.getNumberOfCols(); j++) {
				row.add(new ProjectionSeat(room.getSeat(i, j), true));
			}
			seats.add(row);
		}
	}

	/**
	 * Imposta la data della proiezione.
	 * 
	 * @param dateTime data e ora della proiezione.
	 * @throws ProjectionException se la data della proiezione inserita è già
	 *                             passata.
	 */
	public void setDateTime(LocalDateTime dateTime) throws ProjectionException {
		if (dateTime.isBefore(LocalDateTime.now()))
			throw new ProjectionException("La data della proiezione non può essere nel passato.");
		this.dateTime = dateTime;
	}

	/**
	 * Aggiunge il prezzo alla proiezione.
	 * 
	 * @param price prezzo del film per persona.
	 * @throws ProjectionException qualora il prezzo inserito sia negativo,o nullo.
	 */
	public void setPrice(double price) throws ProjectionException {
		if (price <= 0)
			throw new ProjectionException("Il prezzo del biglietto per una proiezione non può essere negativo.");
		this.price = Math.round(price * 100.0) / 100.0;
	}

	/**
	 * Verifica se un posto specifico sia libero.
	 * 
	 * @param row coordinata riga.
	 * @param col coordinata colonna.
	 * @return True: libero, False: occupato.
	 * @throws RoomException qualora il posto selezionato non esista.
	 */
	public boolean checkIfSeatIsAvailable(int row, int col) throws RoomException {
		try {
			return seats.get(row).get(col).isAvailable();
		} catch (IndexOutOfBoundsException e) {
			throw new RoomException(
					"Il posto selezionato (" + Room.rowIndexToRowLetter(row) + "-" + (col + 1) + " non esiste.");
		}
	}

	/**
	 * Restituisce il numero di posti liberi per la stanza presa in considerazione.
	 * 
	 * @return numero di posti disponibili/liberi.
	 * @throws RoomException qualora vi siano errori legati alla gestione della sala
	 *                       del cinema.
	 */
	public int getNumberAvailableSeat() throws RoomException {
		int availableSeats = 0;
		for (int i = 0; i < this.getRoom().getNumberOfRows(); i++) {
			for (int j = 0; j < this.getRoom().getNumberOfCols(); j++) {
				if (checkIfSeatIsAvailable(i, j)) {
					availableSeats++;
				}
			}
		}
		return availableSeats;
	}

	/**
	 * Occupa il posto della sala in cui è fatta la proiezione.
	 * 
	 * @param row coordinata riga.
	 * @param col coordinata colonna.
	 * @return True: posto occupato con successo, False: fallimento nell'occupare il
	 *         posto.
	 * @throws RoomException qualora vi siano errori legati alla gestione della sala
	 *                       del cinema.
	 */
	public boolean takeSeat(int row, int col) throws RoomException {
		if (checkIfSeatIsAvailable(row, col)) {
			seats.get(row).get(col).setAvailable(false);
			return true;
		}
		return false;
	}

	/**
	 * Libera il posto di una sala.
	 * 
	 * @param row coordinata riga.
	 * @param col coordinata colonna.
	 * @return True: posto rilasciato con successo, False: fallimento nel rilasciare
	 *         il posto.
	 * @throws RoomException qualora vi siano errori legati alla gestione della sala
	 *                       del cinema.
	 */
	public boolean freeSeat(int row, int col) throws RoomException {
		if (!checkIfSeatIsAvailable(row, col)) {
			seats.get(row).get(col).setAvailable(true);
			return true;
		}
		return false;
	}

	/**
	 * Restituisce un posto, date le coordinate.
	 * 
	 * @param row coordinata riga.
	 * @param col coordinata colonna.
	 * @return posto fisico.
	 * @throws RoomException qualora vi siano errori legati alla gestione della sala
	 *                       del cinema.
	 */
	public PhysicalSeat getPhysicalSeat(int row, int col) throws RoomException {
		return this.getSeats().get(row).get(col).getPhysicalSeat();
	}

	/**
	 * Restituisce le coordinate di un posto.
	 * 
	 * @param s posto fisico.
	 * @return coordinate del posto.
	 * @throws RoomException qualora vi siano errori legati alla gestione della sala
	 *                       del cinema.
	 */
	public String getSeatCoordinates(PhysicalSeat s) throws RoomException {
		for (int i = 0; i < room.getNumberOfRows(); i++) {
			for (int j = 0; j < room.getNumberOfCols(); j++) {
				if (getPhysicalSeat(i, j) == s)
					return Room.rowIndexToRowLetter(i) + (j + 1);
			}
		}
		return null;
	}

	@Override
	public int compareTo(Projection projection) {
		return dateTime.compareTo(projection.getDateTime());
	}

	@Override
	public String toString() {
		int availableSeats = 0;
		try {
			availableSeats = this.getNumberAvailableSeat();
		} catch (RoomException exception) {
		}

		String dayOfWeek = getDateTime().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ITALIAN);
		String month = getDateTime().getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN);

		return "Sala " + this.getRoom().getNumber() + "\n" + "Data: " + dayOfWeek.toUpperCase().charAt(0)
				+ dayOfWeek.substring(1) + " " + this.getDateTime().getDayOfMonth() + " "
				+ month.toUpperCase().charAt(0) + month.substring(1) + " " + this.getDateTime().getYear() + "   "
				+ "Ora: " + String.format("%02d", this.getDateTime().getHour()) + ":"
				+ String.format("%02d", this.getDateTime().getMinute()) + "\n" + "Prezzo: "
				+ String.format("%.2f €", this.getPrice()) + "\n" + "Posti disponibili: " + availableSeats + "\n";
	}

	public double getPrice() {
		return price;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public Room getRoom() {
		return room;
	}

	public int getId() {
		return id;
	}

	public ArrayList<ArrayList<ProjectionSeat>> getSeats() {
		return seats;
	}

	public Movie getMovie() {
		return movie;
	}

}
