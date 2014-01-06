package prototyp.client.view.mapgenerator;

import prototyp.client.util.Button;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

public abstract class MapGeneratorAbstractLoadDeleteWindow extends Window {
	// Areas
	private final HorizontalStack buttonArea;
	private final HorizontalStack mainAreaOuter;

	/** Vorschaubild */
	private final Img previewPicture = new Img("ui/default_map_image.png", 350, 350);
	private final VerticalStack mainAreaInner, pictureArea;

	// Forms
	private final DynamicForm form;

	// Listgrid mit Fields
	private final ListGrid listGrid;

	// Widgets
	private final Button buttonAction, buttonAbort;
	private final ListGridField playingBoardID, name, size, numberOfCheckpoints, maxPlayers, imageFileName, nickname;
	private final TextItem searchItem;

	/**
	 * Konstruktor
	 */
	public MapGeneratorAbstractLoadDeleteWindow() {
		setIsModal(true);
		setShowModalMask(true);

		this.mainAreaOuter = new HorizontalStack(Integer.valueOf(Page.props.global_marginBetweenStackAreas()), 0);
		this.mainAreaInner = new VerticalStack(Integer.valueOf(Page.props.global_marginBetweenStackAreas()), 0);
		this.pictureArea = new VerticalStack(0, 0);
		this.pictureArea.setHeight(365);
		this.pictureArea.setWidth(365);
		this.pictureArea.setAlign(Alignment.CENTER);
		this.buttonArea = new HorizontalStack(Integer.valueOf(Page.props.global_marginInStackAreas()), 0);
		this.buttonArea.setWidth(560);
		this.buttonArea.setAlign(Alignment.CENTER);
		this.buttonAction = new Button(this.getButtonActionText());
		this.buttonAction.setDisabled(true);
		this.buttonAction.setAlign(Alignment.CENTER);
		this.buttonAbort = new Button(Page.props.mapGeneratorLoadingPopUp_abortButton());
		this.buttonAbort.setAlign(Alignment.CENTER);
		this.buttonArea.addMember(this.buttonAction);
		this.buttonArea.addMember(this.buttonAbort);

		// Form
		this.form = new DynamicForm();
		this.searchItem = new TextItem();
		this.searchItem.setTitle(Page.props.mapGeneratorLoadingPopUp_searchItem_title());
		this.searchItem.setWidth(520);
		this.form.setFields(this.searchItem);

		// ListgridFields
		this.playingBoardID = new ListGridField("playingBoardID", Page.props.mapGeneratorLoadingPopUp_listGridItem_ID());
		this.playingBoardID.setHidden(true);
		this.name = new ListGridField("name", Page.props.mapGeneratorLoadingPopUp_listGridItem_name());
		this.name.setWidth("30%");
		this.size = new ListGridField("size", Page.props.mapGeneratorLoadingPopUp_listGridItem_size());
		this.size.setWidth("10%");
		this.size.setAlign(Alignment.CENTER);
		this.numberOfCheckpoints = new ListGridField("numberOfCheckpoints",
				Page.props.mapGeneratorLoadingPopUp_listGridItem_numberOfCheckpoints());
		this.numberOfCheckpoints.setWidth("18%");
		this.numberOfCheckpoints.setAlign(Alignment.CENTER);
		this.maxPlayers = new ListGridField("maxPlayers", Page.props.mapGeneratorLoadingPopUp_listGridItem_maxPlayers());
		this.maxPlayers.setWidth("17%");
		this.maxPlayers.setAlign(Alignment.CENTER);
		this.imageFileName = new ListGridField("imageFileName", Page.props.mapGeneratorLoadingPopUp_listGridItem_imageFileName());
		this.imageFileName.setHidden(true);
		this.nickname = new ListGridField("nickname", Page.props.mapGeneratorLoadingPopUp_listGridItem_nickname());
		this.nickname.setWidth("25%");

		// ListGrid-Einstellungen
		this.listGrid = new ListGrid();
		this.listGrid.setWidth(560);
		this.listGrid.setHeight(300);
		this.listGrid.setFields(new ListGridField[] { this.playingBoardID, this.name, this.size, this.numberOfCheckpoints,
				this.maxPlayers, this.imageFileName, this.nickname });
		this.listGrid.setShowAllRecords(true);
		this.listGrid.setCanEdit(false);
		this.listGrid.setCanResizeFields(true);
		this.listGrid.setWrapCells(true);
		this.listGrid.setFixedRecordHeights(false);

		// Anfangs nach Namen aufsteigend sortieren
		this.listGrid.sort("name", SortDirection.ASCENDING);

		this.mainAreaInner.addMember(this.form);
		this.mainAreaInner.addMember(this.listGrid);
		this.mainAreaInner.addMember(this.buttonArea);

		this.mainAreaOuter.addMember(this.mainAreaInner);

		this.previewPicture.setMargin(6);
		this.previewPicture.setBorder("1px solid yellow");
		this.previewPicture.setLayoutAlign(Alignment.CENTER);
		this.previewPicture.setLayoutAlign(VerticalAlignment.CENTER);

		this.pictureArea.addMember(this.previewPicture);
		this.mainAreaOuter.addMember(this.pictureArea);
		this.mainAreaOuter.setTop(20);

		this.addChild(this.mainAreaOuter);

		this.setLeft(200);
		this.setTop(200);
		this.setWidth(950);
		this.setHeight(420);
		setTitle(Page.props.mapGeneratorLoadingPopUp_title());

		centerInPage();

		setCanDrag(false);
		setCanDragReposition(false);

		show();
	}

	/**
	 * Liefert die Beschriftung des Action-Buttons
	 * 
	 * @return String
	 */
	protected abstract String getButtonActionText();

	/**
	 * Liefert den Abort-Button
	 * 
	 * @return buttonAbort
	 */
	public Button getButtonAbort() {
		return this.buttonAbort;
	}

	/**
	 * Liefert den OK-Button
	 * 
	 * @return buttonOk
	 */
	public Button getButtonAction() {
		return this.buttonAction;
	}

	/**
	 * Liefert das ListGrid mit den Spielbrettern
	 * 
	 * @return listGrid
	 */
	public ListGrid getListGrid() {
		return this.listGrid;
	}

	/**
	 * Liefert das Vorschau-Img
	 * 
	 * @return Img
	 */
	public Img getPreviewPicture() {
		return this.previewPicture;
	}

	/**
	 * Liefert das Textfeld für die Suche
	 * 
	 * @return searchItem
	 */
	public TextItem getSearchItem() {
		return this.searchItem;
	}

}
