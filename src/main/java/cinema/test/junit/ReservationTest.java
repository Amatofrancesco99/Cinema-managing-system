package cinema.test.junit;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import cinema.model.Movie;
import cinema.model.spectator.Spectator;
import cinema.model.cinema.Room;
import cinema.model.projection.Projection;
import cinema.model.reservation.Reservation;

/** BREVE DESCRIZIONE CLASSE ReservationTest
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Le stesse osservazioni che abbiamo fatto sulla classe CinemaTest, all'interno del medesimo
 * package di questa classe, valgono ora (sia per quanto riguarda la descrizione, sia per quanto
 * riguarda la descrizione dei diversi metodi).
 */
public class ReservationTest {

	private static Reservation r;
	/** 
	 * METODO per poter effettuare l'impostazione del nostro sistema, creando gli input
	 * e gli output previsti.
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		r = new Reservation();
		r.setPurchaser(new Spectator("Francesco", "Amato" , "francesco.amato01@universitadipavia.it"));
		Room room = new Room(3,3);
		
		ArrayList<String> genres, directors, cast;
		genres = new ArrayList<>();
		directors = new ArrayList<>();
		cast = new ArrayList<>();
		
		genres.add("Azione");
		genres.add("Fantascienza");
		genres.add("Avventura");
		directors.add("Anthony Russo");
		directors.add("Joe Russo");
		cast.add("Robert Downey Jr.");
		cast.add("Chris Evans");
		cast.add("Mark Ruffalo");
		cast.add("Chris Hemsworth");
		cast.add("Scarlett Johansson");
		cast.add("Jeremy Renner");
		Movie AvengersEndgameMovie = new Movie(2, "Avengers - Endgame",
				"Tutto ovviamente parte dalle vicende di \"Avengers: Infinity War\". Thanos ha distrutto mezzo Universo grazie alle Gemme dell’Infinito (sei pietre e ognuna dona un particolare tipo di potere). La ricerca e la protezione di queste pietre sono state alle base degli altri film, ma ora Thanos le ha tutte ed è praticamente onnipotente. Le gemme dello Spazio, della Mente, del Potere, della Realtà, del Tempo e dell’Anima gli hanno permesso di raggiungere il suo scopo: distruggere l’universo. Tra i sopravvissuti al progetto diabolico del cattivo di turno, ci sono gli Avengers della Fase 1 (Capitan America, Thor, Vedova Nera, Occhio di Falco, Hulk e Iron Man) insieme ad Ant-Man e Captain Marvel. Lo scopo è quello ovviamente di sconfiggere Thanos e di far tornare in vita tutti quelli che non ci sono più come Spider-Man, Black Panther, Doctor Strange, Falcon, Scarlet Witch, Star-Lord, Drax, Groot, Mantis, Bucky Barnes, Nick Fury, Maria Hill, Loki, Visione e Gamora. Un mix di azione, comicità e riflessioni sul genere umano perché il film mostra il lato fragile e vulnerabile presente sia nei buoni sia nei cattivi.  Oltre ad un Robert Downey Jr in stato di grazia, spicca l’interpretazione di Chris Hemsworth che nonostante il cambiamento totale di look riesce ad essere credibile nei momenti comici e in quelli drammatici. ",
				genres, directors, cast, 5, 182,
				"https://images-na.ssl-images-amazon.com/images/I/71HyTegC0SL._AC_SY879_.jpg",
				"https://www.youtube.com/watch?v=vqWz0ZCpYBs");
		r.setProjection(new Projection(109, AvengersEndgameMovie, LocalDateTime.parse("2021-06-02T22:30:00"), 12.5,	room));
		
		r.addSeat(0, 0);
		r.addSeat(0, 1);
		r.addSeat(0, 2);
	}

	
	/**
	 * METODO per effettuare il test sulla classe, usando gli oggetti istanziati e usando
	 * questi ultimi all'interno di metodi specifici della classe presa in considerazione.
	 * Inoltre viene effettuata l'asserzione, ossia viene confrontato il risultato previsto
	 * con quello ottenuto dalla chiamata del metodo specifico.
	 * Se l'asserzione è vera il test ha avuto successo, viceversa ha fallito.
	 */
	@Test
	public void test() {
		// Test invio email
		assertEquals(true, r.sendEmail());
	}

}