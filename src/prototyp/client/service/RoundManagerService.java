package prototyp.client.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import prototyp.client.presenter.lobby.LobbyPagePresenter;
import prototyp.client.presenter.pregame.PreGamePagePresenter;
import prototyp.client.presenter.pregame.RefereePagePresenter;
import prototyp.server.model.RoundManager;
import prototyp.shared.exception.RoundNotThereException;
import prototyp.shared.exception.mapGenerator.CantLoadPlayingBoardException;
import prototyp.shared.exception.pregame.ColorAlreadyExistsException;
import prototyp.shared.exception.round.AlreadyInRoundException;
import prototyp.shared.exception.round.NoRoundsCreatedException;
import prototyp.shared.exception.round.RoundAlreadyExistsException;
import prototyp.shared.exception.round.RoundFullException;
import prototyp.shared.exception.round.UserNotInRoundException;
import prototyp.shared.programmingcard.Programmingcard;
import prototyp.shared.round.Player;
import prototyp.shared.round.RoundInfo;
import prototyp.shared.round.Watcher;
import prototyp.shared.useradministration.User;
import prototyp.shared.util.Color;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * RoundManager, clientseitige Sicht.
 * 
 * @author Andreas, Jannik, Timo
 * @version 1.0
 * @version 1.1 Farben können jetzt ausgewählt werden.
 * @version 1.2 jetzt keine integer Werte mehr für addPlayer/Watcher war eh
 *          sinnlos mit einem ArrayListindex! Außerdem getWatchers -Timo
 * 
 * @see Serverseite: {@link RoundManager}
 */
@RemoteServiceRelativePath("roundmanager")
public interface RoundManagerService extends RemoteService {

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
	public boolean addPlayer(Player newPlayer, int roundID)
			throws AlreadyInRoundException, RoundFullException,
			RoundNotThereException;

	/**
	 * Benutzt vom {@link RefereePagePresenter}.
	 * 
	 * Speichert eine neue Runde und gibt den Index zurück (damit muss man
	 * später wieder auf die Runde zugreifen!). Wird außerdem ein RoundEvent für
	 * das Observer-Pattern der Round - Timo 22.09.10
	 * 
	 * @param newRound
	 *            Die Runde, die gespeichert werden soll.
	 * @return Index der gerade gespeicherten Round
	 * @throws CantLoadPlayingBoardException 
	 */
	public int addRound(RoundInfo roundInfo) throws RoundAlreadyExistsException, CantLoadPlayingBoardException;

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
	public boolean addWatcher(Watcher newWatcher, int roundID)
			throws AlreadyInRoundException, RoundFullException,
			RoundNotThereException;

	/**
	 * kündigt den Powerdown eines Spielers an
	 * 
	 * @param roundId
	 * @param playerId
	 * @param powerDownState
	 * @return
	 */
	boolean announcePowerDown(int roundId, int playerId, int powerDownState);

	boolean cardSetInSlot(int roundId, int playerId, int slot, int set);

	/**
	 * Wird aufgerufen, um den Spieler in einen Watcher zu verwandeln, nachdem
	 * dieser endgültig gestorben ist.
	 * 
	 * @param roundId
	 * @param playerId
	 * @return
	 */
	boolean changePlayerToWatcher(int roundId, int playerId);

	/**
	 * Benutzt vom {@link PreGamePagePresenter}.
	 * 
	 * Liefert eine HashMap mit Playern. Als Key wird die UserID verwendet.
	 * 
	 * @return HashMap<Integer, Round>
	 * @param roundID
	 *            Die Round, deren Player erfragt werden sollen
	 * @author Jannik, Timo
	 * @version 1.0 (28.9.10)
	 * @version 1.1 Timo 02.11.10
	 */
	public Map<Integer, Player> getPlayers(int roundID)
			throws RoundNotThereException;

	/**
	 * Liefert alle Player und Watcher in einer Map. (casten)
	 * 
	 * @author timo
	 * @param roundID
	 * @return Map<Integer, Watcher>
	 */
	public Map<Integer, Watcher> getPlayersAndWatchers(int roundID)
			throws RoundNotThereException;

	/**
	 * Benutzt vom {@link PreGamePagePresenter}.
	 * 
	 * Gibt die RoundInfo zu einer Round zurück. Hier ist nicht das richtige
	 * PlayingBoard gespeichert!
	 * 
	 * @param roundID
	 *            Die ID der Round
	 * @return Die ID der übergebenen Round.
	 * @throws IllegalArgumentException
	 *             Wird geworfen, wenn die roundID ungültig ist.
	 */
	public RoundInfo getRound(int roundID) throws IllegalArgumentException,
			RoundNotThereException;

	/**
	 * Benutzt vom {@link LobbyPagePresenter}.
	 * 
	 * Liefert eine HashMap mit RoundInfos. Der Key ist die RoundID
	 * 
	 * @return HashMap<Integer, RoundInfo>
	 */
	public Map<Integer, RoundInfo> getRounds() throws NoRoundsCreatedException;

	/**
	 * Benutzt vom {@link PreGamePagePresenter}.
	 * 
	 * Liefert eine Map mit Watchern. Als Key wird die UserID verwendet.
	 * 
	 * @return HashMap<Integer, Round>
	 * @param roundID
	 *            Die Round, deren Player erfragt werden sollen
	 * @author Timo
	 * @version 1.0
	 */
	public Map<Integer, Watcher> getWatchers(int roundID)
			throws RoundNotThereException;

	/**
	 * Wird aufgerufen, um Programmierkarten per Event-Service anzufordern.
	 * 
	 * @param roundId
	 *            die Runde, die angesprochen werden soll
	 * @param playerId
	 *            der Player, der Karten anfordert
	 * @param callback
	 *            Callback, der einen nicht weiter benötigten boolean kapselt
	 */
	List<Programmingcard> receiveProgrammingCards(int roundId, int playerId);

	/**
	 * Löscht einen User aus allen angegebenen Rounds. Außerdem wird die Round
	 * gelöscht, fall er GI war. Wird aufgerufen sobald der User die Seite
	 * verlässt. -> Hier sind Exceptions egal.
	 * 
	 * @author timo
	 * @param rounds
	 * @param user
	 * @return
	 */
	public boolean removeFromAllRounds(List<Integer> rounds, User user);

	/**
	 * Löscht den Spieler aus der Runde
	 * 
	 * @param player
	 *            Spieler
	 * @return true, falls der Spieler gelöscht wurde
	 */
	boolean removePlayer(int roundId, int player) throws RoundNotThereException;

	/**
	 * Löscht den Spieler aus der Runde
	 * 
	 * @param player
	 *            Spieler
	 * @return true, falls der Spieler gelöscht wurde
	 */
	public boolean removePlayer(Player player, int roundID)
			throws RoundNotThereException;

	/**
	 * Spieler wird beim Verlassen der RoboCraftSeite aus allen Spielrunden
	 * geschmissen
	 * 
	 * @param roundId
	 * @param playerId
	 * @return
	 * @throws RoundNotThereException
	 */
	boolean removePlayerOnUnload(int roundId, int playerId)
			throws RoundNotThereException;

	/**
	 * Löscht eine Runde aus dem RoundManager (wenn der Gameinitiator auf
	 * "Abbrechen" klickt)
	 * 
	 * @author Jannik, Timo
	 * @version 1.0
	 * @version 1.1 jetzt auch mit false Teil und Events -Timo
	 * @param roundID
	 *            die zu löschende Runde
	 * @return true wenn es gelöscht werden konnte; sonst false
	 */
	public boolean removeRound(int roundID) throws RoundNotThereException,
			UserNotInRoundException;

	/**
	 * Löscht den Beobachter aus der Runde
	 * 
	 * @param watcher
	 *            Beobachter
	 * @return true, falls der Beobachter gelöscht wurde
	 */
	public boolean removeWatcher(Watcher watcher, int roundID)
			throws RoundNotThereException;

	/**
	 * Trägt die Statistiken der Spieler nach Beendigung einer Spielrunde in die
	 * DB ein.
	 * 
	 * @param roundId
	 * @return
	 */
	boolean saveStatisticsInDB(final int roundId, final int winnerId, final Map<Integer, Integer> statistic,
			final Map<Integer, Set<Integer>> awards);

	/**
	 * Wird vom Spieler aufgerufen, nachdem er seine Programmierkarten gewählt
	 * und auf bereit geklickt hat.
	 * 
	 * @return immer true;
	 */
	boolean sendProgrammingcards(int roundId, int playerId,
			List<Programmingcard> cards);

	/**
	 * wird aufgerufen, nachdem der Spieler mit der Abarbeitung eines
	 * Spielschritts fertig ist.
	 * 
	 * @param roundId
	 * @param playerId
	 * @param userState
	 * @return
	 */
	boolean sendRequestStepReady(int roundId, int playerId, int userState);

	/**
	 * Sendet das neue Restartfield eines Spielers an den Server
	 * 
	 * @param roundId
	 * @param playerId
	 * @param i
	 * @param j
	 * @return
	 */
	boolean sendRestartField(int roundId, int playerId, int i, int j);

	/**
	 * Setzt eine Farbe für einen Player
	 * 
	 * @author timo
	 * @version 1.0
	 */
	public boolean setPlayerColor(Player player, int roundID, Color color)
			throws UserNotInRoundException, RoundNotThereException,
			ColorAlreadyExistsException;

	/**
	 * @param player
	 *            Setzt einen Spieler auf das Attribut flag. Wirft außerdem ein
	 *            InternalRoundEvent
	 */
	public boolean setPlayerReady(Player player, int roundID, boolean flak)
			throws RoundNotThereException, UserNotInRoundException;

	/**
	 * Setzt alle Robots der Player, setzt eine Farbe für jeden Player (der noch
	 * keine gewählt hat)
	 */
	public boolean setRobotPositionAndColor(int roundID)
			throws RoundNotThereException, CantLoadPlayingBoardException;


	/**
	 * Startet den CountDown, bevor die Round los geht.
	 * 
	 * @param roundID
	 * @return true
	 */
	boolean startCountDown(int roundID);
	
	/**
	 * 
	 * @param roundId
	 *            Die ID der Spielrunde
	 * @param playerId
	 *            Die UserID des Spielers
	 * @return Ergebnis der Methode removePlayer()
	 * @throws RoundNotThereException
	 *             Wird geworfen, wenn die Runde nicht existiert
	 */
	public boolean abortFromRound(int roundId, int playerId) throws RoundNotThereException;

	boolean removeWatcher(int watcher, int roundID)
			throws RoundNotThereException;

}
