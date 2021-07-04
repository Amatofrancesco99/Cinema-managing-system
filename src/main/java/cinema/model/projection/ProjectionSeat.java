package cinema.model.projection;

import cinema.model.cinema.PhysicalSeat;

/**
 * Posto di proiezione associato ad uno specifico posto fisico di una sala.
 *
 * A differenza di un posto fisico, un posto di proiezione tiene conto dello
 * stato del posto fisico per una determinata proiezione (in particolare modella
 * la disponibilità del posto fisico nel contesto della proiezione).
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class ProjectionSeat {

	/**
	 * Posto fisico associato al posto di proiezione.
	 */
	private PhysicalSeat physicalSeat;

	/**
	 * Stato del posto di proiezione (true = disponibile, false = non disponibile).
	 */
	private boolean available;

	/**
	 * Costruttore del posto di proiezione.
	 * 
	 * @param seat      posto fisico da associare al posto di proiezione.
	 * @param available disponibilità del posto (true = disponibile, false = non
	 *                  disponibile).
	 */
	public ProjectionSeat(PhysicalSeat seat, boolean available) {
		this.physicalSeat = seat;
		this.available = available;
	}

	public PhysicalSeat getPhysicalSeat() {
		return physicalSeat;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

}
