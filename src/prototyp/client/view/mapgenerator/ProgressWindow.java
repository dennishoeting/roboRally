package prototyp.client.view.mapgenerator;

import prototyp.client.util.StandardLabel;
import prototyp.client.view.Page;

import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.widgets.Progressbar;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Fortschschrittsfenster
 * 
 * @author Marcus, Dennis (Verantworlicher)
 * @version 1.0
 */
public class ProgressWindow extends Window {
	// Attribute
	private Progressbar progressbar;
	private StandardLabel label;

	/**
	 * Konstruktor
	 */
	public ProgressWindow(String title) {
		
		setShowModalMask(true);
		setIsModal(true);

		this.setWidth(230);
		this.setHeight(100);
		setAutoCenter(true);

		setTitle(title);

		setHeaderControls(HeaderControls.HEADER_LABEL);

		// Das Layout
		final VLayout layout = new VLayout(3);
		layout.setWidth(200);
		layout.setHeight(40);

		// Das label
		this.label = new StandardLabel(Page.props.progressBar_mainLabel());
		this.label.setHeight(16);
		layout.addMember(this.label);

		// Die Progressbar
		this.progressbar = new Progressbar();
		this.progressbar.setHeight(20);
		this.progressbar.setVertical(false);
		layout.addMember(this.progressbar);

		layout.setTop(30);
		layout.setLeft(10);

		this.addChild(layout);

		centerInPage();
		
		setCanDrag(false);
		setCanDragReposition(false);

		show();

	}

	/**
	 * Liefert das Label
	 * 
	 * @return label
	 */
	public StandardLabel getLabel() {
		return this.label;
	}

	/**
	 * Liefert die Progressbar
	 * 
	 * @return the progressbar
	 */
	public Progressbar getProgressbar() {
		return this.progressbar;
	}
}
