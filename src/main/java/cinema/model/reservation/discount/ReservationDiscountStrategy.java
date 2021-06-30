package cinema.model.reservation.discount;

import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.types.util.TypeOfDiscounts;


/** BREVE DESCRIZIONE CLASSE ReservationDiscountStrategy (Pattern Strategy + Pattern Composite)
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Questa classe, più specificatamente interfaccia, specifica il metodo che deve essere 
 * implementato da tutte le strategie che applicano sconti sulla prenotazione specifica.
 * Esistono diversi tipi di sconti che si possono applicare, a seconda delle
 * politiche del cinema e del direttore di quest ultimo. Chiaramente se ne possono aggiungere
 * moltissimi altri a piacere. Trovate alcune tra le strategie applicabili sulle prenotazioni
 * nel sottopackage types, di questo stesso package. 
 */
public interface ReservationDiscountStrategy {
	
	
	/**
	 * METODO per farsi restituire il totale di una prenotazione, dopo che è stato verificato
	 * se si può calcolare dello sconto su di essa.
	 * @param r	  	  Prenotazione a cui si vuole verificare se la strategia di sconto è 
	 * 				  applicabile
	 * @return Money  Ritorno del prezzo totale scontato
	 */
	public double getTotal(Reservation r);
	
	public TypeOfDiscounts getTypeOfDiscount();

}