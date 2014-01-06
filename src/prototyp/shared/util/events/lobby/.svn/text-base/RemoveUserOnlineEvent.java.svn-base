package prototyp.shared.util.events.lobby;

import prototyp.client.presenter.lobby.LobbyPagePresenter;

/**
 * Wird geworfen wenn ein User sich ausloggt.
 * 
 * @author timo
 * 
 */
public class RemoveUserOnlineEvent implements LobbyEvent {
	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 5384838195452077530L;

	/**
	 * UserID des ausgelogten Users
	 */
	private int userID;

	/**
	 * Default
	 */
	public RemoveUserOnlineEvent() {

	}

	public RemoveUserOnlineEvent(int userID) {
		this.userID = userID;
	}

	@Override
	public boolean apply(LobbyPagePresenter lobbyPagePresenter) {
		// löschen
		lobbyPagePresenter.getOnlineUsers().remove(this.userID);
		// Zeichnen
		lobbyPagePresenter.showOnlineUsersInGrid();
		return true;
	}
}
