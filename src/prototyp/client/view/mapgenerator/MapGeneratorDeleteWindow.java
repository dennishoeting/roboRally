package prototyp.client.view.mapgenerator;

import prototyp.client.view.Page;

/**
 * Window für das Löschen von Spielbrettern
 * 
 * @author Marcus
 * @version 1.0
 * 
 */
public class MapGeneratorDeleteWindow extends MapGeneratorAbstractLoadDeleteWindow {

	/**
	 * Liefert für den Button den Titel "Löschen"
	 * 
	 * @return String
	 */
	@Override
	protected String getButtonActionText() {
		return Page.props.mapGeneratorDeletePopUp_deleteButton();
	}

}
