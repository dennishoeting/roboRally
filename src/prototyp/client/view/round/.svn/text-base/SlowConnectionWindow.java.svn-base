package prototyp.client.view.round;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.round.SlowConnectionWindowPresenter;
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
 * View, der angezeigt wird, wenn ein Spieler aufgrund einer zu langsamen Internetverbindung aus einer laufenden Spielrunde
 * geworfen wurde
 * 
 * @author Marcus
 * @version 1.0
 * 
 * @see SlowConnectionWindowPresenter
 */
public class SlowConnectionWindow extends Window {

	/** Label */
	private final StandardLabel labelText;

	/** Schließen-Button */
	private Button buttonClose;

	/** Bild */
	private final Img picture;

	/**
	 * Konstruktor
	 */
	public SlowConnectionWindow(PagePresenter presenter) {
		setTitle(Page.props.slowConnectionWindowTitle()); // Titel angegeben
		setHeaderControls(HeaderControls.HEADER_LABEL);

		/*
		 * Einzelne Objekte
		 */

		this.labelText = new StandardLabel(Page.props.roundErrorSlowConnection());
		this.labelText.setWrap(false);
		this.labelText.setAlign(Alignment.CENTER);
		this.labelText.setWidth(240);

		HorizontalStack labelArea = new HorizontalStack(0, 0);
		labelArea.setWidth(240);
		labelArea.setHeight(30);
		labelArea.setAlign(Alignment.CENTER);
		labelArea.setMembers(this.labelText);

		// Grafik zeigen
		this.picture = new Img("ui/robot_Lost.png");
		this.picture.setWidth(132);
		this.picture.setHeight(100);
		this.picture.setAlign(Alignment.CENTER);

		this.buttonClose = new Button(Page.props.global_title_close());
		this.buttonClose.setAlign(Alignment.CENTER);

		// Areas
		HorizontalStack imgArea = new HorizontalStack(Integer.valueOf(Page.props.global_marginInStackAreas()),
				Integer.valueOf(Page.props.global_paddingInStackAreas()));
		imgArea.setWidth(240);
		imgArea.setAlign(Alignment.CENTER);
		imgArea.setMembers(this.picture);

		HorizontalStack buttons = new HorizontalStack(Integer.valueOf(Page.props.global_marginInStackAreas()),
				Integer.valueOf(Page.props.global_paddingInStackAreas()));
		buttons.setWidth(240);
		buttons.setAlign(Alignment.CENTER);
		buttons.setMembers(this.buttonClose);

		VerticalStack vStack = new VerticalStack(Integer.valueOf(Page.props.global_marginInStackAreas()),
				Integer.valueOf(Page.props.global_paddingInStackAreas()));
		vStack.setTop(20);
		vStack.setMembers(labelArea, imgArea, buttons);

		// Hinzufügen
		this.addChild(vStack);

		// Eigenschaften des Windows
		setWidth(270);
		setHeight(240);
		setAutoCenter(true);
		setCanDrag(false);
		setCanDragReposition(false);

		presenter.getPage().addChild(this);
	}

	/**
	 * Liefert den Schließen-Button
	 * 
	 * @return the buttonClose
	 */
	public Button getButtonClose() {
		return buttonClose;
	}
}
