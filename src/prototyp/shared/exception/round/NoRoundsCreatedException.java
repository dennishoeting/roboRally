package prototyp.shared.exception.round;

import prototyp.server.model.RoundManager;

/**
 * Wird geworfen wenn getRounds() im RoundManager ausgeführt wird, es allerdings
 * keine Rounds gibt.
 * 
 * @author Timo
 * @version 1.0
 * 
 * @see RoundManager
 */
public class NoRoundsCreatedException extends Exception {

	/** Seriennummer */
	private static final long serialVersionUID = -6032407355252272489L;

	public NoRoundsCreatedException() {
		super("Momentan sind leider keine Rounds verfügbar!");
	}

}
