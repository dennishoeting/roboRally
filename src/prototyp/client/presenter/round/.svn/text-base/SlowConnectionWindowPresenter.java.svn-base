package prototyp.client.presenter.round;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.TabManager;
import prototyp.client.view.round.SlowConnectionWindow;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * Presenter, der ein Fenster anzeigt, wenn man aufgrund einer langsamen Internetverbindung aus einer laufenden Spielrunde
 * herausgeschmissen wird.
 * 
 * @author Marcus
 * @version 1.0
 * 
 * @see SlowConnectionWindow
 */
public class SlowConnectionWindowPresenter implements PagePresenter {

	/** der View des Presenters */
	private final SlowConnectionWindow page;

	/**
	 * Konstruktor
	 * 
	 * @param presenter
	 *            der Presenter, auf dessen View das SlowConnectionWindow angezeigt werden.
	 */
	public SlowConnectionWindowPresenter(RoundPlayerPagePresenter presenter) {
		this.page = new SlowConnectionWindow(presenter);
		
		// Listener
		this.addListeners();
	}

	/**
	 * Erzeugt eine Ereignisbehandlung für den View
	 * 
	 * @return true
	 */
	private boolean addListeners() {

		/*
		 * dem CloseButton wird eine Ereignisbehandlung hinzugefügt
		 */
		this.page.getButtonClose().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				/*
				 * den aktuellen Tab schließen
				 */
				TabManager.getInstanceMain().closeTab();
			}
		});

		/*
		 * immer true
		 */
		return true;
	}

	/**
	 * Liefert den View des Presenters
	 * 
	 * @return view
	 */
	@Override
	public Canvas getPage() {
		return this.page;
	}

}
