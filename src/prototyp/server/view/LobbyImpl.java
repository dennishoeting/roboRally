package prototyp.server.view;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import prototyp.client.presenter.lobby.HighScorePagePresenter;
import prototyp.client.service.LobbyService;
import prototyp.server.lib.DBConnection;
import prototyp.server.lib.DBStatements;
import prototyp.server.lib.Encrypt;
import prototyp.shared.exception.administration.NoModificationException;
import prototyp.shared.exception.administration.NotAllTextFieldsFilledException;
import prototyp.shared.exception.frontpage.PasswordGenerationException;
import prototyp.shared.exception.frontpage.WrongPasswordException;
import prototyp.shared.exception.lobby.PhotoUploadException;
import prototyp.shared.exception.lobby.Unable2LoadUserException;
import prototyp.shared.exception.lobby.UsersBrowserCrashedException;
import prototyp.shared.exception.mail.EmailAlreadyExistsException;
import prototyp.shared.exception.registration.NicknameAlreadyExistsException;
import prototyp.shared.useradministration.AccountData;
import prototyp.shared.useradministration.Award;
import prototyp.shared.useradministration.Statistic;
import prototyp.shared.useradministration.User;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Lobby, serverseitige Sicht. Hier befinden sich alle Methoden für die Presenter aus dem Lobby-Package.
 * 
 * @author Andreas, Robert
 * @version 1.0
 * @version 1.1 getUsersWithStatistic(), editUser(..), getUser(String nickname) Timo
 * @version 1.2 getUserAwards (30.09.10, Robert)
 * @version 1.3 activeUsers beinhaltet alle eingelogten User (21.10.10 Timo)
 * 
 * @see Clientseite: {@link LobbyService}
 */
public class LobbyImpl extends RemoteServiceServlet implements LobbyService {

	/** Seriennummer */
	private static final long serialVersionUID = 6271420445518165016L;

	/**
	 * Enthält alle User, die gerade online sind. Static damit es von anderen Klassen auf dem Server verwendet werden kann.
	 * Änderungen nur über LobbyImpl.putOnlineUser bzw. LobbyImpl.removeOnlineUser -> Wegen dem synchronized.
	 * 
	 * @author timo
	 * @return Map<Integer, String> Integer = UserID String = Nickname
	 */
	private static Map<Integer, String> onlineUsers = new HashMap<Integer, String>();

	/**
	 * Speichert für jeden aktiven User die letzte Zeit, zu der er sich zurück gemeldet hat.
	 * 
	 * @author timo
	 * @return Map<Integer, Long>
	 */
	private static Map<Integer, Long> onlineUsersTimeStamps = new HashMap<Integer, Long>();

	/**
	 * Liefert die TimeStamps, der User, die Online sind.
	 * 
	 * @return Map<Integer, Long>
	 */
	public static synchronized Map<Integer, Long> getOnlineUsersTimeStamps() {
		return LobbyImpl.onlineUsersTimeStamps;
	}

	/**
	 * Liefert die online User, thread sicher
	 * 
	 * @return Map<Integer, String>
	 */
	public static synchronized Map<Integer, String> getThreadSafeOnlineUsers() {
		return LobbyImpl.onlineUsers;
	}

	/**
	 * Löscht einen User aus der OnlineUsersTimeStamps Liste.
	 * 
	 * @param userID
	 * @return true, falls der Nutzer richtig gelöscht wurde
	 */
	public static synchronized boolean removeOnlineUsersTimeStamp(int userID) {
		return LobbyImpl.onlineUsersTimeStamps.remove(userID) != null;
	}

	/**
	 * Online User Thread sicher entfernen -> static damit es serverseitig verwendet werden kann
	 * 
	 * @param userID
	 * @return true, falls der Nutzer richtig gelöscht wurde
	 * @author timo
	 */
	public static synchronized boolean removeOnlineUserThreadSafe(int userID) {
		return LobbyImpl.onlineUsers.remove(userID) != null;
	}

	/**
	 * Sperrt/Entsperrt einen User und gibt das neue User-Objekt zurück.
	 * 
	 * @param userID
	 *            DatenbankID des Nutzers
	 * @param lock
	 *            Angabe, ob der User gesperrt oder entsperrt werden soll
	 * 
	 * @return User-Objekt
	 * 
	 * @throws Unable2LoadUserException
	 *             wird geworfen wenn ein User nicht geladen werden kann (in Fkt: getUser())
	 */
	@Override
	public User editLocked(int userID, boolean lock) throws Unable2LoadUserException {
		try {
			// PreparedStatement vorbereiten
			PreparedStatement usersStmt;

			if (lock) {
				usersStmt = DBConnection.getPstmt(DBStatements.LOCK_USER.getStmt());
			} else {
				usersStmt = DBConnection.getPstmt(DBStatements.UNLOCK_USER.getStmt());
			}
			// Variablen binden
			usersStmt.setInt(1, userID);
			// Statement ausführen
			usersStmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return getUser(userID);
	}

	/**
	 * Ändert ein User-Objekt in der DB und gibt das neue User-Objekt zurück. Gleichzeitig wird das Password neu gesetzt.
	 * 
	 * @param oldnickname
	 *            Alter Nickname -> Wird für die Passwortüberprüfung benötigt
	 * @param password
	 *            Password des Nutzers
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
	 * @return true, falls alles geklappt hat
	 * @throws NotAllTextFieldsFilledException
	 *             Wird geworfen, wenn der Nickname oder die E-Mailadresse nicht vorhanden sind
	 * @throws EmailAlreadyExistsException
	 *             Wird geworfen, wenn die E-Mailadresse schon verwendet wird
	 * @throws NicknameAlreadyExistsException
	 *             Wird geworfen, wenn der Nickname schon verwendet wird
	 * @throws NoModificationException
	 *             Wird geworfen, wenn keine Änderung übernommen werden konnte.
	 * @throws PasswordGenerationException
	 *             Wird geworfen, wenn das Passwort nicht generiert werden kann
	 * @throws WrongPasswordException
	 *             Wird geworfen, wenn das übergebene Passwort nicht mit dem vorhanden Passwort übereinstimmt
	 */
	@Override
	public boolean editUserWithNewNickname(String oldnickname, String nickname, String password, String email, String firstname,
			String surname, int userID) throws NotAllTextFieldsFilledException, EmailAlreadyExistsException,
			NicknameAlreadyExistsException, NoModificationException, WrongPasswordException, PasswordGenerationException {
		// Sind alle Pflichtfelder nicht leer?
		if (nickname.trim().equals("") || email.trim().equals("")) {
			throw new NotAllTextFieldsFilledException();
		}

		ResultSet stmtResult = null;
		try {
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

			// Neues Passwort speichern
			editUserPassword(password, password, oldnickname, nickname, userID);

			// Und nun alles eintragen - Keine Fehler vorhanden.

			// PreparedStatement vorbereiten
			usersStmt = DBConnection.getPstmt(DBStatements.UPDATE_ACCOUNTDATA.getStmt());

			// Variablen binden
			usersStmt.setString(1, nickname); // Nickname binden
			usersStmt.setString(2, email); // Email binden
			usersStmt.setString(3, firstname.trim()); // Vorname binden
			usersStmt.setString(4, surname.trim()); // Nachname binden
			usersStmt.setInt(5, userID); // Userid binden

			// Statement ausführen
			usersStmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
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
	 * Ändert ein User-Objekt in der DB und gibt das neue User-Objekt zurück.
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
	public boolean editUser(String nickname, String email, String firstname, String surname, int userID)
			throws NotAllTextFieldsFilledException, EmailAlreadyExistsException, NicknameAlreadyExistsException,
			NoModificationException {
		// Sind alle Pflichtfelder nicht leer?
		if (nickname.trim().equals("") || email.trim().equals("")) {
			throw new NotAllTextFieldsFilledException();
		}

		ResultSet stmtResult = null;
		try {
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

			// Und nun alles eintragen - Keine Fehler vorhanden.

			// PreparedStatement vorbereiten
			usersStmt = DBConnection.getPstmt(DBStatements.UPDATE_ACCOUNTDATA.getStmt());

			// Variablen binden
			usersStmt.setString(1, nickname); // Nickname binden
			usersStmt.setString(2, email); // Email binden
			usersStmt.setString(3, firstname.trim()); // Vorname binden
			usersStmt.setString(4, surname.trim()); // Nachname binden
			usersStmt.setInt(5, userID); // Userid binden

			// Statement ausführen
			usersStmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
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
	 * Ändert das Passwort des Nutzer in der Datenbank.
	 * 
	 * @param oldPassword
	 *            Altes Passwort
	 * @param newPassword
	 *            Neues Passwort
	 * @param nickname
	 *            Alter Nickname
	 * @param nickname
	 *            Neuer Nickname
	 * @param userID
	 *            UserID aus der Datenbank
	 * @return true, falls alles geklappt hat
	 * @throws NotAllTextFieldsFilledException
	 *             Wird geworfen, wenn der Nickname oder die E-Mailadresse nicht vorhanden sind
	 * @throws NoModificationException
	 *             Wird geworfen, wenn keine Änderung übernommen werden konnte.
	 * @throws WrongPasswordException
	 *             Wird geworfen, wenn das Passwort falsch eingegeben wurde
	 * @throws PasswordGenerationException
	 *             Wird geworfen, wenn das Passwort nicht generiert werden kann
	 */
	@Override
	public boolean editUserPassword(String oldPassword, String newPassword, String oldNickname, String nickname, int userID)
			throws NotAllTextFieldsFilledException, NoModificationException, WrongPasswordException, PasswordGenerationException {
		// Sind alle Pflichtfelder nicht leer?
		if (oldPassword.trim().equals("") || newPassword.trim().equals("")) {
			throw new NotAllTextFieldsFilledException();
		}
		ResultSet stmtResult = null;
		try {
			// Altes Passwort verschlüsseln
			String oldEncryptedPassword = Encrypt.generatePassword(oldNickname, oldPassword.trim());
			// PreparedStatement vorbereiten
			PreparedStatement usersStmt = DBConnection.getPstmt(DBStatements.SELECT_NICKNAME_BY_PASSWORD.getStmt());
			usersStmt.setString(1, oldEncryptedPassword); // Altes Passwort binden
			usersStmt.setInt(2, userID); // UserID binden
			stmtResult = usersStmt.executeQuery(); // Statement ausführen
			if (stmtResult.next()) {
				// Neues Passwort verschlüsseln
				String newEncryptedPassword = Encrypt.generatePassword(nickname, newPassword.trim());
				// PreparedStatement vorbereiten
				PreparedStatement updatePasswordStmt = DBConnection.getPstmt(DBStatements.UPDATE_PASSWORD_BY_USERID.getStmt());

				// Variablen binden
				updatePasswordStmt.setString(1, newEncryptedPassword);
				updatePasswordStmt.setInt(2, userID);

				updatePasswordStmt.executeUpdate(); // Statement ausführen
			} else {
				throw new WrongPasswordException();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmtResult.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Wenn alles geklappt hat
		return true;
	}

	/**
	 * Liefert alle User, die gerade online sind.
	 * 
	 * @author timo
	 * @return Map<Integer, nickname>
	 */
	@Override
	public Map<Integer, String> getOnlineUsers() {
		return getThreadSafeOnlineUsers();
	}

	/**
	 * Holt den User mittels der userID aus der DB und gibt ein User-Objekt zurück
	 * 
	 * @param userID
	 *            DatenbankID des Nutzers
	 * @return User-Objekt
	 */
	@Override
	public User getUser(int userID) throws Unable2LoadUserException {
		User user = null;
		ResultSet stmtResult = null;

		try {
			// PreparedStatement vorbereiten
			PreparedStatement usersStmt = DBConnection.getPstmt(DBStatements.SELECT_ACCOUNTDATA_TO_EDIT.getStmt());

			// Nickname binden
			usersStmt.setInt(1, userID);

			// Statement ausführen
			stmtResult = usersStmt.executeQuery();
			if (stmtResult.next()) {
				// AccountData des Nutzers erstellen
				AccountData accountData = new AccountData();
				accountData.setNickname(stmtResult.getString("nickname"));
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
				user = new User(userID, accountData);

				// Admin Rechte?
				if (stmtResult.getInt("isAdmin") == 1) {
					user.setAdmin(true);
				} else {
					user.setAdmin(false);
				}
			} else {
				throw new Unable2LoadUserException();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// throw new Throwable();
		} finally {
			try {
				if (stmtResult != null) {
					stmtResult.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return user;
	}

	/**
	 * Holt mittels des Nicknames ein Userobjekt aus der DB
	 * 
	 * @param nickname
	 *            Nickname des Nutzers
	 * @return User-Objekt
	 */
	@Override
	public User getUser(String nickname) throws Unable2LoadUserException {
		User user = null;
		ResultSet stmtResult = null;

		try {
			// PreparedStatement vorbereiten
			PreparedStatement usersStmt = DBConnection.getPstmt(DBStatements.SELECT_ACCOUNTDATA_TO_EDIT_BY_NICKNAME.getStmt());

			// Nickname binden
			usersStmt.setString(1, nickname);

			// Statement ausführen
			stmtResult = usersStmt.executeQuery();
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
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (stmtResult != null) {
					stmtResult.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return user;
	}

	/**
	 * Liefert alle UserAwards zu der übergebenen DatenbankID des Nutzers
	 * 
	 * @param userID
	 *            DatenbankID des Nutzer
	 * 
	 * @return ArrayList mit Awards
	 */
	@Override
	public List<Award> getUserAwards(int userID) {
		List<Award> awards = new ArrayList<Award>();
		ResultSet stmtResult = null;

		try {
			// PreparedStatement vorbereiten
			PreparedStatement awardsStmt = DBConnection.getPstmt(DBStatements.SELECT_USER_STATISTICAWARDS.getStmt());

			// userID binden
			awardsStmt.setInt(1, userID);

			// Statement ausführen
			stmtResult = awardsStmt.executeQuery();
			while (stmtResult.next()) {
				// Award x des Nutzers erstellen
				Award award = new Award(stmtResult.getString("name"), stmtResult.getString("description"),
						stmtResult.getString("imageFileName"));
				awards.add(award);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmtResult != null) {
					stmtResult.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return awards;
	}

	/**
	 * Liefert den Path zum User Foto
	 */
	@Override
	public String getUserPicture(int userID) {
		// Falls das Bild nicht da ist -> Standartbild
		if (!new File(getServletContext().getRealPath("/") + "/images/userphotos/" + userID + ".png").exists()) {
			return "userphotos/default.png";
		}

		return "userphotos/" + userID + ".png";
	}

	/**
	 * Liefert die Statistik zu einem User
	 * 
	 * @param userID
	 *            UserID (DatenbankID)
	 * @return Userstatistik
	 */
	public Statistic getUserStatistic(int userID) {
		// Statistik: dazu eine Map mit den Usern holen (damit der Rang stimmt)
		Map<Integer, User> allUsers = getUsersWithStatistic();

		return allUsers.get(userID).getAccountData().getStatistic();
	}

	/**
	 * Benutzt vom {@link HighScorePagePresenter}.
	 * 
	 * Holt sich alle User, mit ihrer Statistic, aus der Datenbank als HashMap mit {@link User}-instanzen wieder zurück.
	 * 
	 * @return HashMap mit Userobjekten
	 */
	@Override
	public Map<Integer, User> getUsersWithStatistic() {
		Map<Integer, User> users = new HashMap<Integer, User>();
		ResultSet stmtResult = null;

		try {
			// PreparedStatement vorbereiten
			PreparedStatement usersStmt = DBConnection.getPstmt(DBStatements.SELECT_USERS_WITH_STATISTIC.getStmt());

			// Statement ausführen
			stmtResult = usersStmt.executeQuery();

			// DB Abfrage ist sortiert nach Wins. Dh. der erste Eintrag ist Rank
			// 1 usw.
			int rank = 1;

			while (stmtResult.next()) {
				// AccountData des Nutzers erstellen
				AccountData accountData = new AccountData();
				accountData.setNickname(stmtResult.getString("nickname"));
				accountData.setEmail(stmtResult.getString("email"));
				accountData.setFirstname(stmtResult.getString("firstname"));
				accountData.setSurname(stmtResult.getString("surname"));
				accountData.setStatistic(new Statistic(stmtResult.getInt("wins"), rank, stmtResult.getInt("playedRounds"),
						stmtResult.getInt("abortedRounds"), stmtResult.getInt("points"), stmtResult.getString("rankname")));

				// Locked?
				if (stmtResult.getInt("locked") == 0) {
					accountData.setLocked(false);
				} else {
					accountData.setLocked(true);
				}

				// User erstellen
				User tmpUser = new User(stmtResult.getInt("userID"), accountData);

				// Admin?
				if (stmtResult.getInt("isAdmin") == 1) {
					tmpUser.setAdmin(true);
				} else {
					tmpUser.setAdmin(false);
				}

				// User in die Liste
				users.put(tmpUser.getId(), tmpUser);

				// Rank erhöhen
				rank++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (stmtResult != null) {
					stmtResult.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return users;
	}

	/**
	 * Soll jede Minute in etwa vom Client aufgerufen werden und dem Server zeigen, dass er noch da ist.
	 * 
	 * @author timo
	 * @param userID
	 * @return true
	 */
	@Override
	public synchronized boolean giveAliveSignal(int userID) throws UsersBrowserCrashedException {
		// Wenn kein alter Wert besteht -> Exception
		if (LobbyImpl.onlineUsersTimeStamps.get(userID) == null) {
			throw new UsersBrowserCrashedException();
		}

		// Falls bereits einer existiert -> überschreiben.
		return LobbyImpl.onlineUsersTimeStamps.put(userID, System.currentTimeMillis()) != null;
	}

	/**
	 * Fügt einen User in die online Collection ein. Nicht direkt über die Collection aufrufen, wegen dem synchronized.
	 * 
	 * @author timo
	 * @param userID
	 * @param nickname
	 * @return true wenn es klappte, sonst false
	 */
	public synchronized boolean putOnlineUser(int userID, String nickname) {
		if (LobbyImpl.onlineUsersTimeStamps.put(userID, System.currentTimeMillis()) == null) {
			return LobbyImpl.onlineUsers.put(userID, nickname) == null;
		} else {
			return false;
		}

	}

	/**
	 * Löscht einen User aus der online Collection. Nicht direkt über die Collection aufrufen, wegen dem synchronized.
	 * 
	 * @author timo
	 * @param userID
	 * @return true wenn es klappte, sonst false
	 */
	public boolean removeOnlineUser(int userID) {
		return removeOnlineUserThreadSafe(userID);
	}

	/**
	 * Holt das User Foto aus images/temp/, benennt es richtig, verschiebt es in images/userphotos und ändert die größe
	 * 
	 * @author timo
	 */
	@Override
	public String setUserPicture(int userID, String imageName) throws PhotoUploadException {

		try {
			if (!new File(getServletContext().getRealPath("/") + "/images/temp" + File.separator + imageName).exists()) {
				throw new PhotoUploadException();
			}

			// Bild laden
			File file = new File(getServletContext().getRealPath("/") + "/images/temp/" + imageName);
			Image tmpImage = ImageIO.read(file);

			// Größe anpassen: 200x200px
			tmpImage = tmpImage.getScaledInstance(200, 200, 0);
			BufferedImage bImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = bImage.createGraphics();
			g2.drawImage(tmpImage, 0, 0, null);

			// Bild schreiben -> images/userphotos/userID.png
			File newFile = new File(getServletContext().getRealPath("/") + "/images/userphotos/" + userID + ".png");
			ImageIO.write(bImage, "png", newFile);

			// Bild aus temp löschen
			file.delete();

		} catch (Exception e) {
			throw new PhotoUploadException();
		}

		return "userphotos/" + userID + ".png";
	}

	/**
	 * Holt den User mittels der userID aus der DB und gibt eine ArrayList mit Spielbrettnamen zurück
	 * 
	 * @param userID
	 *            User-Objekt
	 * @return ArrayList<String>
	 * @throws Unable2LoadUserException
	 *             Wird geworfen, wenn die Statistik nicht geladen werden kann
	 */
	@Override
	public ArrayList<String> getLastMaps(int userID) throws Unable2LoadUserException {
		// Hilfsvariablen
		ResultSet stmtResult = null;
		ArrayList<String> lastRounds = new ArrayList<String>();

		try {
			// PreparedStatement vorbereiten
			PreparedStatement lastRoundsStmt = DBConnection.getPstmt(DBStatements.SELECT_LASTROUNDS.getStmt());

			// Nickname binden
			lastRoundsStmt.setInt(1, userID);
			lastRoundsStmt.setInt(2, userID);
			lastRoundsStmt.setInt(3, userID);

			// Statement ausführen
			stmtResult = lastRoundsStmt.executeQuery();
			while (stmtResult.next()) {
				lastRounds.add(stmtResult.getString(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// PreparedStatements schließen
			try {
				if (stmtResult != null) {
					stmtResult.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return lastRounds;
	}
}
