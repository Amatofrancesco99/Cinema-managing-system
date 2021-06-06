package cinema.model;

import java.time.LocalDate;
import java.time.Period;

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
	 * @param birthDate		Data di nascita
	 */
	private String name, surname, email;
	private LocalDate birthDate;

	/**
	 * METODO per farsi restituire l'età dello spettatore
	 * @return age	Età 
	 */
	public int getAge() {
		return Period.between(birthDate, java.time.LocalDate.now()).getYears();
	}
}