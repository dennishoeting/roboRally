package prototyp.client.view.mapgenerator;

import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Zeigt einen Prozess an, dessen Fortschritt unbekannt ist.
 * 
 * @author Marcus
 * @version 1.0
 */
public class UnknownProgressWindow extends Window {
	/**
	 * Konstruktor
	 * 
	 * @param title
	 *            Titel der Seite
	 */
	public UnknownProgressWindow(String title) {
		setIsModal(true);
		setShowModalMask(true);

		setCanDragReposition(false);
		setCanDragResize(false);

		setAutoCenter(true);

		this.setWidth(120);
		this.setHeight(165);

		setTitle(title);

		setHeaderControls(HeaderControls.HEADER_LABEL);

		final VLayout layout = new VLayout();
		layout.setWidth(256);
		layout.setHeight(55);
		layout.addChild(new Img("ui/UPload_01.gif", 100, 100));

		layout.setTop(30);
		layout.setLeft(10);

		this.addChild(layout);

		centerInPage();

		show();
	}
}
