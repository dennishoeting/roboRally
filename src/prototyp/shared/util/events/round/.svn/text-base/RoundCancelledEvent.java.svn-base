package prototyp.shared.util.events.round;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.pregame.PreGamePresenter;
import prototyp.client.view.Page;

/**
 * Wird aufgerufen, wenn die Runde beendet wurde.
 * 
 * @author timo
 * @version 1.0
 */
public class RoundCancelledEvent implements InternalRoundEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -1942475191872321613L;

	/**
	 * Default-Konstruktor
	 */
	public RoundCancelledEvent() {

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
			((PreGamePresenter) presenter).closeTab(Page.props
					.eventRoundRoundCancelledEvent());
		}

		return true;
	}

}
