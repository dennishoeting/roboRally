package prototyp.shared.exception.round;

/**
 * Falls ein User nicht an einer Round teilnimmt.
 * 
 * @author timo
 * 
 */
public class UserNotInRoundException extends Exception {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -2914890301781091742L;

	/**
	 * Default
	 */
	public UserNotInRoundException() {
		super("User nicht mehr in der Runde.");
	}
}
