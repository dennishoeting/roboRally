package prototyp.client.presenter.round;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.shape.Circle;

import prototyp.client.presenter.TabManager;
import prototyp.client.presenter.chat.ChatPresenter;
import prototyp.client.presenter.user.UserPresenter;
import prototyp.client.service.RoundManagerService;
import prototyp.client.service.RoundManagerServiceAsync;
import prototyp.client.util.CardSlot;
import prototyp.client.util.DraggedObject;
import prototyp.client.util.MusicManager;
import prototyp.client.util.OthersReadyRecord;
import prototyp.client.util.OthersStateRecord;
import prototyp.client.util.ProgrammingcardImg;
import prototyp.client.util.SoundManager;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;
import prototyp.client.view.round.PlayerStatusArea;
import prototyp.client.view.round.RoundPlayerPage;
import prototyp.shared.animation.Animate;
import prototyp.shared.field.CheckpointField;
import prototyp.shared.programmingcard.Programmingcard;
import prototyp.shared.round.Player;
import prototyp.shared.round.Robot;
import prototyp.shared.round.RoundInfo;
import prototyp.shared.round.RoundNeedsWrapper;
import prototyp.shared.util.DrawingInfo;
import prototyp.shared.util.RobotMovement;
import prototyp.shared.util.events.round.InternalRoundEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Img;
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
 * RoundPlayerPagePresenter für den Player
 * 
 * @author Marcus (Verantwortlicher), timo
 * @version 1.0
 * @version 1.1 chatPresenter jetzt in diesem Presenter und nicht im View.
 * @version 1.2 RoundId wird übergeben
 * 
 */
public class RoundPlayerPagePresenter implements RoundPresenterInterface {
	/**
	 * der Zeitraum für die Programmierphase
	 * 
	 * @author Marcus
	 * @version 1.0
	 */
	public class CountdownTimer extends Timer {

		/** Zeit für den Timer (steht für die Sekundenanzahl) */
		private int seconds = roundInfo.getRoundOption().getCountDownTime();

		/** Wann soll der Timer ausgeführt werden (in Millisekunden) */
		private int restartTime = 1000;

		@Override
		public void run() {
			// Timer anzeigen
			final String time = this.seconds >= 10 ? this.seconds + "" : "0" + this.seconds;
			((PlayerStatusArea) RoundPlayerPagePresenter.this.page.getRobotStatusArea()).getTimer().setContents("00:" + time);

			// Runter zählen
			this.seconds = this.seconds - 1;

			// Wird ausgeführt, wenn die Zeit abgelaufen ist.
			if (this.seconds == -1) {

				shuffleProgrammingCards();

				cancel();
				return;
			}

			// Piepen ab 5 Sekunden ausgeben
			if (this.seconds < 6) {
				SOUND_MANAGER.play("gamesound-piep-1.mp3");
			}

			schedule(this.restartTime);
		}
	}

	/** der Presenter für den Chat */
	private final ChatPresenter chatPresenter;

	/** der LogicManager */
	private final LogicManager logicManager;

	/** die PlayerId */
	private final int myPlayerId = UserPresenter.getInstance().getUser().getId();

	/** die Image-HashMap für die Roboter */
	private final Map<Robot, Image> robotsImageList = new HashMap<Robot, Image>();

	/** das ImageListArray für die Felder */
	private final Map<String, Image>[][] fieldsImageList;

	/** der dazugehörige View */
	private final RoundPlayerPage page;

	/** die jeweilige Spielrunde */
	private final RoundInfo roundInfo;

	/** Domain für das Observer-Pattern der Round */
	protected final Domain DOMAIN;

	/** Gwt-EventService */
	protected final RemoteEventService theEventService;

	/** Gwt-EventService-Fabrik */
	protected final RemoteEventServiceFactory theEventServiceFactory;

	/** das RPC */
	private final RoundManagerServiceAsync roundService = GWT.create(RoundManagerService.class);

	/** AREA-STATES */
	public static final int PROGRAMMING = 0, ACTIVE_ROUND = 1, WAITING = 2, POWER_DOWN_ACTIVE = 3, DEAD = 4,
			POWER_DOWN_PROGRAMMING = 5;

	/** flag, ob Menue gezeigt wird */
	private boolean menueIsShown;

	/** derzeitiger AreaState */
	private int currAreaState;


	/** Map mit den Angaben für das ListGrid in der RobotStatusArea */
	private final Map<Integer, OthersReadyRecord> othersReadyRecords = new HashMap<Integer, OthersReadyRecord>();

	/** Map mit den Angaben für das ListGrid in der RobotStatusArea */
	private final Map<Integer, OthersStateRecord> othersStateRecords = new HashMap<Integer, OthersStateRecord>();

	/** der CountdownTimer */
	private CountdownTimer countdownTimer;
	
	/** das Bild meines Roboters oben neben den Schadenspunkten */
	private final Img myRobotImage;
	
	/** Bild, des aktuell errechten Checkpoints */
	private final Img myCheckpointImage;

	/** SoundManager für die Soundeffekte */
	public static final SoundManager SOUND_MANAGER = new SoundManager();
	

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
	 * @param round
	 *            RoundInfo der ausgewählten Runde
	 */
	@SuppressWarnings("unchecked")
	public RoundPlayerPagePresenter(final ChatPresenter chatPresenter, final RoundInfo round, final RoundNeedsWrapper roundNeedsWrapper) {
		this.menueIsShown = false;
		this.currAreaState = RoundPlayerPagePresenter.PROGRAMMING;

		/*
		 * Runde zuweisen
		 */
		this.roundInfo = round;

		/*
		 * Page erstellen
		 */
		this.page = new RoundPlayerPage(this);

		/*
		 * den LogicManager erstellen
		 */
		this.logicManager = new LogicManager(this);
		this.logicManager.setRoundOption(roundNeedsWrapper.getRoundOption());
		this.logicManager.setPlayingBoard(roundNeedsWrapper.getPlayingBoard());
		this.logicManager.setRobots(roundNeedsWrapper.getRobots());

		this.logicManager.createBackupFieldMap();

		/*
		 * Chat aus der PreGamePage übenehmen
		 */
		this.chatPresenter = chatPresenter;

		/*
		 * Chat reinpacken
		 */
		this.page.getRobotStatusArea().setChatPresenter(chatPresenter);

		/*
		 * Spielfeld reinpacken
		 */
		this.page.getDrawingArea().setHeight(roundNeedsWrapper.getPlayingBoard().getHeight() * 50);
		this.page.getDrawingArea().setWidth(roundNeedsWrapper.getPlayingBoard().getWidth() * 50);

		/*
		 * Spielfeld zeichnen
		 */
		this.fieldsImageList = new HashMap[this.logicManager.getPlayingBoard().getHeight()][this.logicManager.getPlayingBoard()
				.getWidth()];
		for (int i = 0; i < this.logicManager.getPlayingBoard().getHeight(); i++) {
			for (int j = 0; j < this.logicManager.getPlayingBoard().getWidth(); j++) {

				this.fieldsImageList[i][j] = new HashMap<String, Image>();
				for (DrawingInfo info : this.logicManager.getPlayingBoard().getFields()[i][j].getImagePathList()) {
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
		for (Robot robot : this.logicManager.getRobots().values()) {
			Image image = new Image(robot.getJ() * 50, robot.getI() * 50, 50, 50, robot.getImagePath());
			this.page.getDrawingArea().add(image);
			this.robotsImageList.put(robot, image);
		}

		/*
		 * Die DrawingArea wird richtig positioniert
		 */
		this.page.buildDrawingArea();

		/*
		 * Den MyRobot und aktuellen Checkpoint (rechts oben als Bild) setzen und einen Timer als Label hinzufügen
		 */
		this.myRobotImage = new Img(this.logicManager.setMyRobot().getPreviewImagePath(), 50, 50);
		this.myRobotImage.setCursor(Cursor.HAND);
		((PlayerStatusArea) this.page.getRobotStatusArea()).getUpperPlayerStateStack().addMember(this.myRobotImage);
		this.myCheckpointImage = new Img("robotStatus/checkpoint_0v2.png", 50, 50);
		this.myCheckpointImage.setCursor(Cursor.HAND);
		((PlayerStatusArea) this.page.getRobotStatusArea()).getUpperPlayerStateStack().addMember(this.myCheckpointImage);
		((PlayerStatusArea) this.page.getRobotStatusArea()).getUpperPlayerStateStack().addMember(
				((PlayerStatusArea) this.page.getRobotStatusArea()).getTimer());

		/*
		 * Informationen über andere Spieler erzeugen und in der RobotStatusarea visualisieren
		 */
		for (int key : roundNeedsWrapper.getRobots().keySet()) {
			if (key != this.myPlayerId) {
				final Player enemy = roundNeedsWrapper.getRobots().get(key).getPlayer();
				this.othersReadyRecords.put(key,
						new OthersReadyRecord(key, "colorpreview/"
								+ this.logicManager.getRobots().get(key).getColor().getColorPreviewPath(), enemy.getUser()
								.getAccountData().getNickname()));
				this.othersStateRecords.put(key,
						new OthersStateRecord(key, "colorpreview/"
								+ this.logicManager.getRobots().get(key).getColor().getColorPreviewPath(), enemy.getUser()
								.getAccountData().getNickname()));
			}
		}
		this.page.getRobotStatusArea().getOthersReadyGrid()
				.setData(this.othersReadyRecords.values().toArray(new OthersReadyRecord[0]));
		this.page.getRobotStatusArea().getOthersStateGrid()
				.setData(this.othersStateRecords.values().toArray(new OthersStateRecord[0]));


		// Domain erstellen
		this.DOMAIN = DomainFactory.getDomain("round_" + round.getRoundId());

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
		RoundPlayerPagePresenter.SOUND_MANAGER.play("gamesound-beginn-1.mp3");

		// Andere Musik laden, wenn dies noch nicht geschehen ist.
		if (!MusicManager.getInstance().getSoundfile().equals("robocraft-action-1_6.mp3")) {
			MusicManager.getInstance().stopAndClear();
			MusicManager.getInstance().play("robocraft-action-1_6.mp3");
		}
		
		/*
		 * Die ersten Programmierkarten werden per RPC geholt.
		 */
		receiveProgrammingCards();
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
				RoundPlayerPagePresenter.this.startScrolledX = RoundPlayerPagePresenter.this.currentScrolledX = -1;
				RoundPlayerPagePresenter.this.currentScrolledY = -1;
			}
		});

		/*
		 * Um das Playingboard mit der Maus zu ziehen
		 */
		stack.addMouseMoveHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if (RoundPlayerPagePresenter.this.startScrolledX != -1) {

					RoundPlayerPagePresenter.this.oldScrolledX = RoundPlayerPagePresenter.this.currentScrolledX;
					RoundPlayerPagePresenter.this.oldScrolledY = RoundPlayerPagePresenter.this.currentScrolledY;

					RoundPlayerPagePresenter.this.currentScrolledX = event.getX();
					RoundPlayerPagePresenter.this.currentScrolledY = event.getY();

					scrollArea.scrollTo(scrollArea.getScrollLeft() + RoundPlayerPagePresenter.this.oldScrolledX
							- RoundPlayerPagePresenter.this.currentScrolledX, scrollArea.getScrollTop()
							+ RoundPlayerPagePresenter.this.oldScrolledY - RoundPlayerPagePresenter.this.currentScrolledY);
				}
			}
		});

		/*
		 * Um das Playingboard mit der Maus zu ziehen
		 */
		stack.addMouseDownHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				RoundPlayerPagePresenter.this.startScrolledX = RoundPlayerPagePresenter.this.currentScrolledX = event.getX();
				RoundPlayerPagePresenter.this.currentScrolledY = event.getY();
			}
		});

		/*
		 * Um das Playingboard mit der Maus zu ziehen
		 */
		stack.addMouseUpHandler(new MouseUpHandler() {

			@Override
			public void onMouseUp(MouseUpEvent event) {
				RoundPlayerPagePresenter.this.startScrolledX = -1;
			}
		});

		/*
		 * Menue-Regelung
		 */
		((PlayerStatusArea) this.page.getRobotStatusArea()).getMenueButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				organizeMenue(RoundPlayerPagePresenter.this.menueIsShown);
			}
		});
		
		
		/*
		 * ClickHandler für die Listgrids mit den anderen Spielern
		 */
		CellClickHandler cellClickHandler = new CellClickHandler() {
			@Override
			public void onCellClick(CellClickEvent event) {
				final int playerId = event.getRecord().getAttributeAsInt("id");
				final Image img = robotsImageList.get(RoundPlayerPagePresenter.this.logicManager.getRobots().get(playerId));				
								
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
		this.page.getRobotStatusArea().getOthersReadyGrid().addCellClickHandler(cellClickHandler);
		this.page.getRobotStatusArea().getOthersStateGrid().addCellClickHandler(cellClickHandler);

		/*
		 * Wenn man auf sein Roboterbild klickt, wird einem gezeigt, wo dieser auf dem Spielfeld steht.
		 */
		this.myRobotImage.addClickHandler(new ClickHandler() {	
			@Override
			public void onClick(ClickEvent event) {
				final Image img = robotsImageList.get(RoundPlayerPagePresenter.this.logicManager.getMyRobot());				
								
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
		});
		
		/*
		 * Wenn man auf sein Checkpointbild klickt, wird der Checkpoint
		 * angezeigt, der als nächstes zu erreichen ist
		 */
		this.myCheckpointImage.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {			
				
				for(final CheckpointField cpField : logicManager.getPlayingBoard().getCheckpointFieldList()) {
					if(cpField.getNumberOfCheckpoint() == logicManager.getMyRobot().getNumberOfReachedCheckpoints() + 1) {
						
					    /*
						 * Koordinaten ermitteln
						 */
						final int x = cpField.getJ()*50 + 25;
						final int y = cpField.getI()*50 + 25;
							
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
						circle.setStrokeColor("lightgreen");
						circle.setStrokeWidth(2);
						page.getDrawingArea().add(circle);
						new Animate(circle, "radius", 50, 0, 400) {
							public void onComplete() {
								page.getDrawingArea().remove(circle);
							}
						}.start();
						
						/*
						 * die Schleife verlassen
						 */
						break;
					}
				}
				
			}
			
		});
		
		/*
		 * EndGame
		 */
		this.page.getRobotStatusArea().getEndGameButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				SC.ask(Page.props.roundPlayerPage_exit_text(), new BooleanCallback() {
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
		this.page.getRobotStatusArea().getSoundOn().addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				if (RoundPlayerPagePresenter.this.page.getRobotStatusArea().getSoundOn().getValueAsBoolean()) {
					RoundPlayerPagePresenter.this.page.getRobotStatusArea().getSoundSlider().setDisabled(false);
					RoundPlayerPagePresenter.SOUND_MANAGER.stopAndClear();
					RoundPlayerPagePresenter.SOUND_MANAGER.setCanPlaySounds(true);
				} else {
					RoundPlayerPagePresenter.this.page.getRobotStatusArea().getSoundSlider().setDisabled(true);
					RoundPlayerPagePresenter.SOUND_MANAGER.setCanPlaySounds(false);
				}

			}
		});

		/*
		 * Soundslider verändert sich -> Lautstärke verändern
		 */
		this.page.getRobotStatusArea().getSoundSlider().addValueChangedHandler(new ValueChangedHandler() {
			@Override
			public void onValueChanged(ValueChangedEvent event) {
				RoundPlayerPagePresenter.SOUND_MANAGER.setVolume((int) RoundPlayerPagePresenter.this.page.getRobotStatusArea()
						.getSoundSlider().getValue());
			}
		});

		/*
		 * Power-Down Button
		 */
		((PlayerStatusArea) this.page.getRobotStatusArea()).getPowerDownButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				if(logicManager.getMyRobot().getPowerDown() == 0) {
					
					/*
					 * An server schicken mit event zurück
					 */
					RoundPlayerPagePresenter.this.roundService.announcePowerDown(
							RoundPlayerPagePresenter.this.roundInfo.getRoundId(), RoundPlayerPagePresenter.this.myPlayerId, 1,
							new AsyncCallback<Boolean>() {

								@Override
								public void onFailure(Throwable caught) {
									// Ausgeben
									// TODO: Muss vll. raus, ist zu Testzwecken drin
									SC.say(caught.getMessage());
								}

								@Override
								public void onSuccess(Boolean result) {
								
									/*
									 * Chatnachricht, dass ich PowerDown bin
									 */
									RoundPlayerPagePresenter.this.logicManager.sendMessage(UserPresenter.getInstance().getNickname() + " "
											+ Page.props.roundPlayerPage_chat_powerdown_start());
								
									((PlayerStatusArea) page.getRobotStatusArea()).getPowerDownButton().setDisabled(true);
								
								}

							});
					}
			}
		});

		/*
		 * Hier wird die Ereignisbehandlung des Bereit-Buttons durchgeführt. Karten, wenn sie richtig gewählt wurden, werden an
		 * den Server gesandt und der Bereit-Button wird deaktiviert. Anschließend wird auf ein Event gewartet, das geworfen wird,
		 * sobald alle Spieler ihre Karten gesendet haben.
		 */
		((PlayerStatusArea) this.page.getRobotStatusArea()).getReadyButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (sendProgrammingCards()) {
					RoundPlayerPagePresenter.this.setAreaState(WAITING);
				}
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
						((InternalRoundEvent) anEvent).apply(RoundPlayerPagePresenter.this);
					}
				});

		return true;
	}

	/**
	 * Spielrunde abbrechen
	 */
	public void cancelRound() {
		this.roundService.removePlayer(this.roundInfo.getRoundId(), this.myPlayerId, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				// Ausgeben
				// TODO: Muss vll. raus, ist zu Testzwecken drin
				SC.say(caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
			}
		});
	}

	/**
	 * Beendet die aktive Spielrunde
	 * 
	 * @param value
	 *            Angabe, ob das Spiel beendet werden soll
	 */
	public void exitGame(final boolean value) {
		if (value) {
			this.countdownTimer.cancel();

			this.logicManager.setMeLeft(true);

			unlistenRemoteListener();
			
			this.logicManager.sendMessage(UserPresenter.getInstance().getNickname() + " "
					+ Page.props.roundPlayerPage_chat_exitRound());
			TabManager.getInstanceMain().closeTab();

			if (this.logicManager.getAnimationTimer() != null) {
				setStepReady(1);
				try {
					this.logicManager.getAnimationTimer().cancel();
				} catch (Exception ex) {
					SC.say(Page.props.global_title_error(), ex.getMessage());
				}
			} else {

				// SOFORT raus
				this.roundService.abortFromRound(this.roundInfo.getRoundId(), this.myPlayerId, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						// Ausgeben
						// TODO: Muss vll. raus, ist zu Testzwecken drin
						SC.say(caught.getMessage());
					}

					@Override
					public void onSuccess(Boolean result) {
						// Interessiert gar keinen mehr
					}

				});
			}
		}
	}


	/**
	 * Liefert den ChatPresenter
	 * 
	 * @return the chatPresenter
	 */
	public ChatPresenter getChatPresenter() {
		return this.chatPresenter;
	}

	/**
	 * Liefert den CoundDownTimer
	 * 
	 * @return countdownTimer
	 */
	public CountdownTimer getCountdownTimer() {
		return this.countdownTimer;
	}

	/**
	 * @return the fieldsImageList
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
	public RoundPlayerPage getPage() {
		return this.page;
	}

	/**
	 * @return the robotsImageList
	 */
	public Map<Robot, Image> getRobotsImageList() {
		return this.robotsImageList;
	}

	/**
	 * @return the roundInfo
	 */
	public RoundInfo getRoundInfo() {
		return this.roundInfo;
	}

	/**
	 * Diese Methode liefert eine Liste mit den gewählten Programmierkarten des Spielers
	 * 
	 * @return eine Liste mit den ausgewählten Programmierkarten oder null, wenn nicht alle verfügbaren Programmierkarten
	 *         ausgewählt wurden.
	 */
	private List<Programmingcard> getSetCards() {

		/*
		 * Programmierkartenliste erstellen
		 */
		List<Programmingcard> programmingcardList = new ArrayList<Programmingcard>();
		for (int i = 0; i < 5; i++) {

			// null wird geliefert, falls sich auf irgendeinem Kartenslot sich
			// keine programmierkarte befindet
			if (((PlayerStatusArea) this.page.getRobotStatusArea()).getCardSetSlots()[i].isFree()) {
				return null;
			}

			// die Programmierkarte hinzufügen
			programmingcardList
					.add(((PlayerStatusArea) this.page.getRobotStatusArea()).getCardSetSlots()[i].getProgrammingcardImg()
							.getProgrammingcard());
		}

		/*
		 * Liefern der Programmierkartenliste
		 */
		return programmingcardList;
	}

	/**
	 * Organisiert die robotStateArea bei Menüanfrage.
	 * 
	 * @param menueIsShown
	 *            true, wenn das Menü gezeigt wird
	 */
	private void organizeMenue(boolean menueIsShown) {
		SectionStack stack = this.page.getRobotStatusArea().getSectionStack();

		if (menueIsShown) {
			this.menueIsShown = !this.menueIsShown;

			/*
			 * Alle Sections wieder öffnen Sections setCards-stateSecton werden über currAreaState hergestellt
			 */
			stack.expandSection("playerState");
			setAreaState(this.currAreaState);
			stack.expandSection("chat");
			stack.collapseSection("menue");
		} else {
			this.menueIsShown = !this.menueIsShown;

			/*
			 * Alle Sections schließen und menueSection öffnen
			 */
			stack.collapseSection("playerState");
			stack.collapseSection("setCards");
			stack.collapseSection("givenCards");
			stack.collapseSection("powerDownInfo");
			stack.collapseSection("deadInfo");
			stack.collapseSection("readyButton");
			stack.collapseSection("readySection");
			stack.collapseSection("stateSection");
			stack.collapseSection("chat");
			stack.expandSection("menue");

			/*
			 * Soundeinstellungen verändern, wichtig für mehrere gleichzeitige Spiele
			 */
			this.page.getRobotStatusArea().getSoundOn().setValue(RoundPlayerPagePresenter.SOUND_MANAGER.isCanPlaySounds());
			this.page.getRobotStatusArea().getSoundSlider().setValue(RoundPlayerPagePresenter.SOUND_MANAGER.getVolume());
		}
	}

	/**
	 * Diese Methode fordert beim Server Programmierkarten an
	 */
	public void receiveProgrammingCards() {

		this.roundService.receiveProgrammingCards(this.roundInfo.getRoundId(), this.myPlayerId,
				new AsyncCallback<List<Programmingcard>>() {

					@Override
					public void onFailure(final Throwable caught) {
						/*
						 * Ausgeben der Fehlermeldung
						 */
						SC.say(Page.props.global_title_error(), caught.getMessage());
					}

					@Override
					public void onSuccess(final List<Programmingcard> result) {
						if (!RoundPlayerPagePresenter.this.logicManager.getMyRobot().isDead()) {
							
							((PlayerStatusArea)page.getRobotStatusArea()).getReadyButton().setDisabled(
									!((PlayerStatusArea)page.getRobotStatusArea()).allCardsSet());

							/*
							 * Erste Karte, die immer eine Bewegungskarte ist, zufällig woanders hinsetzen
							 */
							final int random = (int) (Math.random() * RoundPlayerPagePresenter.this.logicManager.getMyRobot()
									.getNumberOfAvailableCards());
							final Programmingcard switchedCard = result.set(random, result.get(0));
							result.set(0, switchedCard);

							/*
							 * CountdownTimer zurücksetzen
							 */
							final String time = 
								roundInfo.getRoundOption().getCountDownTime() >= 10 
								? roundInfo.getRoundOption().getCountDownTime() + "" : "0" + roundInfo.getRoundOption().getCountDownTime();
								((PlayerStatusArea) RoundPlayerPagePresenter.this.page.getRobotStatusArea()).getTimer().setContents("00:" + time);

							/*
							 * Areas organisieren: Programmierphase
							 */
							setAreaState(RoundPlayerPagePresenter.PROGRAMMING);

							/*
							 * Bereit-Status in othersReady und KartenSlots markieren
							 */
							for (final int key : RoundPlayerPagePresenter.this.logicManager.getRobots().keySet()) {
								if (key != UserPresenter.getInstance().getUser().getId() // nicht ich selbst
										&& !RoundPlayerPagePresenter.this.logicManager.getRobots().get(key).isDead() // anderer
																														// nicht
																														// tot
										&& RoundPlayerPagePresenter.this.logicManager.getRobots().get(key).getPowerDown() != 2) {// anderer
																																	// nicht
									final Robot otherRobot = RoundPlayerPagePresenter.this.logicManager.getRobots().get(key);																						// powerdown
									final OthersReadyRecord record = RoundPlayerPagePresenter.this.othersReadyRecords.get(key);
									record.setIsReady(false); 
									for (int i = 1; i <= otherRobot.getNumberOfFreeCardSlots(); i++) {
										record.setCardSet(i, 1); 
									}
									for (int i = otherRobot.getNumberOfFreeCardSlots() + 1; i <= 5; i++) {
										record.setCardSet(i, 2);
									}
								} 
							}
							RoundPlayerPagePresenter.this.page.getRobotStatusArea().getOthersReadyGrid().redraw();
							
							
							/*
							 * Eigene KartenSlots anhand der Schadenspunkte sperren(cardsSetSlot) + Karten entfernen
							 */
							for (int i = 0; i < RoundPlayerPagePresenter.this.logicManager.getMyRobot()
									.getNumberOfFreeCardSlots(); i++) {
								final CardSlot slot = ((PlayerStatusArea) RoundPlayerPagePresenter.this.page.getRobotStatusArea())
								.getCardSetSlots()[i]; 
								slot.setIsLocked(false);
								slot.removeProgrammingcardImg();
							}
							for (int i = RoundPlayerPagePresenter.this.logicManager.getMyRobot()
									.getNumberOfFreeCardSlots(); i < 5; i++) {
								
								final CardSlot slot = ((PlayerStatusArea) RoundPlayerPagePresenter.this.page.getRobotStatusArea())
								.getCardSetSlots()[i]; 
								slot.setIsLocked(true);
							}
							

							/*
							 * Programmierkarten den Kartenslots hinzufügen
							 */
							// Wenn NICHT PowerDown
							if (RoundPlayerPagePresenter.this.logicManager.getMyRobot().getPowerDown() != 2) {
								// für jede mögliche Karte
								for (int i = 0; i < RoundPlayerPagePresenter.this.logicManager.getMyRobot()
										.getNumberOfAvailableCards(); i++) {
									final ProgrammingcardImg dragImg = new ProgrammingcardImg(result.get(i),
											((PlayerStatusArea) RoundPlayerPagePresenter.this.page.getRobotStatusArea())
													.getCardGivenSlots()[i], 35, 50,
											((PlayerStatusArea) RoundPlayerPagePresenter.this.page.getRobotStatusArea())
													.getCardSetSlots(), ((PlayerStatusArea) RoundPlayerPagePresenter.this.page
													.getRobotStatusArea()).getCardGivenSlots());

									((PlayerStatusArea) RoundPlayerPagePresenter.this.page.getRobotStatusArea())
											.getCardGivenSlots()[i].setProgrammingcardImg(dragImg);

									/*
									 * PlayerId der Programmierkarte setzen
									 */
									result.get(i).setPlayerId(UserPresenter.getInstance().getUser().getId());

								}
								

								/*
								 * Countdown starten
								 */
								RoundPlayerPagePresenter.this.countdownTimer = new CountdownTimer();
								RoundPlayerPagePresenter.this.countdownTimer.schedule(1000);

							} else {
								// Wenn Powerdown, bleibe in beobachtersicht
								/*
								 * Available Cards und Ready-Button weg
								 */
								RoundPlayerPagePresenter.this.setAreaState(RoundPlayerPagePresenter.POWER_DOWN_PROGRAMMING);
								sendProgrammingCards();
							}
						}
					}
				});
	}

	/**
	 * Sendet an den Server ein Event, dass der Spieler eine Karte gelegt hat oder wieder eine entfernt hat.
	 * 
	 * @param slot
	 *            Kartenslot
	 * @param set
	 *            Art der Veränderung
	 */
	public void sendOneCardSet(int slot, int set) {
		this.roundService.cardSetInSlot(this.roundInfo.getRoundId(), this.myPlayerId, slot, set, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {

				// TODO: Muss vll. raus, ist zu Testzwecken drin
				SC.say(caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				/*
				 * Nix passiert, da auf ein Event gewartet wird
				 */
			}
		});
	}

	/**
	 * Programmierkarten senden
	 */
	public boolean sendProgrammingCards() {
		
		
		/*
		 * Programmierkartenliste deklarieren
		 */
		List<Programmingcard> programmingcardList = getSetCards();
		

		/*
		 * Pseudoliste erstellen, wenn Spieler PowerDown ist
		 */
		if (this.logicManager.getMyRobot().getPowerDown() == 2) {

			this.countdownTimer.cancel();

			programmingcardList = new ArrayList<Programmingcard>();

			for (int i = 0; i < 5; i++) {
				final Programmingcard dummyCard = new Programmingcard(1, RobotMovement.MOVE_FORWARDS);
				dummyCard.setPlayerId(this.myPlayerId);
				programmingcardList.add(dummyCard);
			}

			/*
			 * RPC-Call, der die Programmierkarten an den Server sendet
			 */
			this.roundService.sendProgrammingcards(this.roundInfo.getRoundId(), this.myPlayerId, programmingcardList,
					new AsyncCallback<Boolean>() {
						@Override
						public void onFailure(Throwable caught) {
							/*
							 * Ausgeben der Fehlermeldung
							 */
							SC.say(Page.props.global_title_error(), caught.getMessage());
						}

						@Override
						public void onSuccess(Boolean result) {
							/*
							 * Nix
							 */
						}
					});
		}

		/*
		 * Wenn alle Karten richtig gewählt wurden, können werden diese an den Server versendet und der Bereit-Button wird
		 * deaktiviert. Auf dem Server wird ein Event geschmissen, sobald alle Spieler ihre Programmierkarten gesendet haben In
		 * dem Event werden die Bewegungen ausgeführt.
		 */
		else if (programmingcardList != null) {

			this.countdownTimer.cancel();


			/*
			 * Karten aus den GivenSlots entfernen
			 */
			for (int i = 0; i < 9; i++) {
				((PlayerStatusArea)this.page.getRobotStatusArea()).getCardGivenSlots()[i].removeProgrammingcardImg();
			}

			/*
			 * Karten umdrehen, sodass nur die Hinterseite zu sehen ist, und die Karten undragable machen
			 */
			for (int i = 0; i < 5; i++) {
				// Karte undragable machen
				((PlayerStatusArea) this.page.getRobotStatusArea()).getCardSetSlots()[i].setIsLocked(true);

				// Priority entfernen
				((ProgrammingcardImg) ((PlayerStatusArea) this.page.getRobotStatusArea()).getCardSetSlots()[i].getProgrammingcardImg())
						.setPriorityVisible(false);

				// Karte verdeckt umdrehen
				((ProgrammingcardImg) ((PlayerStatusArea) this.page.getRobotStatusArea()).getCardSetSlots()[i].getProgrammingcardImg())
						.setSrc(Programmingcard.BACKSIDE_IMAGEPATH);

			}

			/*
			 * RPC-Call, der die Programmierkarten an den Server sendet
			 */
			this.roundService.sendProgrammingcards(this.roundInfo.getRoundId(), this.myPlayerId, programmingcardList,
					new AsyncCallback<Boolean>() {
						@Override
						public void onFailure(Throwable caught) {
							/*
							 * Ausgeben der Fehlermeldung
							 */
							SC.say(Page.props.global_title_error(), caught.getMessage());
						}

						@Override
						public void onSuccess(Boolean result) {
							/*
							 * Nix
							 */
						}
					});
			
			// In den Wartezustand versetzen
			return true;
			
		}
		
		return false;
	}

	/**
	 * Sendet ein Event an den Server, dass ein neues RestartField gesetzt wurde.
	 * 
	 * @param i
	 *            X-Koordinate
	 * @param j
	 *            Y-Koordinate
	 */
	public void sendRestartField(int i, int j) {
		this.roundService.sendRestartField(this.roundInfo.getRoundId(), this.myPlayerId, i, j, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				// Ausgeben
				// TODO: Muss vll. raus, ist zu Testzwecken drin
				SC.say(caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				// Nix
			}
		});
	}

	/**
	 * Areas organisieren Geschlossen werden givenCardsStack, readyButtonStack, othersReadyStack Geöffnet werden othersStatusStack
	 * 
	 * @param state
	 *            Status
	 */
	public void setAreaState(int state) {
		final SectionStack sectionStack = this.page.getRobotStatusArea().getSectionStack();
		// Speichern für Menue

		if (this.logicManager.getMyRobot().getPowerDown() == 2) {
			if (state == PROGRAMMING || state == WAITING) {
				state = POWER_DOWN_PROGRAMMING;
			} else if (state == ACTIVE_ROUND) {
				state = POWER_DOWN_ACTIVE;
			}
		}

		this.currAreaState = state;

		if (!this.menueIsShown) {
			switch (state) {
			/*
			 * Programmierphase. GivenCardSection und ReadyButton sind sichtbar, außerdem (in klein) die othersReadyArea
			 */
			case PROGRAMMING:
				this.page.getRobotStatusArea().getOthersReadyGrid().setHeight(135);
				sectionStack.expandSection("setCards");
				sectionStack.expandSection("givenCards");
				sectionStack.expandSection("readyButton");
				sectionStack.collapseSection("powerDownInfo");
				sectionStack.collapseSection("deadInfo");
				sectionStack.expandSection("readySection");
				sectionStack.collapseSection("stateSection");
				break;
			/*
			 * Wartephase (auf Ready geklickt, aber andere noch nicht fertig) GivenCardSection und ReadyButton verschwinden
			 * othersReady wird größer
			 */
			case WAITING:
				this.page.getRobotStatusArea().getOthersReadyGrid().setHeight(235);
				sectionStack.expandSection("setCards");
				sectionStack.collapseSection("givenCards");
				sectionStack.collapseSection("readyButton");
				sectionStack.collapseSection("powerDownInfo");
				sectionStack.collapseSection("deadInfo");
				sectionStack.expandSection("readySection");
				sectionStack.collapseSection("stateSection");
				break;
			/*
			 * Aktive Runde GivenCardSection udn ReadyButton verschwinden (oder sind schon weg, sicher ist sicher) othersReady ist
			 * groß (war vlt schon aber.. s.o.)
			 */
			case ACTIVE_ROUND:
				this.page.getRobotStatusArea().getOthersStateGrid().setHeight(235);
				sectionStack.expandSection("setCards");
				sectionStack.collapseSection("givenCards");
				sectionStack.collapseSection("powerDownInfo");
				sectionStack.collapseSection("deadInfo");
				sectionStack.collapseSection("readyButton");
				sectionStack.collapseSection("readySection");
				sectionStack.expandSection("stateSection");
				break;

			case POWER_DOWN_ACTIVE:
				this.page.getRobotStatusArea().getOthersStateGrid().setHeight(135);
				sectionStack.collapseSection("setCards");
				sectionStack.collapseSection("givenCards");
				sectionStack.collapseSection("readyButton");
				sectionStack.expandSection("powerDownInfo");
				sectionStack.collapseSection("deadInfo");
				sectionStack.collapseSection("readySection");
				sectionStack.expandSection("stateSection");
				break;

			case POWER_DOWN_PROGRAMMING:
				this.page.getRobotStatusArea().getOthersReadyGrid().setHeight(135);
				sectionStack.collapseSection("setCards");
				sectionStack.collapseSection("givenCards");
				sectionStack.collapseSection("readyButton");
				sectionStack.expandSection("powerDownInfo");
				sectionStack.collapseSection("deadInfo");
				sectionStack.expandSection("readySection");
				sectionStack.collapseSection("stateSection");
				break;

			case DEAD:
				this.page.getRobotStatusArea().getOthersStateGrid().setHeight(135);
				sectionStack.collapseSection("setCards");
				sectionStack.collapseSection("givenCards");
				sectionStack.collapseSection("readyButton");
				sectionStack.collapseSection("powerDownInfo");
				sectionStack.expandSection("deadInfo");
				sectionStack.collapseSection("readySection");
				sectionStack.expandSection("stateSection");
				break;
			}
		}
	}

	/**
	 * Setzt die RestartTimer für die einzelnen Spielrunden (Muss verändert werden, wenn sich der CountDown verringern soll)
	 * 
	 * @param restartTime
	 *            Neue Restartzeit
	 */
	public void setRestartTime(int restartTime) {
		this.countdownTimer.restartTime = restartTime;
	}

	/**
	 * Wird vom LogicManager aufgerufen, nachdem ein SPIELSCHRITT abgearbeitet ist
	 * 
	 * @param robotState
	 *            Roboterstatus
	 */
	public void setStepReady(int robotState) {
		this.roundService.sendRequestStepReady(this.roundInfo.getRoundId(), this.myPlayerId, robotState,
				new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						// Ausgeben
						SC.say(Page.props.global_title_error(), caught.getMessage());
					}

					@Override
					public void onSuccess(Boolean result) {
						/*
						 * Hier passiert nichts, da auf ein event gewartet wird
						 */
					}
				});
	}

	/**
	 * Wird aufgerufen, wenn der Countdown abgelaufen ist
	 */
	private void shuffleProgrammingCards() {
		
		/*
		 * Kartenliste
		 */
		List<ProgrammingcardImg> list = new ArrayList<ProgrammingcardImg>(9);

		for (final CardSlot cardSlot : ((PlayerStatusArea) this.page.getRobotStatusArea()).getCardSetSlots()) {
			if (!cardSlot.isFree() && !cardSlot.isLocked()) {
				try {
					list.add(cardSlot.getProgrammingcardImg());
					cardSlot.removeProgrammingcardImg();
				} catch (Exception ex) {
					/*
					 * Karte Wird grad gedragged
					 */
					list.add((ProgrammingcardImg) DraggedObject.getDragObject());
					cardSlot.removeProgrammingcardImg();
				}
			}
		}

		for (final CardSlot cardSlot : ((PlayerStatusArea) this.page.getRobotStatusArea()).getCardGivenSlots()) {
			if (!cardSlot.isFree()) {
				try {
					list.add(cardSlot.getProgrammingcardImg());
					cardSlot.removeProgrammingcardImg();
				} catch (Exception ex) {
					/*
					 * Karte Wird grad gedragged
					 */
					list.add((ProgrammingcardImg) DraggedObject.getDragObject());
					cardSlot.removeProgrammingcardImg();
				}
			}
		}

		/*
		 * Liste mischen
		 */
		for (int i = 0; i < list.size(); i++) {
			final ProgrammingcardImg t = list.get(i);
			int random = (int) (Math.random() * list.size());

			list.set(i, list.get(random));
			list.set(random, t);
		}

		list = list.subList(0, this.logicManager.getMyRobot().getNumberOfFreeCardSlots());

		/*
		 * die sublist auf die Kartenslots packen
		 */
		int i = 0;
		for (final ProgrammingcardImg programmingcardImg : list) {

			((PlayerStatusArea) this.page.getRobotStatusArea()).getCardSetSlots()[i++].setProgrammingcardImg(programmingcardImg);
		}

		if (this.logicManager.getMyRobot().getPowerDown() != 2 && !this.logicManager.getMyRobot().isDead())
			this.setAreaState(RoundPlayerPagePresenter.WAITING);
		GWT.log("PowerDown " + this.logicManager.getMyRobot().getPlayer().getUser().getAccountData().getNickname() + ": "
				+ this.logicManager.getMyRobot().getPowerDown());
		
		/*
		 * die zufällig ausgewähltenKarten an den Server senden
		 */
		sendProgrammingCards();
	}

	/**
	 * Löscht die Domain für die Events
	 */
	public void unlistenRemoteListener() {
		this.theEventService.removeListeners(this.DOMAIN);
	}

	/**
	 * Liefert den Manager
	 * 
	 * @return Manager
	 */
	@Override
	public ManagerInterface getManager() {
		return this.logicManager;
	}

	@Override
	public void resetAreaState() {
		this.setAreaState(this.currAreaState);
	}

	/**
	 * @return the myCheckpointImage
	 */
	public Img getMyCheckpointImage() {
		return myCheckpointImage;
	}
}
