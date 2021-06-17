package cinema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** BREVE DESCRIZIONE CLASSE Spectator
 * 
 * @author Screaming Hairy Armadillo Team
 *	
 * Questa classe rappresenta lo spettatore che effettua la prenotazione della visione del film
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Spectator {

	/** ATTRIBUTI
	 * @param name			Nome/Nomi
	 * @param surname		Cognome/Cognomi
	 * @param email			Email
	 */
	private String name, surname, email;

}