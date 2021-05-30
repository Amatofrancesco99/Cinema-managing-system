package cinema.model.reservation;

import java.util.ArrayList;
import java.util.Date;

import cinema.model.Projection;
import cinema.model.Spectator;
import cinema.model.Ticket;
import cinema.model.payment.PaymentCard;

public class Reservation {

	private int progressive;
	private Date purchaseDate;
	private ArrayList<Spectator> spectators;
	private ArrayList<Ticket> tickets;
	private Projection projection;
	private PaymentCard paymentCard;
	
	public Reservation (int id, Date purchaseDate) {
		progressive=id;
		this.purchaseDate=purchaseDate;
		spectators=new ArrayList<Spectator>();
		tickets=new ArrayList<Ticket>();
	}
	
	// aggiungi e rimuovi spettatori dalla Reservation
	public void addSpectator(Spectator s) {
		spectators.add(s);
	}
	public void removeSpectator(Spectator s) {
		spectators.remove(s);
	}
	
	// aggiungi e rimuovi tickets dalla Reservation
	public void addTicket(Ticket t) {
		tickets.add(t);
	}
	public void removeTicket(Ticket t) {
		tickets.remove(t);
	}
	
	// imposta o cambia il metodo di pagamento
	public void setPaymentCard(PaymentCard p) {
		paymentCard=p;
	}
	
	// imposta la proiezione di un film 
	public void setProjection(Projection p) {
		projection=p;
	}
	
	// generazione di un documento con tutte le informazioni della reservation
	public boolean createReport() {
		return true;
	}
	
	// invio per email del documento con le informazioni inerenti la reservation
	public String sendEmail(String email) {
		if (createReport()!=false) {
			return "Email spedita";
		}
		else return "L'invio dell'email non è andato a buon fine";
	}
}