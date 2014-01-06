package prototyp.shared.round;

import java.io.Serializable;

import prototyp.shared.useradministration.User;

/**
 * Rundeninformationen, die per Event geschickt werden
 * 
 * @author Timo
 * @version 1.0
 */
public class RoundInfo implements Serializable {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -5412838643123282831L;

	private int roundId, freePlayerSlots, freeWatcherSlots;

	/** Ersteller der Round(Info) */
	private User gameInitiator;

	/** der Name des Spielbretts */
	private PlayingBoard playingboard;

	/** die Rundenoptionen */
	private RoundOption roundOption;

	/** Gibt an ob die Runde gestartet ist */
	private boolean isStarted;

	/**
	 * Default-Konstruktor für Serializable-Interface
	 */
	public RoundInfo() {

	}

	/**
	 * Konstruktor wenn die RoundID fest steht. Also wenn RoundInfo Serverseitig
	 * erstellt wird.
	 * 
	 * @param roundId
	 *            RoundID
	 * @param playingboardName
	 *            Spielbrettname
	 * @param maxPlayers
	 *            Maximale Anzahl der Spieler
	 */
	public RoundInfo(int roundId, User creator, PlayingBoard playingboard,
			RoundOption roundOption, int freePlayerSlots, int freeWatcherSlots, boolean isStarted) {
		this.roundId = roundId;
		this.gameInitiator = creator;
		this.playingboard = playingboard;
		this.roundOption = roundOption;
		this.freePlayerSlots = freePlayerSlots;
		this.freeWatcherSlots = freeWatcherSlots;
		this.isStarted = isStarted;
	}

	/**
	 * Konstruktor wenn eine neue Round erstellt werden soll. (RefereePage)
	 * 
	 * @param creator
	 *            Ersteller
	 * @param playingboard
	 *            Spielbrettname
	 * @param roundOption
	 *            Rundenoptionen
	 */
	public RoundInfo(User creator, PlayingBoard playingboard,
			RoundOption roundOption) {
		this.gameInitiator = creator;
		this.playingboard = playingboard;
		this.roundOption = roundOption;
	}

	/**
	 * 
	 * @return Freie Player Plätze
	 */
	public int getFreePlayerSlots() {
		return this.freePlayerSlots;
	}

	/**
	 * 
	 * @return Freie Watcher Plätze
	 */
	public int getFreeWatcherSlots() {
		return this.freeWatcherSlots;
	}

	/**
	 * @return the GI
	 */
	public User getGameInitiator() {
		return this.gameInitiator;
	}

	/**
	 * @return the playingboardName
	 */
	public PlayingBoard getPlayingboard() {
		return this.playingboard;
	}

	/**
	 * @return the roundId
	 */
	public int getRoundId() {
		return this.roundId;
	}

	/**
	 * @return the roundOption
	 */
	public RoundOption getRoundOption() {
		return this.roundOption;
	}

	/**
	 * 
	 * @return isStarted?
	 */
	public boolean isStarted() {
		return this.isStarted;
	}

	/**
	 * Setzt die FreePlayerSlots auf den Wert.
	 * 
	 * @param freePlayerSlots
	 * @return true
	 */
	public boolean setFreePlayerSlots(int freePlayerSlots) {
		this.freePlayerSlots = freePlayerSlots;
		return true;
	}

	/**
	 * Setzt die FreeWatcherSlots auf den Wert.
	 * 
	 * @param freeWatcherSlots
	 * @return true
	 */
	public boolean setFreeWatcherSlots(int freeWatcherSlots) {
		this.freeWatcherSlots = freeWatcherSlots;
		return true;
	}

	/**
	 * Setzt das Playingboard
	 * 
	 * @param playingBoard
	 * @return
	 */
	public boolean setPlayingBoard(PlayingBoard playingBoard) {
		this.playingboard = playingBoard;
		return true;
	}

	/**
	 * Setzt die RoundID
	 * 
	 * @param roundID
	 * @return
	 */
	public boolean setRoundID(int roundID) {
		this.roundId = roundID;
		return true;
	}

	/**
	 * Setzt die Runde auf gestartet.
	 * 
	 * @return immer true
	 */
	public boolean setStarted() {
		this.isStarted = true;
		return true;
	}
}
