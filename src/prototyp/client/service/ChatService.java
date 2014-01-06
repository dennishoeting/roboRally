package prototyp.client.service;

import prototyp.shared.exception.RoundNotThereException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Services für den Chat
 * 
 * @author Timo
 * @version 1.0
 * 
 */
@RemoteServiceRelativePath("chatservice")
public interface ChatService extends RemoteService {
	
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
	public boolean sendGeneratedRoundMessage(String message, String color,
			String domain);

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
	public boolean sendMessage(String message, String nickname, String domain);

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
	public boolean sendPrivateMessage(String message, String to, String from,
			String domain) throws NumberFormatException, RoundNotThereException;

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
	public boolean sendPostGameLeaveMessage(String nickname, String domain);
	
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
	public boolean sendPostGameEnterMessage(String nickname, String domain);
}
