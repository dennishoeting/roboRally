package prototyp.shared.exception.lobby;

import prototyp.client.view.Page;
import prototyp.shared.exception.RoboException;

/**
 * Falls ein hochgeladenes Image nicht existiert. Oder das Format falsch ist.
 * 
 * @author timo
 * 
 */
public class PhotoUploadException extends RoboException {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -2667411717021053588L;

	public PhotoUploadException() {

	}

	@Override
	public String getMessage() {
		return Page.props.exception_lobby_photoUploadException();
	}

}
