package prototyp.client.view.round;

import prototyp.client.presenter.chat.ChatPresenter;
import prototyp.client.presenter.round.RoundPlayerPagePresenter;
import prototyp.client.util.Button;
import prototyp.client.util.CardSlot;
import prototyp.client.util.Form;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.StandardLabel;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;
import prototyp.client.view.chat.ChatArea;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Slider;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;

/**
 * PlayerStatusArea. Hier befindet sich alles, was für die visuelle Darstellung wichtig ist.
 * 
 * @author Marcus
 * @version 1.0
 */
public class PlayerStatusArea extends RobotStatusArea {


	/** der Powerdownbutton */
	private final ImgButton powerDownButton;

	/** der Readybutton */
	private final Button readyButton;

	/** der Menuebutton */
	private final Button menueButton;

	/** der EndGameButton */
	private final Button endGameButton;

	/** der Slider für die Soundeffekte */
	private final Slider soundSlider;

	/** die LifeToken */
	private final Img[] lifeTokens = new Img[3];

	/** DamageTokenleiste */
	private final Img damageTokens;

	/** Timer für einen Spielschritt */
	private final StandardLabel timer;

	/** der Platz für die gesetzten Programmierkarten */
	private final CardSlot[] cardSetSlots = new CardSlot[5];

	/** der Platz für die ausgeteilten Programmierkarten */
	private final CardSlot[] cardGivenSlots = new CardSlot[9];

	/** die Area für die ausgewählten Karten */
	private final HorizontalStack setCardsArea;

	/** die givenCardsArea für die zwei givenCardsAreas.. */
	private final VerticalStack givenCardsArea;

	/** die Area für die zugeteilten Karten 0-4 */
	private final HorizontalStack givenCardsArea1;


	/** der Stack für powerDown-Button und liveToken */
	private final HorizontalStack upperPlayerStateStack;

	/** der Stack für die lifeToken */
	private final HorizontalStack lifeTokenStack;

	/** der Stack für die damageToken */
	private final HorizontalStack damageTokenStack;

	/** der SectionStack */
	private final SectionStack sectionStack;

	/** die Areas für die StackSections */
	private final VerticalStack playerStateArea, readyButtonArea, othersReadyArea, othersStateArea, chatArea, menueButtonArea,
			menueArea;

	/**
	 * Die StackSections
	 */
	private final SectionStackSection playerStateSection, setCardsSection, givenCardsSection, deadInfoSection, powerDownInfoSection, readyButtonSection, othersReadySection,
			othersStateSection, chatSection, menueButtonSection, menueSection;

	/** das playerReadyGrid */
	private final ListGrid othersReadyGrid;

	/** die Fields für das playerReadyGrid */
	private final ListGridField othersReadyGridPlayerID, othersReadyGridPlayerIcon, othersReadyGridPlayerName, othersReadyGridCheckpoint,
			othersReadyGridPlayerCard1, othersReadyGridPlayerCard2, othersReadyGridPlayerCard3, othersReadyGridPlayerCard4,
			othersReadyGridPlayerCard5, othersReadyGridPlayerReady;

	/** das playerStateGrid */
	private final ListGrid othersStateGrid;

	/** die Fields für das playerStateGrid */
	private final ListGridField othersStateGridPlayerID, othersStateGridPlayerIcon, othersStateGridPlayerName, othersStateGridCheckpoint,
			othersStateGridPlayerLifeToken1, othersStateGridPlayerLifeToken2, othersStateGridPlayerLifeToken3,
			othersStateGridPlayerDamageToken1, othersStateGridPlayerDamageToken2, othersStateGridPlayerDamageToken3,
			othersStateGridPlayerDamageToken4, othersStateGridPlayerDamageToken5, othersStateGridPlayerDamageToken6,
			othersStateGridPlayerDamageToken7, othersStateGridPlayerDamageToken8, othersStateGridPlayerDamageToken9,
			othersStateGridPlayerDamageToken10;

	/** Anschalter für den Sound */
	private final CheckboxItem soundOn;

	/** DynamicForm für die SoundArea */
	private final Form soundArea;
	
	/** PowerDownInfo */
	private final Img powerDownInfoImg;
	
	/** DeadInfo */
	private final Img deadInfoImg;

	/**
	 * Konstanten
	 */
	// Gesamter SectionStack
	private static final int WIDTH = 370, HEIGHT = 560;
	// Höhe der einzelnen SectionStackSections
	private static final int PLAYER_STATE_AREA_HEIGHT = 80, SET_CARDS_AREA_HEIGHT = 55,
			GIVEN_CARDS_AREA_HEIGHT = SET_CARDS_AREA_HEIGHT, READY_BUTTON_AREA_HEIGHT = 25,
			OTHERS_AREA_HEIGHT = 135, CHAT_AREA_HEIGHT = 130, MENUE_BUTTON_AREA_HEIGHT = 25,
			MENUE_HEIGHT = PlayerStatusArea.HEIGHT;

	/** Angabe der Höhe des Stacks */
	private static final int TOTAL_STACK_HEIGHT = 560;

	/**
	 * Konstruktor
	 * 
	 * @param robot
	 */
	public PlayerStatusArea(RoundPlayerPagePresenter presenter) {
		super(0, 0);

		this.setWidth(PlayerStatusArea.WIDTH);
		this.setHeight(PlayerStatusArea.HEIGHT);
		this.setLeft(20);

		// this.setCanAcceptDrop(true);

		/*
		 * -------------------------------------------------- PLAYER STATE Powerdownbutton erzeugen
		 */
		this.powerDownButton = new ImgButton();
		this.powerDownButton.setSrc("robotStatus/powerDown.png");
		this.powerDownButton.setBackgroundImage("robotStatus/powerDownHover.png");
		this.powerDownButton.setWidth(50);
		this.powerDownButton.setHeight(50);
		this.powerDownButton.setCanFocus(false);

		/*
		 * LifeTokenStack erzeugen
		 */
		for (int i = 0; i < this.lifeTokens.length; i++) {
			this.lifeTokens[i] = new Img("robotStatus/lToken.png", 20, 20);
		}
		this.lifeTokenStack = new HorizontalStack(20, 15);
		this.lifeTokenStack.setHeight(20);
		this.lifeTokenStack.setMembers(this.lifeTokens);

		/*
		 * Timer vorbereiten
		 */
		this.timer = new StandardLabel("00:00");
		this.timer.setStyleName("timer");
		this.timer.setWidth(50);
		this.timer.setAlign(Alignment.RIGHT);

		/*
		 * PlayerStateStack
		 */
		this.upperPlayerStateStack = new HorizontalStack(5, 0);
		this.upperPlayerStateStack.setHeight(50);
		this.upperPlayerStateStack.setMembers(this.powerDownButton, this.lifeTokenStack);

		/*
		 * DamageToken erzeugen
		 */
		this.damageTokens = new Img("robotStatus/damageToken0.png", 355, 31);
		this.damageTokenStack = new HorizontalStack();
		this.damageTokenStack.addMember(this.damageTokens);
		this.damageTokenStack.setHeight(20);

		/*
		 * PlayerStateStack
		 */
		this.playerStateArea = new VerticalStack(0, 0);
		this.playerStateArea.setHeight(PlayerStatusArea.PLAYER_STATE_AREA_HEIGHT);
		this.playerStateArea.addMembers(this.upperPlayerStateStack, this.damageTokenStack);

		/*
		 * -------------------------------------------------- SET CARDS Gewählte Karten
		 */
		this.cardSetSlots[0] = new CardSlot(1, presenter);
		this.cardSetSlots[1] = new CardSlot(2, presenter);
		this.cardSetSlots[2] = new CardSlot(3, presenter);
		this.cardSetSlots[3] = new CardSlot(4, presenter);
		this.cardSetSlots[4] = new CardSlot(5, presenter);

		final Img[] arrows = new Img[4];
		for (int i = 0; i < arrows.length; i++) {
			arrows[i] = new Img("robotStatus/arrow.png", 35, 50);
		}

		this.setCardsArea = new HorizontalStack(5, 5);
		this.setCardsArea.setHeight(PlayerStatusArea.SET_CARDS_AREA_HEIGHT);
		this.setCardsArea.setMembers(this.cardSetSlots[0], arrows[0], this.cardSetSlots[1], arrows[1], this.cardSetSlots[2],
				arrows[2], this.cardSetSlots[3], arrows[3], this.cardSetSlots[4]);

		/*
		 * -------------------------------------------------- GIVEN CARDS Gegebene Karten
		 */
		this.cardGivenSlots[0] = new CardSlot(-1, presenter);
		this.cardGivenSlots[1] = new CardSlot(-1, presenter);
		this.cardGivenSlots[2] = new CardSlot(-1, presenter);
		this.cardGivenSlots[3] = new CardSlot(-1, presenter);
		this.cardGivenSlots[4] = new CardSlot(-1, presenter);
		this.cardGivenSlots[5] = new CardSlot(-1, presenter);
		this.cardGivenSlots[6] = new CardSlot(-1, presenter);
		this.cardGivenSlots[7] = new CardSlot(-1, presenter);
		this.cardGivenSlots[8] = new CardSlot(-1, presenter);

		/*
		 * Given Cards Area;
		 */
		this.givenCardsArea1 = new HorizontalStack(5, 0);
		this.givenCardsArea1.setHeight(PlayerStatusArea.SET_CARDS_AREA_HEIGHT);
		this.givenCardsArea1.setMembers(this.cardGivenSlots[0], this.cardGivenSlots[1], this.cardGivenSlots[2],
				this.cardGivenSlots[3], this.cardGivenSlots[4], this.cardGivenSlots[5], this.cardGivenSlots[6],
				this.cardGivenSlots[7], this.cardGivenSlots[8]);
		this.givenCardsArea = new VerticalStack(0, 5);
		this.givenCardsArea.setHeight(PlayerStatusArea.GIVEN_CARDS_AREA_HEIGHT);
		this.givenCardsArea.addMembers(this.givenCardsArea1);
		
		/*
		 * PowerDownInfo
		 */
		this.powerDownInfoImg = new Img(Page.props.playerStatusArea_powerDownInfoSrc(), 355, 155);
		
		/*
		 * DeadInfo
		 */
		this.deadInfoImg = new Img(Page.props.playerStatusArea_deadInfoSrc(), 355, 155);

		/*
		 * -------------------------------------------------- READY BUTTON ReadyButton erzeugen
		 */
		this.readyButton = new Button(Page.props.roundPlayerPage_ready_title());
		this.readyButton.setDisabled(true);
		this.readyButton.setWidth100();
		this.readyButton.setHeight(PlayerStatusArea.READY_BUTTON_AREA_HEIGHT);

		this.readyButtonArea = new VerticalStack(0, 5);
		this.readyButtonArea.setHeight(PlayerStatusArea.READY_BUTTON_AREA_HEIGHT);
		this.readyButtonArea.addMembers(this.readyButton);

		/*
		 * -------------------------------------------------- OTHERS READY GRID othersReadyGrid
		 */
		this.othersReadyGrid = new ListGrid();
		this.othersReadyGrid.setWidth100();
		this.othersReadyGrid.setHeight(PlayerStatusArea.OTHERS_AREA_HEIGHT);
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
		//TODO
		this.othersReadyGridCheckpoint = new ListGridField("nextCheckpoint");
		this.othersReadyGridCheckpoint.setWidth(25);
		this.othersReadyGridCheckpoint.setType(ListGridFieldType.IMAGE);
		this.othersReadyGridCheckpoint.setAlign(Alignment.CENTER);
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
		this.othersReadyGridPlayerCard5.setWidth(110);
		this.othersReadyGridPlayerCard5.setType(ListGridFieldType.IMAGE);
		this.othersReadyGridPlayerCard5.setAlign(Alignment.LEFT);
		this.othersReadyGridPlayerReady = new ListGridField("ready");
		this.othersReadyGridPlayerReady.setWidth(20);
		this.othersReadyGridPlayerReady.setType(ListGridFieldType.IMAGE);
		this.othersReadyGridPlayerReady.setAlign(Alignment.CENTER);
		this.othersReadyGrid.setFields(this.othersReadyGridPlayerIcon, this.othersReadyGridPlayerName, this.othersReadyGridCheckpoint,
				this.othersReadyGridPlayerCard1, this.othersReadyGridPlayerCard2, this.othersReadyGridPlayerCard3,
				this.othersReadyGridPlayerCard4, this.othersReadyGridPlayerCard5, this.othersReadyGridPlayerReady);

		/*
		 * othersReadyArea
		 */
		this.othersReadyArea = new VerticalStack(0, 5);
		this.othersReadyArea.setHeight(PlayerStatusArea.OTHERS_AREA_HEIGHT);
		this.othersReadyArea.addMembers(this.othersReadyGrid);

		/*
		 * -------------------------------------------------- OTHERS STATE GRID othersStateGrid
		 */
		this.othersStateGrid = new ListGrid();
		this.othersStateGrid.setWidth100();
		// 100px größer, da Karten verschwinden.. :)
		this.othersStateGrid.setHeight(PlayerStatusArea.OTHERS_AREA_HEIGHT + 100); // +100 weil givenCard und ReadyButton weg
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
		this.othersStateGridPlayerName.setWidth(45);
		//TODO
		this.othersStateGridCheckpoint = new ListGridField("nextCheckpoint");
		this.othersStateGridCheckpoint.setWidth(25);
		this.othersStateGridCheckpoint.setType(ListGridFieldType.IMAGE);
		this.othersStateGridCheckpoint.setAlign(Alignment.CENTER);
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
		this.othersStateGridPlayerDamageToken1 = new ListGridField("damageToken1");
		this.othersStateGridPlayerDamageToken1.setWidth(20);
		this.othersStateGridPlayerDamageToken1.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerDamageToken1.setAlign(Alignment.CENTER);
		this.othersStateGridPlayerDamageToken2 = new ListGridField("damageToken2");
		this.othersStateGridPlayerDamageToken2.setWidth(20);
		this.othersStateGridPlayerDamageToken2.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerDamageToken2.setAlign(Alignment.CENTER);
		this.othersStateGridPlayerDamageToken3 = new ListGridField("damageToken3");
		this.othersStateGridPlayerDamageToken3.setWidth(20);
		this.othersStateGridPlayerDamageToken3.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerDamageToken3.setAlign(Alignment.CENTER);
		this.othersStateGridPlayerDamageToken4 = new ListGridField("damageToken4");
		this.othersStateGridPlayerDamageToken4.setWidth(20);
		this.othersStateGridPlayerDamageToken4.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerDamageToken4.setAlign(Alignment.CENTER);
		this.othersStateGridPlayerDamageToken5 = new ListGridField("damageToken5");
		this.othersStateGridPlayerDamageToken5.setWidth(20);
		this.othersStateGridPlayerDamageToken5.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerDamageToken5.setAlign(Alignment.CENTER);
		this.othersStateGridPlayerDamageToken6 = new ListGridField("damageToken6");
		this.othersStateGridPlayerDamageToken6.setWidth(20);
		this.othersStateGridPlayerDamageToken6.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerDamageToken6.setAlign(Alignment.CENTER);
		this.othersStateGridPlayerDamageToken7 = new ListGridField("damageToken7");
		this.othersStateGridPlayerDamageToken7.setWidth(20);
		this.othersStateGridPlayerDamageToken7.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerDamageToken7.setAlign(Alignment.CENTER);
		this.othersStateGridPlayerDamageToken8 = new ListGridField("damageToken8");
		this.othersStateGridPlayerDamageToken8.setWidth(20);
		this.othersStateGridPlayerDamageToken8.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerDamageToken8.setAlign(Alignment.CENTER);
		this.othersStateGridPlayerDamageToken9 = new ListGridField("damageToken9");
		this.othersStateGridPlayerDamageToken9.setWidth(20);
		this.othersStateGridPlayerDamageToken9.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerDamageToken9.setAlign(Alignment.CENTER);
		this.othersStateGridPlayerDamageToken10 = new ListGridField("damageToken10");
		this.othersStateGridPlayerDamageToken10.setWidth(20);
		this.othersStateGridPlayerDamageToken10.setType(ListGridFieldType.IMAGE);
		this.othersStateGridPlayerDamageToken10.setAlign(Alignment.CENTER);
		this.othersStateGrid.setFields(this.othersStateGridPlayerIcon, this.othersStateGridPlayerName, this.othersStateGridCheckpoint,
				this.othersStateGridPlayerLifeToken1, this.othersStateGridPlayerLifeToken2, this.othersStateGridPlayerLifeToken3,
				this.othersStateGridPlayerDamageToken1, this.othersStateGridPlayerDamageToken2,
				this.othersStateGridPlayerDamageToken3, this.othersStateGridPlayerDamageToken4,
				this.othersStateGridPlayerDamageToken5, this.othersStateGridPlayerDamageToken6,
				this.othersStateGridPlayerDamageToken7, this.othersStateGridPlayerDamageToken8,
				this.othersStateGridPlayerDamageToken9, this.othersStateGridPlayerDamageToken10);

		/*
		 * othersStateArea
		 */
		this.othersStateArea = new VerticalStack(0, 5);
		this.othersStateArea.setHeight(PlayerStatusArea.OTHERS_AREA_HEIGHT);
		this.othersStateArea.addMembers(this.othersStateGrid);

		/*
		 * -------------------------------------------------- CHAT Chat
		 */
		this.chatArea = new VerticalStack(0, 5);
		this.chatArea.setHeight(PlayerStatusArea.CHAT_AREA_HEIGHT);

		/*
		 * -------------------------------------------------- MENUE BUTTON MenueButton
		 */
		this.menueButton = new Button(Page.props.roundPlayerPage_menueSection_title());
		this.menueButton.setWidth100();
		this.menueButton.setHeight(PlayerStatusArea.MENUE_BUTTON_AREA_HEIGHT);

		this.menueButtonArea = new VerticalStack(0, 5);
		this.menueButtonArea.setHeight(PlayerStatusArea.MENUE_BUTTON_AREA_HEIGHT);
		this.menueButtonArea.addMembers(this.menueButton);

		/*
		 * -------------------------------------------------- MENUE EndGameButton
		 */
		this.endGameButton = new Button(Page.props.roundPlayerPage_buttonPostGame_title(), 355);
		this.endGameButton.setAlign(Alignment.CENTER);

		/* Sound an */
		this.soundOn = new CheckboxItem();
		this.soundOn.setTitle(Page.props.roundPlayerPage_menueSection_soundOn_title());
		this.soundOn.setValue(true);

		/*
		 * Soundeffekte
		 */
		this.soundSlider = new Slider(Page.props.roundPlayerPage_menueSection_sound_title());
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
		this.soundArea.setFields(this.soundOn);

		/*
		 * MenueStack
		 */
		this.menueArea = new VerticalStack(15, 5);
		this.menueArea.setWidth(PlayerStatusArea.WIDTH);
		this.menueArea.setHeight(PlayerStatusArea.MENUE_HEIGHT);
		this.menueArea.setAlign(Alignment.CENTER);
		this.menueArea.setAlign(VerticalAlignment.TOP);
		this.menueArea.addMembers(this.soundArea, this.soundSlider, this.endGameButton);

		/*
		 * -------------------------------------------------- DER SECTION STACK Der SectionStack
		 */
		this.sectionStack = new SectionStack();
		this.sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		this.sectionStack.setHeight(PlayerStatusArea.TOTAL_STACK_HEIGHT);
		this.sectionStack.setBorder("1px dashed black");
		this.sectionStack.setAnimateSections(true);
		this.playerStateSection = new SectionStackSection();
		this.playerStateSection.setShowHeader(false);
		this.playerStateSection.setExpanded(true);
		this.playerStateSection.setItems(this.playerStateArea);
		this.setCardsSection = new SectionStackSection();
		this.setCardsSection.setShowHeader(false);
		this.setCardsSection.setExpanded(true);
		this.setCardsSection.setItems(this.setCardsArea);
		this.givenCardsSection = new SectionStackSection();
		this.givenCardsSection.setShowHeader(false);
		this.givenCardsSection.setExpanded(true);
		this.givenCardsSection.setItems(this.givenCardsArea);
		this.powerDownInfoSection = new SectionStackSection();
		this.powerDownInfoSection.setShowHeader(false);
		this.powerDownInfoSection.setExpanded(false);
		this.powerDownInfoSection.setItems(this.powerDownInfoImg);
		this.deadInfoSection = new SectionStackSection();
		this.deadInfoSection.setShowHeader(false);
		this.deadInfoSection.setExpanded(false);
		this.deadInfoSection.setItems(this.deadInfoImg);
		this.readyButtonSection = new SectionStackSection();
		this.readyButtonSection.setShowHeader(false);
		this.readyButtonSection.setExpanded(true);
		this.readyButtonSection.setItems(this.readyButtonArea);
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
		this.menueButtonSection = new SectionStackSection();
		this.menueButtonSection.setShowHeader(false);
		this.menueButtonSection.setExpanded(true);
		this.menueButtonSection.addItem(this.menueButtonArea);
		this.menueSection = new SectionStackSection();
		this.menueSection.setShowHeader(false);
		this.menueSection.setExpanded(false);
		this.menueSection.addItem(this.menueArea);
		this.playerStateSection.setName("playerState");
		this.setCardsSection.setName("setCards");
		this.givenCardsSection.setName("givenCards");
		this.powerDownInfoSection.setName("powerDownInfo");
		this.deadInfoSection.setName("deadInfo");
		this.readyButtonSection.setName("readyButton");
		this.othersReadySection.setName("readySection");
		this.othersStateSection.setName("stateSection");
		this.chatSection.setName("chat");
		this.menueButtonSection.setName("menueButton");
		this.menueSection.setName("menue");
		this.sectionStack.setSections(
				this.playerStateSection, 
				this.setCardsSection, 
				this.givenCardsSection, 
				this.powerDownInfoSection,
				this.deadInfoSection,
				this.readyButtonSection, 
				this.othersReadySection, 
				this.othersStateSection, 
				this.chatSection, 
				this.menueButtonSection, 
				this.menueSection); 

		/*
		 * Das ganze adden
		 */
		setMembers(this.sectionStack);
	}

	/**
	 * Liefert die Card-Given-Slots
	 * 
	 * @return cardGivenSlots
	 */
	public CardSlot[] getCardGivenSlots() {
		return this.cardGivenSlots;
	}

	/**
	 * Liefert die Card-Set-Slots
	 * 
	 * @return cardSetSlots
	 */
	public CardSlot[] getCardSetSlots() {
		return this.cardSetSlots;
	}

	/**
	 * Liefert die Damage-Tokens
	 * 
	 * @return damageTokens
	 */
	public Img getDamageTokens() {
		return this.damageTokens;
	}

	/**
	 * Liefert den endGameButton
	 */
	public Button getEndGameButton() {
		return this.endGameButton;
	}

	/**
	 * Liefert die 1. Area mit den givenCards 0-4
	 * 
	 * @return 1. Area mit givenCards
	 */
	public HorizontalStack getGivenCardsArea1() {
		return this.givenCardsArea1;
	}




	/**
	 * Liefert die Life-Tokens
	 * 
	 * @return lifeTokens
	 */
	public Img[] getLifeTokens() {
		return this.lifeTokens;
	}

	/**
	 * Liefert den MenueButton
	 * 
	 * @return menueButton
	 */
	public Button getMenueButton() {
		return this.menueButton;
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
	 * Liefert den Power-Down-Button
	 * 
	 * @return btnPowerDown
	 */
	public ImgButton getPowerDownButton() {
		return this.powerDownButton;
	}

	/**
	 * Liefert den Ready-Button
	 * 
	 * @return btnReady
	 */
	public IButton getReadyButton() {
		return this.readyButton;
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
	 * Liefert die Area mit den setCards
	 * 
	 * @return givenCards
	 */
	public HorizontalStack getSetCardsArea() {
		return this.setCardsArea;
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
	 * Liefert den Timer für einen Spielschritt
	 * 
	 * @return the timer
	 */
	public StandardLabel getTimer() {
		return this.timer;
	}

	/**
	 * Liefert den upperPlayerStateStack
	 * 
	 * @return upperPlayerStateStack
	 */
	public HorizontalStack getUpperPlayerStateStack() {
		return this.upperPlayerStateStack;
	}

	/**
	 * Setzt die ChatArea
	 */
	public void setChatPresenter(ChatPresenter chatPresenter) {
		ChatArea chatPage = (ChatArea) chatPresenter.getPage();
		chatPage.setWidth(PlayerStatusArea.WIDTH - 10);
		chatPage.setHeight(PlayerStatusArea.CHAT_AREA_HEIGHT - 10);
		this.chatArea.addMembers(chatPage);
	}

	public void setDamageToken(int damageToken) {
		this.damageTokens.setSrc("robotStatus/damageToken" + damageToken + ".png");
	}

	public void setLifeToken(int lifeToken) {
		if(lifeToken < this.lifeTokens.length && lifeToken >= 0) {
			this.lifeTokens[lifeToken].setSrc("robotStatus/lTokenAway.png");
		}
	}
	
	/**
	 * Gibt an ob alle oberen 5 Kartenslots mit Programmierkarten belegt sind
	 * @return
	 */
	public boolean allCardsSet() {
		for(final CardSlot cardSlot : this.cardSetSlots) {
			if(cardSlot.isFree()) {
				return false;
			}
		}
		return true;
	}
	
	
}
