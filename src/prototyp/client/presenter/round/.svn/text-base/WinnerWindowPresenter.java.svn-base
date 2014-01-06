package prototyp.client.presenter.round;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.TabManager;
import prototyp.client.presenter.postgame.PostGamePagePresenter;
import prototyp.client.presenter.user.UserPresenter;
import prototyp.client.service.RoundManagerService;
import prototyp.client.service.RoundManagerServiceAsync;
import prototyp.client.view.Page;
import prototyp.client.view.round.WinnerWindow;
import prototyp.shared.round.Robot;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Presenter für das WinnerWindow für Spieler und Beobachter
 * 
 * @author Robert, Andreas
 * @version 1.3
 */
public class WinnerWindowPresenter implements PagePresenter {

	/** Zugehöriger Presenter */
	final private RoundPresenterInterface presenter;

	/** RoundManagerService */
	private RoundManagerServiceAsync roundService = GWT.create(RoundManagerService.class);

	/** Zugehörige Page */
	private final WinnerWindow page;

	/** RecordList für das PostGame mit den Awards */
	private RecordList awardsList;

	/** RecordList für das PostGame mit der Statistik */
	private RecordList statisticList;

	/**
	 * Konstruktor
	 * 
	 * @param presenter
	 *            Zugehöriger Presenter
	 * @param winnerName
	 *            Gewinner
	 * @param picture
	 *            Bildpfad vom Gewinner
	 * @param text
	 *            Benutzerdefinierter Text
	 * @param winner
	 *            Robot des Gewinners
	 */
	public WinnerWindowPresenter(final int roundId, final RoundPresenterInterface presenter, final String picture,
			final String winnerName, final String text, final List<Robot> robots, final Robot winner) {

		// RundenTimer stoppen
		if (presenter instanceof RoundPlayerPagePresenter) {
			((RoundPlayerPagePresenter) presenter).getCountdownTimer().cancel();
		}

		// Page
		this.page = new WinnerWindow();

		// Presenter (RoundPlayer.. oder RoundWatcher...)
		this.presenter = presenter;

		// Die Anzeige mit Inhalt füllen
		fillWithContent(winnerName, text, picture);

		UserPresenter.getInstance().deleteRound(roundId);
		
		// Statistik und das Record für die Anzeige erstellen
		final Map<Integer, Integer> stats = initStatistic(robots);

		// Awards und das Record für die Anzeige erstellen
		final Map<Integer, Set<Integer>> awards = initAwards(robots, winner);

		// Nur Spieler können die Statistik speichern
		if (presenter instanceof RoundPlayerPagePresenter) {
			// Überprüfen, ob es überhaupt Roboter gibt, sonst kommt es zur einer Exception
			if (robots != null && robots.size() > 0) {
				// Überprüfen, ob des einen Gewinner gibt
				int winnerID = (winner != null) ? winner.getPlayer().getUser().getId() : 0;

				// Statistik in der Datenbank speichern
				roundService.saveStatisticsInDB(roundId, winnerID, stats, awards, new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						// wird nichts ausgegeben, da es sonst ständig zu einer Fehlermeldung kommt. Nur einer darf nämlich die
						// Statistik speichern.
					}

					@Override
					public void onSuccess(Boolean result) {
						// wird nichts ausgegeben
					}
				});

			} else {
				// Fehlermeldung ausgeben
				SC.say(Page.props.global_title_error(), Page.props.postGamePage_noStatistic_text());
			}
		}

		// Listener hinzufügen
		addListener();
	}

	/**
	 * Fügt die Listener hinzu
	 */
	private void addListener() {
		// Wenn auf Okay geklickt wurde
		this.page.getButtonOkay().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// Zum neuen Tab wechseln
				TabManager.getInstanceMain().switchToTab(
						new PostGamePagePresenter(awardsList, statisticList, WinnerWindowPresenter.this.presenter
								.getChatPresenter()));

				// Chatnachricht schreiben, dass er die Statistik anschaut
				WinnerWindowPresenter.this.presenter.getChatPresenter().sendEnterPostGameMessage(
						UserPresenter.getInstance().getNickname(),
						WinnerWindowPresenter.this.presenter.getChatPresenter().getDomain());

				// Page löschen
				WinnerWindowPresenter.this.page.destroy();
			}
		});
	}

	/**
	 * Füllt den Inhalt der Seite
	 * 
	 * @param picture
	 *            Bildpfad
	 * @param text
	 *            Text
	 * @param winnerName
	 *            Nickname, der gewonnen hat
	 */
	private void fillWithContent(final String winnerName, final String text, final String picture) {
		this.page.setTitle(Page.props.winnerwindow_winner() + " " + winnerName); // Titel
		this.page.getLabelInformation().setContents(winnerName + " " + text + "<br>");
		this.page.getImage().setSrc(picture);
	}

	/**
	 * Berechnet die Awards, zeigt sie an und liefert sie für die Datenbank
	 * 
	 * @return Map<Integer, Set<Integer>> (Schlüssel ist die AwardID, Wert: Set der User IDs)
	 */
	private Map<Integer, Set<Integer>> initAwards(final List<Robot> robots, final Robot winner) {

		/*
		 * Rückgabewert. Schlüssel ist die AwardID, Wert: Liste der User IDs
		 */
		final Map<Integer, Set<Integer>> awards = new HashMap<Integer, Set<Integer>>();

		/*
		 * das ListGridRecordList für den PostgamePresenter
		 */
		this.awardsList = new RecordList();

		/*
		 * aktuelles ListgridRecord
		 */
		ListGridRecord tmp;

		/*
		 * "King of the Hill" Award hinzugügen, falls es einen Gewinner gibt. dieser hat die AwardId = 3
		 */
		if (winner != null) {

			final Set<Integer> winnerSet = new HashSet<Integer>(1);
			winnerSet.add(winner.getPlayer().getUser().getId());
			awards.put(3, winnerSet);

			tmp = new ListGridRecord();
			tmp.setAttribute("", "awards/king_of_the_hill_normal.png");
			tmp.setAttribute("awardName", Page.props.postGamePage_winner());
			tmp.setAttribute("awardDescription", Page.props.postGamePage_winnerDesc());
			tmp.setAttribute("winnerName", winner.getPlayer().getUser().getAccountData().getNickname());
			this.awardsList.add(tmp);
		}

		/*
		 * Schwindelig-Award mit dem Key "1". Zuerst wird die höchste Anzahl der Drehungen ermittelt und dann alle UserIds zur der
		 * AwardId "1" hinzugefügt, die diese Anzahl an Drehungen haben Der Eintrag wird anschließend zur awardsMap hinzugefügt
		 */
		final int maxTurns = Collections.max(robots, new Comparator<Robot>() {
			@Override
			public int compare(Robot o1, Robot o2) {
				return o1.getNumberOfTurns() - o2.getNumberOfTurns();
			}
		}).getNumberOfTurns();
		if (maxTurns > 0) {
			final Set<Integer> schwindeligSet = new HashSet<Integer>();

			tmp = new ListGridRecord();
			tmp.setAttribute("", "awards/schwindelig_normal.png");
			tmp.setAttribute("awardName", Page.props.postGamePage_dizzy());
			tmp.setAttribute("awardDescription", Page.props.postGamePage_dizzyDesc());
			tmp.setAttribute("winnerName", "");

			for (final Robot robot : robots) {
				if (robot.getNumberOfTurns() == maxTurns) {
					schwindeligSet.add(robot.getPlayer().getUser().getId());
					tmp.setAttribute("winnerName", tmp.getAttribute("winnerName")
							+ robot.getPlayer().getUser().getAccountData().getNickname() + ", ");
				}
			}
			tmp.setAttribute("winnerName",
					tmp.getAttribute("winnerName").substring(0, tmp.getAttribute("winnerName").length() - 2));
			this.awardsList.add(tmp);
			awards.put(1, schwindeligSet);
		}

		/*
		 * Opfer-Award mit dem Key "2". Zuerst wird die höchste Anzahl der Tode ermittelt und dann alle UserIds zur der AwardId
		 * "2" hinzugefügt, die diese Anzahl an Toden haben Der Eintrag wird anschließend zur awardsMap hinzugefügt
		 */
		final int maxDeaths = Collections.max(robots, new Comparator<Robot>() {
			@Override
			public int compare(Robot o1, Robot o2) {
				return o1.getNumberOfDeaths() - o2.getNumberOfDeaths();
			}
		}).getNumberOfDeaths();
		if (maxDeaths > 0) {
			final Set<Integer> opferSet = new HashSet<Integer>();

			tmp = new ListGridRecord();
			tmp.setAttribute("", "awards/opfer_normal.png");
			tmp.setAttribute("awardName", Page.props.postGamePage_victim());
			tmp.setAttribute("awardDescription", Page.props.postGamePage_victimDesc());
			tmp.setAttribute("winnerName", "");

			for (final Robot robot : robots) {
				if (robot.getNumberOfDeaths() == maxDeaths) {
					opferSet.add(robot.getPlayer().getUser().getId());
					tmp.setAttribute("winnerName", tmp.getAttribute("winnerName")
							+ robot.getPlayer().getUser().getAccountData().getNickname() + ", ");
				}
			}
			tmp.setAttribute("winnerName",
					tmp.getAttribute("winnerName").substring(0, tmp.getAttribute("winnerName").length() - 2));
			this.awardsList.add(tmp);
			awards.put(2, opferSet);
		}

		/*
		 * Hilfsbedürftig-Award mit dem Key "4". Zuerst wird die höchste Anzahl an Reparaturen ermittelt und dann alle UserIds zur
		 * der AwardId "4" hinzugefügt, die diese Anzahl an Reparaturen haben Der Eintrag wird anschließend zur awardsMap
		 * hinzugefügt
		 */
		final int maxRepairs = Collections.max(robots, new Comparator<Robot>() {
			@Override
			public int compare(Robot o1, Robot o2) {
				return o1.getNumberOfRepairs() - o2.getNumberOfRepairs();
			}
		}).getNumberOfRepairs();
		if (maxRepairs > 0) {
			final Set<Integer> repairSet = new HashSet<Integer>();

			tmp = new ListGridRecord();
			tmp.setAttribute("", "awards/hilfsbeduerftig_normal.png");
			tmp.setAttribute("awardName", Page.props.postGamePage_help());
			tmp.setAttribute("awardDescription", Page.props.postGamePage_helpDesc());
			tmp.setAttribute("winnerName", "");

			for (final Robot robot : robots) {
				if (robot.getNumberOfRepairs() == maxRepairs) {
					repairSet.add(robot.getPlayer().getUser().getId());
					tmp.setAttribute("winnerName", tmp.getAttribute("winnerName")
							+ robot.getPlayer().getUser().getAccountData().getNickname() + ", ");
				}
			}
			tmp.setAttribute("winnerName",
					tmp.getAttribute("winnerName").substring(0, tmp.getAttribute("winnerName").length() - 2));
			this.awardsList.add(tmp);
			awards.put(4, repairSet);
		}

		/*
		 * Karatekid-Award mit dem Key "5". Zuerst wird die höchste Anzahl verschobenen Robotern ermittelt und dann alle UserIds
		 * zur der AwardId "5" hinzugefügt, die diese Anzahl an verschobenen Robotern haben Der Eintrag wird anschließend zur
		 * awardsMap hinzugefügt
		 */
		final int maxPushed = Collections.max(robots, new Comparator<Robot>() {
			@Override
			public int compare(Robot o1, Robot o2) {
				return o1.getNumberOfPushedRobots() - o2.getNumberOfPushedRobots();
			}
		}).getNumberOfPushedRobots();
		if (maxPushed > 0) {
			final Set<Integer> karateSet = new HashSet<Integer>();

			tmp = new ListGridRecord();
			tmp.setAttribute("", "awards/karate_kid_normal.png");
			tmp.setAttribute("awardName", Page.props.postGamePage_karate());
			tmp.setAttribute("awardDescription", Page.props.postGamePage_karateDesc());
			tmp.setAttribute("winnerName", "");

			for (final Robot robot : robots) {
				if (robot.getNumberOfPushedRobots() == maxPushed) {
					karateSet.add(robot.getPlayer().getUser().getId());
					tmp.setAttribute("winnerName", tmp.getAttribute("winnerName")
							+ robot.getPlayer().getUser().getAccountData().getNickname() + ", ");
				}
			}
			tmp.setAttribute("winnerName",
					tmp.getAttribute("winnerName").substring(0, tmp.getAttribute("winnerName").length() - 2));
			this.awardsList.add(tmp);
			awards.put(5, karateSet);
		}

		/*
		 * LordOfTheStep-Award mit dem Key "6". Zuerst wird die höchste Anzahl an Schritten ermittelt und dann alle UserIds zur
		 * der AwardId "6" hinzugefügt, die diese Anzahl an Schritten haben Der Eintrag wird anschließend zur awardsMap
		 * hinzugefügt
		 */
		final int maxSteps = Collections.max(robots, new Comparator<Robot>() {
			@Override
			public int compare(Robot o1, Robot o2) {
				return o1.getNumberOfSteps() - o2.getNumberOfSteps();
			}
		}).getNumberOfSteps();
		if (maxSteps > 0) {
			final Set<Integer> maxStepSet = new HashSet<Integer>();

			tmp = new ListGridRecord();
			tmp.setAttribute("", "awards/lord_of_the_step_normal.png");
			tmp.setAttribute("awardName", Page.props.postGamePage_steps());
			tmp.setAttribute("awardDescription", Page.props.postGamePage_stepsDesc());
			tmp.setAttribute("winnerName", "");

			for (final Robot robot : robots) {
				if (robot.getNumberOfSteps() == maxSteps) {
					maxStepSet.add(robot.getPlayer().getUser().getId());
					tmp.setAttribute("winnerName", tmp.getAttribute("winnerName")
							+ robot.getPlayer().getUser().getAccountData().getNickname() + ", ");
				}
			}
			tmp.setAttribute("winnerName",
					tmp.getAttribute("winnerName").substring(0, tmp.getAttribute("winnerName").length() - 2));
			this.awardsList.add(tmp);
			awards.put(6, maxStepSet);
		}

		/*
		 * Schnecke-Award mit dem Key "7". Zuerst wird die höchste Anzahl an Schritten ermittelt und dann alle UserIds zur der
		 * AwardId "7" hinzugefügt, die diese Anzahl an Schritten haben Der Eintrag wird anschließend zur awardsMap hinzugefügt
		 */
		final int minSteps = Collections.min(robots, new Comparator<Robot>() {
			@Override
			public int compare(Robot o1, Robot o2) {
				return o1.getNumberOfSteps() - o2.getNumberOfSteps();
			}
		}).getNumberOfSteps();
		final Set<Integer> minStepSet = new HashSet<Integer>();

		tmp = new ListGridRecord();
		tmp.setAttribute("", "awards/schnecke_normal.png");
		tmp.setAttribute("awardName", Page.props.postGamePage_snail());
		tmp.setAttribute("awardDescription", Page.props.postGamePage_snailDesc());
		tmp.setAttribute("winnerName", "");

		for (final Robot robot : robots) {
			if (robot.getNumberOfSteps() == minSteps) {
				minStepSet.add(robot.getPlayer().getUser().getId());
				tmp.setAttribute("winnerName", tmp.getAttribute("winnerName")
						+ robot.getPlayer().getUser().getAccountData().getNickname() + ", ");
			}
		}
		tmp.setAttribute("winnerName", tmp.getAttribute("winnerName").substring(0, tmp.getAttribute("winnerName").length() - 2));
		this.awardsList.add(tmp);
		awards.put(7, minStepSet);

		/*
		 * DarkSide-Award mit dem Key "8". Alle, die mehr als einen anderen Roboter in ein Loch geschoben haben, wird der
		 * Darkside-Award verliehen
		 */
		final Set<Integer> darkSideSet = new HashSet<Integer>();

		tmp = new ListGridRecord();
		tmp.setAttribute("winnerName", "");

		for (final Robot robot : robots) {
			if (robot.getNumberOfRobotsPushedInHole() > 0) {
				darkSideSet.add(robot.getPlayer().getUser().getId());
				tmp.setAttribute("winnerName", tmp.getAttribute("winnerName")
						+ robot.getPlayer().getUser().getAccountData().getNickname() + ", ");
			}
		}
		if (darkSideSet.size() > 0) {
			tmp.setAttribute("", "awards/dark_side_normal.png");
			tmp.setAttribute("awardName", Page.props.postGamePage_dark());
			tmp.setAttribute("awardDescription", Page.props.postGamePage_darkDesc());
			tmp.setAttribute("winnerName",
					tmp.getAttribute("winnerName").substring(0, tmp.getAttribute("winnerName").length() - 2));

			this.awardsList.add(tmp);
			awards.put(8, darkSideSet);
		}

		/*
		 * Grillhähnchen-Award mit dem Key "9". Zuerst wird die höchste Anzahl der eingesteckten Lasertreffer ermittelt und dann
		 * alle UserIds zur der AwardId "9" hinzugefügt, die diese Anzahl an Treffern haben Der Eintrag wird anschließend zur
		 * awardsMap hinzugefügt
		 */
		final int maxDamage = Collections.max(robots, new Comparator<Robot>() {
			@Override
			public int compare(Robot o1, Robot o2) {
				return o1.getNumberOfLaserHits() - o2.getNumberOfLaserHits();
			}
		}).getNumberOfLaserHits();
		if (maxDamage > 0) {
			final Set<Integer> maxDamageSet = new HashSet<Integer>();

			tmp = new ListGridRecord();
			tmp.setAttribute("", "awards/grillhaehnchen_normal.png");
			tmp.setAttribute("awardName", Page.props.postGamePage_chickn());
			tmp.setAttribute("awardDescription", Page.props.postGamePage_chicknDesc());
			tmp.setAttribute("winnerName", "");

			for (final Robot robot : robots) {
				if (robot.getNumberOfLaserHits() == maxDamage) {
					maxDamageSet.add(robot.getPlayer().getUser().getId());
					tmp.setAttribute("winnerName", tmp.getAttribute("winnerName")
							+ robot.getPlayer().getUser().getAccountData().getNickname() + ", ");
				}
			}
			tmp.setAttribute("winnerName",
					tmp.getAttribute("winnerName").substring(0, tmp.getAttribute("winnerName").length() - 2));
			this.awardsList.add(tmp);
			awards.put(9, maxDamageSet);
		}

		/*
		 * Megatron-Award mit dem Key "10". Zuerst wird die höchste Anzahl Treffer ermittelt und dann alle UserIds zur der AwardId
		 * "10" hinzugefügt, die diese Anzahl an Treffern haben Der Eintrag wird anschließend zur awardsMap hinzugefügt
		 */
		final int maxHits = Collections.max(robots, new Comparator<Robot>() {
			@Override
			public int compare(Robot o1, Robot o2) {
				return o1.getNumberOfOtherRobotHit() - o2.getNumberOfOtherRobotHit();
			}
		}).getNumberOfOtherRobotHit();
		if (maxHits > 0) {
			final Set<Integer> maxHitsSet = new HashSet<Integer>();

			tmp = new ListGridRecord();
			tmp.setAttribute("", "awards/megatron_normal.png");
			tmp.setAttribute("awardName", Page.props.postGamePage_megaT());
			tmp.setAttribute("awardDescription", Page.props.postGamePage_megaTDesc());
			tmp.setAttribute("winnerName", "");

			for (final Robot robot : robots) {
				if (robot.getNumberOfOtherRobotHit() == maxHits) {
					maxHitsSet.add(robot.getPlayer().getUser().getId());
					tmp.setAttribute("winnerName", tmp.getAttribute("winnerName")
							+ robot.getPlayer().getUser().getAccountData().getNickname() + ", ");
				}
			}
			tmp.setAttribute("winnerName",
					tmp.getAttribute("winnerName").substring(0, tmp.getAttribute("winnerName").length() - 2));
			this.awardsList.add(tmp);
			awards.put(10, maxHitsSet);
		}

		/*
		 * Hamster-Award mit dem Key "11"
		 */
		final Set<Integer> hamsterSet = new HashSet<Integer>();

		tmp = new ListGridRecord();
		tmp.setAttribute("winnerName", "");

		for (final Robot robot : robots) {
			if (robot.isHasAwardHamster()) {
				hamsterSet.add(robot.getPlayer().getUser().getId());
				tmp.setAttribute("winnerName", tmp.getAttribute("winnerName")
						+ robot.getPlayer().getUser().getAccountData().getNickname() + ", ");
			}
		}
		if (hamsterSet.size() > 0) {
			tmp.setAttribute("", "awards/hamster.png");
			tmp.setAttribute("awardName", Page.props.postGamePage_hamster());
			tmp.setAttribute("awardDescription", Page.props.postGamePage_hamsterDesc());
			tmp.setAttribute("winnerName",
					tmp.getAttribute("winnerName").substring(0, tmp.getAttribute("winnerName").length() - 2));
			this.awardsList.add(tmp);
			awards.put(11, hamsterSet);
		}

		/*
		 * Barmherziger Samariter-Award mit dem Key "12"
		 */
		final Set<Integer> samariterSet = new HashSet<Integer>();

		tmp = new ListGridRecord();
		tmp.setAttribute("winnerName", "");

		for (final Robot robot : robots) {
			if (robot.isHasPushedOnRepairfield()) {
				samariterSet.add(robot.getPlayer().getUser().getId());
				tmp.setAttribute("winnerName", tmp.getAttribute("winnerName")
						+ robot.getPlayer().getUser().getAccountData().getNickname() + ", ");
			}
		}
		if (samariterSet.size() > 0) {
			tmp.setAttribute("", "awards/barmherziger_samariter.png");
			tmp.setAttribute("awardName", Page.props.postGamePage_samariter());
			tmp.setAttribute("awardDescription", Page.props.postGamePage_samariter());
			tmp.setAttribute("winnerName",
					tmp.getAttribute("winnerName").substring(0, tmp.getAttribute("winnerName").length() - 2));

			this.awardsList.add(tmp);
			awards.put(12, samariterSet);
		}

		/*
		 * Scrottpresse-Award mit dem Key "13". Alle, die diesen Award erhalten haben werden in die Map gepackt
		 */
		final Set<Integer> presserSet = new HashSet<Integer>();

		tmp = new ListGridRecord();
		tmp.setAttribute("winnerName", "");

		for (final Robot robot : robots) {
			if (robot.isHasRobotBeenPressed()) {
				presserSet.add(robot.getPlayer().getUser().getId());
				tmp.setAttribute("winnerName", tmp.getAttribute("winnerName")
						+ robot.getPlayer().getUser().getAccountData().getNickname() + ", ");
			}
		}
		if (presserSet.size() > 0) {
			tmp.setAttribute("", "awards/reif_fuer_die_schrottpresse.png");
			tmp.setAttribute("awardName", Page.props.postGamePage_press());
			tmp.setAttribute("awardDescription", Page.props.postGamePage_pressDesc());
			tmp.setAttribute("winnerName",
					tmp.getAttribute("winnerName").substring(0, tmp.getAttribute("winnerName").length() - 2));

			this.awardsList.add(tmp);
			awards.put(13, presserSet);
		}

		/*
		 * das AwardSet zurückliefern
		 */
		return awards;
	}

	/**
	 * Erstellt die Statistik, zeigt sie an und liefert sie für die Datenbank
	 * 
	 * @return Map<Integer, Integer> (UserID, Wert: Punktezahl)
	 */
	private Map<Integer, Integer> initStatistic(final List<Robot> robots) {
		// Hilfsvariablen
		statisticList = new RecordList();

		// Schlüssel: UserID, Wert: Punktezahl
		Map<Integer, Integer> statistic = new HashMap<Integer, Integer>();

		// Roboter sortieren
		Collections.sort(robots, new Comparator<Robot>() {
			@Override
			public int compare(Robot o1, Robot o2) {
				return o2.getPoints() - o1.getPoints();
			}
		});

		// Statistik aufbauen
		for (final Robot robot : robots) {
			// Für die Anzeige
			ListGridRecord tmp = new ListGridRecord();
			tmp.setAttribute("color", "colorpreview/" + robot.getColor().getColorPreviewName() + ".png");
			tmp.setAttribute("nickname", robot.getPlayer().getUser().getAccountData().getNickname());
			tmp.setAttribute("hits", robot.getNumberOfLaserHits());
			tmp.setAttribute("deaths", robot.getNumberOfDeaths());
			tmp.setAttribute("points", robot.getPoints());
			tmp.setAttribute("rotations", robot.getNumberOfTurns());
			tmp.setAttribute("hitOthers", robot.getNumberOfOtherRobotHit());
			tmp.setAttribute("steps", robot.getNumberOfSteps());
			tmp.setAttribute("reachedCheckpoints", robot.getNumberOfReachedCheckpoints());
			tmp.setAttribute("numberOfRepairs", robot.getNumberOfRepairs());
			tmp.setAttribute("numberOfRobotsPushedInHole", robot.getNumberOfRobotsPushedInHole());
			
			this.statisticList.add(tmp);

			// Für die DB
			statistic.put(robot.getPlayer().getUser().getId(), robot.getPoints());
		}

		return statistic;
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
}
