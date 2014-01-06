package prototyp.client.view.lobby;

import gwtupload.client.Uploader;
import prototyp.client.presenter.lobby.ProfilePagePresenter;
import prototyp.client.util.Button;
import prototyp.client.util.Form;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.PreferencesHelperDataSource;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;

import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.MatchesFieldValidator;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

/**
 * ProfilePage
 * 
 * @author Kamil (Verantwortlicher), Andreas
 * @version 1.0
 * @version 1.1 neben Andy's Änderungen auch noch irgendwas Layoutmäßiges verändert (30.09.10, Robert)
 * 
 * @see ProfilePagePresenter
 */
public class ProfilePage extends Page {
	// Attribute
	private VerticalStack accountDataArea, statisticDataArea, statsArea, lastGamesArea, mainAwardsArea, photoUploadArea;
	private Button buttonEdit, buttonEditPassword, buttonReset;
	private Form editFormRequired, editFormOptional, editFormPassword;
	private TextItem firstnameTextItem, surnameTextItem, emailTextItem, nicknameTextItem;
	private HorizontalStack mainArea, awardsArea1, awardsArea2, awardsArea3, buttonEditArea, buttonEditPasswordArea,
			editPasswordArea;
	private MatchesFieldValidator matchesValidator;
	private RegExpValidator nameValidator, emailValidator, nicknameValidator;
	private PasswordItem password1TextItem, password2TextItem, passwordOldTextItem;
	private ListGrid statisticListGrid;
	private ListGridField statisticPreferences, statisticDescription, statisticType;
	/** Uploader für die Fotos */
	private Uploader photoUploader;

	/**
	 * Konstruktor
	 */
	public ProfilePage() {
		super(Page.props.profilePage_title());

		// Areas
		this.mainArea = new HorizontalStack(Integer.valueOf(Page.props.global_marginBetweenStackAreas()), 0);
		this.accountDataArea = new VerticalStack(Page.props.profilePage_accountDataArea_groupTitle());
		this.accountDataArea.setHeight(488);
		this.accountDataArea.setStyleName("profileArea");
		this.buttonEditArea = new HorizontalStack(Integer.valueOf(Page.props.global_marginBetweenStackAreas()), 0);
		this.buttonEditArea.setHeight(25);
		this.buttonEditArea.setStyleName("buttonAreaMoreMargin2");
		this.buttonEditPasswordArea = new HorizontalStack(Integer.valueOf(Page.props.global_marginBetweenStackAreas()), 0);
		this.buttonEditPasswordArea.setHeight(25);
		this.buttonEditPasswordArea.setStyleName("buttonAreaMoreMargin2");
		this.statisticDataArea = new VerticalStack(Page.props.profilePage_statisticDataArea_groupTitle());
		this.statisticDataArea.setHeight(488);
		this.statisticDataArea.setStyleName("profileArea");
		this.mainAwardsArea = new VerticalStack(Integer.valueOf(Page.props.global_marginInStackAreas()),
				Integer.valueOf(Page.props.global_paddingInStackAreas()));
		this.mainAwardsArea.setTop(500);
		this.mainAwardsArea.setWidth(450);
		this.mainAwardsArea.setHeight(200);
		this.mainAwardsArea.setIsGroup(true);
		this.mainAwardsArea.setGroupTitle(Page.props.global_title_awards());
		this.mainAwardsArea.setStyleName("innerArea");
		this.awardsArea1 = new HorizontalStack();
		this.awardsArea1.setHeight(65);
		this.awardsArea2 = new HorizontalStack();
		this.awardsArea2.setHeight(65);
		this.awardsArea3 = new HorizontalStack();
		this.awardsArea3.setHeight(65);
		this.statsArea = new VerticalStack(Page.props.profilePage_statsArea_groupTitle());
		this.statsArea.setWidth(450);
		this.lastGamesArea = new VerticalStack(Page.props.profilePage_lastGamesArea_groupTitle());
		this.lastGamesArea.setWidth(450);
		this.photoUploadArea = new VerticalStack(Integer.valueOf(Page.props.global_marginInStackAreas()),
				Integer.valueOf(Page.props.global_paddingInStackAreas()));
		this.photoUploadArea.setIsGroup(true);
		this.photoUploadArea.setGroupTitle(Page.props.profilePage_photoUploadArea());
		this.photoUploadArea.setWidth(400);
		this.photoUploadArea.setStyleName("innerAreaSmall");

		// Dynamic Forms
		this.editFormRequired = new Form(Page.props.global_title_requiredFields());
		this.editFormRequired.setWidth(400);
		this.editFormRequired.setAutoFocus(true);
		this.editFormRequired.setStyleName("innerAreaSmall");
		// this.editFormRequired.setPadding(Integer.valueOf(Page.props.global_paddingInStackAreas()));
		this.editFormRequired.setPadding(5);
		this.editFormRequired.setMargin(0);
		this.editFormOptional = new Form(Page.props.global_title_optionalFields());
		this.editFormOptional.setWidth(400);
		this.editFormOptional.setStyleName("innerAreaSmall");
		// this.editFormOptional.setPadding(Integer.valueOf(Page.props.global_paddingInStackAreas()));
		this.editFormOptional.setPadding(5);
		this.editFormOptional.setMargin(0);
		this.editFormPassword = new Form(Page.props.global_title_passwordFields());
		this.editFormPassword.setWidth(400);
		this.editFormPassword.setStyleName("innerAreaSmall");
		// this.editFormPassword.setPadding(Integer.valueOf(Page.props.global_paddingInStackAreas()));
		this.editFormPassword.setPadding(5);
		this.editFormPassword.setMargin(0);
		this.editPasswordArea = new HorizontalStack(0, 0);
		this.editPasswordArea.setAlign(VerticalAlignment.BOTTOM);

		// Nickname
		this.nicknameTextItem = new TextItem(Page.props.global_title_nickname());
		this.nicknameTextItem.setWidth(250);
		this.nicknameTextItem.setLength(30);
		this.nicknameTextItem.setRequired(true);
		this.nicknameTextItem.setRequiredMessage(Page.props.global_title_requiredMessage());
		this.nicknameValidator = new RegExpValidator();
		this.nicknameValidator.setErrorMessage(Page.props.page_textItem_failure_nospecialchars());
		this.nicknameValidator.setExpression("^([a-zA-Z0-9_-])+$");
		this.nicknameTextItem.setValidators(this.nicknameValidator);

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

		// password + password2
		this.passwordOldTextItem = new PasswordItem();
		this.passwordOldTextItem.setTitle(Page.props.profilePage_passwordOld_title());
		this.passwordOldTextItem.setRequired(true);
		this.passwordOldTextItem.setRequiredMessage(Page.props.global_title_password());
		this.password1TextItem = new PasswordItem("Passwort");
		this.password1TextItem.setTitle(Page.props.profilePage_passwordNew_title());
		this.password1TextItem.setRequired(true);
		this.password1TextItem.setRequiredMessage(Page.props.global_title_password());
		this.password2TextItem = new PasswordItem();
		this.password2TextItem.setTitle(Page.props.profilePage_passwordNew2_title());
		this.password2TextItem.setRequired(true);
		this.password2TextItem.setRequiredMessage(Page.props.global_title_password());

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
		this.matchesValidator = new MatchesFieldValidator();
		this.matchesValidator.setOtherField("Passwort");
		this.matchesValidator.setErrorMessage(Page.props.registrationPage_passNotMatch());
		this.password2TextItem.setValidators(this.matchesValidator);

		// Zu den DynForms hinzufügen
		this.editFormRequired.setFields(this.nicknameTextItem, this.emailTextItem);
		this.editFormOptional.setFields(this.firstnameTextItem, this.surnameTextItem);
		this.editFormPassword.setFields(this.passwordOldTextItem, this.password1TextItem, this.password2TextItem);

		// Buttons
		this.buttonEdit = new Button(Page.props.global_title_editProfile(), 150);
		this.buttonReset = new Button(Page.props.global_title_reset(), 150);
		this.buttonEditPassword = new Button(Page.props.profilePage_buttonEditPassword_title(), 90);

		// Fotouploader
		this.photoUploader = new Uploader(true);
		this.photoUploader.setSize("300px", "25px");

		// Statistik
		this.statisticListGrid = new ListGrid();
		this.statisticPreferences = new ListGridField("optionTitle", Page.props.global_title_preference());
		this.statisticDescription = new ListGridField("optionDescription", Page.props.global_title_description());
		this.statisticType = new ListGridField("optionType", Page.props.global_title_type());
		this.statisticListGrid.setWidth(450);
		this.statisticListGrid.setHeight(200);
		this.statisticListGrid.setShowAllRecords(true);
		this.statisticListGrid.setSelectionType(SelectionStyle.SINGLE);
		this.statisticListGrid.setWrapCells(true);
		this.statisticListGrid.setFixedRecordHeights(false);
		this.statisticListGrid.setFields(this.statisticPreferences, this.statisticDescription, this.statisticType);
		this.statisticListGrid.setGroupStartOpen(GroupStartOpen.ALL);
		this.statisticListGrid.setGroupByField("optionType");
		this.statisticListGrid.hideField("optionType");
		this.statisticListGrid.setDataSource(new PreferencesHelperDataSource());
		this.statisticListGrid.setShowDetailFields(false);

		// Hinzufügen der Komponenten
		this.buttonEditArea.addMembers(this.buttonEdit, this.buttonReset);
		this.buttonEditPasswordArea.addMembers(this.buttonEditPassword);
		this.editPasswordArea.addMember(this.editFormPassword);
		// this.buttonEditPassword.setTop(105); // Warum dies? Sieht doch so
		// blöd aus.
		// this.buttonEditPassword.setLeft(310);
		this.photoUploadArea.addMember(this.photoUploader);
		this.accountDataArea.addMembers(this.editFormRequired, this.editFormOptional, this.buttonEditArea, this.editPasswordArea,
				this.buttonEditPasswordArea, this.photoUploadArea);
		this.mainAwardsArea.addMembers(this.awardsArea1, this.awardsArea2, this.awardsArea3);
		this.statisticDataArea.addMembers(this.statisticListGrid, this.mainAwardsArea);
		this.mainArea.addMembers(this.accountDataArea, this.statisticDataArea);
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
		this.editFormPassword.clearErrors(true);

		return true;
	}

	/**
	 * Liefert die gewünschte Awards-Area
	 * 
	 * @return Awards-Area 1,2 oder 3
	 */
	public HorizontalStack getAwardsArea(int i) {
		if (i == 0) {
			return this.awardsArea1;
		} else if (i == 1) {
			return this.awardsArea2;
		} else {
			return this.awardsArea3;
		}
	}

	/**
	 * Liefert den Buttom zum Editieren des Accounts
	 * 
	 * @return buttonEdit Button
	 */
	public Button getButtonEdit() {
		return this.buttonEdit;
	}

	/**
	 * Liefert den Buttom zum Editieren des Passwortes
	 * 
	 * @return buttonEditPassword Button
	 */
	public Button getButtonEditPassword() {
		return this.buttonEditPassword;
	}

	/**
	 * Liefert den Buttom zum Zurücksetzen der Profildaten
	 * 
	 * @return buttonReset Button
	 */
	public Button getButtonReset() {
		return this.buttonReset;
	}

	/**
	 * Liefert das TextItemfeld der Emailadresse
	 * 
	 * @return emailTextItem TextItem
	 */
	public TextItem getEmailTextItem() {
		return this.emailTextItem;
	}

	/**
	 * Liefert das TextItemfeld des Vornamens
	 * 
	 * @return firstnameTextItem TextItem
	 */
	public TextItem getFirstnameTextItem() {
		return this.firstnameTextItem;
	}

	/**
	 * Liefert das TextItemfeld des neuen Passwortes zum Bestätigen
	 * 
	 * @return password1TextItem TextItem
	 */
	public PasswordItem getNewPassword2TextItem() {
		return this.password2TextItem;
	}

	/**
	 * Liefert das TextItemfeld des neuen Passwortes
	 * 
	 * @return password1TextItem TextItem
	 */
	public PasswordItem getNewPasswordTextItem() {
		return this.password1TextItem;
	}

	/**
	 * Liefert das TextItemfeld des Nicknamens
	 * 
	 * @return nicknameTextItem TextItem
	 */
	public TextItem getNicknameTextItem() {
		return this.nicknameTextItem;
	}

	/**
	 * Liefert das TextItemfeld des alten Passwortes
	 * 
	 * @return passwordOldTextItem TextItem
	 */
	public PasswordItem getOldPasswordTextItem() {
		return this.passwordOldTextItem;
	}

	/**
	 * Liefert den PhotoUploader
	 * 
	 * @return photouploader
	 */
	public Uploader getPhotoUploader() {
		return this.photoUploader;
	}

	/**
	 * Liefert das Statistik-ListGrid
	 * 
	 * @return statisticListGrid ListGrid mit den Statistiken und zuletzt gespielten Spielbrettern
	 */
	public ListGrid getStatisticListGrid() {
		return this.statisticListGrid;
	}

	/**
	 * Liefert das TextItemfeld des Nachnamens
	 * 
	 * @return surnameTextItem TextItem
	 */
	public TextItem getSurnameTextItem() {
		return this.surnameTextItem;
	}

	/**
	 * Validiert die TextItemfelder zum Editieren des Accounts
	 * 
	 * @return true, wenn alles korrekt eingegeben wurde
	 */
	public boolean validateEditFields() {
		boolean required = this.editFormRequired.validate();
		boolean optional = this.editFormOptional.validate();

		return required && optional;
	}

	/**
	 * Validiert die TextItemfelder zum Editieren des Passwords
	 * 
	 * @return true, wenn alles korrekt eingegeben wurde
	 */
	public boolean validatePasswordFields() {
		return this.editFormPassword.validate();
	}

	/**
	 * Deaktiviert alle Buttons
	 */
	public void disableAllButtons() {
		this.buttonEdit.disable();
		this.buttonEditPassword.disable();
		this.buttonReset.disable();
	}
}
