package cinema.model.cinema;

import cinema.model.enumerations.TypeOfSeat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PhysicalSeat {
	
	private char row;
	private int number;
	private TypeOfSeat type;
}
