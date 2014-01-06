package prototyp.shared.util.events.round;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.round.RoundPlayerPagePresenter;
import prototyp.client.presenter.round.RoundPresenterInterface;
import prototyp.client.view.round.RoundWatcherPage;
import prototyp.shared.round.Robot;

/**
 * Wird geschmissen, wenn noch ein Spieler übrig ist.
 * 
 * @author Marcus
 * @version 1.0
 * 
 */
public class OnePlayerLeftEvent implements InternalRoundEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 428292200347457524L;

	/** Player ID */
	private int playerId;

	/**
	 * Default-Konstruktor
	 */
	public OnePlayerLeftEvent() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param playerId
	 *            PlayerID
	 */
	public OnePlayerLeftEvent(int playerId) {
		this.playerId = playerId;
	}

	/**
	 * Wird ausgeführt, wenn das Event geworfen wird.
	 * 
	 * @param pagePresenter
	 *            PagePresenter
	 * @return true
	 */
	@Override
	public boolean apply(final PagePresenter pagePresenter) {
		if (pagePresenter instanceof RoundPresenterInterface) {
			final RoundPresenterInterface presenter = (RoundPresenterInterface) pagePresenter;

			presenter.getOthersReadyRecords().get(this.playerId).setDead();
			presenter.getOthersStateRecords().get(this.playerId).setDead();

			final Robot robot = presenter.getManager().getRobots().get(this.playerId);
			robot.setDeadForTurn(true);
			robot.setDead(true);
			robot.setI(-1);

			if(presenter instanceof RoundPlayerPagePresenter) {
				presenter.getPage().getRobotStatusArea().getOthersReadyGrid().redraw();
				presenter.getPage().getRobotStatusArea().getOthersStateGrid().redraw();
			} else {
				((RoundWatcherPage) pagePresenter.getPage()).getWatcherStatusArea().getOthersReadyGrid().redraw();
				((RoundWatcherPage) pagePresenter.getPage()).getWatcherStatusArea().getOthersStateGrid().redraw();
			}

			presenter.getManager().isGameFinished();
		}

		return false;
	}

}
