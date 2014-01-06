package prototyp.shared.util.events.round;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.round.RoundPresenterInterface;
import prototyp.client.util.OthersReadyRecord;
import prototyp.client.util.OthersStateRecord;
import prototyp.client.view.round.RoundWatcherPage;
import prototyp.shared.round.Robot;

/**
 * Dieses Event wird geschmissen, wenn alle Spieler gleichzeitig die Runde verlassen haben.
 * Es benachricht alle Watcher.
 * @author Marcus
 *
 */
public class WatcherNotificationNoWinnerEvent implements InternalRoundEvent {
	
	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -1748915132730138366L;

	/**
	 * Default-Konstruktor
	 */
	public WatcherNotificationNoWinnerEvent() {}

	/**
	 * Behandlung des Events
	 */
	@Override
	public boolean apply(final PagePresenter pagePresenter) {


		if (pagePresenter instanceof RoundPresenterInterface) {
			final RoundPresenterInterface presenter = (RoundPresenterInterface) pagePresenter;
			
			
			/*
			 * Alle Roboter sterben lassen und verschwinden lassen
			 */
			for(Robot derTodGeweihte : presenter.getManager().getRobots().values()) {
				derTodGeweihte.setDeadForTurn(true);
				derTodGeweihte.setDead(true);
				derTodGeweihte.setI(-1);
				
				try {
					presenter.getPage().getDrawingArea().remove(presenter.getRobotsImageList().get(derTodGeweihte));
				} catch(Exception e) {}
				
			}
			
			/*Alles auf tot setzen
			 * 
			 */
			for(OthersReadyRecord record : presenter.getOthersReadyRecords().values()) {
				record.setDead();
			}
			
			/*Alles auf tot setzen
			 * 
			 */
			for(OthersStateRecord record : presenter.getOthersStateRecords().values()) {
				record.setDead();
			}
			
			/*
			 * Listgrids neuzeichnen
			 */
			((RoundWatcherPage) pagePresenter.getPage()).getWatcherStatusArea().getOthersReadyGrid().redraw();
			((RoundWatcherPage) pagePresenter.getPage()).getWatcherStatusArea().getOthersStateGrid().redraw();
			
			/*
			 * Spiel beenden
			 */
			presenter.getManager().isGameFinished();
		}
		
		return true;
	}
}
