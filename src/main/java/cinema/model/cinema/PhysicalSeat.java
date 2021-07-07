package cinema.model.cinema;

import cinema.model.cinema.util.TypeOfSeat;

/**
 * Posto fisico presente all'interno delle sale del cinema.
 *
 * <p>
 * Questa classe rappresenta un singolo posto fisico presente in una determinata
 * sala del cinema, differente da uno o più "posti di proiezione" ad esso
 * associati che tengono invece conto dello stato del posto fisico per una
 * specifica proiezione di uno specifico film.
 *
 * @author Screaming Hairy Armadillo Team
 *
 */
public class PhysicalSeat {

	/**
	 * Tipo del posto. Al momento sono implementate solamente le funzionalità per il
	 * tipo {@code TypeOfSeat.NORMAL}.
	 */
	@SuppressWarnings("unused")
	private TypeOfSeat type;

	/**
	 * Costruttore del posto fisico.
	 *
	 * <p>
	 * Al momento sono implementate solamente le funzionalità per il tipo
	 * {@code TypeOfSeat.NORMAL}.
	 *
	 * @param type tipo del posto fisico.
	 */
	public PhysicalSeat(TypeOfSeat type) {
		this.type = type;
	}

}
