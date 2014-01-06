package prototyp.client.view;

import prototyp.client.properties.Properties;
import prototyp.client.properties.PropertiesDe;
import prototyp.client.util.Headline;
import prototyp.client.util.MainArea;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;

/**
 * Basisklasse jeder Page (jedes Views) des Prototypen.
 * 
 * @author Dennis
 * @version 1.0
 */
public class Page extends Canvas {
	// Attribute
	/** Spracheklasse (Properties) */
	public static Properties props = GWT.create(PropertiesDe.class);

	/** Höhe der Hauptarea */
	public static final int MAIN_AREA_HEIGHT = Integer.valueOf(Page.props.prototyp_childTabManager_height()) - 40 - 35;

	/** Breite der Hauptarea */
	public static final int MAIN_AREA_WIDTH = Integer.valueOf(Page.props.prototyp_childTabManager_width()) - 12;

	/** Überschrift */
	protected Headline headline;

	/** Titel */
	protected String title;

	/** Main-Area */
	private MainArea main;

	/**
	 * Konstruktor
	 * 
	 * @param title
	 *            Titel der Page
	 */
	protected Page(String title) {
		this.title = title;
		this.main = new MainArea();

		// Auf der Startseite einen anderes Hintergrundbild zeigen
		if (this.title.equals(Page.props.frontPage_title())) {
			this.main.setStyleName("frontpage");
		} else {
			// Default-Bild zeigen
			this.main.setStyleName("general");
		}

		setShowHeadline(true);
		addChild(this.main);
	}

	/**
	 * Liefert den Titel der Page
	 * 
	 * @return Titel der Page
	 */
	@Override
	public String getTitle() {
		return this.title;
	}

	/**
	 * Setzt den MainStack der Page.
	 * 
	 * @param mainStack
	 *            MainStack
	 */
	protected final void setMainStack(Canvas mainStack) {
		this.main.addChild(mainStack);
	}

	/**
	 * Setzt die Größe des mainTabs auf die gesamte Tab-Größe oder nicht
	 * 
	 * @param fullTabSize
	 *            true, falls gesamte Tabgröße, false sonst
	 */
	public final void setMainTabSizeToFullTabSize(boolean fullTabSize) {
		this.main.setSizeToFullTabSize(fullTabSize);
	}

	/**
	 * Setzt eine neue Überschrift und fügt sie der Seite hinzu.
	 * 
	 * @param title
	 *            Überschrift
	 */
	public final void setNewHeadline(String title) {
		this.headline = new Headline(title);
		addChild(this.headline);
	}

	/**
	 * Erstellt bzw. entfernt die Headline der Page. Im Konstruktor wird setShowHeadline(true) aufgerufen. Beim Entfernen wird der
	 * Verschiebung des MainPanels nach unten aufgehoben.
	 * 
	 * @param show
	 *            true, falls Headline gezeigt werden soll (und vorher deaktiviert wurde), false, falls Headline entfernt werden
	 *            soll.
	 */
	public final void setShowHeadline(boolean show) {
		if (show) {
			this.headline = new Headline(this.title);
			addChild(this.headline);
		} else {
			removeChild(this.headline);
			this.headline = null;
			this.main.setTop(0);
		}
	}
}
