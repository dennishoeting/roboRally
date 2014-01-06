package prototyp.client.presenter.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.properties.PropertiesEn;
import prototyp.client.service.LobbyService;
import prototyp.client.service.LobbyServiceAsync;
import prototyp.client.util.HighScoreRecord;
import prototyp.client.util.StandardLabel;
import prototyp.client.util.StatisticPreferencesRecord;
import prototyp.client.view.Page;
import prototyp.client.view.lobby.HighScorePage;
import prototyp.shared.useradministration.Award;
import prototyp.shared.useradministration.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;

/**
 * Der Presenter zur HighScorePage
 * 
 * @author Robert (Verantwortlicher)
 * @version 1.0
 * 
 * @see HighScorePage
 */
public class HighScorePagePresenter implements PagePresenter {
	/** Alle User aus der Datenbank */
	private Map<Integer, User> allUsers = null;

	/** Async-Objekt */
	private final LobbyServiceAsync lobbyService;

	/** Zugehörige Page */
	private final HighScorePage page;

	/** User, der rechts angezeigt werden soll */
	private User tmpUser;

	private RecordList statisticData;

	/** Konstruktor */
	public HighScorePagePresenter() {
		this.lobbyService = GWT.create(LobbyService.class);

		this.page = new HighScorePage();

		// Fügt die Listener hinzu
		addListeners();
	}

	/**
	 * Fügt die Listener hinzu
	 * 
	 * @return true
	 */
	private boolean addListeners() {

		// Listener für SingleSelection
		this.page.getHighScoreGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

			@Override
			public void onSelectionChanged(SelectionEvent event) {
				if (HighScorePagePresenter.this.page.getHighScoreGrid().getSelectedRecord() != null) {
					// User ID holen
					final int id = HighScorePagePresenter.this.page.getHighScoreGrid().getSelectedRecord().getAttributeAsInt("userID");

					// Dazu gehörigen User suchen
					HighScorePagePresenter.this.tmpUser = HighScorePagePresenter.this.allUsers.get(id);
					// Bild Path holen
					HighScorePagePresenter.this.lobbyService.getUserPicture(id, new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							// Passiert nichts
						}

						@Override
						public void onSuccess(String result) {
							HighScorePagePresenter.this.fillWithContent(HighScorePagePresenter.this.tmpUser, result);
						}

					});

				}
			}
		});

		// Listener für die Suche
		this.page.getSearchUserItem().addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				if (HighScorePagePresenter.this.allUsers != null) {
					if (HighScorePagePresenter.this.page.getSearchUserItem().getValue() == null) {
						showUsersInList(HighScorePagePresenter.this.allUsers);
					} else {
						// Neue HashMap
						Map<Integer, User> tmpUsers = new HashMap<Integer, User>();
						// Welcher Nickname fängt mit dem Suchmuster an?
						for (Integer key : HighScorePagePresenter.this.allUsers.keySet()) {
							User user = HighScorePagePresenter.this.allUsers.get(key);
							if (user.getAccountData()
									.getNickname()
									.toLowerCase()
									.startsWith(
											HighScorePagePresenter.this.page.getSearchUserItem().getValue().toString()
													.toLowerCase())) {
								tmpUsers.put(user.getId(), user);
							}
						}
						// HighScoreGrid neu füllen
						showUsersInList(tmpUsers);
					}
				}
			}

		});

		return true;
	}

	/**
	 * Füllt die EnemiesProfilePage mit Informationen zum angewählten Nutzer
	 * 
	 * @param user
	 *            Ausgewählter Nutzer
	 * @param userPicture
	 *            Dateipfad der Bilddatei
	 */
	public boolean fillWithContent(final User user, String userPicture) {
		// Altes Bild löschen
		this.page.getPictureArea().removeMembers(this.page.getPictureArea().getMembers());

		// Bild mit dem Rang setzen
		final Img userImg = new Img(userPicture, 200, 200);

		// Rang auf das Bild setzen
		if (user != null) {
			final Img rang = new Img("ranks/" + user.getAccountData().getStatistic().getUserRang() + ".png", 32, 60);
			rang.setLeft(7);
			rang.setTop(7);
			userImg.addChild(rang);
		}

		this.page.getPictureArea().addMember(userImg);

		// Füllt die Statistiktabelle
		statisticData = new RecordList();
		if (user != null) {
			// 1. Die allgemeine Statistik
			statisticData.add(new StatisticPreferencesRecord(1, Page.props.statistic_highscorerank_title(), user.getAccountData()
					.getStatistic().getHighScoreRang()));
			statisticData.add(new StatisticPreferencesRecord(1, Page.props.statistic_rank_title(), user.getAccountData()
					.getStatistic().getUserRang()));
			statisticData.add(new StatisticPreferencesRecord(1, Page.props.statistic_playedgames_title(), user.getAccountData()
					.getStatistic().getPlayedGames()));
			statisticData.add(new StatisticPreferencesRecord(1, Page.props.statistic_wins_title(), user.getAccountData()
					.getStatistic().getWins()));
			statisticData.add(new StatisticPreferencesRecord(1, Page.props.statistic_lostgames_title(), user.getAccountData()
					.getStatistic().getLostGames()));
			statisticData.add(new StatisticPreferencesRecord(1, Page.props.statistic_abortedgames_title(), user.getAccountData()
					.getStatistic().getAbortedGames()));
			statisticData.add(new StatisticPreferencesRecord(1, Page.props.statistic_points_title(), user.getAccountData()
					.getStatistic().getPoints()));
			page.getStatisticListGrid().setData(statisticData); // Anzeigen

			// Letzte Karten holen
			HighScorePagePresenter.this.lobbyService.getLastMaps(user.getId(), new AsyncCallback<ArrayList<String>>() {
				@Override
				public void onFailure(Throwable caught) {
					SC.say(Page.props.profilePage_title(), caught.getMessage());
				}

				@Override
				public void onSuccess(ArrayList<String> result) {
					// 2. Die letzten Karten anzeigen
					if (result != null && result.size() > 0) {
						// Altes löschen
						page.getStatisticListGrid().setData(new RecordList());

						if (result.get(0) != null) {
							statisticData.add(new StatisticPreferencesRecord(2, Page.props.statistic_playingboard_title() + " 1",
									result.get(0)));
						}
						if (result.size() > 1 && result.get(1) != null) {
							statisticData.add(new StatisticPreferencesRecord(2, Page.props.statistic_playingboard_title() + " 2",
									result.get(1)));
						}
						if (result.size() > 2 && result.get(2) != null) {
							statisticData.add(new StatisticPreferencesRecord(2, Page.props.statistic_playingboard_title() + " 3",
									result.get(2)));
						}

						// Neue Statistik anzeigen
						page.getStatisticListGrid().setData(statisticData);
					}
				}

			});
		}

		// Statistik anzeigen
		this.page.getStatisticListGrid().setData(statisticData);

		// Awards
		if (user != null) {
			String groupTitle;
			if(user.getAccountData().getNickname().toLowerCase().endsWith("s")
					|| user.getAccountData().getNickname().toLowerCase().endsWith("x")
					|| user.getAccountData().getNickname().toLowerCase().endsWith("z")) {			
				groupTitle = user.getAccountData().getNickname() +  "' " + Page.props.enemiesProfile_title_awardsArea();
			} else if(Page.props instanceof PropertiesEn){
				groupTitle = user.getAccountData().getNickname() +  "'s " + Page.props.enemiesProfile_title_awardsArea();
			} else {
				groupTitle = user.getAccountData().getNickname() +  "s " + Page.props.enemiesProfile_title_awardsArea();
			}
					
			this.page.getMainAwardsArea().setGroupTitle(groupTitle);
			this.lobbyService.getUserAwards(user.getId(), new AsyncCallback<List<Award>>() {
				@Override
				public void onFailure(Throwable caught) {
					// Alles löschen
					HighScorePagePresenter.this.page.getAwardsArea(0).removeMembers(
							HighScorePagePresenter.this.page.getAwardsArea(0).getMembers());
					HighScorePagePresenter.this.page.getAwardsArea(1).removeMembers(
							HighScorePagePresenter.this.page.getAwardsArea(1).getMembers());
					HighScorePagePresenter.this.page.getAwardsArea(2).removeMembers(
							HighScorePagePresenter.this.page.getAwardsArea(2).getMembers());

					// Hinweis anzeigen, dass noch keine Awards da sind.
					StandardLabel label = new StandardLabel(Page.props.profilePagePresenter_noAwards_text());
					HighScorePagePresenter.this.page.getAwardsArea(0).addMember(label);
				}

				@Override
				public void onSuccess(List<Award> result) {
					if (result != null) {
						// Alles löschen
						HighScorePagePresenter.this.page.getAwardsArea(0).removeMembers(
								HighScorePagePresenter.this.page.getAwardsArea(0).getMembers());
						HighScorePagePresenter.this.page.getAwardsArea(1).removeMembers(
								HighScorePagePresenter.this.page.getAwardsArea(1).getMembers());
						HighScorePagePresenter.this.page.getAwardsArea(2).removeMembers(
								HighScorePagePresenter.this.page.getAwardsArea(2).getMembers());

						// Neu einfügen
						if (result.size() > 0) {
							for (int i = 0; i < result.size(); i++) {
								Img temp = new Img(Award.IMAGE_PATH + result.get(i).getImageFileName());
								temp.setTooltip(result.get(i).getName() + ": " + result.get(i).getDescription());
								temp.setSize("65", "65");
								HighScorePagePresenter.this.page.getAwardsArea(i / 6).addMember(temp);
							}
						} else {
							// Hinweis anzeigen, dass noch keine Awards da sind.
							StandardLabel label = new StandardLabel(Page.props.profilePagePresenter_noAwards_text());
							HighScorePagePresenter.this.page.getAwardsArea(0).addMember(label);
						}
					}
				}
			});
		} else {
			// Default-Einstellungen zeigen
			this.page.getMainAwardsArea().setGroupTitle(Page.props.global_title_awards());
			
			// Alles löschen
			HighScorePagePresenter.this.page.getAwardsArea(0).removeMembers(
					HighScorePagePresenter.this.page.getAwardsArea(0).getMembers());
			HighScorePagePresenter.this.page.getAwardsArea(1).removeMembers(
					HighScorePagePresenter.this.page.getAwardsArea(1).getMembers());
			HighScorePagePresenter.this.page.getAwardsArea(2).removeMembers(
					HighScorePagePresenter.this.page.getAwardsArea(2).getMembers());

			// Hinweis anzeigen, dass noch keine Awards da sind.
			StandardLabel label = new StandardLabel(Page.props.profilePagePresenter_showAwards_text());
			HighScorePagePresenter.this.page.getAwardsArea(0).addMember(label);
		}

		return true;
	}

	/**
	 * Liefert die zugehörige Page
	 */
	@Override
	public Canvas getPage() {
		return this.page;
	}

	/**
	 * Holt alle User mit Statistik aus der DB, füllt das HighScoreGrid mit den Daten und setzt sie als allUsers.
	 * 
	 * @return true, immer
	 */
	public boolean getUsersFromDBAndFillHighScoreGrid() {
		// User aus der DB holen und in die HighScoreGrid damit
		this.lobbyService.getUsersWithStatistic(new AsyncCallback<Map<Integer, User>>() {

			@Override
			public void onFailure(Throwable caught) {
				// Leere linke Seite anzeigen
				page.getHighScoreGrid().setData(new RecordList());

				// Rechte Seite Default-Einstellungen zeigen
				HighScorePagePresenter.this.fillWithContent(null, "userphotos/default.png");
			}

			@Override
			public void onSuccess(Map<Integer, User> result) {
				if (result != null && result.size() > 0) {
					HighScorePagePresenter.this.allUsers = result;

					// Daten ins HighScoreGrid einfügen
					showUsersInList(HighScorePagePresenter.this.allUsers);
				}
			}
		});
		return true;
	}

	/**
	 * Fügt die übergeben Daten in das HighScoreGrid ein
	 * 
	 * @return true, immer
	 */
	private boolean showUsersInList(Map<Integer, User> tmpUsers) {
		HighScoreRecord tmpRecord[] = new HighScoreRecord[tmpUsers.size()];
		int i = 0;
		for (final User user : tmpUsers.values()) {
			tmpRecord[i] = new HighScoreRecord(user);
			i++;
		}

		// Record ins ListGrid
		this.page.getHighScoreGrid().setData(tmpRecord);

		return true;
	}
}
