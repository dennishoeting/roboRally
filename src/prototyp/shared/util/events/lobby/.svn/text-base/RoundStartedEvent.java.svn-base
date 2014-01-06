package prototyp.shared.util.events.lobby;

import prototyp.client.presenter.lobby.LobbyPagePresenter;

/**
 * Wird geworfen, sobald eine Runde gestartet wird.
 * 
 * @author timo
 * @version 1.0
 */
public class RoundStartedEvent implements LobbyEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -5626328405542323995L;

	/**
	 * RoundID
	 */
	private int roundID;

	/**
	 * Default
	 */
	public RoundStartedEvent() {

	}

	/**
	 * Konstruktor
	 * 
	 * @param roundID RundenID des RoundManagers
	 */
	public RoundStartedEvent(int roundID) {
		this.roundID = roundID;
	}

	/**
	 * Setzt eine RoundInfo auf ready
	 * 
	 * @return false
	 */
	@Override
	public boolean apply(LobbyPagePresenter lobbyPagePresenter) {

		if (lobbyPagePresenter.getRoundInfos().get(this.roundID) != null) {
			lobbyPagePresenter.getRoundInfos().get(this.roundID).setStarted();
		}

		return false;
	}

}
