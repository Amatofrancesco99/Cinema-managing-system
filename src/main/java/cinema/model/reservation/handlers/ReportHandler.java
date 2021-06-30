package cinema.model.reservation.handlers;

import java.io.FileOutputStream;
import java.net.URL;

import com.itextpdf.text.Document;
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
		// posizione in cui il report sarà salvato
		String FILE = "./savedReports/Reservation_"+Long.toString(r.getProgressive())+".pdf";
		
		// impostazione dei caratteri che verranno utilizzati nel report
		Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 33,
	            Font.BOLD);
		Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 20,
	            Font.BOLD);
		Font subFont2 = new Font(Font.FontFamily.TIMES_ROMAN, 14,
	            Font.NORMAL);
		Font subFont3 = new Font(Font.FontFamily.TIMES_ROMAN, 18,
	            Font.NORMAL);
		Font smallFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,
	            Font.ITALIC);  
		// tentativo di generazione del report
		 try {
			 	// creazione di un nuovo documento
	            Document document = new Document();
	            PdfWriter.getInstance(document, new FileOutputStream(FILE));
	            document.open();
	            
	            // aggiungere i metadati al documento
	            document.addTitle("PDF Report Reservation n°" + r.getProgressive());
		        document.addSubject("Using iText");
		        document.addKeywords("Java, PDF, iText");
		        document.addAuthor("Screaming Hairy Armadillo Team");
		        document.addCreator("Screaming Hairy Armadillo Team");
		        
		        // creazione di un colore di background per il nostro report (giallognolo)
		        String imageBG = "https://i.pinimg.com/originals/00/25/03/002503946c0a59d4ae800ab05a037fda.jpg";
		        
		        // creare una nuova immagine di quel colore e setting delle sue dimensioni
	            Image bg = Image.getInstance(new URL(imageBG));
	            bg.scalePercent(100f);
	            bg.setAbsolutePosition(-600f, -500f);
	            
	            // creazione di una nuova immagine con il logo del cinema
	            String imageUrl = Cinema.getLogoURL();
	            Image image = Image.getInstance(new URL(imageUrl));
	            image.scalePercent(20f);
	            image.setAbsolutePosition(250f, 715f);
		        
	            // creare un titolo per il report
		        Paragraph titleP = new Paragraph(Cinema.getName() + "\n", catFont);
		        titleP.setSpacingBefore(80);
		        titleP.setAlignment(Element.ALIGN_CENTER);
		        
		        // creare informazioni sul nome del cinema 
		        Paragraph infoCinemaP = new Paragraph(Cinema.getLocation() 
		        		+ "\n" + Cinema.getEmail() + "\n", smallFont);
		        infoCinemaP.setSpacingBefore(10);
		        infoCinemaP.setAlignment(Element.ALIGN_CENTER);
		        
		        // creazione di informazioni sul film che si vuole visionare
		        Paragraph FilmP = new Paragraph(r.getProjection().getMovie().getTitle(), subFont);
		        FilmP.setSpacingBefore(40);
		        
		        // creazione di alcune informazioni riassuntive sul film che si è prenotato
		        Paragraph infoFilmP = new Paragraph("Regista/i:  " + r.getProjection().getMovie().getDirectors().toString()
		        					+ "\t\t\t\t\t\t\tDurata:  " + r.getProjection().getMovie().getDuration() 
		        					+ "\t\t\t\t\t\t\tRating film:  " + r.getProjection().getMovie().getRating(),
		        					subFont2);
		        
		        // creazione di informazioni sulla prenotazione
		        Paragraph infoReservationP = new Paragraph("Sala n°:  " + r.getProjection().getRoom().getProgressive()
		        							+ "\t\t\t\t\t\t\tData:  " + r.getProjection().getDateTime().getDayOfWeek().toString().toLowerCase()
		        							+ " " + r.getProjection().getDateTime().getDayOfMonth()
		        							+ " " + r.getProjection().getDateTime().getMonth().toString().toLowerCase()
		        							+ " " + r.getProjection().getDateTime().getYear()
		        							+ " \t\t\t\t\t\tOra: " + String.format("%02d", r.getProjection().getDateTime().getHour()) 
		        							+ ":" + String.format("%02d", r.getProjection().getDateTime().getMinute())
		        							+ " \t\t\t\t\t\tCliente: " + r.getPurchaser().getName() + " " + r.getPurchaser().getSurname(),
		        							subFont2);
		        infoReservationP.setSpacingBefore(30);
		        
		        // generazione di una tabella 
		        PdfPTable table = new PdfPTable(1);
		        table.setSpacingBefore(60);

		        // aggiungere alla tabella come primo campo il titolo "Posti riservati"
		        PdfPCell c1 = new PdfPCell(new Phrase("POSTI RISERVATI"));
		        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(c1);
		        
		        //aggiungere alla tabella tutti i posti che si sono aggiunti alla prenotazione
		        for(PhysicalSeat s : r.getSeats()) {
		        	String seatCoordinates = r.getProjection().getSeatCoordinates(s);
		        	if(seatCoordinates != null)
		        		table.addCell("Fila: " + seatCoordinates.replaceAll("\\d","") +
		        				 "\t\tPosto: " + seatCoordinates.replaceAll("[\\D]",""));	
		        }
		        
		        //totale della prenotazione
		        Paragraph totalP = new Paragraph("TOTALE:  " + String.format("%.02f",r.getTotal())
		        				   + "€ ", subFont3);
		        totalP.setSpacingBefore(80);
		        totalP.setAlignment(Element.ALIGN_RIGHT);
		        totalP.setIndentationRight(55);
		        
		        // aggiunte al documento tutte le informazioni precedentemente create
		        document.add(bg);
		        document.add(image);
		        document.add(titleP);
		        document.add(infoCinemaP);
		        document.add(FilmP);
		        document.add(infoFilmP);
		        document.add(infoReservationP);
		        document.add(table);
		        document.add(totalP);
		        
		        // Chiusura del documento
	            document.close();
	            r.setReportLocation(FILE);
	        } catch (Exception e) {
	        	throw new HandlerException("Si è verificato un problema nella generazione del report.");
	        }
	}
}
