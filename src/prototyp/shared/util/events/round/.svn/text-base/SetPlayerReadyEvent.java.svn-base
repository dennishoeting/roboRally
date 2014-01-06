package prototyp.shared.util.events.round;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.pregame.PreGamePresenter;
import prototyp.client.view.pregame.PreGamePage;
import prototyp.shared.round.Player;

/**
 * Wird geworfen, wenn ein Player seinen readyStatus ändert.
 * 
 * @author timo
 * @version 1.0
 * 
 * @see PreGamePage
 */
public class SetPlayerReadyEvent implements InternalRoundEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -3686205955353311995L;

	/** Spieler */
	private Player player;

	/** Angabe, ob der Spieler bereit ist. */
	private boolean readyFlag;

	/**
	 * Default
	 */
	public SetPlayerReadyEvent() {

	}

	/**
	 * Konstruktor benötigt den Player, wo man den Status ändern möchte und den Status.
	 * 
	 * @param player
	 *            Spieler
	 * @param readyFlag
	 *            Angabe, ob der Spieler bereit ist, oder nicht
	 */
	public SetPlayerReadyEvent(Player player, boolean readyFlag) {
		this.player = player;
		this.readyFlag = readyFlag;
	}

	/**
	 * Wird ausgeführt, wenn das Event geworfen wird.
	 * 
	 * @param pagePresenter
	 *            PagePresenter
	 * @return true
	 */
	@Override
	public boolean apply(PagePresenter presenter) {
		if (presenter instanceof PreGamePresenter) {
			// Auf null prüfen
			if (((PreGamePresenter) presenter).getAllPlayers().get(this.player.getUser().getId()) == null) {
				return false;
			}

			// Player Referenz holen und Flag setzen
			((PreGamePresenter) presenter).getAllPlayers().get(this.player.getUser().getId()).setReady(this.readyFlag);

			// Dann noch das ListGrid neu zeichnen
			((PreGamePresenter) presenter).showPlayersAndWatchersInList();

		}

		return true;
	}

}
