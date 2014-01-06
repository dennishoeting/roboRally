package prototyp.client.presenter.pregame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.TabManager;
import prototyp.client.presenter.chat.ChatPresenter;
import prototyp.client.presenter.lobby.LobbyPagePresenter;
import prototyp.client.presenter.user.UserPresenter;
import prototyp.client.service.RoundManagerService;
import prototyp.client.service.RoundManagerServiceAsync;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.PlayerRecord;
import prototyp.client.view.Page;
import prototyp.client.view.pregame.PreGameAbstractPage;
import prototyp.shared.round.Player;
import prototyp.shared.round.RoundInfo;
import prototyp.shared.round.Watcher;
import prototyp.shared.util.Color;
import prototyp.shared.util.events.round.InternalRoundEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.tab.Tab;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.RemoteEventServiceFactory;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;

/**
 * Abstrakte Oberklasse für PreGamGameInitiatorPagePresenter und PreGamePagePresenter
 * 
 * Wichtig: alle public Methoden müssen public bleiben, wegen den Events! -Timo
 * 
 * @author Marcus, Timo (Verantwortlicher), Jannik (Verantwortlicher), Mischa
 * @version 1.0
 * @version 1.1 Im ListGrid werden jetzt Player und Watcher angezeigt. -Timo
 * @version 1.2 Farbauswahl ist jetzt möglich. -Timo
 * @version 1.3 drawRobots verändert
 * 
 * @see PreGameAbstractPage
 * 
 */
public abstract class PreGamePresenter implements PagePresenter {

	/** Presenter für den Chat */
	protected ChatPresenter chatPresenter;

	/** Domain für das Observer-Pattern der Round */
	protected final Domain DOMAIN;

	/** die zugehörende Page */
	protected PreGameAbstractPage page;

	/** PlayerListe für die Tabelle */
	protected RecordList playerData;

	/** Beobachter oder was?? */
	protected int playerWatcherOrGameinitiator;

	/** die Runde die für den Presenter */
	protected RoundInfo round;

	/** RoundManager */
	protected final RoundManagerServiceAsync roundManagerService;

	/** EventService */
	protected RemoteEventService theEventService;

	/** Factory für den EventService */
	protected RemoteEventServiceFactory theEventServiceFactory;

	/** Alle Player der Runde */
	protected Map<Integer, Player> allPlayers;

	/** Alle Watcher der Runde */
	protected Map<Integer, Watcher> allWatchers;

	/** UserID damit diese im Event abgefragt werden kann. */
	protected int userID;

	/** Speichert den Tab */
	protected Tab tab;

	/** Beinhaltet alle auswählbaren Farben. */
	private LinkedHashMap<String, String> availableColorsMap;

	/** Preview für den eigenen Roboter */
	private Img roboterPreviewImage;

	/** Stack für das Zeichnen des Roboters */
	private HorizontalStack roboStack;

	/**
	 * Konstruktor
	 * 
	 * @param round
	 *            RoundInfo der ausgewählten Runde
	 */
	public PreGamePresenter(RoundInfo round) {
		// UserID setzen, damit diese im Event abgefragt werden kann.
		this.userID = UserPresenter.getInstance().getUser().getId();

		// Am Anfang ist noch kein Roboter PreviewImage gesetzt
		this.roboterPreviewImage = null;

		// Round zuweise
		this.round = round;

		this.allPlayers = new HashMap<Integer, Player>();
		this.allWatchers = new HashMap<Integer, Watcher>();

		// Domain(s) erstellen
		this.DOMAIN = DomainFactory.getDomain("round_" + round.getRoundId());

		// Services erzeugen
		this.roundManagerService = GWT.create(RoundManagerService.class);

		// EventService um die Round neu zu laden
		this.theEventServiceFactory = RemoteEventServiceFactory.getInstance();
		this.theEventService = this.theEventServiceFactory.getRemoteEventService();
	}

	/**
	 * Fügt die Listener hinzu (abstrakt)
	 * 
	 * @return true, falls alles geklappt hat
	 */
	protected abstract boolean addListeners();

	/**
	 * Fügt den EventListener für die RoundEvents hinzu.
	 * 
	 * @author timo
	 * @version 1.0
	 * 
	 * @return true, falls alles geklappt hat
	 */
	protected boolean addRoundListener() {

		this.theEventService.addListener(this.DOMAIN, new RemoteEventListener() {

			@Override
			public void apply(Event anEvent) {

				if (anEvent instanceof InternalRoundEvent) {
					((InternalRoundEvent) anEvent).apply(PreGamePresenter.this);
				}

			}
		});

		return true;
	}

	/**
	 * Schließt den Tab. Wird u.a. gebraucht wenn man gekickt wird oder der GI das PreGame verlässt.
	 * 
	 * @return
	 * @param Nachricht
	 *            , die angezeigt werden soll
	 */
	public boolean closeTab(String message) {
		// Listener löschen
		this.theEventService.removeListeners(this.DOMAIN);
		// RoundID aus dem UserPresenter austragen
		UserPresenter.getInstance().deleteRound(this.round.getRoundId());

		// Nachricht anzeigen
		SC.say(message);

		// Tab schließen
		TabManager.getInstanceMain().removeTab(this.tab);

		return true;
	}

	/**
	 * Falls eine Color ausgewählt wurde, wird das entsprechende Roboter Bild gezeichnet.
	 * 
	 * @author timo
	 * @version 1.0
	 * @return true falls eine Color gesetzt, false falls nicht
	 */
	private boolean drawRobotPreviewPicture() {
		this.roboStack = this.page.getRobotorStack();
		/*
		 * Roboter Bild für den richtigen Player prüfen, falls es keines gibt return false bzw. Random wählen
		 */
		if (this.allPlayers.get(UserPresenter.getInstance().getUser().getId()) == null
				|| this.allPlayers.get(UserPresenter.getInstance().getUser().getId()).getRobot().getColor() == null) {

			if (this.roboterPreviewImage != null) {
				this.page.getRobotorStack().removeMember(this.roboterPreviewImage);
				this.page.getPlayerOptionsArea().removeMember(this.roboStack);
			}

			this.roboterPreviewImage = null;

			return false;
		}

		/*
		 * Falls bereits eines gezeichnet wurde -> entfernen
		 */
		if (this.roboterPreviewImage != null) {
			this.page.getRobotorStack().removeMember(this.roboterPreviewImage);
			this.page.getPlayerOptionsArea().removeMember(this.roboStack);
		}

		/*
		 * Jetzt das neue Image setzen (damit es später gegebenen falls gelöscht werden kann).
		 */
		this.roboterPreviewImage = this.allPlayers.get(UserPresenter.getInstance().getUser().getId()).getRobot().getColor()
				.getRobotPicture();

		/*
		 * Jetzt das neue Image auf die Area zeichen.
		 */
		this.roboStack.addMember(this.roboterPreviewImage);
		this.page.getPlayerOptionsArea().addMember(this.roboStack);

		return true;
	}

	/**
	 * Liefert die Map mit allen Playern zurück. Wird von den InternalRoundEvents verwendet um dies upzudaten.
	 * 
	 * @author timo
	 * @version 1.0
	 * 
	 * @return Map<Integer, Player>
	 */
	public Map<Integer, Player> getAllPlayers() {
		return this.allPlayers;
	}

	/**
	 * Liefert die Map mit allen Watchern zurück. Wird von den InternalRoundEvents verwendet um dies upzudaten.
	 * 
	 * @author timo
	 * @version 1.0
	 * 
	 * @return
	 */
	public Map<Integer, Watcher> getAllWatchers() {
		return this.allWatchers;
	}

	public ChatPresenter getChatPresenter() {
		return this.chatPresenter;
	}

	/**
	 * Liefert die Page
	 * 
	 * @return page
	 */
	@Override
	public Canvas getPage() {
		return this.page;
	}

	/**
	 * Zeigt ob jemand GI, Player oder Watcher ist.
	 * 
	 * @return 0 = GI, 1 = player, 2 = watcher
	 */
	public int getPlayerWatcherOrGameInitiator() {
		return this.playerWatcherOrGameinitiator;
	}

	/**
	 * @return the round
	 */
	public RoundInfo getRound() {
		return this.round;
	}

	/**
	 * Gibt den Tab zurück.
	 * 
	 * @return
	 */
	public Tab getTab() {
		return this.tab;
	}

	/**
	 * Gibt die UserID zurück. Wird u.a. vom RemovePlayerWatcherRoundEvent benötigt.
	 * 
	 * @return
	 */
	public int getUserID() {
		return this.userID;
	}

	/**
	 * Liefert alle User der Aktuellen Round. Also Watcher so wie Player. Wird für das PlayerListGrid gebraucht.
	 * 
	 * @author timo
	 * @version 1.0
	 * 
	 * @return Map<Integer, Watcher>, wobei Watcher auch Player sein können!
	 */
	private Map<Integer, Watcher> getUsersInRound() {
		// Player / Watcher in eine Map schreiben
		Map<Integer, Watcher> allUsers = new HashMap<Integer, Watcher>();

		// Alle Player
		for (Integer key : this.allPlayers.keySet()) {
			allUsers.put(key, this.allPlayers.get(key));
		}
		// alle Watcher
		for (Integer key : this.allWatchers.keySet()) {
			allUsers.put(key, this.allWatchers.get(key));
		}

		return allUsers;
	}

	/**
	 * Ändert die möglichen Farben, die ein Player auswählen kann. Wird von addRoundListener und vom PreGamePagePresenter (nur für
	 * Player) und PreGameGameInitiatorPresenter aufgerufen..
	 * 
	 * @author timo
	 * @version 1.0
	 * @version 1.1 jetzt Public, damit es vom ChancePlayerColorEvent aufgerufen werden kann -Timo
	 * @version 1.2 ruft drawRobotPreviewPicture auf, damit es zum richtigen Zeitpunkt gezeichnet werden kann. (wenn das Event
	 *          setAvailableColors ändert)
	 * @return true, falls alles geklappt hat.
	 */
	public boolean setAvailableColors() {

		// Map mit möglichen Farben erstellen:
		this.availableColorsMap = new LinkedHashMap<String, String>();
		// Map für die Vorschaubilder
		LinkedHashMap<String, String> tmpIconMap = new LinkedHashMap<String, String>();

		// Verfügbare Farben ermitteln und Maps damit füllen
		// Alle vergebenen Farben ermitteln:
		List<Color> tmpColorList = new ArrayList<Color>();
		// Fragezeichen hinzufügen
		this.availableColorsMap.put("random", Page.props.preGamePagePresenter_randomColor());
		tmpIconMap.put("random", "icon-Fragezeichen");
		// Alle anderen Farben ermitteln
		for (Integer key : this.allPlayers.keySet()) {
			if (this.allPlayers.get(key).getRobot().getColor() != null) {
				tmpColorList.add(this.allPlayers.get(key).getRobot().getColor());
			}
		}
		// Vergebene Farben aus allen Farben abziehen
		final List<Color> colorList = new ArrayList<Color>(Arrays.asList(Color.values()));
		colorList.remove(Color.WHITE);
		for (final Color color : colorList) {
			// Wenn nicht vergeben -> rein
			if (!tmpColorList.contains(color)) {
				this.availableColorsMap.put(color.name(), color.getColorName());
				tmpIconMap.put(color.name(), color.getColorPreviewName());
			}
		}

		// Falls eine Farbe ausgewählt wurde -> ebenfalls rein (damit diese
		// angezeigt werden kann)
		if (this.allPlayers.get(UserPresenter.getInstance().getUser().getId()) != null
				&& this.allPlayers.get(UserPresenter.getInstance().getUser().getId()).getRobot() != null
				&& this.allPlayers.get(UserPresenter.getInstance().getUser().getId()).getRobot().getColor() != null) {

			// Farbe
			this.availableColorsMap.put(this.allPlayers.get(UserPresenter.getInstance().getUser().getId()).getRobot().getColor()
					.name(), this.allPlayers.get(UserPresenter.getInstance().getUser().getId()).getRobot().getColor()
					.getColorName());

			// Preview Image
			tmpIconMap.put(this.allPlayers.get(UserPresenter.getInstance().getUser().getId()).getRobot().getColor().name(),
					this.allPlayers.get(UserPresenter.getInstance().getUser().getId()).getRobot().getColor()
							.getColorPreviewName());
		}

		// Maps setzen
		this.page.getColorChooser().setValueMap(this.availableColorsMap);
		// Prefix/Suffix ist bei allen gleich
		this.page.getColorChooser().setImageURLPrefix("colorpreview/");
		this.page.getColorChooser().setImageURLSuffix(".png");
		this.page.getColorChooser().setValueIcons(tmpIconMap);

		// Robot zeichnen (falls einer gewählt wurde)
		drawRobotPreviewPicture();

		return true;
	}

	/**
	 * Handler für die Combobox der Farbauswahl. Sollte gesetzt werden nach dem Player/Watcher geadded wurden. Für Random wird
	 * null gesendet
	 * 
	 * @author timo
	 * @version 1.0
	 * 
	 * @return true, falls alles geklappt hat.
	 */
	protected boolean setColorChooseListener() {
		this.page.getColorChooser().addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {

				if (PreGamePresenter.this.page.getColorChooser().getDisplayValue() != null) {

					// Farbe aus der Map suchen
					Color tmpColor = null;
					// Nur wenn es nicht Random ist, sonst bleibt es null
					if (!PreGamePresenter.this.page.getColorChooser().getDisplayValue()
							.equals(Page.props.preGamePagePresenter_randomColor())) {
						for (String key : PreGamePresenter.this.availableColorsMap.keySet()) {
							if (PreGamePresenter.this.availableColorsMap.get(key).equals(
									PreGamePresenter.this.page.getColorChooser().getDisplayValue())) {
								tmpColor = Color.valueOf(key);
								break;
							}
						}
					}

					// Falls genau die Color schon selbst gewählt -> keine
					// Serveranfrage!
					if (PreGamePresenter.this.allPlayers.get(UserPresenter.getInstance().getUser().getId()).getRobot() != null
							|| PreGamePresenter.this.allPlayers.get(UserPresenter.getInstance().getUser().getId()).getRobot()
									.getColor() != null
							|| !PreGamePresenter.this.allPlayers.get(UserPresenter.getInstance().getUser().getId()).getRobot()
									.getColor().equals(tmpColor)) {

						PreGamePresenter.this.roundManagerService.setPlayerColor(
								new Player(UserPresenter.getInstance().getUser()), PreGamePresenter.this.round.getRoundId(),
								tmpColor, new AsyncCallback<Boolean>() {
									@Override
									public void onFailure(Throwable caught) {
										// Ausgeben
										SC.say(Page.props.global_title_error(), caught.getMessage());
									}

									@Override
									public void onSuccess(Boolean result) {
										// Nichts
									}
								});
					}
				}
			}
		});

		return true;
	}

	/**
	 * Setzt das Preview rechts
	 * 
	 * @author Jannik, Timo
	 * @param roundID
	 *            die ID müsste von der RefereePage oder der Lobby übergeben werden. (Andy ist dafür verantwortlich xD)
	 * @version 1.0 (20.9.10)
	 * @version 1.1 (22.9.10) getGameInfo und updateGameInfo zu einer Methode (setPreview) vereint, damit diese vergleichbar zur
	 *          Lobby ist --Jannik
	 * @version 1.2 Macht jetzt keine Serveranfrage. Die Daten sind schon seit der Lobby bekannt. -Timo 02.11.10
	 * 
	 * @see {@link LobbyPagePresenter#setPreview}
	 * @see {@link PreGamePagePresenter#setPreview}
	 */
	protected void setPreview() {
		PreGamePresenter.this.page.getHeadline().setContents(
				PreGamePresenter.this.page.getTitle() + " - " + this.round.getRoundOption().getGameName());

		// Preview verändern
		PreGamePresenter.this.page.getPreviewArea().fillWithContent(this.round);
	}

	/**
	 * Setzt den Tab
	 * 
	 * @param tab
	 * @return true
	 */
	public boolean setTab(Tab tab) {
		this.tab = tab;
		return true;
	}

	/**
	 * Zeigt die Player in der Tabelle an.
	 * 
	 * @param roundList
	 *            List mit Rounds
	 * @return true, falls alles geklappt hat.
	 */
	public boolean showPlayersAndWatchersInList() {
		final Map<Integer, Watcher> roundList = getUsersInRound();
		if (roundList == null) {
			return false;
		}

		// Alte löschen
		this.playerData = new RecordList();
		for (final Integer key : roundList.keySet()) {
			final Watcher watcher = roundList.get(key);
			this.playerData.add(new PlayerRecord(key, watcher));
		}

		// Anzeigen
		this.page.getMenueGrid().setData(this.playerData);

		return true;
	}
}
