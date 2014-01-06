package prototyp.client.util;

import com.smartgwt.client.widgets.Label;

/**
 * Hilfsklasse zum Anzeigen Überschriften
 * 
 * @author Dennis Höting (die Sau hat den jDoc natürlich wieder vergessen xD )
 * 
 * @version 1.0
 * @version 1.1 mit jDoc
 * @version 1.2 Headline nach oben verschoben, um diese auszublenden auf der
 *          Logout Seite (provisorisch)
 * 
 * @see Label
 */

public class Headline extends Label {
	/**
	 * Konstruktor
	 * 
	 * @param text
	 *            Text der Headline
	 */
	public Headline(String text) {
		super(text);
		this.setStyleName("headline");

		this.setTop(-35);
		setWidth100();
		setWrap(false);
	}
}
