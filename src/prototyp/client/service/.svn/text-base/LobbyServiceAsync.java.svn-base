package prototyp.client.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import prototyp.client.presenter.lobby.HighScorePagePresenter;
import prototyp.shared.exception.administration.NoModificationException;
import prototyp.shared.exception.administration.NotAllTextFieldsFilledException;
import prototyp.shared.exception.frontpage.PasswordGenerationException;
import prototyp.shared.exception.frontpage.WrongPasswordException;
import prototyp.shared.exception.lobby.Unable2LoadUserException;
import prototyp.shared.exception.mail.EmailAlreadyExistsException;
import prototyp.shared.exception.registration.NicknameAlreadyExistsException;
import prototyp.shared.useradministration.Award;
import prototyp.shared.useradministration.Statistic;
import prototyp.shared.useradministration.User;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Die zugehörige Async-Klasse zu {@link LobbyService}.
 * 
 * @author Andreas
 * @version 1.0
 * @version 1.1 getUsersWithStatistic(), editUser(..), getUser(String nickname) Timo
 * @version 1.2 getUserAward() (30.09.10, Robert)
 */
public interface LobbyServiceAsync {

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
	 *             wird geworfen wenn ein User nicht geladen werden kann (in Fkt: getUser)
	 */
	void editLocked(int userID, boolean lock, AsyncCallback<User> callback);

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
	void editUser(String nickname, String email, String firstname, String surname, int userID, AsyncCallback<Boolean> callback);

	/**
	 * Liefert alle User, die gerade online sind.
	 * 
	 * @author timo
	 * @return Map<Integer, nickname>
	 */
	void getOnlineUsers(AsyncCallback<Map<Integer, String>> callback);

	/**
	 * Holt den User mittels der userID aus der DB und gibt ein User-Objekt zurück
	 * 
	 * @param userID
	 *            DatenbankID des Nutzers
	 * @return User-Objekt
	 */
	void getUser(int userID, AsyncCallback<User> callback);

	/**
	 * Holt mittels des Nicknames ein Userobjekt aus der DB
	 * 
	 * @param nickname
	 *            Nickname des Nutzers
	 * @return User-Objekt
	 */
	void getUser(String nickname, AsyncCallback<User> callback);

	/**
	 * Liefert alle UserAwards zu der übergebenen DatenbankID des Nutzers
	 * 
	 * @param userID
	 *            DatenbankID des Nutzer
	 * 
	 * @return ArrayList mit Awards
	 */
	void getUserAwards(int userID, AsyncCallback<List<Award>> asyncCallback);

	/**
	 * Liefert den Path zum User Foto
	 */
	void getUserPicture(int userID, AsyncCallback<String> callback);

	/**
	 * Benutzt vom {@link HighScorePagePresenter}.
	 * 
	 * Holt sich alle User, mit ihrer Statistic, aus der Datenbank als HashMap mit {@link User}-instanzen wieder zurück.
	 * 
	 * @return HashMap mit Userobjekten
	 */
	void getUsersWithStatistic(AsyncCallback<Map<Integer, User>> callback);

	/**
	 * Soll jede Minute in etwa vom Client aufgerufen werden und dem Server zeigen, dass er noch da ist.
	 * 
	 * @author timo
	 * @param userID
	 * @return true
	 */
	void giveAliveSignal(int userID, AsyncCallback<Boolean> callback);

	/**
	 * Holt das User Foto aus images/temp/, benennt es richtig, verschiebt es in images/userphotos und ändert die größe
	 * 
	 * @author timo
	 */
	void setUserPicture(int userID, String imageName, AsyncCallback<String> callback);

	/**
	 * Holt den User mittels der userID aus der DB und gibt eine ArrayList mit Spielbrettnamen zurück
	 * 
	 * @param userID
	 *            User-Objekt
	 * @return ArrayList<String>
	 * @throws Unable2LoadUserException
	 *             Wird geworfen, wenn die Statistik nicht geladen werden kann
	 */
	void getLastMaps(int userID, AsyncCallback<ArrayList<String>> callback);

	/**
	 * Liefert die Statistik zu einem User
	 * 
	 * @param userID
	 *            UserID (DatenbankID)
	 * @return Userstatistik
	 */
	void getUserStatistic(int userID, AsyncCallback<Statistic> callback);

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
	void editUserWithNewNickname(String oldnickname, String nickname, String password, String email, String firstname,
			String surname, int userID, AsyncCallback<Boolean> callback);

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
	void editUserPassword(String oldPassword, String newPassword, String oldNickname, String nickname, int userID,
			AsyncCallback<Boolean> callback);
}
