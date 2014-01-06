package prototyp.shared.exception.playingboard;

import prototyp.client.view.Page;

/**
 * Exception, die geworfen wird, falls ein Reparaturfeld nicht erreichbar ist
 * 
 * @author Marcus
 * @version 1.0
 */
public class RepairFieldNotReachableExeption extends PlayingboardException {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 462334137113138849L;

	/** X-Koordinate */
	private int x;

	/** Y-Koordinate */
	private int y;

	/**
	 * Default-Konstruktor
	 */
	public RepairFieldNotReachableExeption() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param x
	 *            X-Koordinate
	 * @param y
	 *            Y-Koordinate
	 */
	public RepairFieldNotReachableExeption(int x, int y) {
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
		return Page.props.exception_playingboard_repairfieldnotreachableException_part1() + this.x + ", " + this.y
				+ Page.props.exception_playingboard_repairfieldnotreachableException_part2();
	}
}
