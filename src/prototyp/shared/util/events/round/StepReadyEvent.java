package prototyp.shared.util.events.round;

import java.util.Set;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.round.RoundPlayerPagePresenter;
import prototyp.client.presenter.round.RoundWatcherPagePresenter;
import prototyp.client.presenter.round.SlowConnectionWindowPresenter;
import prototyp.client.presenter.user.UserPresenter;
import prototyp.client.view.round.RoundWatcherPage;
import prototyp.shared.round.Robot;

/**
 * Initiiert den nächsten Spielschritt. Bei langsamer Verbindung wird man rausgeschmießen. Alle Roboter, die tot sind, werden noch
 * gelöscht.
 * 
 * @author Marcus
 * @version 1.2
 */
public final class StepReadyEvent implements InternalRoundEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -2193937812993108505L;

	/** Liste mit SpielerIDs */
	private Set<Integer> leftSet;

	/**
	 * Default-Konstruktor
	 */
	public StepReadyEvent() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param leftSet
	 *            Liste mit SpielerIDs
	 */
	public StepReadyEvent(final Set<Integer> leftSet) {
		this.leftSet = leftSet;
	}

	/**
	 * Wird ausgeführt, wenn das Event geworfen wird.
	 * 
	 * @param pagePresenter
	 *            PagePresenter
	 * @return false
	 */
	@Override
	public boolean apply(final PagePresenter pagePresenter) {
		if (pagePresenter instanceof RoundPlayerPagePresenter) {

			final RoundPlayerPagePresenter presenter = (RoundPlayerPagePresenter) pagePresenter;

			/*
			 * Zuerst alle Roter töten, die die Runde verlassen haben
			 */
			if (!this.leftSet.contains(UserPresenter.getInstance().getUser().getId())) {
				for (final int playerId : this.leftSet) {

					/*
					 * entfernen von der DrawingArea und im LogicManager auf Dead setzen
					 */
					final Robot robot = presenter.getManager().getRobots().get(playerId);
					robot.setDead(true);
					robot.setDeadForTurn(true);
					robot.setI(-1);
					robot.setJ(-1);

					presenter.getPage().getDrawingArea().remove(presenter.getRobotsImageList().get(robot));

					presenter.getOthersReadyRecords().get(playerId).setDead();
					presenter.getOthersStateRecords().get(playerId).setDead();

					presenter.getPage().getRobotStatusArea().getOthersReadyGrid().redraw();
					presenter.getPage().getRobotStatusArea().getOthersStateGrid().redraw();
				}

				/*
				 * schauen, ob das Spiel beendet ist
				 */
				if (!presenter.getManager().isGameFinished()) {
					if (presenter.getManager().getAnimationTimer() != null) {
						presenter.getManager().getAnimationTimer().schedule(100);
					}
				}
			} else {

				/*
				 * Rauschmeißen
				 */

				// ChatNachricht schicken, dass man die Runde geschlossen hat.
				presenter.getChatPresenter().sendLeavePostGameMessage(UserPresenter.getInstance().getNickname(),
						presenter.getChatPresenter().getDomain());

				presenter.getCountdownTimer().cancel();
				presenter.getManager().getAnimationTimer().cancel();

				presenter.unlistenRemoteListener();
				
				//Im UserPresenter aktive Spielrunde löschen
				UserPresenter.getInstance().deleteRound(presenter.getRoundInfo().getRoundId());
				
				new SlowConnectionWindowPresenter(presenter);
			}

		} else if (pagePresenter instanceof RoundWatcherPagePresenter) {

			final RoundWatcherPagePresenter presenter = (RoundWatcherPagePresenter) pagePresenter;
			/*
			 * Zuerst alle Roter töten, die die Runde verlassen haben
			 */
			for (final int playerId : this.leftSet) {

				/*
				 * entfernen von der DrawingArea und im LogicManager auf Dead setzen
				 */
				final Robot robot = presenter.getManager().getRobots().get(playerId);
				robot.setDead(true);
				robot.setDeadForTurn(true);
				robot.setI(-1);
				robot.setJ(-1);

				presenter.getPage().getDrawingArea().remove(presenter.getRobotsImageList().get(robot));

				presenter.getOthersReadyRecords().get(playerId).setDead();
				presenter.getOthersStateRecords().get(playerId).setDead();

				((RoundWatcherPage) presenter.getPage()).getWatcherStatusArea().getOthersReadyGrid().redraw();
				((RoundWatcherPage) presenter.getPage()).getWatcherStatusArea().getOthersStateGrid().redraw();
			}

			/*
			 * schauen, ob das Spiel beendet ist
			 */
			if (!presenter.getManager().isGameFinished()) {
				if (presenter.getManager().getAnimationTimer() != null) {
					presenter.getManager().getAnimationTimer().schedule(100);
				}
			}

		}

		return false;
	}

}
