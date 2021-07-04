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
 * Apre la porta sulla quale rimane in ascolto in attesa di rischieste da parte
 * degli spettatori. In seguito passa la gestione delle richieste alla Servlet.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
public class WebGUIServer {

	private int port;
	private Servlet servlet;

	public WebGUIServer(int port, Servlet servlet) {
		this.port = port;
		this.servlet = servlet;
	}

	public void start() throws Exception {
		initRythm();
		Server server = new Server(port);
		ServletContextHandler handler = new ServletContextHandler();
		handler.addServlet(new ServletHolder(servlet), "/*");
		addStaticFileServing(handler);
		server.setHandler(handler);
		server.start();
	}

	private void addStaticFileServing(ServletContextHandler handler) {
		ServletHolder holderPwd = new ServletHolder("default", new DefaultServlet());
		holderPwd.setInitParameter("resourceBase", "./src/main/resources/static");
		holderPwd.setInitParameter("dirAllowed", "false");
		holderPwd.setInitParameter("pathInfoOnly", "true");
		handler.addServlet(holderPwd, "/static/*");
	}

	private void initRythm() {
		Map<String, Object> configuration = new HashMap<>();
		configuration.put("home.template.dir", "templates");
		Rythm.init(configuration);
	}

}
