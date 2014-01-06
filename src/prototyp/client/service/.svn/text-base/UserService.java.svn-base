package prototyp.client.service;

import java.util.List;

import prototyp.shared.exception.frontpage.UserNotInDataBaseException;
import prototyp.shared.useradministration.User;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.novanic.eventservice.client.event.domain.Domain;

/**
 * Service für den UserPresenter
 * 
 * @author timo
 * @version 1.0
 */
@RemoteServiceRelativePath("userservice")
public interface UserService extends RemoteService {
	/**
	 * Aendert ein User-Objekt in der DB.
	 * 
	 * @param nickname
	 * @param email
	 * @param firstname
	 * @param surname
	 * @param userID
	 * @return boolean
	 */
	public boolean editUser(String nickname, String email, String firstname,
			String surname, int userID);

	/**
	 * Holt mittels des Nicknames ein Userobjekt aus der DB
	 * 
	 * @param nickname
	 * @return User
	 */
	public User getUser(String nickname) throws UserNotInDataBaseException;

	/**
	 * Diese Methode wird aufgerufen wenn der Browser geschlossen wird! Hier
	 * kann alles rein was Serverseitig noch passieren soll. Beispielsweise ein
	 * PlayerManagerEvent auslösen oder ein ChatEventAuslösen.
	 * 
	 * @return boolean
	 */
	public boolean onClosing(User user, List<Domain> chatServiceDomains,
			List<Integer> roundIDs);

	/**
	 * Schreibt eine LogIn Nachricht in den Chat
	 * 
	 * @param nickname
	 * @return
	 */
	public boolean sendChatLogIn(String nickname);
}
