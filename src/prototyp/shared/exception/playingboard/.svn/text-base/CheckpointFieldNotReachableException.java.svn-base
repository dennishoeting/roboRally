package prototyp.shared.exception.playingboard;

import prototyp.client.view.Page;

/**
 * Wird geworfen, wenn die Checkpoints nicht erreichbar sind
 * 
 * @author Marcus
 * @version 1.0
 */
public class CheckpointFieldNotReachableException extends PlayingboardException {
	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -4261188609632021151L;

	/** Anfang */
	private int first;

	/** Zu Angabe */
	private int to;

	/** Angabe, ob es vom Startfeld ausgeht */
	private boolean startfield;

	/**
	 * Default-Konstruktor
	 */
	public CheckpointFieldNotReachableException() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param first
	 *            Von was
	 * @param to
	 *            Zu was
	 * @param startfield
	 *            Angabe, ob es vom Startfeld ausgeht
	 */
	public CheckpointFieldNotReachableException(int first, int to, boolean startfield) {
		this.first = first;
		this.to = to;
		this.startfield = startfield;
	}

	/**
	 * Liefert die Fehlermeldung
	 * 
	 * @return Fehlermeldung
	 */
	@Override
	public String getMessage() {
		String middle = (this.startfield) ? Page.props.exception_playingboard_checkpointfieldnotreachableException_part2()
				: Page.props.exception_playingboard_checkpointfieldnotreachableException_part3();

		return Page.props.exception_playingboard_checkpointfieldnotreachableException_part1() + " " + first + " " + middle + " "
				+ to + " " + Page.props.exception_playingboard_checkpointfieldnotreachableException_part4();
	}

}
