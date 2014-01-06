package prototyp.client.view.lobby;

import prototyp.client.presenter.lobby.LobbyPagePresenter;
import prototyp.client.util.Button;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.PreviewArea;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

/**
 * LobbyPage (View) mit einer Liste der erstellten Spiele, einem globalen Chat, einer Spielbrettvorschau sowie diverse Such- und
 * Auswahlkriterien.
 * 
 * @author Andreas (Verantwortlicher), Robert, Timo, Jens
 * @version 1.2
 * 
 * @see LobbyPagePresenter
 */
public class LobbyPage extends Page {

	// Attribute
	private final Button buttonCreateRound, buttonJoinRound, buttonWatchRound;
	private final ListGrid gameListGrid, onlineUsersListGrid;
	private final VerticalStack leftArea, gameListArea;
	private final HorizontalStack mainArea, buttonArea, globalChatArea;
	private final ListGridField gameName, mapName, playerCount, watcherCount, difficulty, timeElapsed, nickname;
	private final PreviewArea previewArea;

	/**
	 * Konstruktor
	 */
	public LobbyPage() {
		super(Page.props.lobbyPage_title());

		// Areas
		this.mainArea = new HorizontalStack(Integer.valueOf(Page.props.global_marginBetweenStackAreas()), 0);
		this.leftArea = new VerticalStack(Integer.valueOf(Page.props.global_marginBetweenStackAreas()), 0);
		this.previewArea = new PreviewArea(Page.props.global_title_preview(), "lobbyArea");
		this.gameListArea = new VerticalStack(Page.props.lobbyPage_availableRounds_title());
		this.gameListArea.setStyleName("lobbyArea");
		this.buttonArea = new HorizontalStack(Integer.valueOf(Page.props.global_marginInStackAreas()), 0);
		this.buttonArea.setHeight(5);
		this.buttonArea.setStyleName("buttonArea");
		this.globalChatArea = new HorizontalStack(Page.props.global_title_chat());
		this.globalChatArea.setWidth(520);
		this.globalChatArea.setStyleName("lobbyArea");

		// ListGrid für die erstellten Spiele
		this.gameListGrid = new ListGrid();
		this.gameListGrid.setEmptyMessage(Page.props.lobbyPage_gameListGrid_emptyMessage());
		this.gameListGrid.setWidth(513);
		this.gameListGrid.setHeight(173);
		this.gameListGrid.setShowAllRecords(true);
		this.gameListGrid.setSelectionType(SelectionStyle.SINGLE);
		this.gameListGrid.setWrapCells(true);
		this.gameListGrid.setFixedRecordHeights(false);
		this.gameListGrid.setShowRowNumbers(true);
		this.gameListGrid.setSortField(5);
		this.gameName = new ListGridField("gameName", Page.props.preGamePage_gameName());
		this.mapName = new ListGridField("mapName", Page.props.lobbyPage_menueGrid_mapName_text());
		this.playerCount = new ListGridField("playerCount", Page.props.lobbyPage_menueGrid_playerCount_text());
		this.playerCount.setAlign(Alignment.CENTER);
		this.watcherCount = new ListGridField("watcherCount", Page.props.lobbyPage_menueGrid_watcherCount_text());
		this.watcherCount.setAlign(Alignment.CENTER);
		this.difficulty = new ListGridField("difficulty", Page.props.lobbyPage_menueGrid_difficulty_text());
		this.timeElapsed = new ListGridField("timeElapsed", Page.props.lobbyPage_menueGrid_timeElapsed_text());
		this.timeElapsed.setType(ListGridFieldType.IMAGE);
		this.timeElapsed.setAlign(Alignment.CENTER);
		this.timeElapsed.setWidth(45);
		this.gameListGrid.setFields(this.gameName, this.mapName, this.playerCount, this.watcherCount, this.difficulty,
				this.timeElapsed);

		// Buttons
		this.buttonCreateRound = new Button(Page.props.lobbyPage_buttonCreateRound_text(), 150); // "Spielrunde erstellen"
		this.buttonJoinRound = new Button(Page.props.lobbyPage_buttonJoinRound_text(), 150); // "Spielrunde beitreten"
		this.buttonJoinRound.setDisabled(true); // Wird erst enabled, wenn man
												// eine Runde auswählt
		this.buttonWatchRound = new Button(Page.props.lobbyPage_buttonWatchRound_text(), 150); // "Spielrunde beobachten"
		this.buttonWatchRound.setDisabled(true); // Wird erst enabled, wenn man
													// eine Runde auswählt

		// ListGrid für die Online Users
		this.onlineUsersListGrid = new ListGrid();
		this.onlineUsersListGrid.setWidth(100);
		this.onlineUsersListGrid.setHeight(159);
		this.onlineUsersListGrid.setSelectionType(SelectionStyle.SINGLE);
		this.onlineUsersListGrid.setShowHeader(false);
		this.onlineUsersListGrid.setFixedRecordHeights(false);
		this.onlineUsersListGrid.setWrapCells(true);

		// Fields
		this.nickname = new ListGridField("nickname", Page.props.global_title_nickname());
		this.onlineUsersListGrid.setFields(this.nickname);
		// Nach Nickname absteigend sortieren.
		this.onlineUsersListGrid.sort("nickname", SortDirection.ASCENDING);

		// Areas zusammenfügen
		this.gameListArea.addMember(this.gameListGrid);
		this.buttonArea.addMembers(this.buttonCreateRound, this.buttonJoinRound, this.buttonWatchRound);
		this.leftArea.addMembers(this.gameListArea, this.buttonArea, this.globalChatArea);
		this.mainArea.addMembers(this.leftArea, this.previewArea);
		setMainStack(this.mainArea);
	}

	// Getter

	/**
	 * Liefert den Button zum Erstellen einer Runde
	 * 
	 * @return buttonCreateRound
	 */
	public Button getButtonCreateRound() {
		return this.buttonCreateRound;
	}

	/**
	 * Liefert den Button zum Beitreten einer Runde
	 * 
	 * @return buttonJoinRound
	 */
	public Button getButtonJoinRound() {
		return this.buttonJoinRound;
	}

	/**
	 * Liefert den Button zum Beobachten einer Runde
	 * 
	 * @return buttonWatchRound
	 */
	public Button getButtonWatchRound() {
		return this.buttonWatchRound;
	}

	/**
	 * Liefert die Tabelle mit den aktuellen Runden
	 * 
	 * @return gameListGrid
	 */
	public ListGrid getGameListGrid() {
		return this.gameListGrid;
	}

	/**
	 * Liefert die ChatArea
	 * 
	 * @return globalChatArea
	 */
	public HorizontalStack getGlobalChatArea() {
		return this.globalChatArea;
	}

	/**
	 * Übergibt das ListGrid, in dem alle User, die Online sind angezeigt werden sollten. Damit man dieses ListGrid editieren kann
	 * oder was auch immer.
	 * 
	 * @return onlineUsersListGrid
	 */
	public ListGrid getOnlineUserListGrid() {
		return this.onlineUsersListGrid;
	}

	/**
	 * Liefert die PreviewArea
	 * 
	 * @return previewArea
	 */
	public PreviewArea getPreviewArea() {
		return this.previewArea;
	}

}