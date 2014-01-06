package prototyp.shared.util.events.lobby;

import prototyp.client.presenter.lobby.LobbyPagePresenter;
import prototyp.shared.round.RoundInfo;

/**
 * Wird geworfen wenn eine neue Runde erzeugt wird.
 * 
 * @author timo
 * @version 1.0
 * 
 */
public class NewRoundEvent implements LobbyEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 3538608725662920920L;

	/**
	 * Die RoundInfo dieses Events
	 */
	private RoundInfo roundInfo;

	/**
	 * Default
	 */
	public NewRoundEvent() {

	}

	/**
	 * Konstruktor mit RoundInfo
	 */
	public NewRoundEvent(RoundInfo roundInfo) {
		this.roundInfo = roundInfo;
	}

	/**
	 * Fügt jeder Lobby die neue Runde hinzu.
	 * 
	 * @author timo
	 */
	@Override
	public boolean apply(LobbyPagePresenter lobbyPagePresenter) {

		/*
		 * Round der Map mit RoundInfos hinzufügen. Mit der RoundId als Key
		 */
		return lobbyPagePresenter.getRoundInfos().put(
				this.roundInfo.getRoundId(), this.roundInfo) != null;
	}

}
