package prototyp.client.util;

import prototyp.client.view.Page;

import com.smartgwt.client.widgets.layout.Layout;

/**
 * MainLayout in allen Views, Erweiterung von Layout
 * 
 * @author Dennis
 * @version 1.0
 * @see Layout
 */
public class MainArea extends Layout {
	// private final Properties props = GWT.create(Properties.class);

	/**
	 * Konstruktor, Verschiebung um 40px nach unten
	 */
	public MainArea() {
		super();
		setSizeToFullTabSize(true);
		this.setTop(40);
	}

	/**
	 * Setzt die maximal verfügbare Größe eines Tabs fest
	 * 
	 * @default true
	 * @param fullTabSize
	 */
	public void setSizeToFullTabSize(boolean fullTabSize) {
		if (fullTabSize) {
			this.setWidth(Page.MAIN_AREA_WIDTH);
			this.setHeight(Page.MAIN_AREA_HEIGHT);
		} else {
			setAutoWidth();
			setAutoHeight();
		}
	}
}
