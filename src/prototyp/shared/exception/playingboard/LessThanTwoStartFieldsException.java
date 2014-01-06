package prototyp.shared.exception.playingboard;

import prototyp.client.view.Page;

/**
 * Wird geworfen, wenn sich auf einem Spiebrett weniger als 2 Startfelder
 * befinden
 * 
 * @author Marcus
 * @version 1.0
 */
public class LessThanTwoStartFieldsException extends PlayingboardException {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -2497983351929261701L;

	/**
	 * Konstruktor
	 */
	public LessThanTwoStartFieldsException() {
	}

	/**
	 * Liefert die Fehlermeldung
	 * 
	 * @return Fehlermeldung
	 */
	@Override
	public String getMessage() {
		return Page.props.exception_playingboard_lessThanTwoStartFieldsException();
	}

}
