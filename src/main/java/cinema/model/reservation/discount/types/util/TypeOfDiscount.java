package cinema.model.reservation.discount.types.util;

/**
 * Tipologia di un determinato sconto.
 *
 * @author Screaming Hairy Armadillo Team
 * 
 */
public enum TypeOfDiscount {
	/**
	 * Sconto basato sull'età degli spettatori che guardano il film.
	 */
	AGE,

	/**
	 * Sconto basato sul numero di posti riservati all'interno di una determinata
	 * proiezione.
	 */
	NUMBER,

	/**
	 * Sconto basato sulla data della proiezione associata alla prenotazione.
	 */
	DAY;

}
