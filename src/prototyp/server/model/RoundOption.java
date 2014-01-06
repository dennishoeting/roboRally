package prototyp.server.model;

import java.io.Serializable;

/**
 * Die Spieloptionen für eine Spielrunde
 * 
 * @author Marcus, Jannik, Andreas
 * @version 1.0
 * 
 * @see {@link Round}
 */
public class RoundOption implements Serializable {

	/** Seriennummer */
	private static final long serialVersionUID = 2066044679284089766L;

	/** gibt an, ob die Presser während einer Spielrunde ständig aktiviert sind */
	private boolean alwaysPresserOn;

	/** gibt an, ob die Pusher während einer Spielrunde ständig aktiviert sind */
	private boolean alwaysPusherOn;

	/** gibt an, ob Programmierkarten der anderen gezeigt werden */
	private boolean alwaysShowProgrammingCardsOn;

	/** gibt an, ob Laser in der Spielrunde an oder ausgestellt */
	private boolean laserOn;

	/** die Anzahl der Spielerplätze einer Spielrunde */
	private int playerSlots;

	/** gibt an, ob Roboter nach jedem Spielzug schießen */
	private boolean robotShootsOn;

	/** gibt an, ob es einen Countdown beim Legen der Programmierkarten gibt */
	private boolean timeCountdownOn;

	/** die Anzahl der Beobachterplätze einer Spielrunde standardmäßig "5" */
	private int watcherSlots = 5;

	/** Der Spielname */
	private String gameName;

	/**
	 * Default-Konstruktor zum Erzeugen von Optionen
	 */
	public RoundOption() {
		this.laserOn = false;
		this.robotShootsOn = false;
		this.timeCountdownOn = false;
		this.alwaysPusherOn = false;
		this.alwaysPresserOn = false;
		this.alwaysShowProgrammingCardsOn = false;
		this.watcherSlots = 5;
		this.playerSlots = 0;
		this.gameName = "Neues Spiel";
	}

	/**
	 * Gibt den Namen des Spiels zurück
	 * 
	 * @return Spielnamen
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
	 * Gibt an, ob Presser während einer Spielrunde immer aktiviert sind.
	 * 
	 * @return true, wenn diese immer aktiviert sind
	 */
	public boolean isAlwaysPresserOn() {
		return this.alwaysPresserOn;
	}

	/**
	 * Gibt an, ob Pusher während einer Spielrunde immer aktiviert sind.
	 * 
	 * @return true, wenn diese immer aktiviert sind
	 */
	public boolean isAlwaysPusherOn() {
		return this.alwaysPusherOn;
	}

	/**
	 * Gibt an, ob die Programmierkarten des Anderen immer gezeigt werden.
	 * 
	 * @return true, wenn diese immer gezeigt werden.
	 */
	public boolean isAlwaysShowProgrammingCardsOn() {
		return this.alwaysShowProgrammingCardsOn;
	}

	/**
	 * Gibt an, ob Laser in einer Spielrunde an- oder ausgeschaltet sind.
	 * 
	 * @return true, wenn Laser angeschaltet sind
	 */
	public boolean isLaserOn() {
		return this.laserOn;
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
	 * Gibt an, ob die Countdown-Funktion beim Programmkartenlegen eingeschaltet
	 * ist.
	 * 
	 * @return true, wenn die Funktion aktiviert ist.
	 */
	public boolean isTimeCountDownOn() {
		return this.timeCountdownOn;
	}

	/**
	 * Legt fest, ob Presser in einer Spielrunde ständig aktiv sind.
	 * 
	 * @param alwaysPresserOn
	 *            true, wenn Presser ständig aktiv sein sollen
	 */
	public void setAlwaysPresserOn(boolean alwaysPresserOn) {
		this.alwaysPresserOn = alwaysPresserOn;
	}

	/**
	 * Legt fest, ob Pusher in einer Spielrunde ständig aktiv sind.
	 * 
	 * @param alwaysPusherOn
	 *            true, wenn Pusher ständig aktiv sein sollen
	 */
	public void setAlwaysPusherOn(boolean alwaysPusherOn) {
		this.alwaysPusherOn = alwaysPusherOn;
	}

	/**
	 * Legt fest, die Programmierkarten immer angezeigt werden.
	 * 
	 * @param alwaysShowProgrammingCardsOn
	 *            true, wenn Programmierkarten angezeigt werden sollen
	 */
	public void setAlwaysShowProgrammingCardsOn(
			boolean alwaysShowProgrammingCardsOn) {
		this.alwaysShowProgrammingCardsOn = alwaysShowProgrammingCardsOn;
	}

	/**
	 * Legt den Namen des Spiels fest.
	 * 
	 * @param gameName
	 *            vom GI eingegebener Name
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
	public void setLaserOn(boolean laserOn) {
		this.laserOn = laserOn;
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
	 * Legt fest, ob Roboter nach einem Spielschritt mit Lasern schießen.
	 * 
	 * @param robotShootsOn
	 *            true, wenn Roboter schießen sollen.
	 */
	public void setRobotShootsOn(boolean robotShootsOn) {
		this.robotShootsOn = robotShootsOn;
	}

	/**
	 * Legt fest, ob ein Countdown beim Legen der Programmierkarten aktiviert
	 * ist.
	 * 
	 * @param timeCountdownOn
	 *            true, wenn der Countdown aktiviert werden soll
	 */
	public void setTimeCountdownOn(boolean timeCountdownOn) {
		this.timeCountdownOn = timeCountdownOn;
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
}
