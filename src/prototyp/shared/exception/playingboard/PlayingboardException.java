package prototyp.shared.exception.playingboard;

import prototyp.shared.exception.RoboException;


/**
 * Exception die geworfen wird, wenn ein ungültiges Playingboard gespeichert
 * werden soll.
 * 
 * @author Marcus
 * 
 */
public abstract class PlayingboardException extends RoboException {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -282200998749767644L;

	/**
	 * Konstruktor
	 */
	public PlayingboardException() {
	}

}
