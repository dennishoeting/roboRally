package prototyp.shared.util.events.round;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.pregame.PreGamePresenter;
import prototyp.shared.round.Player;

/**
 * Wird geworfen, wenn einer der Player seine Farbe ändert.
 * 
 * @author timo
 * @version 1.0
 */
public class ChangePlayerColorEvent implements InternalRoundEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 6499634980866007189L;

	/**
	 * Player der die Farbe ändert.
	 */
	private Player player;

	/**
	 * Default
	 */
	public ChangePlayerColorEvent() {

	}

	/**
	 * Konstruktor mit dem Player, der die Farbe ändert.
	 * 
	 * @param player
	 */
	public ChangePlayerColorEvent(Player player) {
		this.player = player;
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
			// Farbe für den Player setzen
			((PreGamePresenter) presenter).getAllPlayers().get(this.player.getUser().getId()).getRobot()
					.setColor(this.player.getRobot().getColor());

			// Farbauswahl neu setzen, nur bei GI und Player
			if (((PreGamePresenter) presenter).getPlayerWatcherOrGameInitiator() <= 1) {
				((PreGamePresenter) presenter).setAvailableColors();
			}

			// ListGrid neu zeichnen
			((PreGamePresenter) presenter).showPlayersAndWatchersInList();
		}
		return true;
	}

}
