package prototyp.server.view;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Random;

import prototyp.client.presenter.frontpage.FrontPagePresenter;
import prototyp.client.service.FrontPageService;
import prototyp.server.lib.DBConnection;
import prototyp.server.lib.DBStatements;
import prototyp.server.lib.Encrypt;
import prototyp.server.util.CheckOnlineUsersThread;
import prototyp.server.util.MailServiceImpl;
import prototyp.shared.exception.administration.NoModificationException;
import prototyp.shared.exception.frontpage.AlreadyInGameException;
import prototyp.shared.exception.frontpage.CantLoadAgbsException;
import prototyp.shared.exception.frontpage.FailedToSendMailException;
import prototyp.shared.exception.frontpage.InvalidLogInDataException;
import prototyp.shared.exception.frontpage.PasswordGenerationException;
import prototyp.shared.exception.frontpage.SameBrowserException;
import prototyp.shared.exception.frontpage.TooManyFailedLoginsException;
import prototyp.shared.exception.frontpage.TooManyGeneratedPasswordsException;
import prototyp.shared.exception.frontpage.UserAlreadyOnlineException;
import prototyp.shared.exception.frontpage.UserLockedException;
import prototyp.shared.exception.frontpage.UserNotInDataBaseException;
import prototyp.shared.exception.frontpage.WrongPasswordException;
import prototyp.shared.exception.mail.AccountLockedException;
import prototyp.shared.exception.mail.EmailAlreadyExistsException;
import prototyp.shared.exception.mail.MailNotFoundException;
import prototyp.shared.exception.registration.NicknameAlreadyExistsException;
import prototyp.shared.util.events.lobby.NewUserOnlineEvent;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.service.RemoteEventServiceServlet;

/**
 * FrontPage, serverseitige Sicht. Hier befinden sich alle Methoden für die
 * Presenter aus dem FrontPage-Package.
 * 
 * @author Andreas, Marina
 * @version 1.2 1.3, Marina, 26.09.2010: E-Mail versenden weiter bearbeitet -
 *          SMTP Server fehlt noch
 * 
 * @see Clientseite: {@link FrontPageService}
 */
public class FrontPageImpl extends RemoteEventServiceServlet implements
		FrontPageService {

	/** Domainname für das Observer-Pattern der Lobby */
	private static final Domain DOMAIN = DomainFactory
			.getDomain("lobby-domain");

	/** Seriennummer */
	private static final long serialVersionUID = -6697361503684392686L;

	/**
	 * Generiert ein 6-stelliges Passwort, verschlüsselt dieses und aktualisiert
	 * den Account des Nutzers mit dem neuen Passwort.
	 * 
	 * @param eMail
	 *            E-Mail Adresse des Empfängers
	 * @return String Neu generiertes Passwort
	 * 
	 * @see FrontPageImpl#sendNewPassword(String)
	 */
	private String generatePassword(String eMail) {
		Random random = new Random();
		String newPassword;
		char[] passArray = new char[6];

		// Passwort generieren
		for (int i = 0; i < 6; i++) {
			passArray[i] = (char) (random.nextInt(26) + 97);
		}
		newPassword = new String(passArray);

		try {
			// PreparedStatement vorbereiten
			PreparedStatement nicknameStmt = DBConnection
					.getPstmt(DBStatements.SELECT_NICKNAME_AND_USERID_BY_EMAIL
							.getStmt());

			// E-Mail an das Statement binden
			nicknameStmt.setString(1, eMail);

			// Statement ausführen
			ResultSet nicknameResult = nicknameStmt.executeQuery();
			if (nicknameResult.next()) {
				// Neues Passwort verschlüsseln
				String newEncryptedPassword = Encrypt.generatePassword(
						nicknameResult.getString("nickname"), newPassword);
				// PreparedStatement vorbereiten
				PreparedStatement updatePasswordStmt = DBConnection
						.getPstmt(DBStatements.UPDATE_PASSWORD_BY_USERID
								.getStmt());
				// Das neue verschlüsselte Passwort und die userID an das
				// Statement binden
				updatePasswordStmt.setString(1, newEncryptedPassword);
				updatePasswordStmt.setInt(2, nicknameResult.getInt("userID"));
				// Statement ausführen
				updatePasswordStmt.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newPassword;
	}

	/**
	 * Lädt die AGBs aus /other/agbs.txt Werden immer aus der txt geladen ->
	 * lohnt sich nicht so einen großen String im Speicher zu lassen.
	 * 
	 * @author timo
	 * @version 1.0
	 */
	@Override
	public String getAGBs() throws CantLoadAgbsException {

		
		final StringBuilder tmpReturn = new StringBuilder();
		InputStream fis = null;
		boolean throwExceptionFlag = false;

		try {
			// AGBs laden
			fis = getServletContext().getResourceAsStream("/other/agbs.txt");

			// Text setzen
			for (int c; (c = fis.read()) != -1;) {
				tmpReturn.append((char) c);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// Merken, dass eine Exception geworfen werden muss:
			throwExceptionFlag = true;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// Falls nicht alles geklappt hat:
		if (throwExceptionFlag) {
			throw new CantLoadAgbsException();
		}

		return tmpReturn.toString();
	}

	/**
	 * Benutzt vom {@link FrontPagePresenterPagePresenter}.
	 * 
	 * Legt einen neuen Nutzer in der Datenbank an. Gleichzeitig wird für ihn
	 * auch eine neue Statistic angelegt.
	 * 
	 * @param nickname
	 *            Nickname des Nutzers
	 * @param email
	 *            Emailadresse des Nutzers
	 * @param password
	 *            Passwort des Nutzers
	 * @param firstname
	 *            Vorname des Nutzers
	 * @param surname
	 *            Nachname des Nutzers
	 * @return true, falls alles geklappt hat
	 * @throws EmailAlreadyExistsException
	 *             Wird geworfen, wenn die E-Mailadresse schon verwendet wird
	 * @throws NicknameAlreadyExistsException
	 *             Wird geworfen, wenn der Nickname schon verwendet wird
	 * @throws NoModificationException
	 *             Wird geworfen, wenn keine Änderung übernommen werden konnte
	 * @throws PasswordGenerationException
	 *             Wird geworfen,wenn das Passwort nicht generiert werden kann
	 */
	@Override
	public boolean registerUser(String nickname, String email, String password,
			String firstname, String surname)
			throws EmailAlreadyExistsException, NicknameAlreadyExistsException,
			NoModificationException, PasswordGenerationException {
		ResultSet stmtResult = null;
		try {
			// Ist die Emailadresse schon vorhanden?
			email = email.trim().toLowerCase();
			// PreparedStatement vorbereiten
			PreparedStatement usersStmt = DBConnection
					.getPstmt(DBStatements.CHECK_EMAIL_CONSTRAINT.getStmt());
			usersStmt.setString(1, email); // Email binden
			stmtResult = usersStmt.executeQuery(); // Statement
			// ausführen
			if (stmtResult.next()) {
				throw new EmailAlreadyExistsException();
			}
			stmtResult.close();

			// Ist der Nickname schon vorhanden?
			nickname = nickname.trim();
			// PreparedStatement vorbereiten
			usersStmt = DBConnection
					.getPstmt(DBStatements.CHECK_NICKNAME_CONSTRAINT.getStmt());
			usersStmt.setString(1, nickname); // Nickname binden
			stmtResult = usersStmt.executeQuery(); // Statement ausführen
			if (stmtResult.next()) {
				throw new NicknameAlreadyExistsException();
			}
			stmtResult.close();

			// Und nun alles eintragen - Keine Fehler vorhanden.

			// Das Passwort nun verschlüsseln
			password = Encrypt.generatePassword(nickname, password);

			// Neuen Nutzer einfügen
			usersStmt = DBConnection.getPstmt(DBStatements.INSERT_NEW_USER
					.getStmt());
			usersStmt.setString(1, nickname); // Nickname binden
			usersStmt.setString(2, email); // Email binden
			usersStmt.setString(3, password); // Password binden
			usersStmt.setString(4, firstname.trim()); // Vorname binden
			usersStmt.setString(5, surname.trim()); // Nachname binden
			usersStmt.executeUpdate(); // Statement ausführen

			usersStmt = DBConnection
					.getPstmt(DBStatements.CHECK_NICKNAME_CONSTRAINT.getStmt());
			usersStmt.setString(1, nickname); // Nickname binden
			stmtResult = usersStmt.executeQuery(); // Statement ausführen
			if (stmtResult.next()) {
				int userID = stmtResult.getInt("userID");
				// Neue Statistic einfügen
				usersStmt = DBConnection
						.getPstmt(DBStatements.INSERT_NEW_STATISTIC.getStmt());
				usersStmt.setInt(1, userID); // Nickname binden
				usersStmt.executeUpdate(); // Statement ausführen

				// Neue Statistic für die LastRounds einfügen
				usersStmt = DBConnection
						.getPstmt(DBStatements.INSERT_NEW_LASTROUNDS.getStmt());
				usersStmt.setInt(1, userID); // Nickname binden
				usersStmt.executeUpdate(); // Statement ausführen
			}

			// E-Mail senden
			String subject = "Willkommen bei RoboRally!";
			String content = "<p>Willkommen</p>"
					+ nickname
					+ ",</p>"
					+ "<p>Ein Administrator hat Änderungen an ihrem Account vorgenommen. Ihre neue Daten lauten:</p><ul><li>Nickname: "
					+ nickname + "</li><li>E-Mailadresse: " + email
					+ "</li><li>Vorname: " + firstname + "</li><li>Nachname: "
					+ surname + "</li></ul>";

			MailServiceImpl.sendEMail(email, subject, content, true);

		} catch (MailNotFoundException e) {
			// Exceptions werden verschluckt.
		} catch (AccountLockedException e) {
			// Exceptions werden verschluckt.
		} catch (PasswordGenerationException e) {
			throw new PasswordGenerationException();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NoModificationException();
		} finally {
			try {
				stmtResult.close();
			} catch (SQLException e) {
				// verschlucken, ist nicht relevant
			}
		}

		return true;
	}

	/**
	 * Benutzt vom {@link FrontPagePresenter}.
	 * 
	 * Verschickt ein neues Passwort per Email an die übergebene E-Mailadresse
	 * und trägt in die Datenbank die Zeit ein.
	 * 
	 * @param recipient
	 *            E-Mail Adresse
	 * @return true, wenn alles geklappt hat
	 * @throws MailNotFoundException
	 *             Wird geworfen, wenn die E-Mail Adresse nicht existiert
	 * @throws FailedToSendMailException
	 *             Wird geworfen, wenn die E-Mail nicht versandt werden konnte
	 * @throws TooManyGeneratedPasswordsException
	 *             Wird geworfen, wenn zu viele Passwoerter generiert worden
	 *             sind
	 * @throws AccountLockedException
	 *             Wird geworfen, wenn der Account gesperrt ist
	 * @throws NoModificationException
	 *             Wird geworfen, wenn keine Änderung übernommen werden konnte
	 */
	@Override
	public boolean sendNewPassword(String recipient)
			throws AccountLockedException, MailNotFoundException,
			FailedToSendMailException, TooManyGeneratedPasswordsException,
			NoModificationException {
		ResultSet stmtResult = null;

		try {
			if (!MailServiceImpl.validateEMail(recipient)) {
				throw new FailedToSendMailException();
			}
			// Darf der User ein neues Passwort updaten?
			PreparedStatement checkPasswordtime = DBConnection
					.getPstmt(DBStatements.CHECK_NEWPASSWORDTIME.getStmt());
			checkPasswordtime.setString(1, recipient);
			stmtResult = checkPasswordtime.executeQuery();
			if (!stmtResult.next()) {
				throw new TooManyGeneratedPasswordsException();
			}
			stmtResult.close();

			String content = "<p>Liebe Mitspielerin, lieber Mitspieler,</p>"
					+ "<p>Das Kennwort der registrierten E-Mail Adresse "
					+ recipient
					+ " wurde erfolgreich zur&uuml;ckgesetzt.<br>"
					+ "Wenn Sie glauben, dass Sie diese E-Mail aus Versehen erhalten haben oder eine unbefugte Person auf Ihrem Account zugegriffen hat, "
					+ "besuchen Sie bitte unsere RoboCraft-Seite www.RoboCraft.de und setzen Ihr Kennwort sofort zur&uuml;ck.</p>"
					+ "<p>Ihr neues Kennwort lautet: <b>"
					+ generatePassword(recipient) + "</b></p>";

			String subject = "Das Kennwort für ihren RoboCraft Account wurde neu gesetzt.";

			if (MailServiceImpl.sendEMail(recipient, subject, content, false)) {
				// Zeit eintragen
				// PreparedStatement vorbereiten
				PreparedStatement updateStmt = DBConnection
						.getPstmt(DBStatements.UPDATE_NEWPASSWORDTIME.getStmt());
				// E-Mail an das Statement binden
				updateStmt.setString(1, recipient);
				// Statement ausführen
				updateStmt.executeUpdate();
			} else {
				throw new FailedToSendMailException();
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
		return true;
	}

	/**
	 * Benutzt vom {@link FrontPagePresenter}.
	 * 
	 * Überprüft die Benutzereingaben (User vorhanden, locked). Nickname und
	 * Passwort werden mit der Datenbank abgeglichen und ein Code als String
	 * zurückgegeben.
	 * 
	 * @param nickname
	 *            Vom Benutzer eingegebener Nickname
	 * @param password
	 *            Vom Benutzer eingegebenes Password
	 * @return Boolean, true falls erfolgreich, sonst false
	 * 
	 * @throws WrongPasswordException
	 *             , wenn Passwort falsch ist TooManyFailedLoginsException, wenn
	 *             zuviele fehlgeschlagene LogIn Versuche vorhanden sind
	 *             UserLockedException, wenn der User gesperrt ist
	 *             UserAlreadyOnlineException, wenn User schon online ist
	 *             InvalidLogInDataException, wenn Login Daten ungültig sind
	 *             SQLException, falls ein DB Fehler auftritt
	 *             PasswordGenerationException, falls ein Fehler beim Erstellen
	 *             des Passwords auftritt
	 * 
	 *             Alt: Wird noch entfernt: --Kamil Integer. Dabei steht 1 =
	 *             Korrekter Login, 2 Ungültiger LogIn, 3 = Benutzer gesperrt, 4
	 *             = User hat sich zu oft falsch eingeloggt , 5 = User bereits
	 *             eingeloggt 100 = Keine Datenbankverbindung/Genereller
	 * @throws AlreadyInGameException
	 * @throws UserNotInDataBaseException
	 */
	@Override
	public Boolean validateLogIn(String nickname, String password, int userIdCookie)
			throws WrongPasswordException, TooManyFailedLoginsException,
			UserLockedException, UserAlreadyOnlineException,
			InvalidLogInDataException, PasswordGenerationException,
			AlreadyInGameException, UserNotInDataBaseException, SameBrowserException {
		
		// Wenn im Cookie eine UserId steht, schauen ob der User noch online ist
		if(userIdCookie != -1) {
			for(Integer key : LobbyImpl.getThreadSafeOnlineUsers().keySet()) {
				if(key.equals(userIdCookie)) {
					/*
					 * Falls er noch online ist, dann wohl zwei mal mit einem
					 * Browser! Böse!!
					 */
					throw new SameBrowserException();
				}
			}
		}
		
		boolean result = false;
		boolean valid = false;
		int userID = -1;

		ResultSet logInResult = null;

		// Ermitteln des aktuellen Datums und der akt. Uhrzeit
		Calendar cal = Calendar.getInstance();
		cal.setTime(new java.util.Date());
		java.sql.Date now = new java.sql.Date(cal.getTime().getTime());

		try {
			// PreparedStatement für die LogIn Daten vorbereiten
			PreparedStatement logInDataStmt = DBConnection
					.getPstmt(DBStatements.SELECT_LOGIN_DATA.getStmt());
			logInDataStmt.setString(1, nickname);

			// Statement für die LogIn Daten ausführen
			logInResult = logInDataStmt.executeQuery();
			if (logInResult.next()) {

				userID = logInResult.getInt("userID");
				// User ist nicht eingelogt und nicht gesperrt
				if (new LobbyImpl().getOnlineUsers().get(userID) != null) {
					throw new AlreadyInGameException();
				}
				if (logInResult.getString("locked").equals("0")) {
					// Prüfen ob sich der User schon mehrmals falsch eingeloggt
					// hat.
					if (logInResult.getInt("failedLogInCount") >= 3
							&& now.getTime() <= logInResult.getTimestamp(
									"failedLogInTime").getTime()) {
						throw new TooManyFailedLoginsException();
					} else {

						// Eingetragenes Passwort verschlüsseln und auf
						// Gleichheit prüfen (1=korrekt, 2=falsch)
						String encrytedPassword = Encrypt.generatePassword(
								nickname, password);
						valid = logInResult.getString("password").equals(
								encrytedPassword) ? true : false;

						if (!valid) {
							// Fehlversuche erhöhen
							PreparedStatement incFailedLogInCountStmt = DBConnection
									.getPstmt(DBStatements.UPDATE_FAILED_LOGIN_COUNT
											.getStmt());
							incFailedLogInCountStmt.setInt(1,
									logInResult.getInt("failedLogInCount") + 1);
							incFailedLogInCountStmt.setInt(2, userID);
							incFailedLogInCountStmt.executeUpdate();
							throw new WrongPasswordException();
						}
					}
				} else {
					throw new UserLockedException();
				}

				// Falls gültiger LogIn, User als Online kennzeichnen
				if (valid) {

					// User in die Collection der online Users setzen
					new LobbyImpl().putOnlineUser(userID, nickname);

					// Thread ausführen, falls noch nicht getan
					CheckOnlineUsersThread.startThread();

					// NewUserOnlineEvent werden
					addEvent(FrontPageImpl.DOMAIN, new NewUserOnlineEvent(
							userID, nickname));

					if (logInResult.getInt("failedLogInCount") > 0) {
						// Fehlversuche zurücksetzen falls vorhanden
						PreparedStatement resetFailedLogIns = DBConnection
								.getPstmt(DBStatements.UPDATE_RESET_FAILED_LOGIN_COUNT
										.getStmt());
						resetFailedLogIns.setInt(1, userID);
						resetFailedLogIns.executeUpdate();
					}
				}
				logInResult.close();
			} else {
				throw new UserNotInDataBaseException();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (logInResult != null) {
					logInResult.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}
}