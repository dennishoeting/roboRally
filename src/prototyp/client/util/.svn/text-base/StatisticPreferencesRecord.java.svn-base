package prototyp.client.util;

import prototyp.client.view.lobby.ProfilePage;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Hilfsklasse für die Anzeige der Statistik in der {@link ProfilePage}.
 * 
 * @author Andreas Rehfeldt
 * @version 1.0
 */
public class StatisticPreferencesRecord extends ListGridRecord {

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
	public StatisticPreferencesRecord(int type, String title, int description) {
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
	public StatisticPreferencesRecord(int type, String title, String description) {
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
			return "Statistik";
		case 2:
			return "Zuletzt gespielte Spielbretter";
		default:
			return "Sonstiges";
		}
	}

}
