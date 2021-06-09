package cinema.model.cinema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
	 * @param int     Numero
	 * @return char   Carattere associato a quel numero
	 */
	public static String rowIndexToRowLetter(int i) {
		char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	    if ((i > 25)||(i < 0)) {
	        return null;
	    }
	    return Character.toString(alphabet[i]);
	}
	
	/**
	 * METODO per convertire un carattere (numero di una fila), in valore numerico da 0 a 25
	 * @param s  	Lettera che si vuole convertire in numero
	 * @return int 	Numero intero della lettera dell'alfabeto
	 */
	public static int rowLetterToRowIndex(String s) {
		int number = -1;
		HashMap<String, Integer> letterToNumber = new HashMap<>();
		letterToNumber.put("aA", 0);
		letterToNumber.put("bB", 1);
		letterToNumber.put("cC", 2);
		letterToNumber.put("dD", 3);
		letterToNumber.put("eE", 4);
		letterToNumber.put("fF", 5);
		letterToNumber.put("gG", 6);
		letterToNumber.put("hH", 7);
		letterToNumber.put("iI", 8);
		letterToNumber.put("jJ", 9);
		letterToNumber.put("kK", 10);
		letterToNumber.put("lL", 11);
		letterToNumber.put("mM", 12);
		letterToNumber.put("nN", 13);
		letterToNumber.put("oO", 14);
		letterToNumber.put("pP", 15);
		letterToNumber.put("qQ", 16);
		letterToNumber.put("rR", 17);
		letterToNumber.put("sS", 18);
		letterToNumber.put("tT", 19);
		letterToNumber.put("uU", 20);
		letterToNumber.put("vV", 21);
		letterToNumber.put("wW", 22);
		letterToNumber.put("xX", 23);
		letterToNumber.put("yY", 24);
		letterToNumber.put("zZ", 25);
		
		for(Map.Entry<String,Integer> entry : letterToNumber.entrySet()) {
		    if(entry.getKey().contains(s)){
	            number = entry.getValue();
	        }
		}
		return number;
	}
}