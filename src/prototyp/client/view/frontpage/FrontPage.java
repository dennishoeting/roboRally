package prototyp.client.view.frontpage;

import prototyp.client.presenter.frontpage.FrontPagePresenter;
import prototyp.client.util.Button;
import prototyp.client.util.Form;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.StandardLabel;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Progressbar;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;

/**
 * FrontPage. Hier befindet sich alles, was für die visuelle Darstellung wichtig ist.
 * 
 * @author Marina, Robert, Kamil (Verantwortlicher), Andreas
 * @version 1.3: Marina, 26.09.2010: Passwort vergessen - Cursor hinzugefügt
 * @version 1.4 Buttons werden deaktiviert beim Login --Andreas (29.09)
 * @version 1.5 Passwort-Vergessen Fenster hinzugefügt (Marina)
 * 
 * @see FrontPagePresenter
 */
public class FrontPage extends Page {
	// Attribute
	private Button buttonLogIn, buttonRegister, buttonSend, buttonAbort;
	private HorizontalStack buttonPanel, buttonPanelSendPassword;
	private VerticalStack mainArea;
	private StandardLabel buttonSendPassword;
	private Form loginForm;
	private DynamicForm emailarea;
	private TextItem nicknameField, eMailField;
	private PasswordItem passwordField;
	private RegExpValidator emailValidator;
	private Window winModal;
	private Img flaggeGerman, flaggeEnglish;

	private final Progressbar progressbar;

	/**
	 * Konstruktor
	 */
	public FrontPage() {
		super(Page.props.frontPage_title());

		// Main Panel
		this.mainArea = new VerticalStack();
		this.mainArea.setTop(150);
		this.mainArea.setLeft(350);
		this.mainArea.setStyleName("logInArea");
		this.mainArea.setGroupTitle(Page.props.frontPage_login_text());
		this.mainArea.setIsGroup(true);
		this.mainArea.setPadding(Integer.valueOf(Page.props.global_paddingInStackAreas()));

		// Dynamic Form
		this.loginForm = new Form();
		this.loginForm.setAutoFocus(true);
		this.loginForm.setWidth(200);

		// Button Panel

		this.buttonPanel = new HorizontalStack();
		this.buttonPanel.setPadding(Integer.valueOf(Page.props.global_paddingInStackAreas()));
		this.buttonPanel.setHeight(35);
		this.buttonPanel.setMembersMargin(Integer.valueOf(Page.props.global_paddingInStackAreas()));
		this.buttonPanel.setStyleName("buttonArea");

		// Nickname-Feld
		this.nicknameField = new TextItem(Page.props.global_title_nickname());
		this.nicknameField.setRequired(true);
		this.nicknameField.setRequiredMessage(Page.props.global_title_requiredMessage());

		// Passwort-Feld
		this.passwordField = new PasswordItem(Page.props.global_title_password());
		this.passwordField.setRequired(true);
		this.passwordField.setRequiredMessage(Page.props.global_title_requiredMessage());

		// Registrierungsbutton
		this.buttonRegister = new Button(Page.props.global_title_register(), 100);

		// Loginbutton
		this.buttonLogIn = new Button(Page.props.global_title_logIn(), 100);

		// "Password senden"-Button
		this.buttonSendPassword = new StandardLabel(Page.props.frontPage_buttonSendPassword_text());

		this.buttonSendPassword.setCursor(Cursor.HAND);

		// Progressbar
		this.progressbar = new Progressbar();
		this.progressbar.setMargin(3);
		this.progressbar.setAutoWidth();
		this.progressbar.setVertical(false);
		this.progressbar.setVisible(false);
		this.progressbar.setLength(240);
		this.progressbar.setHeight(10);

		// Flaggen
		HorizontalStack flags = new HorizontalStack(5, 5);
		this.flaggeEnglish = new Img("ui/gb.png", 30, 30);
		this.flaggeGerman = new Img("ui/ger.png", 30, 30);
		this.flaggeEnglish.setCursor(Cursor.HAND);
		this.flaggeGerman.setCursor(Cursor.HAND);
		flags.addMembers(this.flaggeEnglish, this.flaggeGerman);
		flags.setLeft(Integer.valueOf(Page.props.prototyp_childTabManager_width()) - 100);

		// Hinzufügen
		this.loginForm.setFields(this.nicknameField, this.passwordField);
		this.buttonPanel.addMember(this.buttonLogIn);
		this.buttonPanel.addMember(this.buttonRegister);
		this.mainArea.addMember(this.loginForm);
		this.mainArea.addMember(this.buttonPanel);
		this.mainArea.addMember(this.buttonSendPassword);
		this.mainArea.addMember(this.progressbar);
		setMainStack(this.mainArea);
		setMainStack(flags);

		sendPasswordPopUp();
	}

	/**
	 * Deaktiviert / Aktiviert alle Buttons auf der Seite
	 * 
	 * @param disable
	 *            Bei true werden alle Buttons deaktiviert.
	 */
	public void disableButtons(boolean disable) {
		if (disable) {
			this.buttonLogIn.setSelected(false);
			this.buttonLogIn.setTitle(Page.props.page_button_wait());
			this.buttonLogIn.setDisabled(true);

			this.buttonRegister.setSelected(false);
			this.buttonRegister.setDisabled(true);

			this.buttonSendPassword.setSelected(false);
			this.buttonSendPassword.setDisabled(false);
		} else {
			this.buttonLogIn.setSelected(true);
			this.buttonLogIn.setTitle(Page.props.frontPage_login_text());
			this.buttonLogIn.setDisabled(false);

			this.buttonRegister.setDisabled(false);

			this.buttonSendPassword.setDisabled(false);
		}
	}

	/**
	 * Liefert den Abort-Button des Passwort-Vergessen-Popups
	 * 
	 * @return buttonAbort
	 */
	public Button getButtonAbort() {
		return this.buttonAbort;
	}

	/**
	 * Liefert den Login-Button
	 * 
	 * @return buttonLogIn
	 */
	public Button getButtonLogIn() {
		return this.buttonLogIn;
	}

	/**
	 * Liefert den Register-Button
	 * 
	 * @return buttonRegister
	 */
	public Button getButtonRegister() {
		return this.buttonRegister;
	}

	/**
	 * Liefert den Send-Button des Passwort-Vergessen-Popups
	 * 
	 * @return buttonSend
	 */
	public Button getButtonSend() {
		return this.buttonSend;
	}

	/**
	 * Liefert den SendPassword-Button
	 * 
	 * @return buttonSendPassword
	 */
	public StandardLabel getButtonSendPassword() {
		return this.buttonSendPassword;
	}

	/**
	 * Liefert die E-Mail Area
	 * 
	 * @return emailArea
	 */
	public DynamicForm getEmailarea() {
		return this.emailarea;
	}

	/**
	 * Liefert das E-Mail-Feld des Passwort-Vergessen-Popups
	 * 
	 * @return eMailField
	 */
	public TextItem getEMailField() {
		return this.eMailField;
	}

	/**
	 * Liefert das Nickname-Feld
	 * 
	 * @return nameField
	 */
	public TextItem getNameField() {
		return this.nicknameField;
	}

	/**
	 * Liefert das Password-Feld
	 * 
	 * @return passwordField
	 */
	public PasswordItem getPasswordField() {
		return this.passwordField;
	}

	/**
	 * Liefert die Progressbar
	 * 
	 * @return progressbar
	 */
	public Progressbar getProgressbar() {
		return this.progressbar;
	}

	/**
	 * Liefert das Fenster beim Passwort-Vergessen
	 * 
	 * @return Window
	 */
	public Window getWinModal() {
		return this.winModal;
	}

	/**
	 * Liefert das Bild mit der deutschen Flagge
	 * 
	 * @return flaggeEnglish
	 */
	public Img getGermanImg() {
		return this.flaggeGerman;

	}

	/**
	 * Liefert das Bild mit der englischen Flagge
	 * 
	 * @return flaggeEnglish
	 */
	public Img getEnglishImg() {
		return this.flaggeEnglish;

	}

	/**
	 * Erstellt ein Fenster für die Passwort-Vergessen Funktion
	 * 
	 * @return true
	 */
	public boolean sendPasswordPopUp() {
		// Window
		this.winModal = new Window();
		this.winModal.setWidth(360);
		this.winModal.setHeight(115);
		this.winModal.setTitle(Page.props.frontPage_buttonSendPassword_text());
		this.winModal.setShowMinimizeButton(false);
		this.winModal.setIsModal(true);
		this.winModal.setShowModalMask(true);
		this.winModal.centerInPage();
		this.winModal.setAlign(Alignment.CENTER);

		// Email Area
		this.emailarea = new DynamicForm();
		this.emailarea.setHeight100();
		this.emailarea.setWidth100();
		this.emailarea.setPadding(5);
		this.emailarea.setLayoutAlign(VerticalAlignment.BOTTOM);
		this.emailarea.setAutoFocus(true);

		// Button Area
		this.buttonPanelSendPassword = new HorizontalStack();
		this.buttonPanelSendPassword.setPadding(Integer.valueOf(Page.props.global_paddingInStackAreas()));
		this.buttonPanelSendPassword.setHeight(35);
		this.buttonPanelSendPassword.setMembersMargin(Integer.valueOf(Page.props.global_paddingInStackAreas()));

		// E-Mail Feld
		this.eMailField = new TextItem();
		this.eMailField.setTitle(Page.props.frontPage_eMailField_title());
		this.eMailField.setRequired(true);
		this.eMailField.setRequiredMessage(Page.props.global_title_requiredMessage());
		this.emailValidator = new RegExpValidator();
		this.emailValidator.setErrorMessage(Page.props.global_title_badEMailMessage());
		this.emailValidator.setExpression("^([a-zA-Z0-9_.\\-+])+@(([a-zA-Z0-9\\-])+\\.)+[a-zA-Z0-9]{2,4}$"); // prop
		this.eMailField.setValidators(this.emailValidator);

		// Button zum Senden des Passwortes
		this.buttonSend = new Button(Page.props.frontPage_passwortLost_buttonSend_title());
		this.buttonSend.setLeft(90);
		this.buttonAbort = new Button(Page.props.global_title_abort());
		this.buttonAbort.setLeft(215);

		// Hinzufügen
		this.emailarea.setFields(this.eMailField);
		this.buttonPanelSendPassword.addChild(this.buttonSend);
		this.buttonPanelSendPassword.addChild(this.buttonAbort);
		this.winModal.addItem(this.emailarea);
		this.winModal.addItem(this.buttonPanelSendPassword);

		return true;
	}

	/**
	 * Validiert alle Felder
	 * 
	 * @return true, wenn alles okay ist.
	 */
	public boolean validate() {
		return this.loginForm.validate();
	}
}
