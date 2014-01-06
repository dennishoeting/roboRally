package prototyp.shared.util.events.round;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.pregame.PreGameGameInitiatorPagePresenter;
import prototyp.client.presenter.pregame.PreGamePagePresenter;
import prototyp.client.view.pregame.PreGameGameInitiatorPage;
import prototyp.client.view.pregame.PreGamePage;

/**
 * Deaktiviert die Farbauswahl, bereit-Button usw. in der PreGamePage.
 * 
 * @author timo
 * @version 1.0
 * 
 * @see PreGamePage, PreGameGameInitiatorPage, RoundManager
 */
public class DisableWidgetsEvent implements InternalRoundEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -175973378107663793L;

	/**
	 * Default
	 */
	public DisableWidgetsEvent() {

	}

	/**
	 * Wird ausgeführt, wenn das Event geworfen wird.
	 * 
	 * @param pagePresenter
	 *            PagePresenter
	 * @return true
	 */
	@Override
	public boolean apply(PagePresenter pagePresenter) {
		// GameInitiator
		if (pagePresenter instanceof PreGameGameInitiatorPagePresenter) {
			((PreGameGameInitiatorPage) pagePresenter.getPage()).getButtonKickPlayer().disable();
			((PreGameGameInitiatorPage) pagePresenter.getPage()).getColorChooser().disable();
			((PreGameGameInitiatorPage) pagePresenter.getPage()).getButtonCloseTab().disable();

			return true;
		}
		// Spieler
		if (pagePresenter instanceof PreGamePagePresenter) {
			((PreGamePage) pagePresenter.getPage()).getButtonReady().disable();
			((PreGamePage) pagePresenter.getPage()).getColorChooser().disable();
			((PreGamePage) pagePresenter.getPage()).getButtonCloseTab().disable();

			return true;
		}

		// Watcher: nichts bisher

		return true;
	}

}
