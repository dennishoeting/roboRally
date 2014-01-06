package prototyp.shared.exception.round;

/**
 * Falls einer Round beigetreten wird, die schon voll ist. Könnte in einem sehr
 * ungünstigen Fall durchaus passieren!
 * 
 * @author timo
 * 
 */
public class RoundFullException extends Exception {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 142804845095454726L;

	public RoundFullException() {
		super("Die Round ist doch schon voll :(");
	}
}
