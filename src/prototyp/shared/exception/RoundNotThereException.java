package prototyp.shared.exception;

import prototyp.client.view.Page;

/**
 * Wird immer geworfen, wenn eine Rounde nicht Existiert. Beispielsweise wenn es
 * eine RoundID nicht gibt.
 * 
 * @author timo
 * @version 1.0
 */
public class RoundNotThereException extends RoboException {
	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 5659066786255279331L;

	/**
	 * Default
	 */
	public RoundNotThereException() {

	}

	/**
	 * Liefert die Fehlermeldung
	 * 
	 * @return Fehlermeldung
	 */
	@Override
	public String getMessage() {
		return Page.props.exception_roundNotThereException();
	}
}
