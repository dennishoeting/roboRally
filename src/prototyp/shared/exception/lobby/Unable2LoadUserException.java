package prototyp.shared.exception.lobby;

import prototyp.client.view.Page;
import prototyp.server.view.LobbyImpl;
import prototyp.shared.exception.RoboException;

/**
 * Unable2LoadUserException - Wird geworfen wenn ein User nicht geladen werden
 * kann
 * 
 * @author Kamil
 * @version 1.0
 * 
 * @see LobbyImpl
 */

public class Unable2LoadUserException extends RoboException {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 7948096866946908632L;

	public Unable2LoadUserException() {

	}

	@Override
	public String getMessage() {
		return Page.props.exception_lobby_unable2LoadUserException();
	}
	
	
}
