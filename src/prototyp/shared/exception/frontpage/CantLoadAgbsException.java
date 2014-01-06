package prototyp.shared.exception.frontpage;

import prototyp.client.view.Page;
import prototyp.shared.exception.RoboException;

/**
 * Falls die Agbs nicht geladen werden können.
 * 
 * @author timo
 * 
 */
public class CantLoadAgbsException extends RoboException {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 4380981583312669931L;
	
	/**
	 * Default
	 */
	public CantLoadAgbsException() {

	}

	@Override
	public String getMessage() {
		return Page.props.exception_frontpage_cantLoadAgbsException();
	}

}
