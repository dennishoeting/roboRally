package prototyp.client.view.chat;

import prototyp.client.util.Button;
import prototyp.client.util.Form;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.form.fields.TextItem;

/**
 * Chatmodul
 * 
 * @author Timo (Verantwortlicher)
 * @version 1.0
 * @version 1.1 (Layout angepasst --Jannik)
 * @version 1.2 Bild hinzugefügt --Andreas
 */
public class ChatArea extends Page {
	// Attribute
	private final HorizontalStack inputArea;
	private final TextItem inputField;
	private final Form inputForm;
	private final VerticalStack mainArea;
	private final HTMLPane messages;
	private final Button buttonSendMessage;

	/**
	 * Konstruktor
	 */
	public ChatArea() {
		super("");
		setMainTabSizeToFullTabSize(false);
		setShowHeadline(false);

		// Areas definieren
		this.mainArea = new VerticalStack(0, 0);
		this.inputArea = new HorizontalStack(0, 0);
		this.inputArea.setHeight(25);
		this.mainArea.setStyleName("chatArea");

		// Forms definieren
		this.inputForm = new Form();
		this.inputForm.setMargin(0);
		this.inputForm.setPadding(0);

		// Feld, in dem alles Geschriebene angezeigt wird
		this.messages = new HTMLPane();
		this.messages.setMargin(0);
		this.messages.setWidth(490);
		this.messages.setHeight(134);
		this.messages.setOverflow(Overflow.AUTO);
		this.messages.setContents("");

		// Eingabefeld
		this.inputField = new TextItem();
		this.inputField.setWidth(500);
		this.inputField.setShowTitle(false);

		// Button zum Abschicken
		this.buttonSendMessage = new Button(Page.props.global_title_send(), 61);
		this.buttonSendMessage.setShowFocused(false);
		this.buttonSendMessage.setMargin(0);

		// Den Forms die Felder zuweisen
		this.inputForm.setFields(this.inputField);
		this.inputForm.setAutoFocus(true);

		// Alles zusammenfügen
		this.inputArea.addMembers(this.inputForm, this.buttonSendMessage);
		this.inputArea.setTop(100);
		this.mainArea.addMembers(this.messages, this.inputArea);

		addChild(this.mainArea);
	}

	/**
	 * Liefert den SendMessage Button
	 * 
	 * @return buttonSendMessage
	 */
	public Button getButtonSendMessage() {
		return this.buttonSendMessage;
	}

	/**
	 * Liefert die ChatArea als HTMLPane
	 * 
	 * @return ChatArea
	 */
	public HTMLPane getChatTextArea() {
		return this.messages;
	}

	/**
	 * Liefert das MessageField zum Eingeben von Chatnachrichten
	 * 
	 * @return InputField
	 */
	public TextItem getMessageField() {
		return this.inputField;
	}

	/**
	 * Setzt die Höhe des Views. Darf nicht kleiner als 120px sein.
	 */
	@Override
	public void setHeight(int height) {
		GWT.log("Höhe wurde auf " + height + " gesetzt.");

		if (height < 120) {
			height = 120;
		}
		this.mainArea.setHeight(height);
		this.messages.setHeight(height - 32);
	}

	/**
	 * Setzt die Breite des Views. Darf nicht kleiner als 160px sein.
	 */
	@Override
	public void setWidth(int width) {
		if (width < 160) {
			width = 160;
		}
		this.mainArea.setWidth(width);
		this.messages.setWidth(width);
		this.inputField.setWidth(width - 60);
	}
}
