package prototyp.shared.round;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Marcus
 * @version 1.0
 */
public class RoundNeedsWrapper implements Serializable {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -4971859002414978488L;

	/** das Spielbrett */
	private PlayingBoard playingBoard;

	/** die Rundenoptionen */
	private RoundOption roundOption;

	/** die Roboter */
	private Map<Integer, Robot> robots = new HashMap<Integer, Robot>();

	/**
	 * Fügt dem RobotManager einen Roboter hinzu.
	 * 
	 * @param robot
	 *            der hinzuzufügende Roboter
	 * @return true, wenn das Hinzufügen erfolgreich war
	 */
	public boolean addRobot(int playerId, Robot robot) {
		return this.robots.put(playerId, robot) != null;
	}

	/**
	 * @return the playingBoard
	 */
	public PlayingBoard getPlayingBoard() {
		return this.playingBoard;
	}

	/**
	 * @return the robots
	 */
	public Map<Integer, Robot> getRobots() {
		return this.robots;
	}

	/**
	 * @return the roundOption
	 */
	public RoundOption getRoundOption() {
		return this.roundOption;
	}

	/**
	 * @param playingBoard
	 *            the playingBoard to set
	 */
	public void setPlayingBoard(PlayingBoard playingBoard) {
		this.playingBoard = playingBoard;
	}

	/**
	 * @param robots
	 *            the robots to set
	 */
	public void setRobots(Map<Integer, Robot> robots) {
		this.robots = robots;
	}

	/**
	 * @param roundOption
	 *            the roundOption to set
	 */
	public void setRoundOption(RoundOption roundOption) {
		this.roundOption = roundOption;
	}

}
