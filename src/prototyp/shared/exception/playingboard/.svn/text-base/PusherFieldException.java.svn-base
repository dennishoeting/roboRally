package prototyp.shared.exception.playingboard;

import prototyp.client.view.Page;

/**
 * @author Marcus
 * @version 1.0
 */
public class PusherFieldException extends PlayingboardException {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 6556600970738647471L;

	/** X-Koordinate */
	private int x;

	/** Y-Koordinate */
	private int y;

	/**
	 * Default-Konstruktor
	 */
	public PusherFieldException() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param x
	 *            X-Koordinate
	 * @param y
	 *            Y-Koordinate
	 */
	public PusherFieldException(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Liefert die Fehlermeldung
	 * 
	 * @return Fehlermeldung
	 */
	@Override
	public String getMessage() {
		return Page.props.exception_playingboard_pusherfieldException_part1() + this.x + ", " + this.y
				+ Page.props.exception_playingboard_pusherfieldException_part2();
	}
}
