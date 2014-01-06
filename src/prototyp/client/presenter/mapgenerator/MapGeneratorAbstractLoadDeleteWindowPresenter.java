package prototyp.client.presenter.mapgenerator;

import java.util.HashMap;
import java.util.Map;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.user.UserPresenter;
import prototyp.client.service.MapGeneratorService;
import prototyp.client.service.MapGeneratorServiceAsync;
import prototyp.client.util.MapGeneratorLoadingRecord;
import prototyp.client.view.Page;
import prototyp.client.view.mapgenerator.MapGeneratorAbstractLoadDeleteWindow;
import prototyp.shared.round.PlayingBoard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;

/**
 * Presenter für das Löschen von Spielbrettern
 * 
 * @author Marcus
 * @version 1.0
 * 
 */
public abstract class MapGeneratorAbstractLoadDeleteWindowPresenter implements PagePresenter {

	/** MapGenerator Presenter */
	protected final MapGeneratorPresenter mapGeneratorPresenter;

	/** Async-Objekt */
	protected final MapGeneratorServiceAsync mapGeneratorService;

	/** Zugehörige Page */
	protected MapGeneratorAbstractLoadDeleteWindow page;

	/** HashMap mit PlayingBoards */
	protected Map<Integer, PlayingBoard> playingBoardMap;

	/** Angabe, ob der Spielbretter gelöscht werden sollen */
	protected boolean isDeleteRequest;

	/** Defaultgröße des Vorschaubildes */
	private final int PICTURE_SIZE = 350;

	/**
	 * Konstruktor
	 * 
	 * @param mapGeneratorPresenter
	 *            MapGenerator-Presenter
	 */
	public MapGeneratorAbstractLoadDeleteWindowPresenter(MapGeneratorPresenter mapGeneratorPresenter) {
		this.mapGeneratorService = GWT.create(MapGeneratorService.class);
		this.mapGeneratorPresenter = mapGeneratorPresenter;
	}

	/**
	 * Fügt die Listener hinzu
	 * 
	 * @return true
	 */
	protected boolean addListeners() {

		// Selektiert (Vorschaubild laden)
		this.page.getListGrid().addCellClickHandler(new CellClickHandler() {
			@Override
			public void onCellClick(CellClickEvent event) {
				MapGeneratorAbstractLoadDeleteWindowPresenter.this.page.getButtonAction().setDisabled(false);

				MapGeneratorLoadingRecord record = (MapGeneratorLoadingRecord) event.getRecord();
				showPicture(record.getPlayingboardImageName(), record.getPlayingboardWidth(), record.getPlayingboardHeight());
			}
		});

		// Wenn in der Suche was eingegeben wird
		this.page.getSearchItem().addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				MapGeneratorAbstractLoadDeleteWindowPresenter.this.page.getButtonAction().setDisabled(true);
				showPicture(null, 7, 7); // Default anzeigen
			}
		});

		// "Abort"-Button
		this.page.getButtonAbort().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MapGeneratorAbstractLoadDeleteWindowPresenter.this.page.clear();
			}

		});

		// SearchItem
		this.page.getSearchItem().addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				if (MapGeneratorAbstractLoadDeleteWindowPresenter.this.page.getSearchItem().getValue() != null
						&& MapGeneratorAbstractLoadDeleteWindowPresenter.this.playingBoardMap != null) {
					final Map<Integer, PlayingBoard> tmpMap = new HashMap<Integer, PlayingBoard>();
					final String tmpSearchText = MapGeneratorAbstractLoadDeleteWindowPresenter.this.page.getSearchItem()
							.getValue().toString().toLowerCase();
					// Map durchlaufen und filtern:
					for (Integer key : MapGeneratorAbstractLoadDeleteWindowPresenter.this.playingBoardMap.keySet()) {
						if (MapGeneratorAbstractLoadDeleteWindowPresenter.this.playingBoardMap.get(key).getName().toLowerCase()
								.startsWith(tmpSearchText)) {
							tmpMap.put(key, MapGeneratorAbstractLoadDeleteWindowPresenter.this.playingBoardMap.get(key));
						}
					}
					// Neue Map zeigen
					showPlayingBoardsInListGrid(tmpMap);
				} else {
					if (MapGeneratorAbstractLoadDeleteWindowPresenter.this.playingBoardMap != null) {
						showPlayingBoardsInListGrid(MapGeneratorAbstractLoadDeleteWindowPresenter.this.playingBoardMap);
					}
				}
			}
		});

		return true;
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

	/**
	 * Füllt die übergebene Map in das ListGrid
	 * 
	 * @return true
	 * @param Map
	 *            <Integer, Playingboard>
	 */
	protected boolean showPlayingBoardsInListGrid(Map<Integer, PlayingBoard> map) {
		// Record erstellen:
		MapGeneratorLoadingRecord tmpRecord[] = new MapGeneratorLoadingRecord[map.size()];
		int i = 0;
		for (final PlayingBoard playingBoard : map.values()) {
			tmpRecord[i] = new MapGeneratorLoadingRecord(playingBoard);
			i++;
		}

		// Anzeigen
		this.page.getListGrid().setData(tmpRecord);

		return true;
	}

	/**
	 * Holt die PlayingBoards aus der DB und füllt das ListGrid damit.
	 * 
	 * @return true
	 */
	protected boolean getPlayingBoardDataAndFillGrid() {
		this.mapGeneratorService.getPlayingBoards(UserPresenter.getInstance().getUser().getId(), UserPresenter.getInstance()
				.isAdmin(), this.isDeleteRequest, new AsyncCallback<Map<Integer, PlayingBoard>>() {
			@Override
			public void onFailure(Throwable caught) {
				// Ausgeben
				SC.say(Page.props.mapGeneratorPage_title(), caught.getMessage());
			}

			@Override
			public void onSuccess(Map<Integer, PlayingBoard> result) {
				if (result != null) {
					if (result.size() > 0) {
						MapGeneratorAbstractLoadDeleteWindowPresenter.this.playingBoardMap = result;

						// ListGrind füllen:
						showPlayingBoardsInListGrid(result);
					}
				}
			}
		});

		return true;
	}

	/**
	 * Zeigt das Spielbrett in der Vorschau an
	 * 
	 * @param filename
	 *            Dateiname des Spielbrettes
	 * @param width
	 *            Breite
	 * @param height
	 *            Höhe
	 */
	private void showPicture(String filename, int width, int height) {
		Img picture = MapGeneratorAbstractLoadDeleteWindowPresenter.this.page.getPreviewPicture();

		// Berechnung der Größen
		float actwidth = width * 50;
		float actheight = height * 50;
		float factor = 1;

		// Größe ermitteln
		if (actwidth == actheight) { // Ist gleich groß, muss dann nicht skaliert werden
			actwidth = PICTURE_SIZE;
			actheight = PICTURE_SIZE;
		} else if (actwidth > PICTURE_SIZE || actheight > PICTURE_SIZE) { // Muss angepasst werden
			while ((actwidth > PICTURE_SIZE) || (actheight > PICTURE_SIZE)) {
				// Verkleinerungsfaktor ausrechnen
				factor = (actwidth > actheight) ? 1 - ((actwidth - PICTURE_SIZE) / actwidth)
						: 1 - ((actheight - PICTURE_SIZE) / actheight);

				// Faktor anwenden
				actwidth *= factor;
				actheight *= factor;
			}
		}

		// Größe dem Bild zu weisen
		picture.setWidth((int) actwidth);
		picture.setHeight((int) actheight);

		// Neues Bild anzeigen
		if (filename == null || filename.equals("")) {
			picture.setSrc("ui/default_map_image.png");
		} else {
			picture.setSrc("maps/" + filename);
		}
	}
}
