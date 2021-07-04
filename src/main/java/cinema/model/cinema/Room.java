package cinema.model.cinema;

import java.util.ArrayList;

import cinema.model.cinema.util.RoomException;
import cinema.model.cinema.util.TypeOfSeat;

/**
 * Contiene le informazioni che riguardano la sala di un cinema.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class Room {

	/**
	 * Identificativo progressivo della sala.
	 */
	private final int number;

	/**
	 * Matrice di posti che compongono la sala.
	 */
	private ArrayList<ArrayList<PhysicalSeat>> seats;

	/**
	 * Costruttore della sala.
	 * 
	 * @param id   Identificativo progressivo della sala.
	 * @param rows numero di righe della sala.
	 * @param cols numero di colonne della sala
	 * @throws RoomException se ci sono delle eccezioni generate dalla classe Room.
	 */
	public Room(int id, int rows, int cols) throws RoomException {
		if (rows <= 0 || cols <= 0)
			throw new RoomException("La stanza deve contenere almeno un posto.");

		seats = new ArrayList<ArrayList<PhysicalSeat>>();
		for (int i = 0; i < rows; i++) {
			ArrayList<PhysicalSeat> row = new ArrayList<PhysicalSeat>();
			for (int j = 0; j < cols; j++) {
				row.add(new PhysicalSeat(TypeOfSeat.NORMAL));
			}
			seats.add(row);
		}
		this.number = id;
	}

	/**
	 * Restituisce il numero totale di posti della sala.
	 * 
	 * @return il numero totale di posti.
	 */
	public int getNumberSeats() {
		return seats.size() * seats.get(0).size();
	}

	public int getNumber() {
		return this.number;
	}

	/**
	 * Restituisce il numero di colonne della sala.
	 * 
	 * @return il numero di colonne.
	 */
	public int getNumberCols() {
		return seats.get(0).size();
	}

	/**
	 * Restituisce il numero di righe della sala.
	 * 
	 * @return il numero di righe.
	 */
	public int getNumberRows() {
		return seats.size();
	}

	/**
	 * Restituisce uno specifico posto della sala, date le sue coordinate
	 * (riga,colonna).
	 * 
	 * @param row riga del posto.
	 * @param col colonna del posto.
	 * @return un posto della sala.
	 */
	public PhysicalSeat getSeat(int row, int col) {
		return seats.get(row).get(col);
	}

	/**
	 * Converte il numero della riga in un carattere.
	 * 
	 * @param number numero da convertire.
	 * @return il carattere associato al numero inserito.
	 */
	public static String rowIndexToRowLetter(int number) {
		return ((number >= 0 && number <= 25) ? String.valueOf((char) (number + 65)) : null);
	}

	/**
	 * Converte un carattere (numero di una fila) in un valore numerico da 0 a 25.
	 * 
	 * @param fila lettera che si vuole convertire in numero.
	 * @return il numero intero corrispondente alla lettera dell'alfabeto.
	 */
	public static int rowLetterToRowIndex(String fila) {
		char letter = fila.toUpperCase().charAt(0);
		return (Character.getNumericValue(letter) - Character.getNumericValue('A'));
	}

	public long getProgressive() {
		return number;
	}

	@Override
	public String toString() {
		return "Sala n°: " + number + "  Posti: " + getNumberSeats();
	}
}