package prototyp.client.presenter.pregame;

import java.util.Map;

import prototyp.client.presenter.TabManager;
import prototyp.client.presenter.chat.ChatPresenter;
import prototyp.client.presenter.user.UserPresenter;
import prototyp.client.view.Page;
import prototyp.client.view.chat.ChatArea;
import prototyp.client.view.lobby.MapPreviewWindow;
import prototyp.client.view.pregame.PreGamePage;
import prototyp.shared.round.Player;
import prototyp.shared.round.RoundInfo;
import prototyp.shared.round.Watcher;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;

/**
 * Definiert das Verhalten der Buttons etc.
 * 
 * @author Dennis, Timo (Verantwortlicher), Jannik (Verantwortlicher), Mischa
 * @version 1.0
 * @version 1.1 (20.9.10) (getGameInfo und updateGameInfo hinzugefügt --Jannik)
 * @version 1.2 (27.9.10) Interner Chat wird jetzt über diesen Presenter geadded -Timo
 * @version 1.3 (21.10.10) Unterscheidung zwischen Watcher/Player -Timo
 * @version 1.4 (26.10.10) Wenn man gekickt wird, wird der Tab geschlossen. -Timo
 * @version 1.5 (30.12.10) Chatarea angepasst - Mischa
 * 
 * @see PreGamePage
 */
public class PreGamePagePresenter extends PreGamePresenter {

	/** Die Domain für die Runde */
	private final Domain DOMAIN;

	/** Flag für den Bereit Button */
	private boolean readyFlag;

	/**
	 * Konstruktor
	 * 
	 * @param round
	 *            RoundInfo von der ausgewählten Runde
	 * @param playerWatcherOrGameinitiator
	 *            ID des Gamesinitators
	 */
	public PreGamePagePresenter(RoundInfo round, int playerWatcherOrGameinitiator) {
		// Super Konstruktor
		super(round);

		// Für den Bereit-Button
		this.readyFlag = false;

		this.playerWatcherOrGameinitiator = playerWatcherOrGameinitiator;

		this.DOMAIN = DomainFactory.getDomain("round_" + round.getRoundId());

		this.page = new PreGamePage();

		// Internen Chat hinzufügen
		this.chatPresenter = new ChatPresenter("internalChatRound:" + round.getRoundId());
		((ChatArea) this.chatPresenter.getPage()).setWidth(338);
		((ChatArea) this.chatPresenter.getPage()).setHeight(211);
		this.page.getInternalChatArea().addMember(this.chatPresenter.getPage());

		super.setPreview();

		// Player/Watcher laden
		PreGamePagePresenter.this.roundManagerService.getPlayersAndWatchers(PreGamePagePresenter.this.round.getRoundId(),
				new AsyncCallback<Map<Integer, Watcher>>() {

					@Override
					public void onFailure(Throwable caught) {
						// Tab schließen
						TabManager.getInstanceChild().closeTab();

						// Ausgeben
						SC.say(Page.props.preGamePage_title(), caught.getMessage());
					}

					@Override
					public void onSuccess(Map<Integer, Watcher> result) {
						// Player/Watcher speichern
						addPlayersWatchers(result);

						// Hier jetzt den RoundListener adden
						addRoundListener();

						// 3) als Player/Watcher adden
						addToPlayersOrWatchers();
					}
				});
	}

	/**
	 * Abbrechen Button, Bereit Button, EventListener
	 * 
	 * @return true
	 */
	@Override
	protected boolean addListeners() {

		// Vorschau vergrößern, wenn auf das Bild geklickt wird
		this.page.getPreviewArea().getPreviewMapImage().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (round != null) {
					final MapPreviewWindow window = new MapPreviewWindow();

					window.show(page.getPreviewArea().getPreviewMapImage(), round.getPlayingboard().getWidth(), round.getPlayingboard()
							.getHeight());
				}
			}
		});

		// "Abbrechen"
		this.page.getButtonCloseTab().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				/*
				 * Egal ob alles klappt hat oder nicht -> Listener werden gelöscht und der Tab geschlossen.
				 */

				// Alle überflüssigen Listener löschen
				PreGamePagePresenter.this.chatPresenter.deleteEventListener();
				PreGamePagePresenter.this.theEventService.removeListeners(PreGamePagePresenter.this.DOMAIN);
				// RoundID aus dem UserPresenter austragen
				UserPresenter.getInstance().deleteRound(PreGamePagePresenter.this.round.getRoundId());

				switch (PreGamePagePresenter.this.playerWatcherOrGameinitiator) {

				case 1: // Player
					PreGamePagePresenter.this.roundManagerService.removePlayer(new Player(UserPresenter.getInstance().getUser()),
							PreGamePagePresenter.this.round.getRoundId(), new AsyncCallback<Boolean>() {
								@Override
								public void onFailure(Throwable caught) {
									// Ausgeben
									SC.say(Page.props.preGamePage_title(), caught.getMessage());
								}

								@Override
								public void onSuccess(Boolean result) {
									// Nichts
								}
							});
					break;
				case 2: // Watcher
					PreGamePagePresenter.this.roundManagerService.removeWatcher(
							new Watcher(UserPresenter.getInstance().getUser()), PreGamePagePresenter.this.round.getRoundId(),
							new AsyncCallback<Boolean>() {
								@Override
								public void onFailure(Throwable caught) {
									// Ausgeben
									SC.say(Page.props.preGamePage_title(), caught.getMessage());
								}

								@Override
								public void onSuccess(Boolean result) {
									// Nichts
								}
							});
					break;
				}

				// Tab schließen
				TabManager.getInstanceMain().closeTab();

			}
		});

		// Bereit
		if (this.playerWatcherOrGameinitiator == 1) {
			((PreGamePage) this.page).getButtonReady().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					PreGamePagePresenter.this.readyFlag = !PreGamePagePresenter.this.readyFlag;
					// Auf Bereit/Nicht Bereit setzen:
					PreGamePagePresenter.this.roundManagerService.setPlayerReady(
							new Player(UserPresenter.getInstance().getUser()), PreGamePagePresenter.this.round.getRoundId(),
							PreGamePagePresenter.this.readyFlag, new AsyncCallback<Boolean>() {

								@Override
								public void onFailure(Throwable caught) {
									// Ausgeben
									SC.say(Page.props.preGamePage_title(), caught.getMessage());
								}

								@Override
								public void onSuccess(Boolean result) {
									if (result) {
										if (PreGamePagePresenter.this.readyFlag) { // Wenn bereit, kann zu nicht bereit gewechselt
																					// werden.
											((PreGamePage) PreGamePagePresenter.this.page).getButtonReady().setTitle(
													Page.props.preGamePagePresenter_ready());
										} else { // Wenn nicht bereit, kann zu bereit gewechselt werden.
											((PreGamePage) PreGamePagePresenter.this.page).getButtonReady().setTitle(
													Page.props.preGamePagePresenter_notReady());
										}
									}
								}
							});
				}
			});
		}
		return true;
	}

	/**
	 * Fügt die Player und Watcher in die richtige Map.
	 * 
	 * @author timo
	 * @version 1.0
	 * @param playersAndWatchers
	 *            HashMap mit Players und Watchers
	 * @return true, falls alles geklappt hat
	 */
	private boolean addPlayersWatchers(Map<Integer, Watcher> playersAndWatchers) {
		for (final Integer key : playersAndWatchers.keySet()) {
			// Player
			final Watcher watcher = playersAndWatchers.get(key);
			if (watcher instanceof Player) {
				this.allPlayers.put(key, ((Player) watcher));
			} else { // Watcher
				this.allWatchers.put(key, watcher);
			}
		}
		return true;
	}

	/**
	 * Fügt den Player/Watcher beim Server hinzu.
	 * 
	 * @return true, falls alles geklappt hat
	 */
	private boolean addToPlayersOrWatchers() {
		switch (this.playerWatcherOrGameinitiator) {
		case 1: // Player
			this.roundManagerService.addPlayer(new Player(UserPresenter.getInstance().getUser()), this.round.getRoundId(),
					new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							// Tab schließen
							TabManager.getInstanceMain().closeTab();

							// Ausgeben
							SC.say(Page.props.preGamePage_title(), caught.getMessage());
						}

						@Override
						public void onSuccess(Boolean result) {
							if (result) {
								// Jetzt die Listener adden
								addListeners();
								// Farbauswahl das erste Mal setzen
								setAvailableColors();
								// Listener für den Farbauswahl Button setzen
								setColorChooseListener();
								// Listgrid setzen
								showPlayersAndWatchersInList();
								// RoundID in den UserPresenter eintragen
								UserPresenter.getInstance().addRound(PreGamePagePresenter.this.round.getRoundId());
							} else { // Solle nie vorkommen, falls doch Tab
										// schließen.
								TabManager.getInstanceMain().closeTab();
							}
						}
					});
			break;
		case 2: // Watcher

			// Für den Watcher sind alle Button/Comboboxes außer Abbrechen
			// deaktiviert.
			((PreGamePage) this.page).getButtonReady().disable(); // BereitButton deaktivieren
			this.page.getPlayerOptionsArea().disable(); // Farbauswahl deaktivieren

			this.roundManagerService.addWatcher(new Watcher(UserPresenter.getInstance().getUser()), this.round.getRoundId(),
					new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							// Tab schließen
							TabManager.getInstanceMain().closeTab();

							// Ausgeben
							SC.say(Page.props.preGamePage_title(), caught.getMessage());
						}

						@Override
						public void onSuccess(Boolean result) {
							if (result) {
								// Jetzt die Listener hinzufügen
								addListeners();
								// ListGrid setzen
								showPlayersAndWatchersInList();
								// RoundID in den UserPresenter eintragen
								UserPresenter.getInstance().addRound(PreGamePagePresenter.this.round.getRoundId());
							} else { // sollte nie vorkommen, falls doch -> Tab
										// schließen
								TabManager.getInstanceMain().closeTab();
							}
						}

					});

			break;
		}

		return true;
	}
}
