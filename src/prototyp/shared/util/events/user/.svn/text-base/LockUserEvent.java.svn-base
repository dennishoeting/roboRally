package prototyp.shared.util.events.user;

import com.google.gwt.user.client.Window;


/**
 * Wird aufgerufen, wenn der User gesperrt wurde.
 * @author timo
 *
 */
public class LockUserEvent implements UserEvent{

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -4411501209770665482L;
	
	/**
	 * Default
	 */
	public LockUserEvent() {
		
	}

	@Override
	public boolean apply() {
		//Raus werfen :)
		Window.Location.reload();
		return true;
	}
	
	
}
