package cinema.controller.handlers.util;

/**
 * Gestisce le eccezioni generate dalle classi EmailHanlder e ReportHandler.
 * 
 * @author Screaming Hairy Armadillo Team
 *
 */
@SuppressWarnings("serial")
public class HandlerException extends Exception {

	public HandlerException(String message) {
		super(message);
	}

}