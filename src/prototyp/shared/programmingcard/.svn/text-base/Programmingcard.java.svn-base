package prototyp.shared.programmingcard;

import java.io.Serializable;

import prototyp.shared.util.RobotMovement;

/**
 * Programmierkarte
 * 
 * @author Marcus
 */
public class Programmingcard implements Serializable {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -9114168661196601407L;

	/** der Prioritätswert */
	private int priority;

	/** die Roboterbewegung */
	private RobotMovement robotMovement;

	/** PlayerId */
	private int playerId;

	/** der Pfad zum Bild */
	private String imagePath;

	/** der Pfad zur rückseite der Karte */
	public static final String BACKSIDE_IMAGEPATH = "playingCards/blank.png";

	/**
	 * Default-Konstruktor für Serializable-Interface
	 */
	public Programmingcard() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param priority
	 * @param robotMovement
	 */
	public Programmingcard(int priority, RobotMovement robotMovement) {
		this.priority = priority;
		this.robotMovement = robotMovement;

		switch (robotMovement) {
		case MOVE_FORWARDS:
			this.imagePath = "playingCards/move1.png";
			break;
		case MOVE_TWO_FORWARDS:
			this.imagePath = "playingCards/move2.png";
			break;
		case MOVE_THREE_FORWARDS:
			this.imagePath = "playingCards/move3.png";
			break;
		case MOVE_BACKWARDS:
			this.imagePath = "playingCards/backUp.png";
			break;
		case TURN_LEFT:
			this.imagePath = "playingCards/rotL.png";
			break;
		case TURN_RIGHT:
			this.imagePath = "playingCards/rotR.png";
			break;
		case TURN_AROUND:
			this.imagePath = "playingCards/uTurn.png";
			break;
		}
	}

	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return this.imagePath;
	}

	/**
	 * @return the playerId
	 */
	public int getPlayerId() {
		return this.playerId;
	}

	/**
	 * Liefert den Prioritätswert der Karte
	 * 
	 * @return der Prioritätswert
	 */
	public int getPriority() {
		return this.priority;
	}


	/**
	 * Liefert die Roboterbewegung
	 * 
	 * @return die Roboterbewegung
	 */
	public RobotMovement getRobotMovement() {
		return this.robotMovement;
	}

	/**
	 * @param playerId
	 *            the playerId to set
	 */
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

}
