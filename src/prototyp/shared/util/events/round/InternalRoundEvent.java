package prototyp.shared.util.events.round;

import prototyp.client.presenter.PagePresenter;
import de.novanic.eventservice.client.event.Event;

/**
 * Interface für alle Events in der Round (also ab PreGame)
 * 
 * @author timo
 * @version 1.0
 */
public interface InternalRoundEvent extends Event {
	/**
	 * Wird ausgeführt, wenn das Event geworfen wird.
	 * 
	 * @param pagePresenter
	 *            PagePresenter
	 * @return true
	 */
	public boolean apply(PagePresenter pagePresenter);
}
