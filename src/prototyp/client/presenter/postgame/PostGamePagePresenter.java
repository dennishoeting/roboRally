package prototyp.client.presenter.postgame;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.TabManager;
import prototyp.client.presenter.chat.ChatPresenter;
import prototyp.client.presenter.user.UserPresenter;
import prototyp.client.view.postgame.PostGamePage;

import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * Presenter für die PostGamePage
 * 
 * @author Timo, Tim (Verantwortlicher), Robert (Verantwortlicher), Andreas
 * @version 1.0
 * @version 1.1 der ChatPresenter aus der Round wird übernommen und hier hinzugefügt.
 * @version 1.1 kann den Listener für das EventService löschen
 * @version 1.2 Awards werden in das ListGrid hinzugefügt
 * @version 1.3 Awards und Statistik ausgelagert und eine Überprüfung auf null hinzugefügt (Andreas)
 * @version 1.4 Alles geändert. Speicherung geschieht nun im WinnerWindow (Andreas)
 * 
 * @see PostGamePage
 */
public class PostGamePagePresenter implements PagePresenter {
	/** Chat-Presenter */
	private final ChatPresenter chatPresenter;

	/** Zugehörige Page */
	private final PostGamePage page;

	/**
	 * Konstruktor. Erstellt die PostGamePage und fügt die Listener hinzu.
	 * 
	 * @param awards
	 *            RecordList für das ListGrid mit Awards
	 * @param statistic
	 *            RecordList für das ListGrid mit Statistik
	 * @param chatPresenter
	 *            Chat-Presenter
	 */
	public PostGamePagePresenter(final RecordList awards, final RecordList statistic, final ChatPresenter chatPresenter) {
		// Neue Page
		this.page = new PostGamePage();

		// Chat aus der Round übernehmen
		this.chatPresenter = chatPresenter;
		this.chatPresenter.getPage().setHeight(180);
		this.chatPresenter.getPage().setWidth(439);
		this.page.getChatArea().addMember(chatPresenter.getPage());

		// Awards anzeigen
		this.page.getAwardsGrid().setData(awards);

		// Statistik anzeigen
		this.page.getStatisticsGrid().setData(statistic);

		// Hinzufügen der Listener
		addListeners();
	}

	/**
	 * Fügt die Listener hinzu
	 * 
	 * @return true
	 */
	private boolean addListeners() {
		// Wird ausgeführt, wenn der Button "Schließen" betätigt wird
		this.page.getButtonCloseTab().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// Hier sollten spätestens alle EventServiceListener der Round geschlossen werden

				// ChatNachricht schicken:
				PostGamePagePresenter.this.chatPresenter.sendLeavePostGameMessage(UserPresenter.getInstance().getNickname(), 
						PostGamePagePresenter.this.chatPresenter.getDomain());
				
				// Chat Service schließen:
				PostGamePagePresenter.this.chatPresenter.deleteEventListener();

				// schließt den Tab
				TabManager.getInstanceMain().closeTab();
			}
		});

		return true;
	}

	/**
	 * Liefert die Page
	 */
	@Override
	public Canvas getPage() {
		return this.page;
	}
}
