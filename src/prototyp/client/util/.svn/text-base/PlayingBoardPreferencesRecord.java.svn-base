package prototyp.client.util;

import prototyp.client.view.Page;
import prototyp.client.view.pregame.RefereePage;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Hilfsklasse für die Anzeige der Spielbrettinformationen in der {@link RefereePage}.
 * 
 * @author Andreas Rehfeldt
 * @version 1.0
 */
public class PlayingBoardPreferencesRecord extends ListGridRecord {

	/**
	 * Konstruktor
	 * 
	 * @param title
	 *            Eigenschaft
	 * @param description
	 *            Beschreibung (true = aktiviert, false = deaktiviert)
	 */
	public PlayingBoardPreferencesRecord(String title, boolean description) {
		setAttribute("optionTitle", title);
		if (description) {
			setAttribute("optionDescription", Page.props.global_title_on());
		} else {
			setAttribute("optionDescription", Page.props.global_title_off());
		}
		setAttribute("optionType", Page.props.previewArea_roundPreferencesRecord_playingBoard());
	}

	/**
	 * Konstruktor
	 * 
	 * @param title
	 *            Eigenschaft
	 * @param description
	 *            Beschreibung (Integerwert)
	 */
	public PlayingBoardPreferencesRecord(String title, int description) {
		setAttribute("optionTitle", title);
		setAttribute("optionDescription", description);
		setAttribute("optionType", Page.props.previewArea_roundPreferencesRecord_playingBoard());
	}

	/**
	 * Konstruktor
	 * 
	 * @param title
	 *            Eigenschaft
	 * @param description
	 *            Beschreibung
	 */
	public PlayingBoardPreferencesRecord(String title, String description) {
		setAttribute("optionTitle", title);
		setAttribute("optionDescription", description);
		setAttribute("optionType", Page.props.previewArea_roundPreferencesRecord_playingBoard());
	}
}
