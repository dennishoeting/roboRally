package prototyp.shared.util.events.round;

import java.util.List;
import java.util.Map;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.round.RoundPlayerPagePresenter;
import prototyp.client.presenter.round.RoundPresenterInterface;
import prototyp.shared.programmingcard.Programmingcard;

/**
 * Wird geworfen, wenn alle Ready sind und dann der Spielschritt ausgeführt werden kann.
 * 
 * @author Marcus
 * @version 1.0
 * 
 */
public class CardsSendEvent implements InternalRoundEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 7400794086793150155L;

	/** Map mit SpielerID und Programmierkartenliste */
	private Map<Integer, List<Programmingcard>> hMap;

	/**
	 * Default-Konstruktor
	 */
	public CardsSendEvent() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param hMap
	 *            Map mit den Programmierkarten
	 */
	public CardsSendEvent(Map<Integer, List<Programmingcard>> hMap) {
		this.hMap = hMap;
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
		if (pagePresenter instanceof RoundPresenterInterface) {
			RoundPresenterInterface presenter = (RoundPresenterInterface) pagePresenter;

			/*
			 * Animationen werden ausgeführt
			 */
			if (!presenter.getManager().isGameFinished()) {

				/*
				 * Areas organisieren
				 */
				presenter.setAreaState(RoundPlayerPagePresenter.ACTIVE_ROUND);

				((RoundPresenterInterface) pagePresenter).getManager().moveAllRobots(this.hMap);
			}
		}

		return true;
	}

}
