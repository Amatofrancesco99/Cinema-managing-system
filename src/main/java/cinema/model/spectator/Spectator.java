package cinema.model.spectator;

import cinema.model.spectator.util.InvalidSpectatorInfoException;

/** BREVE DESCRIZIONE CLASSE Spectator
 * 
 * @author Screaming Hairy Armadillo Team
 *	
 * Questa classe rappresenta lo spettatore che effettua la prenotazione della visione del film
 */
public class Spectator {

	/** ATTRIBUTI
	 * @param name			Nome/Nomi
	 * @param surname		Cognome/Cognomi
	 * @param email			Email
	 */
	private String name, surname, email;

	/**
	 * COSTRUTTORE della classe
	 * @param name
	 * @param surname
	 * @param email
	 * @throws InvalidSpectatorInfoException
	 */
	public Spectator(String name, String surname, String email) throws InvalidSpectatorInfoException {
		if ((email.equals("")) || (name.equals("")) || (surname.equals(""))) {
			throw new InvalidSpectatorInfoException();
		}
		else {
			this.name = name;;
			this.surname = surname;
			this.email = email;
		}
	}

	/** METODO per farsi dire il nome dello spettatore */
	public String getName() {
		return name;
	}
	
	/** METODO per farsi dire il cognome dello spettatore */
	public String getSurname() {
		return surname;
	}
	
	/** METODO per farsi dire l'email dello spettatore */
	public String getEmail() {
		return email;
	}
}