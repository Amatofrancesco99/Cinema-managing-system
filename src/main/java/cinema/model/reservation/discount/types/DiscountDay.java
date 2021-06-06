package cinema.model.reservation.discount.types;

import java.time.LocalDate;

import cinema.model.money.Money;
import cinema.model.reservation.Reservation;
import cinema.model.reservation.discount.ReservationDiscountStrategy;

/** BREVE DESCRIZIONE CLASSE DiscontDay	 (Pattern Strategy)
 * 
 * @author Screaming Hairy Armadillo Team
 *	
 *  Questa classe rappresenta una strategia di sconto sulla prenotazione basata sul giorno
 *  in cui gli spettatori che visioneranno il film
 */
public class DiscountDay implements ReservationDiscountStrategy{
	
	/** ATTRIBUTI 
	 *  @param start		giorno da cui parte lo sconto
	 *  @param end			giorno in cui finisce lo sconto
	 *  @param day			giorno in cui vale lo sconto
	 *  @param PERCENTAGE	percentuale di sconto
	 */
	private LocalDate start, end, day;
	private final float PERCENTAGE = (float) 0.90;
	
	/**
	 * METODO utilizzato per poter effettuare lo sconto sulla prenotazione e farsi
	 * restituire il nuovo totale, dato lo sconto
	 */
	@Override
	public Money getTotal(Reservation r) {
		float totalPrice = 0;
		if(((r.getPurchaseDate().compareTo(start)>=0) && (r.getPurchaseDate().compareTo(end)<=0)) 
		|| (r.getPurchaseDate().compareTo(day)==0)) {
			totalPrice+=r.getProjection().getPrice().getAmount()*PERCENTAGE;
		}
		else totalPrice+=r.getProjection().getPrice().getAmount();
		return new Money(totalPrice,r.getProjection().getPrice().getCurrency());
	}
	
	/** 
	 * METODO per impostare il periodo di validità dello sconto
	 * @param start	 Giorno a partire dal quale lo sconto inizia
	 * @param end	 Giorno a partire dal quale lo sconto termina
	 */
	public void setDateDiscount(LocalDate start, LocalDate end) {
		this.start = start;
		this.end = end;
	}
	
	/**
	 * METODO per impostare lo sconto in un determinato giorno
	 * @param day	Giorno in cui si vuole valga lo sconto
	 */
	public void setDayDiscount(LocalDate day) {
		this.day = day;
	}
	
}