package cinema.view.webgui;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.rythmengine.Rythm;

/**
 * Apre la porta TCP sulla quale rimane in ascolto in attesa di rischieste da
 * parte dei client degli spettatori.
 *
 * <p>
 * Una volta aperta una connessione la richiesta viene gestita da un'istanza di
 * {@code WebGUIServlet}, tramite una servlet Jetty, sfruttando il motore di
 * rendering Rhythm per la generazione dinamica delle pagine web da mostrare
 * allo spettatore.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class WebGUIServer {

	/**
	 * Porta aperta dal server web in ascolto in attesa delle richieste HTTP
	 * generate dai client (spettatori).
	 */
	private int port;

	/**
	 * Servlet utilizzata per gestire le richieste dei client (spettatori).
	 */
	private Servlet servlet;

	/**
	 * Costruttore del server web.
	 *
	 * @param port    porta aperta dal server web.
	 * @param servlet servlet utilizzata per gestire le richieste dei client.
	 */
	public WebGUIServer(int port, Servlet servlet) {
		this.port = port;
		this.servlet = servlet;
	}

	/**
	 * Inizializza il server web e lo avvia in attesa di connessioni.
	 *
	 * <p>
	 * Viene configurato il motore di rendering delle pagine web e successivamente
	 * creato inizializzato l'oggetto {@code Server} e avviato quest'ultimo.
	 *
	 * @throws Exception in caso di errori riscontrati durante la fase di
	 *                   inizializzazione dei componenti.
	 */
	public void start() throws Exception {
		initRythm();
		Server server = new Server(port);
		ServletContextHandler handler = new ServletContextHandler();
		handler.addServlet(new ServletHolder(servlet), "/*");
		addStaticFileServing(handler);
		server.setHandler(handler);
		server.start();
	}

	/**
	 * Imposta il gestore dei file statici utilizzati dalle pagine web della GUI.
	 *
	 * <p>
	 * I file statici (immagini e script JavaScript di frontend) si trovano
	 * all'interno della cartella {@code src/main/resources/static}.
	 *
	 * @param handler gestore del contesto per la servlet utilizzata dal server.
	 */
	private void addStaticFileServing(ServletContextHandler handler) {
		ServletHolder holderPwd = new ServletHolder("default", new DefaultServlet());
		holderPwd.setInitParameter("resourceBase", "./src/main/resources/static");
		holderPwd.setInitParameter("dirAllowed", "false");
		holderPwd.setInitParameter("pathInfoOnly", "true");
		handler.addServlet(holderPwd, "/static/*");
	}

	/**
	 * Inizializza il motore di rendering dei template (Rhythm) delle pagine web
	 * gestite dal server.
	 *
	 * <p>
	 * I template si trovano all'interno della cartella
	 * {@code src/main/resources/templates}
	 */
	private void initRythm() {
		Map<String, Object> configuration = new HashMap<>();
		configuration.put("home.template.dir", "templates");
		Rythm.init(configuration);
	}

}
