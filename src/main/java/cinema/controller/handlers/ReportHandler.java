package cinema.controller.handlers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import cinema.model.cinema.PhysicalSeat;
import cinema.model.cinema.util.RoomException;
import cinema.model.reservation.Reservation;
import cinema.controller.handlers.util.HandlerException;

/**
 * Crea un report, in formato .pdf, contenente tutte le informazioni inerenti
 * alla prenotazione.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class ReportHandler {

	/**
	 * Nome del cinema.
	 */
	private String name;

	/**
	 * E-mail del cinema.
	 */
	private String email;

	/**
	 * Ubicazione del cinema.
	 */
	private String location;

	/**
	 * URL del logo del cinema.
	 */
	private String logoURL;

	/**
	 * Costruttore del gestore del report.
	 * 
	 * @param name     nome del cinema.
	 * @param email    e-mail del cinema.
	 * @param location ubicazione del cinema.
	 * @param logoURL  URL del logo del cinema (iconfinder.com).
	 */
	public ReportHandler(String name, String email, String location, String logoURL) {
		this.name = name;
		this.email = email;
		this.location = location;
		this.logoURL = logoURL;
	}

	/** METODO per la creazione del report */
	public void createReport(Reservation reservation) throws HandlerException {
		// Posizione in cui il report sarà salvato
		String FILE = "./savedReports/Reservation_" + Long.toString(reservation.getProgressive()) + ".pdf";
		HashMap<String, Font> allFonts = createAllFonts(); // impostazione dei font che verranno utilizzati nel report
		try {
			Document document = createEmptyDocument(FILE); // Genera un documento vuoto
			document.open(); // Apre il documento
			addDocumentProperties(document, reservation); // Aggiunge le proprietà al documenti
			Image image = createReportLogoImage(); // Crea una nuova immagine con il logo del cinema
			Paragraph titleP = createReportTitleParagraph(allFonts); // Crea un paragrafo contenente il titolo del
																		// cinema
			Paragraph infoCinemaP = createCinemaInfoParagraph(allFonts); // Crea un paragrafo contenente alcune
																			// proprietà del cinema
			Paragraph filmP = createFilmTitleParagraph(allFonts, reservation); // Crea un paragrafo contenente il
																				// titolo del
			// film associato alla prenotazioni
			Paragraph infoFilmP = createFilmPropertiesParagraph(allFonts, reservation); // Crea un paragrafo
																						// contenente
			// alcune informazioni riassuntive sul
			// film che si vuole visionare
			// Crea un paragrafo con le informazioni della reservation
			Paragraph infoReservationP = createReservationPropertiesParagraph(allFonts, reservation);
			PdfPTable table = createEmptyTable(); // Genera una tabella
			insertFieldsIntoTable(table, reservation); // Aggiunge alla tabella i posti selezionati
			Paragraph totalP = createTotalParagraph(allFonts, reservation); // Crea un paragrafo con il totale della
																			// prenotazione
			addAllInfoToDocument(document, image, titleP, infoCinemaP, filmP, infoFilmP, infoReservationP, table,
					totalP); // Aggiunge al documento tutte le informazioni precedentemente create
			document.close(); // Chiude del documento
			reservation.setReportLocation(FILE); // se tutto va bene aggiunge il report alla cartella contenente tutti i
													// report
			// emessi dal cinema
		} catch (Exception e) {
			throw new HandlerException("Si è verificato un problema nella generazione del report.");
		}
	}

	/**
	 * Aggiugne al documento (report) tutte le informazioni e i paragrafi utili a
	 * generarlo.
	 * 
	 * @param document         documento che formalmente rappresenta il nostro
	 *                         report.
	 * @param image            immagine con il logo del cinema.
	 * @param titleP           paragrafo con il titolo del cinema.
	 * @param infoCinemaP      paragrafo che contiene le proprietà del cinema.
	 * @param filmP            paragrafo che contiene il titolo del film associato
	 *                         alla prenotazione.
	 * @param infoFilmP        paragrafo che contiene le informazioni del film
	 *                         associato alla prenotazione.
	 * @param infoReservationP paragrafo che contiene le informazioni della
	 *                         prenotazione.
	 * @param table            tabella che conterrà i posti associati alla
	 *                         prenotazione.
	 * @param totalP           paragrafo che contiene il totale della prenotazione.
	 * @throws DocumentException se si verificano problemi nella generazione del
	 *                           documento.
	 */
	private void addAllInfoToDocument(Document document, Image image, Paragraph titleP, Paragraph infoCinemaP,
			Paragraph filmP, Paragraph infoFilmP, Paragraph infoReservationP, PdfPTable table, Paragraph totalP)
			throws DocumentException {
		document.add(image);
		document.add(titleP);
		document.add(infoCinemaP);
		document.add(filmP);
		document.add(infoFilmP);
		document.add(infoReservationP);
		document.add(table);
		document.add(totalP);
	}

	/**
	 * Generare un paragrafo contenente il totale della prenotazione.
	 * 
	 * @param allFonts    HashMap che contiene tutti i font utili.
	 * @param reservation prenotazione di cui si vuole creare il report.
	 * @return il paragrafo che contiene il totale della prenotazione.
	 */
	private Paragraph createTotalParagraph(HashMap<String, Font> allFonts, Reservation reservation) {
		Paragraph totalP = new Paragraph("Totale   " + String.format("%.02f", reservation.getTotal()) + " EUR",
				allFonts.get("subFont3"));
		totalP.setSpacingBefore(60);
		totalP.setAlignment(Element.ALIGN_RIGHT);
		totalP.setIndentationRight(55);
		return totalP;
	}

	/**
	 * Aggiungere alla tabella i posti selezionati nella prenotazione.
	 * 
	 * @param table       tabella che conterrà i posti associati alla prenotazione.
	 * @param reservation prenotazione di cui si vuole creare il report.
	 * @throws RoomException se ci sono delle eccezioni generate dalla classe Room.
	 */
	private void insertFieldsIntoTable(PdfPTable table, Reservation reservation) throws RoomException {
		PdfPCell c1 = new PdfPCell(new Phrase("Posti prenotati scelti al momento dell'acquisto"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		c1.setMinimumHeight(30);
		table.addCell(c1);
		for (PhysicalSeat s : reservation.getSeats()) {
			String seatCoordinates = reservation.getProjection().getSeatCoordinates(s);
			if (seatCoordinates != null) {
				PdfPCell cSeat = new PdfPCell(new Phrase("Fila " + seatCoordinates.replaceAll("\\d", "") + ",   Posto "
						+ seatCoordinates.replaceAll("[\\D]", "")));
				cSeat.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cSeat.setMinimumHeight(20);
				table.addCell(cSeat);
			}
		}
	}

	/**
	 * Crea una tabella vuota.
	 * 
	 * @return una tabella vuota.
	 */
	private PdfPTable createEmptyTable() {
		PdfPTable table = new PdfPTable(1);
		table.setSpacingBefore(60);
		return table;
	}

	/**
	 * Genera un paragrafo contenente alcune informazioni riassuntive sulla
	 * prenotazione effettuata.
	 * 
	 * @param allFonts    HashMap che contiene tutti i font utili.
	 * @param reservation prenotazione di cui si vuole creare il report.
	 * @return il paragrafo che contiene le informazioni della prenotazione.
	 */
	private Paragraph createReservationPropertiesParagraph(HashMap<String, Font> allFonts, Reservation reservation) {
		String dayOfWeek = reservation.getProjection().getDateTime().getDayOfWeek().getDisplayName(TextStyle.FULL,
				Locale.ITALIAN);
		String month = reservation.getProjection().getDateTime().getMonth().getDisplayName(TextStyle.FULL,
				Locale.ITALIAN);
		Paragraph infoReservationP = new Paragraph("Prenotazione effettuata da " + reservation.getPurchaser().getName()
				+ " " + reservation.getPurchaser().getSurname() + "\n" + "Sala "
				+ reservation.getProjection().getRoom().getNumber() + "   -   " + dayOfWeek.toUpperCase().charAt(0)
				+ dayOfWeek.substring(1) + " " + reservation.getProjection().getDateTime().getDayOfMonth() + " "
				+ month.toUpperCase().charAt(0) + month.substring(1) + " "
				+ reservation.getProjection().getDateTime().getYear() + "  alle  "
				+ String.format("%02d", reservation.getProjection().getDateTime().getHour()) + ":"
				+ String.format("%02d", reservation.getProjection().getDateTime().getMinute()),
				allFonts.get("subFont25"));
		infoReservationP.setSpacingBefore(30);
		return infoReservationP;
	}

	/**
	 * Genera un paragrafo contente alcune informazioni riassuntive sul film.
	 * 
	 * @param allFonts    HashMap che contiene tutti i font utili.
	 * @param reservation prenotazione di cui si vuole creare il report.
	 * @return il paragrafo che contiene le informazioni del film associato alla
	 *         prenotazione.
	 */
	private Paragraph createFilmPropertiesParagraph(HashMap<String, Font> allFonts, Reservation reservation) {
		Paragraph infoFilmP = new Paragraph("Regista/i:  "
				+ reservation.getProjection().getMovie().getDirectors().toString().replaceAll("\\[", "").replaceAll(
						"\\]", "")
				+ "      Durata:  " + reservation.getProjection().getMovie().getDuration() + " min."
				+ "      Rating film:  " + reservation.getProjection().getMovie().getRating() + "/5",
				allFonts.get("subFont2"));
		return infoFilmP;
	}

	/**
	 * Genera un paragrafo contenente il titolo del film.
	 * 
	 * 
	 * @param allFonts    HashMap che contiene tutti i font utili.
	 * @param reservation prenotazione di cui si vuole creare il report.
	 * @return il paragrafo che contiene il titolo del film associato alla
	 *         prenotazione.
	 */
	private Paragraph createFilmTitleParagraph(HashMap<String, Font> allFonts, Reservation reservation) {
		Paragraph FilmP = new Paragraph(">  " + reservation.getProjection().getMovie().getTitle(),
				allFonts.get("subFont"));
		FilmP.setSpacingBefore(40);
		return FilmP;
	}

	/**
	 * Genera un paragrafo contenente alcune proprietà riassuntive del cinema.
	 * 
	 * @param allFonts HashMap che contiene tutti i font utili.
	 * @return il paragrafo che contiene le proprietà del cinema.
	 */
	private Paragraph createCinemaInfoParagraph(HashMap<String, Font> allFonts) {
		Paragraph infoCinemaP = new Paragraph(location + "\n" + email + "\n", allFonts.get("smallFont"));
		infoCinemaP.setSpacingBefore(10);
		infoCinemaP.setAlignment(Element.ALIGN_CENTER);
		return infoCinemaP;
	}

	/**
	 * Genera un paragrafo contenente il titolo del report (nome del cinema).
	 * 
	 * @param allFonts HashMap che contiene tutti i font utili.
	 * @return il paragrafo con il titolo del cinema.
	 */
	private Paragraph createReportTitleParagraph(HashMap<String, Font> allFonts) {
		Paragraph titleP = new Paragraph(name + "\n", allFonts.get("catFont"));
		titleP.setSpacingBefore(80);
		titleP.setAlignment(Element.ALIGN_CENTER);
		return titleP;
	}

	/**
	 * Crea una nuova immagine contenente il logo del cinema e setta le sue
	 * dimensioni.
	 * 
	 * @return l'immagine con il logo del cinema.
	 * @throws BadElementException   se l'immagine non viene generata nel modo
	 *                               corretto.
	 * @throws MalformedURLException se l'URL è malformato.
	 * @throws IOException           se l'immagine non viene generata con un input
	 *                               corretto.
	 */
	private Image createReportLogoImage() throws BadElementException, MalformedURLException, IOException {
		String imageUrl = logoURL;
		Image image = Image.getInstance(new URL(imageUrl));
		image.scalePercent(20f);
		image.setAbsolutePosition(250f, 715f);
		return image;
	}

	/**
	 * Aggiugne i metadati al documento.
	 * 
	 * @param document    documento che formalmente rappresenta il nostro report.
	 * @param reservation prenotazione di cui si vuole creare il report.
	 */
	private void addDocumentProperties(Document document, Reservation reservation) {
		document.addTitle("Prenotazione numero " + reservation.getProgressive());
		document.addSubject("Using iText");
		document.addKeywords("Java, PDF, iText");
		document.addAuthor("Screaming Hairy Armadillo Team");
		document.addCreator("Screaming Hairy Armadillo Team");
	}

	/**
	 * Crea un documento e lo apre in lettura.
	 * 
	 * @param FILE si referisce al percorso relativo dove verrà salvato il report.
	 * @return documento che formalmente rappresenta il nostro report.
	 * @throws FileNotFoundException se si verificano errori nell'apertura del file
	 *                               dove verrà salvato il report.
	 * @throws DocumentException     se si verificano problemi nella generazione del
	 *                               documento.
	 */
	private Document createEmptyDocument(String FILE) throws FileNotFoundException, DocumentException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(FILE));
		return document;
	}

	/**
	 * Genera tutti i font che verranno utilizzati nel report.
	 * 
	 * @return allFonts HashMap che contiene tutti i font utili.
	 */
	private HashMap<String, Font> createAllFonts() {
		HashMap<String, Font> allFonts = new HashMap<String, Font>();

		Font catFont = new Font(Font.FontFamily.HELVETICA, 33, Font.BOLD);
		Font subFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
		Font subFont2 = new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL);
		Font subFont25 = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
		Font subFont3 = new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL);
		Font smallFont = new Font(Font.FontFamily.HELVETICA, 14, Font.ITALIC);

		allFonts.put("catFont", catFont);
		allFonts.put("subFont", subFont);
		allFonts.put("subFont2", subFont2);
		allFonts.put("subFont25", subFont25);
		allFonts.put("subFont3", subFont3);
		allFonts.put("smallFont", smallFont);
		return allFonts;
	}
}
