package prototyp.shared.exception.playingboard;

/**
 * Wird geworfen, wenn Förderbänder auf dem Spielbrett falsch angeordnet sind
 * 
 * Diese Klasse benutzt noch keine Props, dazu haben wir (Timo, Andreas) keine Lust zu.
 * 
 * @author Marcus
 * @version 1.0
 */
public class ConveyorBeltsInconsistentException extends PlayingboardException {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 2487964556765298384L;

	/** Nachricht */
	private String message;

	/**
	 * Default-Konstruktor für Serializabe-Interface
	 */
	public ConveyorBeltsInconsistentException() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param message
	 *            Fehlermeldung
	 */
	public ConveyorBeltsInconsistentException(String message) {
		this.message = message;
	}

	/**
	 * Liefert die Fehlernachricht
	 * 
	 * @return Fehlernachricht
	 */
	@Override
	public String getMessage() {
		return this.message;
	}

}
