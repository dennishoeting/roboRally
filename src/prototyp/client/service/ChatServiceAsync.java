package prototyp.client.service;

import prototyp.shared.exception.RoundNotThereException;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Services für den Chat
 * 
 * @author Timo
 * @version 1.0
 * 
 */
public interface ChatServiceAsync {

	/**
	 * Sendet eine farbige Nachricht
	 * 
	 * @param message
	 *            Nachricht als String
	 * @param color
	 *            Farbe
	 * @param domain
	 *            Chat-Domain
	 * @return true
	 */
	void sendGeneratedRoundMessage(String message, String color, String domain,
			AsyncCallback<Boolean> callback);

	/**
	 * Zum Versenden einer Nachricht im internen Chat. Es muss immer die Domain
	 * angegeben werden!
	 * 
	 * @param message
	 *            Messagetext
	 * @param domain
	 *            Chatdomain, an welche geschickt werden soll
	 * @param nickname
	 *            Nickname des Senders
	 * @return true, falls alles geklappt hat
	 */
	void sendMessage(String message, String nickname, String domain,
			AsyncCallback<Boolean> callback);

	/**
	 * Sendet eine Private Nachricht
	 * 
	 * @param message
	 *            Chatnachricht
	 * @param to
	 *            Empfänger (Nickname)
	 * @param from
	 *            Sender (Nickname)
	 * @param domain
	 *            Chat-Domain
	 * 
	 * @throws RoundNotThereException Wird geworfen, wenn die Runde nicht mehr existiert
	 * @throws NumberFormatException Wird bei der Rundensuche geworfen
	 */
	void sendPrivateMessage(String message, String to, String from,
			String domain, AsyncCallback<Boolean> callback);

	/**
	 * Schickt eine Nachricht, wenn jemand das PostGame verlassen hat.
	 * 
	 * @param nickname
	 *            Nickname des Spielers
	 * @param domain
	 *            Chatdomain
	 * 
	 * @return true
	 */
	void sendPostGameLeaveMessage(String nickname, String domain,
			AsyncCallback<Boolean> callback);

	/**
	 * Schickt eine Nachricht, wenn jemand das PostGame betreten hat.
	 * 
	 * @param nickname
	 *            Nickname des Spielers
	 * @param domain
	 *            Chatdomain
	 * 
	 * @return true
	 */
	void sendPostGameEnterMessage(String nickname, String domain, AsyncCallback<Boolean> callback);

}
