package prototyp.shared.exception.lobby;

import prototyp.client.view.Page;
import prototyp.shared.exception.RoboException;

/**
 * Wird genau dann geworfen wenn: Der Browser des Users sich drei Min. nicht
 * gemeldet hat und sich dann plötzlich wieder meldet.
 * 
 * @author timo
 * 
 */
public class UsersBrowserCrashedException extends RoboException {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 3139399671072421200L;

	public UsersBrowserCrashedException() {

	}

	@Override
	public String getMessage() {
		return Page.props.exception_lobby_usersBrowserCrashedException();
	}
}
