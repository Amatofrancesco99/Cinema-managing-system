package cinema.model.reservation.handlers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

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

import cinema.controller.Cinema;
import cinema.model.cinema.PhysicalSeat;
import cinema.model.cinema.util.RoomException;
import cinema.model.reservation.Reservation;
import cinema.model.reservation.handlers.util.HandlerException;


/** BREVE DESCRIZIONE CLASSE ReportHandler
 * 
 * @author Screaming HairyArmadillo
 * 
 * Questa classe serve per creare un report, in formato .pdf, contenente tutte le informazioni 
 * inerenti la prenotazione.
 */
public class ReportHandler {
	
	/**
	 * METODO per la creazione del report
	 * @param r 					Prenotazione di cui si vuole creare il report
	 * @throws HandlerException 
	 */
	public static void createReport(Reservation r) throws HandlerException {
		String FILE = "./savedReports/Reservation_"+Long.toString(r.getProgressive())+".pdf";	// posizione in cui il report sarà salvato
		HashMap<String,Font> allFonts = createAllFonts();	// impostazione dei font che verranno utilizzati nel report
		generateReport(FILE,allFonts,r);					// tentativo di generazione del report
	}

	
	/** METODO per la creazione del report*/
	private static void generateReport(String FILE, HashMap<String,Font> allFonts, Reservation r) throws HandlerException {
		try {
			Document document = createEmptyDocument(FILE);		// generazione di un documento vuoto e apertura di quest ultimo
			document.open();
			addDocumentProperties(document,r);									// aggiungere le proprietà al documenti
	        Image image = createReportLogoImage();								// creazione di una nuova immagine con il logo del cinema
	        Paragraph titleP = createReportTitleParagraph(allFonts);			// creare un paragrafo contenente il titolo del cinema
	        Paragraph infoCinemaP = createCinemaInfoParagraph(allFonts);		// creare un paragrafo contenente alcune proprietà del cinema
	        Paragraph filmP = createFilmTitleParagraph(allFonts,r);				// creare un paragrafo contenente il titolo del film associato alla prenotazioni
	        Paragraph infoFilmP = createFilmPropertiesParagraph(allFonts, r);	// creazione di un paragrafo contenente alcune informazioni riassuntive sul film che si vuole visionare
	        Paragraph infoReservationP = createReservationPropertiesParagraph(allFonts, r); 	// creazione di informazioni sulla prenotazione
	        PdfPTable table = createEmptyTable();					// generazione di una tabella
	        insertFieldsIntoTable(table, r);						// aggiungere alla tabella i posti selezionati
	        Paragraph totalP = createTotalParagraph(allFonts, r);	//totale della prenotazione
	        addAllInfoToDocument(document, image, titleP, infoCinemaP, filmP, infoFilmP, infoReservationP, table, totalP); // aggiunte al documento tutte le informazioni precedentemente create
            document.close();	// Chiusura del documento
            r.setReportLocation(FILE);	// se tutto va bene aggiungo il report alla cartella contenente tutti i report emessi dal cinema
        } catch (Exception e) {
        	throw new HandlerException("Si è verificato un problema nella generazione del report.");
        }
	}


	/** METODO per aggiungere al documento tutte le informazioni e paragrafi generati 
	 * precedentemente
	 * @param document
	 * @param image
	 * @param titleP
	 * @param infoCinemaP
	 * @param filmP
	 * @param infoFilmP
	 * @param infoReservationP
	 * @param table
	 * @param totalP
	 * @throws DocumentException
	 */
	private static void addAllInfoToDocument(Document document, Image image, Paragraph titleP, Paragraph infoCinemaP, Paragraph filmP, Paragraph infoFilmP, Paragraph infoReservationP, PdfPTable table, Paragraph totalP) throws DocumentException {
		document.add(image);
        document.add(titleP);
        document.add(infoCinemaP);
        document.add(filmP);
        document.add(infoFilmP);
        document.add(infoReservationP);
        document.add(table);
        document.add(totalP);
	}


	/** METODO per generare un paragrafo contenente il totale della prenotazione
	 * 
	 * @param allFonts
	 * @param r
	 * @return
	 */
	private static Paragraph createTotalParagraph(HashMap<String, Font> allFonts, Reservation r) {
        Paragraph totalP = new Paragraph("TOTALE:  " + String.format("%.02f",r.getTotal())
		   + " EUR ", allFonts.get("subFont3"));
        totalP.setSpacingBefore(80);
        totalP.setAlignment(Element.ALIGN_RIGHT);
        totalP.setIndentationRight(55);
        return totalP;
	}


	/** METODO per aggiungere alla tabella i posti selezionati
	 * 
	 * @param table
	 * @param r
	 * @throws RoomException
	 */
	private static void insertFieldsIntoTable(PdfPTable table, Reservation r) throws RoomException {
		PdfPCell c1 = new PdfPCell(new Phrase("POSTI RISERVATI"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        for(PhysicalSeat s : r.getSeats()) {
        	String seatCoordinates = r.getProjection().getSeatCoordinates(s);
        	if(seatCoordinates != null)
        		table.addCell("Fila: " + seatCoordinates.replaceAll("\\d","") +
        				 "\t\tPosto: " + seatCoordinates.replaceAll("[\\D]",""));	
        }
	}


	/** METODO per creare una tabella vuota 
	 * 
	 * @return
	 */
	private static PdfPTable createEmptyTable() {
		PdfPTable table = new PdfPTable(1);
        table.setSpacingBefore(60);
        return table;
	}


	/** METODO per generare un paragrafo contenente alcune informazioni riassuntive sulla
	 * prenotazione effettuata
	 * 
	 * @param allFonts
	 * @param r
	 * @return
	 */
	private static Paragraph createReservationPropertiesParagraph(HashMap<String, Font> allFonts, Reservation r) {
		Paragraph infoReservationP = new Paragraph("Cliente: " + r.getPurchaser().getName() + " " + r.getPurchaser().getSurname() + "\n"
				+ "Sala " + r.getProjection().getRoom().getProgressive()
				+ "\t\t\t\t\t\t\tData:  " + r.getProjection().getDateTime().getDayOfWeek().toString().toLowerCase()
				+ " " + r.getProjection().getDateTime().getDayOfMonth() + " " + r.getProjection().getDateTime().getMonth().toString().toLowerCase() + " " + r.getProjection().getDateTime().getYear()
				+ " \t\t\t\t\t\tOra: " + String.format("%02d", r.getProjection().getDateTime().getHour()) + ":" + String.format("%02d", r.getProjection().getDateTime().getMinute()), allFonts.get("subFont2"));
		infoReservationP.setSpacingBefore(30);
		return infoReservationP;
	}


	/** METODO per generare un paragrafo contente alcune informazioni riassuntive sul film
	 * 
	 * @param allFonts
	 * @param r
	 * @return
	 */
	private static Paragraph createFilmPropertiesParagraph(HashMap<String, Font> allFonts, Reservation r) {
		Paragraph infoFilmP = new Paragraph("Regista/i:  " + r.getProjection().getMovie().getDirectors().toString()
				+ "\t\t\t\t\t\t\tDurata:  " + r.getProjection().getMovie().getDuration() 
				+ "\t\t\t\t\t\t\tRating film:  " + r.getProjection().getMovie().getRating(),
				allFonts.get("subFont2"));
		return infoFilmP;
	}


	/** METODO per generare un paragrafo contenente il titolo del film
	 * 
	 * @param allFonts
	 * @return
	 */
	private static Paragraph createFilmTitleParagraph(HashMap<String, Font> allFonts, Reservation r) {
		Paragraph FilmP = new Paragraph(r.getProjection().getMovie().getTitle(), allFonts.get("subFont"));
        FilmP.setSpacingBefore(40);
        return FilmP;
	}


	/** METODO per generare un paragrafo contenente alcune proprietà riassuntive del cinema
	 * 
	 * @param allFonts
	 * @return
	 */
	private static Paragraph createCinemaInfoParagraph(HashMap<String, Font> allFonts) {
		Paragraph infoCinemaP = new Paragraph(Cinema.getLocation() 
        		+ "\n" + Cinema.getEmail() + "\n", allFonts.get("smallFont"));
        infoCinemaP.setSpacingBefore(10);
        infoCinemaP.setAlignment(Element.ALIGN_CENTER);
        return infoCinemaP;
	}


	/** METODO per generare un paragrafo contenente il titolo del report (nome del cinema)
	 * 
	 * @param allFonts
	 * @return
	 */
	private static Paragraph createReportTitleParagraph(HashMap<String, Font> allFonts) {
		Paragraph titleP = new Paragraph(Cinema.getName() + "\n", allFonts.get("catFont"));
        titleP.setSpacingBefore(80);
        titleP.setAlignment(Element.ALIGN_CENTER);
        return titleP;
	}


	/** METODO per creare una nuova immagine contenente il logo del cinema e settare le sue
	 * dimensioni
	 * 
	 * @return
	 * @throws BadElementException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private static Image createReportLogoImage() throws BadElementException, MalformedURLException, IOException {
		String imageUrl = Cinema.getLogoURL();
        Image image = Image.getInstance(new URL(imageUrl));
        image.scalePercent(20f);
        image.setAbsolutePosition(250f, 715f);
        return image;
	}


	/** METODO per aggiungere i metadati al documento
	 * 
	 * @param document
	 * @param r
	 */
	private static void addDocumentProperties(Document document, Reservation r) {
		document.addTitle("Prenotazione numero " + r.getProgressive());
		document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Screaming Hairy Armadillo Team");
        document.addCreator("Screaming Hairy Armadillo Team");
	}


	/** METODO per creare un documento ed aprirlo in lettura
	 * 
	 * @param FILE
	 * @return
	 * @throws FileNotFoundException
	 * @throws DocumentException
	 */
	private static Document createEmptyDocument(String FILE) throws FileNotFoundException, DocumentException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(FILE));
		return document;
	}


	/** METODo per generare tutti i font che verranno utilizzati nel report
	 *  
	 * @return allFonts		Tutti i font utili nel report
	 */
	private static HashMap<String,Font> createAllFonts() {
		HashMap<String,Font> allFonts = new HashMap<String,Font>();
		
		Font catFont = new Font(Font.FontFamily.HELVETICA, 33, Font.BOLD);
		Font subFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
		Font subFont2 = new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL);
		Font subFont3 = new Font(Font.FontFamily.HELVETICA, 18,Font.NORMAL);
		Font smallFont = new Font(Font.FontFamily.HELVETICA, 14, Font.ITALIC);
		
		allFonts.put("catFont", catFont);
		allFonts.put("subFont", subFont);
		allFonts.put("subFont2", subFont2);
		allFonts.put("subFont3", subFont3);
		allFonts.put("smallFont", smallFont);
		return allFonts;  
	}
}
