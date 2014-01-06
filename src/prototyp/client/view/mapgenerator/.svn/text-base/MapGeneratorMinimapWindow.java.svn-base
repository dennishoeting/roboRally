package prototyp.client.view.mapgenerator;

import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;

import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * MapGeneratorMinimapWindow. Hier befindet sich alles, was für die visuelle
 * Darstellung wichtig ist.
 * 
 * @author Dennis (Verantwortlicher)
 * @version 1.0
 */
public class MapGeneratorMinimapWindow extends Window {
	// Attribute
	private VerticalStack panel = new VerticalStack();

	/**
	 * Konstruktor
	 */
	public MapGeneratorMinimapWindow() {
		setTitle(Page.props.mapGeneratorMinimapWindow_title());
		setCanDragReposition(true);
		setCanDragResize(false);
		setOverflow(Overflow.AUTO);
		setHeaderControls(HeaderControls.MINIMIZE_BUTTON,
				HeaderControls.HEADER_LABEL);
		this.addItem(this.panel);
		setAutoSize(true);
	}

	/**
	 * Liefert das Panel
	 * 
	 * @return the panel
	 */
	public VStack getPanel() {
		return this.panel;
	}
}
