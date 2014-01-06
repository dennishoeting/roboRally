package prototyp.client.presenter;

import prototyp.client.presenter.administration.AdministrationPagePresenter;
import prototyp.client.presenter.lobby.HighScorePagePresenter;
import prototyp.client.presenter.lobby.LogoutPresenter;
import prototyp.client.presenter.lobby.ProfilePagePresenter;
import prototyp.client.presenter.pregame.PreGamePresenter;
import prototyp.client.presenter.round.RoundPlayerPagePresenter;
import prototyp.client.presenter.round.RoundPresenterInterface;
import prototyp.client.presenter.round.RoundWatcherPagePresenter;
import prototyp.client.presenter.user.UserPresenter;
import prototyp.client.util.MusicManager;
import prototyp.client.view.Page;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

/**
 * Verwaltung der Tabs.
 * 
 * @author Dennis
 * 
 * @version 1.1 GWT (25.08.10) von Dennis
 * @version 1.2 Smart GWT (30.08.10) von Robert
 * @version 1.3 Methode closeTab() hinzugefügt (14.09.10) von Jannik
 * @version 1.4 Methode addListener hinzugefügt (23.09.10) von Andreas
 * @version 1.5 Methode switchToTab geändert (27.09.10) von Robert
 * @version 1.6 Methode addListener bearbeitet (03.10.10) von Marina
 * @version 1.7 Methode switchTab(Tab, PagePresenter)(28.12.10) Timo
 */
public class TabManager extends TabSet {
	/**
	 * Singleton-Getter (CHILD)
	 * 
	 * @return ChildTabManager
	 */
	public static TabManager getInstanceChild() {
		return TabManager.INSTANCE_CHILD;
	}

	/**
	 * Singleton-Getter (MAIN)
	 * 
	 * @return MainTabManager
	 */
	public static TabManager getInstanceMain() {
		return TabManager.INSTANCE_MAIN;
	}

	/** TabSelectedHandler */
	private TabSelectedHandler handler = null;

	/** HandlerRegistration */
	private HandlerRegistration tabSelectionHandlerRegistration;

	/** Großer TabManager */
	private static TabManager INSTANCE_MAIN = new TabManager();

	/** Kleiner TabManager (fürs Hauptmenü) */
	private static TabManager INSTANCE_CHILD = new TabManager();

	/**
	 * Konstruktor
	 */
	private TabManager() {
		setPixelSize(Integer.valueOf(Page.props.prototyp_childTabManager_width()) + 12,
				Integer.valueOf(Page.props.prototyp_childTabManager_height()) + 37);
	}

	/**
	 * Fügt dem TabManager einen FocusChangeListener hinzu. Dies wird für den ChildManager benötigt.
	 * 
	 * @param adminPresenter
	 *            Presenter der AdministrationPage
	 * @param highscorePresentern
	 *            Presenter der HighScorePage
	 * @param profilePresentern
	 *            Presenter der ProfilePage
	 * @param logoutPresenter
	 *            Presenter der LogoutPage
	 */
	public void addListener(final AdministrationPagePresenter adminPresenter, final HighScorePagePresenter highscorePresentern,
			final ProfilePagePresenter profilePresentern, final LogoutPresenter logoutPresenter) {
		this.handler = new TabSelectedHandler() {
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				if (event.getTab().getTitle().toString().equals(Page.props.administrationPage_title())) {
					// Alle User holen
					adminPresenter.loadUsersFromDatabase();
				} else if (event.getTab().getTitle().toString().equals(Page.props.highScorePage_title())) {
					// Rechte Seite Default-Einstellungen zeigen
					highscorePresentern.fillWithContent(null, "userphotos/default.png");
					// Linke Seite mit Inhalt füllen
					highscorePresentern.getUsersFromDBAndFillHighScoreGrid();
				} else if (event.getTab().getTitle().toString().equals(Page.props.profilePage_title())) {
					// Statistik, etc. laden
					profilePresentern.fillFieldsWithUserData();
				} else if (event.getTab().getTitle().toString().equals(Page.props.logOutPage_title())) {
					logoutPresenter.showPopup();
				}
			}
		};
		if (this.tabSelectionHandlerRegistration != null) {
			this.tabSelectionHandlerRegistration.removeHandler();
		}
		this.tabSelectionHandlerRegistration = addTabSelectedHandler(this.handler);
	}

	/**
	 * Erstellen eines Tabs. Hinzufügen der Page in den Tab, Titel der Page ist Titels des Tabs. Wenn Spielinstanzen aufgerufen
	 * werden wird ein neuer, schließbarer Tab dem TabPanel hinzugefügt ansonsten feste Tabs
	 * 
	 * @param pagePresenter
	 *            Einen PagePresenter
	 */
	public boolean addTab(PagePresenter pagePresenter) {
		final Tab newTab = new Tab(pagePresenter.getPage().getTitle());
		newTab.setCanClose(false); // Kann nicht mehr geschlossen werden
		newTab.setPane(pagePresenter.getPage());
		this.addTab(newTab);

		if (getID().equals("mainTabManager")) {
			this.selectTab(getNumTabs() - 1);
		}

		/*
		 * Beim PreGamePresenter den Tab speichern
		 */
		if (pagePresenter instanceof PreGamePresenter) {
			((PreGamePresenter) pagePresenter).setTab(this.getTab(getSelectedTabNumber()));
		}

		return true;
	}

	/**
	 * Schließt den aktuell angezeigten Tab (außer Hauptmenü)
	 * 
	 * @return true wenn erfolgreich, ansonsten false.
	 */
	public boolean closeTab() {
		// Musik ändern, wenn er nicht mehr in einer aktiven Spielrunde ist.
		if (!MusicManager.getInstance().getSoundfile().equals("robocraft-back-1_2.mp3")
				&& UserPresenter.getInstance().getActiveRounds().size() < 1) {
			MusicManager.getInstance().stopAndClear();
			MusicManager.getInstance().play("robocraft-back-1_2.mp3");
		}
		
		// wenn der Tab nicht das Hauptmenü ist
		if (getSelectedTabNumber() != 0) {
			removeTab(getTab(getSelectedTabNumber()));
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Wechselt einen Tab anhand seines Indexes
	 * 
	 * @param tab
	 *            neuer Tab
	 * @param pagePresenter
	 *            Zugehöriger Presenter
	 * @return true
	 */
	public boolean switchTab(Tab tab, final PagePresenter pagePresenter) {

		this.setTabPane(getTabNumber(tab.getID()), pagePresenter.getPage());
		this.getTab(getTabNumber(tab.getID())).setTitle(pagePresenter.getPage().getTitle());

		/*
		 * Beim Game bei Tab-Delesection/-Selection
		 */
		if (pagePresenter instanceof RoundPlayerPagePresenter || pagePresenter instanceof RoundWatcherPagePresenter) {
			this.addTabSelectedHandler(new TabSelectedHandler() {
				@Override
				public void onTabSelected(TabSelectedEvent event) {
					((RoundPresenterInterface) pagePresenter).resetAreaState();
				}
			});
		}

		return true;
	}

	/**
	 * Wechselt die Tabs FrontPage-Lobby, ProfilePage-ProfileEditpage, Administrator-AdministratorEditPage. Beim Logout werden
	 * zusätzlich alle Spiele(main)tabs und alle Menü(child)tabs gelöscht. Ist der User Administrator, werden 4 der 5 Tabs
	 * gelöscht, sonst 3.
	 * 
	 * @param pagePresenter
	 *            Einen PagePresenter
	 * @return true
	 */
	public boolean switchToTab(final PagePresenter pagePresenter) {
		// Ersetzen eines Tabs
		this.setTabPane(getSelectedTabNumber(), pagePresenter.getPage());
		this.getTab(getSelectedTabNumber()).setTitle(pagePresenter.getPage().getTitle());

		// Beim PreGamePresenter den Tab speichern
		if (pagePresenter instanceof PreGamePresenter) {
			((PreGamePresenter) pagePresenter).setTab(this.getTab(getSelectedTabNumber()));
		}

		/*
		 * Beim Game bei Tab-Delesection/-Selection
		 */
		if (pagePresenter instanceof RoundPlayerPagePresenter || pagePresenter instanceof RoundWatcherPagePresenter) {
			this.addTabSelectedHandler(new TabSelectedHandler() {
				@Override
				public void onTabSelected(TabSelectedEvent event) {
					((RoundPresenterInterface) pagePresenter).resetAreaState();
				}
			});
		}

		return true;
	}
}
