package cinema.model.projection;

import java.time.LocalDateTime;
import java.util.ArrayList;

import cinema.model.money.Money;
import cinema.model.cinema.util.InvalidRoomSeatCoordinates;
import cinema.model.Movie;
import cinema.model.cinema.PhysicalSeat;
import cinema.model.cinema.Room;
import lombok.*;


/** BREVE DESCRIZIONE CLASSE Projection
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Questa classe comprende tutte le informazioni e metodi
 * che servono per rappresentare una proiezione che viene
 * effettuata dal cinema.
 */
@Data
@AllArgsConstructor
public class Projection implements Comparable<Projection> {
	
	
	/** ATTRIBUTI
	 * @param id			Id
	 * @param movie			Film associato
	 * @param room			Sala in cui il film verrà proiettato
	 * @param dateTime		Data e ora
	 * @param price			Prezzo
	 * @param seats			Posti della sala in cui il film è proiettato
	 */
	private int id;
	private Movie movie;
	private Room room;
	private LocalDateTime dateTime;
	private Money price;
	private ArrayList<ArrayList<ProjectionSeat>> seats;
	
	
	/**
	 * COSTRUTTORE 
	 * @param id
	 * @param movie
	 * @param dateTime
	 * @param price
	 * @param room
	 */
	public Projection(int id, Movie movie, LocalDateTime dateTime, Money price, Room room) {
		this.id = id;
		this.movie = movie;
		this.dateTime = dateTime;
		this.price = price;
		this.room = room;
		this.seats = new ArrayList<ArrayList<ProjectionSeat>>();
		for(int i = 0; i < room.getNumberRows(); i++) {
			ArrayList<ProjectionSeat> row = new ArrayList<ProjectionSeat>();
			for(int j = 0; j < room.getNumberCols(); j++) {
				row.add(new ProjectionSeat(room.getSeat(i, j), true));
			}
			seats.add(row);
		}
	}
	
	
	/**
	 * METODO che serve per verificare se un posto specifico
	 * è libero.
	 * @param row, col		Coordinate 
	 * @return				True: libero, False: occupato
	 * @throws InvalidRoomSeatCoordinates 
	 */
	public boolean verifyIfSeatAvailable(int row, int col) throws InvalidRoomSeatCoordinates {
		try {
			return seats.get(row).get(col).isAvailable();
		}
		catch (IndexOutOfBoundsException e) {
			{
				throw new InvalidRoomSeatCoordinates(row, col);
			}
		}
	}
	
	
	/**
	 * METODO per farsi restituire il numero di posti liberi per quella stanza
	 * @return int  Numero di posti disponibili/liberi
	 * @throws InvalidRoomSeatCoordinates 
	 */
	public int getNumberAvailableSeat() throws InvalidRoomSeatCoordinates {
		int availableSeats = 0;
		for (int i = 0 ; i < this.getRoom().getNumberRows() ; i++) {
			for (int j = 0 ; j < this.getRoom().getNumberCols() ; j++) {
				if (verifyIfSeatAvailable(i, j)) {
					availableSeats++;
				}
			}
		}
		return availableSeats;
	}
	
	
	/**
	 * METODO occupa posto della sala in cui è fatta la proiezione
	 * @param row, col		Coordinate 
	 * @return esito 		Esito occupazione del posto
	 * @throws InvalidRoomSeatCoordinates 
	 */
	public boolean takeSeat(int row, int col) throws InvalidRoomSeatCoordinates {
			if (verifyIfSeatAvailable(row, col)) {
				seats.get(row).get(col).setAvailable(false);
				return true;
			}
			return false;
	}
	
	
	/**
	 * METODO per liberare il posto di una sala
	 * @param row, col		Coordinate 
	 * @return esito 		Esito rilascio del posto
	 * @throws InvalidRoomSeatCoordinates 
	 */
	public boolean freeSeat(int row, int col) throws InvalidRoomSeatCoordinates {
		if(!verifyIfSeatAvailable(row, col)) {
			seats.get(row).get(col).setAvailable(true);
			return true;
		}
		return false;
	}
	
	
	/**
	 * METODO per restituire un posto, date le coordinate
	 * @param row, col		Coordinate 
	 * @return 		 		Posto fisico
	 * @throws InvalidRoomSeatCoordinates 
	 */
	public PhysicalSeat getPhysicalSeat(int row, int col) throws InvalidRoomSeatCoordinates {
		 return this.getSeats().get(row).get(col).getPhysicalSeat();
	}
	
	
	/**
	 * METODO per farsi dare le coordinate di un posto
	 * @param s			Posto fisico
	 * @return			Coordinate
	 * @throws InvalidRoomSeatCoordinates 
	 */
	public String getSeatCoordinates(PhysicalSeat s) throws InvalidRoomSeatCoordinates {
		for(int i=0; i < room.getNumberRows(); i++) {
			for(int j=0; j < room.getNumberCols(); j++) {
				if(getPhysicalSeat(i,j) == s)
					return "Fila: " + Room.rowIndexToRowLetter(i) + "\t\t\tPosto: " + (j+1);		
			}
		}
		return null;		
	}
	
	@Override
	public int compareTo(Projection p) {
		return this.dateTime.compareTo(p.getDateTime());
	}
	
	/**
	 * METODO per stampare le caratteristiche principali della classe
	 * @return caratteristiche
	 */
	@Override 
	public String toString() {
		int availableSeats = 0;
		try { 
			availableSeats = this.getNumberAvailableSeat();
		}
		catch (InvalidRoomSeatCoordinates e) {
		}
		
		return "Sala n°: " + this.getRoom().getProgressive() + "\n"
				+ "Data: " + this.getDateTime().getDayOfWeek().toString().toLowerCase()
				+ " " + this.getDateTime().getDayOfMonth()
				+ " " + this.getDateTime().getMonth().toString().toLowerCase()
				+ " " + this.getDateTime().getYear() + "   "
				+ "Ora: " + String.format("%02d", this.getDateTime().getHour()) 
				+ ":" + String.format("%02d", this.getDateTime().getMinute()) + "\n"
				+ "Prezzo: " + this.getPrice().getAmount() + " "
				+ this.getPrice().getCurrency().toString() + "\n" 
				+ "Posti disponibili: " + availableSeats + "\n";
	}
}