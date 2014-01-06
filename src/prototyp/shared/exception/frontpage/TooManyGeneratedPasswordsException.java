package prototyp.shared.exception.frontpage;

import prototyp.client.view.Page;
import prototyp.server.view.FrontPageImpl;
import prototyp.shared.exception.RoboException;

/**
 * FTooManyGeneratedPasswordsException - Wird geworfen, wenn zu viele Passwörter
 * Generiert wurden.
 * 
 * @author Marina
 * @version 1.0
 * 
 * @see FrontPageImpl
 */
public class TooManyGeneratedPasswordsException extends RoboException {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 200011450236360823L;

	/**
	 * Konstruktor
	 */
	public TooManyGeneratedPasswordsException() {

	}

	@Override
	public String getMessage() {
		return Page.props.exception_frontpage_tooManyGeneratedPassWordsException();
	}
}