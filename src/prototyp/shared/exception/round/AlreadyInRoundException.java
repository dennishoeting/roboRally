package prototyp.shared.exception.round;

/**
 * Wird geworfen wenn man einer Rounde beitreten möchte, in der man sich bereits
 * befindet.
 * 
 * @author timo
 * 
 */
public class AlreadyInRoundException extends Exception {
	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -3362066121112535573L;

	public AlreadyInRoundException() {
		super("Sie befinden sich bereits in dieser Runde!");
	}
}
