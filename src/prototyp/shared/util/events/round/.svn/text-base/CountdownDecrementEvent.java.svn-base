package prototyp.shared.util.events.round;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.round.RoundPlayerPagePresenter;
import prototyp.client.presenter.user.UserPresenter;

/**
 * Wird ausgelöst, wenn der Countdown verringert wird.
 * 
 * @author Marcus
 * @version 1.0
 */
public class CountdownDecrementEvent implements InternalRoundEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 4428470010912948698L;

	/** SpielerID */
	private int playerId;

	/**
	 * Default-Konstruktor
	 */
	public CountdownDecrementEvent() {

	}

	/**
	 * Konstruktor
	 * 
	 * @param playerId
	 *            SpielerID
	 */
	public CountdownDecrementEvent(final int playerId) {
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
		if (pagePresenter instanceof RoundPlayerPagePresenter) {
			if (this.playerId == UserPresenter.getInstance().getUser().getId()) {
				((RoundPlayerPagePresenter) pagePresenter).setRestartTime(500);
			}
		}

		return true;
	}
}
