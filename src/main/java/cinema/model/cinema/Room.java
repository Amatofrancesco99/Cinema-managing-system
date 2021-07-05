package cinema.model.cinema;

import java.util.ArrayList;

import cinema.model.cinema.util.RoomException;
import cinema.model.cinema.util.TypeOfSeat;

/**
 * Una specifica sala del cinema.
 *
 * @author Screaming Hairy Armadillo Team
 *
 */
public class Room {

	/**
	 * Identificativo univoco della sala.
	 */
	private final int number;

	/**
	 * Matrice di posti contenuti nella sala.
	 */
	private ArrayList<ArrayList<PhysicalSeat>> seats;

	/**
	 * Costruttore della sala.
	 *
	 * {@code rows} e {@code cols} partono da zero (il primo posto vicino allo
	 * schermo a sinistra dall'alto ha coordinate (0, 0)).
	 * 
	 * @param number identificativo univoco della sala.
	 * @param rows   numero di file della sala.
	 * @param cols   numero di posti per fila della sala.
	 * @throws RoomException se le dimensioni della sala (file e posti per fila) non
	 *                       sono valide.
	 */
	public Room(int number, int rows, int cols) throws RoomException {
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
		this.number = number;
	}

	/**
	 * Restituisce il numero totale di posti presenti nella sala.
	 * 
	 * @return il numero totale di posti.
	 */
	public int getNumberOfSeats() {
		return seats.size() * seats.get(0).size();
	}

	/**
	 * Restituisce il numero di posti per fila della sala.
	 * 
	 * @return il numero di posti per fila.
	 */
	public int getNumberOfCols() {
		return seats.get(0).size();
	}

	/**
	 * Restituisce il numero di file della sala.
	 * 
	 * @return il numero di righe.
	 */
	public int getNumberOfRows() {
		return seats.size();
	}

	/**
	 * Restituisce uno specifico posto della sala date le sue coordinate (fila,
	 * posto).
	 * 
	 * @param row fila del posto.
	 * @param col numero del posto all'interno della fila.
	 * @return il posto della sala richiesto.
	 */
	public PhysicalSeat getSeat(int row, int col) {
		return seats.get(row).get(col);
	}

	/**
	 * Converte il numero della fila {@code number} nella lettera corrispondente.
	 * 
	 * @param number numero della fila.
	 * @return la lettera associata alla fila o null se il numero della fila non è
	 *         valido.
	 */
	public static String rowIndexToRowLetter(int number) {
		return ((number >= 0 && number <= 25) ? String.valueOf((char) (number + 65)) : null);
	}

	/**
	 * Converte una lettera (identificante una fila) nel corrispettivo indice
	 * numerico all'interno della sala.
	 * 
	 * @param fila lettera identificante la fila.
	 * @return l'indice numerico corrispondente alla lettera.
	 */
	public static int rowLetterToRowIndex(String fila) {
		char letter = fila.toUpperCase().charAt(0);
		return Character.getNumericValue(letter) - Character.getNumericValue('A');
	}

	public int getNumber() {
		return number;
	}

	@Override
	public String toString() {
		return String.format("Sala %d: %d posti", number, getNumberOfSeats());
	}

}
