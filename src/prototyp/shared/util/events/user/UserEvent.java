package prototyp.shared.util.events.user;

import de.novanic.eventservice.client.event.Event;

/**
 * Events für den UserPresenter
 * @author timo
 *
 */
public interface UserEvent extends Event{
	/**
	 * Wird ausgeführt, wenn das Event geworfen wird.
	 * @return true
	 */
	public boolean apply();
}
