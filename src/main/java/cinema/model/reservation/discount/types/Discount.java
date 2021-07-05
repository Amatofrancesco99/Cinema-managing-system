package cinema.model.reservation.discount.types;

import cinema.model.reservation.discount.IReservationDiscountStrategy;
import cinema.model.reservation.discount.types.util.TypeOfDiscount;

/**
 * Strategia generica di sconto da applicare al costo totale delle prenotazioni.
 *
 * Questa classe rappresenta una strategia generica dotata di id univoco e di un
 * determinato tipo, senza specificare la reale implementazione della strategia.
 *
 * @author Screaming Hairy Armadillo Team
 * 
 */
public abstract class Discount implements IReservationDiscountStrategy {

	/**
	 * Tipo della strategia di sconto.
	 */
	private TypeOfDiscount type;

	/**
	 * Id della strategia di sconto.
	 */
	private int id;

	/**
	 * Costruttore della strategia di sconto.
	 * 
	 * @param type tipo della strategia di sconto.
	 * @param id   id della strategia di sconto.
	 */
	public Discount(TypeOfDiscount type, int id) {
		this.type = type;
		this.id = id;
	}

	@Override
	public TypeOfDiscount getTypeOfDiscount() {
		return type;
	}

	@Override
	public int getDiscountId() {
		return id;
	}

}
