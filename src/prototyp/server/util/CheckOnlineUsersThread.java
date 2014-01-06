package prototyp.server.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prototyp.server.model.RoundManager;
import prototyp.server.view.LobbyImpl;
import prototyp.shared.useradministration.AccountData;
import prototyp.shared.useradministration.User;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;

/**
 * Soll User finden, die offline sind. Diese Klasse ist nach dem
 * Single-Ton-Pattern realisiert worden.
 * 
 * @author Timo
 * @version 1.0
 */
public class CheckOnlineUsersThread extends Thread {

	/** Instanz für das Singleton-Pattern */
	private static CheckOnlineUsersThread instance = null;

	/**
	 * Starte den Tread
	 * 
	 * @return true, falls alles geklappt hat
	 */
	public static synchronized boolean startThread() {
		if (CheckOnlineUsersThread.instance == null) {
			CheckOnlineUsersThread.instance = new CheckOnlineUsersThread();
			CheckOnlineUsersThread.instance.start();
		}

		return true;
	}

	/**
	 * Default-Konstruktor
	 */
	private CheckOnlineUsersThread() {

	}

	/**
	 * Wird immer ausgeführt.
	 */
	@Override
	public void run() {
		while (true) {
			try {
				// Checken ob jemand sich zulange nicht gemeldet hat.
				final Map<Integer, Long> onlineUsersWithTimeStamps = new HashMap<Integer, Long>(
						LobbyImpl.getOnlineUsersTimeStamps());
				for (final Integer userID : onlineUsersWithTimeStamps.keySet()) {

					// Wenn der Eintrag älter ist als drei Minuten dann sollte
					// er Offline sein
					if (System.currentTimeMillis()
							- onlineUsersWithTimeStamps.get(userID) - 60000 > 0) {

						// Nickname sichern
						String tmpNickname = new LobbyImpl().getOnlineUsers()
								.get(userID);

						// Aus den Collections austragen
						LobbyImpl.removeOnlineUsersTimeStamp(userID);
						new LobbyImpl().removeOnlineUser(userID);

						// RoundList erstellen
						List<Integer> roundList = new ArrayList<Integer>();
						// Liste mit aktiven Chats
						List<Domain> chatDomains = new ArrayList<Domain>();
						// als erstes den globalen Chat hinzufügen
						chatDomains.add(DomainFactory.getDomain("global-chat"));

						// Jetzt alle Rounds suchen in dem der User sich
						// befindet
						try {
							// Alle Rounds durchlaufen
							for (final Integer roundID : new RoundManager()
									.getRounds().keySet()) {
								// Schauen ob der User drin ist
								for (final Integer userIDKey : new RoundManager()
										.getPlayersAndWatchers(roundID)
										.keySet()) {
									// Wenn der User in dieser Round ist (als
									// Player oder Watcher)
									if (userIDKey == userID) {
										chatDomains.add(DomainFactory
												.getDomain("internalChatRound:"
														+ roundID));
										roundList.add(roundID);
										break;
									}
								}
							}

						} catch (Exception e) {
							// Hier wird gar nichts gemacht, weil es keine Sau
							// interessiert. -timo
						} finally {
							// Nachricht in allen Chats durch onClosing
							final AccountData tmpAccData = new AccountData();
							tmpAccData.setNickname(tmpNickname);
							new UserServiceImpl().onClosing(new User(userID,
									tmpAccData), chatDomains, roundList);
						}

					}
				}

				// 1min Schlafen
				sleep(60000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
