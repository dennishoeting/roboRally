package prototyp.client.view.lobby;

import prototyp.client.view.Page;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;

/**
 * Vorschaubild für die hochgeladenen Profilbilder
 * 
 * @author Andreas (Verantwortlicher)
 * @version 1.0
 * 
 * @see ProfilePage
 */
public class ProfilePictureWindow {
	/** Window */
	private Window window;

	/** Image für die Vorschau */
	private Img previewImage = null;

	/**
	 * Konstruktor.
	 * 
	 * @param picturePath
	 *            Pfad zum Userpicture
	 * 
	 * @throws IllegalArgumentException
	 *             Wird geworfen, wenn der Pfad null oder leer ist.
	 */
	public ProfilePictureWindow(String picturePath)
			throws IllegalArgumentException {
		if (picturePath == null || picturePath.equals("")) {
			throw new IllegalArgumentException();
		}

		// Fenster
		this.window = new Window();
		this.window.setPadding(Integer.valueOf(Page.props
				.global_paddingInStackAreas()));
		this.window.setTitle(Page.props.profilePictureWindow_title());
		this.window.setShowMinimizeButton(false);
		this.window.setIsModal(true);
		this.window.setShowModalMask(true);
		this.window.setAutoSize(true);
		this.window.setAutoCenter(true);
		this.window.setAlign(Alignment.CENTER);
		this.window.setCanDrag(false);
		this.window.setCanDragReposition(false);

		// Vorschaubild
		this.previewImage = new Img(picturePath);
		this.previewImage.setWidth(200);
		this.previewImage.setHeight(200);
		this.previewImage.setLayoutAlign(Alignment.CENTER);
		this.previewImage.setLayoutAlign(VerticalAlignment.CENTER);

		// Img hinzufügen
		this.window.addItem(this.previewImage);

		// Fenster anzeigen
		this.window.show();

		// Listener hinzufügen
		addListener();
	}

	/**
	 * Fügt alle Listener hinzu
	 */
	private void addListener() {
		// Das Fenster kann geschlossen werden
		this.window.addCloseClickHandler(new CloseClickHandler() {
			@Override
			public void onCloseClick(CloseClientEvent event) {
				ProfilePictureWindow.this.window.destroy();
			}
		});

		// Wenn auf das Fenster geklickt wird.
		this.window.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ProfilePictureWindow.this.window.destroy();
			}
		});
	}
}
