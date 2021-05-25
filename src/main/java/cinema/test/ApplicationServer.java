package cinema.test;

import javax.servlet.Servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class ApplicationServer {

    private int port;
    private Servlet servlet;
    private Server server;

    public ApplicationServer(int port, Servlet servlet) {
        this.port = port;
        this.servlet = servlet;
    }

    public void start() throws Exception {
        server = new Server(port);
        ServletContextHandler handler = new ServletContextHandler();
        handler.addServlet(new ServletHolder(servlet), "/*");
        addStaticFileServing(handler);
        server.setHandler(handler);
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }
  
    private void addStaticFileServing(ServletContextHandler handler) {
        ServletHolder holderPwd = new ServletHolder("default", new DefaultServlet());
        holderPwd.setInitParameter("resourceBase", "./src/main/resources/static");
        holderPwd.setInitParameter("dirAllowed","false");
        holderPwd.setInitParameter("pathInfoOnly","true");
        handler.addServlet(holderPwd, "/static/*");
    }
}
