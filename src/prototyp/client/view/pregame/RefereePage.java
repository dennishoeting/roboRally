package prototyp.client.view.pregame;

import prototyp.client.util.Button;
import prototyp.client.util.Form;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.PreviewArea;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

/**
 * RefereePage, um eine Spielrunde zu erstellen
 * 
 * @author Andreas, Jannik (Verantwortlicher)
 * @version 1.1
 * @version 1.2 (an MVP angepasst --Jannik)
 * @version 1.3 Preview umgeschrieben --Andreas
 * @version 1.4 Properties hinzugefügt (Marina)
 * @version 1.5 Validieren hinzugefügt
 */
public class RefereePage extends Page {
	// Attribute
	private final Button buttonCloseTab, buttonAbort;
	private final HorizontalStack contentArea, buttonArea, optionsArea;
	private final ListGridField idListGridField, nameListGridField, sizeListGridField, numberOfCheckpointsListGridField,
			maxPlayersListGridField;
	private final CheckboxItem lasersOn, robotShootsOn, reduceCountdown, alwaysPushersOn, alwaysCompactorsOn, alwaysCBOn, alwaysGearsOn,
			allowObservers;
	private final TextItem gameName;
	private final VerticalStack mainArea, leftArea, listArea;
	private final PreviewArea rightArea;
	private final ListGrid mapGrid;
	private final SpinnerItem watcherSlots, playerSlots, countDownTime;
	private final Form checkboxFormLeft, checkboxFormRight;

	/**
	 * Konstruktor
	 */
	public RefereePage() {
		super(Page.props.refereePage_title());

		// Areas
		this.mainArea = new VerticalStack(Integer.valueOf(Page.props.global_marginBetweenStackAreas()), 0);

		this.contentArea = new HorizontalStack(Integer.valueOf(Page.props.global_marginBetweenStackAreas()), 0);

		this.leftArea = new VerticalStack(Integer.valueOf(Page.props.global_marginBetweenStackAreas()), 0);
		this.leftArea.setWidth(550);

		this.rightArea = new PreviewArea(Page.props.refereePage_area_rightArea_groupTitle(), "refereeArea");

		this.listArea = new VerticalStack(Page.props.refereePage_area_listArea_groupTitle());
		this.listArea.setHeight(210);
		this.listArea.setStyleName("refereeArea");

		this.optionsArea = new HorizontalStack(Page.props.refereePage_area_optionsArea_groupTitle());
		this.optionsArea.setWidth(550);
		this.optionsArea.setHeight(208);
		this.optionsArea.setStyleName("refereeArea");

		this.buttonArea = new HorizontalStack(Integer.valueOf(Page.props.global_marginInStackAreas()), 0);
		this.buttonArea.setHeight(30);

		// Liste mit Karten links
		this.mapGrid = new ListGrid();
		this.mapGrid.setWidth(510);
		this.mapGrid.setHeight(213);

		// ListGridFields
		this.idListGridField = new ListGridField("id", Page.props.global_symbol_hash());
		this.idListGridField.setHidden(true);
		this.nameListGridField = new ListGridField("name", Page.props.refereePage_table_nameListGridField_title());
		this.sizeListGridField = new ListGridField("size", Page.props.refereePage_table_sizeListGridField_title());
		this.numberOfCheckpointsListGridField = new ListGridField("numberOfCheckpoints",
				Page.props.refereePage_table_numberOfCheckpointsListGridField_title());
		this.maxPlayersListGridField = new ListGridField("maxPlayers",
				Page.props.refereePage_table_maxPlayersListGridField_title());
		this.mapGrid.setFields(new ListGridField[] { this.idListGridField, this.nameListGridField, this.sizeListGridField,
				this.numberOfCheckpointsListGridField, this.maxPlayersListGridField });
		this.mapGrid.setShowAllRecords(true);
		this.mapGrid.setCanEdit(false);
		this.mapGrid.setCanResizeFields(true);
		this.mapGrid.setWrapCells(true);
		this.mapGrid.setFixedRecordHeights(false);
		this.mapGrid.setShowRowNumbers(false);
		this.mapGrid.setSortField(4);

		// Optionen
		this.checkboxFormLeft = new Form();
		this.checkboxFormLeft.setPadding(0);
		this.checkboxFormRight = new Form();
		this.checkboxFormRight.setPadding(0);
		this.gameName = new TextItem(Page.props.preGamePage_gameName());
		this.gameName.setWidth(150);
		this.gameName.setLength(30);
		this.checkboxFormLeft.setAutoFocus(true);

		this.lasersOn = new CheckboxItem();
		this.lasersOn.setTitle(Page.props.preview_roundoption_laserOn_title());
		this.lasersOn.setValue(true);
		this.lasersOn.setDisabled(true);
		this.robotShootsOn = new CheckboxItem();
		this.robotShootsOn.setTitle(Page.props.preview_roundoption_robotShootsOn_title());
		this.robotShootsOn.setValue(true);
		this.reduceCountdown = new CheckboxItem();
		this.reduceCountdown.setTitle(Page.props.preview_roundoption_timeCountdownOn_title());
		this.reduceCountdown.setValue(true);
		this.alwaysPushersOn = new CheckboxItem();
		this.alwaysPushersOn.setTitle(Page.props.preview_roundoption_alwaysPusherOn_title());
		this.alwaysPushersOn.setValue(true);
		this.alwaysPushersOn.setDisabled(true);
		this.alwaysCompactorsOn = new CheckboxItem();
		this.alwaysCompactorsOn.setTitle(Page.props.preview_roundoption_alwaysPresserOn_title());
		this.alwaysCompactorsOn.setDisabled(true);
		this.alwaysCBOn = new CheckboxItem();
		this.alwaysCBOn.setTitle(Page.props.preview_roundoption_alwaysCBOn_title());
		this.alwaysCBOn.setValue(true);
		this.alwaysCBOn.setDisabled(true);
		this.alwaysGearsOn = new CheckboxItem();
		this.alwaysGearsOn.setTitle(Page.props.preview_roundoption_alwaysGearOn_title());
		this.alwaysGearsOn.setValue(true);
		this.alwaysGearsOn.setDisabled(true);
		this.allowObservers = new CheckboxItem();
		this.allowObservers.setTitle(Page.props.preview_roundoption_allowObservers_title());
		this.allowObservers.setValue(true);
		this.watcherSlots = new SpinnerItem(Page.props.refereePage_watcherSlots_title());
		this.watcherSlots.setTitle(Page.props.preview_roundoption_watcherSlots_title());
		this.watcherSlots.setWidth(50);
		this.watcherSlots.setValue(8);
		this.watcherSlots.setMax(8);
		this.watcherSlots.setMin(0);
		this.playerSlots = new SpinnerItem(Page.props.refereePage_playerSlots_title());
		this.playerSlots.setTitle(Page.props.preview_roundoption_playerSlots_title());
		this.playerSlots.setWidth(50);
		this.playerSlots.setValue(0);
		this.playerSlots.setMax(0);
		this.playerSlots.setMin(2);
		this.countDownTime = new SpinnerItem(Page.props.refereePage_countDownTime_title());
		this.countDownTime.setTitle(Page.props.preview_roundoption_countDownTime_title());
		this.countDownTime.setWidth(50);
		this.countDownTime.setValue(40);
		this.countDownTime.setMax(60);
		this.countDownTime.setMin(15);

		this.checkboxFormLeft.setFields(this.gameName, this.playerSlots, this.allowObservers, this.watcherSlots,
				this.reduceCountdown, this.countDownTime);
		this.checkboxFormRight.setFields(this.alwaysCBOn, this.alwaysCompactorsOn, this.alwaysPushersOn, 
				this.alwaysGearsOn, this.lasersOn, this.robotShootsOn);

		// Preview auf der rechten Seite
		

		// Buttons unten
		this.buttonCloseTab = new Button(Page.props.global_title_abort(), Integer.valueOf(Page.props.global_buttonWidth()));
		this.buttonAbort = new Button(Page.props.global_title_continue(), Integer.valueOf(Page.props.global_buttonWidth())); // PreGameGameInitiatorPage
		this.buttonAbort.setDisabled(true);

		// hinzufügen
		this.listArea.addMember(this.mapGrid);
		this.optionsArea.addMembers(this.checkboxFormLeft, this.checkboxFormRight);
		this.leftArea.addMembers(this.listArea, this.optionsArea);

		this.contentArea.addMembers(this.leftArea, this.rightArea);
		this.buttonArea.addMembers(this.buttonCloseTab, this.buttonAbort);
		this.buttonArea.setLayoutAlign(Alignment.CENTER);
		this.mainArea.addMembers(this.contentArea, this.buttonArea);
		setMainStack(this.mainArea);
	}

	/**
	 * Liefert das allowObserver-CheckboxItem
	 * 
	 * @return allowObservers
	 */
	public CheckboxItem getAllowObservers() {
		return this.allowObservers;
	}

	/**
	 * Liefert das alwaysShowProgrammingCardsOn-CheckboxItem
	 * 
	 * @return alwaysShowProgrammingCardsOn
	 */
	public CheckboxItem getAlwaysCBOn() {
		return this.alwaysCBOn;
	}

	/**
	 * Liefert die Angabe der Zahnräder
	 * 
	 * @return alwaysGearOn
	 */
	public CheckboxItem getAlwaysGearsOn() {
		return this.alwaysGearsOn;
	}

	/**
	 * Liefert das alwaysPresserOn-CheckboxItem
	 * 
	 * @return alwaysPresserOn
	 */
	public CheckboxItem getAlwaysCompactorsOn() {
		return this.alwaysCompactorsOn;
	}

	/**
	 * Liefert das alwaysPusherOn-CheckboxItem
	 * 
	 * @return alwaysPusherOn
	 */
	public CheckboxItem getAlwaysPushersOn() {
		return this.alwaysPushersOn;
	}

	/**
	 * Liefert den Abort Button
	 * 
	 * @return buttonAbort
	 */
	public Button getButtonAbort() {
		return this.buttonAbort;
	}

	/**
	 * Liefert den Close-Button
	 * 
	 * @return buttonCloseTab
	 */
	public Button getButtonCloseTab() {
		return this.buttonCloseTab;
	}

	/**
	 * Liefert das gameName-TextItem
	 * 
	 * @return gameName
	 */
	public TextItem getGameName() {
		return this.gameName;
	}

	/**
	 * Liefert das laserOn-CheckboxItem
	 * 
	 * @return laserOn
	 */
	public CheckboxItem getLaserOn() {
		return this.lasersOn;
	}

	/**
	 * Liefert das mapGrid
	 * 
	 * @return mapGrid
	 */
	public ListGrid getMapGrid() {
		return this.mapGrid;
	}

	/**
	 * Liefert die PlayerSlots
	 * 
	 * @return playerSlots
	 */
	public SpinnerItem getPlayerSlots() {
		return this.playerSlots;
	}

	/**
	 * Liefert die PreviewArea
	 * 
	 * @return previewListGrid
	 */
	public PreviewArea getPreviewArea() {
		return this.rightArea;
	}

	/**
	 * Liefert das timeCountdownOn-CheckboxItem
	 * 
	 * @return timeCountdownOn
	 */
	public CheckboxItem getReduceCountDown() {
		return this.reduceCountdown;
	}

	/**
	 * Liefert das robotShootsOn-CheckboxItem
	 * 
	 * @return robotShootsOn
	 */
	public CheckboxItem getRobotShootsOn() {
		return this.robotShootsOn;
	}

	/**
	 * Liefert die WatcherSlots
	 * 
	 * @return watcherSlots
	 */
	public SpinnerItem getWatcherSlots() {
		return this.watcherSlots;
	}

	/**
	 * Liefert die Zeit für den CountDown in einer Spielrunde
	 * 
	 * @return countDownTime
	 */
	public SpinnerItem getCountDownTime() {
		return countDownTime;
	}

	/**
	 * Validiert die DynamicForms
	 * 
	 * @return true, falls alles okay ist, ansonsten werden die Fehler angezeigt
	 */
	public boolean validate() {
		return this.checkboxFormLeft.validate();
	}
}