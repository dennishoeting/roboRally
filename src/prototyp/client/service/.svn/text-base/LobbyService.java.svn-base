package prototyp.client.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import prototyp.server.view.LobbyImpl;
import prototyp.shared.exception.administration.NoModificationException;
import prototyp.shared.exception.administration.NotAllTextFieldsFilledException;
import prototyp.shared.exception.frontpage.PasswordGenerationException;
import prototyp.shared.exception.frontpage.WrongPasswordException;
import prototyp.shared.exception.lobby.PhotoUploadException;
import prototyp.shared.exception.lobby.Unable2LoadUserException;
import prototyp.shared.exception.lobby.UsersBrowserCrashedException;
import prototyp.shared.exception.mail.EmailAlreadyExistsException;
import prototyp.shared.exception.registration.NicknameAlreadyExistsException;
import prototyp.shared.useradministration.Award;
import prototyp.shared.useradministration.Statistic;
import prototyp.shared.useradministration.User;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Lobby, clientseitige Sicht. Hier befinden sich alle Methoden für die Presenter aus dem Lobby-Package.
 * 
 * @author Andreas
 * @version 1.0
 * @version 1.1 getUsersWithStatistic(), editUser(..), getUser(String nickname), editPassword(), editLocked() Timo
 * @version 1.2 getUserAwards() (30.09.10, Robert)
 * 
 * @see Serverseite: {@link LobbyImpl}
 */
@RemoteServiceRelativePath("lobby")
public interface LobbyService extends RemoteService {
	/**
	 * Sperrt/Entsperrt einen User und gibt das neue User-Objekt zurück.
	 * 
	 * @param userID
	 * @param lock
	 * @return User
	 * @throws Unable2LoadUserException
	 */
	public User editLocked(int userID, boolean lock) throws Unable2LoadUserException;

	/**
	 * Holt den User mittels der userID aus der DB und gibt eine ArrayList mit Spielbrettnamen zurück
	 * 
	 * @param userID
	 *            User-Objekt
	 * @return ArrayList<String>
	 * @throws Unable2LoadUserException
	 *             Wird geworfen, wenn die Statistik nicht geladen werden kann
	 */
	public ArrayList<String> getLastMaps(int userID) throws Unable2LoadUserException;

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
	public boolean editUser(String nickname, String email, String firstname, String surname, int userID)
			throws NotAllTextFieldsFilledException, EmailAlreadyExistsException, NicknameAlreadyExistsException,
			NoModificationException;
	
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
	public boolean editUserWithNewNickname(String oldnickname, String nickname, String password, String email, String firstname,
			String surname, int userID) throws NotAllTextFieldsFilledException, EmailAlreadyExistsException,
			NicknameAlreadyExistsException, NoModificationException, WrongPasswordException, PasswordGenerationException;

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
	public boolean editUserPassword(String oldPassword, String newPassword, String oldNickname, String nickname, int userID)
			throws NotAllTextFieldsFilledException, NoModificationException, WrongPasswordException, PasswordGenerationException;

	/**
	 * Liefert alle User, die gerade online sind.
	 * 
	 * @author timo
	 * @return Map<Integer, Nickname>
	 */
	public Map<Integer, String> getOnlineUsers();

	/**
	 * Holt mittels der UserID ein UserObjekt aus der DB
	 * 
	 * @param userID
	 * @return User
	 * @throws Unable2LoadUserException
	 */
	public User getUser(int userID) throws Unable2LoadUserException;

	/**
	 * Holt mittels des Nicknames ein Userobjekt aus der DB
	 * 
	 * @param nickname
	 * @return User
	 */
	public User getUser(String nickname) throws Unable2LoadUserException;

	/**
	 * Erfragt alle Awards zu einer UserID
	 * 
	 * @param userID
	 * @return Award
	 */
	public List<Award> getUserAwards(int userID);

	/**
	 * Liefert den Path zum User Foto
	 * 
	 * @param userID
	 * @return Dateipfad des Bildes
	 */
	public String getUserPicture(int userID);
	
	/**
	 * Liefert die Statistik zu einem User
	 * 
	 * @param userID
	 *            UserID (DatenbankID)
	 * @return Userstatistik
	 */
	public Statistic getUserStatistic(int userID);

	/**
	 * Liefert alle User mit Statistic aus der DB. Der Integer-Wert entspricht der userID.
	 * 
	 * @return HashMap<Integer, User>
	 */
	public Map<Integer, User> getUsersWithStatistic();

	/**
	 * Soll jede Minute in etwa vom Client aufgerufen werden und dem Server zeigen, dass er noch da ist.
	 * 
	 * @author timo
	 * @param userID
	 * @return true
	 */
	public boolean giveAliveSignal(int userID) throws UsersBrowserCrashedException;

	/**
	 * Holt das User Foto aus images/temp/, benennt es richtig, verschiebt es in images/userphotos und ändert die größe
	 * 
	 * @author timo
	 */
	public String setUserPicture(int userID, String imageName) throws PhotoUploadException;

}
