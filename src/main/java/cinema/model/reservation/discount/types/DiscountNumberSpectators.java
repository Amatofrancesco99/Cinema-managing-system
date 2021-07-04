package cinema.model.reservation.discount.types;

import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.types.util.DiscountException;
import cinema.model.reservation.discount.types.util.TypeOfDiscounts;


/** BREVE DESCRIZIONE CLASSE DiscountNumberSpectator  (Pattern Strategy)
 * 
 * @author Screaming Hairy Armadillo Team
 *
 *	Questa classe rappresenta una strategia di sconto sulla prenotazione basata sul uno
 *  sconto comitiva, ovvero a seconda di quante persone fanno parte di quella 
 *  specifica prenotazione (si può anche vedere come numero di posti che sono stati occupati).
 */
public class DiscountNumberSpectators extends Discount{

	/** ATTRIBUTI
	 * @param NUMBER_PEOPLE 	Numero di persone minimo, al di sopra del quale lo sconto
	 * 							comitiva sarà valido
	 * @param PERCENTAGE  		Percentuale di sconto effettuata
	 */
	private int numberPeople;
	private double percentage;
	
	/**
	 * COSTRUTTORE
	 * @param numberPeople
	 * @param percentage
	 */
	public DiscountNumberSpectators(int numberPeople, double percentage, int id) {
		super(TypeOfDiscounts.NUMBER, id);
		this.numberPeople = numberPeople;
		this.percentage = percentage;
		
	}
	
	
	/**
	 * METODO utilizzato per poter effettuare lo sconto sulla prenotazione e farsi
	 * restituire il nuovo totale, dato lo sconto
	 */
	@Override
	public double getTotal(Reservation r) {
		double totalPrice = 0;
		if(r.getNSeats() >= getNumberPeople()){
			totalPrice+=r.getProjection().getPrice()*(1 - percentage)*r.getNSeats();
		}
		else 
			totalPrice+=r.getProjection().getPrice()*r.getNSeats();
		return totalPrice;
	}
	

	/**
	 * METODO per settare il numero di persone da cui parte lo sconto comitiva
	 * @param n			Numero di persone minimo
	 * @throws InvalidNumberPeopleValueException 
	 */
	public void setNumberPeople(int n) throws DiscountException {
		if (n > 0) {
			this.numberPeople = n;
		}
		else throw new DiscountException("Il numero di persone sopra il quale applicare lo sconto deve essere maggiore di zero.");
	}
	
	
	/**METODO per farsi dire il numero di persone da cui parte lo sconto */
	public int getNumberPeople() {
		return numberPeople;
	}


	/**METODO per farsi restituire le caratteristiche dello sconto comitiva*/
	@Override
	public String toString() {
		return  "[ " + this.getTypeOfDiscount() + " ]" + "\n" +
				"Numero di persone al di sopra del quale parte lo sconto è valido: " + numberPeople 
				+ "\n" + "Percentuale di sconto applicata: " + percentage;
	}
}