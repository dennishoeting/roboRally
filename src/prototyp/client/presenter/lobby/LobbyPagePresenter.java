package prototyp.client.presenter.lobby;

import java.util.HashMap;
import java.util.Map;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.TabManager;
import prototyp.client.presenter.chat.ChatPresenter;
import prototyp.client.presenter.pregame.PreGamePagePresenter;
import prototyp.client.presenter.pregame.RefereePagePresenter;
import prototyp.client.presenter.user.UserPresenter;
import prototyp.client.service.LobbyService;
import prototyp.client.service.LobbyServiceAsync;
import prototyp.client.service.RoundManagerService;
import prototyp.client.service.RoundManagerServiceAsync;
import prototyp.client.util.GameRecord;
import prototyp.client.util.OnlineUserRecord;
import prototyp.client.view.Page;
import prototyp.client.view.chat.ChatArea;
import prototyp.client.view.lobby.LobbyPage;
import prototyp.client.view.lobby.MapPreviewWindow;
import prototyp.server.model.RoundManager;
import prototyp.shared.exception.round.NoRoundsCreatedException;
import prototyp.shared.round.RoundInfo;
import prototyp.shared.util.events.lobby.LobbyEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;

/**
 * Presenter der LobbyPage
 * 
 * @author Andreas (Verantwortlicher), Timo
 * @version 1.0
 * @version 1.1 Observer-Pattern für die Rounds -Timo
 * @version 1.2 Keine unnötigen Serveranfragen mehr
 * @version 1.3 Exceptions eingefügt --Andreas
 * 
 * @see {@link LobbyPage}
 */
public class LobbyPagePresenter implements PagePresenter {

	/** Domain für das Observer-Pattern bei der Round */
	private static final Domain DOMAIN = DomainFactory.getDomain("lobby-domain");

	/** Zugehörige Page */
	private final LobbyPage page;

	/** RoundManager */
	private final RoundManagerServiceAsync roundManagerService;

	/** LobbyService */
	private final LobbyServiceAsync lobbyService;

	/** Selektierte Runde */
	private RoundInfo selectedRound = null;

	/** EventService */
	private final RemoteEventServiceFactory roundEventServiceFactory;

	/** EventService für die aktuellen Runden */
	private final RemoteEventService roundEventService;

	/** Enthält Infos über alle aktuellen Rounds */
	private Map<Integer, RoundInfo> roundInfos;

	/** Enthält alle Nicknames, die Online sind */
	private Map<Integer, String> onlineUsers;

	/** (globaler) ChatPresenter */
	private final ChatPresenter chatPresenter;

	/**
	 * Konstruktor
	 * 
	 * @param tabManager
	 *            TabManager
	 */
	public LobbyPagePresenter() {
		this.roundManagerService = GWT.create(RoundManagerService.class);
		this.lobbyService = GWT.create(LobbyService.class);

		this.onlineUsers = new HashMap<Integer, String>();

		// Page anzeigen und mit Daten füllen
		this.page = new LobbyPage();
		getRoundsFromRoundManager();

		// EventService um die Round neu zu laden
		this.roundEventServiceFactory = RemoteEventServiceFactory.getInstance();
		this.roundEventService = this.roundEventServiceFactory.getRemoteEventService();

		// Globalen Chat hinzufügen
		this.chatPresenter = new ChatPresenter("global-chat");
		this.page.getGlobalChatArea().addMember(this.chatPresenter.getPage());
		this.chatPresenter.getPage().setWidth(407);

		// Online User ListGrid hinzufügen
		this.page.getGlobalChatArea().addMember(this.page.getOnlineUserListGrid());

		// Listener hinzufügen
		addListeners();

		// Alle Online User ins ListGrid setzen
		this.lobbyService.getOnlineUsers(new AsyncCallback<Map<Integer, String>>() {

			@Override
			public void onFailure(Throwable caught) {
				// Gibt keine eigenen Exceptions
			}

			@Override
			public void onSuccess(Map<Integer, String> result) {
				if (result != null) {
					// Speichern
					LobbyPagePresenter.this.onlineUsers = result;
					// Zeichnen
					showOnlineUsersInGrid();

				}
			}

		});
	}

	/**
	 * Fügt die Listener hinzu
	 */
	private boolean addListeners() {
		// Spielrunde erstellen
		this.page.getButtonCreateRound().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				TabManager.getInstanceMain().addTab(new RefereePagePresenter());
			}
		});

		// Spielrunde betreten
		this.page.getButtonJoinRound().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				RoundInfo round = LobbyPagePresenter.this.selectedRound;

				// Überprüfen, ob man Beitreten darf
				if (round != null) {
					TabManager.getInstanceMain().addTab(new PreGamePagePresenter(round, 1));
				} else {
					SC.say(Page.props.lobbyPage_joinRound_title(), Page.props.lobbyPage_joinRound_noEntry());
				}
			}
		});

		// Spielrunde beobachten
		this.page.getButtonWatchRound().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				RoundInfo round = LobbyPagePresenter.this.selectedRound;

				// Überprüfen, ob man Beitreten darf
				if (round != null) {
					TabManager.getInstanceMain().addTab(new PreGamePagePresenter(round, 2));
				} else {
					SC.say(Page.props.lobbyPage_joinRound_title(), Page.props.lobbyPage_joinRound_noEntry());
				}
			}
		});

		// Wenn man eine Runde auswählt
		this.page.getGameListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				GameRecord data = (GameRecord) event.getSelectedRecord();
				if (data != null) {
					try {
						setSelectedRound(data.getRound());
					} catch (IllegalArgumentException e) {
						SC.say(Page.props.lobbyPage_title(), e.getMessage());
					}
				}
			}
		});

		/*
		 * Kümmert sich um alle Events in der Lobby. Was genau ausgeführt wird, ist im jeweiligen Event festgelegt. z.B. neue
		 * Runde hinzufügen, Runde löschen, Spieler hinzufügen..
		 */
		this.roundEventService.addListener(LobbyPagePresenter.DOMAIN, new RemoteEventListener() {
			@Override
			public void apply(Event anEvent) {
				if (anEvent instanceof LobbyEvent) {
					// Ausführung des Events
					((LobbyEvent) anEvent).apply(LobbyPagePresenter.this);

					// Danach das ListGrid neuzeichnen, da es immer eine
					// Änderung gibt.
					showRoundsInList(LobbyPagePresenter.this.roundInfos);

					// Join Button disablen/enablen
					if (LobbyPagePresenter.this.selectedRound != null) {
						if (UserPresenter.getInstance().getActiveRounds()
								.contains(LobbyPagePresenter.this.selectedRound.getRoundId())) {
							// Buttons disablen
							(LobbyPagePresenter.this.page).getButtonJoinRound().disable();
							(LobbyPagePresenter.this.page).getButtonWatchRound().disable();
						} else {
							// Buttons enablen
							(LobbyPagePresenter.this.page).getButtonJoinRound().enable();
							(LobbyPagePresenter.this.page).getButtonWatchRound().enable();
						}
					}
				}
			}
		});

		// ChangeHandler für die OnlineUserListe um flüster Nachtrichten im Chat
		// zu verfassen
		this.page.getOnlineUserListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

			@Override
			public void onSelectionChanged(SelectionEvent event) {
				if (LobbyPagePresenter.this.page.getOnlineUserListGrid().getSelectedRecord() != null
						&& !LobbyPagePresenter.this.page.getOnlineUserListGrid().getSelectedRecord().equals("")) {
					// @Nickname in das Input Feld des Chats schreiben
					((ChatArea) LobbyPagePresenter.this.chatPresenter.getPage()).getMessageField().setValue(
							"@"
									+ LobbyPagePresenter.this.page.getOnlineUserListGrid().getSelectedRecord()
											.getAttribute("nickname") + " ");
					// Selection zurücksetzen
					LobbyPagePresenter.this.page.getOnlineUserListGrid().deselectAllRecords();

					// Focus setzen
					((ChatArea) LobbyPagePresenter.this.chatPresenter.getPage()).getMessageField().focusInItem();
				}
			}

		});

		// Doppelklick auf die Karte
		this.page.getGameListGrid().addCellDoubleClickHandler(new CellDoubleClickHandler() {

			@Override
			public void onCellDoubleClick(CellDoubleClickEvent event) {
				// Nur wenn etwas ausgewählt wurde und Round nicht voll
				// ist
				if (LobbyPagePresenter.this.page.getGameListGrid().getSelectedRecord() != null
						&& ((GameRecord) LobbyPagePresenter.this.page.getGameListGrid().getSelectedRecord()).getRound()
								.getFreePlayerSlots() > 0
						&& !((GameRecord) LobbyPagePresenter.this.page.getGameListGrid().getSelectedRecord()).getRound()
								.isStarted()) {
					// Dann als Player beitreten
					TabManager.getInstanceMain().addTab(
							new PreGamePagePresenter(((GameRecord) LobbyPagePresenter.this.page.getGameListGrid()
									.getSelectedRecord()).getRound(), 1));
				}
			}

		});

		// Vorschau vergrößern, wenn auf das Bild geklickt wird
		this.page.getPreviewArea().getPreviewMapImage().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (selectedRound != null) {
					final MapPreviewWindow window = new MapPreviewWindow();
					window.show(page.getPreviewArea().getPreviewMapImage(), selectedRound.getPlayingboard().getWidth(),
							selectedRound.getPlayingboard().getHeight());
				}
			}
		});

		return true;
	}

	/**
	 * Gibt alle User zurück die Online sind (Laut dieser Map)
	 * 
	 * @return Map<userID, Nickname>
	 */
	public Map<Integer, String> getOnlineUsers() {
		return this.onlineUsers;
	}

	/**
	 * Liefert die Page
	 */
	@Override
	public Canvas getPage() {
		return this.page;
	}

	/**
	 * Gibt die Map mit allen aktuellen RoundInfos zurück. Wird von den LobbyEvents verwendet!
	 * 
	 * @return roundInfos
	 */
	public Map<Integer, RoundInfo> getRoundInfos() {
		return this.roundInfos;
	}

	/**
	 * Füllt die Tabelle mit Runden aus dem RoundManager
	 * 
	 * @see {@link RoundManager#getRounds()}
	 */
	private void getRoundsFromRoundManager() {
		this.roundManagerService.getRounds(new AsyncCallback<Map<Integer, RoundInfo>>() {
			@Override
			public void onFailure(Throwable caught) {
				try {
					throw caught;
				} catch (NoRoundsCreatedException e) {
					// Nichts
				} catch (Throwable e) {
					e.printStackTrace();
				} finally {
					LobbyPagePresenter.this.roundInfos = new HashMap<Integer, RoundInfo>();
				}
			}

			@Override
			public void onSuccess(Map<Integer, RoundInfo> result) {
				try {
					// RoundInfos speichern
					if (result != null) {
						LobbyPagePresenter.this.roundInfos = result;
					} else {
						LobbyPagePresenter.this.roundInfos = new HashMap<Integer, RoundInfo>();
					}

					// Anzeigen
					showRoundsInList(result);
				} catch (IllegalArgumentException e) {
					// ListGrid reseten
					LobbyPagePresenter.this.page.getGameListGrid().setData(new RecordList());
				}
			}
		});
	}

	/**
	 * Wenn ein Benutzer aus der Liste mit den erstellten Runden eine Runde auswählt, dann schreibt diese Methode alle
	 * Informationen in die PreviewArea.
	 * 
	 * @author Andreas, Jannik
	 * @param round
	 *            Ausgewählte Runde
	 * @return true, falls alles geklappt hat
	 * 
	 * @throws IllegalArgumentException
	 *             Wird geworfen, wenn die übergebene Runde null ist.
	 * 
	 * @see {@link LobbyPagePresenter#setPreview}
	 * @see {@link PreGamePagePresenter#setPreview}
	 * @see {@link RefereePagePresenter#setPreview}
	 */
	private boolean setPreview(RoundInfo round) throws IllegalArgumentException {
		if (round == null) {
			throw new IllegalArgumentException(Page.props.lobbyPage_getRoundException());
		}

		// Preview verändern
		LobbyPagePresenter.this.page.getPreviewArea().fillWithContent(round);

		return true;
	}

	/**
	 * Speichert die ausgewählte Runde, zeigt die Vorschau an und überprüft, ob man der Runde beitreten darf
	 * 
	 * @param round
	 *            Ausgewählte Runde
	 * @return true, falls alles geklappt hat
	 * @version 1.1 wirf keine Exception mehr, weil ich null übergeben muss. Außerdem public jetzt -timo
	 */
	public boolean setSelectedRound(RoundInfo round) {
		// Ausgewählte Runde speichern
		LobbyPagePresenter.this.selectedRound = round;

		// Neue Preview setzen
		if (this.selectedRound != null) {
			try {
				setPreview(round);
			} catch (IllegalArgumentException e) {
				SC.say(Page.props.lobbyPage_title(), e.getMessage());
			}

			// Darf man als Spieler joinen?
			if (round.getFreePlayerSlots() > 0 && !round.isStarted()
					&& !UserPresenter.getInstance().getActiveRounds().contains(round.getRoundId())) {
				LobbyPagePresenter.this.page.getButtonJoinRound().setDisabled(false);
			} else {
				LobbyPagePresenter.this.page.getButtonJoinRound().setDisabled(true);
			}

			// Darf man als Beobachter joinen?
			if (round.getFreeWatcherSlots() > 0 && !round.isStarted()
					&& !UserPresenter.getInstance().getActiveRounds().contains(round.getRoundId())) {
				LobbyPagePresenter.this.page.getButtonWatchRound().setDisabled(false);
			} else {
				LobbyPagePresenter.this.page.getButtonWatchRound().setDisabled(true);
			}
		}

		return true;
	}

	/**
	 * Gibt die selektierte Round zurück
	 * 
	 * @return
	 */
	public RoundInfo getSelectedRound() {
		return selectedRound;
	}

	/**
	 * Zeigt die OnlineUser im ListGrid an.
	 * 
	 * @author Timo
	 * @version 1.0
	 * @return true, immer
	 */
	public boolean showOnlineUsersInGrid() {
		// Erstellt eine RecordList mit Nicknames
		RecordList recordList = new RecordList();
		for (Integer key : this.onlineUsers.keySet()) {
			recordList.add(new OnlineUserRecord(key, this.onlineUsers.get(key)));
		}

		// Daten setzen
		this.page.getOnlineUserListGrid().setData(recordList);

		return true;
	}

	/**
	 * Zeigt die Spielrunden aus dem RoundManager in der Tabelle mit den erstellen Spielrunden an.
	 * 
	 * @param roundList
	 *            List mit Rounds von dem RoundManager
	 * @return true, falls alles geklappt hat.
	 * 
	 * @throws IllegalArgumentException
	 *             Wird geworfen, wenn die Liste mit Runden null ist.
	 */
	private boolean showRoundsInList(Map<Integer, RoundInfo> roundList) throws IllegalArgumentException {
		if (roundList == null) {
			throw new IllegalArgumentException(Page.props.lobbyPage_getRoundsException());
		}

		// Erstellt eine RecordList mit Runden
		final RecordList roundData = new RecordList();
		for (final RoundInfo round : roundList.values()) {
			roundData.add(new GameRecord(round));
		}

		// Anzeigen
		this.page.getGameListGrid().setData(roundData);

		return true;
	}
}
