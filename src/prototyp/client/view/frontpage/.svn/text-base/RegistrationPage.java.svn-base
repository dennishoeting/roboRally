package prototyp.client.view.frontpage;

import prototyp.client.presenter.frontpage.RegistrationPagePresenter;
import prototyp.client.util.Button;
import prototyp.client.util.Form;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.MatchesFieldValidator;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;

/**
 * RegistrationPage
 * 
 * @author Jens (Verantwortlicher), Dennis, Mischa
 * @version 1.3
 * @version 1.2 QS und Methode gelöscht, da überflüssig 25.10.10 (Mischa)
 * 
 * @see RegistrationPagePresenter
 */
public class RegistrationPage extends Page {
	// Atrribute
	private CheckboxItem accAGBCheckboxItem;
	/** HTML Pane mit den AGB-Inhalten */
	private HTMLPane agbPane;
	private Button buttonAbort, buttonSubmit;
	private VerticalStack leftArea, agbArea;
	private HorizontalStack mainPanel, buttonPanel;
	private MatchesFieldValidator matchesValidator;
	private RegExpValidator nameValidator, emailValidator, nicknameValidator;
	private TextItem nicknameTextItem, emailTextItem, firstnameTextItem,
			surnameTextItem;
	private PasswordItem password1TextItem, password2TextItem;
	private Form pflichtfelderForm, agbForm, optionaleFelderForm;

	/**
	 * Konstruktor
	 */
	public RegistrationPage() {
		super(Page.props.registrationPage_title());

		// Panels + Forms
		this.mainPanel = new HorizontalStack(Integer.valueOf(Page.props
				.global_marginBetweenStackAreas()), 0);
		this.leftArea = new VerticalStack(Integer.valueOf(Page.props
				.global_marginBetweenStackAreas()), Integer.valueOf(Page.props
				.global_paddingInStackAreas()));
		this.pflichtfelderForm = new Form(
				Page.props.global_title_requiredFields());
		this.pflichtfelderForm.setWidth(300);
		this.pflichtfelderForm.setStyleName("registerArea");
		this.optionaleFelderForm = new Form(
				Page.props.global_title_optionalFields());
		this.optionaleFelderForm.setWidth(300);
		this.optionaleFelderForm.setStyleName("registerArea");
		this.agbForm = new Form();
		this.agbForm.setTop(115);
		this.agbForm.setLeft(30);
		this.buttonPanel = new HorizontalStack(Integer.valueOf(Page.props
				.global_marginBetweenStackAreas()), Integer.valueOf(Page.props
				.global_paddingInStackAreas()));
		this.buttonPanel.setStyleName("buttonArea");
		this.agbArea = new VerticalStack(
				Page.props.registrationPage_agb_title());
		this.agbArea.setOverflow(Overflow.AUTO);
		this.agbArea.setWidth(600);
		this.agbArea.setHeight(450);
		this.agbArea.setStyleName("registerArea");
		this.agbArea.scrollToTop();

		// Nickname-Feld
		this.nicknameTextItem = new TextItem(Page.props.global_title_nickname());
		this.nicknameTextItem.setRequired(true);
		this.nicknameTextItem.setRequiredMessage(Page.props
				.global_title_requiredMessage());
		this.nicknameValidator = new RegExpValidator();
		this.nicknameValidator.setErrorMessage(Page.props
				.page_textItem_failure_nospecialchars());
		this.nicknameValidator.setExpression("^([a-zA-Z0-9_-])+$");
		this.nicknameTextItem.setValidators(this.nicknameValidator);

		// Email-Feld
		this.emailTextItem = new TextItem(Page.props.global_title_email());
		this.emailTextItem.setRequired(true);
		this.emailTextItem.setRequiredMessage(Page.props
				.global_title_requiredMessage());
		this.emailValidator = new RegExpValidator();
		this.emailValidator.setErrorMessage(Page.props
				.global_title_badEMailMessage());
		this.emailValidator
				.setExpression("^([a-zA-Z0-9_.\\-+])+@(([a-zA-Z0-9\\-])+\\.)+[a-zA-Z0-9]{2,4}$");
		this.emailTextItem.setValidators(this.emailValidator);

		// Password + Password2 -Feld
		this.password1TextItem = new PasswordItem(
				Page.props.global_title_password());
		this.password1TextItem.setRequired(true);
		this.password1TextItem.setRequiredMessage(Page.props
				.global_title_requiredMessage());
		this.password2TextItem = new PasswordItem();
		this.password2TextItem.setTitle(Page.props
				.registrationPage_password2_title());
		this.password2TextItem.setRequired(true);
		this.password2TextItem.setRequiredMessage(Page.props
				.global_title_requiredMessage());
		this.matchesValidator = new MatchesFieldValidator();
		this.matchesValidator.setOtherField(Page.props.global_title_password());
		this.matchesValidator.setErrorMessage(Page.props
				.registrationPage_passNotMatch());
		this.password2TextItem.setValidators(this.matchesValidator);

		// AGB-Checkbox
		this.accAGBCheckboxItem = new CheckboxItem();
		this.accAGBCheckboxItem.setTitle(Page.props
				.registrationPage_agbCheckBox_title());
		this.accAGBCheckboxItem.setRequired(true);
		this.accAGBCheckboxItem.setRequiredMessage(Page.props
				.global_title_requiredMessage());

		// Vorname-Feld
		this.firstnameTextItem = new TextItem(
				Page.props.global_title_firstname());

		// Nachname-Feld
		this.surnameTextItem = new TextItem(Page.props.global_title_surname());
		this.nameValidator = new RegExpValidator();
		this.nameValidator.setErrorMessage(Page.props
				.page_textItem_failure_nonumbers());
		this.nameValidator.setExpression("^\\D*$");
		this.firstnameTextItem.setValidators(this.nameValidator);
		this.surnameTextItem.setValidators(this.nameValidator);

		// Buttons
		this.buttonSubmit = new Button(Page.props.global_title_submit(), 100);
		this.buttonAbort = new Button(Page.props.global_title_abort(), 100);

		// AGBs
		this.agbPane = new HTMLPane();
		this.agbPane.setHeight(405);
		this.agbPane.setWidth(570);

		// Hinzufügen
		this.pflichtfelderForm.setFields(this.nicknameTextItem,
				this.emailTextItem, this.password1TextItem,
				this.password2TextItem);
		this.pflichtfelderForm.setAutoFocus(true);
		this.agbForm.setFields(this.accAGBCheckboxItem);
		this.pflichtfelderForm.addChild(this.agbForm);
		this.optionaleFelderForm.setFields(this.firstnameTextItem,
				this.surnameTextItem);
		this.leftArea.addMembers(this.pflichtfelderForm,
				this.optionaleFelderForm, this.buttonPanel);
		this.buttonPanel.addMembers(this.buttonSubmit, this.buttonAbort);
		this.agbArea.addMember(this.agbPane);
		this.mainPanel.addMembers(this.leftArea, this.agbArea);
		setMainStack(this.mainPanel);
	}

	/**
	 * Liefert den Abbruch-Button
	 * 
	 * @return Abbruch-Button
	 */
	public Button getAbortButton() {
		return this.buttonAbort;
	}

	/**
	 * Liefert das CheckboxItem der AGB-Checkbox
	 * 
	 * @return AGBCheckboxItem
	 */

	public CheckboxItem getAccAGBCheckBoxItem() {
		return this.accAGBCheckboxItem;
	}

	/**
	 * Liefert die AGB-Pane. Also das wo die ABGs drauf stehen.
	 * 
	 * @return
	 */
	public HTMLPane getAgbPane() {
		return this.agbPane;
	}

	/**
	 * Liefert den Submit-Button
	 * 
	 * @return Submit-Button
	 */
	public Button getButtonSubmit() {
		return this.buttonSubmit;
	}

	/**
	 * Liefert den Text im Feld Email, also die Email-Adresse
	 * 
	 * @return Email-Adresse
	 */
	public TextItem getEmailTextItem() {
		return this.emailTextItem;
	}

	/**
	 * Liefert den eingegeben Vornamen
	 * 
	 * @return firstnameTextitem
	 */
	public TextItem getFirstnameTextItem() {
		return this.firstnameTextItem;
	}

	/**
	 * Liefert die Form der Pflichtfelder
	 * 
	 * @return Form der Pflichtfelder (DynamicForm)
	 */
	public DynamicForm getForm() {
		return this.pflichtfelderForm;
	}

	/**
	 * Liefert den eingegeben Nicknamen
	 * 
	 * @return nicknameTextItem
	 */
	public TextItem getNicknameTextItem() {
		return this.nicknameTextItem;
	}

	/**
	 * Liefert das eingebene Password
	 * 
	 * @return PasswordItem
	 */
	public PasswordItem getPassword1TextItem() {
		return this.password1TextItem;
	}

	/**
	 * Liefert den Nachnamen
	 * 
	 * @return surnameTextItem
	 */
	public TextItem getSurnameTextItem() {
		return this.surnameTextItem;
	}

	/**
	 * Prüft die Pflichtfelder
	 * 
	 * @return true, wenn alles okay ist
	 */
	public boolean validate() {
		boolean agb = this.agbForm.validate();
		boolean pflicht = this.pflichtfelderForm.validate();
		boolean optional = this.optionaleFelderForm.validate();

		return agb && pflicht && optional;
	}
}
