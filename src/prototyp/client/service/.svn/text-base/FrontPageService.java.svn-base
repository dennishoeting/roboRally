package prototyp.client.service;

import prototyp.client.presenter.frontpage.FrontPagePresenter;
import prototyp.server.view.FrontPageImpl;
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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * FrontPage, clientseitige Sicht. Hier befinden sich alle Methoden für die
 * Presenter aus dem FrontPage-Package.
 * 
 * @author Andreas, Marina
 * @version 1.0
 * 
 * @see Serverseite: {@link FrontPageImpl}
 */
@RemoteServiceRelativePath("frontpage")
public interface FrontPageService extends RemoteService {

	/**
	 * Lädt die AGBs aus war/other/agbs.txt Werden immer aus der txt geladen ->
	 * lohnt sich nicht so einen großen String im Speicher zu lassen.
	 * 
	 * @author timo
	 * @version 1.0
	 */
	public String getAGBs() throws CantLoadAgbsException;

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
	public boolean registerUser(String nickname, String email, String password,
			String firstname, String surname)
			throws EmailAlreadyExistsException, NicknameAlreadyExistsException,
			NoModificationException, PasswordGenerationException;

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
	public boolean sendNewPassword(String recipient)
			throws AccountLockedException, MailNotFoundException,
			FailedToSendMailException, TooManyGeneratedPasswordsException,
			NoModificationException;

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
	 * @param userId
	 * 			UserId falls der User schon mal eingelogt war (ausm Cookie)
	 * 			um zu schauen ob er sich zwei mal mit einem Browser einlogt.           
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
	 * @throws SameBrowserException 
	 */
	public Boolean validateLogIn(String nickname, String password, int userId)
			throws WrongPasswordException, TooManyFailedLoginsException,
			UserLockedException, UserAlreadyOnlineException,
			InvalidLogInDataException, PasswordGenerationException,
			AlreadyInGameException, UserNotInDataBaseException, SameBrowserException;
}
