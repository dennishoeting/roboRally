package prototyp.shared.exception.administration;

import prototyp.client.view.Page;
import prototyp.server.view.AdministrationImpl;
import prototyp.shared.exception.RoboException;

/**
 * NoModificationException - Wird geworfen, wenn keine Änderungen vorgenommen
 * werden können.
 * 
 * @author Andreas
 * @version 1.0
 * 
 * @see AdministrationImpl
 */
public class NoModificationException extends RoboException {

	/** Seriennummer */
	private static final long serialVersionUID = 1788913777823124189L;

	/**
	 * Konstruktor
	 */
	public NoModificationException() {

	}

	@Override
	public String getMessage() {
		return Page.props.exception_noModificationException();
	}
}
