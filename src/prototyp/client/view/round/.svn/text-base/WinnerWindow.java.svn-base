package prototyp.client.view.round;

import prototyp.client.util.Button;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.StandardLabel;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Window;

/**
 * Zeigt den Gewinner-Dialog an.
 * 
 * @author Marcus, Andreas, Robert
 * @version 1.0
 * @version 1.1 Umgestaltet und den Timer verändert
 * @version 1.2 Funktionalität erweitert und in Presenter getrennt - wird nun auch bei award/rang gewonnen benötigt
 * 
 */
public final class WinnerWindow extends Window {
	// Attribute
	private Button buttonOkay;
	private StandardLabel labelInformation;
	private Img image;
	private HorizontalStack images, buttons;
	private VerticalStack vStack;

	/**
	 * Konstruktor
	 */
	public WinnerWindow() {
		 // Schließen und  Minimieren ist dann weg
		setHeaderControls(HeaderControls.HEADER_LABEL);

		// Objekte
		this.labelInformation = new StandardLabel();
		this.labelInformation.setWrap(false);

		this.buttonOkay = new Button(Page.props.global_title_continue());
		this.buttonOkay.setAlign(Alignment.CENTER);

		// Img
		this.image = new Img("", 150, 150);
		this.image.setAlign(Alignment.CENTER);
		this.image.setLeft(85);

		// Areas
		this.images = new HorizontalStack();
		this.images.setWidth(220);
		this.images.setAlign(Alignment.CENTER);
		this.images.addMember(this.image);
		this.images.setTop(75);

		this.buttons = new HorizontalStack();
		this.buttons.setWidth(220);
		this.buttons.setAlign(Alignment.CENTER);
		this.buttons.addMember(this.buttonOkay);
		this.buttons.setTop(233);

		this.vStack = new VerticalStack(Integer.valueOf(Page.props.global_marginInStackAreas()), Integer.valueOf(Page.props
				.global_paddingInStackAreas()));
		this.vStack.addMember(this.labelInformation);
		this.vStack.addChild(this.images);
		this.vStack.addChild(this.buttons);
		this.vStack.setTop(-20);

		// Hinzufügen
		this.addChild(this.vStack);

		// Eigenschaften des Fensters
		this.setWidth(250);
		this.setHeight(250);
		this.setAutoCenter(true);
		
		setCanDrag(false);
		setCanDragReposition(false);
	}

	public Button getButtonOkay() {
		return this.buttonOkay;
	}

	public Img getImage() {
		return this.image;
	}

	public StandardLabel getLabelInformation() {
		return this.labelInformation;
	}

}
