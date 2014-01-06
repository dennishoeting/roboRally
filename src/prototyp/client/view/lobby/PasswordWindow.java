package prototyp.client.view.lobby;

import prototyp.client.util.Button;
import prototyp.client.util.Form;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.fields.PasswordItem;

/**
 * Wird aufgerufen, wenn der Nickname geändert werden soll. Dazu benötigt man eine Änderung des Passworts, da man sich sonst nicht
 * mehr einloggen kann.
 * 
 * @author Andreas
 * @version 1.0
 * 
 * @see ProfilePage
 * 
 */
public class PasswordWindow extends Window {

	// Buttons
	private final Button buttonOk, buttonAbort;

	/** Passwortfeld */
	private final PasswordItem passwordField;

	/** Area für das Passwortfeld */
	private final Form inputArea;

	/**
	 * Konstruktor
	 */
	public PasswordWindow() {
		this.setHeaderControls(HeaderControls.HEADER_LABEL);
		this.setIsModal(true);
		this.setShowModalMask(true);
		this.setTitle(Page.props.passwordWindow_title());

		// Areas
		VerticalStack mainArea = new VerticalStack();
		mainArea.setTop(20);
		HorizontalStack buttons = new HorizontalStack(Integer.valueOf(Page.props.global_marginInStackAreas()), 0);
		buttons.setWidth(180);
		buttons.setAlign(Alignment.CENTER);
		this.inputArea = new Form();
		this.inputArea.setWidth(180);
		this.inputArea.setAlign(Alignment.CENTER);
		this.inputArea.setAutoFocus(true);

		// Buttons
		this.buttonOk = new Button(Page.props.global_title_continue());
		this.buttonOk.setAlign(Alignment.CENTER);
		this.buttonAbort = new Button(Page.props.global_title_abort());
		this.buttonAbort.setAlign(Alignment.CENTER);

		// Feld
		this.passwordField = new PasswordItem(Page.props.global_title_password());
		this.passwordField.setRequired(true);
		this.passwordField.setRequiredMessage(Page.props.global_title_requiredMessage());
		this.passwordField.setWidth(100);

		// Hinzufügen
		this.inputArea.setFields(this.passwordField);
		buttons.addMembers(this.buttonOk, this.buttonAbort);
		mainArea.addMembers(this.inputArea, buttons);
		this.addChild(mainArea);

		// Fenstereigenschaften
		this.setWidth(200);
		this.setHeight(120);

		this.centerInPage();

		this.setCanDrag(false);
		this.setCanDragReposition(false);

		this.show();
	}

	/**
	 * Liefert den Weiter-Button
	 * 
	 * @return the buttonOk
	 */
	public Button getButtonOk() {
		return buttonOk;
	}

	/**
	 * Liefert den Abbreche-Button
	 * 
	 * @return the buttonAbort
	 */
	public Button getButtonAbort() {
		return buttonAbort;
	}

	/**
	 * Liefert das Passwortfeld
	 * 
	 * @return the passwordField
	 */
	public PasswordItem getPasswordField() {
		return passwordField;
	}

	/**
	 * Validiert die Inputfelder
	 * 
	 * @return true, wenn alles okay ist, ansonsten false;
	 */
	public boolean validate() {
		return this.inputArea.validate();
	}
}
