package prototyp.shared.exception.frontpage;

import prototyp.client.view.Page;
import prototyp.shared.exception.RoboException;

/**
 * InvalidLogInDataException - Wird geworfen wenn die LogInDaten ungültig sind
 * 
 * @author Kamil, Andreas
 * @version 1.0
 */
public class InvalidLogInDataException extends RoboException {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -1492186408465902789L;

	public InvalidLogInDataException() {
		
	}

	@Override
	public String getMessage() {
		return Page.props.exception_frontpage_invalidLogInDataException();
	}
}
