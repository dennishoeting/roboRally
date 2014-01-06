package prototyp.client.view.postgame;

import prototyp.client.util.Button;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

/**
 * PostGamePage. Hier befindet sich alles, was für die visuelle Darstellung wichtig ist.
 * 
 * @author Tim (Verantwortlicher), Andreas
 * @version 1.0
 */
public class PostGamePage extends Page {
	// Attribute
	private Button buttonCloseTab; // Schließen Button
	private VerticalStack mainArea; // Einteilung in Bereiche
	private HorizontalStack topArea, bottomArea, chatArea, statisticsArea, awardsArea, buttonsArea; // Einteilung in Bereiche
	private ListGridField colorGridField, playerGridField, hitsGridField, deathsGridField, pointsGridField, awardIconField,
			awardNameField, awardDescriptionField, winnerNameField, rotationsGridField, hitOthersGridField, stepsGridField,
			numberOfReachedCheckpointsGridField, numberOfRepairsGridField, numberOfRobotsPushedInHoleGridField; // Table wird
																												// erstellt
	private ListGrid statisticsGrid, awardsGrid;

	/**
	 * Konstruktor
	 */
	public PostGamePage() {
		super(Page.props.postGamePage_title());

		// Main-Area
		this.mainArea = new VerticalStack(0, 0);
		this.topArea = new HorizontalStack(Integer.valueOf(Page.props.global_marginBetweenStackAreas()), 0);
		this.bottomArea = new HorizontalStack(Integer.valueOf(Page.props.global_marginBetweenStackAreas()), 0);
		this.bottomArea.setAlign(Alignment.CENTER);
		this.awardsArea = new HorizontalStack(Page.props.global_title_awards());
		this.awardsArea.setStyleName("postGameArea");
		this.statisticsArea = new HorizontalStack(Page.props.global_title_statistics());
		this.statisticsArea.setStyleName("postGameArea");
		this.statisticsArea.setHeight(240);
		this.statisticsArea.setWidth(950);
		this.chatArea = new HorizontalStack(Page.props.global_title_chat());
		this.chatArea.setWidth(440);
		this.chatArea.setHeight(150);
		this.chatArea.setStyleName("postGameArea");
		this.buttonsArea = new HorizontalStack(Integer.valueOf(Page.props.global_marginBetweenStackAreas()), 0);
		this.buttonsArea.setAlign(Alignment.CENTER);
		this.buttonsArea.setHeight(30);
		this.buttonsArea.setWidth(960);

		// Statistik erstellen
		this.statisticsGrid = new ListGrid();
		this.statisticsGrid.setWidth(930);
		this.statisticsGrid.setHeight(220);
		this.statisticsGrid.setWrapCells(true);
		this.statisticsGrid.setFixedRecordHeights(false);
		this.statisticsGrid.setSortDirection(SortDirection.DESCENDING);
		this.statisticsGrid.setSortField("points");
		this.playerGridField = new ListGridField("nickname", Page.props.preGamePage_nameListGridField());
		this.colorGridField = new ListGridField();
		this.colorGridField.setName("color");
		this.colorGridField.setTitle(" ");
		this.colorGridField.setType(ListGridFieldType.IMAGE);
		this.colorGridField.setWidth(30);
		this.hitsGridField = new ListGridField("hits", Page.props.postGamePage_hitsListGrid());
		this.deathsGridField = new ListGridField("deaths", Page.props.postGamePage_deathsGridField());
		this.pointsGridField = new ListGridField("points", Page.props.statistic_points_title());
		this.rotationsGridField = new ListGridField("rotations", Page.props.postGamePage_rotationsGridField());
		this.hitOthersGridField = new ListGridField("hitOthers", Page.props.postGamePage_hitOthersGridField());
		this.stepsGridField = new ListGridField("steps", Page.props.postGamePage_stepsGridField());
		this.numberOfReachedCheckpointsGridField = new ListGridField("reachedCheckpoints",
				Page.props.postGamePage_numberOfReachedCheckpointsGridField());
		this.numberOfRepairsGridField = new ListGridField("numberOfRepairs", Page.props.postGamePage_repairsGridField());
		this.numberOfRobotsPushedInHoleGridField = new ListGridField("numberOfRobotsPushedInHole",
				Page.props.postGamePage_robotsPushedInHoleGridField());
		this.statisticsGrid
				.setFields(this.pointsGridField, this.colorGridField, this.playerGridField, this.hitsGridField, this.hitOthersGridField, this.deathsGridField,
						this.rotationsGridField, this.stepsGridField,
						this.numberOfReachedCheckpointsGridField, this.numberOfRepairsGridField,
						this.numberOfRobotsPushedInHoleGridField);

		// AwardsListe erstellen
		this.awardsGrid = new ListGrid();
		this.awardsGrid.setWidth(440);
		this.awardsGrid.setHeight(180);
		this.awardsGrid.setWrapCells(true);
		this.awardsGrid.setFixedRecordHeights(false);
		this.awardIconField = new ListGridField("");
		this.awardIconField.setWidth(25);
		this.awardIconField.setType(ListGridFieldType.IMAGE);
		this.awardIconField.setAlign(Alignment.CENTER);
		this.awardNameField = new ListGridField("awardName", Page.props.postGamePage_awardNameField());
		this.awardNameField.setWidth(100);
		this.awardDescriptionField = new ListGridField("awardDescription", Page.props.postGamePage_awardDescriptionField());
		this.awardDescriptionField.setWidth(150);
		this.winnerNameField = new ListGridField("winnerName", Page.props.postGamePage_winnerNameField());
		this.awardsGrid.setFields(new ListGridField[] { this.awardIconField, this.awardNameField, this.awardDescriptionField,
				this.winnerNameField });

		// Buttons
		this.buttonCloseTab = new Button(Page.props.global_title_close(), 150);

		// Hinzufügen
		this.statisticsArea.addMember(this.statisticsGrid);
		this.buttonsArea.addMembers(this.buttonCloseTab);
		this.awardsArea.addMember(this.awardsGrid);

		this.topArea.addMembers(this.statisticsArea);
		this.bottomArea.addMembers(this.chatArea, this.awardsArea);

		this.mainArea.addMembers(this.topArea, this.bottomArea, this.buttonsArea);
		setMainStack(this.mainArea);

	}

	/**
	 * Liefert das AwardsGrid, hierüber können alle Records angesprochen werden
	 * 
	 * @return awardsGrid
	 */
	public ListGrid getAwardsGrid() {
		return this.awardsGrid;
	}

	/**
	 * Liefert den CloseTab-Button
	 * 
	 * @return CloseTab-Button
	 */
	public Button getButtonCloseTab() {
		return this.buttonCloseTab;
	}

	/**
	 * Liefert die Chat-Area als HorizontalStack
	 * 
	 * @return Chat-Area
	 */
	public HorizontalStack getChatArea() {
		return this.chatArea;
	}

	/**
	 * Liefert das ListGrid für die Statistik
	 * 
	 * @return StatistikListGrid
	 */
	public ListGrid getStatisticsGrid() {
		return this.statisticsGrid;
	}
}
