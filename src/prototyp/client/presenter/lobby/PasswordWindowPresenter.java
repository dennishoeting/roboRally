package prototyp.client.presenter.lobby;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.user.UserPresenter;
import prototyp.client.service.LobbyService;
import prototyp.client.service.LobbyServiceAsync;
import prototyp.client.view.Page;
import prototyp.client.view.lobby.PasswordWindow;
import prototyp.client.view.lobby.ProfilePage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;

public class PasswordWindowPresenter implements PagePresenter {

	/** Zugehörige Page */
	private final PasswordWindow page;

	/** ProfilePagePresenter */
	private final ProfilePagePresenter parentPresenter;

	/** Async-Objekt */
	private final LobbyServiceAsync lobbyService;

	/** AccountDaten des Users */
	private final String nickname, email, firstname, surname;

	/** UserID */
	private final int userID;

	/**
	 * Konstruktor
	 * 
	 * @param userID
	 *            UserID
	 * @param surname
	 *            Nachname
	 * @param firstname
	 *            Vorname
	 * @param email
	 *            Email
	 * @param nickname
	 *            Nickname
	 * @param parent
	 *            ProfilePagePresenter (um Buttons wieder zu aktivieren, wenn man abbricht)
	 */
	public PasswordWindowPresenter(ProfilePagePresenter parent, String nickname, String email, String firstname, String surname,
			int userID) {
		this.lobbyService = GWT.create(LobbyService.class);
		this.nickname = nickname;
		this.email = email;
		this.firstname = firstname;
		this.surname = surname;
		this.userID = userID;
		this.parentPresenter = parent;

		// Window zeigen
		this.page = new PasswordWindow();

		// Listener hinzufügen
		this.addListener();
	}

	/**
	 * Fügt die Listener hinzu
	 */
	private void addListener() {
		// Wird ausgeführt, wenn "Enter" im Passwortfeld gedrückt wurde
		this.page.getPasswordField().addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getKeyName() != null && event.getKeyName().equals("Enter") && page.validate()) {
					changeUserData((String) page.getPasswordField().getValue());
				}
			}
		});

		// Wird ausgeführt, wenn Weiter gedrückt wird
		this.page.getButtonOk().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (page.validate()) {
					changeUserData((String) page.getPasswordField().getValue());
				}
			}
		});

		// Wird ausgeführt, wenn Abbrechen gedrückt wird
		this.page.getButtonAbort().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// Buttons wieder aktivieren
				((ProfilePage) parentPresenter.getPage()).getButtonEdit().enable();
				((ProfilePage) parentPresenter.getPage()).getButtonReset().enable();

				// Window verbergen
				page.destroy();
			}
		});
	}

	/**
	 * Ändert die Accountdaten des Users
	 * 
	 * @param password
	 *            Passwort
	 * 
	 */
	private void changeUserData(String password) {
		this.lobbyService.editUserWithNewNickname(UserPresenter.getInstance().getNickname(), nickname, password, email,
				firstname, surname, userID, new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						// Buttons wieder aktivieren
						((ProfilePage) parentPresenter.getPage()).getButtonEdit().enable();
						((ProfilePage) parentPresenter.getPage()).getButtonReset().enable();

						// Ausgeben
						SC.say(Page.props.administrationPage_editUser_title(), caught.getMessage());
					}

					@Override
					public void onSuccess(Boolean result) {
						// Meldung ausgeben
						SC.say(Page.props.administrationPage_editUser_title(),
								Page.props.administrationPage_editUser_text_success_logout());
						// User rauskicken
						Window.Location.reload();
					}
				});

		// Window verbergen
		page.destroy();
	}

	/**
	 * Liefert die Page
	 * 
	 * @return Page
	 */
	@Override
	public Canvas getPage() {
		return this.page;
	}

}
