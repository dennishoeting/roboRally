package prototyp.shared.exception.mail;

import prototyp.client.view.Page;
import prototyp.shared.exception.RoboException;

/**
 * AccountLockedException - Wird geworfen, wenn der Account gesperrt ist
 * 
 * @author Marina
 * @version 1.0
 */
public class AccountLockedException extends RoboException {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -3906886122238882641L;

	/**
	 * Konstruktor
	 */
	public AccountLockedException() {

	}

	@Override
	public String getMessage() {
		return Page.props.exception_mail_accountLockedException();
	}
}