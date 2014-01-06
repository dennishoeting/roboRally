package prototyp.server.view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import prototyp.client.presenter.administration.AdministrationPagePresenter;
import prototyp.client.service.AdministrationService;
import prototyp.server.lib.DBConnection;
import prototyp.server.lib.DBStatements;
import prototyp.server.util.MailServiceImpl;
import prototyp.shared.exception.administration.GotNoUserException;
import prototyp.shared.exception.administration.NoModificationException;
import prototyp.shared.exception.administration.NotAllTextFieldsFilledException;
import prototyp.shared.exception.mail.AccountLockedException;
import prototyp.shared.exception.mail.EmailAlreadyExistsException;
import prototyp.shared.exception.mail.MailNotFoundException;
import prototyp.shared.exception.registration.NicknameAlreadyExistsException;
import prototyp.shared.useradministration.AccountData;
import prototyp.shared.useradministration.User;
import prototyp.shared.util.events.user.LockUserEvent;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.service.RemoteEventServiceServlet;

/**
 * Administration, serverseitige Sicht. Hier befinden sich alle Methoden für die Presenter aus dem Administration-Package.
 * 
 * @author Andreas, Mischa
 * @version 1.3
 * @version 1.4 Viele Methoden in andere Services ausgelagert.
 * @version 1.5 OnSucces, OnFailure, Exceptions verändert
 * @version 1.6 Methode getUser gelöscht, Methoden umgeschrieben
 * 
 * @see Clientseite: {@link AdministrationService}
 */
public class AdministrationImpl extends RemoteEventServiceServlet implements AdministrationService {

	/** Seriennummer */
	private static final long serialVersionUID = 4418391142059529212L;

	/**
	 * Benutzt vom {@link AdministrationPagePresenter}.
	 * 
	 * Editiert einen Nutzer in der Datenbank. Die übergebenen Parameter werden in die Datenbank geschrieben. Zurückgegeben wird
	 * ein Integerwert den Status angibt. Es wird außerdem überprüft, ob der Nickname und die Email noch eindeutig sind.
	 * 
	 * @param nickname
	 *            Neuer Nickname
	 * @param email
	 *            Neue Emailadresse
	 * @param firstname
	 *            Neuer Vorname
	 * @param surname
	 *            Neuer Nachname
	 * @param userid
	 *            DatenbankID des Nutzers
	 * @param editor
	 *            Administrator, der die Änderungen durchgeführt hat
	 * @return true, falls alles geklappt hat
	 * @throws NotAllTextFieldsFilledException
	 *             Wird geworfen, wenn der Nickname oder die E-Mailadresse nicht vorhanden sind
	 * @throws EmailAlreadyExistsException
	 *             Wird geworfen, wenn die E-Mailadresse schon verwendet wird
	 * @throws NicknameAlreadyExistsException
	 *             Wird geworfen, wenn der Nickname schon verwendet wird
	 * @throws NoModificationException
	 *             Wird geworfen, wenn keine Änderung übernommen werden konnte.
	 */
	@Override
	public boolean editUser(String nickname, String email, String firstname, String surname, int userID, String editor)
			throws NotAllTextFieldsFilledException, EmailAlreadyExistsException, NicknameAlreadyExistsException,
			NoModificationException {
		ResultSet stmtResult = null;
		// Sind alle Pflichtfelder nicht leer?
		if (nickname.trim().equals("") || email.trim().equals("")) {
			throw new NotAllTextFieldsFilledException();
		}

		try {
			/*
			 * Überprüfen, ob eine Änderung durchgeführt werden kann
			 */
			// Ist die Emailadresse schon vorhanden?
			email = email.trim().toLowerCase();

			// PreparedStatement vorbereiten
			PreparedStatement usersStmt = DBConnection.getPstmt(DBStatements.CHECK_EMAIL_CONSTRAINT.getStmt());
			usersStmt.setString(1, email); // Email binden
			stmtResult = usersStmt.executeQuery(); // Statement ausführen
			if (stmtResult.next() && stmtResult.getInt("userID") != userID) {
				throw new EmailAlreadyExistsException();
			}
			stmtResult.close();

			// Ist der Nickname schon vorhanden?
			nickname = nickname.trim();
			// PreparedStatement vorbereiten
			usersStmt = DBConnection.getPstmt(DBStatements.CHECK_NICKNAME_CONSTRAINT.getStmt());
			usersStmt.setString(1, nickname); // Nickname binden
			stmtResult = usersStmt.executeQuery(); // Statement ausführen
			if (stmtResult.next() && stmtResult.getInt("userID") != userID) {
				throw new NicknameAlreadyExistsException();
			}
			stmtResult.close();

			/*
			 * Alte Daten holen
			 */
			// PreparedStatement vorbereiten (hier werden nicht alle Ergebnisse benötigt)
			usersStmt = DBConnection.getPstmt(DBStatements.SELECT_ACCOUNTDATA_TO_EDIT.getStmt());
			// UserID binden
			usersStmt.setInt(1, userID);

			// Statement ausführen
			stmtResult = usersStmt.executeQuery();
			String old_nickname = "";
			String old_email = "";
			String old_surname = "";
			String old_firstname = "";
			if (stmtResult.next()) {
				old_nickname = stmtResult.getString("nickname");
				old_email = stmtResult.getString("email");
				old_firstname = stmtResult.getString("firstname");
				old_surname = stmtResult.getString("surname");
			}

			/*
			 * Und nun alles eintragen - Keine Fehler vorhanden.
			 */
			firstname = firstname.trim();
			surname = surname.trim();

			// Überprüfen, ob es überhaupt Änderungen gibt.
			if (old_nickname.equals(nickname) && old_email.equals(email) && old_firstname.equals(firstname)
					&& old_surname.equals(surname)) {
				throw new NoModificationException();
			}

			// PreparedStatement vorbereiten
			usersStmt = DBConnection.getPstmt(DBStatements.UPDATE_ACCOUNTDATA.getStmt());

			// Variablen binden
			usersStmt.setString(1, nickname); // Nickname binden
			usersStmt.setString(2, email); // Email binden
			usersStmt.setString(3, firstname); // Vorname binden
			usersStmt.setString(4, surname); // Nachname binden
			usersStmt.setInt(5, userID); // Userid binden

			// Statement ausführen
			usersStmt.executeUpdate();

			/*
			 * E-Mails versenden
			 */
			try {
				// Anrede formulieren
				String name = (!firstname.equals("") && !surname.equals("")) ? "Hallo " + firstname + " " + surname + " (alias "
						+ nickname + ")" : "Hallo " + nickname;

				String subject = "Ein Administrator hat Änderungen an ihrem Account vorgenommen.";
				String content = "<p>"
						+ name
						+ ",</p>"
						+ "<p>Der Administrator ("
						+ editor
						+ ") hat &Auml;nderungen an ihrem Account vorgenommen.</p><p>Ihre <strong>alten</strong> Daten waren:</p><ul><li>Nickname: "
						+ old_nickname + "</li><li>E-Mailadresse: " + old_email + "</li><li>Vorname: " + old_firstname
						+ "</li><li>Nachname: " + old_surname
						+ "</li></ul><p>Ihre <strong>neue</strong> Daten lauten:</p><ul><li>Nickname: " + nickname
						+ "</li><li>E-Mailadresse: " + email + "</li><li>Vorname: " + firstname + "</li><li>Nachname: " + surname
						+ "</li></ul><p>Mit freundlichen Gr&uuml;&szlig;en,<br/>ihr <i>Plizzard-Team</i></p>";

				// E-Mail auch an die alte Adresse schicken, wenn diese von der neuen Adresse abweicht.
				if (!old_email.equals(email)) {
					MailServiceImpl.sendEMail(old_email, subject, content, false);
				}

				// E-Mail an die neue Adresse schicken
				MailServiceImpl.sendEMail(email, subject, content, true);
			} catch (MailNotFoundException e) {
				// Exceptions werden verschluckt.
			} catch (AccountLockedException e) {
				// Exceptions werden verschluckt.
			}
		} catch (SQLException e) {
			throw new NoModificationException();
		} finally {
			try {
				stmtResult.close();
			} catch (Exception e) {
				// verschlucken, ist nicht relevant
			}
		}

		// Es hat alles geklappt
		return true;
	}

	/**
	 * Benutzt vom {@link AdministrationPagePresenter}.
	 * 
	 * Holt sich alle User aus der Datenbank und gibt eine HashMap mit {@link User}-instanzen wieder zurück. Der Key ist die
	 * userID.
	 * 
	 * @return HashMap<Integer, User> mit Usern und als Key wird die DatenbankID genommen
	 * @throws GotNoUserException
	 *             Wird geworfen, wenn keine User gefunden werden konnten
	 */
	@Override
	public Map<Integer, User> getUsers() throws GotNoUserException {
		Map<Integer, User> users = new HashMap<Integer, User>();
		ResultSet stmtResult = null;

		try {
			// PreparedStatement vorbereiten
			PreparedStatement usersStmt = DBConnection.getPstmt(DBStatements.SELECT_USERS_TO_EDIT.getStmt());

			// Statement ausführen
			stmtResult = usersStmt.executeQuery();
			while (stmtResult.next()) {
				// AccountData des Nutzers erstellen
				AccountData accountData = new AccountData();
				accountData.setNickname(stmtResult.getString("nickname"));
				accountData.setEmail(stmtResult.getString("email"));
				accountData.setFirstname(stmtResult.getString("firstname"));
				accountData.setSurname(stmtResult.getString("surname"));
				if (stmtResult.getInt("locked") == 1) {
					accountData.setLocked(true);
				} else {
					accountData.setLocked(false);
				}

				// User erstellen und in die Liste einfügen
				users.put(stmtResult.getInt("userID"), new User(stmtResult.getInt("userID"), accountData));
			}

			// Exception werden, wenn die Liste leer ist
			if (users.size() == 0) {
				throw new GotNoUserException();
			}
		} catch (SQLException e) {
			throw new GotNoUserException();
		} finally {
			try {
				stmtResult.close();
			} catch (SQLException e) {
				// verschlucken, ist nicht relevant
			}
		}

		// HashMap zurückschicken
		return users;
	}

	/**
	 * Benutzt vom {@link AdministrationPagePresenter}.
	 * 
	 * Sperrt bzw. entsperrt einen Nutzer in der Datenbank. Die Funktion ist vom Parameter lock abhängig.
	 * 
	 * @param userid
	 *            DatenbankID des Nutzers
	 * @param lock
	 *            true, falls der Nutzer gesperrt werden soll, ansonsten false.
	 * @return true, falls alles geklappt hat
	 * @throws NoModificationException
	 *             Wird geworfen, wenn keine Änderung übernommen werden konnte.
	 */
	@Override
	public boolean lockUser(int userid, boolean lock) throws NoModificationException {
		try {
			// PreparedStatement vorbereiten
			PreparedStatement usersStmt = lock ? DBConnection.getPstmt(DBStatements.LOCK_USER.getStmt()) : DBConnection
					.getPstmt(DBStatements.UNLOCK_USER.getStmt());
			usersStmt.setInt(1, userid); // Userid binden
			usersStmt.executeUpdate(); // Statement ausführen

		} catch (SQLException e) {
			throw new NoModificationException();
		}

		// Wenn er gesperrt wurde, dann ein Event auslösen
		if (lock) {
			this.addEvent(DomainFactory.getDomain("user:" + userid), new LockUserEvent());
		}

		// Es hat alles geklappt
		return true;
	}
}
