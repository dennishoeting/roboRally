package prototyp.client.util;

import prototyp.client.view.Page;
import prototyp.client.view.lobby.LobbyPage;
import prototyp.client.view.pregame.PreGameAbstractPage;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Hilfsklasse für die Anzeige der Rundenoption und Spielbrettinformationen in der {@link LobbyPage} und
 * {@link PreGameAbstractPage}.
 * 
 * @author Andreas Rehfeldt
 * @version 1.0
 */
public class RoundPreferencesRecord extends ListGridRecord {

	/**
	 * Default-Konstruktor
	 */
	public RoundPreferencesRecord() {
		setAttribute("optionType", "");
		setAttribute("optionTitle", "");
		setAttribute("optionDescription", "");
	}

	/**
	 * Konstruktor
	 * 
	 * @param type
	 *            Typ des Eintrages
	 * @param title
	 *            Titel
	 * @param description
	 *            Beschreibung als Boolean
	 */
	public RoundPreferencesRecord(int type, String title, boolean description) {
		setAttribute("optionType", getType(type));
		setAttribute("optionTitle", title);
		if (description) {
			setAttribute("optionDescription", Page.props.global_title_on());
		} else {
			setAttribute("optionDescription", Page.props.global_title_off());
		}
	}

	/**
	 * Konstruktor
	 * 
	 * @param type
	 *            Typ des Eintrages
	 * @param title
	 *            Titel
	 * @param description
	 *            Beschreibung als Integer
	 */
	public RoundPreferencesRecord(int type, String title, int description) {
		setAttribute("optionType", getType(type));
		setAttribute("optionTitle", title);
		setAttribute("optionDescription", description);
	}

	/**
	 * Konstruktor
	 * 
	 * @param type
	 *            Typ des Eintrages
	 * @param title
	 *            Titel
	 * @param description
	 *            Beschreibung
	 */
	public RoundPreferencesRecord(int type, String title, String description) {
		setAttribute("optionType", getType(type));
		setAttribute("optionTitle", title);
		setAttribute("optionDescription", description);
	}

	/**
	 * Liefert den Type als String
	 * 
	 * @param type
	 *            Type des Eintrages
	 * @return Titel des Typs
	 */
	private String getType(int type) {
		switch (type) {
		case 1:
			return Page.props.previewArea_roundPreferencesRecord_playingBoard();
		case 2:
			return Page.props.previewArea_roundPreferencesRecord_roundOptions();
		default:
			return Page.props.previewArea_roundPreferencesRecord_other();
		}
	}

}
