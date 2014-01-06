package prototyp.shared.util.events.round;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.pregame.PreGamePresenter;
import prototyp.client.view.Page;
import prototyp.shared.round.Player;
import prototyp.shared.round.Watcher;

/**
 * Wird aufgerufen, wenn ein Player/Watcher gelöscht wird bzw. wenn er gekickt
 * wird.
 * 
 * @author timo
 * 
 */
public class RemovePlayerWatcherRoundEvent implements InternalRoundEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -5103137287636956693L;

	/**
	 * Gelöschter Player/Watcher
	 */
	private Watcher removedPlayerWatcher;

	/**
	 * Default
	 */
	public RemovePlayerWatcherRoundEvent() {

	}

	/**
	 * Übergibt den gelöschten (gekickten) Player/Watcher
	 * 
	 * @param removedPlayer
	 * @param removedWatcher
	 */
	public RemovePlayerWatcherRoundEvent(Watcher removedPlayerWatcher) {
		this.removedPlayerWatcher = removedPlayerWatcher;
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
			// Prüfen ob man gekickt wurde
			if (((PreGamePresenter) presenter).getUserID() == this.removedPlayerWatcher
					.getUser().getId()) {

				// Tab schließen
				((PreGamePresenter) presenter).closeTab(Page.props
						.eventRoundRemovePlayerWatcherEvent());
				return true;
			}

			// Player
			if (this.removedPlayerWatcher instanceof Player) {
				// Löschen
				((PreGamePresenter) presenter).getAllPlayers().remove(
						this.removedPlayerWatcher.getUser().getId());

				// Mögliche Farben neu setzen
				((PreGamePresenter) presenter).setAvailableColors();

				// Neu Zeichnen
				return ((PreGamePresenter) presenter)
						.showPlayersAndWatchersInList();
			} else { // Watcher
				// Löschen
				((PreGamePresenter) presenter).getAllWatchers().remove(
						this.removedPlayerWatcher.getUser().getId());

				// Neu Zeichnen
				return ((PreGamePresenter) presenter)
						.showPlayersAndWatchersInList();

			}
		}
		return false;
	}

}
