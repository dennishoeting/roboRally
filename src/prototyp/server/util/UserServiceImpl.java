package prototyp.server.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import prototyp.client.service.UserService;
import prototyp.server.lib.DBConnection;
import prototyp.server.lib.DBStatements;
import prototyp.server.model.RoundManager;
import prototyp.server.view.LobbyImpl;
import prototyp.shared.exception.frontpage.UserNotInDataBaseException;
import prototyp.shared.useradministration.AccountData;
import prototyp.shared.useradministration.User;
import prototyp.shared.util.events.chat.ChatEventEnterGame;
import prototyp.shared.util.events.chat.ChatEventLeaveGame;
import prototyp.shared.util.events.lobby.RemoveUserOnlineEvent;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.service.RemoteEventServiceServlet;

/**
 * Implementierung der Services für den UserPresenter
 * 
 * @author timo
 * @version 1.0
 * @version 1.1 onClosing() hinzugefügt.
 */
public class UserServiceImpl extends RemoteEventServiceServlet implements UserService {

	/*
	 * Domains für den EventService
	 */

	/** Domain für den globalen Chat */
	private static final Domain GLOBALCHATDOMAIN = DomainFactory.getDomain("global-chat");

	/** Domain für den Chat in der Lobby */
	private static final Domain LOBBYDOMAIN = DomainFactory.getDomain("lobby-domain");
	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -2808763993529607069L;

	/**
	 * Ändert ein User-Objekt in der DB.
	 * 
	 * @param nickname
	 *            Nickname
	 * @param email
	 *            E-Mailadresse
	 * @param firstname
	 *            Vorname
	 * @param surname
	 *            Nachname
	 * @param userID
	 *            UserID
	 * @return boolean Angabe, ob es geklappt hat
	 */
	@Override
	public boolean editUser(String nickname, String email, String firstname, String surname, int userID) {
		try {
			// PreparedStatement vorbereiten
			PreparedStatement usersStmt = DBConnection.getPstmt(DBStatements.UPDATE_ACCOUNTDATA.getStmt());

			// Variablen binden
			usersStmt.setString(1, nickname); // Nickname binden
			usersStmt.setString(2, email); // Email binden
			usersStmt.setString(3, firstname); // Vorname binden
			usersStmt.setString(4, surname); // Nachname binden
			usersStmt.setInt(5, userID); // Userid binden

			// Statement ausführen
			usersStmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Holt mittels des Nicknames ein Userobjekt aus der DB
	 * 
	 * @param nickname
	 *            Nickname
	 * @return User
	 */
	@Override
	public User getUser(String nickname) throws UserNotInDataBaseException {
		User user = null;

		try {
			// PreparedStatement vorbereiten
			PreparedStatement usersStmt = DBConnection.getPstmt(DBStatements.SELECT_ACCOUNTDATA_TO_EDIT_BY_NICKNAME.getStmt());

			// Nickname binden
			usersStmt.setString(1, nickname);

			// Statement ausführen
			ResultSet stmtResult = usersStmt.executeQuery();
			if (stmtResult.next()) {
				// AccountData des Nutzers erstellen
				AccountData accountData = new AccountData();
				int userid = stmtResult.getInt("userID");
				accountData.setNickname(nickname);
				accountData.setEmail(stmtResult.getString("email"));
				accountData.setFirstname(stmtResult.getString("firstname"));
				accountData.setSurname(stmtResult.getString("surname"));
				accountData.setPassword(stmtResult.getString("password"));
				if (stmtResult.getInt("locked") == 1) {
					accountData.setLocked(true);
				} else {
					accountData.setLocked(false);
				}

				// User erstellen
				user = new User(userid, accountData);

				// Admin Rechte?
				if (stmtResult.getInt("isAdmin") == 1) {
					user.setAdmin(true);
				} else {
					user.setAdmin(false);
				}
			}
			stmtResult.close(); // Result schließen

		} catch (Exception e) {
			e.printStackTrace();
			throw new UserNotInDataBaseException();
		}

		return user;
	}

	/**
	 * Diese Methode wird aufgerufen wenn der Browser geschlossen wird! Hier kann alles rein was Serverseitig noch passieren soll.
	 * Beispielsweise ein PlayerManagerEvent auslösen, ein ChatEventAuslösen.
	 * 
	 * @return true
	 */
	@Override
	public boolean onClosing(User user, List<Domain> chatServiceDomains, List<Integer> rounds) {

		// ChatEvents für alle geöffneten Chats auslösen

		// Message zum Verlassen:
		Date date = new Date();
		DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.GERMANY);

		// Alle angemeldeten Chatdomänen durchgehen
		for (int i = 0; i < chatServiceDomains.size(); i++) {
			addEvent(chatServiceDomains.get(i), new ChatEventLeaveGame(user.getAccountData().getNickname(), df.format(date),
					"red", null));
		}

		// Alle aktiven Runden durchgehen
		for (Integer roundID : rounds) {
			try {
				new RoundManager().removePlayerOnUnload(roundID, user.getId());
			} catch (Exception e) {
				// Verschlucken -> kann eh nicht drauf eingegangen werden.
			}
		}

		// Aus der Collection der online User löschen.
		new LobbyImpl().removeOnlineUser(user.getId());
		LobbyImpl.removeOnlineUsersTimeStamp(user.getId());

		// LobbyEvent für das ListGrid der eingeloggten User senden
		addEvent(UserServiceImpl.LOBBYDOMAIN, new RemoveUserOnlineEvent(user.getId()));

		return true;
	}

	/**
	 * Schreibt eine LogIn Nachricht in den Chat
	 * 
	 * @param nickname
	 *            Nickname des Users
	 * @return true
	 */
	@Override
	public boolean sendChatLogIn(String nickname) {
		// Zeit bestimmen
		Date date = new Date();
		DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.GERMANY);

		// Nachricht schicken
		addEvent(UserServiceImpl.GLOBALCHATDOMAIN, new ChatEventEnterGame(nickname, "red", df.format(date), "gamesound-logon-1.mp3"));

		return true;
	}
}
