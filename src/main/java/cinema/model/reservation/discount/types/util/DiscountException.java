package cinema.model.reservation.discount.types.util;

/**
 * Eccezione lanciata qualora via sia un problema nell'utilizzo degli
 * sconti.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class DiscountException extends Exception {
	public DiscountException(String message) {
		super(message);
	}
}
