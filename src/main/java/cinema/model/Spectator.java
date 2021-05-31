package cinema.model;

import java.time.LocalDate;
import java.time.Period;

public class Spectator {

	private String name, surname, email;
	private LocalDate birthDate;
	
	public Spectator(String name,String surname, String email, LocalDate birthDate) {
		this.name=name;
		this.surname=surname;
		this.email=email;
		this.birthDate=birthDate;
	}
	
	public String getEmail() {
		return email;
	}
	
	public int getAge() {
		LocalDate now = java.time.LocalDate.now();
		return Period.between(birthDate, now).getYears();
	}
}