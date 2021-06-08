package cinema.model.cinema;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import cinema.model.cinema.util.InvalidRoomDimensionsException;
import lombok.Data;

/** BREVE DESCRIZIONE CLASSE Room
 * 
 * @author Screaming Hairy Armadillo Team
 *	
 * Questa classe rappresenta la sala del cinema
 */
@Data
public class Room {
	
	/** ATTRIBUTI
	 * @param progressive	Id progressivo della sala
	 * @param seats			Posti che compongono la sala
	 */
	private static final AtomicInteger count = new AtomicInteger(0); 
	private final long progressive;
	private ArrayList<ArrayList<PhysicalSeat>> seats;
	
	/** COTRUTTORE, inseriti il numero di righe e di colonne di una sala, esso 
	 * genera tutti i posti
	 * 
	 * @param rows	Numero di righe della sala
	 * @param cols	Numero di colonne della sala
	 * @throws InvalidRoomDimensionsException	Eccezione lanciata qualora non siano
	 * 											inseriti un numero di righe o colonne valido
	 */
	public Room(int rows, int cols) throws InvalidRoomDimensionsException{

		if(rows <= 0 || cols <= 0)
			throw new InvalidRoomDimensionsException();

		seats = new ArrayList<ArrayList<PhysicalSeat>>();
		for(int i = 0; i < rows; i++) {
			ArrayList<PhysicalSeat> row = new ArrayList<PhysicalSeat>();
			for(int j = 0; j < cols; j++) {
				row.add(new PhysicalSeat());
			}
			seats.add(row);
		}
		this.progressive = count.incrementAndGet();
	}
	
	/**
	 * METODO per farsi dire il numero di posti che compongono la sala
	 * @return n	Numero di posti
	 */
	public int getNumberSeats() {
		return seats.size()*seats.get(0).size();
	}
	
	/**
	 * METODO per farsi dire il numero di colonne che compongono la sala
	 * @return n	Numero di colonne
	 */
	public int getNumberCols() {
		return seats.get(0).size();
	}
	
	/**
	 * METODO per farsi dire il numero di righe che compongono la sala
	 * @return n	Numero di righe
	 */
	public int getNumberRows() {
		return seats.size();
	}	
	
	/**
	 * METODO per farsi dare il posto di una sala
	 * @param row	Riga
	 * @param col	Colonna
	 * @return n    Posto fisico
	 */
	public PhysicalSeat getSeat(int row, int col) {
		return seats.get(row).get(col);
	}
	
	/**
	 * METODO per convertire il numero della riga in un carattere
	 * 
	 */
	public static String rowIndexToRowLetter(int i) {
		char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	    if ((i > 25)||(i < 0)) {
	        return null;
	    }
	    return Character.toString(alphabet[i]);
	}
	
}