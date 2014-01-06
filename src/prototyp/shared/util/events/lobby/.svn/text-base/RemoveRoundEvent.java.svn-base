package prototyp.shared.util.events.lobby;

import prototyp.client.presenter.lobby.LobbyPagePresenter;
import prototyp.client.view.lobby.LobbyPage;


/**
 * Wird geworfen sobald eine Runde gelöscht werden soll.
 * 
 * @author timo
 * 
 */
public class RemoveRoundEvent implements LobbyEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 996930312159727151L;

	/**
	 * RoundID
	 */
	private int roundID;

	/**
	 * Default
	 */
	public RemoveRoundEvent() {

	}

	/**
	 * Konstruktor
	 * 
	 * @param roundID
	 *            RundenID
	 */
	public RemoveRoundEvent(int roundID) {
		this.roundID = roundID;
	}

	@Override
	public boolean apply(LobbyPagePresenter lobbyPagePresenter) {
		// Löscht nur die RoundInfo
		if (lobbyPagePresenter.getRoundInfos().get(this.roundID) != null) {

			// Falls die Runde Selektiert ist dann rechte Seite und Buttons neu laden
			if (lobbyPagePresenter.getSelectedRound() != null
					&& lobbyPagePresenter.getSelectedRound().getRoundId() == this.roundID) {

				// PreviewArea wiederherstellen
				((LobbyPage) lobbyPagePresenter.getPage()).getPreviewArea().showDefaultArea();

				// Selected Round auf Null setzen
				lobbyPagePresenter.setSelectedRound(null);
			}

			// Runde löschen
			lobbyPagePresenter.getRoundInfos().remove(this.roundID);
		}

		return false;
	}

}
