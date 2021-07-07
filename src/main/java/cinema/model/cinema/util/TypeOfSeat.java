package cinema.model.cinema.util;

/**
 * Tipologie di posti di cui il cinema dispone posizionati nelle varie sale.
 *
 * <p>
 * Al momento la logica di business contempla solamente posti di tipo
 * {@code NORMAL} ma sono sempre possibili future implementazioni che
 * implementano le funzionalità rispettive anche per il tipo di posto
 * {@code PREMIUM}.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public enum TypeOfSeat {

	/**
	 * Posto normale di tipo classico.
	 */
	NORMAL,

	/**
	 * Posto privilegiato con un livello di comfort aggiuntivo.
	 */
	PREMIUM;

}
