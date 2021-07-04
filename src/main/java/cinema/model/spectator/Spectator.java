package cinema.model.spectator;

import cinema.model.spectator.util.InvalidSpectatorInfoException;

/**
 * Rappresenta lo spettatore che effettua la prenotazione della proiezione del
 * film.
 * 
 * @author Screaming Hairy Armadillo Team
 * 
 */
public class Spectator {

	/**
	 * Nome dello spettatore.
	 */
	private String name;

	/**
	 * Cognome dello spettatore.
	 */
	private String surname;

	/**
	 * E-mail dello spettatore.
	 */
	private String email;

	/**
	 * Costruttore dello spettatore.
	 * 
	 * @param name    nome dello spettatore.
	 * @param surname cognome dello spettatore.
	 * @param email   e-mail dello spettatore.
	 * @throws InvalidSpectatorInfoException se almeno uno dei parametri è una
	 *                                       stringa vuota.
	 */
	public Spectator(String name, String surname, String email) throws InvalidSpectatorInfoException {
		if ((email.equals("")) || (name.equals("")) || (surname.equals(""))) {
			throw new InvalidSpectatorInfoException("I dati personali richiesti sono mancanti.");
		} else {
			this.name = name;
			this.surname = surname;
			this.email = email;
		}
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getEmail() {
		return email;
	}

}
