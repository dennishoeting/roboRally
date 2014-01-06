package prototyp.shared.util.events.round;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.round.RoundPlayerPagePresenter;
import prototyp.client.presenter.round.RoundPresenterInterface;
import prototyp.client.presenter.user.UserPresenter;
import prototyp.client.view.round.RoundWatcherPage;
/**
 * Wird geworfen, wenn ein Spieler eine Karte gelegt hat und die andere dies im ListGrid angezeigt bekommen.
 * 
 * @author Marcus
 * @version 1.0
 *
 */
public class PlayerOneCardSetEvent implements InternalRoundEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 6933286950127411139L;

	/** SpielerID */
	private int playerId;

	/** Kartenslot */
	private int slot;

	/** Angabe, ob die Karte gesetzt wurde oder wieder entfernt wurde */
	private int set;

	/**
	 * Default-Konstruktor
	 */
	public PlayerOneCardSetEvent() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param playerId SpielerID
	 * @param slot Kartenslot
	 * @param set Angabe, ob die Karte gesetzt wurde oder wieder entfernt wurde
	 */
	public PlayerOneCardSetEvent(int playerId, int slot, int set) {
		this.playerId = playerId;
		this.slot = slot;
		this.set = set;
	}

	/**
	 * Wird ausgeführt, wenn das Event geworfen wird.
	 * 
	 * @param pagePresenter
	 *            PagePresenter
	 * @return false
	 */
	@Override
	public boolean apply(PagePresenter pagePresenter) {
		if (pagePresenter instanceof RoundPresenterInterface) {
			if (this.playerId != UserPresenter.getInstance().getUser().getId()) {
				((RoundPresenterInterface) pagePresenter)
						.getOthersReadyRecords().get(this.playerId)
						.setCardSet(this.slot, this.set);
				
				if(pagePresenter instanceof RoundPlayerPagePresenter) {
					((RoundPresenterInterface) pagePresenter).getPage()
					.getRobotStatusArea().getOthersReadyGrid().redraw();
				} else {
					((RoundWatcherPage) pagePresenter.getPage()).getWatcherStatusArea().getOthersReadyGrid().redraw();
				}
			}
		}

		return true;
	}

}
