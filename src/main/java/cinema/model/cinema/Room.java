package cinema.model.cinema;

import java.util.ArrayList; 
import java.util.concurrent.atomic.AtomicInteger;

import cinema.model.cinema.util.RoomException;
import cinema.model.cinema.util.TypeOfSeat;

/** BREVE DESCRIZIONE CLASSE Room
 * 
 * @author Screaming Hairy Armadillo Team
 *	
 * Questa classe rappresenta la sala del cinema
 */
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
	public Room(int rows, int cols) throws RoomException{
		if(rows <= 0 || cols <= 0)
			throw new RoomException("La stanza deve contenere almeno un posto.");

		seats = new ArrayList<ArrayList<PhysicalSeat>>();
		for(int i = 0; i < rows; i++) {
			ArrayList<PhysicalSeat> row = new ArrayList<PhysicalSeat>();
			for(int j = 0; j < cols; j++) {
				row.add(new PhysicalSeat(TypeOfSeat.NORMAL));
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
	 * @param int     Numero
	 * @return char   Carattere associato a quel numero
	 */
	public static String rowIndexToRowLetter(int i) {
		return ( (i >= 0 && i <= 25) ? String.valueOf((char)(i + 65)) : null);
	}
	
	/**
	 * METODO per convertire un carattere (numero di una fila), in valore numerico da 0 a 25
	 * si considera il fatto che la fila del cinema è un carattere, quindi una string con un solo elemento
	 * @param s  	Lettera che si vuole convertire in numero
	 * @return int 	Numero intero della lettera dell'alfabeto
	 */
	public static int rowLetterToRowIndex(String s) {
		char letter = s.toUpperCase().charAt(0);
		return ( Character.getNumericValue(letter) - Character.getNumericValue('A') );
	}

	
	/** METODO per farsi dare il progressivo della sala
	 * 
	 * @return
	 */
	public long getProgressive() {
		return progressive;
	}
	
	
	/** METODO per stampare le caratteristiche della sala*/
	@Override
	public String toString() {
		return  "Sala n°: " + progressive + 
				"  Posti: " + getNumberSeats();
	}
}