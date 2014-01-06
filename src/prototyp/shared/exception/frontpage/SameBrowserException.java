package prototyp.shared.exception.frontpage;

import prototyp.client.view.Page;
import prototyp.shared.exception.RoboException;

/**
 * Wird geworfen wenn der User sich zwei mal Versucht mit einem
 * Browser einzulogen.
 * @author timo
 *
 */
public class SameBrowserException extends RoboException{

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 241230995898309223L;

	@Override
	public String getMessage() {
		return Page.props.exception_frontpage_sameBrowserException();
	}

}
