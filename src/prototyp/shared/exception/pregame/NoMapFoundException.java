package prototyp.shared.exception.pregame;

import prototyp.client.view.Page;
import prototyp.shared.exception.RoboException;

/**
 * Wird geworfen, wenn die Karte nicht in der Datenbank gefunden werden kann.
 * 
 * @author Andreas
 * @version 1.0
 * 
 * @see RefereePage, MapGeneratorLoadWindow
 */
public class NoMapFoundException extends RoboException {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 5862517286608158536L;

	/**
	 * Default
	 */
	public NoMapFoundException() {

	}

	/**
	 * Liefert die Fehlermeldung
	 * 
	 * @param Fehlermeldung
	 */
	@Override
	public String getMessage() {
		return Page.props.exception_noMapFoundException();
	}

}
