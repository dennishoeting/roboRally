package prototyp.client.util;

import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.ImgButton;

public class ToggleButton extends ImgButton {

	protected String name = "";

	public ToggleButton() {
	}

	/**
	 * Konstruktor für einen ToggleButton mit titel und namen
	 * 
	 * @param title
	 *            = Titel der auf dem Button angezeigt wird
	 * @param name
	 *            = ButtonID
	 */
	public ToggleButton(String image, String name) {
		setSrc(image);
		this.name = name;
		this.setWidth(24);
		this.setHeight(24);
		setShowRollOver(false);
		setActionType(SelectionType.CHECKBOX);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

}
