package prototyp.client.view.pregame;

import prototyp.client.presenter.pregame.PreGameGameInitiatorPagePresenter;
import prototyp.client.util.Button;
import prototyp.client.view.Page;

/**
 * Erweitert PreGamePage
 * 
 * @author Dennis, Jens, Timo (Verantwortlicher), Jannik (Verantwortlicher)
 * @version 1.1
 * @version 1.2 RemoveWatcherPlayer Button -Timo 26.10.10
 * 
 * @see PreGameGameInitiatorPagePresenter
 */
public class PreGameGameInitiatorPage extends PreGameAbstractPage {
	private Button buttonStart, buttonKickPlayer;

	public PreGameGameInitiatorPage() {
		super(Page.props.preGameGameInitiatorPage_title());

		// Start
		this.buttonStart = new Button(
				Page.props.preGameGameInitiatorPage_buttonStart_title(),
				Integer.valueOf(Page.props.global_buttonWidth()));
		this.buttonArea.addMembers(this.buttonStart, this.buttonCloseTab);

		// Remove Button
		this.buttonKickPlayer = new Button(
				Page.props
						.preGameGameInitiatorPage_buttonRemovePlayerWatcher_title(),
				Integer.valueOf(Page.props.global_buttonWidth()));
		this.playerListArea.addMember(this.buttonKickPlayer);
	}

	/**
	 * Liefert einen Button zum Löschen von Spielern und Watchern aus dem
	 * PreGame
	 * 
	 * @return RemovePlayerWatcherButton
	 */
	public Button getButtonKickPlayer() {
		return this.buttonKickPlayer;
	}

	/**
	 * Liefert den Start-Button (hat nur der GI)
	 * 
	 * @return Start-Button
	 */
	public Button getButtonStart() {
		return this.buttonStart;
	}

}
