package prototyp.shared.util.events.round;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.round.RoundPresenterInterface;

/**
 * Wird geworfen, wenn ein Spieler ein PowerDown ankündigt.
 * 
 * @author Marcus
 * @version 1.0
 *
 */
public class PowerDownEvent implements InternalRoundEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -1404243553227383521L;

	/** SpielerID */
	private int playerId;

	/** PowerDown Status */
	private int powerDownState;

	/**
	 * Default-Konstruktor
	 */
	public PowerDownEvent() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param playerId SpielerID
	 * @param powerDownState PowerDown-Status
	 */
	public PowerDownEvent(int playerId, int powerDownState) {
		this.playerId = playerId;
		this.powerDownState = powerDownState;
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
			((RoundPresenterInterface) pagePresenter).getManager()
					.getRobots().get(this.playerId)
					.setPowerDown(this.powerDownState);
		}

		return false;
	}

}
