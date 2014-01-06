package prototyp.shared.util.events.round;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.TabManager;
import prototyp.client.presenter.pregame.PreGamePresenter;
import prototyp.client.presenter.round.RoundPlayerPagePresenter;
import prototyp.client.presenter.round.RoundWatcherPagePresenter;
import prototyp.client.view.Page;
import prototyp.shared.round.RoundNeedsWrapper;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;
import de.novanic.eventservice.client.event.domain.DomainFactory;

/**
 * Wird geworfen, wenn eine Round gestartet wurde.
 * 
 * @author timo
 * @version 1.0
 */
public class StartRoundEvent implements InternalRoundEvent {

	/** Seriennummer */
	private static final long serialVersionUID = 6960576385816106717L;

	/** */
	private RoundNeedsWrapper roundNeedsWrapper;

	/** Spielrunden-ID */
	private int roundID;

	/**
	 * Default-Konstruktor
	 */
	public StartRoundEvent() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param roundID
	 *            SpielrundenID
	 * @param roundNeedsWrapper
	 */
	public StartRoundEvent(int roundID, RoundNeedsWrapper roundNeedsWrapper) {
		this.roundID = roundID;
		this.roundNeedsWrapper = roundNeedsWrapper;
	}

	/**
	 * Wird ausgeführt, wenn das Event geworfen wird.
	 * 
	 * @param pagePresenter
	 *            PagePresenter
	 * @return true
	 */
	@Override
	public boolean apply(PagePresenter presenter) {
		if (presenter instanceof PreGamePresenter) {
			RemoteEventServiceFactory.getInstance().getRemoteEventService()
					.removeListeners(DomainFactory.getDomain("round_" + this.roundID));

			// Ist GI oder Player
			if (((PreGamePresenter) presenter).getPlayerWatcherOrGameInitiator() < 2) {

				TabManager.getInstanceMain().switchTab(
						((PreGamePresenter) presenter).getTab(),
						new RoundPlayerPagePresenter(((PreGamePresenter) presenter).getChatPresenter(),
								((PreGamePresenter) presenter).getRound(), this.roundNeedsWrapper));
				
				// SelfMessage schicken
				((PreGamePresenter) presenter).getChatPresenter().sendSelfMessage(Page.props.chatPresenterEnterRound());

			} else { // ist Watcher

				TabManager.getInstanceMain().switchTab(
						((PreGamePresenter) presenter).getTab(),
						new RoundWatcherPagePresenter(((PreGamePresenter) presenter).getChatPresenter(),
								((PreGamePresenter) presenter).getRound(), this.roundNeedsWrapper));

			}
		}

		return true;
	}

}
