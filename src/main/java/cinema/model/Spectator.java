package cinema.model;

import java.util.Date;

public class Spectator {

	private String name, surname, email;
	private Date birthDate;
	
	public Spectator(String name,String surname, String email, Date birthDate) {
		this.name=name;
		this.surname=surname;
		this.email=email;
		this.birthDate=birthDate;
	}
	
	public String getEmail() {
		return email;
	}
	
}