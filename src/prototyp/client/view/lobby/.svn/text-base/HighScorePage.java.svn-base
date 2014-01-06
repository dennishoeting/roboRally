package prototyp.client.view.lobby;

import prototyp.client.presenter.lobby.HighScorePagePresenter;
import prototyp.client.util.Form;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.PreferencesHelperDataSource;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

/**
 * Links wird ein ListGrid mit den HighScores angezeigt und rechts die aktuell
 * ausgewählte EnemiesProfilePage
 * 
 * @author Timo, Robert (Verantwortlicher)
 * @version 1.0
 * @version 1.1 Rang und Suche hinzugefügt - 23.09.10 Timo
 * 
 * @see HighScorePagePresenter
 */
public class HighScorePage extends Page {
	// Attribute
	private ListGrid highScoreGrid, statisticListGrid;
	private VerticalStack leftArea, rightArea, mainAwardsArea;
	private HorizontalStack mainArea, profileArea, pictureArea, awardsArea1,
			awardsArea2, awardsArea3;
	private ListGridField nickname, wins, losts, rank, points,
			statisticPreferences, statisticDescription, statisticType;
	private Form searchUserForm;
	private TextItem searchUserItem;

	/**
	 * Konstruktor
	 */
	public HighScorePage() {
		super(Page.props.highScorePage_title());

		this.mainArea = new HorizontalStack(Integer.valueOf(Page.props
				.global_marginBetweenStackAreas()), 0);

		// Linke Seite
		this.leftArea = new VerticalStack(
				Page.props.highScorePage_title_rankList());
		this.leftArea.setStyleName("highscoreArea");
		this.leftArea.setHeight(488);

		// HighScoreGrid
		this.highScoreGrid = new ListGrid();
		this.highScoreGrid.setWidth(420);
		this.highScoreGrid.setHeight(380);

		// ListGridFields
		this.rank = new ListGridField("rank",
				Page.props.highScorePage_highScoreGrid_rank());
		this.rank.setWidth(40);
		this.rank.setAlign(Alignment.CENTER);
		this.nickname = new ListGridField("nickname",
				Page.props.global_title_nickname());
		this.wins = new ListGridField("wins",
				Page.props.highScorePage_highScoreGrid_wins());
		this.wins.setAlign(Alignment.CENTER);
		this.losts = new ListGridField("losts",
				Page.props.highScorePage_highScoreGrid_losts());
		this.losts.setAlign(Alignment.CENTER);
		this.points = new ListGridField("points",
				Page.props.highScorePage_highScoreGrid_points());
		this.points.setAlign(Alignment.CENTER);

		this.highScoreGrid.setFields(new ListGridField[] { this.rank,
				this.nickname, this.wins, this.losts, this.points });

		// HighScoreGrid Einstellungen:
		this.highScoreGrid.setShowAllRecords(true);
		this.highScoreGrid.setCanEdit(false);
		this.highScoreGrid.setCanResizeFields(true);
		this.highScoreGrid.setWrapCells(true);
		this.highScoreGrid.setFixedRecordHeights(false);
		// Anfangs nach Gewonnen absteigend sortieren
		this.highScoreGrid.sort("rank", SortDirection.ASCENDING);

		// SearchUserItem
		this.searchUserForm = new Form();
		this.searchUserForm.setAutoFocus(true);
		this.searchUserItem = new TextItem(
				Page.props.highScorePage_userSearchItem_text());
		this.searchUserItem.setWidth(366);
		this.searchUserForm.setFields(this.searchUserItem);

		this.leftArea.addMembers(this.searchUserForm, this.highScoreGrid);
		this.mainArea.addMember(this.leftArea);

		// Rechte Seite
		this.rightArea = new VerticalStack(
				Page.props.highScorePage_enemiesProfile_title());
		this.rightArea.setHeight(488);
		this.rightArea.setStyleName("highscoreArea");

		// Areas
		this.profileArea = new HorizontalStack();
		this.pictureArea = new HorizontalStack();
		this.pictureArea.setHeight(200);
		this.pictureArea.setWidth(200);
		this.pictureArea.setStyleName("pictureArea");

		// Statistik
		this.statisticListGrid = new ListGrid();
		this.statisticPreferences = new ListGridField("optionTitle",
				Page.props.global_title_preference());
		this.statisticDescription = new ListGridField("optionDescription",
				Page.props.global_title_description());
		this.statisticType = new ListGridField("optionType",
				Page.props.global_title_type());
		this.statisticType.setWidth(100);
		this.statisticDescription.setWidth(80);
		this.statisticListGrid.setWidth(200);
		this.statisticListGrid.setHeight(200);
		this.statisticListGrid.setShowAllRecords(true);
		this.statisticListGrid.setSelectionType(SelectionStyle.SINGLE);
		this.statisticListGrid.setWrapCells(true);
		this.statisticListGrid.setFixedRecordHeights(false);
		this.statisticListGrid.setFields(this.statisticPreferences,
				this.statisticDescription, this.statisticType);
		this.statisticListGrid.setGroupStartOpen(GroupStartOpen.ALL);
		this.statisticListGrid.setGroupByField("optionType");
		this.statisticListGrid.hideField("optionType");
		this.statisticListGrid.setDataSource(new PreferencesHelperDataSource());
		this.statisticListGrid.setShowDetailFields(false);

		// Award-Area initialisieren
		this.mainAwardsArea = new VerticalStack();
		this.mainAwardsArea.setWidth(420);
		this.mainAwardsArea.setIsGroup(true);
		this.mainAwardsArea.setStyleName("innerArea");
		this.awardsArea1 = new HorizontalStack();
		this.awardsArea1.setHeight(65);
		this.awardsArea2 = new HorizontalStack();
		this.awardsArea2.setHeight(65);
		this.awardsArea3 = new HorizontalStack();
		this.awardsArea3.setHeight(65);

		// hinzufügen
		this.profileArea.addMembers(this.pictureArea, this.statisticListGrid);
		this.mainAwardsArea.addMembers(this.awardsArea1, this.awardsArea2,
				this.awardsArea3);
		this.rightArea.addMembers(this.profileArea, this.mainAwardsArea);
		this.mainArea.addMember(this.rightArea);
		setMainStack(this.mainArea);
	}

	/**
	 * Liefert die gewünschte Awards-Area
	 * 
	 * @return Awards-Area 1,2 oder 3 Liefert die Award-Area
	 * 
	 * @return Award-Area
	 */
	public HorizontalStack getAwardsArea(int i) {
		if (i == 0) {
			return this.awardsArea1;
		} else if (i == 1) {
			return this.awardsArea2;
		} else {
			return this.awardsArea3;
		}
	}

	/**
	 * Liefert das HighScoreGrid
	 * 
	 * @return VStack
	 */
	public ListGrid getHighScoreGrid() {
		return this.highScoreGrid;
	}

	/**
	 * Liefert den Rahmen der Awards-Area(s)
	 * 
	 * @return Awards-Area Gibt das Label mit den verlassenen Spielen zurück
	 * 
	 * @return LeftGames
	 */
	public VerticalStack getMainAwardsArea() {
		return this.mainAwardsArea;
	}

	/**
	 * Liefert den HorizontalStack der PictureArea
	 * 
	 * @return PictureArea
	 */
	public HorizontalStack getPictureArea() {
		return this.pictureArea;
	}

	// Getter / Setter
	/**
	 * Liefert das Panel in dem das Gegnerprofil dargestellt wird
	 * 
	 * @return VStack
	 */
	public VerticalStack getRightArea() {
		return this.rightArea;
	}

	/**
	 * Liefert das getSearchUserItem
	 * 
	 * @return VStack
	 */
	public TextItem getSearchUserItem() {
		return this.searchUserItem;
	}

	/**
	 * Liefert das ListGrid für die Statistik-Spezifikationen eines Users
	 * 
	 * @return statisticListGrid Gibt das Label mit den gespielten Spielen
	 *         zurück
	 * 
	 * @return PlayedGames
	 */
	public ListGrid getStatisticListGrid() {
		return this.statisticListGrid;
	}
}