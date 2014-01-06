package prototyp.client.presenter.pregame;

import prototyp.client.presenter.TabManager;
import prototyp.client.presenter.chat.ChatPresenter;
import prototyp.client.presenter.user.UserPresenter;
import prototyp.client.view.Page;
import prototyp.client.view.chat.ChatArea;
import prototyp.client.view.lobby.MapPreviewWindow;
import prototyp.client.view.pregame.PreGameGameInitiatorPage;
import prototyp.shared.round.Player;
import prototyp.shared.round.RoundInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;

/**
 * Erbt von PreGamePagePresenter (abstrakt) Hier hat der GI die Möglichkeit Player/Watcher zu kicken, das Spiel zu Starten, das
 * Spiel doch noch abzubrechen und natürlich kann er seine Farbe auswählen.
 * 
 * @author Jannik (Verantwortlicher), Dennis, Timo (Verantwortlicher)
 * @version 1.0 Grundgerüst von Denise
 * @version 1.1 Kick-your-Balls-Version von Jannik
 * @version 1.2 Andere Player/Watcher können jetzt gekickt werden -Timo 26.10.10
 * @version 1.3 Vor dem Starten wird mitgeteilt 1) Falls noch freie PlayerSlots da sind 2) Falls nicht alle auf bereit sind. ->
 *          Starten kann man das Spiel trotzdem! -Timo 26.10.10
 * 
 * @see PreGameGameInitiatorPage
 */
public class PreGameGameInitiatorPagePresenter extends PreGamePresenter {

	/**
	 * Hier wird die Selection des Menü Grid zwischen gespeichert, falls diese vor dem Button klicken aus irgendeinem Grund wieder
	 * null ist. Sonst müsste man zur Sicherheit zwei mal Abfragen
	 */
	private Record selectedRecordMenueGrid;

	/**
	 * Konstruktor
	 * 
	 * @param round
	 *            RoundInfo der ausgewählten Runde
	 */
	public PreGameGameInitiatorPagePresenter(RoundInfo round) {
		// SuperKonstruktor
		super(round);

		// Sich selbst eintragen/ready setzen
		Player gameInitiator = new Player(UserPresenter.getInstance().getUser());
		gameInitiator.setReady(true);
		super.allPlayers.put(UserPresenter.getInstance().getUser().getId(), gameInitiator);

		this.playerWatcherOrGameinitiator = 0;

		this.page = new PreGameGameInitiatorPage();

		// Internen Chat hinzufügen
		this.chatPresenter = new ChatPresenter("internalChatRound:" + round.getRoundId());
		((ChatArea) this.chatPresenter.getPage()).setWidth(338);
		((ChatArea) this.chatPresenter.getPage()).setHeight(182);
		this.page.getInternalChatArea().addMember(this.chatPresenter.getPage());

		// Bevor das erste Round Event kommt müssen diese Sachen "per Hand"
		// ausgeführt werden.
		super.setPreview();
		super.showPlayersAndWatchersInList();
		super.setAvailableColors();

		// Kicken Button disable setzen (da noch keine Auswahl getroffen wurde)
		((PreGameGameInitiatorPage) this.page).getButtonKickPlayer().disable();

		// Listener Hinzufügen
		super.setColorChooseListener();
		super.addRoundListener();
		addListeners();
	}

	/**
	 * Fügt die Listener hinzu
	 * 
	 * @return
	 */
	@Override
	protected boolean addListeners() {

		// Vorschau vergrößern, wenn auf das Bild geklickt wird
		this.page.getPreviewArea().getPreviewMapImage().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final MapPreviewWindow window = new MapPreviewWindow();
				window.show(page.getPreviewArea().getPreviewMapImage(), round.getPlayingboard().getWidth(), round
						.getPlayingboard().getHeight());
			}
		});

		// Start-Button
		((PreGameGameInitiatorPage) this.page).getButtonStart().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				// Abfragen, ob überhaupt mehr Player als der GI dabei
				// sind
				if (PreGameGameInitiatorPagePresenter.this.allPlayers.size() <= 1) {

					SC.say(Page.props.global_title_attention(), Page.props.preGameGameInitiatorPage_start_noPlayerToStart());

				} else { // Mehr als ein Player

					// Abfragen ob alle Playerslots belegt sind
					if (PreGameGameInitiatorPagePresenter.this.allPlayers.size() < PreGameGameInitiatorPagePresenter.this.round
							.getRoundOption().getPlayersSlots()) {
						SC.ask(Page.props.global_title_attention(),
								Page.props.preGameGameInitiatorPage_start_morePlayerPossible(), new BooleanCallback() {
									@Override
									public void execute(Boolean value) {
										if (value != null && value) {
											/*
											 * Abfragen ob alle Spieler ready sind und gegebenenfalls roundStart() aufrufen.
											 */
											PreGameGameInitiatorPagePresenter.this.allPlayersReadyAskAndStartRound();
										}
									}
								});
					} else {
						/*
						 * Wenn alle PlayerSlots belegt sind abfragen ob alle Spieler ready sind und gegebenenfalls roundStart()
						 * aufrufen.
						 */
						PreGameGameInitiatorPagePresenter.this.allPlayersReadyAskAndStartRound();
					}
				}
			}
		});

		// "Abbrechen"
		this.page.getButtonCloseTab().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				SC.ask(Page.props.preGameGameInitiatorPagePresenter_cancelRoundAsk(), new BooleanCallback() {
					@Override
					public void execute(Boolean value) {
						if (value != null && value) {

							// RoundID aus dem UserPresenter austragen
							UserPresenter.getInstance().deleteRound(PreGameGameInitiatorPagePresenter.this.round.getRoundId());
							// Listener für den Chat löschen
							PreGameGameInitiatorPagePresenter.this.chatPresenter.deleteEventListener();
							// EventListener löschen
							PreGameGameInitiatorPagePresenter.this.theEventService
									.removeListeners(PreGameGameInitiatorPagePresenter.this.DOMAIN);
							// Tab schließen
							TabManager.getInstanceMain().closeTab();

							PreGameGameInitiatorPagePresenter.this.roundManagerService.removeRound(
									PreGameGameInitiatorPagePresenter.this.round.getRoundId(), new AsyncCallback<Boolean>() {
										@Override
										public void onFailure(Throwable caught) {
											// Ausgeben
											SC.say(Page.props.preGameGameInitiatorPage_title(), caught.getMessage());
										}

										@Override
										public void onSuccess(Boolean result) {
											// Nichts
										}

									});
						} else {
							// "nein" -> es passiert nichts
						}
					}
				});

			}
		});

		/*
		 * MenueListGrid Single Selection Im ListGrid können Player/Watcher markiert werden, die gekickt werden sollen. Falls der
		 * GI sich selbst auswählt, wird der Kicken-Button disabled!
		 */
		((PreGameGameInitiatorPage) this.page).getMenueGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

			@Override
			public void onSelectionChanged(SelectionEvent event) {
				// Einen Player/Watcher ausgewählt && nicht sich selbst.
				if (((PreGameGameInitiatorPage) PreGameGameInitiatorPagePresenter.this.page).getMenueGrid().getSelectedRecord() != null
						&& ((PreGameGameInitiatorPage) PreGameGameInitiatorPagePresenter.this.page).getMenueGrid()
								.getSelectedRecord().getAttributeAsInt("userID") != UserPresenter.getInstance().getUser().getId()) {

					PreGameGameInitiatorPagePresenter.this.selectedRecordMenueGrid = ((PreGameGameInitiatorPage) PreGameGameInitiatorPagePresenter.this.page)
							.getMenueGrid().getSelectedRecord();
					((PreGameGameInitiatorPage) PreGameGameInitiatorPagePresenter.this.page).getButtonKickPlayer().enable();

				} else { // Keine Auswahl oder sich selbst ausgewählt.
					PreGameGameInitiatorPagePresenter.this.selectedRecordMenueGrid = null;
					((PreGameGameInitiatorPage) PreGameGameInitiatorPagePresenter.this.page).getButtonKickPlayer().disable();
				}
			}

		});

		// Remove Player/Watcher Button (kicken)
		((PreGameGameInitiatorPage) this.page).getButtonKickPlayer().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				// Falls der User ein Player ist -> removePlayer:
				if (PreGameGameInitiatorPagePresenter.this.selectedRecordMenueGrid.getAttributeAsString("observer").equals(
						Page.props.playerRecord_isPlayer())) {

					if (PreGameGameInitiatorPagePresenter.this.allPlayers
							.get(PreGameGameInitiatorPagePresenter.this.selectedRecordMenueGrid.getAttributeAsInt("userID")) != null) {
						PreGameGameInitiatorPagePresenter.this.roundManagerService.removePlayer(
								PreGameGameInitiatorPagePresenter.this.allPlayers
										.get(PreGameGameInitiatorPagePresenter.this.selectedRecordMenueGrid
												.getAttributeAsInt("userID")), PreGameGameInitiatorPagePresenter.this.round
										.getRoundId(), new AsyncCallback<Boolean>() {

									@Override
									public void onFailure(Throwable caught) {
										// Ausgeben
										SC.say(Page.props.preGameGameInitiatorPage_title(), caught.getMessage());
									}

									@Override
									public void onSuccess(Boolean result) {
										// Auswahl zurück setzen
										PreGameGameInitiatorPagePresenter.this.selectedRecordMenueGrid = null;
										((PreGameGameInitiatorPage) PreGameGameInitiatorPagePresenter.this.page)
												.getButtonKickPlayer().disable();
									}

								});

					}

				} else { // Falls der User ein Watcher ist ->
							// removeWatcher

					if (PreGameGameInitiatorPagePresenter.this.allWatchers
							.get(PreGameGameInitiatorPagePresenter.this.selectedRecordMenueGrid.getAttributeAsInt("userID")) != null) {
						PreGameGameInitiatorPagePresenter.this.roundManagerService.removeWatcher(
								PreGameGameInitiatorPagePresenter.this.allWatchers
										.get(PreGameGameInitiatorPagePresenter.this.selectedRecordMenueGrid
												.getAttributeAsInt("userID")), PreGameGameInitiatorPagePresenter.this.round
										.getRoundId(), new AsyncCallback<Boolean>() {

									@Override
									public void onFailure(Throwable caught) {
										// Ausgeben
										SC.say(Page.props.preGameGameInitiatorPage_title(), caught.getMessage());
									}

									@Override
									public void onSuccess(Boolean result) {
										// Auswahl zurück setzen
										PreGameGameInitiatorPagePresenter.this.selectedRecordMenueGrid = null;
										((PreGameGameInitiatorPage) PreGameGameInitiatorPagePresenter.this.page)
												.getButtonKickPlayer().disable();
									}

								});
					}

				}
			}

		});

		return true;
	}

	/**
	 * Zählt ob alle Spieler ready sind. Falls ja -> startRound Falls nein -> Dialog ob trotzdem angefangen werden soll, falls
	 * hier mit ja geantwortet wird -> startRound()
	 * 
	 * @return
	 */
	private boolean allPlayersReadyAskAndStartRound() {

		// Spieler, mit isReady == true zählen
		int readyCounter = 0;
		for (Integer key : this.allPlayers.keySet()) {
			if (this.allPlayers.get(key).isReady()) {
				readyCounter++;
			}
		}
		// Alle Spieler sind ready
		if (readyCounter >= this.allPlayers.size()) {
			startRound();
		} else {
			// Nicht alle Spieler sind ready (nachfragen und gegebenenfalls
			// trotzdem starten)

			SC.ask(Page.props.global_title_attention(), Page.props.preGameGameInitiatorPage_start_notAllReady(),
					new BooleanCallback() {
						@Override
						public void execute(Boolean value) {
							if (value != null && value) {
								PreGameGameInitiatorPagePresenter.this.startRound();
							}
						}
					});
		}

		return true;
	}

	/**
	 * Startet eine Round
	 * 
	 * @author timo
	 * @version 1.1 Abbruch wird auch deaktiviert
	 * @return true
	 */
	private boolean startRound() {
		// Start-Button disablen
		((PreGameGameInitiatorPage) this.page).getButtonStart().disable();

		// Abbruch deaktivieren
		this.page.getButtonCloseTab().disable();

		// Round starten
		PreGameGameInitiatorPagePresenter.this.roundManagerService.startCountDown(
				PreGameGameInitiatorPagePresenter.this.round.getRoundId(), new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						// Abbruch aktivieren
						page.getButtonCloseTab().enable();

						// Start-Button disablen
						((PreGameGameInitiatorPage) page).getButtonStart().enable();

						// Ausgeben
						SC.say(Page.props.preGameGameInitiatorPage_title(), caught.getMessage());
					}

					@Override
					public void onSuccess(Boolean result) {
						/*
						 * Hier passiert nichts (Sex Machine)
						 */
					}
				});

		return true;
	}

}
