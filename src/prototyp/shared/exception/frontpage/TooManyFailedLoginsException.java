package prototyp.shared.exception.frontpage;

import prototyp.client.view.Page;
import prototyp.shared.exception.RoboException;

/**
 * TooManyFailedLogins - Wird geworfen wenn ein User zu viele fehlgeschlagene
 * Loginversuche hat
 * 
 * @author Kamil
 * @version 1.0
 */
public class TooManyFailedLoginsException extends RoboException {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 2513319996028546919L;

	/**
	 * Default
	 */
	public TooManyFailedLoginsException() {

	}

	@Override
	public String getMessage() {
		return Page.props.exception_tooManyFailedLoginsException();
	}
}
