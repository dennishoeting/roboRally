package prototyp.client.presenter.lobby;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.IUploader.Utils;

import java.util.ArrayList;
import java.util.List;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.TabManager;
import prototyp.client.presenter.user.UserPresenter;
import prototyp.client.service.LobbyService;
import prototyp.client.service.LobbyServiceAsync;
import prototyp.client.util.StandardLabel;
import prototyp.client.util.StatisticPreferencesRecord;
import prototyp.client.view.Page;
import prototyp.client.view.lobby.ProfilePage;
import prototyp.client.view.lobby.ProfilePictureWindow;
import prototyp.shared.useradministration.AccountData;
import prototyp.shared.useradministration.Award;
import prototyp.shared.useradministration.Statistic;
import prototyp.shared.useradministration.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

/**
 * ProfilePage
 * 
 * @author Kamil (Verantwortlicher), Andreas
 * @version 1.0
 * @version 1.1 fillWithContent() editiert (30.09.10, Robert)
 * @version 1.2 Zurücksetzen hinzugefügt --Andreas
 * 
 * @see ProfilePage
 */
public class ProfilePagePresenter implements PagePresenter {
	/** Async-Objekt */
	private final LobbyServiceAsync lobbyService;

	/** Page */
	private ProfilePage page;

	/** Statistik des Users */
	private RecordList statisticData;

	/** Angabe, ob sich die Felder geändert haben */
	private boolean changedValues = false;

	/**
	 * Konstruktor
	 */
	public ProfilePagePresenter() {
		this.lobbyService = GWT.create(LobbyService.class);

		this.page = new ProfilePage();
		
		// Fügt die Listener hinzu
		addListeners();
	}

	/**
	 * Fügt Listener hinzu
	 * 
	 * @return true, falls alles geklappt hat
	 */
	private boolean addListeners() {
		// Zurücksetzen wurde geklickt
		this.page.getButtonReset().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				try {
					// Füllt die Edit-Profil-Textfelder
					setAccountData(UserPresenter.getInstance().getUser());
				} catch (IllegalArgumentException e) {
					// Wird verschluckt. Darf eigentlich nicht vorkommen.
				}
			}
		});

		// Profildaten ändern wurde geklickt
		this.page.getButtonEdit().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (ProfilePagePresenter.this.page.validateEditFields()) {
					// Optionale Felder behandeln
					final String firstname = ProfilePagePresenter.this.page.getFirstnameTextItem().getValue() != null ? ProfilePagePresenter.this.page
							.getFirstnameTextItem().getValue().toString()
							: "";
					final String surname = ProfilePagePresenter.this.page.getSurnameTextItem().getValue() != null ? ProfilePagePresenter.this.page
							.getSurnameTextItem().getValue().toString()
							: "";

					// Neue Profildaten speichern
					saveNewAccountDataToDatabase(ProfilePagePresenter.this.page.getNicknameTextItem().getValue().toString(),
							ProfilePagePresenter.this.page.getEmailTextItem().getValue().toString(), firstname, surname,
							UserPresenter.getInstance().getUser().getId());
				}
			}
		});

		// Passwort ändern wurde geklickt
		this.page.getButtonEditPassword().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (ProfilePagePresenter.this.page.validatePasswordFields()) {
					// Button deaktivieren
					ProfilePagePresenter.this.page.getButtonEditPassword().setDisabled(true);

					// Neues Passwort speichern
					saveNewPasswordToDatabase(ProfilePagePresenter.this.page.getOldPasswordTextItem().getValue().toString(),
							ProfilePagePresenter.this.page.getNewPasswordTextItem().getValue().toString(), UserPresenter
									.getInstance().getUser().getAccountData().getNickname(), UserPresenter.getInstance()
									.getUser().getId());
				}
			}
		});

		// Listener für den Photouploader
		this.page.getPhotoUploader().addOnFinishUploadHandler(new OnFinishUploaderHandler() {

			@Override
			public void onFinish(IUploader uploader) {
				// Wenn das Bild erfolgreich hochgeladen wurde
				if (uploader.getStatus() == Status.SUCCESS) {
					// XML parsen:
					Document doc = XMLParser.parse(uploader.getServerResponse());
					// Hier möchte ich nur den Namen des Images haben
					String imageName = Utils.getXmlNodeValue(doc, "file-name");
					if (imageName != null && !imageName.equals("")) {
						// Jetzt das Bild richtig benennen, verschieben usw.
						ProfilePagePresenter.this.lobbyService.setUserPicture(UserPresenter.getInstance().getUser().getId(),
								imageName, new AsyncCallback<String>() {

									@Override
									public void onFailure(Throwable caught) {
										// Ausgeben
										SC.say(Page.props.profilePage_title(), caught.getMessage());
									}

									@Override
									public void onSuccess(String result) {
										// Bild wird angezeigt
										new ProfilePictureWindow(result);
									}

								});
					} else {
						SC.say(Page.props.profilePage_title(), Page.props.profilePage_photoUploadError());
					}
				}
			}

		});

		// Wird bei allen Felder benutzt, um Änderungen zu registrieren
		ChangedHandler fieldChangeHandler = new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				if (!changedValues) {
					changedValues = true;

					// Buttons aktivieren
					page.getButtonEdit().enable();
					page.getButtonReset().enable();
				}
			}
		};

		// Felder bekommen ChangedHandler, um Änderungen zu bemerken
		this.page.getNicknameTextItem().addChangedHandler(fieldChangeHandler);
		this.page.getEmailTextItem().addChangedHandler(fieldChangeHandler);
		this.page.getSurnameTextItem().addChangedHandler(fieldChangeHandler);
		this.page.getFirstnameTextItem().addChangedHandler(fieldChangeHandler);

		// Wird bei allen Passwortfelder benutzt, um Änderungen zu registrieren
		ChangedHandler passwordChangeHandler = new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				PasswordItem[] textField = { page.getOldPasswordTextItem(), page.getNewPasswordTextItem(),
						page.getNewPassword2TextItem() };

				boolean error = false;
				// Felder prüfen, ob nicht alle leer sind
				for (PasswordItem field : textField) {
					if (field.getValue() == null || ((String) field.getValue()).equals("")) {
						error = true;
						break;
					}
				}

				// Button deaktivieren oder aktivieren
				if (!error) {
					page.getButtonEditPassword().enable();
				} else {
					page.getButtonEditPassword().disable();
				}
			}
		};

		// Passwortfelder bekommen ChangedHandler, um Änderungen zu bemerken
		this.page.getOldPasswordTextItem().addChangedHandler(passwordChangeHandler);
		this.page.getNewPasswordTextItem().addChangedHandler(passwordChangeHandler);
		this.page.getNewPassword2TextItem().addChangedHandler(passwordChangeHandler);

		return true;
	}

	/**
	 * Füllt alle Felder mit Nutzerdaten. Funktion wird im TabManager aufgerufen.
	 * 
	 * @return true, falls alles geklappt hat
	 * 
	 * @see TabManager#addListener(prototyp.client.presenter.administration.AdministrationPagePresenter, HighScorePagePresenter)
	 */
	public boolean fillFieldsWithUserData() {
		try {
			// Neue Userdaten laden
			fillWithContent(UserPresenter.getInstance().getUser());
		} catch (IllegalArgumentException e) {
			// Alle Buttons deaktivieren
			this.page.disableAllButtons();
		}
		return true;
	}

	/**
	 * Füllt die Profilseite mit Inhalt
	 * 
	 * @param user
	 *            Aktuelle eingeloggtes User-Objekt aus dem UserPresenter
	 * @return true, falls alles geklappt hat
	 * @throws IllegalArgumentException
	 *             Wird geworfen, wenn das User-Objekt null ist
	 */
	private boolean fillWithContent(final User user) throws IllegalArgumentException {
		if (user == null) {
			throw new IllegalArgumentException();
		}

		// Alle Fehlermeldungen löschen
		this.page.clearAllErrors();

		// Füllt die Edit-Profil-Textfelder
		setAccountData(user);

		// Allgemeine Statistik anzeigen
		ProfilePagePresenter.this.lobbyService.getUserStatistic(user.getId(), new AsyncCallback<Statistic>() {

			@Override
			public void onFailure(Throwable caught) {
				// Anzeigen
				page.getStatisticListGrid().setData(new RecordList());
			}

			@Override
			public void onSuccess(Statistic result) {
				// Statistik zusammenbauen und anzeigen
				statisticData = new RecordList();

				statisticData.add(new StatisticPreferencesRecord(1, Page.props.statistic_highscorerank_title(), result
						.getHighScoreRang()));
				statisticData.add(new StatisticPreferencesRecord(1, Page.props.statistic_rank_title(), result.getUserRang()));
				statisticData.add(new StatisticPreferencesRecord(1, Page.props.statistic_points_title(), result.getPoints()));
				statisticData.add(new StatisticPreferencesRecord(1, Page.props.statistic_playedgames_title(), result
						.getPlayedGames()));
				statisticData.add(new StatisticPreferencesRecord(1, Page.props.statistic_wins_title(), result.getWins()));
				statisticData.add(new StatisticPreferencesRecord(1, Page.props.statistic_lostgames_title(), result.getLostGames()));
				statisticData.add(new StatisticPreferencesRecord(1, Page.props.statistic_abortedgames_title(), result
						.getAbortedGames()));

				// Anzeigen
				page.getStatisticListGrid().setData(statisticData);

				// Letzte Karten holen
				ProfilePagePresenter.this.lobbyService.getLastMaps(user.getId(), new AsyncCallback<ArrayList<String>>() {
					@Override
					public void onFailure(Throwable caught) {
						SC.say(Page.props.profilePage_title(), caught.getMessage());
					}

					@Override
					public void onSuccess(ArrayList<String> result) {
						// 2. Die letzten Karten anzeigen
						if (result != null && result.size() > 0) {
							// Altes löschen
							page.getStatisticListGrid().setData(new RecordList());

							if (result.get(0) != null) {
								statisticData.add(new StatisticPreferencesRecord(2, Page.props.statistic_playingboard_title()
										+ " 1", result.get(0)));
							}
							if (result.size() > 1 && result.get(1) != null) {
								statisticData.add(new StatisticPreferencesRecord(2, Page.props.statistic_playingboard_title()
										+ " 2", result.get(1)));
							}
							if (result.size() > 2 && result.get(2) != null) {
								statisticData.add(new StatisticPreferencesRecord(2, Page.props.statistic_playingboard_title()
										+ " 3", result.get(2)));
							}

							// Neue Statistik anzeigen
							page.getStatisticListGrid().setData(statisticData);
						}
					}

				});
			}

		});

		// 3.Das Awards Feld
		this.lobbyService.getUserAwards(user.getId(), new AsyncCallback<List<Award>>() {
			@Override
			public void onFailure(Throwable caught) {
				// Alles löschen
				ProfilePagePresenter.this.page.getAwardsArea(0).removeMembers(
						ProfilePagePresenter.this.page.getAwardsArea(0).getMembers());
				ProfilePagePresenter.this.page.getAwardsArea(1).removeMembers(
						ProfilePagePresenter.this.page.getAwardsArea(1).getMembers());
				ProfilePagePresenter.this.page.getAwardsArea(2).removeMembers(
						ProfilePagePresenter.this.page.getAwardsArea(2).getMembers());

				// Hinweis anzeigen, dass noch keine Awards da sind.
				StandardLabel label = new StandardLabel(Page.props.profilePagePresenter_noAwards_text());
				ProfilePagePresenter.this.page.getAwardsArea(0).addMember(label);
			}

			@Override
			public void onSuccess(List<Award> result) {
				if (result != null) {
					// Alles löschen
					ProfilePagePresenter.this.page.getAwardsArea(0).removeMembers(
							ProfilePagePresenter.this.page.getAwardsArea(0).getMembers());
					ProfilePagePresenter.this.page.getAwardsArea(1).removeMembers(
							ProfilePagePresenter.this.page.getAwardsArea(1).getMembers());
					ProfilePagePresenter.this.page.getAwardsArea(2).removeMembers(
							ProfilePagePresenter.this.page.getAwardsArea(2).getMembers());

					if (result.size() > 0) {
						for (int i = 0; i < result.size(); i++) {
							Img temp = new Img(Award.IMAGE_PATH + result.get(i).getImageFileName());
							temp.setTooltip(result.get(i).getName() + ": " + result.get(i).getDescription());
							temp.setSize("55", "55");
							ProfilePagePresenter.this.page.getAwardsArea(i / 6).addMember(temp);
						}
					} else {
						// Hinweis anzeigen, dass noch keine Awards da sind.
						StandardLabel label = new StandardLabel(Page.props.profilePagePresenter_noAwards_text());
						ProfilePagePresenter.this.page.getAwardsArea(0).addMember(label);
					}
				}
			}
		});
		return true;
	}

	/**
	 * Liefert die Page
	 * 
	 * @return page
	 */
	@Override
	public Canvas getPage() {
		return this.page;
	}

	/**
	 * Speichert die neuen Accountdaten vom Nutzer in die Datenbank
	 * 
	 * @param nickname
	 *            neuer Nickname des Users
	 * @param email
	 *            neue Emailadresse
	 * @param firstname
	 *            neuer Vorname
	 * @param surname
	 *            neuer Nachname
	 */
	private void saveNewAccountDataToDatabase(final String nickname, final String email, final String firstname,
			final String surname, int userID) {
		// Buttons deaktivieren
		ProfilePagePresenter.this.page.getButtonEdit().disable();
		ProfilePagePresenter.this.page.getButtonReset().disable();

		// Hat sich der Nickname geändert? Dann wird das Passwort benötigt
		if (!nickname.equals(UserPresenter.getInstance().getNickname())) {
			new PasswordWindowPresenter(this, nickname, email, firstname, surname, userID);
		} else {
			// Wird ausgeführt, wenn der Nickname nicht geändert wird
			this.lobbyService.editUser(nickname, email, firstname, surname, userID, new AsyncCallback<Boolean>() {
				@Override
				public void onFailure(Throwable caught) {
					// Button wieder aktivieren
					ProfilePagePresenter.this.page.getButtonEdit().setDisabled(false);
					ProfilePagePresenter.this.page.getButtonReset().setDisabled(false);

					// Ausgeben
					SC.say(Page.props.administrationPage_editUser_title(), caught.getMessage());
				}

				@Override
				public void onSuccess(Boolean result) {
					// Änderungen im Userobjekt speichern
					final AccountData data = UserPresenter.getInstance().getUser().getAccountData();
					data.setNickname(nickname);
					data.setEmail(email);
					data.setFirstname(firstname);
					data.setSurname(surname);
					UserPresenter.getInstance().getUser().setAccountData(data);

					// Page anzeigen
					fillFieldsWithUserData();

					// Meldung ausgeben
					SC.say(Page.props.administrationPage_editUser_title(), Page.props.administrationPage_editUser_text_success());
				}
			});
		}
	}

	/**
	 * Speichert das neue Passwort vom Nutzer in die Datenbank. Dabei wird überprüft, ob das der Benutzer das aktuelle Passwort
	 * richtig eingegeben hat.
	 * 
	 * @param oldPassword
	 *            Altes Passwort
	 * @param newPassword
	 *            Neues Passwort
	 * @param nickname
	 *            Nickname des Users
	 * @param userID
	 *            UserID
	 */
	private void saveNewPasswordToDatabase(String oldPassword, String newPassword, String nickname, int userID) {
		this.lobbyService.editUserPassword(oldPassword, newPassword, nickname, nickname, userID, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				// Button wieder aktivieren
				ProfilePagePresenter.this.page.getButtonEditPassword().setDisabled(false);

				// Ausgeben
				SC.say(Page.props.profilePage_editPassword_title(), caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					// Button wieder aktivieren
					ProfilePagePresenter.this.page.getButtonEditPassword().setDisabled(false);

					// Felder leeren
					ProfilePagePresenter.this.page.getOldPasswordTextItem().clearValue();
					ProfilePagePresenter.this.page.getNewPasswordTextItem().clearValue();
					ProfilePagePresenter.this.page.getNewPassword2TextItem().clearValue();

					// Passwort wurde geändert
					SC.say(Page.props.profilePage_editPassword_title(), Page.props.profilePage_editPassword_text_success());
				}
			}
		});
	}

	/**
	 * Füllt die Edit-Profil-Textfelder
	 * 
	 * @param user
	 *            Userobjekt
	 * 
	 * @return true
	 * @throws IllegalArgumentException
	 *             Wird geworfen, wenn das User-Objekt null ist
	 */
	private void setAccountData(User user) throws IllegalArgumentException {
		if (user == null) {
			throw new IllegalArgumentException();
		}

		// Alle Buttons deaktivieren
		this.page.disableAllButtons();

		// Accoutfelder
		this.page.getNicknameTextItem().setValue(user.getAccountData().getNickname());
		this.page.getFirstnameTextItem().setValue(user.getAccountData().getFirstname());
		this.page.getSurnameTextItem().setValue(user.getAccountData().getSurname());
		this.page.getEmailTextItem().setValue(user.getAccountData().getEmail());

		// Passwortfelder leeren
		ProfilePagePresenter.this.page.getOldPasswordTextItem().clearValue();
		ProfilePagePresenter.this.page.getNewPasswordTextItem().clearValue();
		ProfilePagePresenter.this.page.getNewPassword2TextItem().clearValue();

		// Angabe, ob Änderungen vorgenommen wurden
		changedValues = false;
	}
}
