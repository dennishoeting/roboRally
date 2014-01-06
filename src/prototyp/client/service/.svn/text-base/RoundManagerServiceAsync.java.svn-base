package prototyp.client.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import prototyp.client.presenter.lobby.LobbyPagePresenter;
import prototyp.client.presenter.pregame.PreGamePagePresenter;
import prototyp.client.presenter.pregame.RefereePagePresenter;
import prototyp.server.model.RoundManager;
import prototyp.shared.exception.RoundNotThereException;
import prototyp.shared.programmingcard.Programmingcard;
import prototyp.shared.round.Player;
import prototyp.shared.round.RoundInfo;
import prototyp.shared.round.Watcher;
import prototyp.shared.useradministration.User;
import prototyp.shared.util.Color;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Die zugehörige Async-Klasse zu {@link RoundManagerService}.
 * 
 * @author Jannik, Andreas
 * @version 1.0
 */
public interface RoundManagerServiceAsync {

	/**
	 * Fügt einen Player dem PlayerManager hinzu. NICHT direkt aufrufen, sondern {@link RoundManager#addPlayer}!
	 * 
	 * @param newPlayer
	 *            der zu speichernde Player
	 * @return Index des Players wenn erfolgreich, sonst -1
	 * @author Jannik
	 * @version 1.0 (28.9.10)
	 */
	void addPlayer(Player newPlayer, int roundID, AsyncCallback<Boolean> callback);

	/**
	 * Benutzt vom {@link RefereePagePresenter}.
	 * 
	 * Speichert eine neue Runde und gibt den Index zurück (damit muss man später wieder auf die Runde zugreifen!). Wird außerdem
	 * ein RoundEvent für das Observer-Pattern der Round - Timo 22.09.10
	 * 
	 * @param newRound
	 *            Die Runde, die gespeichert werden soll.
	 * @return Index der gerade gespeicherten Round
	 */
	void addRound(RoundInfo newRound, AsyncCallback<Integer> callback);

	/**
	 * Fügt einen Watcher dem PlayerManager hinzu. NICHT direkt aufrufen, sondern {@link RoundManager#addWatcher}!
	 * 
	 * @param newWatcher
	 *            der zu speichernde Watcher
	 * @return Index des Watchers wenn erfolgreich, sonst -1
	 * @author Jannik
	 * @version 1.0 (28.9.10)
	 */
	void addWatcher(Watcher newWatcher, int roundID, AsyncCallback<Boolean> callback);

	/**
	 * Spieler kündigt seinen PowerDown an
	 * 
	 * @param roundId
	 *            die Id der entsprechenden Spielrunde
	 * @param player
	 *            die User-Id des entsprechenden Spielers
	 * @param powerDownState
	 *            1, wenn angekündigt; 2, wenn powerDown
	 * @param callback
	 */
	void announcePowerDown(int roundId, int playerId, int powerDownState, AsyncCallback<Boolean> callback);

	/**
	 * Wird aufgerufen, wenn ein Spieler eine Karte in den Kartenslot gelegt oder entfernt hat Dieses wird per Event den anderen
	 * Spielern in der Runde ersichtbar
	 * 
	 * @param roundId
	 *            die Id der entsprechenden Spielrunde
	 * @param player
	 *            die User-Id des entsprechenden Spielers
	 * @param slot
	 *            die Nummer des Kartenslots (0,..,4)
	 * @param set
	 *            0, wenn gesetzt und 1, wenn Karte entfernt wurde
	 * @param callback
	 */
	void cardSetInSlot(int roundId, int playerId, int slot, int set, AsyncCallback<Boolean> callback);

	/**
	 * Wird aufgerufen, wenn ein Spieler in einer Spierunde endgültig gestorben ist, um den Status eines Watchers anzunehmen
	 * 
	 * @param roundId
	 *            die Id der entsprechenden Spielrunde
	 * @param player
	 *            die User-Id des entsprechenden Spielers
	 * @param callback
	 */
	void changePlayerToWatcher(int roundId, int playerId, AsyncCallback<Boolean> callback);

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
	void getPlayers(int roundID, AsyncCallback<Map<Integer, Player>> callback);

	/**
	 * Liefert alle Player und Watcher in einer Map. (casten)
	 * 
	 * @author timo
	 * @param roundID
	 * @return Map<Integer, Watcher>
	 */
	void getPlayersAndWatchers(int roundID, AsyncCallback<Map<Integer, Watcher>> callback);

	/**
	 * Benutzt vom {@link PreGamePagePresenter}.
	 * 
	 * Gibt die RoundInfo zu einer Round zurück. Hier ist nicht das richtige PlayingBoard gespeichert!
	 * 
	 * @param roundID
	 *            Die ID der Round
	 * @return Die ID der übergebenen Round.
	 * @throws IllegalArgumentException
	 *             Wird geworfen, wenn die roundID ungültig ist.
	 */
	void getRound(int roundID, AsyncCallback<RoundInfo> callback);

	/**
	 * Benutzt vom {@link LobbyPagePresenter}.
	 * 
	 * Liefert eine HashMap mit RoundInfos. Der Key ist die RoundID
	 * 
	 * @return HashMap<Integer, RoundInfo>
	 */
	void getRounds(AsyncCallback<Map<Integer, RoundInfo>> callback);

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
	void getWatchers(int roundID, AsyncCallback<Map<Integer, Watcher>> callback);

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
	void receiveProgrammingCards(int roundId, int playerId, AsyncCallback<List<Programmingcard>> callback);

	/**
	 * Löscht einen User aus allen angegebenen Rounds. Außerdem wird die Round gelöscht, fall er GI war. Wird aufgerufen sobald
	 * der User die Seite verlässt.
	 * 
	 * @author timo
	 * @param rounds
	 * @param user
	 * @return true
	 */
	void removeFromAllRounds(List<Integer> rounds, User user, AsyncCallback<Boolean> callback);

	/**
	 * entfernt einen Spieler aus der laufenden Spielrunde
	 * 
	 * @param roundId
	 *            die Id der entsprechenden Spielrunde
	 * @param player
	 *            die User-Id des entsprechenden Spielers
	 * @param callback
	 */
	void removePlayer(int roundId, int player, AsyncCallback<Boolean> callback);

	/**
	 * Löscht den Spieler aus der Runde
	 * 
	 * @param player
	 *            Spieler
	 * @return true, falls der Spieler gelöscht wurde
	 */
	void removePlayer(Player player, int roundID, AsyncCallback<Boolean> Callback);

	/**
	 * Entfernt eines Spieler aus einer laufenden Runde. Diese Methode soll aufgerufen werden, wenn der Spieler die Runde nicht
	 * über den Beenden-Button verlässt sondern die Robocraftseite.
	 * 
	 * @param roundId
	 * @param playerId
	 * @param callback
	 */
	void removePlayerOnUnload(int roundId, int playerId, AsyncCallback<Boolean> callback);

	/**
	 * Löscht eine Runde aus dem RoundManager (wenn der Gameinitiator auf "Abbrechen" klickt)
	 * 
	 * @author Jannik, Timo
	 * @version 1.0
	 * @version 1.1 jetzt auch mit false Teil und Events -Timo
	 * @param roundID
	 *            die zu löschende Runde
	 * @return true wenn es gelöscht werden konnte; sonst false
	 */
	void removeRound(int roundID, AsyncCallback<Boolean> Callback);

	/**
	 * Löscht den Beobachter aus der Runde
	 * 
	 * @param watcher
	 *            Beobachter
	 * @return true, falls der Beobachter gelöscht wurde
	 */
	void removeWatcher(Watcher watcher, int roundID, AsyncCallback<Boolean> Callback);

	/**
	 * Trägt die Statistiken der Spieler nach Beendigung einer Spielrunde in die DB ein.
	 * 
	 * @param roundId
	 *            die entprechende Runde
	 * @param callback
	 */
	void saveStatisticsInDB(int roundId, int winnerId, Map<Integer, Integer> statistic, Map<Integer, Set<Integer>> awards,
			AsyncCallback<Boolean> callback);

	/**
	 * Wird vom Spieler aufgerufen, nachdem er seine Programmierkarten gewählt und auf bereit geklickt hat. Diese Methode liefert
	 * immer true, da er die Karten der anderen Spieler per Event zugeschickt bekommt, wenn diese alle ihre Karten an den Server
	 * gesendet haben.
	 * 
	 * @return immer true;
	 */
	void sendProgrammingcards(int roundId, int playerId, List<Programmingcard> cards, AsyncCallback<Boolean> callback);

	/**
	 * Wird vom Spieler nach Abarbeitung eines Spielschritts aufgerufen
	 * 
	 * @param roundId
	 *            die Id der entsprechenden Spielrunde
	 * @param player
	 *            die User-Id des entsprechenden Spielers
	 * @param userState
	 *            der Status, der der Spieler momentan hat. Gibt an ob der Spieler nun Roboter ist
	 * @param callback
	 */
	void sendRequestStepReady(int roundId, int playerId, int userState, AsyncCallback<Boolean> callback);

	/**
	 * Spieler sendet sein Restartfield an den Server
	 * 
	 * @param roundId
	 *            die Id der entsprechenden Spielrunde
	 * @param player
	 *            die User-Id des entsprechenden Spielers
	 * @param i
	 *            der i-Index des RestartFields
	 * @param j
	 *            der j-Index des RestartFields
	 * @param callback
	 */
	void sendRestartField(int roundId, int playerId, int i, int j, AsyncCallback<Boolean> callback);

	/**
	 * Setzt eine Farbe für einen Player
	 * 
	 * @author timo
	 * @version 1.0
	 */
	void setPlayerColor(Player player, int roundID, Color color, AsyncCallback<Boolean> callback);

	/**
	 * Setzt einen Spieler auf das Attribut flag. Wirft außerdem ein InternalRoundEvent
	 * 
	 * @param player
	 *            Player
	 * @param roundID
	 *            RundenID
	 * @param flag
	 *            Angabe, ob er bereit oder nicht ist
	 * @return true
	 * 
	 */
	void setPlayerReady(Player player, int roundID, boolean flak, AsyncCallback<Boolean> callback);

	/**
	 * Setzt alle Robots der Player, setzt eine Farbe für jeden Player (der noch keine gewählt hat)
	 */
	void setRobotPositionAndColor(int roundID, AsyncCallback<Boolean> callback);

	/**
	 * Startet den CountDown, bevor die Round los geht.
	 * 
	 * @param roundID
	 * @return true
	 */
	void startCountDown(int roundID, AsyncCallback<Boolean> callback);

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
	void abortFromRound(int roundId, int playerId, AsyncCallback<Boolean> callback);

	void removeWatcher(int watcher, int roundID, AsyncCallback<Boolean> callback);

}
