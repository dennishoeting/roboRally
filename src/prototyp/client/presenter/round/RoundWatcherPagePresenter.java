package prototyp.client.presenter.round;

import java.util.HashMap;
import java.util.Map;

import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.shape.Circle;

import prototyp.client.presenter.TabManager;
import prototyp.client.presenter.chat.ChatPresenter;
import prototyp.client.presenter.user.UserPresenter;
import prototyp.client.service.RoundManagerService;
import prototyp.client.service.RoundManagerServiceAsync;
import prototyp.client.util.MusicManager;
import prototyp.client.util.OthersReadyRecord;
import prototyp.client.util.OthersStateRecord;
import prototyp.client.util.SoundManager;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.round.RoundPage;
import prototyp.client.view.round.RoundPlayerPage;
import prototyp.client.view.round.RoundWatcherPage;
import prototyp.shared.animation.Animate;
import prototyp.shared.round.Player;
import prototyp.shared.round.Robot;
import prototyp.shared.round.RoundInfo;
import prototyp.shared.round.RoundNeedsWrapper;
import prototyp.shared.util.DrawingInfo;
import prototyp.shared.util.events.round.InternalRoundEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.MouseDownEvent;
import com.smartgwt.client.widgets.events.MouseDownHandler;
import com.smartgwt.client.widgets.events.MouseMoveEvent;
import com.smartgwt.client.widgets.events.MouseMoveHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseUpEvent;
import com.smartgwt.client.widgets.events.MouseUpHandler;
import com.smartgwt.client.widgets.events.ValueChangedEvent;
import com.smartgwt.client.widgets.events.ValueChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.layout.SectionStack;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;

/**
 * RoundPagePresenter für den Watcher
 * 
 * @author Marcus (Verantwortlicher), Dennis, Timo
 * @version 1.1
 */
public class RoundWatcherPagePresenter implements RoundPresenterInterface {
	/** Zugehörige Page */
	private RoundWatcherPage page;

	/** der Presenter für den Chat */
	private final ChatPresenter chatPresenter;

	/** der LogicManager */
	private final WatcherManager watcherManager;

	/** die Image-HashMap für die Roboter */
	private final Map<Robot, Image> robotsImageList = new HashMap<Robot, Image>();

	/** das ImageListArray für die Felder */
	private final Map<String, Image>[][] fieldsImageList;

	/** die jeweilige Spielrunde */
	private final RoundInfo roundInfo;

	/** Domain für das Observer-Pattern der Round */
	protected final Domain DOMAIN;

	/** Gwt-EventService */
	protected final RemoteEventService theEventService;

	/** Gwt-EventService-Fabrik */
	protected final RemoteEventServiceFactory theEventServiceFactory;
	
	/** das RPC */
	private RoundManagerServiceAsync roundService = GWT.create(RoundManagerService.class);

	/** AREA-STATES */
	public static final int WAITING = 0, ACTIVE_ROUND = 1;

	/** */
	private final Map<Integer, OthersReadyRecord> othersReadyRecords = new HashMap<Integer, OthersReadyRecord>();

	/** */
	private final Map<Integer, OthersStateRecord> othersStateRecords = new HashMap<Integer, OthersStateRecord>();

	/** SoundManager für die Soundeffekte */
	public static final SoundManager SOUND_MANAGER = new SoundManager();

	public int currentState = -1;
	
	/* Fürs Scrollen */
	private int startScrolledX = -1;
	private int currentScrolledX = -1;
	private int currentScrolledY = -1;
	private int oldScrolledX = -1;
	private int oldScrolledY = -1;

	/**
	 * Konstruktor
	 * 
	 * @param chatPresenter
	 *            Chat-Presenter
	 * @param roundInfo
	 *            RoundInfo der ausgewählten Runde
	 */
	@SuppressWarnings("unchecked")
	public RoundWatcherPagePresenter(ChatPresenter chatPresenter, RoundInfo roundInfo, RoundNeedsWrapper roundNeedsWrapper) {
		/*
		 * Runde zuweisen
		 */
		this.roundInfo = roundInfo;

		/*
		 * Page erstellen
		 */
		this.page = new RoundWatcherPage(this);

		/*
		 * den LogicManager erstellen
		 */
		// wird so nicht funzen
		this.watcherManager = new WatcherManager(this);
		this.watcherManager.setRoundOption(roundNeedsWrapper.getRoundOption());
		this.watcherManager.setPlayingBoard(roundNeedsWrapper.getPlayingBoard());
		this.watcherManager.setRobots(roundNeedsWrapper.getRobots());
		this.watcherManager.createBackupFieldMap();

		/*
		 * Chat aus der PreGamePage übenehmen und reinpacken
		 */
		this.chatPresenter = chatPresenter;
		this.page.getWatcherStatusArea().setChatPresenter(chatPresenter);
		this.page.getDrawingArea().setHeight(roundNeedsWrapper.getPlayingBoard().getHeight() * 50);
		this.page.getDrawingArea().setWidth(roundNeedsWrapper.getPlayingBoard().getWidth() * 50);
		/*
		 * Spielfeld zeichnen
		 */
		this.fieldsImageList = new HashMap[this.watcherManager.getPlayingBoard().getHeight()][this.watcherManager
				.getPlayingBoard().getWidth()];
		for (int i = 0; i < this.watcherManager.getPlayingBoard().getHeight(); i++) {
			for (int j = 0; j < this.watcherManager.getPlayingBoard().getWidth(); j++) {
				this.fieldsImageList[i][j] = new HashMap<String, Image>();
				for (DrawingInfo info : this.watcherManager.getPlayingBoard().getFields()[i][j].getImagePathList()) {
					Image img = new Image(j * 50 + info.getX(), i * 50 + info.getY(), info.getWidth(), info.getHeight(),
							info.getImageFileName());
					this.page.getDrawingArea().add(img);

					this.fieldsImageList[i][j].put(info.getImageFileName(), img);
				}
			}
		}

		/*
		 * Roboter putten
		 */
		for (Robot robot : this.watcherManager.getRobots().values()) {
			Image image = new Image(robot.getJ() * 50, robot.getI() * 50, 50, 50, robot.getImagePath());
			this.page.getDrawingArea().add(image);
			this.robotsImageList.put(robot, image);
		}
		
		/*
		 * Die DrawingArea wird richtig positioniert
		 */
		this.page.buildDrawingArea();

		/*
		 * Informationen über andere Spieler erzeugen und in der RobotStatusarea visualisieren
		 */
		for (int key : roundNeedsWrapper.getRobots().keySet()) {
			// if (key != this.myPlayerId) {
			Player enemy = roundNeedsWrapper.getRobots().get(key).getPlayer();
			this.othersReadyRecords.put(key, new OthersReadyRecord(key, "colorpreview/"
					+ this.watcherManager.getRobots().get(key).getColor().getColorPreviewPath(), enemy.getUser().getAccountData()
					.getNickname()));
			this.othersStateRecords.put(key, new OthersStateRecord(key, "colorpreview/"
					+ this.watcherManager.getRobots().get(key).getColor().getColorPreviewPath(), enemy.getUser().getAccountData()
					.getNickname()));
			// }
		}
		this.page.getWatcherStatusArea().getOthersReadyGrid()
				.setData(this.othersReadyRecords.values().toArray(new OthersReadyRecord[0]));
		this.page.getWatcherStatusArea().getOthersStateGrid()
				.setData(this.othersStateRecords.values().toArray(new OthersStateRecord[0]));

		// Domain erstellen
		this.DOMAIN = DomainFactory.getDomain("round_" + roundInfo.getRoundId());
		// EventService um die Round neu zu laden
		this.theEventServiceFactory = RemoteEventServiceFactory.getInstance();
		this.theEventService = this.theEventServiceFactory.getRemoteEventService();
		/*
		 * Listener für widgets adden
		 */
		addListeners();
		/*
		 * Beginn Sound abspielen
		 */
		RoundWatcherPagePresenter.SOUND_MANAGER.play("gamesound-beginn-1.mp3");

		// Andere Musik laden, wenn dies noch nicht geschehen ist.
		if (!MusicManager.getInstance().getSoundfile().equals("robocraft-action-1_6.mp3")) {
			MusicManager.getInstance().stopAndClear();
			MusicManager.getInstance().play("robocraft-action-1_6.mp3");
		}
	}

	/**
	 * Setzt die Listener
	 * 
	 * @return true, falls alles geklappt hat
	 */
	private boolean addListeners() {

		/*
		 * Um das Playingboard mit der Maus zu ziehen
		 */
		final VerticalStack stack = this.page.getMoveCursorArea();
		
		final VerticalStack scrollArea = this.page.getScrollArea();

		/*
		 * Um das Playingboard mit der Maus zu ziehen
		 */
		stack.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				RoundWatcherPagePresenter.this.startScrolledX = RoundWatcherPagePresenter.this.currentScrolledX = -1;
				RoundWatcherPagePresenter.this.currentScrolledY = -1;
			}
		});

		/*
		 * Um das Playingboard mit der Maus zu ziehen
		 */
		stack.addMouseMoveHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if (RoundWatcherPagePresenter.this.startScrolledX != -1) {

					RoundWatcherPagePresenter.this.oldScrolledX = RoundWatcherPagePresenter.this.currentScrolledX;
					RoundWatcherPagePresenter.this.oldScrolledY = RoundWatcherPagePresenter.this.currentScrolledY;

					RoundWatcherPagePresenter.this.currentScrolledX = event.getX();
					RoundWatcherPagePresenter.this.currentScrolledY = event.getY();

					scrollArea.scrollTo(scrollArea.getScrollLeft() + RoundWatcherPagePresenter.this.oldScrolledX
							- RoundWatcherPagePresenter.this.currentScrolledX, scrollArea.getScrollTop()
							+ RoundWatcherPagePresenter.this.oldScrolledY - RoundWatcherPagePresenter.this.currentScrolledY);
				}
			}
		});

		/*
		 * Um das Playingboard mit der Maus zu ziehen
		 */
		stack.addMouseDownHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				RoundWatcherPagePresenter.this.startScrolledX = RoundWatcherPagePresenter.this.currentScrolledX = event.getX();
				RoundWatcherPagePresenter.this.currentScrolledY = event.getY();
			}
		});

		/*
		 * Um das Playingboard mit der Maus zu ziehen
		 */
		stack.addMouseUpHandler(new MouseUpHandler() {

			@Override
			public void onMouseUp(MouseUpEvent event) {
				RoundWatcherPagePresenter.this.startScrolledX = -1;
			}
		});
		
		/*
		 * ClickHandler für die Listgrids mit den anderen Spielern
		 */
		CellClickHandler cellClickHandler = new CellClickHandler() {
			@Override
			public void onCellClick(CellClickEvent event) {
				final int playerId = event.getRecord().getAttributeAsInt("id");
				final Image img = robotsImageList.get(RoundWatcherPagePresenter.this.watcherManager.getRobots().get(playerId));				
								
				if(img.getParent().equals(page.getDrawingArea())) {
					
					/*
					 * Koordinaten ermitteln
					 */
					final int x = img.getX() + 25;
					final int y = img.getY() + 25;
					
					/*
					 * Auf den Roboter scrollen
					 */
					page.getScrollArea().scrollTo(x - RoundPlayerPage.DRAWING_AREA_WIDTH / 2, 
							y - RoundPlayerPage.DRAWING_AREA_HEIGHT / 2);
					
					/*
					 * Kreisanimation für die ganz blinden User
					 */
					final Circle circle = new Circle(x, y, 50);
					circle.setFillOpacity(0.0);
					circle.setStrokeColor("red");
					circle.setStrokeWidth(2);
					page.getDrawingArea().add(circle);
					new Animate(circle, "radius", 50, 0, 400) {
						public void onComplete() {
							page.getDrawingArea().remove(circle);
						}
					}.start();
				}
			}
		};
		this.page.getWatcherStatusArea().getOthersReadyGrid().addCellClickHandler(cellClickHandler);
		this.page.getWatcherStatusArea().getOthersStateGrid().addCellClickHandler(cellClickHandler);

		/*
		 * EndGame
		 */
		this.page.getWatcherStatusArea().getEndGameButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				SC.ask("Sie befinden sich in einem laufenden Spiel!<br /><br />Spiel wirklich verlassen?", new BooleanCallback() {
					@Override
					public void execute(Boolean value) {
						UserPresenter.getInstance().deleteRound(roundInfo.getRoundId());
						exitGame(value);
					}
				});
			}
		});

		/*
		 * Sound an/aus
		 */
		this.page.getWatcherStatusArea().getSoundOn().addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				if (RoundWatcherPagePresenter.this.page.getWatcherStatusArea().getSoundOn().getValueAsBoolean()) {
					RoundWatcherPagePresenter.this.page.getWatcherStatusArea().getSoundSlider().setDisabled(false);
					RoundWatcherPagePresenter.SOUND_MANAGER.stopAndClear();
					RoundWatcherPagePresenter.SOUND_MANAGER.setCanPlaySounds(true);
				} else {
					RoundWatcherPagePresenter.this.page.getWatcherStatusArea().getSoundSlider().setDisabled(true);
					RoundWatcherPagePresenter.SOUND_MANAGER.setCanPlaySounds(false);
				}

			}
		});

		/*
		 * Soundslider verändert sich -> Lautstärke verändern
		 */
		this.page.getWatcherStatusArea().getSoundSlider().addValueChangedHandler(new ValueChangedHandler() {
			@Override
			public void onValueChanged(ValueChangedEvent event) {
				RoundPlayerPagePresenter.SOUND_MANAGER.setVolume((int) RoundWatcherPagePresenter.this.page.getWatcherStatusArea()
						.getSoundSlider().getValue());
			}
		});

		/*
		 * Eventlistener für alle Events Unter anderem werden hier(im Event) die Programmierkarten erhalten Und die Roboterbewgung
		 * gestartet
		 */
		this.theEventService.addListener(DomainFactory.getDomain("round_" + this.roundInfo.getRoundId()),
				new RemoteEventListener() {
					@Override
					public void apply(Event anEvent) {
						((InternalRoundEvent) anEvent).apply(RoundWatcherPagePresenter.this);
					}
				});

		return true;
	}

	/**
	 * Verlässt die Spielrunde
	 * 
	 * @param value
	 *            wird nicht benutzt
	 */
	public void exitGame(final boolean value) {
		
		/*
		 * Serverrequest um zu der Lobby mitzuteilen, dass ich raus bin
		 */
		this.roundService.removeWatcher(UserPresenter.getInstance().getUser().getId(), roundInfo.getRoundId(), new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				/*
				 * Hier passiert nix
				 */
			}
			
			@Override
			public void onFailure(Throwable caught) {
				SC.say(caught.getMessage());
			}
		});
		
		unlistenRemoteListener();
		TabManager.getInstanceMain().closeTab();
	}

	/**
	 * Liefert den ChatPresenter
	 * 
	 * @return chatPresenter
	 */
	public ChatPresenter getChatPresenter() {
		return this.chatPresenter;
	}

	/**
	 * Liefert die Map mit den Feldbildern
	 * 
	 * @return fieldsImageList
	 */
	public Map<String, Image>[][] getFieldsImageList() {
		return this.fieldsImageList;
	}

	/**
	 * Liefert das othersReadyRecords
	 * 
	 * @return othersReadyRecords
	 */
	public Map<Integer, OthersReadyRecord> getOthersReadyRecords() {
		return this.othersReadyRecords;
	}

	/**
	 * Liefert das othersStateRecords
	 * 
	 * @return othersStateRecords
	 */
	public Map<Integer, OthersStateRecord> getOthersStateRecords() {
		return this.othersStateRecords;
	}

	/**
	 * Liefert die Page
	 * 
	 * @return page
	 */
	@Override
	public RoundPage getPage() {
		return this.page;
	}

	/**
	 * Liefert die Map mit Roboterbildern
	 * 
	 * @return robotsImageList
	 */
	public Map<Robot, Image> getRobotsImageList() {
		return this.robotsImageList;
	}

	/**
	 * Liefert das RoundInfo-Objekt
	 * 
	 * @return roundInfo
	 */
	public RoundInfo getRoundInfo() {
		return this.roundInfo;
	}

	/**
	 * Löscht die Domain für die Events
	 */
	public void unlistenRemoteListener() {
		this.theEventService.removeListeners(this.DOMAIN);
	}

	/**
	 * Liefert den zugehörigen Manager
	 * 
	 * @return watcherManager
	 */
	@Override
	public ManagerInterface getManager() {
		return watcherManager;
	}

	/**
	 * Setzt den Status für die rechte Area
	 * 
	 * @param state
	 *            Angabe des Status
	 */
	@Override
	public void setAreaState(int state) {
		this.currentState = state;
		SectionStack sectionStack = this.page.getWatcherStatusArea().getSectionStack();
		
		switch(state) {
		/*
		 * Wartephase (auf Ready geklickt, aber  andere noch nicht fertig)
		 * GivenCardSection und ReadyButton verschwinden
		 * othersReady wird größer
		 */
		case WAITING:
			sectionStack.expandSection("readySection");
			sectionStack.collapseSection("stateSection");
			this.page.getRobotStatusArea().getOthersReadyGrid().setHeight(235);
			break;
		/*
		 * Aktive Runde
		 * GivenCardSection udn ReadyButton verschwinden (oder sind schon weg, sicher ist sicher)
		 * othersReady ist groß (war vlt schon aber.. s.o.)
		 */
		case ACTIVE_ROUND:
			sectionStack.collapseSection("readySection");
			sectionStack.expandSection("stateSection");
			break;
		}
	}

	@Override
	public void resetAreaState() {
		this.setAreaState(this.currentState);
	}

}
