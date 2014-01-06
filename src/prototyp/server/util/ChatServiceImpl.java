package prototyp.server.util;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import prototyp.client.service.ChatService;
import prototyp.server.model.RoundManager;
import prototyp.server.view.LobbyImpl;
import prototyp.shared.exception.RoundNotThereException;
import prototyp.shared.util.events.chat.ChatEvent;
import prototyp.shared.util.events.chat.ChatEventCountDownStart;
import prototyp.shared.util.events.chat.ChatEventEnterPostGame;
import prototyp.shared.util.events.chat.ChatEventLeavePostGame;
import prototyp.shared.util.events.chat.PrivateChatEvent;
import prototyp.shared.util.events.round.DisableWidgetsEvent;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.service.RemoteEventServiceServlet;

/**
 * ChatModul auf der Serverseite
 * 
 * @author Timo (Verantwortlicher), Andreas, Mischa
 * @version 1.0
 */
public class ChatServiceImpl extends RemoteEventServiceServlet implements ChatService {

	/** Seriennummer */
	private static final long serialVersionUID = 3600386193552467191L;

	/** Bilderpfad für die Smileys */
	private final static String SMILEYS_PICPATH = "images/smileys/";

	/** Filter mit Such- und Ersetzungsmuster */
	private final static String[][] FILTER = { { "Dennis", "Der Kleine" }, { "Kamil", "Der Alte" }, { "Marcus", "Der Ossi" },
			{ "Marina", "Die Designerin" }, { "Andreas", "Die Leitung" }, { "Jens", "Chewbacca" },
			{ "Jannik", "Der, der es verkackt hat," }, { "Tim", "Der Schwede" }, { "Timo", "Der Ostfriese" },
			{ "Robert", "Megatron" }, { "Mischa", "Die Testmuschi" } };

	/** Suchmuster für Schimpfwortfilter, kann beliebig erweitert werden */
	private final static String[] INVECTIVE_FILTER = { "Arschloch", "Wichser", "Fuck", "Hure", "Nutte", "Hurensohn", "Penis",
			"Fotze", "Homo", "Arsch" };

	/** Smiles mit Dateinamen der Bilder */
	private final static String[][] SMILEYS = { { ":)", "heeyyy" }, { ":-)", "heeyyy" }, { "=)", "heeyyy" }, { "8-)", "cool" },
			{ "(*)", "stars" }, { ":-P", "crazy" }, { ":P", "crazy" }, { ":-@", "mad" }, { ":-D", "muhaha" }, { ":-O", "omg" },
			{ ":-S", "whoopsy" }, { ";)", "wink" }, { ";-)", "wink" }, { ":|", "wtf" }, { "8-|", "stupididea" },
			{ ":-#", "stayaway" } };

	/** Farbe für das Verlassen der PostGame */
	private static final String leavePostGameColor = "red";

	/**
	 * Ersetzt in einem übergebenen Text bestimmte Wörter. Hier wird ein regulärer Ausdruck verwendet.
	 * 
	 * @param rstr
	 *            Suchmuster
	 * @param estr
	 *            Ersetzungsmuster
	 * @param message
	 *            Text, der durchsucht werden soll
	 * @return überarbeiteter Text
	 */
	private static String replaceLinks(String rstr, String estr, String message) {
		Pattern pattern = Pattern.compile("(\\b)" + rstr + "(\\b|(\\Z))");
		Matcher matcher = pattern.matcher(message);

		return matcher.replaceAll("$2<i>" + estr + "</i>$3");
	}

	/**
	 * Ersetzt bestimmte Zeichenkette durch Bilder von Smileys
	 * 
	 * @param rstr
	 *            Suchmuster
	 * @param estr
	 *            Dateiname des Smileys
	 * @param message
	 *            Text, der durchsucht werden soll
	 * @return überarbeiteter Text, ggf. mit Bild
	 */
	private static String replaceSmileys(String rstr, String estr, String message) {
		return message.replace(rstr, " <img src='" + ChatServiceImpl.SMILEYS_PICPATH + estr + ".png' alt='" + rstr + "' /> ");
	}

	/**
	 * Liefert einen String mit Sternen
	 * 
	 * @param length
	 *            Anzahl der Sterne
	 * @return String mit Sternen
	 */
	private String getYourStars(int length) {
		final StringBuilder erg = new StringBuilder(length);
		for (int j = 0; j < length; j++) {
			erg.append("*");
		}
		return erg.toString();
	}

	/**
	 * Sendet eine farbige Nachricht
	 * 
	 * @param message
	 *            Nachricht als String
	 * @param color
	 *            Farbe
	 * @param domain
	 *            Chat-Domain
	 * @return true
	 */
	public boolean sendGeneratedRoundMessage(String message, String color, String domain) {

		final Date date = new Date();
		final DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.GERMANY);

		// Message erzeugen
		message = "<strong><font color=" + color + ">" + df.format(date) + ": " + message + "</font></strong>";

		// Event auslösen
		addEvent(DomainFactory.getDomain(domain), new ChatEvent(message, null));

		return true;
	}

	/**
	 * Zum Versenden einer Nachricht im internen Chat. Es muss immer die Domain angegeben werden!
	 * 
	 * @param message
	 *            Messagetext
	 * @param domain
	 *            Chatdomain, an welche geschickt werden soll
	 * @param nickname
	 *            Nickname des Senders
	 * @return true, falls alles geklappt hat
	 */
	@Override
	public boolean sendMessage(String message, String nickname, String domain) {
		if (message == null || message.equals("") || domain == null || domain.equals("")) {
			return false;
		}

		// Nicknamen hinzufügen
		nickname = nickname != null && !nickname.equals("") ? " " + nickname : "";

		// Schimpfwörter rausfiltern
		for (int j = 0; j < ChatServiceImpl.INVECTIVE_FILTER.length; j++) {
			message = replaceLinks(ChatServiceImpl.INVECTIVE_FILTER[j].toLowerCase(),
					getYourStars(ChatServiceImpl.INVECTIVE_FILTER[j].length()), message); // Kleinschreibung
			// testen
			message = replaceLinks(ChatServiceImpl.INVECTIVE_FILTER[j],
					getYourStars(ChatServiceImpl.INVECTIVE_FILTER[j].length()), message);
		}

		// Smileys durch Bilder ersetzen
		for (int i = 0; i < ChatServiceImpl.SMILEYS.length; i++) {
			message = replaceSmileys(ChatServiceImpl.SMILEYS[i][0], ChatServiceImpl.SMILEYS[i][1], message);
		}

		/*
		 * Text nach bestimmten Mustern durchsuchen und ersetzen :P Regulären Ausdruck: Such nach dem Suchmuster. Davor darf kein
		 * "Wortzeichen" stehen und danach muss ich Trennzeichen oder .,- stehen.
		 */
		for (int i = 0; i < ChatServiceImpl.FILTER.length; i++) {
			message = replaceLinks(ChatServiceImpl.FILTER[i][0].toLowerCase(), ChatServiceImpl.FILTER[i][1], message); // Kleinschreibung
																														// testen
			message = replaceLinks(ChatServiceImpl.FILTER[i][0], ChatServiceImpl.FILTER[i][1], message);
		}

		// Timestamp hinzufügen
		final Date date = new Date();
		final DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.GERMANY);

		// Message erzeugen
		final String tmpMes = "<strong>" + df.format(date) + nickname + "</strong>: " + message;

		// Domain erzeugen
		final Domain concreteDomain = DomainFactory.getDomain(domain);

		// Event mit der Nachricht an die richtige Domain feuern
		addEvent(concreteDomain, new ChatEvent(tmpMes, null));

		return true;
	}

	/**
	 * Sendet eine Private Nachricht
	 * 
	 * @param message
	 *            Chatnachricht
	 * @param to
	 *            Empfänger (Nickname)
	 * @param from
	 *            Sender (Nickname)
	 * @param domain
	 *            Chat-Domain
	 * 
	 * @throws RoundNotThereException Wird geworfen, wenn die Runde nicht mehr existiert
	 * @throws NumberFormatException Wird bei der Rundensuche geworfen
	 */
	@Override
	public boolean sendPrivateMessage(String message, String to, String from, String domain) throws NumberFormatException,
			RoundNotThereException {
		if (message == null || message.equals("") || domain == null || domain.equals("") || to == null || to.equals("")
				|| from == null || from.equals("")) {
			return false;
		}

		// Ist der Empfänger überhaupt online?
		if (!new LobbyImpl().getOnlineUsers().containsValue(to)) {
			return false;
		}

		// Falls es sich um einen "Round-Chat" handelt, ist der Empfänger drin?
		if (domain.charAt(0) == 'i') {
			// UserID suchen
			int userID = -1;
			final HashMap<Integer, String> tmpHashMap = new HashMap<Integer, String>(new LobbyImpl().getOnlineUsers());
			for (final Integer key : tmpHashMap.keySet()) {
				if (tmpHashMap.get(key).equals(to)) {
					userID = key;
					break;
				}
			}

			if (userID == -1) {
				return false;
			}

			// Ist der Kerl in der Round? Über den key, weil es Player/Watcher
			// sind
			if (!new RoundManager().getPlayers(Integer.valueOf(domain.substring(18))).containsKey(userID)) {
				if (!new RoundManager().getWatchers(Integer.valueOf(domain.substring(18))).containsKey(userID)) {
					return false;
				}
			}
		}

		// Schimpfwörter rausfiltern
		for (int j = 0; j < ChatServiceImpl.INVECTIVE_FILTER.length; j++) {
			message = replaceLinks(ChatServiceImpl.INVECTIVE_FILTER[j].toLowerCase(),
					getYourStars(ChatServiceImpl.INVECTIVE_FILTER[j].length()), message); // Kleinschreibung
			// testen
			message = replaceLinks(ChatServiceImpl.INVECTIVE_FILTER[j],
					getYourStars(ChatServiceImpl.INVECTIVE_FILTER[j].length()), message);
		}

		// Smileys durch Bilder ersetzen
		for (int i = 0; i < ChatServiceImpl.SMILEYS.length; i++) {
			message = replaceSmileys(ChatServiceImpl.SMILEYS[i][0], ChatServiceImpl.SMILEYS[i][1], message);
		}

		/*
		 * Text nach bestimmten Mustern durchsuchen und ersetzen :P Regulären Ausdruck: Such nach dem Suchmuster. Davor darf kein
		 * "Wortzeichen" stehen und danach muss ich Trennzeichen oder .,- stehen.
		 */
		for (int i = 0; i < ChatServiceImpl.FILTER.length; i++) {
			message = replaceLinks(ChatServiceImpl.FILTER[i][0].toLowerCase(), ChatServiceImpl.FILTER[i][1], message); // Kleinschreibung
																														// testen
			message = replaceLinks(ChatServiceImpl.FILTER[i][0], ChatServiceImpl.FILTER[i][1], message);
		}

		// Timestamp erzeugen
		Date date = new Date();
		DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.GERMANY);

		// Domain erzeugen
		Domain concreteDomain = DomainFactory.getDomain(domain);

		// Event mit der Nachricht an die richtige Domain feuern
		addEvent(concreteDomain, new PrivateChatEvent(message, from, to, df.format(date)));

		return true;
	}

	/**
	 * Zählt die Round an Wird von StartRoundChatThread bzw. startRound() verwendet.
	 * 
	 * @author timo
	 * 
	 * @param message
	 *            Chatnachricht
	 * @param color
	 *            Farbe
	 * @param domain
	 *            Chat-Domain
	 * @return true
	 */
	private boolean sendRoundMessageCount(String message, String color, String domain, String soundfile) {
		// Timestamp hinzufügen
		final Date date = new Date();
		final DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.GERMANY);

		// Message erzeugen
		message = "<strong><font color=" + color + ">" + df.format(date) + ": " + message + "</font></strong>";

		// Event auslösen
		addEvent(DomainFactory.getDomain(domain), new ChatEvent(message, soundfile));

		return true;
	}

	/**
	 * Für die erste Chatnachricht durch den Countdown. Damit der Text aus den Properties stammen kann.
	 * 
	 * @param color
	 *            Farbe
	 * @param domain
	 *            ChatDomain
	 * @return true
	 */
	private boolean sendRoundMessageCountStart(String color, String domain, String sound) {
		final Date date = new Date();
		final DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.GERMANY);

		// Event schicken
		addEvent(DomainFactory.getDomain(domain), new ChatEventCountDownStart(color, df.format(date), sound));

		return true;
	}

	/**
	 * Schreibt in den Chat, dass das Spiel in 3-2-1 Startet und Startet die Round
	 * 
	 * @param domain
	 *            Chat-Domain, round ID
	 * @return true
	 */
	public boolean startRound(int roundID, RoundManager roundManager) {

		class ChatThread extends Thread {
			/** RoundID */
			private int roundID;

			/**
			 * RoundManager
			 */
			private RoundManager roundManager;

			/**
			 * Konstruktor
			 * 
			 * @param roundID
			 */
			public ChatThread(int roundID, RoundManager roundManager) {
				this.roundID = roundID;
				this.roundManager = roundManager;
			}

			@Override
			public void run() {
				try {
					// Elemente deaktivieren (wie Start, Abbruch,..)
					addEvent(DomainFactory.getDomain("round_" + this.roundID), new DisableWidgetsEvent());
					sendRoundMessageCountStart("red", "internalChatRound:" + this.roundID, null);

					sleep(1000);
					sendRoundMessageCount("3", "red", "internalChatRound:" + this.roundID, "gamesound-piep-1.mp3");

					sleep(1000);
					sendRoundMessageCount("2", "red", "internalChatRound:" + this.roundID, "gamesound-piep-1.mp3");

					sleep(1000);
					sendRoundMessageCount("1", "red", "internalChatRound:" + this.roundID, "gamesound-piep-1.mp3");

					// Rounde starten
					if (this.roundManager.getRound(this.roundID).getRoundOption().getPlayersSlots()
							- this.roundManager.getRound(this.roundID).getFreePlayerSlots() >= 2) {
						this.roundManager.setRobotPositionAndColor(this.roundID);
					} else {
						this.roundManager.removeRound(this.roundID);
					}

					// Thread beenden
					interrupt();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		new ChatThread(roundID, roundManager).start();

		return true;
	}

	/**
	 * Schickt eine Nachricht, wenn jemand das PostGame verlassen hat.
	 * 
	 * @param nickname
	 *            Nickname des Spielers
	 * @param domain
	 *            Chatdomain
	 * 
	 * @return true
	 */
	@Override
	public boolean sendPostGameLeaveMessage(String nickname, String domain) {
		// Timestamp hinzufügen
		final Date date = new Date();
		final DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.GERMANY);

		// Domain erzeugen
		final Domain concreteDomain = DomainFactory.getDomain(domain);

		// Event mit der Nachricht an die richtige Domain feuern
		addEvent(concreteDomain, new ChatEventLeavePostGame(nickname, df.format(date), leavePostGameColor,
				"gamesound-logoff-1.mp3"));

		return true;
	}

	/**
	 * Schickt eine Nachricht, wenn jemand das PostGame betreten hat.
	 * 
	 * @param nickname
	 *            Nickname des Spielers
	 * @param domain
	 *            Chatdomain
	 * 
	 * @return true
	 */
	@Override
	public boolean sendPostGameEnterMessage(String nickname, String domain) {
		// Timestamp hinzufügen
		final Date date = new Date();
		final DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.GERMANY);

		// Domain erzeugen
		final Domain concreteDomain = DomainFactory.getDomain(domain);

		// Event mit der Nachricht an die richtige Domain feuern
		addEvent(concreteDomain, new ChatEventEnterPostGame(nickname, df.format(date), leavePostGameColor, null));

		return true;
	}
}
