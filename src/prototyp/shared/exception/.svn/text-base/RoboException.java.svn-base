package prototyp.shared.exception;

/**
 * Eigene Exception Klasse, damit man weiß, dass getMessageClient() überschrieben werden muss.
 * 
 * @author timo
 * @version 1.0
 */
public abstract class RoboException extends Exception {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -1404544169708968516L;

	/**
	 * Default
	 */
	public RoboException() {

	}

	/**
	 * Muss überschrieben werden
	 * 
	 * @return Message
	 */
	@Override
	public abstract String getMessage();

	/**
	 * Überschrieben, damit printStackTrace serverseitig verwendet werden kann.
	 * 
	 * @return Liefert den Namen der Klasse
	 */
	@Override
	public String toString() {
		return this.getClass().getName();
	}
}
