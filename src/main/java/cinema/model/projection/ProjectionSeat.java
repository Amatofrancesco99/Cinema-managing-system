package cinema.model.projection;

import cinema.model.cinema.PhysicalSeat;

/**	BREVE DESCRIZIONE CLASSE ProjectionSeat
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Questa classe rappresenta i posti della sala, ma di una proiezione specifica.
 * Sostanzialmente sono posti fisici (presi dalla sala in cui è proiettato il film), ma
 * hanno come attributo in più il fatto che siano già stati occupati, o che siano liberi.
 */
public class ProjectionSeat {
	
	/** ATTRIBUTI
	 * @param physicalSeat  Posto fisico della sala
	 * @param available		Disponibilità del posto ( 0 = occupato, 1 = libero )
	 */
	private PhysicalSeat physicalSeat;
	private boolean available;
	
	
	/**
	 * COSTRUTTORE DELLA CLASSE
	 * @param seat
	 * @param b
	 */
	public ProjectionSeat(PhysicalSeat seat, boolean b) {
		this.physicalSeat = seat;
		this.available = b;
	}


	/** METODO per farsi dire il posto fisico*/
	public PhysicalSeat getPhysicalSeat() {
		return physicalSeat;
	}
	
	
	/** METODO per farsi dire se il posto è libero o meno*/
	public boolean isAvailable() {
		return available;
	}
	
	
	/* METODO per farsi impostare lo stato di un posto*/
	public void setAvailable(boolean status) {
		this.available = status;
	}
	
}