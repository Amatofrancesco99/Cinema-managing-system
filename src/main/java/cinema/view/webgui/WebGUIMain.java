package cinema.view.webgui;

/**
 * Punto di avvio del server HTTP relativo all'applicazione web che permette
 * agli spettatori del cinema di poter effettuare le operazioni richieste dalle
 * specifiche di progetto (visualizzare i film e le proiezioni programmate con i
 * relativi dettagli, selezionare una specifica proiezione e acquistare
 * biglietti all'interno di un'unica prenotazione per una determinata
 * proiezione).
 * 
 * @author Screaming Hairy Armadillo Team
 * 
 */
public class WebGUIMain {

	/**
	 * Porta aperta dal server web in ascolto in attesa delle richieste HTTP
	 * generate dai client (spettatori).
	 */
	public static final int PORT = 8080;

	/**
	 * Avvia il server web in ascolto sulla porta specificata.
	 *
	 * <p>
	 * Le richieste HTTP dei client sono gestite attraverso l'istanza della servlet
	 * {@code WebGUIServlet} creata al momento dell'avvio del server web.
	 *
	 * @param args parametri dell'applicazione (non utilizzati).
	 */
	public static void main(String[] args) {
		try {
			new WebGUIServer(PORT, new WebGUIServlet()).start();
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
	}

}
