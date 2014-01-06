package prototyp.shared.exception.mapGenerator;

import prototyp.client.view.Page;
import prototyp.shared.exception.RoboException;

/**
 * Wird geworfen, wenn die Größe der Karte falsch ist
 * 
 * @author Marcus
 * @version 1.0
 *
 */
public class MapSizeException extends RoboException {
	/** Typ zu groß */
	public static final int TOO_LARGE = 0;

	/** Typ zu klein */
	public static final int TOO_SMALL = 1;
	
	/** Typ der Exception */
	private int type;

	/**
	 * generierte serialVersionUID
	 */
	private static final long serialVersionUID = 5884659897496658367L;

	/**
	 * Konstruktor
	 * 
	 * @param type
	 *            TOO_LARGE oder TOO_SMALL
	 */
	public MapSizeException(int type) {
		this.type = type;
	}

	/**
	 * Liefert die Fehlernachricht
	 * 
	 * @return Fehlernachricht
	 */
	@Override
	public String getMessage() {
		return (type == MapSizeException.TOO_LARGE ? Page.props.mapGeneratorPopUp_mapSizeException_large() : Page.props
				.mapGeneratorPopUp_mapSizeException_small());
	}
	
	
}
