package prototyp.shared.util.events.round;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.pregame.PreGamePresenter;
import prototyp.shared.round.Player;
import prototyp.shared.round.Watcher;

/**
 * Wird geworfen, sobald es einen neuen Player oder Watcher in der Round gibt.
 * 
 * @author timo
 * 
 */
public class NewPlayerWatcherRoundEvent implements InternalRoundEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -8044162748787608605L;

	/**
	 * Neuer Player/Watcher
	 */
	private Watcher newPlayerWatcher;

	/**
	 * Default
	 */
	public NewPlayerWatcherRoundEvent() {

	}

	/**
	 * Konstruktor mit Player oder Watcher
	 * 
	 * @param newPlayerWatcher
	 */
	public NewPlayerWatcherRoundEvent(Watcher newPlayerWatcher) {
		this.newPlayerWatcher = newPlayerWatcher;
	}

	/**
	 * Wird ausgeführt, wenn das Event geworfen wird.
	 * 
	 * @param pagePresenter
	 *            PagePresenter
	 * @return false
	 */
	@Override
	public boolean apply(PagePresenter presenter) {
		if (presenter instanceof PreGamePresenter) {
			// Player
			if (this.newPlayerWatcher instanceof Player) {
				// Adden
				((PreGamePresenter) presenter).getAllPlayers().put(
						this.newPlayerWatcher.getUser().getId(),
						((Player) this.newPlayerWatcher));
				// Neu Zeichnen
				return ((PreGamePresenter) presenter)
						.showPlayersAndWatchersInList();
			} else { // Watcher
				// Adden
				((PreGamePresenter) presenter).getAllWatchers().put(
						this.newPlayerWatcher.getUser().getId(),
						this.newPlayerWatcher);
				// Neu Zeichnen
				return ((PreGamePresenter) presenter)
						.showPlayersAndWatchersInList();
			}
		}
		return false;
	}

}
