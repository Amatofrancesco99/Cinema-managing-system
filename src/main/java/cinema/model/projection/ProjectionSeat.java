package cinema.model.projection;

import cinema.model.cinema.PhysicalSeat;
import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProjectionSeat {
	
	private PhysicalSeat physicalSeat;
	private boolean available;
	
}
