package prototyp.shared.util.events.round;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.round.RoundPlayerPagePresenter;
import prototyp.client.presenter.round.RoundPresenterInterface;
import prototyp.client.view.round.RoundWatcherPage;
import prototyp.shared.round.Robot;

/**
 * Wird geworfen, wenn ein Spieler eine aktuelle Spielrunde verlässt.
 * 
 * @author Marcus
 * @version 1.0
 *
 */
public class PlayerLeftEvent implements InternalRoundEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 8987509080936341011L;

	/** PlayerID */
	private int playerId;

	/**
	 * Default-Konstruktor für S-Interface
	 */
	public PlayerLeftEvent() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param playerId
	 *            PlayerID
	 */
	public PlayerLeftEvent(int playerId) {
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
			final RoundPresenterInterface presenter = (RoundPresenterInterface) pagePresenter;

			/*
			 * entfernen von der DrawingArea und im LogicManager auf Dead setzen
			 */
			final Robot robot = presenter.getManager().getRobots().get(this.playerId);
			robot.setDeadForTurn(true);
			robot.setDead(true);
			robot.setI(-1);
			presenter.getPage().getDrawingArea().remove(presenter.getRobotsImageList().get(robot));

			presenter.getOthersReadyRecords().get(this.playerId).setDead();
			presenter.getOthersStateRecords().get(this.playerId).setDead();
			
			if(presenter instanceof RoundPlayerPagePresenter) {
				presenter.getPage().getRobotStatusArea().getOthersReadyGrid().redraw();
				presenter.getPage().getRobotStatusArea().getOthersStateGrid().redraw();
			} else {
				((RoundWatcherPage) pagePresenter.getPage()).getWatcherStatusArea().getOthersReadyGrid().redraw();
				((RoundWatcherPage) pagePresenter.getPage()).getWatcherStatusArea().getOthersStateGrid().redraw();
			}
			

		}

		return false;
	}

}
