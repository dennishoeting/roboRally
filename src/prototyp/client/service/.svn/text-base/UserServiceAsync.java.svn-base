package prototyp.client.service;

import java.util.List;

import prototyp.shared.useradministration.User;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.novanic.eventservice.client.event.domain.Domain;

public interface UserServiceAsync {

	/**
	 * Ändert ein User-Objekt in der DB.
	 * 
	 * @param nickname
	 * @param email
	 * @param firstname
	 * @param surname
	 * @param userID
	 * @return boolean
	 */
	void editUser(String nickname, String email, String firstname,
			String surname, int userID, AsyncCallback<Boolean> callback);

	/**
	 * Holt mittels des Nicknames ein Userobjekt aus der DB
	 * 
	 * @param nickname
	 * @return User
	 */
	void getUser(String nickname, AsyncCallback<User> callback);

	/**
	 * Diese Methode wird aufgerufen wenn der Browser geschlossen wird! Hier
	 * kann alles rein was Serverseitig noch passieren soll. Beispielsweise ein
	 * PlayerManagerEvent auslösen oder ein ChatEventAuslösen.
	 * 
	 * @return boolean
	 */
	void onClosing(User user, List<Domain> chatServiceDomains,
			List<Integer> roundIDs, AsyncCallback<Boolean> callback);

	/**
	 * Schreibt eine LogIn Nachricht in den Chat
	 * 
	 * @param nickname
	 * @return
	 */
	void sendChatLogIn(String nickname, AsyncCallback<Boolean> callback);
}
