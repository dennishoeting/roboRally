package prototyp.client.presenter.lobby;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.TabManager;
import prototyp.client.view.Page;
import prototyp.client.view.lobby.LogoutPage;

import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.tab.Tab;

/**
 * LogoutPresenter.
 * 
 * @author Dennis, Marina, Timo (Verantwortlicher)
 * 
 * @version 1.1 Methode createWindow hinzugefügt, alte Listener gelöscht (03.10.10) von Marina
 * @version 1.2 Passwort-Vergessen Fenster in den View gepackt (Marina)
 */
public class LogoutPresenter implements PagePresenter {
	/** Zugehörige Page */
	private LogoutPage page;

	// private final UserServiceAsync userService = GWT.create(UserService.class);

	/**
	 * Konstruktor
	 * 
	 */
	public LogoutPresenter() {
		this.page = new LogoutPage();
		addListeners();
	}

	/**
	 * Fügt die Listerner für die Buttons der LogoutPage hinzu
	 * 
	 * @return true
	 */
	public boolean addListeners() {
		// CloseClickhandler für das x im Fenster
		this.page.getWinModal().addCloseClickHandler(new CloseClickHandler() {
			@Override
			public void onCloseClick(CloseClientEvent event) {
				LogoutPresenter.this.page.getWinModal().clear();
				TabManager.getInstanceChild().selectTab(0);
			}
		});

		// CLickhandler für den Abort-Button
		this.page.getButtonAbort().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				LogoutPresenter.this.page.getWinModal().clear();
				TabManager.getInstanceChild().selectTab(0);
			}
		});

		// Clickhandler für den Logout-Button
		this.page.getButtonLogout().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				try {
					Window.Location.reload();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		return true;
	}

	/**
	 * Liefert den childTabManager
	 * 
	 * @return
	 */
	public TabManager getChildTabManager() {
		return TabManager.getInstanceChild();
	}

	/**
	 * Liefert die Page
	 */
	@Override
	public Canvas getPage() {
		return this.page;
	}

	/**
	 * Liefert das Popup beim Ausloggen
	 * 
	 * @return true
	 */
	public boolean showPopup() {
		this.page.getWinModal().setAutoCenter(true);
		int activeRoundCount = 0;
		String logout;
		for (Tab tab : TabManager.getInstanceMain().getTabs()) {
			if (tab.getTitle().equals(Page.props.roundPlayerPage_title())) {
				activeRoundCount++;
			}
		}

		if (activeRoundCount > 0) {
			if (activeRoundCount >= 1) {
				logout = Page.props.logOutPage_confirm2_title_part1() + " " + activeRoundCount + " "
						+ Page.props.logOutPage_confirm2_title_part2() + "n " + Page.props.logOutPage_confirm2_title_part3();
			} else {
				logout = Page.props.logOutPage_confirm2_title_part1() + " " + activeRoundCount + " "
						+ Page.props.logOutPage_confirm2_title_part2() + Page.props.logOutPage_confirm2_title_part3();
			}
			this.page.getConfirmLabel().setContents(logout);
		} else {
			this.page.getConfirmLabel().setContents(Page.props.logOutPage_confirm_title());
		}

		this.page.getWinModal().show();

		return true;
	}

}
