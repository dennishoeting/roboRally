package prototyp.client.presenter.frontpage;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.TabManager;
import prototyp.client.service.FrontPageService;
import prototyp.client.service.FrontPageServiceAsync;
import prototyp.client.view.Page;
import prototyp.client.view.frontpage.RegistrationPage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * RegistrationPagePresenter
 * 
 * @author Jens (Verantwortlicher), Dennis
 * @version 1.2
 * @version 1.3 Agbs werden aus other/agbs.txt geladen. -Timo
 * 
 * @see RegistrationPage
 */
public class RegistrationPagePresenter implements PagePresenter {
	/** Zugehörige Page */
	private RegistrationPage page;

	/** Async_service */
	private final FrontPageServiceAsync pageService;

	/**
	 * Konstruktor
	 */
	public RegistrationPagePresenter() {
		this.pageService = GWT.create(FrontPageService.class);

		this.page = new RegistrationPage();

		// Fügt die Listener hinzu
		addListeners();
		// Submit erst aktivieren wenn die AGBS geladen wurden.
		this.page.getButtonSubmit().disable();

		// AGBs laden
		this.pageService.getAGBs(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				// Ausgeben
				SC.say(Page.props.registrationPage_title(), caught.getMessage());
			}

			@Override
			public void onSuccess(String result) {
				if (result != null) {
					// Ergebnis setzen und Button aktivieren.
					RegistrationPagePresenter.this.page.getAgbPane().setContents(result);
					RegistrationPagePresenter.this.page.getButtonSubmit().enable();
				}

			}

		});
	}

	/** Listener für Weiter und Abbrechen-Button */
	private boolean addListeners() {
		this.page.getAbortButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				TabManager.getInstanceChild().switchToTab(new FrontPagePresenter());
			}
		});

		/*
		 * Einfügen der Daten in die DB, falls Validierung erfolgreich. Falls firstname/surname leer, füge "" ein.
		 */
		this.page.getButtonSubmit().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (RegistrationPagePresenter.this.page.validate()) {
					Object firstnameObj = RegistrationPagePresenter.this.page.getFirstnameTextItem().getValue(); // Müssen noch
																													// Felder sein
					Object surnameObj = RegistrationPagePresenter.this.page.getSurnameTextItem().getValue();
					register(RegistrationPagePresenter.this.page.getNicknameTextItem().getValue().toString(),
							RegistrationPagePresenter.this.page.getEmailTextItem().getValue().toString(),
							RegistrationPagePresenter.this.page.getPassword1TextItem().getValue().toString(),
							(firstnameObj == null ? "" : firstnameObj.toString()),
							(surnameObj == null ? "" : surnameObj.toString()));
				}
			}

		});

		return true;
	}

	/**
	 * Liefert die Seite zurück
	 * 
	 * @return page
	 */
	@Override
	public Canvas getPage() {
		return this.page;
	}

	/**
	 * Trägt Name etc. in die DB ein.
	 * 
	 * @param password
	 *            Passwort
	 * @param email
	 *            E-Mailadresse
	 * @param nickname
	 *            Nickname
	 * @param firstname
	 *            Vorname
	 * @param surname
	 *            Nachname
	 * @return true, immer
	 * 
	 */
	public boolean register(String nickname, String email, String password, String firstname, String surname) {
		this.pageService.registerUser(nickname, email, password, firstname, surname, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				// Ausgeben
				SC.say(Page.props.registrationPage_title(), caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				TabManager.getInstanceChild().switchToTab(new FrontPagePresenter());
				SC.say(Page.props.registrationPage_welcome_titel(), Page.props.registrationPage_welcome());
			}
		});

		return true;
	}
}
