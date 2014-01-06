package prototyp.client.view.lobby;

import prototyp.client.view.Page;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;

/**
 * Vorschaubild für die ausgewählten Spielbretter
 * 
 * @author Andreas (Verantwortlicher)
 * @version 1.0
 * 
 * @see RefereePage, Lobby, PreGame
 */
public class MapPreviewWindow {

	/** Window */
	private Window window;

	/** Image für die Vorschau */
	private final Img previewImage = new Img();

	/**
	 * Initialisiert und zeigt das Fenster an.
	 * 
	 * @param image
	 *            Vorschauimage
	 * 
	 * @throws IllegalArgumentException
	 *             Wird geworfen, wenn das Bild null ist.
	 */
	public void show(Img image, int width, int height) throws IllegalArgumentException {
		if (image == null) {
			throw new IllegalArgumentException();
		}

		// Fenster
		this.window = new Window();
		this.window.setPadding(Integer.valueOf(Page.props.global_paddingInStackAreas()));
		this.window.setTitle(Page.props.global_selectedPlayingBoard());
		this.window.setShowMinimizeButton(false);
		this.window.setIsModal(true);
		this.window.setShowModalMask(true);
		this.window.setAutoSize(true);
		this.window.setAutoCenter(true);
		this.window.setAlign(Alignment.CENTER);
		this.window.setCanDrag(false);
		this.window.setCanDragReposition(false);

		// Berechnung der Größen
		float actwidth = width * 50;
		float actheight = height * 50;
		final int previewSize = 500;
		float factor = 1;

		// Größe ermitteln
		if (actwidth == actheight) { // Ist gleich groß, muss dann nicht skaliert werden
			actwidth = previewSize;
			actheight = previewSize;
		} else if (actwidth > previewSize || actheight > previewSize) { // Muss angepasst werden
			while ((actwidth > previewSize) || (actheight > previewSize)) {
				// Verkleinerungsfaktor ausrechnen
				factor = (actwidth > actheight) ? 1 - ((actwidth - previewSize) / actwidth) : 1 - ((actheight - previewSize) / actheight);
				
				// Faktor anwenden
				actwidth *= factor;
				actheight *= factor;
			}
		}

		// Vorschaubild
		this.previewImage.setSrc(image.getSrc());
		this.previewImage.setCursor(Cursor.HAND);
		this.previewImage.setWidth((int) actwidth);
		this.previewImage.setHeight((int) actheight);
		this.previewImage.setBorder("1px solid yellow");
		this.previewImage.setLayoutAlign(Alignment.CENTER);
		this.previewImage.setLayoutAlign(VerticalAlignment.CENTER);

		// Img hinzufügen
		this.window.addItem(this.previewImage);

		// Fügt die Listener hinzu
		addListener();

		// Fenster anzeigen
		this.window.show();
	}
	
	/**
	 * Fügt alle Listener hinzu
	 */
	private void addListener() {
		// Das Fenster kann geschlossen werden
		this.window.addCloseClickHandler(new CloseClickHandler() {
			@Override
			public void onCloseClick(CloseClientEvent event) {
				MapPreviewWindow.this.window.destroy();
			}
		});

		// Wenn auf das Fenster geklickt wird.
		this.window.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MapPreviewWindow.this.window.destroy();
			}
		});
	}

}
