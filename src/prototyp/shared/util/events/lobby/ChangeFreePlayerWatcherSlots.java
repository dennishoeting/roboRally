package prototyp.shared.util.events.lobby;

import prototyp.client.presenter.lobby.LobbyPagePresenter;

/**
 * Ändert die Anzahl der Player oder Watcher Slots in der Lobby
 * 
 * @author timo
 * @version 1.0
 */
public class ChangeFreePlayerWatcherSlots implements LobbyEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -592214337567057864L;

	private int roundID, freePlayerSlots, freeWatcherSlots;

	/**
	 * Default
	 */
	public ChangeFreePlayerWatcherSlots() {

	}

	/**
	 * Konstruktor
	 * 
	 * @param roundID
	 *            RundenID
	 * @param freePlayerSlots
	 *            Angabe der freien Spielerplätze
	 * @param freeWatcherSlots
	 *            Angabe der freien Beobachterplätze
	 */
	public ChangeFreePlayerWatcherSlots(int roundID, int freePlayerSlots, int freeWatcherSlots) {
		this.roundID = roundID;
		this.freePlayerSlots = freePlayerSlots;
		this.freeWatcherSlots = freeWatcherSlots;
	}

	/**
	 * Wird ausgeführt, wenn das Event geworfen wird.
	 * 
	 * @param lobbyPagePresenter
	 *            LobbyPagePresenter
	 * @return true, wenn alles geklappt hat
	 */
	@Override
	public boolean apply(LobbyPagePresenter lobbyPagePresenter) {

		if (lobbyPagePresenter.getRoundInfos().get(this.roundID) != null) {

			lobbyPagePresenter.getRoundInfos().get(this.roundID).setFreePlayerSlots(this.freePlayerSlots);
			lobbyPagePresenter.getRoundInfos().get(this.roundID).setFreeWatcherSlots(this.freeWatcherSlots);

			return true;
		}

		return false;
	}

}
