package prototyp.client.util;

import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.IButton;

/**
 * Button mit Radio- und Toggleeigenschaften
 * 
 * @author Marcus
 * @see IButton
 * 
 */
public class RadioToggleButton extends ToggleButton {

	/**
	 * 
	 * @param name
	 * @param radioToggleButtons
	 */
	public static void addButtonsToRadioGroup(String name,
			RadioToggleButton... radioToggleButtons) {
		for (RadioToggleButton btn : radioToggleButtons) {
			btn.setRadioGroup(name);
		}
	}

	/**
	 * Konstruktor für RadioToggleButton mit Titel, Namen und Größe
	 * 
	 * @param image
	 *            = das Bild das auf dem Button dargestellt werden soll
	 * @param name
	 *            = ButtonID
	 * @param size
	 *            = Größe des Buttons
	 */
	public RadioToggleButton(final String image, final String name) {
		this.setSize(24);
		setShowRollOver(false);
		setSrc(image);
		setName(name);
		setActionType(SelectionType.RADIO);
	}
}
