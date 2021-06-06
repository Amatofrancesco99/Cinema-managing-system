package cinema.test.junit;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import cinema.model.Movie;
import cinema.model.cinema.Room;
import cinema.model.cinema.util.InvalidRoomDimensionsException;
import cinema.model.money.Money;
import cinema.model.projection.Projection;

/** BREVE DESCRIZIONE CLASSE CinemaTest
 * 
 * @author Screaming Hairy Armadillo Team
 *
 * Questa classe nasce con lo scopo specifico di effettuare il test di unità (tramite JUnit),
 * ossia test effettuati su una classe specifica e dei suoi metodi, provando differenti
 * input e verificando gli output in maniera automatica (confrontando i risultati attesi
 * con quelli ottenuti), sulla classe Cinema, presente all'interno del model package.
 * Anche se l'implementazione reale prevede l'utilizzo di dati presenti su un DB, per
 * sfruttare il principio di persistenza dei dati (ad esempio dei Film, delle Sale, delle
 * Proiezioni, ecc...) sono utilizzati oggetti istanza "finti" (mock), ossia oggetti
 * che hanno la stessa interfaccia di oggetti esterni realmente utilizzati e che simulano le
 * loro funzionalità. 
 * Questo per evitare di dover effettuare attività preliminari di inserimento dati 
 * all'interno del DB, il che potrebbe comportare eventuali problematiche (perdita dei dati), 
 * un evento non molto gradito e una perdita di tempo, visto che i dati inseriti non sarebbero
 * quelli veri, ma quelli ottenuti di fronte ad errori di inserimento da parte di utenti
 * non particolarmente attenti/istruiti.
 * Chiaramente potrebbe essere utile fare sessioni di "istruzioni" a chi vendiamo il 
 * software, per poter fare in modo tale che inseriscano valori corretti all'interno del DB.
 * 
 */
public class CinemaTest {

	/** 
	 * METODO per poter effettuare l'impostazione del nostro sistema, creando gli input
	 * e gli output previsti.
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
				// ********* TEMPORARY DATA USED FOR TESTING *********
				
				// Test movie
				ArrayList<String> genres, directors, cast;
				genres = new ArrayList<>();
				directors = new ArrayList<>();
				cast = new ArrayList<>();
				
				// DRUK 
				genres.add("Drammatico");
				genres.add("Commedia");
				directors.add("Thomas Vinterberg");
				cast.add("Mads Mikkelsen");
				cast.add("Thomas Bo Larsen");
				cast.add("Lars Ranthe");
				cast.add("Magnus Millang");
				Movie DrukMovie = new Movie(1, "Druk - Un altro giro",
						"C'è una teoria secondo la quale tutti noi siamo nati con una piccola quantità di alcool già presente nel sangue e che, pertanto, una piccola ebbrezza possa aprire le nostre menti al mondo che ci circonda, diminuendo la nostra percezione dei problemi e aumentando la nostra creatività. Rincuorati da questa teoria, Martin e tre suoi amici, tutti annoiati insegnanti delle superiori, intraprendono un esperimento per mantenere un livello costante di ubriachezza durante tutta la giornata lavorativa. Se Churchill vinse la seconda guerra mondiale in preda a un pesante stordimento da alcool, chissà cosa potrebbero fare pochi bicchieri per loro e per i loro studenti?",
						genres, directors, cast, 4, 117,
						"https://200mghercianos.files.wordpress.com/2020/12/another-round-druk-thomas-vinteberg-filme-critica-mostra-sp-poster-1.jpg",
						"https://www.youtube.com/watch?v=hFbDh58QHzw");
				
				genres.clear();
				directors.clear();
				cast.clear();
				
				// AVENGERS - ENDGAME
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
						"Tutto ovviamente parte dalle vicende di “Avengers: Infinity War”. Thanos ha distrutto mezzo Universo grazie alle Gemme dell’Infinito (sei pietre e ognuna dona un particolare tipo di potere). La ricerca e la protezione di queste pietre sono state alle base degli altri film, ma ora Thanos le ha tutte ed è praticamente onnipotente. Le gemme dello Spazio, della Mente, del Potere, della Realtà, del Tempo e dell’Anima gli hanno permesso di raggiungere il suo scopo: distruggere l’universo. Tra i sopravvissuti al progetto diabolico del cattivo di turno, ci sono gli Avengers della Fase 1 (Capitan America, Thor, Vedova Nera, Occhio di Falco, Hulk e Iron Man) insieme ad Ant-Man e Captain Marvel. Lo scopo è quello ovviamente di sconfiggere Thanos e di far tornare in vita tutti quelli che non ci sono più come Spider-Man, Black Panther, Doctor Strange, Falcon, Scarlet Witch, Star-Lord, Drax, Groot, Mantis, Bucky Barnes, Nick Fury, Maria Hill, Loki, Visione e Gamora. Un mix di azione, comicità e riflessioni sul genere umano perché il film mostra il lato fragile e vulnerabile presente sia nei buoni sia nei cattivi.  Oltre ad un Robert Downey Jr in stato di grazia, spicca l’interpretazione di Chris Hemsworth che nonostante il cambiamento totale di look riesce ad essere credibile nei momenti comici e in quelli drammatici. ",
						genres, directors, cast, 5, 182,
						"https://images-na.ssl-images-amazon.com/images/I/71HyTegC0SL._AC_SY879_.jpg",
						"https://www.youtube.com/watch?v=vqWz0ZCpYBs");
				
				// Test room
				ArrayList<Room> rooms = new ArrayList<Room>();
				try {
					rooms.add(new Room(30 , 15));
					rooms.add(new Room(35 , 16));
				} catch (InvalidRoomDimensionsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Test projections
				ArrayList<Projection> cinemaProjections = new ArrayList<Projection>();
				Projection p1 = new Projection(123, DrukMovie, LocalDateTime.parse("2021-06-04T22:30:00"), new Money(12.5f),
						rooms.get(0));
				Projection p2 = new Projection(183, DrukMovie, LocalDateTime.parse("2021-06-01T20:15:00"), new Money(12.5f),
						rooms.get(0));
				Projection p3 = new Projection(193, DrukMovie, LocalDateTime.parse("2021-06-01T22:30:00"), new Money(12.5f),
						rooms.get(0));
				Projection p4 = new Projection(109, DrukMovie, LocalDateTime.parse("2021-06-02T22:30:00"), new Money(12.5f),
						rooms.get(0));
				Projection p5 = new Projection(743, DrukMovie, LocalDateTime.parse("2021-06-02T23:30:00"), new Money(12.5f),
						rooms.get(0));
				Projection p6 = new Projection(233, DrukMovie, LocalDateTime.parse("2021-06-02T19:00:00"), new Money(12.5f),
						rooms.get(0));
				Projection p7 = new Projection(184, DrukMovie, LocalDateTime.parse("2021-06-03T08:05:00"), new Money(12.5f),
						rooms.get(0));
				Projection p8 = new Projection(109, AvengersEndgameMovie, LocalDateTime.parse("2021-06-02T22:30:00"), new Money(12.5f),
						rooms.get(1));
				Projection p9 = new Projection(743, AvengersEndgameMovie, LocalDateTime.parse("2021-06-02T23:30:00"), new Money(12.5f),
						rooms.get(1));
				Projection p10 = new Projection(233, AvengersEndgameMovie, LocalDateTime.parse("2021-06-02T19:00:00"), new Money(12.5f),
						rooms.get(1));
				Projection p11 = new Projection(184, AvengersEndgameMovie, LocalDateTime.parse("2021-06-03T08:05:00"), new Money(12.5f),
						rooms.get(1));
				cinemaProjections.add(p1);
				cinemaProjections.add(p2);
				cinemaProjections.add(p3);
				cinemaProjections.add(p4);
				cinemaProjections.add(p5);
				cinemaProjections.add(p6);
				cinemaProjections.add(p7);
				cinemaProjections.add(p8);
				cinemaProjections.add(p9);
				cinemaProjections.add(p10);
				cinemaProjections.add(p11);
				// ********* END *********
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
		fail("Not yet implemented");
	}

}