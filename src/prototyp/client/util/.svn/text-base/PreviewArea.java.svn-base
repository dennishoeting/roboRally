package prototyp.client.util;

import prototyp.client.presenter.pregame.RefereePagePresenter;
import prototyp.client.view.Page;
import prototyp.client.view.lobby.LobbyPage;
import prototyp.client.view.pregame.PreGameAbstractPage;
import prototyp.client.view.pregame.RefereePage;
import prototyp.shared.round.PlayingBoard;
import prototyp.shared.round.RoundInfo;

import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

/**
 * VorschauArea für die Spielbretter
 * 
 * @author Andreas
 * @version 1.0
 * 
 * @see {@link RefereePage}, {@link PreGameAbstractPage}, {@link LobbyPage}
 */
public class PreviewArea extends VerticalStack {

	/** Default-Spielbrett */
	private final static String DEFAULT_PICTURE = "ui/default_map_image.png";

	/** maximale Spielbretthöhe */
	private final static int PICTURE_SIZE = 180;

	/** Bild für das Spielbrett */
	private final Img previewMapImage;

	/** Titel des Spielbrettes */
	private final StandardLabel previewNameLabel;

	/** ListGridfelder */
	private final ListGridField previewPreferences, previewDescription, previewType;

	/** ListGrid mit der Statistik */
	private final ListGrid previewListGrid;

	/**
	 * Konstruktor
	 * 
	 * @param title
	 *            Titel der Area
	 * @param style
	 *            CSS-Style
	 */
	public PreviewArea(String title, String style) {
		super(title);
		this.setWidth(350);
		this.setHeight(475);
		this.setStyleName(style);

		this.previewNameLabel = new StandardLabel();
		this.previewNameLabel.setHeight(40);
		this.previewNameLabel.setStyleName("headline");

		this.previewMapImage = new Img(DEFAULT_PICTURE, PICTURE_SIZE, PICTURE_SIZE);
		this.previewMapImage.setBorder("1px solid yellow");
		this.previewMapImage.setLayoutAlign(Alignment.CENTER);
		this.previewMapImage.setTooltip(Page.props.preview_image_tooltip());

		this.previewListGrid = new ListGrid();
		this.previewPreferences = new ListGridField("optionTitle", Page.props.global_title_preference());
		this.previewDescription = new ListGridField("optionDescription", Page.props.global_title_description());
		this.previewType = new ListGridField("optionType", "Typ");
		this.previewListGrid.setWidth(350);
		this.previewListGrid.setHeight(210);
		this.previewListGrid.setShowAllRecords(true);
		this.previewListGrid.setSelectionType(SelectionStyle.SINGLE);
		this.previewListGrid.setWrapCells(true);
		this.previewListGrid.setFixedRecordHeights(false);
		this.previewListGrid.setFields(this.previewPreferences, this.previewDescription, this.previewType);
		this.previewListGrid.setGroupStartOpen(GroupStartOpen.ALL);
		this.previewListGrid.setGroupByField("optionType");
		this.previewListGrid.hideField("optionType");
		this.previewListGrid.setDataSource(new PreferencesHelperDataSource());
		this.previewListGrid.setShowDetailFields(false);

		this.addMembers(this.previewNameLabel, this.previewMapImage, this.previewListGrid);
		this.setAlign(Alignment.CENTER);

		// Default-Einstellungen zeigen
		this.showDefaultArea();
	}

	/**
	 * Zeigt das Spielbrett im Preview-Window an
	 * 
	 * @param board
	 *            Spielbrett
	 * @see RefereePagePresenter
	 */
	public void fillWithContent(PlayingBoard board) {
		// Label
		final String name = board.getName().length() < 20 ? board.getName() : board.getName().substring(0, 20) + "...";
		this.previewNameLabel.setContents(name);

		// ListGrid
		final RecordList previewData = new RecordList();
		previewData.add(new PlayingBoardPreferencesRecord(Page.props.preview_playingboard_maptype_title(), board.getName()));
		previewData.add(new PlayingBoardPreferencesRecord(Page.props.preview_playingboard_desciption_title(), board
				.getDescription()));
		previewData.add(new PlayingBoardPreferencesRecord(Page.props.preview_playingboard_size_title(), board.getWidth() + "*"
				+ board.getHeight()));
		previewData.add(new PlayingBoardPreferencesRecord(Page.props.preview_playingboard_difficulty_title(), board
				.getDifficulty()));
		previewData.add(new PlayingBoardPreferencesRecord(Page.props.preview_playingboard_numberOfCheckPoints_title(), board
				.getNumberOfCheckpoints()));
		previewData.add(new PlayingBoardPreferencesRecord(Page.props.preview_playingboard_maxPlayers_title(), board
				.getMaxPlayers()));
		previewData.add(new PlayingBoardPreferencesRecord(Page.props.mapGeneratorLoadingPopUp_listGridItem_nickname(), board
				.getNickname()));
		this.previewListGrid.setData(previewData);

		// Img
		this.setPicture(board.getImageFileName(), board.getWidth(), board.getHeight());
	}

	/**
	 * Zeigt alle Informationen im Preview-Window an
	 * 
	 * @param round
	 *            Spielrundeninformation
	 */
	public void fillWithContent(RoundInfo round) {
		String name = round.getRoundOption().getGameName().length() < 20 ? round.getRoundOption().getGameName() : round
				.getRoundOption().getGameName().substring(0, 20)
				+ "...";
		this.previewNameLabel.setContents(name);

		// ListGrid
		RecordList previewData = new RecordList();
		previewData.add(new RoundPreferencesRecord(1, Page.props.preview_playingboard_maptype_title(), round.getPlayingboard()
				.getName()));
		previewData.add(new RoundPreferencesRecord(1, Page.props.preview_playingboard_desciption_title(), round.getPlayingboard()
				.getDescription()));
		previewData.add(new RoundPreferencesRecord(1, Page.props.preview_playingboard_size_title(), round.getPlayingboard()
				.getWidth() + "*" + round.getPlayingboard().getHeight()));
		previewData.add(new RoundPreferencesRecord(1, Page.props.preview_playingboard_difficulty_title(), round.getPlayingboard()
				.getDifficulty()));
		previewData.add(new RoundPreferencesRecord(1, Page.props.preview_playingboard_numberOfCheckPoints_title(), round
				.getPlayingboard().getNumberOfCheckpoints()));
		previewData.add(new RoundPreferencesRecord(1, Page.props.preview_playingboard_maxPlayers_title(), round.getPlayingboard()
				.getMaxPlayers()));
		previewData.add(new RoundPreferencesRecord(1, Page.props.mapGeneratorLoadingPopUp_listGridItem_nickname(), round
				.getPlayingboard().getNickname()));
		previewData.add(new RoundPreferencesRecord(2, Page.props.preview_roundoption_playerSlots_title(), round.getRoundOption()
				.getPlayersSlots()));
		previewData.add(new RoundPreferencesRecord(2, Page.props.preview_roundoption_watcherSlots_title(), round.getRoundOption()
				.getWatchersSlots()));
		previewData.add(new RoundPreferencesRecord(2, Page.props.preview_roundoption_countDownTime_title(), round
				.getRoundOption().getCountDownTime() + " " + Page.props.global_title_seconds()));
		previewData.add(new RoundPreferencesRecord(2, Page.props.preview_roundoption_timeCountdownOn_title(), round
				.getRoundOption().isReduceCountdown()));
		previewData.add(new RoundPreferencesRecord(2, Page.props.preview_roundoption_laserOn_title(), round.getRoundOption()
				.isLasersOn()));
		previewData.add(new RoundPreferencesRecord(2, Page.props.preview_roundoption_robotShootsOn_title(), round
				.getRoundOption().isRobotShootsOn()));
		previewData.add(new RoundPreferencesRecord(2, Page.props.preview_roundoption_alwaysPusherOn_title(), round
				.getRoundOption().isAlwaysPushersOn()));
		previewData.add(new RoundPreferencesRecord(2, Page.props.preview_roundoption_alwaysPresserOn_title(), round
				.getRoundOption().isAlwaysCompactorsOn()));
		previewData.add(new RoundPreferencesRecord(2, Page.props.preview_roundoption_alwaysCBOn_title(), round.getRoundOption()
				.isAlwaysConveyorBeltsOn()));
		previewData.add(new RoundPreferencesRecord(2, Page.props.preview_roundoption_alwaysGearOn_title(), round.getRoundOption()
				.isAlwaysGearsOn()));
		this.previewListGrid.setData(previewData);

		// Img
		this.setPicture(round.getPlayingboard().getImageFileName(), round.getPlayingboard().getWidth(), round.getPlayingboard()
				.getHeight());
	}

	/**
	 * Setzt die Defaulteinstellungen
	 */
	public void showDefaultArea() {
		// Label
		this.previewNameLabel.setContents("???");

		// ListGrid
		this.previewListGrid.setData(new RecordList());

		// Img
		this.setPicture(null, 0, 0);
	}

	/**
	 * Setzt das Bild ins Img
	 * 
	 * @param fileName
	 *            Dateiname des Bildes
	 * @param height
	 *            Höhe des Spielbrettes
	 * @param width
	 *            Breite des Spielbrettes
	 */
	private void setPicture(final String fileName, int width, int height) {
		if (fileName != null && !fileName.equals("")) {
			this.previewMapImage.setCursor(Cursor.HAND);
			// Größe festlegen
			if (width == height) {
				this.previewMapImage.setWidth(PICTURE_SIZE);
			} else {
				float newWidth = (((float) PICTURE_SIZE / ((float) height * 50)) * ((float) width * 50));
				if (newWidth < 340) {
					this.previewMapImage.setWidth((int) newWidth);
				} else {
					this.previewMapImage.setWidth(340);
				}
			}

			this.previewMapImage.setSrc("maps/" + fileName);
			this.previewMapImage.setShowHover(true);
		} else {
			this.previewMapImage.setCursor(Cursor.DEFAULT);
			this.previewMapImage.setSrc(DEFAULT_PICTURE);
			this.previewMapImage.setShowHover(false);
			this.previewMapImage.setWidth(PICTURE_SIZE);
			this.previewMapImage.setHeight(PICTURE_SIZE);
		}
	}

	/**
	 * Liefert das Image
	 * 
	 * @return the previewMapImage
	 */
	public Img getPreviewMapImage() {
		return previewMapImage;
	}

	/**
	 * Liefert das Label
	 * 
	 * @return the previewNameLabel
	 */
	public StandardLabel getPreviewNameLabel() {
		return previewNameLabel;
	}

	/**
	 * Liefert das ListGrid
	 * 
	 * @return the previewListGrid
	 */
	public ListGrid getPreviewListGrid() {
		return previewListGrid;
	}
}
