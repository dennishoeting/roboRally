package prototyp.client.view.round;

import prototyp.client.presenter.chat.ChatPresenter;
import prototyp.client.presenter.round.RoundWatcherPagePresenter;
import prototyp.client.util.Button;
import prototyp.client.util.Form;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;
import prototyp.client.view.chat.ChatArea;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Slider;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;

/**
 * Area für die rechte Seite auf der RoundWatcherPage
 * 
 * @author Dennis
 * @version 1.0
 * 
 */
public class WatcherStatusArea extends RobotStatusArea {
	/** der EndGameButton */
	private Button endGameButton;

	/** der Slider für die Soundeffekte */
	private Slider soundSlider;

	/** der SectionStack */
	private SectionStack sectionStack;

	/** die Areas für die StackSections */
	private VerticalStack othersReadyArea, othersStateArea, chatArea,
			menueArea;

	/**
	 * Die StackSections
	 */
	private SectionStackSection othersReadySection, othersStateSection,
			chatSection, menueSection;

	/** das playerReadyGrid */
	private ListGrid othersReadyGrid;

	/** die Fields für das playerReadyGrid */
	private ListGridField othersReadyGridPlayerID, othersReadyGridPlayerIcon,
			othersReadyGridPlayerName, othersReadyGridPlayerCard1,
			othersReadyGridPlayerCard2, othersReadyGridPlayerCard3,
			othersReadyGridPlayerCard4, othersReadyGridPlayerCard5,
			othersReadyGridPlayerReady;

	/** das playerStateGrid */
	private ListGrid othersStateGrid;

	/** die Fields für das playerStateGrid */
	private ListGridField othersStateGridPlayerID, othersStateGridPlayerIcon,
			othersStateGridPlayerName, othersStateGridPlayerLifeToken1,
			othersStateGridPlayerLifeToken2, othersStateGridPlayerLifeToken3,
			othersStateGridPlayerDamageToken1,
			othersStateGridPlayerDamageToken2,
			othersStateGridPlayerDamageToken3,
			othersStateGridPlayerDamageToken4,
			othersStateGridPlayerDamageToken5,
			othersStateGridPlayerDamageToken6,
			othersStateGridPlayerDamageToken7,
			othersStateGridPlayerDamageToken8,
			othersStateGridPlayerDamageToken9,
			othersStateGridPlayerDamageToken10;

	/** Anschalter für den Sound */
	private CheckboxItem soundOn;

	/** DynamicForm für die SoundArea */
	private Form soundArea;

	/**
	 * Konstanten
	 */
	// Gesamter SectionStack
	private static final int WIDTH = 370, HEIGHT = 560;
	// Höhe der einzelnen SectionStackSections
	private static final int OTHERS_AREA_HEIGHT = 135, CHAT_AREA_HEIGHT = 130,
			MENUE_HEIGHT = WatcherStatusArea.HEIGHT;
	// private static final int TOTAL_STACK_HEIGHT = PLAYER_STATE_AREA_HEIGHT
	// + SET_CARDS_AREA_HEIGHT
	// + GIVEN_CARDS_AREA_HEIGHT
	// + READY_BUTTON_AREA_HEIGHT
	// + OTHERS_AREA_HEIGHT
	// + CHAT_AREA_HEIGHT
	// + MENUE_BUTTON_AREA_HEIGHT;
	private static final int TOTAL_STACK_HEIGHT = 560;

	public WatcherStatusArea(RoundWatcherPagePresenter presenter) {
		super(0, 0);

		this.setWidth(WatcherStatusArea.WIDTH);
		this.setHeight(WatcherStatusArea.HEIGHT);
		this.setLeft(20);

		/*
		 * -------------------------------------------------- OTHERS READY GRID
		 * othersReadyGrid
		 */
		this.othersReadyGrid = new ListGrid();
		this.othersReadyGrid.setWidth100();
		this.othersReadyGrid.setHeight(WatcherStatusArea.OTHERS_AREA_HEIGHT + 100);
		this.othersReadyGrid.setShowAllRecords(true);
		this.othersReadyGrid.setSelectionType(SelectionStyle.NONE);
		this.othersReadyGrid.setShowRowNumbers(false);
		this.othersReadyGrid.setShowHeader(false);
		this.othersReadyGridPlayerID = new ListGridField("id");
		this.othersReadyGridPlayerID.setHidden(true);
		this.othersReadyGridPlayerIcon = new ListGridField("icon");
		this.othersReadyGridPlayerIcon.setType(ListGridFieldType.IMAGE);
		this.othersReadyGridPlayerIcon.setAlign(Alignment.CENTER);
		this.othersReadyGridPlayerIcon.setWidth(20);
		this.othersReadyGridPlayerName = new ListGridField("name");
		this.othersReadyGridPlayerName.setWidth(100);
		this.othersReadyGridPlayerCard1 = new ListGridField("card1");
		this.othersReadyGridPlayerCard1.setWidth(20);
		this.othersReadyGridPlayerCard1.setType(ListGridFieldType.IMAGE);
		this.othersReadyGridPlayerCard1.setAlign(Alignment.LEFT);
		this.othersReadyGridPlayerCard2 = new ListGridField("card2");
		this.othersReadyGridPlayerCard2.setWidth(20);
		this.othersReadyGridPlayerCard2.setType(ListGridFieldType.IMAGE);
		this.othersReadyGridPlayerCard2.setAlign(Alignment.LEFT);
		this.othersReadyGridPlayerCard3 = new ListGridField("card3");
		this.othersReadyGridPlayerCard3.setWidth(20);
		this.othersReadyGridPlayerCard3.setType(ListGridFieldType.IMAGE);
		this.othersReadyGridPlayerCard3.setAlign(Alignment.LEFT);
		this.othersReadyGridPlayerCard4 = new ListGridField("card4");
		this.othersReadyGridPlayerCard4.setWidth(20);
		this.othersReadyGridPlayerCard4.setType(ListGridFieldType.IMAGE);
		this.othersReadyGridPlayerCard4.setAlign(Alignment.LEFT);
		this.othersReadyGridPlayerCard5 = new ListGridField("card5");
		this.othersReadyGridPlayerCard5.setWidth(120);
		this.othersReadyGridPlayerCard5.setType(ListGridFieldType.IMAGE);
		this.othersReadyGridPlayerCard5.setAlign(Alignment.LEFT);
		this.othersReadyGridPlayerReady = new ListGridField("ready");
		this.othersReadyGridPlayerReady.setWidth(20);
		this.othersReadyGridPlayerReady.setType(ListGridFieldType.IMAGE);
		this.othersReadyGridPlayerReady.setAlign(Alignment.CENTER);
		this.othersReadyGrid.setFields(this.othersReadyGridPlayerIcon,
				this.othersReadyGridPlayerName,
				this.othersReadyGridPlayerCard1,
				this.othersReadyGridPlayerCard2,
				this.othersReadyGridPlayerCard3,
				this.othersReadyGridPlayerCard4,
				this.othersReadyGridPlayerCard5,
				this.othersReadyGridPlayerReady);

		/*
		 * othersReadyArea
		 */
		this.othersReadyArea = new VerticalStack(0, 5);
		this.othersReadyArea.setHeight(WatcherStatusArea.OTHERS_AREA_HEIGHT);
		this.othersReadyArea.addMembers(this.othersReadyGrid);

		/*
		 * -------------------------------------------------- OTHERS STATE GRID
		 * othersStateGrid
		 */
		this.othersStateGrid = new ListGrid();
		this.othersStateGrid.setWidth100();
		// 100px größer, da Karten verschwinden.. :)
		this.othersStateGrid
				.setHeight(WatcherStatusArea.OTHERS_AREA_HEIGHT + 100);
		this.othersStateGrid.setShowAllRecords(true);
		this.othersStateGrid.setSelectionType(SelectionStyle.NONE);
		this.othersStateGrid.setShowRowNumbers(false);
		this.othersStateGrid.setShowHeader(false);
		this.othersStateGridPlayerID = new ListGridField("id");
		this.othersStateGridPlayerID.setHidden(true);
		this.othersStateGridPlayerIcon = new ListGridField("icon");
		this.othersStateGridPlayerIcon.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerIcon.setAlign(Alignment.CENTER);
		this.othersStateGridPlayerIcon.setWidth(20);
		this.othersStateGridPlayerName = new ListGridField("name");
		this.othersStateGridPlayerName.setWidth(70);
		this.othersStateGridPlayerLifeToken1 = new ListGridField("lifeToken1");
		this.othersStateGridPlayerLifeToken1.setWidth(20);
		this.othersStateGridPlayerLifeToken1.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerLifeToken1.setAlign(Alignment.CENTER);
		this.othersStateGridPlayerLifeToken2 = new ListGridField("lifeToken2");
		this.othersStateGridPlayerLifeToken2.setWidth(20);
		this.othersStateGridPlayerLifeToken2.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerLifeToken2.setAlign(Alignment.CENTER);
		this.othersStateGridPlayerLifeToken3 = new ListGridField("lifeToken3");
		this.othersStateGridPlayerLifeToken3.setWidth(20);
		this.othersStateGridPlayerLifeToken3.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerLifeToken3.setAlign(Alignment.CENTER);
		this.othersStateGridPlayerDamageToken1 = new ListGridField(
				"damageToken1");
		this.othersStateGridPlayerDamageToken1.setWidth(20);
		this.othersStateGridPlayerDamageToken1.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerDamageToken1.setAlign(Alignment.CENTER);
		this.othersStateGridPlayerDamageToken2 = new ListGridField(
				"damageToken2");
		this.othersStateGridPlayerDamageToken2.setWidth(20);
		this.othersStateGridPlayerDamageToken2.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerDamageToken2.setAlign(Alignment.CENTER);
		this.othersStateGridPlayerDamageToken3 = new ListGridField(
				"damageToken3");
		this.othersStateGridPlayerDamageToken3.setWidth(20);
		this.othersStateGridPlayerDamageToken3.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerDamageToken3.setAlign(Alignment.CENTER);
		this.othersStateGridPlayerDamageToken4 = new ListGridField(
				"damageToken4");
		this.othersStateGridPlayerDamageToken4.setWidth(20);
		this.othersStateGridPlayerDamageToken4.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerDamageToken4.setAlign(Alignment.CENTER);
		this.othersStateGridPlayerDamageToken5 = new ListGridField(
				"damageToken5");
		this.othersStateGridPlayerDamageToken5.setWidth(20);
		this.othersStateGridPlayerDamageToken5.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerDamageToken5.setAlign(Alignment.CENTER);
		this.othersStateGridPlayerDamageToken6 = new ListGridField(
				"damageToken6");
		this.othersStateGridPlayerDamageToken6.setWidth(20);
		this.othersStateGridPlayerDamageToken6.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerDamageToken6.setAlign(Alignment.CENTER);
		this.othersStateGridPlayerDamageToken7 = new ListGridField(
				"damageToken7");
		this.othersStateGridPlayerDamageToken7.setWidth(20);
		this.othersStateGridPlayerDamageToken7.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerDamageToken7.setAlign(Alignment.CENTER);
		this.othersStateGridPlayerDamageToken8 = new ListGridField(
				"damageToken8");
		this.othersStateGridPlayerDamageToken8.setWidth(20);
		this.othersStateGridPlayerDamageToken8.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerDamageToken8.setAlign(Alignment.CENTER);
		this.othersStateGridPlayerDamageToken9 = new ListGridField(
				"damageToken9");
		this.othersStateGridPlayerDamageToken9.setWidth(20);
		this.othersStateGridPlayerDamageToken9.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerDamageToken9.setAlign(Alignment.CENTER);
		this.othersStateGridPlayerDamageToken10 = new ListGridField(
				"damageToken10");
		this.othersStateGridPlayerDamageToken10.setWidth(20);
		this.othersStateGridPlayerDamageToken10
				.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerDamageToken10.setAlign(Alignment.CENTER);
		this.othersStateGrid.setFields(this.othersStateGridPlayerIcon,
				this.othersStateGridPlayerName,
				this.othersStateGridPlayerLifeToken1,
				this.othersStateGridPlayerLifeToken2,
				this.othersStateGridPlayerLifeToken3,
				this.othersStateGridPlayerDamageToken1,
				this.othersStateGridPlayerDamageToken2,
				this.othersStateGridPlayerDamageToken3,
				this.othersStateGridPlayerDamageToken4,
				this.othersStateGridPlayerDamageToken5,
				this.othersStateGridPlayerDamageToken6,
				this.othersStateGridPlayerDamageToken7,
				this.othersStateGridPlayerDamageToken8,
				this.othersStateGridPlayerDamageToken9,
				this.othersStateGridPlayerDamageToken10);

		/*
		 * othersStateArea
		 */
		this.othersStateArea = new VerticalStack(0, 5);
		this.othersStateArea.setHeight(WatcherStatusArea.OTHERS_AREA_HEIGHT);
		this.othersStateArea.addMembers(this.othersStateGrid);

		/*
		 * -------------------------------------------------- CHAT Chat
		 */
		this.chatArea = new VerticalStack(0, 5);
		this.chatArea.setHeight(WatcherStatusArea.CHAT_AREA_HEIGHT);

		/*
		 * -------------------------------------------------- MENUE
		 * EndGameButton
		 */
		this.endGameButton = new Button(
				Page.props.roundPlayerPage_buttonPostGame_title(), 355	);
		this.endGameButton.setAlign(Alignment.CENTER);

		/* Sound an */
		this.soundOn = new CheckboxItem();
		this.soundOn.setTitle(Page.props
				.roundPlayerPage_menueSection_soundOn_title());
		this.soundOn.setValue(true);

		/*
		 * Soundeffekte
		 */
		this.soundSlider = new Slider(
				Page.props.roundPlayerPage_menueSection_sound_title());
		this.soundSlider.setVertical(false);
		this.soundSlider.setShowRange(false);
		this.soundSlider.setShowValue(false);
		this.soundSlider.setLength(200);
		this.soundSlider.setMinValue(0F);
		this.soundSlider.setMaxValue(100F);
		this.soundSlider.setValue(100F);

		/*
		 * SoundAreas
		 */
		this.soundArea = new Form();
		this.soundArea.setMargin(0);
		this.soundArea.setPadding(0);
		this.soundArea.setHeight(15);
		this.soundArea.setWidth(350);
		this.soundArea.setFields(this.soundOn);

		/*
		 * MenueStack
		 */
		this.menueArea = new VerticalStack(15, 5);
		this.menueArea.setWidth(WatcherStatusArea.WIDTH);
		this.menueArea.setHeight(WatcherStatusArea.MENUE_HEIGHT);
		this.menueArea.setAlign(Alignment.CENTER);
		this.menueArea.setAlign(VerticalAlignment.TOP);
		this.menueArea.addMembers(this.soundArea, this.soundSlider,
				this.endGameButton);

		/*
		 * -------------------------------------------------- DER SECTION STACK
		 * Der SectionStack
		 */
		this.sectionStack = new SectionStack();
		this.sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		this.sectionStack.setHeight(WatcherStatusArea.TOTAL_STACK_HEIGHT);
		this.sectionStack.setBorder("1px dashed black");
		this.sectionStack.setAnimateSections(true);
		this.othersReadySection = new SectionStackSection();
		this.othersReadySection.setShowHeader(false);
		this.othersReadySection.setExpanded(true);
		this.othersReadySection.setItems(this.othersReadyArea);
		this.othersStateSection = new SectionStackSection();
		this.othersStateSection.setShowHeader(false);
		this.othersStateSection.setExpanded(false);
		this.othersStateSection.setItems(this.othersStateArea);
		this.chatSection = new SectionStackSection();
		this.chatSection.setShowHeader(false);
		this.chatSection.setExpanded(true);
		this.chatSection.addItem(this.chatArea);
		this.menueSection = new SectionStackSection();
		this.menueSection.setShowHeader(false);
		this.menueSection.setExpanded(true);
		this.menueSection.addItem(this.menueArea);
		this.othersReadySection.setName("readySection");
		this.othersStateSection.setName("stateSection");
		this.sectionStack.setSections(this.othersReadySection,
				this.othersStateSection, this.chatSection, this.menueSection);

		/*
		 * Das ganze adden
		 */
		setMembers(this.sectionStack);
	}

	/**
	 * Liefert den endGameButton
	 */
	public Button getEndGameButton() {
		return this.endGameButton;
	}

	/**
	 * Liefert othersReadyGrid
	 * 
	 * @return othersReadyGrid
	 */
	public ListGrid getOthersReadyGrid() {
		return this.othersReadyGrid;
	}

	/**
	 * @return the othersStateArea
	 */
	public VerticalStack getOthersStateArea() {
		return this.othersStateArea;
	}

	/**
	 * Liefert othersStateGrid
	 * 
	 * @return othersStateGrid
	 */
	public ListGrid getOthersStateGrid() {
		return this.othersStateGrid;
	}

	/**
	 * Liefert den sectionStack
	 * 
	 * @return sectionStack
	 */
	public SectionStack getSectionStack() {
		return this.sectionStack;
	}

	/**
	 * Liefert die CheckBox für den Sound
	 * 
	 * @return the soundOn
	 */
	public CheckboxItem getSoundOn() {
		return this.soundOn;
	}

	/**
	 * Liefert den SoundSlider
	 * 
	 * @return soundSlider
	 */
	public Slider getSoundSlider() {
		return this.soundSlider;
	}

	/**
	 * Setzt die ChatArea
	 */
	public void setChatPresenter(ChatPresenter chatPresenter) {
		ChatArea chatPage = (ChatArea) chatPresenter.getPage();
		chatPage.setWidth(WatcherStatusArea.WIDTH - 10);
		chatPage.setHeight(WatcherStatusArea.CHAT_AREA_HEIGHT - 10 + 80);
		this.chatArea.addMembers(chatPage);
	}
}
