package prototyp.client.presenter.frontpage;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.TabManager;
import prototyp.client.presenter.administration.AdministrationPagePresenter;
import prototyp.client.presenter.lobby.HighScorePagePresenter;
import prototyp.client.presenter.lobby.LobbyPagePresenter;
import prototyp.client.presenter.lobby.LogoutPresenter;
import prototyp.client.presenter.lobby.ProfilePagePresenter;
import prototyp.client.presenter.lobby.TutorialPagePresenter;
import prototyp.client.presenter.mapgenerator.MapGeneratorPresenter;
import prototyp.client.presenter.user.UserPresenter;
import prototyp.client.service.FrontPageService;
import prototyp.client.service.FrontPageServiceAsync;
import prototyp.client.service.LobbyService;
import prototyp.client.service.LobbyServiceAsync;
import prototyp.client.util.CryptManager;
import prototyp.client.util.MusicManager;
import prototyp.client.view.Page;
import prototyp.client.view.frontpage.FrontPage;
import prototyp.server.view.FrontPageImpl;
import prototyp.shared.exception.mail.AccountLockedException;
import prototyp.shared.exception.mail.MailNotFoundException;
import prototyp.shared.useradministration.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;

/**
 * Presenter des Views "FrontPage" Erzeugt außerdem ein UserObjekt für den Nutzer und instantiiert erstmals den UserPresenter
 * 
 * @author Andreas Rehfeldt, Timo, Marina, Kamil (Verantwortlicher)
 * @version 1.3
 * @version 1.4, Kamil, 22.09.2010: Es wird geprüft ob der Nutzer gesperrt ist und eine entsprechende Meldung ausgegeben.
 * @version 1.6, Andreas, 29.09.2010: Buttons werden deaktiviert beim Login
 * @version 1.5, Marina, 03.10.2010: LogoutPresenter zur AddListeners-Methode der TabManager Klasse hinzugefügt
 * @version 1.6, Marina, 07.11.2010: Exceptions beim Passwort-Vergessen
 * 
 * @see {@link FrontPage}
 */
public class FrontPagePresenter implements PagePresenter {

	/**
	 * Handler für das Einloggen
	 * 
	 * @author Andreas, Kamil (Verantwortlicher)
	 * @version 1.0
	 */
	class LogInHandler implements ClickHandler, KeyPressHandler {
		private final FrontPageServiceAsync logInService;
		private FrontPage page;

		public LogInHandler(Page page) {
			this.logInService = GWT.create(FrontPageService.class);
			this.page = (FrontPage) page;
		}

		/**
		 * Sendet Namen und Passwort an den Server, der vergleicht beides mit der Datenbank
		 * 
		 * @see FrontPageImpl
		 */
		private void login() {
			if (this.page.validate()) {
				final String nickname = this.page.getNameField().getValue().toString();
				final String password = this.page.getPasswordField().getValue().toString();
				int userId = -1;
				// Loginbutton deaktivieren
				this.page.disableButtons(true);

				if (Cookies.getCookie("userId") != null) {
					userId = Integer.valueOf(Cookies.getCookie("userId"));
				}

				this.logInService.validateLogIn(nickname, password, userId, new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {

						// Ausgeben
						SC.say(Page.props.frontPage_title(), caught.getMessage());

						// Buttons wieder entsperren
						LogInHandler.this.page.disableButtons(false);
					}

					@Override
					public void onSuccess(Boolean result) {

						// Eine User Instanz für den Nutzer erzeugen
						// und den UserPresenter erstmals instantiieren
						FrontPagePresenter.this.lobbyService.getUser(nickname, new AsyncCallback<User>() {
							@Override
							public void onFailure(Throwable caught) {
								// Ausgeben
								SC.say(Page.props.frontPage_title(), caught.getMessage());

								// Buttons wieder entsperren
								LogInHandler.this.page.disableButtons(false);
							}

							@Override
							public void onSuccess(final User userResult) {

								LogInHandler.this.page.getProgressbar().setVisible(true);

								new Timer() {

									private int counter = 0;
									private LobbyPagePresenter lobbyPagePresenter;
									private HighScorePagePresenter highscorePresenter;
									private ProfilePagePresenter profilePresenter;
									private AdministrationPagePresenter adminPresenter;
									private MapGeneratorPresenter mapGeneratorPresenter;
									private LogoutPresenter logoutPresenter;
									private TutorialPagePresenter tutorialPresenter;

									@Override
									public void run() {
										/*
										 * Hier wird erstmals der UserPresenter erzeugt und kann hinterher statisch aufgerufen
										 * werden
										 */
										if (this.counter == 0) {
											UserPresenter.putUser(userResult);
											this.counter = 1;
											LogInHandler.this.page.getProgressbar().setPercentDone(10);
											schedule(20);
										}
										/*
										 * Nur wenn dieses Callback aufgerufen wird, werden die Tabs erzeugt. (Weil Ajax asychron
										 * ist)
										 */

										else if (this.counter == 1) {
											// Presenter erzeugen
											this.lobbyPagePresenter = new LobbyPagePresenter();
											this.counter = 2;
											LogInHandler.this.page.getProgressbar().setPercentDone(20);
											schedule(20);
										}

										else if (this.counter == 2) {
											this.highscorePresenter = new HighScorePagePresenter();
											this.counter = 3;
											LogInHandler.this.page.getProgressbar().setPercentDone(30);
											schedule(20);
										}

										else if (this.counter == 3) {
											this.profilePresenter = new ProfilePagePresenter();
											this.counter = 4;
											LogInHandler.this.page.getProgressbar().setPercentDone(40);
											schedule(20);
										}

										// Administrator

										else if (this.counter == 4) {
											this.adminPresenter = null;
											if (UserPresenter.getInstance().isAdmin()) {
												this.adminPresenter = new AdministrationPagePresenter();
											}
											this.counter = 5;
											LogInHandler.this.page.getProgressbar().setPercentDone(50);
											schedule(20);
										}

										else if (this.counter == 5) {
											this.mapGeneratorPresenter = new MapGeneratorPresenter();
											this.counter = 6;
											LogInHandler.this.page.getProgressbar().setPercentDone(75);
											schedule(20);
										}

										else if (this.counter == 6) {
											this.logoutPresenter = new LogoutPresenter();
											this.counter = 7;
											LogInHandler.this.page.getProgressbar().setPercentDone(90);
											schedule(120);
										}

										else if (this.counter == 7) {
											TabManager.getInstanceChild().addListener(this.adminPresenter,
													this.highscorePresenter, this.profilePresenter, this.logoutPresenter);
											this.counter = 8;
											LogInHandler.this.page.getProgressbar().setVisible(false);
											schedule(100);
										}

										else if (this.counter == 8) {
											TabManager.getInstanceChild().switchToTab(this.lobbyPagePresenter);

											// Nur abspielen, wenn noch nichts
											// geladen/abgespielt wurde
											if (MusicManager.getInstance().getSound() == null) {
												MusicManager.getInstance().play("robocraft-back-1_2.mp3");
											}

											this.counter = 9;
											LogInHandler.this.page.getProgressbar().setPercentDone(100);
											schedule(100);
										}

										else if (this.counter == 9) {
											TabManager.getInstanceChild().addTab(this.highscorePresenter);
											this.counter = 10;
											schedule(100);
										}

										else if (this.counter == 10) {
											TabManager.getInstanceChild().addTab(this.profilePresenter);
											this.counter = 11;
											schedule(100);
										}

										else if (this.counter == 11) {
											if (UserPresenter.getInstance().isAdmin()) {
												TabManager.getInstanceChild().addTab(this.adminPresenter);
											}
											this.counter = 12;
											schedule(100);
										}

										else if (this.counter == 12) {
											TabManager.getInstanceChild().addTab(this.mapGeneratorPresenter);
											this.counter = 13;
											schedule(100);
										}

										else if (this.counter == 13) {
											this.tutorialPresenter = new TutorialPagePresenter();
											TabManager.getInstanceChild().addTab(this.tutorialPresenter);
											this.counter = 14;
											schedule(100);
										}

										else if (this.counter == 14) {
											TabManager.getInstanceChild().addTab(this.logoutPresenter);
											this.counter = 15;
											schedule(100);
										}

										else {
											// Cookies anlegen
											Cookies.setCookie("nickname", nickname);

											// User ID im Cookie speichern um mehreres Einlogen mit einem Browser zu verhindern
											Cookies.setCookie("userId", "" + UserPresenter.getInstance().getUser().getId());

											// Passwort verschlüsseln
											final String encpasswd = CryptManager.getInstance().encrypt(password);

											// Passwort nur abspeichern, wenn es gültig verschlüsselt wurde
											if (encpasswd != null) {
												Cookies.setCookie("password", encpasswd);
											}
										}
									}
								}.schedule(20);
							}
						});
					}
				});// Ende validateLogIn
			}
		}

		/**
		 * Wird ausgeführt, wenn der Button "sendButton" gedrückt wird
		 * 
		 * @param event
		 *            ClickEvent
		 * @version 1.1 Login wird nun im Timer ausgeführt
		 */
		@Override
		public void onClick(ClickEvent event) {
			login();
		}

		/**
		 * Wird ausgeführt, entweder das Nickname- oder Passwortfeld selektiert ist und Enter gedrückt wird.
		 * 
		 * @param event
		 *            KeyPressEvent
		 */
		@Override
		public void onKeyPress(KeyPressEvent event) {
			if (event.getKeyName() != null && event.getKeyName().equals("Enter")) {
				login();
			}
		}
	}

	/**
	 * Handler für das Passwortvergessen
	 * 
	 * @author Andreas, Marina (Verantwortlicher)
	 * 
	 * @version 1.1 1.2, Marina, 26.09.2010: NullPointerException hinzugefügt.
	 * @version 1.4 Läuft nun richtig, Abbruchbutton hinzugefügt und komplett den Code umgestellt --Andreas
	 * @version 1.5 Den View Teil in die FrontPage gelegt (Marina)
	 * 
	 */
	static class SendPasswordHandler implements ClickHandler {
		private FrontPage page;
		private final FrontPageServiceAsync sendPasswordService;

		/**
		 * Konstruktor
		 * 
		 * @param page
		 */
		public SendPasswordHandler(Page page) {
			this.sendPasswordService = GWT.create(FrontPageService.class);
			this.page = (FrontPage) page;
		}

		/**
		 * Fügt alle Listener für das Passwort-Vergessen hinzu
		 */
		private boolean addSendPasswordListener() {

			// Das Fenster kann nun geschlossen werden
			this.page.getWinModal().addCloseClickHandler(new CloseClickHandler() {
				@Override
				public void onCloseClick(CloseClientEvent event) {
					SendPasswordHandler.this.page.getWinModal().clear();
				}
			});

			// Abbruch-Button wird geklickt
			this.page.getButtonAbort().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					SendPasswordHandler.this.page.getWinModal().clear();
				}
			});

			// E-Mail Feld kann nun mit Enter abgeschickt werden
			this.page.getEMailField().addKeyPressHandler(new KeyPressHandler() {
				@Override
				public void onKeyPress(KeyPressEvent event) {
					if (event.getKeyName() != null && event.getKeyName().equals("Enter")
							&& !SendPasswordHandler.this.page.getButtonSend().isDisabled()) {
						if (SendPasswordHandler.this.page.getEmailarea().validate()) {
							SendPasswordHandler.this.page.getButtonSend().setDisabled(false);
							sendPassword(SendPasswordHandler.this.page.getEMailField().getValue().toString());
						}
					}
				}
			});

			// ClickHandler für den Passwort-Button
			this.page.getButtonSend().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (SendPasswordHandler.this.page.getEmailarea().validate()) {
						SendPasswordHandler.this.page.getButtonSend().setDisabled(false);
						sendPassword(SendPasswordHandler.this.page.getEMailField().getValue().toString());
					}
				}
			});

			return true;
		}

		/**
		 * Erstellt ein Fenster mit zugehörigem Button und Textfeld um eine E-Mail zu verschicken
		 * 
		 * @param event
		 *            ClickEvent
		 */
		@Override
		public void onClick(ClickEvent event) {
			showPopup();
			addSendPasswordListener();
		}

		/**
		 * Sendet die E-Mail Adresse an den Server, der vergleicht diese mit den E-Mail Adressen in der Datenbank
		 * 
		 * @param email
		 *            E-Mail als String
		 */
		private void sendPassword(String email) {
			// Den String in Kleinbuchstaben umwandeln
			email = email.trim().toLowerCase();

			// E-Mail Adresse überprüfen
			this.sendPasswordService.sendNewPassword(email, new AsyncCallback<Boolean>() {
				@Override
				public void onFailure(Throwable caught) {
					// Button entsperren
					SendPasswordHandler.this.page.getButtonSend().setDisabled(false);

					// Fehlerbehebung
					try {
						throw caught;
					} catch (MailNotFoundException e) {
						SC.say(Page.props.frontPage_window_exception_title(), e.getMessage());
					} catch (AccountLockedException e) {
						SC.say(Page.props.frontPage_window_exception_title(), e.getMessage());
					} catch (Throwable e) {
						SC.say(Page.props.frontPage_window_exception_title(), e.getMessage());
						SendPasswordHandler.this.page.getWinModal().clear();
					}
				}

				@Override
				public void onSuccess(Boolean result) {
					SC.say(Page.props.frontPage_buttonSendPassword_text(), Page.props.frontPage_sendPassword_success_title());
					SendPasswordHandler.this.page.getWinModal().clear();
				}
			});
		}

		/**
		 * Aktiviert das Popup für die Passwort-Vergessen Funktion
		 * 
		 * @return true
		 */
		public boolean showPopup() {
			this.page.getWinModal().show();
			return true;
		}
	}

	/** Async-Objekt */
	private final LobbyServiceAsync lobbyService;

	/** Zugehörige Page */
	private FrontPage page;

	/**
	 * Konstruktor
	 */
	public FrontPagePresenter() {
		this.lobbyService = GWT.create(LobbyService.class);

		// FrontPage initialisieren
		this.page = new FrontPage();

		// Listener hinzufügen
		addListeners();
	}

	/**
	 * Fügt alle Listener hinzu.
	 * 
	 * @return true, wenn alles geklappt hat.
	 */
	private boolean addListeners() {
		// Hilfsvariablen
		LogInHandler logInHandler = new LogInHandler(this.page);
		SendPasswordHandler SendPasswordHandler = new SendPasswordHandler(this.page);

		// Einloggen
		this.page.getButtonLogIn().addClickHandler(logInHandler);
		this.page.getNameField().addKeyPressHandler(logInHandler);
		this.page.getPasswordField().addKeyPressHandler(logInHandler);

		// Passwort vergessen
		this.page.getButtonSendPassword().addClickHandler(SendPasswordHandler);

		// Registrieren
		this.page.getButtonRegister().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				TabManager.getInstanceChild().switchToTab(new RegistrationPagePresenter());
			}
		});

		// Auf Deutsche Sprache ändern
		this.page.getGermanImg().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				changeLanguage(0);
			}
		});

		// Auf Englische Sprache ändern
		this.page.getEnglishImg().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				changeLanguage(1);
			}
		});

		return true;
	}

	/**
	 * Ändert die Sprache. In der Klasse "Prototyp" wird die Sprache wieder ausgelesen.
	 * 
	 * @param language
	 *            Integer, der für eine bestimmte Sprache steht
	 */
	private void changeLanguage(int language) {
		// In Cookie speichern
		Cookies.setCookie("language", "" + language);

		// Seite neu laden
		Window.Location.reload();
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
}
