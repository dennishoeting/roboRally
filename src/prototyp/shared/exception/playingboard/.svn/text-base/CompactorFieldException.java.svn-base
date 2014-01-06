package prototyp.shared.exception.playingboard;

import prototyp.client.view.Page;

/**
 * 
 * @author Marcus
 * @version 1.0
 */
public class CompactorFieldException extends PlayingboardException {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 3502544710165127512L;

	/** X-Koordinate */
	private int x;

	/** Y-Koordiante */
	private int y;

	/** Nachfolger oder Vorgänger */
	private boolean follower;

	/**
	 * Default-Konstruktor für Serializable-Inteerface
	 */
	public CompactorFieldException() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param x
	 *            X-Koordinate
	 * @param y
	 *            Y-Koordinate
	 * @param follower
	 *            Nachfolger oder Vorgänger
	 */
	public CompactorFieldException(int x, int y, boolean follower) {
		this.x = x;
		this.y = y;
		this.follower = follower;
	}

	@Override
	public String getMessage() {
		String end = (follower) ? Page.props.exception_playingboard_compactorFieldException_part3() : Page.props
				.exception_playingboard_compactorFieldException_part2();
		return Page.props.exception_playingboard_compactorFieldException_part1() + this.x + ", " + this.y + end;
	}

}
