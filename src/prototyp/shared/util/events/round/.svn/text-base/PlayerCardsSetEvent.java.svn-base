package prototyp.shared.util.events.round;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.round.LogicManager;
import prototyp.client.presenter.round.RoundPlayerPagePresenter;
import prototyp.client.presenter.round.RoundPresenterInterface;
import prototyp.client.presenter.user.UserPresenter;
import prototyp.client.view.round.RoundWatcherPage;

/**
 * Wird ausgelöst, wenn ein Spieler alle seine Karten gelegt hat.
 * 
 * @author Marcus
 * @version 1.0
 * 
 * @see LogicManager
 */
public class PlayerCardsSetEvent implements InternalRoundEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 1674480410454063715L;

	/** Spieler ID */
	private int playerId;

	/**
	 * Default-Konstruktor
	 */
	public PlayerCardsSetEvent() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param playerId
	 *            SpielerID
	 */
	public PlayerCardsSetEvent(int playerId) {
		this.playerId = playerId;
	}

	/**
	 * Wird ausgeführt, wenn das Event geworfen wird.
	 * 
	 * @param pagePresenter
	 *            PagePresenter
	 * @return false
	 */
	@Override
	public boolean apply(PagePresenter pagePresenter) {
		if (pagePresenter instanceof RoundPresenterInterface) {
			if (this.playerId != UserPresenter.getInstance().getUser().getId()) {
				((RoundPresenterInterface) pagePresenter).getOthersReadyRecords().get(this.playerId).setIsReady(true);
				if(pagePresenter instanceof RoundPlayerPagePresenter) {
					((RoundPresenterInterface) pagePresenter).getPage().getRobotStatusArea().getOthersReadyGrid().redraw();
				} else {
					((RoundWatcherPage) pagePresenter.getPage()).getWatcherStatusArea().getOthersReadyGrid().redraw();
					((RoundWatcherPage) pagePresenter.getPage()).getWatcherStatusArea().getOthersStateGrid().redraw();
				}

			}
		}

		return false;
	}

}
