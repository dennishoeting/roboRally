package prototyp.client.view.lobby;

import prototyp.client.presenter.lobby.LogoutPresenter;
import prototyp.client.util.Button;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.StandardLabel;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;

import com.smartgwt.client.widgets.Window;

/**
 * LogoutPage. Hier befindet sich alles, was für die visuelle Darstellung
 * wichtig ist.
 * 
 * @author Dennis, Marina, Timo (Verantwortlicher)
 * @version 1.1 Alles nicht mehr benötigte gelöscht.
 * 
 * @see LogoutPresenter
 */
public class LogoutPage extends Page {
	// Attribute
	private Button buttonAbort, buttonLogout;
	private HorizontalStack buttonArea;
	private StandardLabel confirmLabel;
	private VerticalStack mainArea;
	private Window winModal;

	/**
	 * Konstruktor
	 */
	public LogoutPage() {
		super(Page.props.logOutPage_title());
		setShowHeadline(false);

		// Erstellen des Windows
		init();
	}

	/**
	 * Liefert den Abbruch-Button
	 * 
	 * @return Abort-Button
	 */
	public Button getButtonAbort() {
		return this.buttonAbort;
	}

	/**
	 * Liefert den Logout-Button
	 * 
	 * @return Logout-Button
	 */
	public Button getButtonLogout() {
		return this.buttonLogout;
	}

	/**
	 * Liefert das confirmLabel
	 * 
	 * @return confirmLabel
	 */
	public StandardLabel getConfirmLabel() {
		return this.confirmLabel;
	}

	/**
	 * Liefert das Fenster beim Logout
	 * 
	 * @return Window
	 */
	public Window getWinModal() {
		return this.winModal;
	}

	/**
	 * Erstellt ein Fenster für das Ausloggen
	 */
	public void init() {
		// Window
		this.winModal = new Window();
		this.winModal.setTitle(Page.props.logOutPage_title());
		this.winModal.setShowMinimizeButton(false);
		this.winModal.setIsModal(true);
		this.winModal.setShowModalMask(true);
		this.winModal.centerInPage();
		this.winModal.setCanDrag(false);
		this.winModal.setCanDragReposition(false);

		// Buttons
		this.buttonAbort = new Button(Page.props.global_title_abort(), 100);
		this.buttonLogout = new Button(Page.props.logOutPage_title(), 100);

		// Areas
		this.mainArea = new VerticalStack(Integer.valueOf(Page.props
				.global_marginBetweenStackAreas()), Integer.valueOf(Page.props
				.global_paddingInStackAreas()));
		this.buttonArea = new HorizontalStack(Integer.valueOf(Page.props
				.global_marginInStackAreas()), 0);
		this.buttonArea.setHeight(this.buttonAbort.getHeight());

		// Label
		this.confirmLabel = new StandardLabel();
		this.confirmLabel.setAutoFit(true);
		this.confirmLabel.setWrap(false);

		// Hinzufügen
		this.buttonArea.addMember(this.buttonLogout);
		this.buttonArea.addMember(this.buttonAbort);
		this.mainArea.addMember(this.confirmLabel);
		this.mainArea.addMember(this.buttonArea);
		this.winModal.addItem(this.mainArea);
		this.winModal.setAutoSize(true);
	}
}
