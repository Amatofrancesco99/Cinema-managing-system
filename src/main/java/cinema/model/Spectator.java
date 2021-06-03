package cinema.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Spectator {

	private String name, surname, email;
	private LocalDate birthDate;

	public int getAge() {
		return Period.between(birthDate, java.time.LocalDate.now()).getYears();
	}
}