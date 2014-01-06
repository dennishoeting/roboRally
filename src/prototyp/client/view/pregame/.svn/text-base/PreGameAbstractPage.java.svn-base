package prototyp.client.view.pregame;

import prototyp.client.util.Button;
import prototyp.client.util.Headline;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.PreviewArea;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

/**
 * OberKlasse für PreGameGameInitiatorPage und PreGamePage
 * 
 * @author Marcus, Timo (Verantwortlicher), Jannik (Verantwortlicher), Mischa
 * @version 1.0
 * @version 1.1 -Roboter ListGridField raus, userID ListGridFiel rein -Timo 26.10.10
 * @version 1.2 - Properties hinzugefügt. -Timo 09.11.10
 * @version 1.3 - playerOptions verschönert + neue Methoden - Mischa 29.12.10
 * 
 */
public class PreGameAbstractPage extends Page {
	// Attribute
	protected HorizontalStack buttonArea;
	protected Button buttonCloseTab;
	protected HorizontalStack contentArea, chatAndColorArea, colorHStack, roboHStack;
	protected VerticalStack leftArea, rightArea, mainArea, playerListArea, internalChatArea, playerOptionsArea;
	protected ListGridField userIDListGridField, nameListGridField, colorListGridField, readyListGridField,
			observerListGridField;
	protected ListGrid playerList;
	protected PreviewArea previewArea;
	protected SelectItem colorChooser;
	protected DynamicForm colorChooserForm;

	/**
	 * Konstruktor
	 * 
	 * @param title
	 *            Titel der Seite
	 */
	protected PreGameAbstractPage(String title) {
		super(title);

		/*
		 * Areas
		 */
		this.mainArea = new VerticalStack(Integer.valueOf(Page.props.global_marginBetweenStackAreas()), 0);
		this.contentArea = new HorizontalStack(Integer.valueOf(Page.props.global_marginBetweenStackAreas()), 0);
		this.leftArea = new VerticalStack(Integer.valueOf(Page.props.global_marginBetweenStackAreas()), 0);
		this.leftArea.setWidth(300);
		this.rightArea = new VerticalStack(Integer.valueOf(Page.props.global_marginBetweenStackAreas()), 0);
		this.rightArea.setWidth(300);
		this.playerListArea = new VerticalStack(Page.props.preGamePage_playerListArea());
		this.playerListArea.setStyleName("preGameArea");
		this.chatAndColorArea = new HorizontalStack(10, 0);
		this.chatAndColorArea.setHeight(217);
		this.internalChatArea = new VerticalStack(Page.props.preGamePage_chat());
		this.internalChatArea.setWidth(350);
		this.internalChatArea.setHeight(217);
		this.internalChatArea.setStyleName("internalChatArea");
		this.playerOptionsArea = new VerticalStack(Page.props.preGamePage_playerOptionsArea());
		this.playerOptionsArea.setWidth(171);
		this.playerOptionsArea.setHeight(217);
		this.playerOptionsArea.setStyleName("preGameArea");
		this.colorHStack = new HorizontalStack(0, 0);
		this.colorHStack.setAlign(Alignment.CENTER);
		this.colorHStack.setHeight(30);
		this.roboHStack = new HorizontalStack(0, 0);
		this.roboHStack.setAlign(Alignment.CENTER);
		this.roboHStack.setHeight(10);

		this.previewArea = new PreviewArea(Page.props.preGamePage_previewArea(), "preGameArea");

		this.buttonArea = new HorizontalStack(Integer.valueOf(Page.props.global_marginInStackAreas()), 0);
		this.buttonArea.setHeight(30);

		/*
		 * Playerliste
		 */
		this.playerList = new ListGrid();
		this.playerList.setWidth(520);
		this.playerList.setHeight(175);
		this.userIDListGridField = new ListGridField("userID", Page.props.preGamePage_userIDListGridField());
		this.userIDListGridField.setHidden(true);
		this.nameListGridField = new ListGridField("nickname", Page.props.preGamePage_nameListGridField());
		this.colorListGridField = new ListGridField("color", Page.props.preGamePage_colorListGridField());
		this.colorListGridField.setType(ListGridFieldType.IMAGE);
		this.readyListGridField = new ListGridField("ready", Page.props.preGamePage_readyListGridField());
		this.observerListGridField = new ListGridField("observer", Page.props.preGamePage_observerListGridField());
		this.playerList.setFields(this.userIDListGridField, this.nameListGridField, this.colorListGridField,
				this.readyListGridField, this.observerListGridField);

		/*
		 * Preview (auf der rechten Seite)
		 */

		// Buttons
		this.buttonCloseTab = new Button(Page.props.global_title_abort(), Integer.valueOf(Page.props.global_buttonWidth()));
		this.buttonArea.addMember(this.buttonCloseTab);

		/*
		 * PlayerOptionsArea
		 */
		// ComboBox
		this.colorChooser = new SelectItem();
		this.colorChooser.setWidth(110);
		this.colorChooser.setShowTitle(false);
		this.colorChooser.setDefaultToFirstOption(true);
		this.colorChooserForm = new DynamicForm();
		this.colorChooserForm.setFields(this.colorChooser);

		/*
		 * Areas elegant zu einem wunderschönen View vereinen
		 */
		this.buttonArea.setLayoutAlign(Alignment.CENTER);
		this.playerListArea.addMember(this.playerList);
		this.colorHStack.addMember(this.colorChooserForm);
		this.playerOptionsArea.addMember(this.colorHStack);
		this.chatAndColorArea.addMembers(this.internalChatArea, this.playerOptionsArea);
		this.leftArea.addMembers(this.playerListArea, this.chatAndColorArea);
		this.rightArea.addMember(this.previewArea);
		this.contentArea.addMembers(this.leftArea, this.rightArea);
		this.mainArea.addMembers(this.contentArea, this.buttonArea);
		setMainStack(this.mainArea);
	}

	/*
	 * *************************************************************************
	 * Es folgen die Getter und Setter
	 */

	/**
	 * Liefert den Close-Tab-Button
	 * 
	 * @return buttonCloseTab
	 */
	public Button getButtonCloseTab() {
		return this.buttonCloseTab;
	}

	/**
	 * Gibt die Auswahlbox für die Farbauswahl zurück
	 * 
	 * @return colorChooser
	 */
	public SelectItem getColorChooser() {
		return this.colorChooser;
	}

	/**
	 * Gibt den HorizontalStack zum Zeichnen des ColorChoosers zurück
	 * 
	 * @return colorHStack
	 */
	public HorizontalStack getColorChooserStack() {
		return this.colorHStack;
	}

	/**
	 * Liefert die Headline
	 * 
	 * @return headline
	 */
	public Headline getHeadline() {
		return this.headline;
	}

	/**
	 * Liefert die InternalChatArea
	 * 
	 * @return internalChatArea
	 */
	public VerticalStack getInternalChatArea() {
		return this.internalChatArea;
	}

	/**
	 * Liefert die Playerlist
	 * 
	 * @return playerList
	 */
	public ListGrid getMenueGrid() {
		return this.playerList;
	}

	/**
	 * Gibt die PlayerOptionsArea zurück
	 * 
	 * @return playerOptiosArea
	 */
	public VerticalStack getPlayerOptionsArea() {
		return this.playerOptionsArea;
	}

	/**
	 * Liefert die Preview-Area
	 * 
	 * @return previewArea
	 */
	public PreviewArea getPreviewArea() {
		return this.previewArea;
	}

	/**
	 * Gibt den HorizontalStack zum Zeichnen der Roboter zurück
	 * 
	 * @return roboHStack
	 */
	public HorizontalStack getRobotorStack() {
		return this.roboHStack;
	}
}
