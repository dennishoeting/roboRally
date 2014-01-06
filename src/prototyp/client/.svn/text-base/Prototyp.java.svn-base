package prototyp.client;

import prototyp.client.presenter.TabManager;
import prototyp.client.presenter.frontpage.FrontPagePresenter;
import prototyp.client.properties.PropertiesDe;
import prototyp.client.properties.PropertiesEn;
import prototyp.client.util.CryptManager;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.MusicManager;
import prototyp.client.view.Page;
import prototyp.client.view.frontpage.FrontPage;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Slider;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.ValueChangedEvent;
import com.smartgwt.client.widgets.events.ValueChangedHandler;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.tab.Tab;

/**
 * Implementierung des GWT-Einstiegspunktes, somit Festlegung der Startseite der Webapplikation. Die Startseite des Prototyps ist
 * die FrontPage.
 * 
 * @author Dennis
 * 
 * @version 1.3 Sound eingebaut und Soundcookie wird nun angelegt und ausgelesen
 * @version 1.2 (25.08.10) View-Transition von Dennis
 * @version 1.1 (21.06.10) LogIn-Methodik von Jannik, Kamil, Jens
 * 
 * @see GWTTabManager
 */
public class Prototyp implements EntryPoint {

	/** Genereller Container für das Spiel */
	private VStack container;

	/** Startseiten-Presenter */
	private FrontPagePresenter frontPagePresenter;

	/** Sound-Button */
	private ImgButton soundButton;

	/** Soundslider für die Musiklautstärke */
	private Slider soundSlider;

	/** Angabe, ob Sound gespielt werden soll */
	private boolean soundFlag;

	/** Aktuelle Lautstärke der Musik */
	private float sliderValue = 50;

	/**
	 * Aktiviert oder Deaktiviert den MusicSlider und -Button. Gleichzeitig werden alle Einstellungen in ein Cookie gespeichert.
	 * 
	 * @param flag
	 *            Angabe, ob die Music gestoppt oder gestartet werden soll
	 */
	private void disableMusic(boolean flag) {
		if (flag) {
			/*
			 * Button ändern
			 */
			this.soundFlag = false;
			this.soundButton.setSrc("icons/soundOff.png");
			this.soundButton.setBackgroundImage("icons/soundOff.png");

			/*
			 * MusicManager stoppen
			 */
			MusicManager.getInstance().setCanPlaySounds(false);

			/*
			 * Slider anpassen und Wert speichern
			 */
			this.sliderValue = this.soundSlider.getValue();
			this.soundSlider.setValue(0F);
			this.soundSlider.setDisabled(true);
		} else {
			/*
			 * Button ändern
			 */
			this.soundFlag = true;
			this.soundButton.setSrc("icons/soundOn.png");
			this.soundButton.setBackgroundImage("icons/soundOn.png");

			/*
			 * MusicManager stoppen
			 */
			MusicManager.getInstance().setCanPlaySounds(true);

			/*
			 * Slider anpassen
			 */
			this.soundSlider.setValue(this.sliderValue);
			this.soundSlider.setDisabled(false);
		}

		// SoundFlag im Cookie speichern
		Cookies.setCookie("nomusic", !this.soundFlag + "");
	}

	/**
	 * Setzt die Sprache des Spieles, d.h. lädt die sprachspezifischen Properties
	 * 
	 * @param language
	 *            1 = englisch, default = deutsch
	 */
	private void setLanguage(int language) {
		// Sprache laden (hier könnten dann weitere Sprachen eingefügt werden)
		switch (language) {
		case 1: // Englisch
			Page.props = GWT.create(PropertiesEn.class);
			break;
		default: // Deutsch
			Page.props = GWT.create(PropertiesDe.class);
			break;
		}
	}

	/**
	 * Wird geladen, sobald Webapplikation aufgerufen. Erstellt TabManager und fügt Startseite (FrontPage) als Tab hinzu.
	 */
	@Override
	public void onModuleLoad() {
		// Sprache laden
		try {
			this.setLanguage(Integer.valueOf(Cookies.getCookie("language")));
		} catch (Exception e) {
			this.setLanguage(0);
		}

		// Container
		this.container = new VStack();
		this.container.setStyleName("gamecontainer");
		this.container.moveTo(0, 10);

		// MainTab
		TabManager mainTabManager = TabManager.getInstanceMain();
		mainTabManager.setID("mainTabManager");
		TabManager childTabManager = TabManager.getInstanceChild();
		childTabManager.setID("childTabManager");

		this.frontPagePresenter = new FrontPagePresenter();
		childTabManager.setPixelSize(Integer.valueOf(Page.props.prototyp_childTabManager_width()),
				Integer.valueOf(Page.props.prototyp_childTabManager_height()));
		childTabManager.addTab(this.frontPagePresenter);

		Tab mainTab = new Tab(Page.props.prototyp_mainTab_text());
		mainTabManager.addTab(mainTab);
		mainTabManager.setTabPane(0, childTabManager);

		// Alles für die Musik

		this.soundFlag = true;
		this.sliderValue = 50F;
		HorizontalStack soundStack = new HorizontalStack(5, 0);
		this.soundSlider = new Slider();
		this.soundSlider.setVertical(false);
		this.soundSlider.setShowRange(false);
		this.soundSlider.setShowValue(false);
		this.soundSlider.setShowTitle(false);
		this.soundSlider.setLength(50);
		this.soundSlider.setMinValue(0F);
		this.soundSlider.setMaxValue(100F);
		this.soundSlider.setValue(this.sliderValue);
		this.soundSlider.addValueChangedHandler(new ValueChangedHandler() {
			@Override
			public void onValueChanged(ValueChangedEvent event) {
				MusicManager.getInstance().setVolume(event.getValue());
			}
		});
		this.soundButton = new ImgButton();
		this.soundButton.setSrc("icons/soundOn.png");
		this.soundButton.setBackgroundImage("icons/soundOn.png");
		this.soundButton.setWidth(17);
		this.soundButton.setHeight(13);
		this.soundButton.setCanFocus(false);
		this.soundButton.setCanHover(false);
		this.soundButton.setShowDown(false);
		this.soundButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				disableMusic(Prototyp.this.soundFlag);
			}
		});
		soundStack.addMembers(this.soundButton, this.soundSlider);
		soundStack.setLeft(920);
		soundStack.setTop(10);
		soundStack.setAutoHeight();

		// Tab/Spiel anzeigen
		this.container.addMember(mainTabManager);
		this.container.addChild(soundStack);
		this.container.draw();
		
		/*
		 * Cookies auslesen
		 */
		FrontPage frontPage = (FrontPage) this.frontPagePresenter.getPage();
		frontPage.getNameField().setValue(Cookies.getCookie("nickname"));

		// Passwort entschlüsseln
		final String decpasswd = CryptManager.getInstance().decrypt(Cookies.getCookie("password"));
		// Passwort nur anzeigen, wenn es gültig entschlüsselt wurde
		if (decpasswd != null) {
			frontPage.getPasswordField().setValue(decpasswd);
		}

		// Musik aktivieren oder deaktivieren
		if (Boolean.valueOf(Cookies.getCookie("nomusic"))) {
			disableMusic(true);
		}
	}
}