package prototyp.client.view.administration;

import prototyp.client.presenter.administration.AdministrationPagePresenter;
import prototyp.client.util.Button;
import prototyp.client.util.Form;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.StandardLabel;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

/**
 * Dies ist der View zur AdministrationPage
 * 
 * @author Andreas, Mischa (Verantwortlicher)
 * @version 1.0
 * 
 * @see {@link AdministrationPagePresenter}
 */
public class AdministrationPage extends Page {
	// Attribute
	private Button buttonLockAccount, buttonEditAccount, buttonReset;
	private StandardLabel information;
	private HorizontalStack mainArea, buttonArea;
	private RegExpValidator nameValidator, emailValidator;
	private Form searchUserForm, editFormRequired, editFormOptional;
	private TextItem searchUserTextItem, firstnameTextItem, surnameTextItem, emailTextItem, nicknameTextItem;
	private ListGridField userIDGridField, userEmailGridField, userNicknameGridField, userLockedGridField;
	private ListGrid userList;
	private VerticalStack userListArea, rightArea;

	/**
	 * Konstruktor
	 */
	public AdministrationPage() {
		super(Page.props.administrationPage_title());

		/* Initialisierung */
		// Areas
		this.mainArea = new HorizontalStack(Integer.valueOf(Page.props.global_marginBetweenStackAreas()), 0);
		this.userListArea = new VerticalStack(Page.props.administrationPage_userListArea_groupTitle());
		this.userListArea.setHeight(488);
		this.userListArea.setStyleName("adminArea");
		this.rightArea = new VerticalStack(Page.props.administrationPage_rightArea_groupTitle());
		this.rightArea.setHeight(488);
		this.rightArea.setStyleName("adminArea");
		this.buttonArea = new HorizontalStack(Integer.valueOf(Page.props.global_marginInStackAreas()), 0);
		this.buttonArea.setMembersMargin(Integer.valueOf(Page.props.global_paddingInStackAreas()));
		this.buttonArea.setAutoHeight();
		this.buttonArea.setStyleName("buttonArea");
		this.editFormRequired = new Form(Page.props.global_title_requiredFields());
		this.editFormRequired.setPadding(Integer.valueOf(Page.props.global_paddingInStackAreas()));
		this.editFormRequired.setWidth(400);
		this.editFormRequired.setStyleName("innerArea");
		this.editFormOptional = new Form(Page.props.global_title_optionalFields());
		this.editFormOptional.setPadding(Integer.valueOf(Page.props.global_paddingInStackAreas()));
		this.editFormOptional.setWidth(400);
		this.editFormOptional.setStyleName("innerArea");

		// UserList
		this.searchUserForm = new Form();
		this.searchUserForm.setAutoFocus(true);
		this.searchUserTextItem = new TextItem(Page.props.administrationPage_searchUserTextItem_title());
		this.searchUserTextItem.setWidth(396);
		this.searchUserTextItem.setTooltip(Page.props.administrationPage_searchUserTextItem_tooltip());
		this.searchUserForm.setFields(this.searchUserTextItem);
		this.userList = new ListGrid();
		this.userList.setSelectionType(SelectionStyle.SINGLE);
		this.userIDGridField = new ListGridField("userid", Page.props.administrationPage_userIDGridField_title());
		this.userIDGridField.setWidth(30);
		this.userNicknameGridField = new ListGridField("nickname", Page.props.global_title_nickname());
		this.userNicknameGridField.setWidth(140);
		this.userEmailGridField = new ListGridField("email", Page.props.global_title_email());
		this.userLockedGridField = new ListGridField("locked", Page.props.administrationPage_lockedGridField_title());
		this.userLockedGridField.setType(ListGridFieldType.IMAGE);
		this.userLockedGridField.setAlign(Alignment.CENTER);
		this.userLockedGridField.setWidth(20);
		this.userList.setFields(this.userIDGridField, this.userNicknameGridField, this.userEmailGridField,
				this.userLockedGridField);
		this.userList.setWidth(450);
		this.userList.setHeight(380);
		this.userList.setShowAllRecords(true);
		this.userList.setCanEdit(false);
		this.userList.setCanResizeFields(true);
		this.userList.setWrapCells(true);
		this.userList.setFixedRecordHeights(false);
		this.userList.setSortField(1);

		// Nickname
		this.nicknameTextItem = new TextItem(Page.props.global_title_nickname());
		this.nicknameTextItem.setWidth(250);
		this.nicknameTextItem.setLength(30);
		this.nicknameTextItem.setRequired(true);
		this.nicknameTextItem.setRequiredMessage(Page.props.global_title_requiredMessage());

		// Email
		this.emailTextItem = new TextItem(Page.props.global_title_email());
		this.emailTextItem.setWidth(250);
		this.emailTextItem.setLength(40);
		this.emailTextItem.setRequired(true);
		this.emailTextItem.setRequiredMessage(Page.props.global_title_requiredMessage());

		// Vorname
		this.firstnameTextItem = new TextItem(Page.props.global_title_firstname());
		this.firstnameTextItem.setWidth(250);
		this.firstnameTextItem.setLength(30);

		// Nachname
		this.surnameTextItem = new TextItem(Page.props.global_title_surname());
		this.surnameTextItem.setWidth(250);
		this.surnameTextItem.setLength(30);

		// Validieren
		this.nameValidator = new RegExpValidator();
		this.nameValidator.setErrorMessage(Page.props.page_textItem_failure_nonumbers());
		this.nameValidator.setExpression("^\\D*$");
		this.firstnameTextItem.setValidators(this.nameValidator);
		this.surnameTextItem.setValidators(this.nameValidator);
		this.emailValidator = new RegExpValidator();
		this.emailValidator.setErrorMessage(Page.props.global_title_badEMailMessage());
		this.emailValidator.setExpression("^([a-zA-Z0-9_.\\-+])+@(([a-zA-Z0-9\\-])+\\.)+[a-zA-Z0-9]{2,4}$");
		this.emailTextItem.setValidators(this.emailValidator);

		// Zu den DynForms hinzufügen
		this.editFormRequired.setFields(this.nicknameTextItem, this.emailTextItem);
		this.editFormOptional.setFields(this.firstnameTextItem, this.surnameTextItem);

		// buttons
		this.buttonEditAccount = new Button(Page.props.global_title_editProfile(), 120);
		this.buttonReset = new Button(Page.props.global_title_reset(), 120);
		this.buttonLockAccount = new Button(Page.props.administrationPage_buttonBanAccount_title_lock(), 120);

		// Hinweistext
		this.information = new StandardLabel(Page.props.administrationPage_information_text());
		this.information.setWidth(400);

		// Hinzufügen der Komponenten in die Areas
		this.rightArea.addMembers(this.editFormRequired, this.editFormOptional);
		this.buttonArea.addMembers(this.buttonEditAccount, this.buttonReset, this.buttonLockAccount);
		this.userListArea.addMembers(this.searchUserForm, this.userList);
		this.rightArea.addMembers(this.buttonArea, this.information);
		this.mainArea.addMembers(this.userListArea, this.rightArea);
		setMainStack(this.mainArea);
	}

	/**
	 * Löscht alle Error in den DynamicForms
	 * 
	 * @return true, falls alles geklappt hat.
	 */
	public boolean clearAllErrors() {
		this.editFormRequired.clearErrors(true);
		this.editFormOptional.clearErrors(true);

		return true;
	}

	/**
	 * Löscht alle Error sowie den Inhalt in den DynamicForms
	 * 
	 * @return true, falls alles geklappt hat.
	 */
	public boolean clearAllFields() {
		clearAllErrors();
		this.firstnameTextItem.clearValue();
		this.surnameTextItem.clearValue();
		this.nicknameTextItem.clearValue();
		this.emailTextItem.clearValue();

		// Selektion aufheben
		if (this.userList.anySelected()) {
			this.userList.deselectAllRecords();
		}

		return true;
	}

	/**
	 * Deaktiviert oder aktiviert alle Buttons auf der Seite
	 * 
	 * @param disable
	 *            Angabe, ob die Buttons deaktiviert werden sollen
	 */
	public void disableAllButtons(boolean disable) {
		if (disable) {
			this.buttonEditAccount.disable();
			this.buttonLockAccount.disable();
			this.buttonReset.disable();
		} else {
			this.buttonEditAccount.enable();
			this.buttonLockAccount.enable();
			this.buttonReset.enable();
		}
	}

	/**
	 * Liefert den Button zum Ändern der Profildaten
	 * 
	 * @return buttonEdit
	 */
	public Button getButtonEditAccount() {
		return this.buttonEditAccount;
	}

	/**
	 * Liefert den Button zum Sperren eines Nutzers
	 * 
	 * @return buttonLockAccount
	 */
	public Button getButtonLockAccount() {
		return this.buttonLockAccount;
	}

	/**
	 * Liefert den Reset-Button zum Zurücksetzen der Profildaten
	 * 
	 * @return buttonReset
	 */
	public Button getButtonReset() {
		return this.buttonReset;
	}

	/**
	 * Liefert das TextItemfeld der Emailadresse
	 * 
	 * @return emailTextItem
	 */
	public TextItem getEmailTextItem() {
		return this.emailTextItem;
	}

	/**
	 * Liefert das TextItemfeld des Vornamens
	 * 
	 * @return firstnameTextItem
	 */
	public TextItem getFirstnameTextItem() {
		return this.firstnameTextItem;
	}

	/**
	 * Liefert das TextItemfeld des Nicknamens
	 * 
	 * @return nicknameTextItem
	 */
	public TextItem getNicknameTextItem() {
		return this.nicknameTextItem;
	}

	/**
	 * Liefert das Suchfeld
	 * 
	 * @return searchUserTextItem
	 */
	public TextItem getSearchUserTextItem() {
		return this.searchUserTextItem;
	}

	/**
	 * Liefert das TextItemfeld des Nachnamens
	 * 
	 * @return surnameTextItem
	 */
	public TextItem getSurnameTextItem() {
		return this.surnameTextItem;
	}

	/**
	 * Liefert die Tabelle mit den Accountdaten
	 * 
	 * @return userList
	 */
	public ListGrid getUserList() {
		return this.userList;
	}

	/**
	 * Validiert die TextItemfelder
	 * 
	 * @return true, wenn alles korrekt eingegeben wurde
	 */
	public boolean validate() {
		boolean required = this.editFormRequired.validate();
		boolean optional = this.editFormOptional.validate();

		return required && optional;
	}
}