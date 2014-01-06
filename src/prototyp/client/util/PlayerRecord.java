package prototyp.client.util;

import prototyp.client.view.Page;
import prototyp.shared.round.Player;
import prototyp.shared.round.Watcher;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Hilfsklasse für die Anzeige der Player im PreGame
 * 
 * @author Jannik
 * @version 1.0 (28.9.10)
 * @version 1.1 (21.10.10) Nimmt jetzt Watcher anstatt Player auf, um 1) Beides
 *          aufnehmen zu können 2) zu unterscheiden -Timo
 * @version 1.2 (09.11.10) Properties hinzugefügt. -Timo
 * @version 1.3. 20.12.10 Fragezeichen Images bei nicht gewählter Color -Timo
 */
public class PlayerRecord extends ListGridRecord {
	/** playerKey */
	private int playerKey;

	/**
	 * Konstruktor.
	 * 
	 * @param key
	 * @param watcher
	 */
	public PlayerRecord(Integer key, Watcher watcher) {
		this.playerKey = key;

		setAttribute("userID", watcher.getUser().getId());
		setAttribute("nickname", watcher.getUser().getAccountData()
				.getNickname());

		// Handelt es sich um einen Player oder Watcher?
		if (watcher instanceof Player) {

			// Farbvorschau setzen
			if (((Player) watcher).getRobot().getColor() == null) {
				setAttribute("color", "colorpreview/icon-Fragezeichen.png");
			} else {
				setAttribute("color", "colorpreview/"
						+ ((Player) watcher).getRobot().getColor()
								.getColorPreviewName() + ".png");
			}

			// Bereit?
			if (((Player) watcher).isReady()) {
				setAttribute("ready", Page.props.playerRecord_ready());
			} else {
				setAttribute("ready", Page.props.playerRecord_notReady());
			}

			setAttribute("observer", Page.props.playerRecord_isPlayer());

		} else {
			setAttribute("color", "");
			setAttribute("ready", Page.props.playerRecord_readyWatcher());
			setAttribute("observer", Page.props.playerRecord_observer());
		}
	}

	/**
	 * @return the roundID
	 */
	public int getRoundID() {
		return this.playerKey;
	}
}
