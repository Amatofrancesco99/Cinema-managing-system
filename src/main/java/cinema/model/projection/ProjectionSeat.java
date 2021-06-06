package cinema.model.projection;

import cinema.model.cinema.PhysicalSeat;
import lombok.*;

/**	BREVE DESCRIZIONE CLASSE ProjectionSeat
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Questa classe rappresenta i posti della sala, ma di una proiezione specifica.
 * Sostanzialmente sono posti fisici (presi dalla sala in cui è proiettato il film), ma
 * hanno come attributo in più il fatto che siano già stati occupati, o che siano liberi.
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProjectionSeat {
	
	/** ATTRIBUTI
	 * @param physicalSeat  Posto fisico della sala
	 * @param available		Disponibilità del posto ( 0=occupato, 1=libero )
	 */
	private PhysicalSeat physicalSeat;
	private boolean available;
	
}