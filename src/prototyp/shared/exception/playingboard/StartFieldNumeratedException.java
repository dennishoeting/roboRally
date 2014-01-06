package prototyp.shared.exception.playingboard;

import prototyp.client.view.Page;

public class StartFieldNumeratedException extends PlayingboardException {
	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 7080962554068376853L;

	/**
	 * Default-Konstruktor
	 */
	public StartFieldNumeratedException() {
	}

	/**
	 * Liefert die Fehlermeldung
	 * 
	 *@return Fehlermeldung
	 */
	@Override
	public String getMessage() {
		return Page.props.exception_playingboard_startfieldNumeratedException();
	}

}
