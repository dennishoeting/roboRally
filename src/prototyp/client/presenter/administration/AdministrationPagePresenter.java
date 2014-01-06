package prototyp.client.presenter.administration;

import java.util.HashMap;
import java.util.Map;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.user.UserPresenter;
import prototyp.client.service.AdministrationService;
import prototyp.client.service.AdministrationServiceAsync;
import prototyp.client.util.UserListRecord;
import prototyp.client.view.Page;
import prototyp.client.view.administration.AdministrationPage;
import prototyp.shared.useradministration.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;

/**
 * Presenter der AdministrationPage. Der Inhalt der Tabelle mit den Usern wird über den TabManager aktualisiert. Erst, wenn der
 * richtige Tab ausgewählt wird, werden die Daten aus der Datenbank gelesen.
 * 
 * @author Andreas, Mischa (Verantwortlicher)
 * @version 1.1
 * @version 1.2 Exception Handling, onFailure
 * @version 1.3 Abfrage, ob der Nutzer gesperrt werden soll, hinzugefügt und Code komplett überarbeitet
 * 
 * @see {@link AdministrationPage}
 */
public class AdministrationPagePresenter implements PagePresenter {
	/** Alle User aus der Datenbank */
	private Map<Integer, User> allUsers = null;

	/** Zugehörige AdministrationPage */
	private AdministrationPage page;

	/** Async-Objekt */
	private final AdministrationServiceAsync pageService;

	/** Selektierter Nutzer */
	private User selectedUser = null;

	/** Angabe, ob sich die Felder geändert haben */
	private boolean changedValues = false;

	/** UserList für die Tabelle */
	private RecordList userData;

	/**
	 * Konstruktor. Erstellt die AdminPage und fügt die Listener hinzu.
	 */
	public AdministrationPagePresenter() {
		this.pageService = GWT.create(AdministrationService.class);

		// Fügt die Seite hinzu
		this.page = new AdministrationPage();

		// Fügt die Listener hinzu
		addListeners();
	}

	/**
	 * Fügt die Listener hinzu
	 * 
	 * @return true
	 */
	private boolean addListeners() {
		// Wird ausgeführt, wenn der Button "Nutzer sperren" betätigt wird
		this.page.getButtonLockAccount().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final User selectedUser = AdministrationPagePresenter.this.selectedUser;
				if (selectedUser != null) {
					// Fragen, ob der Nutzer gesperrt werden soll
					String question = selectedUser.getAccountData().isLocked() ? Page.props
							.administrationPage_unlockUser_question() : Page.props.administrationPage_lockUser_question();
					SC.ask(question, new BooleanCallback() {
						@Override
						public void execute(Boolean value) {
							if (value != null && value) {
								// Buttons deaktivieren
								AdministrationPagePresenter.this.page.disableAllButtons(true);

								// Nutzer sperren/entsperren
								lockUser(selectedUser);
							}
						}
					});
				}
			}
		});

		// Wird ausgeführt, wenn der Button "Zurücksetzen" betätigt wird
		this.page.getButtonReset().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					// Buttons deaktivieren
					AdministrationPagePresenter.this.page.disableAllButtons(true);

					// User anzeigen
					fillField(AdministrationPagePresenter.this.selectedUser);

					// Error bei den Pflichtfeldern löschen
					AdministrationPagePresenter.this.page.clearAllErrors();

					// Button "Nutzer sperren" aktivieren
					AdministrationPagePresenter.this.page.getButtonLockAccount().enable();
				} catch (IllegalArgumentException e) {
					// Alle Felder löschen
					resetFields();
				}
			}
		});

		// Wird ausgeführt, wenn der Button "Profildaten ändern" betätigt wird
		this.page.getButtonEditAccount().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (AdministrationPagePresenter.this.selectedUser != null && AdministrationPagePresenter.this.page.validate()) {
					// Optionale Felder behandeln
					String firstname = AdministrationPagePresenter.this.page.getFirstnameTextItem().getValue() != null ? AdministrationPagePresenter.this.page
							.getFirstnameTextItem().getValue().toString()
							: "";
					String surname = AdministrationPagePresenter.this.page.getSurnameTextItem().getValue() != null ? AdministrationPagePresenter.this.page
							.getSurnameTextItem().getValue().toString()
							: "";

					// Buttons deaktivieren
					AdministrationPagePresenter.this.page.disableAllButtons(true);

					// Nun versuchen in der Datenbank zu speichern.
					saveUserToDatabase(AdministrationPagePresenter.this.page.getNicknameTextItem().getValue().toString(),
							AdministrationPagePresenter.this.page.getEmailTextItem().getValue().toString(), firstname, surname,
							AdministrationPagePresenter.this.selectedUser.getId(), UserPresenter.getInstance().getNickname());
				}
			}
		});

		// Wird ausgeführt, wenn in der Tabelle ein Nutzer angeklickt wird
		this.page.getUserList().addSelectionChangedHandler(new SelectionChangedHandler() {
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				UserListRecord data = (UserListRecord) event.getSelectedRecord();
				try {
					// Nutzer anzeigen
					showUser(data.getUser());

					// Eventuelle Fehler löschen
					AdministrationPagePresenter.this.page.clearAllErrors();
				} catch (IllegalArgumentException e) {
					resetFields();
				} catch (NullPointerException e) {
					// Wird verschluckt.
				}
			}
		});

		// Wird ausgeführt, wenn etwas in die Suche eingegeben wird.
		this.page.getSearchUserTextItem().addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				if (AdministrationPagePresenter.this.allUsers != null) {
					Object searchValue = AdministrationPagePresenter.this.page.getSearchUserTextItem().getValue();
					try {
						if (searchValue == null) {
							showUserInList(AdministrationPagePresenter.this.allUsers);
						} else {
							// Temporäre Liste
							Map<Integer, User> searchUsers = new HashMap<Integer, User>();
							// Welcher Nickname fängt mit dem Suchmuster an?
							for (Integer key : AdministrationPagePresenter.this.allUsers.keySet()) {
								User user = AdministrationPagePresenter.this.allUsers.get(key);
								if (user.getAccountData().getNickname().toLowerCase()
										.startsWith(searchValue.toString().toLowerCase())) {
									searchUsers.put(user.getId(), user);
								}
							}
							// Anzeigen
							showUserInList(searchUsers);
						}
					} catch (IllegalArgumentException e) {
						resetFields();
					}
				}
			}
		});

		// Wird bei allen Feldern benutzt, um Änderungen zu registrieren
		ChangedHandler fieldChangeHandler = new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				if (!changedValues) {
					changedValues = true;

					// Buttons aktivieren
					page.getButtonEditAccount().enable();
					page.getButtonReset().enable();
				}
			}
		};

		// Felder bekommen ChangedHandler, um Änderungen zu bemerken
		this.page.getNicknameTextItem().addChangedHandler(fieldChangeHandler);
		this.page.getEmailTextItem().addChangedHandler(fieldChangeHandler);
		this.page.getSurnameTextItem().addChangedHandler(fieldChangeHandler);
		this.page.getFirstnameTextItem().addChangedHandler(fieldChangeHandler);

		return true;
	}

	/**
	 * Setzt die Felder zum Editieren mit den Nutzerdaten
	 * 
	 * @param user
	 *            Ausgewählter Nutzer
	 * @return true, falls alles geklappt hat.
	 * @throws IllegalArgumentException
	 *             Wird geworfen, wenn der User null ist
	 */
	private boolean fillField(User user) throws IllegalArgumentException {
		if (user == null) {
			throw new IllegalArgumentException();
		}

		// In die Page hinzufügen
		this.page.getNicknameTextItem().setValue(user.getAccountData().getNickname());
		this.page.getFirstnameTextItem().setValue(user.getAccountData().getFirstname());
		this.page.getSurnameTextItem().setValue(user.getAccountData().getSurname());
		this.page.getEmailTextItem().setValue(user.getAccountData().getEmail());
		if (user.getAccountData().isLocked()) {
			this.page.getButtonLockAccount().setTitle(Page.props.administrationPage_buttonBanAccount_title_unlock());
		} else {
			this.page.getButtonLockAccount().setTitle(Page.props.administrationPage_buttonBanAccount_title_lock());
		}

		return true;
	}

	/**
	 * Liefert die Page
	 */
	@Override
	public Canvas getPage() {
		return this.page;
	}

	/**
	 * Lädt die Nutzer aus der Datenbank und zeigt sie in der Liste an. Dieses wird immer ausgeführt, wenn der Tab angezeigt wird.
	 * 
	 * @param true
	 */
	public boolean loadUsersFromDatabase() {
		this.pageService.getUsers(new AsyncCallback<Map<Integer, User>>() {
			@Override
			public void onFailure(Throwable caught) {
				// Leere Liste einfügen
				AdministrationPagePresenter.this.page.getUserList().setData(new RecordList());

				// Alle Felder zurücksetzen
				resetFields();

				// Fehlermeldung ausgeben
				SC.say(Page.props.administrationPage_unlockUser_title(), caught.getMessage());
			}

			@Override
			public void onSuccess(Map<Integer, User> result) {
				try {
					// Inhalt zeigen
					AdministrationPagePresenter.this.allUsers = result;
					showUserInList(result);
				} catch (Exception e) {
					// Sollte eigentlich nicht passieren
					AdministrationPagePresenter.this.page.getUserList().setData(new RecordList());
				} finally {
					// Alle Felder zurücksetzen
					resetFields();
				}
			}
		});

		return true;
	}

	/**
	 * Sperrt einen Nutzer und schreibt dies in die Datenbank.
	 * 
	 * @param selectedUser
	 *            Aktuell ausgewählter Nutzer
	 */
	private void lockUser(final User user) {
		if (user != null) {
			// Welchen Status hat er?
			final boolean unlock = !user.getAccountData().isLocked();
			this.pageService.lockUser(user.getId(), unlock, new AsyncCallback<Boolean>() {
				@Override
				public void onFailure(Throwable caught) {
					// Buttons aktivieren
					AdministrationPagePresenter.this.page.disableAllButtons(false);

					// Fehlermeldung ausgeben
					SC.say(Page.props.administrationPage_unlockUser_title(), caught.getMessage());
				}

				@Override
				public void onSuccess(Boolean result) {
					// Nachricht ausgeben
					if (!unlock) {
						SC.say(Page.props.administrationPage_unlockUser_title(),
								Page.props.administrationPage_unlockUser_text_success());
					} else {
						SC.say(Page.props.administrationPage_lockUser_title(),
								Page.props.administrationPage_lockUser_text_success());
					}

					// Neue Nutzerdaten laden
					loadUsersFromDatabase();
				}
			});

		}
	}

	/**
	 * Setzt alle Felder zurück und löscht die den ausgewählten Nutzer.
	 * 
	 * @return true
	 */
	private boolean resetFields() {
		this.changedValues = false;
		this.page.clearAllFields(); // Felder löschen
		this.selectedUser = null; // Selected User auf null setzen
		this.page.getSearchUserTextItem().clearValue(); // Suche löschen

		// Buttons deaktivieren
		AdministrationPagePresenter.this.page.disableAllButtons(true);

		return true;
	}

	/**
	 * Speichert die neuen Nutzereinstellungen in die Datenbank.
	 * 
	 * @param user
	 *            Aktuell ausgewählter Nutzer
	 */
	private void saveUserToDatabase(final String nickname, final String email, final String firstname, final String surname,
			final int userID, final String editor) {
		this.pageService.editUser(nickname, email, firstname, surname, userID, editor, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				// Fehler anzeigen
				SC.say(Page.props.administrationPage_editUser_title(), caught.getMessage());

				// Buttons aktivieren
				AdministrationPagePresenter.this.page.disableAllButtons(false);
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					// Neue Nutzerdaten laden
					try {
						loadUsersFromDatabase();
					} catch (IllegalArgumentException e) {
						// Alles zurücksetzen
						resetFields();
					}

					// Nachricht ausgeben
					SC.say(Page.props.administrationPage_editUser_title(), Page.props.administrationPage_editUser_text_success());
				}
			}

		});
	}

	/**
	 * Zeigt die Accountdaten vom Nutzer in den Feldern an. Dabei werden die Buttons "Profildaten ändern" und "Zurücksetzen"
	 * deaktiviert, da noch keine Änderungen vorgenommen worden sind.
	 * 
	 * @param user
	 *            Nutzerobjekt
	 * 
	 * @return true, falls alles geklappt hat.
	 */
	private boolean showUser(User user) {
		try {
			// Daten in die Felder hinzufügen
			fillField(user);

			// Ausgewählter Nutzer speichern
			AdministrationPagePresenter.this.selectedUser = user;

			// 2 Buttons deaktivieren, da noch keine Änderungen vorgenommen worden sind
			AdministrationPagePresenter.this.page.getButtonEditAccount().disable();
			AdministrationPagePresenter.this.page.getButtonReset().disable();
			AdministrationPagePresenter.this.page.getButtonLockAccount().enable();

			// Um Änderungen zu merken
			changedValues = false;

		} catch (IllegalArgumentException e) {
			// Felder löschen
			resetFields();

			return false;
		}

		return true;
	}

	/**
	 * Zeigt die Nutzer in der Tabelle an.
	 * 
	 * @param userlist
	 *            Map mit Userobjekten
	 * @return true, falls alles geklappt hat.
	 * @throws IllegalArgumentException
	 *             Wird geworfen, wenn der HashMap null ist
	 */
	private boolean showUserInList(Map<Integer, User> userlist) throws IllegalArgumentException {
		if (userlist == null) {
			throw new IllegalArgumentException();
		}

		// Alte löschen
		this.userData = new RecordList();
		for (final User user : userlist.values()) {
			this.userData.add(new UserListRecord(user));
		}

		// Anzeigen
		this.page.getUserList().setData(this.userData);

		// Es hat alles geklappt
		return true;
	}
}
