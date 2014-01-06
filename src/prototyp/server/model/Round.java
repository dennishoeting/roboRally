package prototyp.server.model;

import java.util.List;

import prototyp.shared.programmingcard.Programmingcard;
import prototyp.shared.round.Player;
import prototyp.shared.round.PlayingBoard;
import prototyp.shared.round.RoundOption;
import prototyp.shared.round.Watcher;
import prototyp.shared.useradministration.User;

/**
 * Verwaltet eine Spielrunde
 * 
 * @author Tim, Jannik, Andreas, Marcus
 * @version 1.1 (getter & setter, RoundOption, nach Shared verschoben,
 *          Konstruktor --Jannik)
 * @version 1.2 Konstruktor umgeschrieben(Marcus)
 * @version 1.3 ID hinzugefügt
 * @version 1.4 (3.10.86 - der schönste Tag der Welt :) ) RobotManger wird
 *          erzeugt(Marcus)
 * 
 */
public class Round {
	

	/** Seriennummer */
	private static final long serialVersionUID = -825066489919886201L;

	/** die Runden-ID intern */
	private int id;

	/** die Runden-ID für DB */
	private int dbID;
	
	/**
	 * Ob die Runde noch auf Mitspieler wartet (man also joinen kann) oder
	 * bereits gespielt wird (man also nur noch Beobachter sein kann)
	 */
	private boolean isStarted;

	/** Informationen über Spieler etc. in der Spielrunde */
	private PlayerManager playerManager;

	/** Informationen über das PlayingBoard der Spielrunde */
	private PlayingBoard playingBoard;

	/** zuständig für die Programmierkarten */
	private ProgrammingCardManager programmingCardManager;

	/** Ein Option-Object */
	private RoundOption roundOption;

	/** Statistiken etc. in der Spielrunde */
	private StatisticsManager statisticsManager;

	/** Angabe, ob zur Zeit die ProgrammingPhase läuft */
	private boolean isInProgrammingPhase = true;
	
	/** gibt an, ob die Statistiken geschrieben wurden */
	private boolean isStatisticSaved = false;


	/**
	 * Konstruktor
	 * 
	 * @param gameinitiator
	 *            Informationen über das PlayingBoard der Spielrunde
	 * @param playingBoard
	 *            Informationen über Spieler etc. in der Spielrunde
	 * @param roundOption
	 *            Ein Option-Object
	 */
	public Round(User gameinitiator, PlayingBoard playingBoard,
			RoundOption roundOption) {
		Player player = new Player(gameinitiator);
		player.setReady(true);
		this.playingBoard = playingBoard;
		this.playerManager = new PlayerManager(player, this);
		this.statisticsManager = new StatisticsManager();
		this.programmingCardManager = new ProgrammingCardManager();
		this.roundOption = roundOption;
		this.isStarted = false;
		this.dbID = 0;
	}

	/**
	 * Fügt einen Player dem PlayerManager hinzu. NICHT direkt aufrufen, sondern
	 * {@link RoundManager#addPlayer}!
	 * 
	 * @param newPlayer
	 *            der zu speichernde Player
	 * @return Index des Players wenn erfolgreich, sonst -1
	 * @author Jannik
	 * @version 1.0 (28.9.10)
	 */
	public boolean addPlayer(Player newPlayer) {
		return this.playerManager.addToPlayers(newPlayer);
	}

	/**
	 * Fügt einen Watcher dem PlayerManager hinzu. NICHT direkt aufrufen,
	 * sondern {@link RoundManager#addWatcher}!
	 * 
	 * @param newWatcher
	 *            der zu speichernde Watcher
	 * @return Index des Watchers wenn erfolgreich, sonst -1
	 * @author Jannik
	 * @version 1.0 (28.9.10)
	 */
	public boolean addWatcher(Watcher newWatcher) {
		return this.playerManager.addToWatchers(newWatcher);

	}

	/**
	 * Diese Methode zählt die freien Plätze für Spieler in der Spielrunde
	 * 
	 * @return Anzahl der freien Spielerplätze
	 */
	public int getFreePlayerSlots() {
		return this.roundOption.getPlayersSlots()
				- this.playerManager.getNumberOfPlayers();
	}

	/**
	 * Diese Methode zählt die freien Plätze für Beobachter in der Spielrunde
	 * 
	 * @return Anzahl der freien Beobachterplätze
	 */
	public int getFreeWatcherSlots() {
		return this.roundOption.getWatchersSlots()
				- this.playerManager.getNumberOfWatchers();
	}

	/**
	 * 
	 * @return the GameInitiator
	 */
	public User getGameInitiator() {
		return this.playerManager.getGameInitiator().getUser();
	}

	/**
	 * Liefert die ID der Runde
	 * 
	 * @return die ID der Runde
	 */
	public int getID() {
		return this.id;
	}



	/**
	 * @return the playerManager
	 */
	public PlayerManager getPlayerManager() {
		return this.playerManager;
	}

	/**
	 * @return the playingBoard
	 */
	public PlayingBoard getPlayingBoard() {
		return this.playingBoard;
	}

	/**
	 * Liefert den ProgrammingCardManager
	 * 
	 * @return programmingCardManager
	 */
	public ProgrammingCardManager getProgrammingCardManager() {
		return this.programmingCardManager;
	}

	/**
	 * @return the roundOption
	 */
	public RoundOption getRoundOption() {
		return this.roundOption;
	}

	/**
	 * @return the statisticsManager
	 */
	public StatisticsManager getStatisticsManager() {
		return this.statisticsManager;
	}


	/**
	 * Liefert die Angabe, ob zur Zeit die Programmierungsphase ist
	 * 
	 * @return isInProgrammingPhase
	 */
	public boolean isInProgrammingPhase() {
		return this.isInProgrammingPhase;
	}

	/**
	 * @return the isStarted
	 */
	public boolean isStarted() {
		return this.isStarted;
	}

	/**
	 * Löscht den Spieler aus der Runde
	 * 
	 * @param player
	 *            Spieler
	 * @return true, falls der Spieler gelöscht wurde
	 */
	public boolean removePlayer(Player player) {
		return this.playerManager.removeFromPlayers(player);
	}

	/**
	 * Löscht den Beobachter aus der Runde
	 * 
	 * @param watcher
	 *            Beobachter
	 * @return true, falls der Beobachter gelöscht wurde
	 */
	public boolean removeWatcher(Watcher watcher) {
		return this.playerManager.removeFromWatchers(watcher);
	}

	/**
	 * Setzt den Status eines Spielers in einer Runde auf "karten gesetzt";
	 * 
	 * @param player
	 * @return
	 */
	public boolean setCards(int playerId, List<Programmingcard> cards) {
		return this.playerManager.setCards(playerId, cards);
	}


	/**
	 * Setzt die ID der Runde
	 * 
	 * @param roundID
	 *            RoundID im RoundManager
	 */
	public void setId(int roundID) {
		this.id = roundID;
	}

	/**
	 * Setzt die Angabe zur Programmierungsphase
	 * 
	 * @param isInProgrammingPhase
	 */
	public void setInProgrammingPhase(boolean isInProgrammingPhase) {
		this.isInProgrammingPhase = isInProgrammingPhase;
	}

	/**
	 * @param playerManager
	 *            the playerManager to set
	 */
	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	/**
	 * @param playingBoard
	 *            the playingBoard to set
	 */
	public void setPlayingBoard(PlayingBoard playingBoard) {
		this.playingBoard = playingBoard;
	}

	/**
	 * @param roundOption
	 *            the roundOption to set
	 */
	public void setRoundOption(RoundOption roundOption) {
		this.roundOption = roundOption;
	}

	/**
	 * @param isStarted
	 *            the isStarted to set
	 */
	public void setStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}

	/**
	 * @param statisticsManager
	 *            the statisticsManager to set
	 */
	public void setStatisticsManager(StatisticsManager statisticsManager) {
		this.statisticsManager = statisticsManager;
	}

	public boolean isStatisticSaved() {
		return isStatisticSaved;
	}

	public void setStatisticSaved(boolean isStatisticSaved) {
		this.isStatisticSaved = isStatisticSaved;
	}
	
	/**
	 * Setzt die Datenbank ID
	 * 
	 * @param dbID die Datenbank ID
	 */
	public void setdbID(Integer dbID){
		this.dbID = dbID;
	}
	
	/**
	 * Liefert die Datenbank ID
	 * 
	 * @return die dbID der Round
	 */
	public Integer getdbID(){
		return this.dbID;
	}
	
	
	
}
