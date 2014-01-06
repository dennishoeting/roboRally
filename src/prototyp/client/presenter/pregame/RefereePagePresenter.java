package prototyp.client.presenter.pregame;

import java.util.Map;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.TabManager;
import prototyp.client.presenter.user.UserPresenter;
import prototyp.client.properties.PropertiesEn;
import prototyp.client.service.PreGameService;
import prototyp.client.service.PreGameServiceAsync;
import prototyp.client.service.RoundManagerService;
import prototyp.client.service.RoundManagerServiceAsync;
import prototyp.client.util.MapRecord;
import prototyp.client.view.Page;
import prototyp.client.view.lobby.MapPreviewWindow;
import prototyp.client.view.pregame.RefereePage;
import prototyp.shared.round.PlayingBoard;
import prototyp.shared.round.RoundInfo;
import prototyp.shared.round.RoundOption;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;

/**
 * RefereePagePresenter definiert das Verhalten der Buttons etc.
 * 
 * @author Jannik (Verantwortlicher), Andreas
 * @version 1.0
 * @version 1.1 (populateMapList() hinzugefügt --Jannik)
 * @version 1.2 (Datenbankschnittstelle eingefügt, JavaDoc hinzugefügt --Andreas)
 * @version 1.3 (Neue Methoden, JavaDoc, Runde wird erstellt --Andreas)
 * @version 1.3.1 Bugfixes bei den WatcherSlots --Jannik
 * @version 1.4 Auf das neue Konzept mit RoundInfo umgestellt -Timo 01.10.10
 * 
 * @see {@link RefereePage}
 */
public class RefereePagePresenter implements PagePresenter {
	/** Alle Spielbretter aus der Datenbank */
	private Map<Integer, PlayingBoard> allPlayingBoards = null;

	/** PlayingBoardListe für die Tabelle */
	private RecordList mapData;

	/** Page */
	private final RefereePage page;

	/** AsyncInstanz */
	private final PreGameServiceAsync pageService;

	/** RoundManager */
	private final RoundManagerServiceAsync roundManagerService;

	/** Selektiertes Spielbrett */
	private PlayingBoard selectedPlayingBoard = null;

	/**
	 * Konstruktor Erstellt den View, fügt in die Kartentabelle Inhalt ein und fügt die nötigen Listener hinzu.
	 */
	public RefereePagePresenter() {
		this.pageService = GWT.create(PreGameService.class);
		this.roundManagerService = GWT.create(RoundManagerService.class);

		// Page hinzufügen
		this.page = new RefereePage();
		loadMapsFromDatabase();

		// Listener hinzufügen
		addListeners();
	}

	/**
	 * Fügt die Listener hinzu.
	 * 
	 * @return true, falls alles geklappt hat.
	 */
	private boolean addListeners() {
		// "Weiter"
		this.page.getButtonAbort().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (RefereePagePresenter.this.selectedPlayingBoard != null) {
					// RundInfo erstellen
					final RoundInfo roundInfo = new RoundInfo(UserPresenter.getInstance().getUser(),
							RefereePagePresenter.this.selectedPlayingBoard, setOptions());
					// Free Playerslots
					roundInfo.setFreePlayerSlots(roundInfo.getRoundOption().getPlayersSlots() - 1);
					// Free Watcherslots
					roundInfo.setFreeWatcherSlots(roundInfo.getRoundOption().getWatchersSlots());

					// Runde in den RundenManager eintragen und den Tab zur
					// PreGamegameInitiatorPage wechseln.
					addToRoundManager(roundInfo);
				}
			}
		});

		// "Abbrechen"
		this.page.getButtonCloseTab().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				TabManager.getInstanceMain().closeTab();
			}
		});

		// "Beobachter erlauben" deaktiviert -> "Anzahl der Beobachterplätze"
		// deaktivieren
		this.page.getAllowObservers().addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				if (RefereePagePresenter.this.page.getAllowObservers().getValueAsBoolean()) {
					RefereePagePresenter.this.page.getWatcherSlots().setDisabled(false);
				} else if (!RefereePagePresenter.this.page.getAllowObservers().getValueAsBoolean()) {
					RefereePagePresenter.this.page.getWatcherSlots().setDisabled(true);
				}

			}
		});

		// wenn man eine Karte in der Tabelle auswählt
		this.page.getMapGrid().addSelectionChangedHandler(new SelectionChangedHandler() {
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				MapRecord data = (MapRecord) event.getSelectedRecord();
				if (data != null) {
					setPreview(data.getAttributeAsInt("playingboardid"));
					RefereePagePresenter.this.page.getButtonAbort().setDisabled(false);

				}
			}
		});

		// Vorschau vergrößern, wenn auf das Bild geklickt wird
		this.page.getPreviewArea().getPreviewMapImage().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (selectedPlayingBoard != null) {
					final MapPreviewWindow window = new MapPreviewWindow();
					window.show(page.getPreviewArea().getPreviewMapImage(), selectedPlayingBoard.getWidth(), selectedPlayingBoard.getHeight());
				}
			}
		});

		return true;
	}

	/**
	 * Fügt die übergebene Runde in den RoundManager hinzu und wechselt den Tab zur PreGamegameInitiatorPage.
	 * 
	 * @param round
	 *            Neu erstellte Runde
	 */
	private void addToRoundManager(final RoundInfo round) {
		this.roundManagerService.addRound(round, new AsyncCallback<Integer>() {
			@Override
			public void onFailure(Throwable caught) {
				// Ausgeben
				SC.say(Page.props.refereePage_title(), caught.getMessage());
			}

			@Override
			public void onSuccess(Integer result) {
				if (result != null) {
					// Tab wechseln
					round.setRoundID(result);
					// Round ID in den UserPresenter eintragen
					UserPresenter.getInstance().addRound(result);
					TabManager.getInstanceMain().switchToTab(new PreGameGameInitiatorPagePresenter(round));
				}
			}
		});
	}

	/**
	 * Liefert den View
	 * 
	 * @see prototyp.client.presenter.PagePresenter#getPage()
	 */
	@Override
	public Canvas getPage() {
		return this.page;
	}

	/**
	 * Lädt die Spielbretter aus der Datenbank und zeigt sie in der Liste an.
	 */
	private void loadMapsFromDatabase() {
		this.pageService.getPlayingBoards(new AsyncCallback<Map<Integer, PlayingBoard>>() {
			@Override
			public void onFailure(Throwable caught) {
				// Ausgeben
				SC.say(Page.props.refereePage_title(), caught.getMessage());
			}

			@Override
			public void onSuccess(Map<Integer, PlayingBoard> result) {
				if (result != null) {
					RefereePagePresenter.this.allPlayingBoards = result;
					showMapsInList(result);
				}
			}
		});
	}

	/**
	 * Erstellt ein RoundOptionobjekt und gibt es zurück. Dabei werden alle vom Nutzer gewählten Optionen eingestellt.
	 * 
	 * @return RoundOption
	 */
	private RoundOption setOptions() {
		final RoundOption options = new RoundOption();
		options.setAlwaysCompactorsOn(this.page.getAlwaysCompactorsOn().getValueAsBoolean());
		options.setAlwaysPushersOn(this.page.getAlwaysPushersOn().getValueAsBoolean());
		options.setAlwaysConveyorBeltsOn(this.page.getAlwaysCBOn().getValueAsBoolean());
		options.setAlwaysGearsOn(this.page.getAlwaysGearsOn().getValueAsBoolean());
		options.setLasersOn(this.page.getLaserOn().getValueAsBoolean());
		options.setRobotShootsOn(this.page.getRobotShootsOn().getValueAsBoolean());
		options.setReduceCountdown(this.page.getReduceCountDown().getValueAsBoolean());
		options.setCountDownTime(Integer.parseInt(this.page.getCountDownTime().getValue().toString()));
		if (this.page.getAllowObservers().getValueAsBoolean()) {
			options.setWatcherSlots(Integer.parseInt(this.page.getWatcherSlots().getValue().toString()));
		} else {
			options.setWatcherSlots(0);
		}

		options.setPlayerSlots(Integer.parseInt(this.page.getPlayerSlots().getValue().toString()));

		// Wenn nichts eingegeben wurde, dann nimm einen Standardwert beim
		// Spielrundennamen.
		if (this.page.getGameName().getValue() == null || this.page.getGameName().getValue().toString().trim().equals("")) {
			if(UserPresenter.getInstance().getNickname().toLowerCase().endsWith("s")
					|| UserPresenter.getInstance().getNickname().toLowerCase().endsWith("x")
					|| UserPresenter.getInstance().getNickname().toLowerCase().endsWith("z")) {			
				options.setGameName(UserPresenter.getInstance().getNickname() + "' " + Page.props.global_title_game());
			} else if(Page.props instanceof PropertiesEn){
				options.setGameName(UserPresenter.getInstance().getNickname() + "'s " + Page.props.global_title_game());
			} else {
				options.setGameName(UserPresenter.getInstance().getNickname() +"s " + Page.props.global_title_game());
			}
		} else {
			options.setGameName(this.page.getGameName().getValue().toString().trim());
		}

		return options;
	}

	/**
	 * Kriegt einen ListGridRecord übergeben und schreibt die Daten in das Vorschaufeld
	 * 
	 * @param playingBoardID
	 *            DatenbankID des Spielbrettes
	 * @return true, falls alles geklappt hat.
	 */
	private boolean setPreview(int playingBoardID) {
		if (!this.allPlayingBoards.containsKey(playingBoardID)) {
			return false;
		}

		// Ausgewählte Karte
		this.selectedPlayingBoard = this.allPlayingBoards.get(playingBoardID);

		// Preview verändern
		this.page.getPreviewArea().fillWithContent(this.selectedPlayingBoard);

		/*
		 *  Optionen anpassen
		 */
		this.page.getPlayerSlots().setMax(this.selectedPlayingBoard.getMaxPlayers());
		this.page.getPlayerSlots().setValue(this.selectedPlayingBoard.getMaxPlayers());
		
		// Checkboxen ändern
		this.page.getLaserOn().setValue(this.selectedPlayingBoard.getNumberOfLaserCannonFields() > 0);
		this.page.getAlwaysCBOn().setValue(this.selectedPlayingBoard.getNumberOfConveyorBeltFields() > 0);
		this.page.getAlwaysCompactorsOn().setValue(this.selectedPlayingBoard.getNumberOfCompactorFields() > 0);
		this.page.getAlwaysPushersOn().setValue(this.selectedPlayingBoard.getNumberOfPusherFields() > 0);
		this.page.getAlwaysGearsOn().setValue(this.selectedPlayingBoard.getNumberOfGearFields() > 0);

		this.page.getLaserOn().setDisabled(this.selectedPlayingBoard.getNumberOfLaserCannonFields() == 0);
		this.page.getAlwaysCBOn().setDisabled(this.selectedPlayingBoard.getNumberOfConveyorBeltFields() == 0);
		this.page.getAlwaysCompactorsOn().setDisabled(this.selectedPlayingBoard.getNumberOfCompactorFields() == 0);
		this.page.getAlwaysPushersOn().setDisabled(this.selectedPlayingBoard.getNumberOfPusherFields() == 0);
		this.page.getAlwaysGearsOn().setDisabled(this.selectedPlayingBoard.getNumberOfGearFields() == 0);
		
		return true;
	}

	/**
	 * Zeigt die PlayingBoards in der Tabelle an.
	 * 
	 * @param mapList
	 *            List mit PlayingBoardobjekten
	 * @return true, falls alles geklappt hat.
	 */
	private boolean showMapsInList(Map<Integer, PlayingBoard> mapList) {
		if (mapList == null) {
			return false;
		}
		this.mapData = new RecordList(); // Alte löschen
		for (Integer key : this.allPlayingBoards.keySet()) {
			PlayingBoard playingboard = this.allPlayingBoards.get(key);
			this.mapData.add(new MapRecord(playingboard));
		}
		this.page.getMapGrid().setData(this.mapData); // Anzeigen

		return true;
	}
}
