package cinema.view.webgui;

/**
 * Classe principale utilizzata per la creazione del server.
 * 
 * @author Screaming Hairy Armadillo Team
 * 
 */
public class WebGUIMain {

	public static void main(String[] argv) {
		try {
			new WebGUIServer(8080, new WebGUIServlet()).start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
