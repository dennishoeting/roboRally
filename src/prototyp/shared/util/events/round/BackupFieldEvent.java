package prototyp.shared.util.events.round;

import java.util.Map;
import java.util.Stack;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.round.RoundPresenterInterface;
import prototyp.shared.util.Position;

/**
 * Wird geworfen, wenn eine Spieler eine BackUpkarte legen möchte
 * 
 * @author Marcus
 * @version 1.0
 * 
 */
public class BackupFieldEvent implements InternalRoundEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 5058801711570709280L;

	/** SpielerID */
	private int playerId;

	/** X-Koordinate */
	private int i;

	/** Y-Koordinate */
	private int j;

	/** Default-Konstruktor */
	public BackupFieldEvent() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param playerId
	 *            SpielerID
	 * @param i
	 *            X-Koordinate
	 * @param j
	 *            Y-Koordinate
	 */
	public BackupFieldEvent(final int playerId, final int i, final int j) {
		this.playerId = playerId;
		this.i = i;
		this.j = j;
	}

	/**
	 * Wird ausgeführt, wenn das Event geworfen wird.
	 * 
	 * @param pagePresenter
	 *            PagePresenter
	 * @return true
	 */
	@Override
	public boolean apply(final PagePresenter pagePresenter) {
		if (pagePresenter instanceof RoundPresenterInterface) {
			final RoundPresenterInterface presenter = (RoundPresenterInterface) pagePresenter;

			final Map<Position, Stack<Integer>> map = presenter.getManager().getBackupFieldStack();
			
			final Position oldPos = new Position(presenter.getManager().getRobots().get(this.playerId).getStartField().getI(), 
					presenter.getManager().getRobots().get(this.playerId).getStartField().getJ());
			final Position newPos = new Position(this.i, this.j);

			map.get(oldPos).remove(Integer.valueOf(this.playerId));
			map.get(newPos).add(this.playerId);
		}

		return true;
	}

}
