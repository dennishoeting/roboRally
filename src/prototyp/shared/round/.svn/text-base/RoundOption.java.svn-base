package prototyp.shared.round;

import java.io.Serializable;

import prototyp.server.model.Round;

/**
 * Die Spieloptionen für eine Spielrunde
 * 
 * @author Marcus, Jannik, Andreas
 * @version 1.3
 * 
 * @see {@link Round}, {@link RoundInfo}
 */
public class RoundOption implements Serializable {

	/** Seriennummer */
	private static final long serialVersionUID = 2066044679284089766L;

	/** gibt an, ob die Presser während einer Spielrunde ständig aktiviert sind */
	private boolean alwaysCompactorsOn;

	/** gibt an, ob die Pusher während einer Spielrunde ständig aktiviert sind */
	private boolean alwaysPushersOn;

	/** gibt an, ob die Förderbänder aktiviert sind */
	private boolean alwaysConveyorBeltsOn;

	/** gibt an, ob die Zahnräder aktiviert sind */
	private boolean alwaysGearsOn;

	/** gibt an, ob Laser in der Spielrunde an oder ausgestellt */
	private boolean lasersOn;

	/** die Anzahl der Spielerplätze einer Spielrunde */
	private int playerSlots;

	/** gibt an, ob Roboter nach jedem Spielzug schießen */
	private boolean robotShootsOn;

	/** gibt an, ob es einen Countdown beim Legen der Programmierkarten gibt */
	private boolean reduceCountdown;

	/** die Anzahl der Beobachterplätze einer Spielrunde standardmäßig "5" */
	private int watcherSlots = 5;
	
	/** Länge des CountdownTimers in der Spielrunde standardmäßig "40" */
	private int countDownTime = 40;

	/** Name des Spiels */
	private String gameName = "Game";

	/**
	 * Default-Konstruktor zum Erzeugen von Optionen
	 */
	public RoundOption() {
		this.lasersOn = false;
		this.robotShootsOn = false;
		this.reduceCountdown = false;
		this.alwaysPushersOn = false;
		this.alwaysCompactorsOn = false;
		this.alwaysConveyorBeltsOn = false;
		this.alwaysGearsOn = false;
		this.watcherSlots = 5;
		this.playerSlots = 0;
		this.setCountDownTime(40);
	}

	/**
	 * Liefert den Namen des Spiels.
	 * 
	 * @return gameName
	 */
	public String getGameName() {
		return this.gameName;
	}

	/**
	 * Liefert die Anzahl der Spielerplätze für eine Spielrunde.
	 * 
	 * @return die Anzahl der Spielerplätze
	 */
	public int getPlayersSlots() {
		return this.playerSlots;
	}

	/**
	 * Liefert die Anzahl der Beobachterplätze für eine Spielrunde.
	 * 
	 * @return die Anzahl der Beobachterplätze
	 */
	public int getWatchersSlots() {
		return this.watcherSlots;
	}

	/**
	 * Gibt an, ob die Förderbänder ständig aktiv sind
	 * 
	 * @return true, wenn diese immer gezeigt werden.
	 */
	public boolean isAlwaysConveyorBeltsOn() {
		return this.alwaysConveyorBeltsOn;
	}

	/**
	 * Liefert die Angabe, ob die Zahnräder aktiviert sind
	 * 
	 * @return alwaysGearsOn
	 */
	public boolean isAlwaysGearsOn() {
		return this.alwaysGearsOn;
	}

	/**
	 * Gibt an, ob Presser während einer Spielrunde immer aktiviert sind.
	 * 
	 * @return true, wenn diese immer aktiviert sind
	 */
	public boolean isAlwaysCompactorsOn() {
		return this.alwaysCompactorsOn;
	}

	/**
	 * Gibt an, ob Pusher während einer Spielrunde immer aktiviert sind.
	 * 
	 * @return true, wenn diese immer aktiviert sind
	 */
	public boolean isAlwaysPushersOn() {
		return this.alwaysPushersOn;
	}

	/**
	 * Gibt an, ob Laser in einer Spielrunde an- oder ausgeschaltet sind.
	 * 
	 * @return true, wenn Laser angeschaltet sind
	 */
	public boolean isLasersOn() {
		return this.lasersOn;
	}

	/**
	 * Gibt an, ob die Countdown-Funktion beim Ready verringert wird
	 * 
	 * @return true, wenn die Funktion aktiviert ist.
	 */
	public boolean isReduceCountdown() {
		return this.reduceCountdown;
	}

	/**
	 * Gibt an, ob Roboter nach jedem Spielschritt mit Lasern schießen.
	 * 
	 * @return true, wenn Roboter schießen
	 */
	public boolean isRobotShootsOn() {
		return this.robotShootsOn;
	}

	/**
	 * Legt fest, die Förderbänder aktiv sind
	 * 
	 * @param alwaysConveyorBeltOn
	 *            true, wenn Förderbänder aktiv sind
	 */
	public void setAlwaysConveyorBeltsOn(boolean alwaysConveyorBeltOn) {
		this.alwaysConveyorBeltsOn = alwaysConveyorBeltOn;
	}

	/**
	 * Setzt die Option, dass die Zahnräder aktiviert sind
	 * 
	 * @param alwaysGearsOn
	 *            the alwaysGearsOn to set
	 */
	public void setAlwaysGearsOn(boolean alwaysGearsOn) {
		this.alwaysGearsOn = alwaysGearsOn;
	}

	/**
	 * Legt fest, ob Presser in einer Spielrunde ständig aktiv sind.
	 * 
	 * @param alwaysPresserOn
	 *            true, wenn Presser ständig aktiv sein sollen
	 */
	public void setAlwaysCompactorsOn(boolean alwaysPresserOn) {
		this.alwaysCompactorsOn = alwaysPresserOn;
	}

	/**
	 * Legt fest, ob Pusher in einer Spielrunde ständig aktiv sind.
	 * 
	 * @param alwaysPusherOn
	 *            true, wenn Pusher ständig aktiv sein sollen
	 */
	public void setAlwaysPushersOn(boolean alwaysPusherOn) {
		this.alwaysPushersOn = alwaysPusherOn;
	}

	/**
	 * Setzt einen Spiel Namen.
	 * 
	 * @param gameName
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	/**
	 * Legt fest, ob Wandlaser in einer Spielrunde aktiviert werden sollen.
	 * 
	 * @param laserOn
	 *            true, wenn Wandlaser aktiviert werden sollen.
	 */
	public void setLasersOn(boolean laserOn) {
		this.lasersOn = laserOn;
	}

	/**
	 * Legt die Anzahl der Spielerplätze fest.
	 * 
	 * @param playerSlots
	 *            die neue Anzahl der Spielerplätze
	 */
	public void setPlayerSlots(int playerSlots) {
		this.playerSlots = playerSlots;
	}

	/**
	 * Legt fest, ob ein Countdown bei einem Ready verringert wird
	 * 
	 * @param reduceCountdown
	 *            true, wenn der Countdown verringert werden soll
	 */
	public void setReduceCountdown(boolean reduceCountdown) {
		this.reduceCountdown = reduceCountdown;
	}

	/**
	 * Legt fest, ob Roboter nach einem Spielschritt mit Lasern schießen.
	 * 
	 * @param robotShootsOn
	 *            true, wenn Roboter schießen sollen.
	 */
	public void setRobotShootsOn(boolean robotShootsOn) {
		this.robotShootsOn = robotShootsOn;
	}

	/**
	 * Legt die Anzahl der Beobachterplätze fest.
	 * 
	 * @param watcherSlots
	 *            die neue Anzahl der Beobachterplätze
	 */
	public void setWatcherSlots(int watcherSlots) {
		this.watcherSlots = watcherSlots;
	}

	/**
	 * Setzt eine neue Zeit für den CountDownTimer in einer Spielrunde fest.
	 * @param countDownTime neue Zeit in Sekunden
	 */
	public void setCountDownTime(int countDownTime) {
		this.countDownTime = countDownTime;
	}

	/**
	 * Liefert die Zeit für den CountDownTimer in einer Spielrunde
	 * 
	 * @return countDownTime
	 */
	public int getCountDownTime() {
		return countDownTime;
	}
}
