package prototyp.server.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import prototyp.client.presenter.lobby.LobbyPagePresenter;
import prototyp.client.presenter.pregame.PreGamePagePresenter;
import prototyp.client.presenter.pregame.PreGamePresenter;
import prototyp.client.presenter.pregame.RefereePagePresenter;
import prototyp.client.service.RoundManagerService;
import prototyp.server.lib.DBConnection;
import prototyp.server.lib.DBStatements;
import prototyp.server.util.ChatServiceImpl;
import prototyp.shared.exception.RoundNotThereException;
import prototyp.shared.exception.mapGenerator.CantLoadPlayingBoardException;
import prototyp.shared.exception.pregame.ColorAlreadyExistsException;
import prototyp.shared.exception.round.AlreadyInRoundException;
import prototyp.shared.exception.round.NoRoundsCreatedException;
import prototyp.shared.exception.round.RoundAlreadyExistsException;
import prototyp.shared.exception.round.RoundFullException;
import prototyp.shared.exception.round.UserNotInRoundException;
import prototyp.shared.field.StartField;
import prototyp.shared.programmingcard.Programmingcard;
import prototyp.shared.round.Player;
import prototyp.shared.round.PlayingBoard;
import prototyp.shared.round.RoundInfo;
import prototyp.shared.round.RoundNeedsWrapper;
import prototyp.shared.round.Watcher;
import prototyp.shared.useradministration.User;
import prototyp.shared.util.Color;
import prototyp.shared.util.Position;
import prototyp.shared.util.events.lobby.ChangeFreePlayerWatcherSlots;
import prototyp.shared.util.events.lobby.NewRoundEvent;
import prototyp.shared.util.events.lobby.RemoveRoundEvent;
import prototyp.shared.util.events.lobby.RoundStartedEvent;
import prototyp.shared.util.events.round.BackupFieldEvent;
import prototyp.shared.util.events.round.CardsSendEvent;
import prototyp.shared.util.events.round.ChangePlayerColorEvent;
import prototyp.shared.util.events.round.CountdownDecrementEvent;
import prototyp.shared.util.events.round.NewPlayerWatcherRoundEvent;
import prototyp.shared.util.events.round.OnePlayerLeftEvent;
import prototyp.shared.util.events.round.PlayerCardsSetEvent;
import prototyp.shared.util.events.round.PlayerLeftEvent;
import prototyp.shared.util.events.round.PlayerOneCardSetEvent;
import prototyp.shared.util.events.round.PowerDownEvent;
import prototyp.shared.util.events.round.RemovePlayerWatcherRoundEvent;
import prototyp.shared.util.events.round.RoundCancelledEvent;
import prototyp.shared.util.events.round.SetPlayerReadyEvent;
import prototyp.shared.util.events.round.StartRoundEvent;
import prototyp.shared.util.events.round.StepReadyEvent;
import prototyp.shared.util.events.round.WatcherNotificationNoWinnerEvent;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.service.RemoteEventServiceServlet;

/**
 * RoundManager.
 * 
 * Diese Klasse verwaltet alle gestarteten sowie zur Zeit gespielten {@link Round} -Objekte.
 * 
 * @author Jannik, Andreas, Timo, Marcus
 * @version 1.2 (20.9.10)
 * @version 1.3 (22.09.10) Observer-Pattern für die Rounds jetzt extrends RemoteEventServiceServlet. Bei addRound(Round)
 *          wird jetzt ein RoundEvent geworfen
 * @version 1.4 Bei Veränderungen wird jetzt immer die Round im Event übergeben! -Timo 26.10.10
 */
public class RoundManager extends RemoteEventServiceServlet implements RoundManagerService {

	/** Domainname für das Observer-Pattern der Round */
	static final Domain DOMAIN = DomainFactory.getDomain("lobby-domain");

	/** Seriennummer */
	private static final long serialVersionUID = 5479033854096581034L;

	/**
	 * Liefert alle Rounds. Richtige Rounds, keine RoundInfos
	 */
	public static Map<Integer, Round> getRealRounds() {
		return RoundManager.rounds;
	}

	/** RundenID, bevor ein Spiel gestartet wird */
	private int roundId = 0;

	/**
	 * Speichert alle aktuellen Runden Muss hier Definiert werden, damit wir keine Statischen Methoden brauchen - Timo
	 * */
	private static Map<Integer, Round> rounds = new HashMap<Integer, Round>();

	/**
	 * Default-Konstruktor
	 */
	public RoundManager() {
		// Nichts
	}

	/**
	 * Hier werden Spieler hinzugefügt. Wirft Events für Lobby und PreGame
	 * 
	 * @param newPlayer
	 *            der zu speichernde Player
	 * @param roundID
	 *            die Round, in der der Player gespeichert werden soll
	 * @return true alls es ging; sonst false.
	 * @see {@link Round#addPlayer(Player)} und {@link PlayerManager#addToPlayers(Player)}
	 */
	@Override
	public boolean addPlayer(Player newPlayer, int roundID) throws AlreadyInRoundException, RoundFullException,
			RoundNotThereException {

		// Falls es die Round nicht gibt -> Exception
		if (RoundManager.rounds.get(roundID) == null) {
			throw new RoundNotThereException();
		}

		// Falls doch schon voll ist (also die Lobby nicht rechtzeitig das Event
		// bekommen hat)
		// Um ganz sicher zugehen <= abfragen :D
		if (RoundManager.rounds.get(roundID).getFreePlayerSlots() <= 0) {
			throw new RoundFullException();
		}

		// Falls er als Player/Watcher schon existiert dann Exception werfen
		if (RoundManager.rounds.get(roundID).getPlayerManager().getPlayers().containsKey(newPlayer.getUser().getId())
				|| RoundManager.rounds.get(roundID).getPlayerManager().getWatchers()
						.containsKey(newPlayer.getUser().getId())) {
			throw new AlreadyInRoundException();
		}

		// Adden und abfragen ob es geklappt hat
		if (RoundManager.rounds.get(roundID).getPlayerManager().addToPlayers(newPlayer)) {

			// Lobbyevent
			addEvent(RoundManager.DOMAIN, new ChangeFreePlayerWatcherSlots(roundID, getRound(roundID)
					.getFreePlayerSlots(), getRound(roundID).getFreeWatcherSlots()));

			// RoundEvent
			addEvent(DomainFactory.getDomain("round_" + roundID), new NewPlayerWatcherRoundEvent(newPlayer));
			return true;
		}

		throw new AlreadyInRoundException();
	}

	/**
	 * Benutzt vom {@link RefereePagePresenter}.
	 * 
	 * Speichert eine neue Runde und gibt den Index zurück (damit muss man später wieder auf die Runde zugreifen!). Wird
	 * außerdem ein RoundEvent für das Observer-Pattern der Round - Timo 22.09.10
	 * 
	 * @param newRound
	 *            Die Runde, die gespeichert werden soll.
	 * @return Index der gerade gespeicherten Round
	 * @throws CantLoadPlayingBoardException
	 */
	@Override
	public synchronized int addRound(RoundInfo newRoundInfo) throws RoundAlreadyExistsException,
			CantLoadPlayingBoardException {

		// Prüfen ob der Roundname schon vergeben ist
		for (final Integer key : RoundManager.rounds.keySet()) {
			if (RoundManager.rounds.get(key).getRoundOption().getGameName()
					.equals(newRoundInfo.getRoundOption().getGameName())) {
				throw new RoundAlreadyExistsException();
			}
		}

		/*
		 * Spielbrett laden
		 */
		final PlayingBoard playingBoard = loadPlayingBoard(newRoundInfo.getPlayingboard().getName());

		// Runde erstellen
		final Round newRound = new Round(newRoundInfo.getGameInitiator(), newRoundInfo.getPlayingboard(),
				newRoundInfo.getRoundOption());

		// Runden ID setzen
		newRound.setId(this.roundId);

		// Runde hinzufügen
		RoundManager.rounds.put(this.roundId, newRound);

		/*
		 * Event für die Lobby auslösen. Dazu RoundID setzen. Hier ist nicht das richtige PlayingBoard enthalten.
		 */
		newRoundInfo.setRoundID(this.roundId);

		/*
		 * Richtiges PlayingBoard laden und zur Runde hinzufügen
		 */
		newRound.setPlayingBoard(playingBoard);

		/*
		 * Lobbyevent werfen, um die Runde in der Lobby anzuzeigen
		 */
		addEvent(RoundManager.DOMAIN, new NewRoundEvent(newRoundInfo));

		// RoundID inkrementieren für die nächste Round
		return this.roundId++;
	}

	/**
	 * Hier werden Watcher hinzugefügt. Dies wird weitergegeben an die Round und von dort an den PlayerManager.
	 * Anmerkung: Hier wird kein PlayerListEvent ausgelöst, das wird im {@link PreGamePresenter} selbst gemacht, da es
	 * auch jedes mal aufgerufen werden muss, wenn sich Spielereigenschaften ändern.
	 * 
	 * @param newWatcher
	 *            der zu speichernde Watcher
	 * @param roundID
	 *            die Round, in der der Watcher gespeichert werden soll
	 * @return true wenn es geklappt hat; sonst false
	 * @see {@link Round#addWatcher(Watcher)} und {@link PlayerManager#addToWatchers(Watcher)}
	 */
	@Override
	public boolean addWatcher(Watcher newWatcher, int roundID) throws AlreadyInRoundException, RoundFullException,
			RoundNotThereException {

		// Falls die Round nicht da ist ->Exception
		if (RoundManager.rounds.get(roundID) == null) {
			throw new RoundNotThereException();
		}

		// Falls doch schon voll ist
		if (RoundManager.rounds.get(roundID).getFreeWatcherSlots() <= 0) {
			throw new RoundFullException();
		}

		// Falls er bereits Player/Watcher ist -> Exception
		if (RoundManager.rounds.get(roundID).getPlayerManager().getPlayers().containsKey(newWatcher.getUser().getId())
				|| RoundManager.rounds.get(roundID).getPlayerManager().getWatchers()
						.containsKey(newWatcher.getUser().getId())) {
			throw new AlreadyInRoundException();
		}

		// Adden und abfragen ob es geklappt hat
		if (RoundManager.rounds.get(roundID).getPlayerManager().addToWatchers(newWatcher)) {

			// LobbyEvent
			addEvent(RoundManager.DOMAIN, new ChangeFreePlayerWatcherSlots(roundID, getRound(roundID)
					.getFreePlayerSlots(), getRound(roundID).getFreeWatcherSlots()));

			// RoundEvent
			addEvent(DomainFactory.getDomain("round_" + roundID), new NewPlayerWatcherRoundEvent(newWatcher));
			return true;
		}

		throw new AlreadyInRoundException();
	}

	/**
	 * Spieler kündigt seinen PowerDown an
	 * 
	 * @param roundId
	 *            die Id der entsprechenden Spielrunde
	 * @param player
	 *            die User-Id des entsprechenden Spielers
	 * @param powerDownState
	 *            1, wenn angekündigt; 2, wenn powerDown
	 */
	@Override
	public boolean announcePowerDown(int roundId, int playerId, int powerDownState) {

		/*
		 * PowerDown Event senden
		 */
		addEvent(DomainFactory.getDomain("round_" + roundId), new PowerDownEvent(playerId, powerDownState));

		return false;
	}

	/**
	 * Wird aufgerufen, wenn ein Spieler eine Karte in den Kartenslot gelegt oder entfernt hat Dieses wird per Event den
	 * anderen Spielern in der Runde ersichtbar
	 * 
	 * @param roundId
	 *            die Id der entsprechenden Spielrunde
	 * @param player
	 *            die User-Id des entsprechenden Spielers
	 * @param slot
	 *            die Nummer des Kartenslots (0,..,4)
	 * @param set
	 *            0, wenn gesetzt und 1, wenn Karte entfernt wurde
	 */
	@Override
	public boolean cardSetInSlot(int roundId, int playerId, int slot, int set) {

		/*
		 * Event senden, das eine Karte gelegt oder wieder entfernt wurde
		 */
		addEvent(DomainFactory.getDomain("round_" + roundId), new PlayerOneCardSetEvent(playerId, slot, set));

		return true;
	}

	/**
	 * Wird aufgerufen, wenn ein Spieler in einer Spierunde endgültig gestorben ist, um den Status eines Watchers
	 * anzunehmen
	 * 
	 * @param roundId
	 *            die Id der entsprechenden Spielrunde
	 * @param player
	 *            die User-Id des entsprechenden Spielers
	 */
	@Override
	public boolean changePlayerToWatcher(int roundId, int playerId) {

		/*
		 * entsprechende Runde ermitteln
		 */
		final Round round = RoundManager.rounds.get(roundId);

		/*
		 * Rundenobjekt sperren um ungültige Listen zu vermeiden
		 */
		synchronized (round) {

			/*
			 * Spieler aus den Spielern entfernen und zu den Beobachtern hinzufügen
			 */
			round.getPlayerManager().getWatchers()
					.put(playerId, round.getPlayerManager().getPlayers().remove(playerId));

			/*
			 * LobbyEvent, um die Anzahl der Spieler und Beobachter im Listgrid zu anzuzeigen
			 */
			addEvent(RoundManager.DOMAIN,
					new ChangeFreePlayerWatcherSlots(roundId, round.getFreePlayerSlots(), round.getFreeWatcherSlots()));

			return true;
		}
	}

	/**
	 * Dateinamen erstellen (ersetzt alles komische durch "_" )
	 * 
	 * @param text
	 *            String, der überprüft werden soll
	 * @return neuer Sring
	 */
	private String createFileName(String text) {
		Pattern pattern = Pattern.compile("([^a-zA-Z0-9.-_])");
		Matcher matcher = pattern.matcher(text);
		return matcher.replaceAll("_");
	}

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
	@Override
	public Map<Integer, Player> getPlayers(int roundID) throws RoundNotThereException {
		if (RoundManager.rounds.get(roundID) == null) {
			throw new RoundNotThereException();
		}
		return RoundManager.rounds.get(roundID).getPlayerManager().getPlayers();
	}

	/**
	 * Liefert alle Player und Watcher in einer Map. (casten)
	 * 
	 * @author timo
	 * @param roundID
	 * @return Map<Integer, Watcher>
	 */
	@Override
	public Map<Integer, Watcher> getPlayersAndWatchers(int roundID) throws RoundNotThereException {

		if (RoundManager.rounds.get(roundID) == null) {
			throw new RoundNotThereException();
		}

		// Tmp Map erstellen
		Map<Integer, Watcher> tmpMap = new HashMap<Integer, Watcher>();

		// Alle Player adden
		tmpMap.putAll(RoundManager.rounds.get(roundID).getPlayerManager().getPlayers());

		// Alle Watcher adden
		tmpMap.putAll(RoundManager.rounds.get(roundID).getPlayerManager().getWatchers());

		return tmpMap;
	}

	/**
	 * Liefert Playingboard Name und Path, aber nicht RealPath
	 * 
	 * @param playingBoardName
	 * @return
	 */
	private String getPlayingBoardFileNameAndPath(String playingBoardName) {
		return "/playingboard/" + playingBoardName.toLowerCase().replaceAll(" ", "") + ".plb";
	}

	/**
	 * Liefert den (möglichen) PlayingBoard Datei Namen und seinen RealPath. Geht nur wenn man dies als Servlet aufruft
	 * 
	 * @param playingBoardName
	 * @return
	 */
	private String getPlayingBoardFileNameAndRealPath(String playingBoardName) {
		return getServletContext().getRealPath("/") + getPlayingBoardFileNameAndPath(playingBoardName);
	}

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
	@Override
	public synchronized RoundInfo getRound(int roundID) throws IllegalArgumentException, RoundNotThereException {

		if (RoundManager.rounds.get(roundID) == null) {
			throw new RoundNotThereException();
		}

		if (roundID < 0 || roundID >= this.roundId) {
			throw new IllegalArgumentException();
		}

		final Round round = RoundManager.rounds.get(roundID);

		// Playingboard nur mit Infos erzeugen (ohne Felder)
		PlayingBoard tmpPlayingBoard = new PlayingBoard();
		tmpPlayingBoard.setName(round.getPlayingBoard().getName());
		tmpPlayingBoard.setDescription(round.getPlayingBoard().getDescription());
		tmpPlayingBoard.setDifficulty(round.getPlayingBoard().getDifficulty());
		tmpPlayingBoard.setHeight(round.getPlayingBoard().getHeight());
		tmpPlayingBoard.setWidth(round.getPlayingBoard().getWidth());
		tmpPlayingBoard.setID(round.getPlayingBoard().getID());
		tmpPlayingBoard.setImageFileName(round.getPlayingBoard().getImageFileName());
		tmpPlayingBoard.setMaxPlayers(round.getPlayingBoard().getMaxPlayers());
		tmpPlayingBoard.setNumberOfCheckpoints(round.getPlayingBoard().getNumberOfCheckpoints());
		tmpPlayingBoard.setNickname(round.getPlayingBoard().getNickname());
		tmpPlayingBoard.setUserID(round.getPlayingBoard().getUserID());

		// RoundInfo erzeugen
		RoundInfo tmpRoundInfo = new RoundInfo(roundID, round.getGameInitiator(), tmpPlayingBoard,
				round.getRoundOption(), round.getFreePlayerSlots(), round.getFreeWatcherSlots(), round.isStarted());

		return tmpRoundInfo;
	}

	/**
	 * Benutzt vom {@link LobbyPagePresenter}.
	 * 
	 * Liefert eine HashMap mit RoundInfos. Der Key ist die RoundID
	 * 
	 * @return HashMap<Integer, RoundInfo>
	 */
	@Override
	public synchronized Map<Integer, RoundInfo> getRounds() throws NoRoundsCreatedException {

		// Falls es keine Rounds gibt
		if (RoundManager.rounds.isEmpty()) {
			throw new NoRoundsCreatedException();
		}

		// Aus den Rounds eine Map mit RoundInfos machen
		final Map<Integer, RoundInfo> tmpRoundInfoMap = new HashMap<Integer, RoundInfo>();
		for (final Round round : RoundManager.rounds.values()) {

			// Playingboard nur mit Infos erzeugen (ohne Felder)
			PlayingBoard tmpPlayingBoard = new PlayingBoard();
			tmpPlayingBoard.setName(round.getPlayingBoard().getName());
			tmpPlayingBoard.setDescription(round.getPlayingBoard().getDescription());
			tmpPlayingBoard.setDifficulty(round.getPlayingBoard().getDifficulty());
			tmpPlayingBoard.setHeight(round.getPlayingBoard().getHeight());
			tmpPlayingBoard.setWidth(round.getPlayingBoard().getWidth());
			tmpPlayingBoard.setID(round.getPlayingBoard().getID());
			tmpPlayingBoard.setImageFileName(round.getPlayingBoard().getImageFileName());
			tmpPlayingBoard.setMaxPlayers(round.getPlayingBoard().getMaxPlayers());
			tmpPlayingBoard.setNumberOfCheckpoints(round.getPlayingBoard().getNumberOfCheckpoints());
			tmpPlayingBoard.setNickname(round.getPlayingBoard().getNickname());
			tmpPlayingBoard.setUserID(round.getPlayingBoard().getUserID());

			final RoundInfo tmpRoundInfo = new RoundInfo(round.getID(), round.getGameInitiator(), tmpPlayingBoard,
					round.getRoundOption(), round.getFreePlayerSlots(), round.getFreeWatcherSlots(), round.isStarted());

			// RoundInfo hinzufügen
			tmpRoundInfoMap.put(round.getID(), tmpRoundInfo);
		}

		return tmpRoundInfoMap;
	}

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
	@Override
	public Map<Integer, Watcher> getWatchers(int roundID) throws RoundNotThereException {
		if (RoundManager.rounds.get(roundID) == null) {
			throw new RoundNotThereException();
		}
		return RoundManager.rounds.get(roundID).getPlayerManager().getWatchers();
	}

	/**
	 * Lädt ein Playingboard anhand seines Namen aus der jeweiligen Datei Ist auch in MapGeneratorImpl aber würde mit
	 * einem Aufruf über MapGeneratorImpl in Jetty nicht Funktionieren (im Dev. Mode hingegen schon)
	 * 
	 * @author timo
	 * @param playingBoardName
	 *            Spielbrett-Name
	 * @return PlayingBoard
	 */
	private PlayingBoard loadPlayingBoard(String playingBoardName) throws CantLoadPlayingBoardException {

		ResultSet stmtResult = null;
		PlayingBoard tmpPlayingBoard = null;
		ObjectInputStream o = null;

		playingBoardName = createFileName(playingBoardName.toLowerCase());

		try {
			if (!new File(getPlayingBoardFileNameAndRealPath(playingBoardName)).exists()) {
				throw new CantLoadPlayingBoardException();
			}

			o = new ObjectInputStream(getServletContext().getResourceAsStream(
					getPlayingBoardFileNameAndPath(playingBoardName)));
			tmpPlayingBoard = (PlayingBoard) o.readObject();

			// Nickname aus der DB holen:

			// PreparedStatement vorbereiten
			PreparedStatement playingBoardsStmt = DBConnection.getPstmt(DBStatements.SELECT_PLAYINGBOARD.getStmt());

			// Variablen binden
			playingBoardsStmt.setString(1, playingBoardName);

			// Statement ausführen
			stmtResult = playingBoardsStmt.executeQuery();

			while (stmtResult.next()) {
				tmpPlayingBoard.setNickname(stmtResult.getString("nickname"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (o != null) {
					o.close();
				}
				if (stmtResult != null) {
					stmtResult.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return tmpPlayingBoard;
	}

	/**
	 * verteilt Programmierkarten an alle Player und Watcher einer Runde.
	 * 
	 * @param roundId
	 *            RoundID
	 * @param playerId
	 *            SpielerID
	 * 
	 * @return List<Programmingcard>
	 */
	@Override
	public List<Programmingcard> receiveProgrammingCards(int roundId, int playerId) {

		/*
		 * Runde ermitteln
		 */
		final Round round = RoundManager.rounds.get(roundId);

		/*
		 * Rundenobjekt sperren um inkonsistente Datenzustände zu vermeiden
		 */
		synchronized (round) {
			if (round.getPlayerManager().addPlayerReceived(playerId)) {
				/*
				 * Alle Spieler haben Programmierkarten erhalten, deshalb befindet sich die Runde nun in der
				 * Programmierphase
				 */
				round.setInProgrammingPhase(true);
			}

			/*
			 * Liefern der Programmierkarten
			 */
			return round.getProgrammingCardManager().getCards();
		}

	}

	/**
	 * Löscht einen User aus allen angegebenen Rounds. Außerdem wird die Round gelöscht, fall er GI war. Wird aufgerufen
	 * sobald der User die Seite verlässt.
	 * 
	 * @param rounds
	 *            Liste der Runden
	 * @param user
	 *            User-Objekt, was aus den Runden gelöscht werden soll
	 * @return true
	 */
	@Override
	public boolean removeFromAllRounds(List<Integer> rounds, User user) {
		// Liste durchlaufen
		for (final Integer roundID : rounds) {
			final Round round = RoundManager.rounds.get(roundID);
			try {
				if (!round.isStarted()) {
					// Falls GI dann Round löschen
					if (RoundManager.rounds.get(roundID).getGameInitiator().equals(user)) {
						removeRound(roundID);
					} else { // Sonst Player/Watcher
						if (removePlayer(new Player(user), roundID)) {
							// Nichts
						} else {
							removeWatcher(new Player(user), roundID);
						}
					}
				}
			} catch (Exception e) {
				// Nichts, weil der User den Browser geschlossen hat.
			}
		}
		return true;
	}

	/**
	 * 
	 * @param roundId
	 *            Die ID der Spielrunde
	 * @param userID
	 *            Die UserID des Spielers
	 * @return Ergebnis der Methode removePlayer()
	 * @throws RoundNotThereException
	 *             Wird geworfen, wenn die Runde nicht existiert
	 */
	public boolean abortFromRound(final int roundId, final int playerId) throws RoundNotThereException {
		/*
		 * Statistic des Spielers wird um ein abgebrochenes Spiel erhöht.
		 */
		this.incrementAbortedRounds(playerId);

		return removePlayer(roundId, playerId);
	}

	/**
	 * Erhöht die Anzahl der abgebrochenen Runden eines Spielers um 1
	 * 
	 * @param playerId
	 *            die UserId des Spielers, der sie Spielrunde abgebrochen hat
	 */
	public void incrementAbortedRounds(final int playerId) {
		/*
		 * Statistic des Spielers wird um ein abgebrochenes Spiel erhöht.
		 */
		PreparedStatement pstmtIncAbortedRouns = null;
		try {
			// Abgebrochende sowie gespielte Spielrunden ums ein erhöhen
			pstmtIncAbortedRouns = DBConnection.getPstmt(DBStatements.UPDATE_GAME_STATISTIC_ABORTED_ROUNDS.getStmt());
			pstmtIncAbortedRouns.setInt(1, playerId);
			pstmtIncAbortedRouns.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * entfernt einen Spieler aus der laufenden Spielrunde
	 * 
	 * @param roundId
	 *            die Id der entsprechenden Spielrunde
	 * @param player
	 *            die User-Id des entsprechenden Spielers
	 * @return false
	 * @throws RoundNotThereException
	 *             Wird geworfen, wenn die Runde nicht existiert
	 */
	@Override
	public boolean removePlayer(int roundId, int playerId) throws RoundNotThereException {

		/*
		 * die entsprechende Runde ermitteln
		 */
		final Round round = RoundManager.rounds.get(roundId);

		/*
		 * Runde darf nicht "null" sein
		 */
		if (round != null) {

			/*
			 * Rundenobjekt sperren um inkonsistente Datenzustände zu vermeiden
			 */
			synchronized (round) {

				/*
				 * Spieler aus der Rundenmap löschen
				 */
				round.getPlayerManager().getPlayers().remove(playerId);

				/*
				 * Neues PlayerLeft Event werfen, das bei jedem Watcher und Player in der Runde den Spieler als
				 * gestorben markiert und ihn aus der Runde nimmt
				 */
				addEvent(DomainFactory.getDomain("round_" + roundId), new PlayerLeftEvent(playerId));

				/*
				 * LobbyEvent, um die anzahl der Spieler im Listgrid zu verringern
				 */
				addEvent(RoundManager.DOMAIN, new ChangeFreePlayerWatcherSlots(roundId, round.getFreePlayerSlots(),
						round.getFreeWatcherSlots()));

				/*
				 * Lobbyevent werfen um Runde zu löschen, wenn keiner mehr in der Runde ist
				 */
				if (round.getPlayerManager().getPlayers().size() == 0) {
					/*
					 * Alle Spieler haben die Runde verlassen
					 */

					/*
					 * Watcher rausschmeißen
					 */
					addEvent(DomainFactory.getDomain("round_" + roundId), new WatcherNotificationNoWinnerEvent());

					/*
					 * Runde löschen aus der Lobby
					 */
					addEvent(RoundManager.DOMAIN, new RemoveRoundEvent(roundId));

					/*
					 * Runde löschen
					 */
					RoundManager.rounds.remove(roundId);

				} else if (round.getPlayerManager().getPlayers().size() == 1) {

					/*
					 * Nur noch ein Spieler in der Runde, dieser hat gewonnen
					 */
					addEvent(DomainFactory.getDomain("round_" + roundId), new OnePlayerLeftEvent(playerId));

				} else if (round.getPlayerManager().getPlayers().size() == round.getPlayerManager()
						.getProgrammingCardsSendMap().size()) {

					/*
					 * Der Animationtimer kann gestartet werden, da schon alle anderen fertig sind
					 */
					final Map<Integer, List<Programmingcard>> hMap = new HashMap<Integer, List<Programmingcard>>(round
							.getPlayerManager().getProgrammingCardsSendMap());
					round.getPlayerManager().getProgrammingCardsSendMap().clear();

					/*
					 * Event werfen, das Die Spieler veranlasst mit der Abarbeitung der Karten zu beginnen
					 */
					addEvent(DomainFactory.getDomain("round_" + roundId), new CardsSendEvent(hMap));

					/*
					 * Die Programmierphase ist beendet
					 */
					round.setInProgrammingPhase(false);
				}
			}
		}

		return false;
	}

	/**
	 * Hier werden Spieler gelöscht. Dies wird weitergegeben an die Round und von dort an den PlayerManager
	 * 
	 * @author Jannik
	 * @version 1.0
	 * @param player
	 *            der zu löschende Player
	 * @param roundID
	 *            die Round, aus der der Player gelöscht werden soll
	 * @return true, wenn erfolgreich
	 * @see {@link Round#removePlayer(Player)} und {@link PlayerManager#removeFromPlayers(Player)}
	 */
	@Override
	public boolean removePlayer(Player player, int roundID) throws RoundNotThereException {

		if (RoundManager.rounds.get(roundID) == null) {
			throw new RoundNotThereException();
		}

		if (RoundManager.rounds.get(roundID).removePlayer(player)) {

			// RoundEvent
			addEvent(DomainFactory.getDomain("round_" + roundID), new RemovePlayerWatcherRoundEvent(player));

			// LobbyEvent
			addEvent(RoundManager.DOMAIN, new ChangeFreePlayerWatcherSlots(roundID, getRound(roundID)
					.getFreePlayerSlots(), getRound(roundID).getFreeWatcherSlots()));

			return true;
		}

		return false;
	}

	/**
	 * Entfernt eines Spieler aus einer laufenden Runde. Diese Methode soll aufgerufen werden, wenn der Spieler die
	 * Runde nicht über den Beenden-Button verlässt sondern die Robocraftseite.
	 * 
	 * @param roundId
	 *            RundenID vom RoundManager
	 * @param userID
	 *            UserID
	 */
	@Override
	public boolean removePlayerOnUnload(int roundId, int userID) throws RoundNotThereException {

		/*
		 * die Runde ermitteln
		 */
		final Round round = RoundManager.rounds.get(roundId);

		/*
		 * die Runde sperren
		 */
		synchronized (round) {

			if (round.getPlayerManager().getPlayers().containsKey(userID)) {

				/*
				 * Spieler aus der Runde werfen
				 */
				if (round.isInProgrammingPhase()) {
					/*
					 * Runde befindet sich in der Programmierphase
					 */
					abortFromRound(roundId, userID);
				} else {

					/*
					 * Runde befindet sich in der Ausführungsphase
					 */
					sendRequestStepReady(roundId, userID, 1);
				}
			} else if (round.getPlayerManager().getWatchers().containsKey(userID)) {

				/*
				 * Watcher entfernen
				 */
				round.getPlayerManager().getWatchers().remove(userID);

				/*
				 * LobbyEvent
				 */
				addEvent(RoundManager.DOMAIN, new ChangeFreePlayerWatcherSlots(roundId, round.getFreePlayerSlots(),
						round.getFreeWatcherSlots()));
			}
		}
		return false;
	}

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
	@Override
	public synchronized boolean removeRound(int roundID) throws RoundNotThereException {

		if (RoundManager.rounds.get(roundID) == null) {
			throw new RoundNotThereException();
		}

		// Wenn die Round gelöscht werden konnte
		if (RoundManager.rounds.remove(roundID) != null) {

			// Lobby Event
			addEvent(RoundManager.DOMAIN, new RemoveRoundEvent(roundID));

			// Round Event
			addEvent(DomainFactory.getDomain("round_" + roundID), new RoundCancelledEvent());

			return true;
		}

		return false;
	}

	/**
	 * Hier werden Watcher gelöscht. Dies wird weitergegeben an die Round und von dort an den PlayerManager
	 * 
	 * @param watcher
	 *            der zu löschende Watcher
	 * @param roundID
	 *            die Round, aus der der Watcher gelöscht werden soll
	 * @return true, wenn erfolgreich
	 * @see {@link Round#removeWatcher(Watcher)} und {@link PlayerManager#removeFromWatchers(Watcher)}
	 */
	@Override
	public boolean removeWatcher(Watcher watcher, int roundID) throws RoundNotThereException {
		if (RoundManager.rounds.get(roundID) == null) {
			throw new RoundNotThereException();
		}

		if (RoundManager.rounds.get(roundID).removeWatcher(watcher)) {

			// RoundEvent
			addEvent(DomainFactory.getDomain("round_" + roundID), new RemovePlayerWatcherRoundEvent(watcher));

			// LobbyEvent
			addEvent(RoundManager.DOMAIN, new ChangeFreePlayerWatcherSlots(roundID, getRound(roundID)
					.getFreePlayerSlots(), getRound(roundID).getFreeWatcherSlots()));

			return true;
		}

		return false;

	}

	/**
	 * Löscht einen Beobachter aus einer Runde
	 * 
	 * @param watcherId
	 *            ID des Beobachters
	 * @param roundID
	 *            RundenID
	 * 
	 * @return true
	 */
	@Override
	public boolean removeWatcher(int watcherId, int roundID) throws RoundNotThereException {

		final Round round = RoundManager.rounds.get(roundID);

		/*
		 * Spieler löschen
		 */
		round.getPlayerManager().getWatchers().remove(watcherId);

		/*
		 * LobbyEvent
		 */
		addEvent(RoundManager.DOMAIN,
				new ChangeFreePlayerWatcherSlots(roundID, round.getFreePlayerSlots(), round.getFreeWatcherSlots()));

		return true;

	}

	/**
	 * Trägt die Statistiken der Spieler nach Beendigung einer Spielrunde in die DB ein.
	 * 
	 * @param roundId
	 *            die entsprechende Runde
	 * @param winnerId
	 *            UserID des Gewinners
	 * @param statistic
	 *            Map mit der Statistik
	 * @param awards
	 *            Map mit den Awards
	 * @return true, wenn alles geklappt hat
	 */
	@Override
	public boolean saveStatisticsInDB(final int roundId, final int winnerId, final Map<Integer, Integer> statistic,
			final Map<Integer, Set<Integer>> awards) {

		// Hilfsvariablen
		PreparedStatement pstmtUpdateGameStatistic = null;
		PreparedStatement pstmtInsertNewAchievment = null;
		PreparedStatement pstmtAwardCount = null;
		ResultSet rsAwardCount = null;
		PreparedStatement pstmtStatisticAward4User = null;
		ResultSet rsStatisticAward4User = null;
		PreparedStatement pstmtReachedAward4User = null;
		ResultSet rsReachedAward4User = null;
		PreparedStatement pstmtCountOfAllAwards = null;
		ResultSet rsCountOfAllAwards = null;

		PreparedStatement pstmtInsertNewStatisticAward = null;
		int newAwardID = 0;

		/*
		 * die Runde holen
		 */
		final Round round = RoundManager.rounds.get(roundId);

		if (round != null) {

			synchronized (round) {

				if (round.isStatisticSaved() == false && statistic != null && awards != null) {

					/*
					 * Als gespeichert setzen
					 */
					round.setStatisticSaved(true);

					/*
					 * Runde entfernen
					 */
					RoundManager.rounds.remove(roundId);

					/*
					 * Lobby Event zum Entfernen aus dem Lobby Listgrid
					 */
					addEvent(RoundManager.DOMAIN, new RemoveRoundEvent(roundId));

					try {
						/*
						 * Awards in DB schreiben
						 */

						// Alle Awards in game_award bzw. game_statistic_award
						pstmtInsertNewAchievment = DBConnection.getPstmt(DBStatements.INSERT_NEW_ARCHIEVED_AWARD
								.getStmt());
						pstmtAwardCount = DBConnection.getPstmt(DBStatements.SELECT_AWARD_COUNT.getStmt());
						pstmtStatisticAward4User = DBConnection.getPstmt(DBStatements.SELECT_STATISTIC_AWARD4USER
								.getStmt());
						pstmtReachedAward4User = DBConnection.getPstmt(DBStatements.SELECT_REACHED_AWARD_FOR_USER
								.getStmt());
						pstmtInsertNewStatisticAward = DBConnection.getPstmt(DBStatements.INSERT_NEW_GAME_STATIC_AWARD
								.getStmt());
						pstmtCountOfAllAwards = DBConnection.getPstmt(DBStatements.SELECT_COUNT_OF_AWARDS.getStmt());

						// Awards durchlaufen
						for (Integer awardID : awards.keySet()) {
							for (Integer userID : awards.get(awardID)) {

								if (awardID <= 10) {
									// Normale Awards
									pstmtInsertNewAchievment.setInt(1, userID);
									pstmtInsertNewAchievment.setInt(2, awardID);
									pstmtInsertNewAchievment.executeUpdate();

									// Anzahl der Awards ermitteln
									pstmtAwardCount.setInt(1, userID);
									pstmtAwardCount.setInt(2, awardID);
									rsAwardCount = pstmtAwardCount.executeQuery();
									if (rsAwardCount.next()) {
										if (rsAwardCount.getInt(1) > 0) {
											// Prüfen ob Grenze für diesen Award
											// erreicht
											pstmtStatisticAward4User.setInt(1, awardID);
											pstmtStatisticAward4User.setInt(2, rsAwardCount.getInt(1));
											rsStatisticAward4User = pstmtStatisticAward4User.executeQuery();
											if (rsStatisticAward4User.next()) {
												// Prüfen ob User einen neuen
												// Statisic Award erreicht hat
												pstmtReachedAward4User.setInt(1, userID);
												pstmtReachedAward4User.setInt(2, rsStatisticAward4User.getInt(1));
												rsReachedAward4User = pstmtReachedAward4User.executeQuery();
												if (!rsReachedAward4User.next()) {
													// Award wurde noch nicht
													// erreicht erreicht
													pstmtInsertNewStatisticAward.setInt(1, userID);
													pstmtInsertNewStatisticAward.setInt(2,
															rsStatisticAward4User.getInt(1));
													pstmtInsertNewStatisticAward.executeUpdate();
												}
											}
										}
									}
								} else {
									// AwardID > 10 sind Statistic-Awards:
									// Hamster usw.
									pstmtInsertNewStatisticAward.setInt(1, userID);
									// ID mapping von game_awardtype nach
									// game_statistic_awardtype
									switch (awardID) {
									case 11:
										newAwardID = 37;
										break;
									case 12:
										newAwardID = 38;
										break;
									case 13:
										newAwardID = 39;
										break;
									default:
										newAwardID = 0;
									}
									if (newAwardID > 0) {
										// Prüfen ob der Award schon vorhanden
										// ist:
										pstmtReachedAward4User.setInt(1, userID);
										pstmtReachedAward4User.setInt(2, newAwardID);
										rsReachedAward4User = pstmtReachedAward4User.executeQuery();
										if (!rsReachedAward4User.next()) {

											// Neuen Award einfügen
											pstmtInsertNewStatisticAward.setInt(2, newAwardID);
											pstmtInsertNewStatisticAward.executeUpdate();
										}
									}
								}
								// Prüfen ob alle Awards erreicht wurden, dann
								// SuperAward einfügen.
								pstmtCountOfAllAwards.setInt(1, userID);
								rsCountOfAllAwards = pstmtCountOfAllAwards.executeQuery();
								if (rsCountOfAllAwards.next()) {
									if (rsCountOfAllAwards.getInt(1) == 35) {
										pstmtReachedAward4User.setInt(1, userID);
										pstmtReachedAward4User.setInt(2, 42);
									}
								}
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {

						// Schliessen der Resultsets
						try {
							if (rsAwardCount != null && !rsAwardCount.isClosed()) {
								rsAwardCount.close();
							}
							if (rsStatisticAward4User != null && !rsStatisticAward4User.isClosed()) {
								rsStatisticAward4User.close();
							}
							if (rsReachedAward4User != null && !rsReachedAward4User.isClosed()) {
								rsReachedAward4User.close();
							}
							if (rsCountOfAllAwards != null && !rsCountOfAllAwards.isClosed()) {
								rsCountOfAllAwards.close();
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					try {
						// Anzahl gewonnener Spiele erhöhen, usw.
						pstmtUpdateGameStatistic = DBConnection.getPstmt(DBStatements.UPDATE_GAME_STATISTIC.getStmt());
						for (final Integer userID : statistic.keySet()) {
							final int points = statistic.get(userID);

							if (winnerId != 0 && winnerId == userID) {
								// Gewinner
								pstmtUpdateGameStatistic.setInt(1, 1);
							} else {
								// Kein Gewinner
								pstmtUpdateGameStatistic.setInt(1, 0);
							}
							pstmtUpdateGameStatistic.setInt(2, points);
							pstmtUpdateGameStatistic.setInt(3, userID);
							pstmtUpdateGameStatistic.executeUpdate();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return false;
	}

	/**
	 * Wird vom Spieler aufgerufen, nachdem er seine Programmierkarten gewählt und auf bereit geklickt hat. Diese
	 * Methode liefert immer true, da er die Karten der anderen Spieler per Event zugeschickt bekommt, wenn diese alle
	 * ihre Karten an den Server gesendet haben.
	 * 
	 * @return immer true;
	 */
	@Override
	public boolean sendProgrammingcards(int roundId, int playerId, List<Programmingcard> cards) {

		/*
		 * Ermitteln der Runde
		 */
		final Round round = RoundManager.rounds.get(roundId);

		/*
		 * Runde darf nicht null sein
		 */
		if (round != null) {

			/*
			 * Nutzer muss sich in der Runde als Spieler befinden
			 */
			if (round.getPlayerManager().getPlayers().containsKey(playerId)) {

				/*
				 * Rundenobjekt sperren, um keine ungeültigen Spielerlisten zu erzeugen
				 */
				synchronized (round) {

					/*
					 * Event werfen, der den Player als "Ready" bei den anderen anzeigt
					 */
					addEvent(DomainFactory.getDomain("round_" + roundId), new PlayerCardsSetEvent(playerId));

					/*
					 * Map deklarieren, die zurückgeliefert werden soll, wenn alle Spieler ihre Programmierkarten
					 * gesendet haben
					 */
					final Map<Integer, List<Programmingcard>> hMap;

					/*
					 * Map zurückliefern wenn diese nicht "null" ist und somit ale Spieler ihre programmierkarten
					 * gesendet haben
					 */
					if ((hMap = round.getPlayerManager().getSortedProgrammingCards(playerId, cards)) != null) {

						/*
						 * Das Event schmeißen, dass dem Spieler zu wissen gibt, dass alle ihre Programmierkarten
						 * gesendet haben Der Spieler kann nun mit der Abarbeitung der Bewegungen beginnen
						 */
						addEvent(DomainFactory.getDomain("round_" + roundId), new CardsSendEvent(hMap));

						/*
						 * Runde ist nun nicht mehr in der Programmierphase
						 */
						round.setInProgrammingPhase(false);

						/*
						 * Prüfen ob nur noch ein Spieler in der Programmierphase ist.
						 */
					} else if (round.getPlayerManager().getProgrammingCardsSendMap().size() == round.getPlayerManager()
							.getPlayers().size() - 1) {

						/*
						 * Wenn "Countdown reduzieren" eingeschaltet" ist
						 */
						if (round.getRoundOption().isReduceCountdown()) {

							/*
							 * Liste mit allen Spielern in der Runde erstellen
							 */
							final List<Integer> last = new ArrayList<Integer>(round.getPlayerManager().getPlayers()
									.keySet());

							/*
							 * Alle Spieler entfernen, die ihre Programmierkarten bereits gesendet haben
							 */
							last.removeAll(round.getPlayerManager().getProgrammingCardsSendMap().keySet());

							/*
							 * Ermitteln des Spielers, der seine Karten noch nicht gesendet hat
							 */
							final int lastPlayerId = last.get(0);

							/*
							 * Event schicken, der den Countdown reduziert Der Client vergleicht die übergebene PlayerId
							 * mit seiner eigenen und reduziert seinen Countdown
							 */
							addEvent(DomainFactory.getDomain("round_" + roundId), new CountdownDecrementEvent(
									lastPlayerId));
						}
					}
				}
			}
		}

		/*
		 * Es wird immer "true" geliefert
		 */
		return true;
	}

	/**
	 * Wird vom Spieler nach Abarbeitung eines Spielschritts aufgerufen
	 * 
	 * @param roundId
	 *            die Id der entsprechenden Spielrunde
	 * @param playerId
	 *            die User-Id des entsprechenden Spielers
	 * @param userState
	 *            der Status, der der Spieler momentan hat. Gibt an ob der Spieler nun Roboter ist
	 */
	@Override
	public boolean sendRequestStepReady(int roundId, int playerId, int userState) {

		/*
		 * die entsprechende Runde ermitteln
		 */
		final Round round = RoundManager.rounds.get(roundId);

		/*
		 * die Runde darf nicht null sein
		 */
		if (round != null) {

			/*
			 * Rundenobjekt wird gesperrt um inkonsistente Datenoperationen zu vermeiden
			 */
			synchronized (round) {

				if (round.getPlayerManager().getPlayers().containsKey(playerId)) {

					if (round.getPlayerManager().setPlayerStepReady(playerId, userState)) {

						if (round.getPlayerManager().getPlayers().size() > 0) {

							/*
							 * Nächsten Spielschritt ausführen
							 */
							addEvent(DomainFactory.getDomain("round_" + roundId), new StepReadyEvent(round
									.getPlayerManager().getPlayerRequestedAbortSet()));

						} else {

							/*
							 * Watcher rausschmeißen
							 */
							addEvent(DomainFactory.getDomain("round_" + roundId),
									new WatcherNotificationNoWinnerEvent());

							/*
							 * Runde löschen aus der Lobby
							 */
							addEvent(RoundManager.DOMAIN, new RemoveRoundEvent(roundId));

							/*
							 * Runde löschen
							 */
							RoundManager.rounds.remove(roundId);

						}
					}
				}
			}
		}
		return true;
	}

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
	 */
	@Override
	public boolean sendRestartField(int roundId, int playerId, int i, int j) {

		/*
		 * Event werfen, das beim Spieler und Beobachter das Restartfeld des angegeben Spielers neu setzt
		 */
		addEvent(DomainFactory.getDomain("round_" + roundId), new BackupFieldEvent(playerId, i, j));

		return true;
	}

	/**
	 * Setzt eine Farbe für einen Player
	 * 
	 * @author timo
	 * @version 1.0
	 */
	@Override
	public boolean setPlayerColor(Player player, int roundID, Color color) throws UserNotInRoundException,
			RoundNotThereException, ColorAlreadyExistsException {

		final Round round = RoundManager.rounds.get(roundID);

		if (round == null) {
			throw new RoundNotThereException();
		}

		/*
		 * Rundenobjekt sperren
		 */
		synchronized (round) {
			final Player tmpPlayer = round.getPlayerManager().getPlayers().get(player.getUser().getId());

			if (tmpPlayer == null) {
				throw new UserNotInRoundException();
			}

			if (!round.getPlayerManager().setRobotColor(player.getUser().getId(), color)) {
				throw new ColorAlreadyExistsException();
			}

			addEvent(DomainFactory.getDomain("round_" + roundID), new ChangePlayerColorEvent(tmpPlayer));

			return true;
		}
	}

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
	@Override
	public boolean setPlayerReady(Player player, int roundID, boolean flag) throws RoundNotThereException,
			UserNotInRoundException {

		if (RoundManager.rounds.get(roundID) == null) {
			throw new RoundNotThereException();
		}

		// Falls der User gar nicht an der Round teilnimmt.
		if (RoundManager.rounds.get(roundID).getPlayerManager().getPlayers().get(player.getUser().getId()) == null) {
			throw new UserNotInRoundException();
		}

		// Die richtige Version vom Player holen und flag setzen
		RoundManager.rounds.get(roundID).getPlayerManager().getPlayers().get(player.getUser().getId()).setReady(flag);

		// RoundEvent
		addEvent(DomainFactory.getDomain("round_" + roundID), new SetPlayerReadyEvent(player, flag));

		return true;
	}

	/**
	 * Setzt alle Robots der Player, setzt eine Farbe für jeden Player (der noch keine gewählt hat). Startet am Ende die
	 * Runde
	 */
	@Override
	public boolean setRobotPositionAndColor(int roundID) throws RoundNotThereException, CantLoadPlayingBoardException {

		if (RoundManager.rounds.get(roundID) == null) {
			throw new RoundNotThereException();
		}

		/*
		 * Die Runde anhand der Id bestimmen
		 */
		final Round round = RoundManager.rounds.get(roundID);

		/*
		 * Runde als gestartet markieren
		 */
		round.setStarted(true);

		/*
		 * Neuen Logicmanager erstellen. Diesem werden im Verlauf der Funktion noch die Roboter und das Spielbrett
		 * hinzugefügt.
		 */
		final RoundNeedsWrapper roundNeedsWrapper = new RoundNeedsWrapper();

		/*
		 * dem LogicManager die Spielregeln hinzufügen
		 */
		roundNeedsWrapper.setRoundOption(round.getRoundOption());

		/*
		 * dem Logicmanager das Playingboard hinzufügen
		 */
		roundNeedsWrapper.setPlayingBoard(round.getPlayingBoard());

		/*
		 * Liste mit verfügbaren Farben erstellen
		 */
		final List<Color> colorList;

		if (!round.getRoundOption().getGameName().equals("Lilliputanien")) {

			colorList = new ArrayList<Color>(Arrays.asList(Color.values()));
			colorList.remove(Color.WHITE);
			for (Player player : round.getPlayerManager().getPlayers().values()) {
				// Farbe entfernen, wenn diese schon vergeben ist
				if (player.getRobot().getColor() != null) {
					colorList.remove(player.getRobot().getColor());
				}
			}
		} else {
			colorList = new ArrayList<Color>();
			for (Player player : round.getPlayerManager().getPlayers().values()) {
				player.getRobot().setColor(null);
				colorList.add(Color.WHITE);
			}
		}

		/*
		 * Liste mischen, um Farben nicht immer der Reihenfolge nach zu vergeben
		 */
		Collections.shuffle(colorList);

		/*
		 * Die Startplätze des Spielbretts in eine neue Liste laden, um die neue Liste später zu permutieren
		 */
		final List<StartField> startFieldList = new ArrayList<StartField>(roundNeedsWrapper.getPlayingBoard()
				.getStartFieldList().subList(0, round.getPlayerManager().getPlayers().size()));

		/*
		 * Startfeldliste permutieren
		 */
		Collections.shuffle(startFieldList);

		/*
		 * 1. Farben für Roboter vergeben, deren Spieler dies nicht explizit taten. 2. Roboter aufs Startfeld setzen und
		 * das RestartField des Roboters mit dem Startfeld verknüpfen. 3. Blickrichtung des Roboters setzen.
		 */
		for (final Player player : round.getPlayerManager().getPlayers().values()) {

			// Farbe setzen, falls nicht vom Spieler gewählt
			if (player.getRobot().getColor() == null) {
				player.getRobot().setColor(colorList.remove(0));
			}

			// Roboter aufs Startfeld setzen
			final StartField startField = startFieldList.remove(0);
			player.getRobot().setPosition(new Position(startField.getI(), startField.getJ()));

			// RestartField des Roboters mit seinem Startfeld verlinken
			player.getRobot().setStartField(startField);

			// Blickrichtung des Roboters setzen
			player.getRobot().setDirection(startField.getRespawnDirection());

			// Spieler des Roboters setzen
			player.getRobot().setPlayer(player);

			// Roboter dem Logical Manager hinzufügen
			roundNeedsWrapper.addRobot(player.getUser().getId(), player.getRobot());
		}

		/*
		 * StartRoundEvent wird an alle Spieler und Beobachter dieser Runde geschickt Das Roundevent leitet die Spieler
		 * und Watcher auf die Roundplayerpage bzw. auf die Roundwatcherpage
		 */
		addEvent(DomainFactory.getDomain("round_" + roundID), new StartRoundEvent(roundID, roundNeedsWrapper));

		/*
		 * Rundeninformation wird initial in der DB gespeichert.
		 */
		PreparedStatement pstmtInsertRound = null;
		try {
			pstmtInsertRound = DBConnection.getPstmt(DBStatements.INSERT_ROUND.getStmt());
			pstmtInsertRound.setInt(1, round.getPlayingBoard().getID());
			pstmtInsertRound.setString(2, round.getRoundOption().getGameName());
			pstmtInsertRound.setTimestamp(3, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			pstmtInsertRound.setInt(4, round.getPlayerManager().getNumberOfPlayers());
			pstmtInsertRound.setInt(5, (round.getRoundOption().isRobotShootsOn() ? 1 : 0));
			pstmtInsertRound.setInt(6, (round.getRoundOption().isAlwaysCompactorsOn() ? 1 : 0));
			pstmtInsertRound.setInt(7, (round.getRoundOption().isAlwaysPushersOn() ? 1 : 0));
			pstmtInsertRound.setInt(8, (round.getRoundOption().isLasersOn() ? 1 : 0));

			pstmtInsertRound.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		/*
		 * Datenbank-ID wird der round zugeordnet
		 */
		ResultSet rsLastID = null;
		try {
			PreparedStatement pstmtLastID = DBConnection.getPstmt(DBStatements.SELECT_LAST_ROUND_ID.getStmt());
			rsLastID = pstmtLastID.executeQuery();
			if (rsLastID.next()) {
				round.setdbID(rsLastID.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rsLastID != null && !rsLastID.isClosed()) {
					rsLastID.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		PreparedStatement pstmtUpdateLastRounds = null;

		/*
		 * Aktualisiert die zuletzt gespielten Runden der Spieler
		 */
		try {
			pstmtUpdateLastRounds = DBConnection.getPstmt(DBStatements.UPDATE_LAST_ROUNDS.getStmt());
			for (Integer id : round.getPlayerManager().getPlayers().keySet()) {
				pstmtUpdateLastRounds.setInt(1, round.getdbID());
				pstmtUpdateLastRounds.setInt(2, id); // userID
				pstmtUpdateLastRounds.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		/*
		 * Da dieser RPC nur dazu dient, Events zum Client zu schicken, ist der Rückgabewert immer true
		 */
		return true;
	}

	/**
	 * Startet den CountDown, bevor die Round los geht.
	 * 
	 * @param roundID
	 * @return true
	 */
	@Override
	public boolean startCountDown(int roundID) {
		/*
		 * Das RoundStartedEvent wird an alle eingeloggten Nutzer geschickt, um die Spielrunde in der Lobby als
		 * "gestartet" zu markieren.
		 */
		addEvent(RoundManager.DOMAIN, new RoundStartedEvent(roundID));

		new ChatServiceImpl().startRound(roundID, this);
		return true;
	}

	/**
	 * Prüft ob ein Playingboard gerade benutzt wird. Dann darf es nicht überschrieben werden! Zur Sicherheit
	 * synchronized
	 * 
	 * @param playingboardname
	 * @return true wenns benutzt wird, sonst false
	 * @author timo
	 */
	public static synchronized boolean isPlayingBoardInUse(String playingboardname) {
		// String kopieren (damit nicht der echte geändert wird)
		String tmpPlayingboardname = playingboardname;
		tmpPlayingboardname = tmpPlayingboardname.toLowerCase().replace(" ", "");

		for (Integer roundID : rounds.keySet()) {
			if (rounds.get(roundID).getPlayingBoard().getName().toLowerCase().replace(" ", "")
					.equals(tmpPlayingboardname)) {
				return true;
			}
		}
		return false;
	}
}
