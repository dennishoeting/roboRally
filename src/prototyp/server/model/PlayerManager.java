package prototyp.server.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import prototyp.shared.exception.round.NoRoundsCreatedException;
import prototyp.shared.programmingcard.Programmingcard;
import prototyp.shared.round.Player;
import prototyp.shared.round.Watcher;
import prototyp.shared.util.Color;
import prototyp.shared.util.RestartFieldInfo;
import prototyp.shared.util.events.lobby.ChangeFreePlayerWatcherSlots;
import prototyp.shared.util.events.lobby.RemoveRoundEvent;
import prototyp.shared.util.events.round.StepReadyEvent;
import prototyp.shared.util.events.round.WatcherNotificationNoWinnerEvent;
import de.novanic.eventservice.client.event.domain.DomainFactory;

/**
 * PlayerManager
 * 
 * @author Marcus, Andreas, Mischa
 * @version 1.0
 * @version 1.1 GameInitialtorklasse gelöscht, wird nicht benötigt.
 * @version 1.2 (26.09.2010) getPlayers
 * 
 * @see {@link Round}
 */
public final class PlayerManager {

	/** Seriennummer */
	private static final long serialVersionUID = -8991455623594871068L;
	
	/**
	 * Thread, der Spieler hinausschmeißt, wenn diese nicht schnell genug antworten
	 * @author Marcus
	 *
	 */
	class StepReadyTimoutThread extends Thread {
		
		@Override
		public void run() {
			try {
				
				/*
				 * 10 sek. schlafen legen
				 */
				Thread.sleep(10000);
				
				/*
				 * ein Set mit Spielern erstellen, die rausgeschmissen werden sollen
				 */
				final Set<Integer> newLeftSet = new HashSet<Integer>(players.keySet());
				newLeftSet.removeAll(playerStepReadySet);
				playerRequestedAbortSet.addAll(newLeftSet);
				
				/*
				 * Die Spieler aus der PlayerMap entfernen
				 */
				for(final Integer key : playerRequestedAbortSet) {
					players.remove(key);
				}
				
				/*
				 * Events schmeißen, damit die Runde weiterlaufen kann und in der Lobby die Spielerzahl geupdatet wird
				 */
				final RoundManager roundManager = new RoundManager();
				roundManager.addEvent(RoundManager.DOMAIN, new ChangeFreePlayerWatcherSlots(round.getID(),
						round.getFreePlayerSlots(), round.getFreeWatcherSlots()));
				roundManager.addEvent(DomainFactory.getDomain("round_" + round.getID()), new StepReadyEvent(playerRequestedAbortSet));
				
				if(players.isEmpty()) {
					
					/*
					 * Watcher rausschmeißen
					 */
					roundManager.addEvent(DomainFactory.getDomain("round_" + round.getID()), new WatcherNotificationNoWinnerEvent());
				
					
					/*
					 * Event schmeißem, um Runde aus der Lobby zu entfernen
					 */
					roundManager.addEvent(RoundManager.DOMAIN, new RemoveRoundEvent(round.getID()));
					
					/*
					 * Runde aus dem RoundManager löschen
					 */
					roundManager.getRounds().remove(round.getID());
				}
				
				
			} catch (InterruptedException e) {} 
			  catch (NoRoundsCreatedException e) {}
		
		}
	}
	
	/** Instanz des TimeoutThreads */
	private StepReadyTimoutThread stepReadyTimoutThread;

	/** Speichert eine Referenz auf den Spielstarter. */
	private final Player gameInitiator;

	/** Map mit den Spielern in einer Spielrunde */
	private final Map<Integer, Player> players;

	/** Liste mit den Beobachtern in einer Spielrunde */
	private final Map<Integer, Watcher> watchers;

	/** Speichert Spieler, die Programmierkarten erhalten wollen */
	private final Set<Player> programmingCardsRequestSet = new HashSet<Player>();

	/** Speichert Spieler, die ihre Programmierkarten gesendet haben */
	private final Map<Integer, List<Programmingcard>> programmingCardsSendMap = new HashMap<Integer, List<Programmingcard>>();

	/** Speichert die Restart-Felder der Roboter */
	private final Stack<RestartFieldInfo> restartFieldMap = new Stack<RestartFieldInfo>();

	/** speichert die Spieler, die einen Spielschritt abgearbeitet haben */
	private final Set<Integer> playerStepReadySet = new HashSet<Integer>();

	/** ich weiß nicht mehr wofür diese Liste war */
	private final Set<Integer> playerRequestedAbortSet = new HashSet<Integer>();

	/** Spieler die schon Programmierkarten erhalten haben */
	private final Set<Integer> playersReceivedProgramm = new HashSet<Integer>();

	/** die Runde */
	private final Round round;

	/**
	 * Konstruktor zum Initialisieren des PlayerManager
	 * 
	 * @param gameInitiator
	 *            der Spielstarter
	 */
	public PlayerManager(final Player gameInitiator, final Round round) {
		this.gameInitiator = gameInitiator;
		this.players = new HashMap<Integer, Player>();
		this.watchers = new HashMap<Integer, Watcher>();
		this.players.put(gameInitiator.getUser().getId(), gameInitiator);
		this.round = round;
	}

	/**
	 * Fügt den Spieler, die Karten schon gelegt haben, einer Liste hinzu
	 * 
	 * @param playerId
	 *            SpielerID
	 * @return true, wenn alle Karten von allen Spielern erhalten worden sind, ansonsten false
	 */
	public boolean addPlayerReceived(int playerId) {
		this.playersReceivedProgramm.add(playerId);

		if (this.playersReceivedProgramm.size() == this.players.size()) {
			/*
			 * Alle Spieler haben Programmierkarten erhalten
			 */
			this.playersReceivedProgramm.clear();
			return true;
		}

		/*
		 * Es gibt Spieler, die noch keine Programmierkarten erhalten haben
		 */
		return false;
	}

	/**
	 * Fügt einen Spieler der ArrayList hinzu. NICHT direkt aufrufen, sondern {@link RoundManager#addPlayer}!
	 * 
	 * @param player
	 *            der hinzuzufügende Spieler
	 * @return Index des Players wenn erfolgreich, sonst -1
	 * @version 1.1 (23.9.10) gibt jetzt int zurück, damit man gezielter auf die Player zugreifen kann --Jannik
	 * @version 1.2 (28.9.10) wird jetzt von der Round bzw vom RoundManager aufgerufen. NICHT direkt aufrufen! -Jannik
	 */
	public boolean addToPlayers(final Player player) {
		return this.players.put(player.getUser().getId(), player) == null;
	}

	/**
	 * Fügt einen Watcher der ArrayList hinzu. NICHT direkt aufrufen, sondern {@link RoundManager#addWatcher}!
	 * 
	 * @param watcher
	 *            der hinzuzufügende Beobachter
	 * @return true, wenn das Hinzufügen erfolgreich war
	 * @version 1.1 (23.9.10) gibt jetzt int zurück, damit man gezielter auf die Player zugreifen kann --Jannik
	 * @version 1.2 (28.9.10) wird jetzt von der Round bzw vom RoundManager aufgerufen. NICHT direkt aufrufen! -Jannik
	 */
	public final boolean addToWatchers(final Watcher watcher) {
		return this.watchers.put(watcher.getUser().getId(), watcher) == null;
	}

	/**
	 * Wechselt den Status eines Spielers zu dem eines Beobachters.
	 * 
	 * @param player
	 *            der Spieler der zum Beobachter werden soll
	 * @return true, wenn das Wechseln erfolgreich war
	 */
	public final boolean changeToWatcher(Player player) {
		// if (this.players.get(payer.get)) {
		// return this.players.remove(player);
		// }
		return false;
	}

	/**
	 * Gibt an ob eine Farbe bereits vergeben wurde.
	 * 
	 * @param color
	 *            Farbe
	 * @return true, wenn die Farbe schon vergeben wurde, ansonsten false
	 */
	public boolean colorThere(Color color) {
		for (Integer key : this.players.keySet()) {
			if (this.players.get(key).getRobot().getColor() != null && this.players.get(key).getRobot().getColor().equals(color)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gibt an, ob alle Player bereit sind
	 * 
	 * @return true, wenn alle Player bereit sind.
	 */
	public boolean everyoneReady() {
		for (Integer key : this.players.keySet()) {
			if (!this.players.get(key).isReady()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Liefert den Gameinitiator
	 * 
	 * @return gameInitiator
	 */
	public Player getGameInitiator() {
		return this.gameInitiator;
	}

	/**
	 * Liefert die Liste mit den Spielern, die rausgegangen sind
	 * 
	 * @return leftList
	 */
	public Set<Integer> getPlayerRequestedAbortSet() {
		return this.playerRequestedAbortSet;
	}

	/**
	 * Liefert die Anzahl der Spieler
	 * 
	 * @returnAnzahl der Spieler
	 */
	public int getNumberOfPlayers() {
		return this.players.size();
	}

	/**
	 * Liefert die Anzahl der Beobachter
	 * 
	 * @return Anzahl der Beobachter
	 */
	public int getNumberOfWatchers() {
		return this.watchers.size();
	}

	/**
	 * Liefert alle Spieler
	 * 
	 * @return players
	 */
	public Map<Integer, Player> getPlayers() {
		return this.players;
	}

	/**
	 * Liefert die Liste der Programmierkarten, die angefordert werden.
	 * 
	 * @return the programmingCardRequestList
	 */
	public Set<Player> getProgrammingCardsRequestSet() {
		return this.programmingCardsRequestSet;
	}

	/**
	 * Liefert die Programmierkarten zum Senden
	 * 
	 * @return the programmingCardsSendMap
	 */
	public Map<Integer, List<Programmingcard>> getProgrammingCardsSendMap() {
		return this.programmingCardsSendMap;
	}

	/**
	 * Liefert den RestartFieldStack
	 * 
	 * @return restartFieldMap
	 */
	public Stack<RestartFieldInfo> getRestartFieldStack() {
		return this.restartFieldMap;
	}

	/**
	 * Diese Methode liefert eine Map mit den Programmierkarten für jeden Spieler
	 * 
	 * @param playerId
	 *            SpielerID
	 * @param programmingcardsList
	 *            Liste mit Programmierkarten
	 * @return die Hashmap, wenn jeder Spieler seine Karten sortiert und auf "Bereit" geklickt hat, ansonsten "null"
	 */
	public Map<Integer, List<Programmingcard>> getSortedProgrammingCards(final int playerId,
			final List<Programmingcard> programmingcardsList) {

		/*
		 * die übergebene Liste der Map hinzufügen
		 */
		this.programmingCardsSendMap.put(this.players.get(playerId).getUser().getId(), programmingcardsList);

		/*
		 * Schauen, ob alle Spieler ihre Karten gesendet haben.
		 */
		if (this.programmingCardsSendMap.size() == this.players.size()) {

			// Map erstellen, die zurückgeliefert werden soll
			final Map<Integer, List<Programmingcard>> returnMap = new HashMap<Integer, List<Programmingcard>>(
					this.programmingCardsSendMap);

			// Map wieder leeren
			this.programmingCardsSendMap.clear();

			// die Map zurückliefern
			return returnMap;
		}
		/*
		 * wird geliefert, wenn noch nicht alle Spieler ihre gewählte Karten gesendet haben
		 */
		return null;

	}

	/**
	 * Liefert die Liste mit den Spielern, die schon ready sind
	 * 
	 * @return stepReadyList
	 */
	public Set<Integer> getPlayerStepReadySet() {
		return this.playerStepReadySet;
	}

	/**
	 * Liefert die Beobachter
	 * @return watchers
	 */
	public Map<Integer, Watcher> getWatchers() {
		return this.watchers;
	}

	/**
	 * Entfernt einen Spieler aus der Spielrunde
	 * 
	 * @param player
	 *            der zu entfernende Spieler
	 * @return true, wenn das Entfernen geklappt hat
	 */
	public boolean removeFromPlayers(Player player) {
		return this.players.remove(player.getUser().getId()) != null;
	}

	/**
	 * Entfernt einen Beobachter aus der Spielrunde
	 * 
	 * @param watcher
	 *            der zu entfernende Beobachter
	 * @return true, wenn das Entfernen geklappt hat
	 */
	public boolean removeFromWatchers(Watcher watcher) {
		return this.watchers.remove(watcher.getUser().getId()) != null;
	}

	/**
	 * Setzt den Kartenstatus eines Spielsers auf "gesetzt".
	 * 
	 * @param playerId
	 *            SpielerID
	 * @param cards
	 *            Liste mit Programmierkarten
	 * @return true, wenn alle SPieler ihre Karten geseztz haben
	 */
	public boolean setCards(int playerId, List<Programmingcard> cards) {
		this.players.get(playerId).setCards(cards);

		for (final Integer key : this.players.keySet()) {
			if (this.players.get(key).getCards() == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gibt an, ob jeder Spieler und Beobachter mit dem spielschritt fertig ist
	 * 
	 * @param playerId
	 *            SpielerID
	 * @param userState
	 *            Status des Spielers
	 * @return true, wenn alles okay ist, ansonsten false
	 */
	public boolean setPlayerStepReady(final int playerId, final int userState) {

		if (this.playerStepReadySet.size() >= this.players.size()) {
			this.playerStepReadySet.clear();
			this.playerRequestedAbortSet.clear();
		}

		// Bei 1 wird abgebrochen und der Spieler rausgeschmießen
		// Bei 2 wird ein Player zum Beobachter gemacht
		// Ansonsten wird ganz normal weitergespielt
		if (userState == 1) {

			// Spieler aus der Liste löschen
			final Player player = this.players.remove(playerId);

			if (player != null) {
				// Spieler zur Liste der verlassenen Spieler hinzufügen
				this.playerRequestedAbortSet.add(playerId);
				
				/*
				 * Events schmeißen
				 */
				final RoundManager roundManager = new RoundManager();
				roundManager.addEvent(RoundManager.DOMAIN, new ChangeFreePlayerWatcherSlots(round.getID(),
						round.getFreePlayerSlots(), round.getFreeWatcherSlots()));
				roundManager.incrementAbortedRounds(playerId);
			}
		} else if (userState == 2) {
			this.watchers.put(playerId, this.players.remove(playerId));
			this.playerStepReadySet.add(playerId);
			
			/*
			 * Events schmeißen
			 */
			final RoundManager roundManager = new RoundManager();
			roundManager.addEvent(RoundManager.DOMAIN, new ChangeFreePlayerWatcherSlots(round.getID(),
					round.getFreePlayerSlots(), round.getFreeWatcherSlots()));
			roundManager.incrementAbortedRounds(playerId);
			
			/*
			 * Timeout starten
			 */			
			if(this.stepReadyTimoutThread != null) {
				this.stepReadyTimoutThread.interrupt();
			}
			this.stepReadyTimoutThread = new StepReadyTimoutThread();
			this.stepReadyTimoutThread.start();
			
		} else {
			this.playerStepReadySet.add(playerId);
			
			/*
			 * Timeout starten
			 */		
			if(this.stepReadyTimoutThread != null) {
				this.stepReadyTimoutThread.interrupt();
			}
			this.stepReadyTimoutThread = new StepReadyTimoutThread();
			this.stepReadyTimoutThread.start();
		}

		if (this.playerStepReadySet.size() >= this.players.size()) {
			
			/*
			 * Alle Spieler haben den Spielschritt fertig animiert, es wird true geliefert.
			 */
			this.stepReadyTimoutThread.interrupt();
			return true;
		}
		
		/*
		 * Es sind nicht alle Spieler mit der Abarbeitung fertig
		 */
		return false;
	}

	/**
	 * Setzt den Status eines RoundUsers auf ein gegebenes flag
	 * 
	 * @version 1.0
	 * @version 1.1 jetzt kann man es mit einem flak setzen -Timo
	 * @param playerID
	 *            SpielerID
	 * @return true
	 */
	public boolean setReady(int playerID, boolean flak) {
		if (playerID < 0 || playerID > this.players.size() - 1) {
			return false;
		}

		this.players.get(playerID).setReady(flak);
		return true;
	}

	/**
	 * Setzt den Status eines Player auf das angegebene Flak
	 * 
	 * @version 1.0
	 * @version 1.1 jetzt kann man es mit einem Flak setzen
	 * @param player
	 *            der betroffene Player
	 * @return true, wenn alles erfolgreich war
	 */
	public boolean setReady(Player player, boolean flakGeschuetz) {
		this.players.get(player.getUser().getId()).setReady(flakGeschuetz);
		return true;
	}

	/**
	 * Setzt die Roboterfarbe.
	 * 
	 * @param userID
	 *            UserID
	 * @param color
	 *            Farbe für den Roboter
	 * @return true wenn es klappte, sonst false
	 * @author timo
	 */
	public boolean setRobotColor(int userID, Color color) {
		if (!colorThere(color)) {
			this.players.get(userID).getRobot().setColor(color);
			return true;
		}

		return false;
	}

	/**
	 * @return the stepReadyTimoutThread
	 */
	public StepReadyTimoutThread getStepReadyTimoutThread() {
		return stepReadyTimoutThread;
	}
	
	
}
