package prototyp.shared.exception.frontpage;

import prototyp.client.view.Page;
import prototyp.shared.exception.RoboException;

/**
 * WrongPasswordException - Wird geworfen wenn ein User nicht das richtige
 * Password eingibt
 * 
 * @author Kamil
 * @version 1.0
 */
public class WrongPasswordException extends RoboException {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -1204525097794653537L;

	/**
	 * Default
	 */
	public WrongPasswordException() {

	}

	@Override
	public String getMessage() {
		return Page.props.exception_wrongPassword();
	}
	
	
}
