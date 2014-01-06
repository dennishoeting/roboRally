package prototyp.client.service;

import java.util.Map;

import prototyp.client.presenter.administration.AdministrationPagePresenter;
import prototyp.server.view.AdministrationImpl;
import prototyp.shared.exception.administration.GotNoUserException;
import prototyp.shared.exception.administration.NoModificationException;
import prototyp.shared.exception.administration.NotAllTextFieldsFilledException;
import prototyp.shared.exception.mail.EmailAlreadyExistsException;
import prototyp.shared.exception.registration.NicknameAlreadyExistsException;
import prototyp.shared.useradministration.User;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Administration, clientseitige Sicht. Hier befinden sich alle Methoden für die
 * Presenter aus dem Administration-Package.
 * 
 * @author Andreas, Mischa
 * @version 1.2
 * @version 1.3 Exceptions werfen
 * 
 * @see Serverseite: {@link AdministrationImpl}
 */
@RemoteServiceRelativePath("administration")
public interface AdministrationService extends RemoteService {

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
	public boolean editUser(String nickname, String email, String firstname,
			String surname, int userID, String editor) throws NotAllTextFieldsFilledException,
			EmailAlreadyExistsException, NicknameAlreadyExistsException,
			NoModificationException;

	/**
	 * Benutzt vom {@link AdministrationPagePresenter}.
	 * 
	 * Holt sich alle User aus der Datenbank und gibt eine HashMap mit
	 * {@link User}-instanzen wieder zurück. Der Key ist die userID.
	 * 
	 * @return HashMap<Integer, User> mit Usern und als Key wird die DatenbankID
	 *         genommen
	 * @throws GotNoUserException
	 *             Wird geworfen, wenn keine User gefunden werden konnten
	 */
	public Map<Integer, User> getUsers() throws GotNoUserException;

	/**
	 * Benutzt vom {@link AdministrationPagePresenter}.
	 * 
	 * Sperrt bzw. entsperrt einen Nutzer in der Datenbank. Die Funktion ist vom
	 * Parameter lock abhängig.
	 * 
	 * @param userid
	 *            DatenbankID des Nutzers
	 * @param lock
	 *            true, falls der Nutzer gesperrt werden soll, ansonsten false.
	 * @return true, falls alles geklappt hat
	 * @throws NoModificationException
	 *             Wird geworfen, wenn keine Änderung übernommen werden konnte.
	 */
	public boolean lockUser(int userid, boolean lock)
			throws NoModificationException;

}
