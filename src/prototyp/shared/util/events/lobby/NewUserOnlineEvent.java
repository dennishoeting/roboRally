package prototyp.shared.util.events.lobby;

import prototyp.client.presenter.lobby.LobbyPagePresenter;

/**
 * Wird geworfen, wenn ein neuer User sich einlogt.
 * 
 * @author timo
 * 
 */
public class NewUserOnlineEvent implements LobbyEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 8389599079186164318L;

	/**
	 * UserID des neuen Users
	 */
	private int userID;

	/**
	 * Nickname des neuen Users
	 */
	private String nickname;

	/**
	 * Default
	 */
	public NewUserOnlineEvent() {

	}

	/**
	 * Bekommt den neuen User übergeben.
	 * 
	 * @param newUser
	 */
	public NewUserOnlineEvent(int userID, String nickname) {
		this.userID = userID;
		this.nickname = nickname;
	}

	@Override
	public boolean apply(LobbyPagePresenter lobbyPagePresenter) {

		// Neuen User hinzufügen.
		lobbyPagePresenter.getOnlineUsers().put(this.userID, this.nickname);
		// Zeichnen
		lobbyPagePresenter.showOnlineUsersInGrid();

		return true;
	}

}
