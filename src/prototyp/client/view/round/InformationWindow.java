package prototyp.client.view.round;

import prototyp.client.util.Button;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.StandardLabel;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;

import com.google.gwt.user.client.Timer;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * Dies ist ein Window für verschiedene Situationen. Dieser Window verschwindet nach einiger Zeit wieder. Es wird z.B. angezeigt,
 * wenn ein Roboter endgültig stirbt bzw. keine Lebenspunkte mehr hat.
 * 
 * @author Andreas
 * @version 1.1
 * 
 */
public final class InformationWindow extends Window {

	/**
	 * Zeigt das Window an, d.h. diese Klasse wird initialisiert.
	 * 
	 * @param parent
	 *            Vater-Canvas
	 * @param title
	 *            Titel des Windows
	 * @param text
	 *            Text, der über das Bild angezeigt werden soll
	 * @param image
	 *            Dateipfad zum Bild
	 */
	public static void showWindow(final Canvas parent, final String title, final String text, final String image) {
		parent.addChild(new InformationWindow(title, text, image));
	}

	/** Label */
	private final StandardLabel labelText;

	/** Schließen-Button */
	private final Button buttonClose;

	/** Bild */
	private final Img picture;

	/** Timer */
	private final Timer timer;

	/**
	 * Konstruktor
	 * 
	 * @param text
	 *            Text, der im Label gezeigt werden soll
	 */
	private InformationWindow(String title, String text, String image) {
		setTitle(title); // Titel angegeben
		setHeaderControls(HeaderControls.HEADER_LABEL);

		/*
		 * Einzelne Objekte
		 */

		this.labelText = new StandardLabel(text);
		this.labelText.setWrap(false);
		this.labelText.setAlign(Alignment.CENTER);
		this.labelText.setWidth(240);

		HorizontalStack labelArea = new HorizontalStack(0, 0);
		labelArea.setWidth(240);
		labelArea.setHeight(30);
		labelArea.setAlign(Alignment.CENTER);
		labelArea.setMembers(this.labelText);

		// Grafik zeigen
		this.picture = new Img(image);
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

		// Listener hinzufügen
		addListener();

		/*
		 * Timer starten (Danach wird das Window wieder entfernt)
		 */
		this.timer = new Timer() {
			/** Zeitangabe */
			private int i = 4;

			@Override
			public void run() {
				// Abbruchbedingung des Timers
				if (this.i == 0) {
					InformationWindow.this.destroy();
					cancel();

					return;
				}

				this.i = this.i - 1;
				schedule(1000);
			}
		};
		// Timer soll sofort anfangen
		this.timer.schedule(1);
	}

	/**
	 * Fügt die Listener hinzu (Muss eigentlich in einen Presenter)
	 */
	private void addListener() {
		// Schließen
		this.buttonClose.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				InformationWindow.this.timer.cancel();

				// Fenster löschen
				InformationWindow.this.destroy();
			}
		});
	}
}
