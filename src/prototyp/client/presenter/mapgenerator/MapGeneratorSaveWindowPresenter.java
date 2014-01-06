package prototyp.client.presenter.mapgenerator;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.user.UserPresenter;
import prototyp.client.service.MapGeneratorService;
import prototyp.client.service.MapGeneratorServiceAsync;
import prototyp.client.view.Page;
import prototyp.client.view.mapgenerator.MapGeneratorSaveWindow;
import prototyp.client.view.mapgenerator.UnknownProgressWindow;
import prototyp.shared.field.Field;
import prototyp.shared.round.PlayingBoard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Dialog;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * Dient zum Speichern von Spielbrettern
 * 
 * @author Dennis (Verantwortlicher)
 * @version 1.0
 * 
 */
public class MapGeneratorSaveWindowPresenter implements PagePresenter {

	/** Felder */
	private Field[][] fields;

	/** RPC zum Server */
	private MapGeneratorServiceAsync mapGeneratorService = GWT.create(MapGeneratorService.class);

	/** Zugehörige Page */
	private MapGeneratorSaveWindow page;

	/** Presenter */
	private MapGeneratorPresenter presenter;

	/**
	 * Konstruktor
	 * 
	 * @param fields
	 *            Felder
	 * @param presenter
	 *            MapGenerator-Presenter
	 * @param saveName
	 *            Name des Spielbrettes
	 * @param saveDescription
	 *            Beschreibung des Spielbrettes
	 * @param saveDifficulty
	 *            Schwierigkeitsgrad
	 */
	public MapGeneratorSaveWindowPresenter(Field[][] fields, MapGeneratorPresenter presenter, String saveName,
			String saveDescription, String saveDifficulty) {
		this.page = new MapGeneratorSaveWindow();
		this.fields = fields;
		this.presenter = presenter;

		MapGeneratorSaveWindowPresenter.this.page.getNameItem().setValue(this.presenter.getSaveName());
		MapGeneratorSaveWindowPresenter.this.page.getDescriptionItem().setValue(this.presenter.getSaveDescription());
		MapGeneratorSaveWindowPresenter.this.page.getDifficultySelector().setValue(this.presenter.getSaveDifficulty());

		addListeners();
	}

	/**
	 * Fügt die Listener hinzu
	 * 
	 * @return true
	 */
	public boolean addListeners() {
		// Abbrechen
		this.page.getAbortButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MapGeneratorSaveWindowPresenter.this.page.clear();
			}
		});

		// Ok
		this.page.getOkButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (MapGeneratorSaveWindowPresenter.this.page.validate()
						&& !MapGeneratorSaveWindowPresenter.this.page.getNameItem().getDisplayValue().equals("")
						&& !MapGeneratorSaveWindowPresenter.this.page.getDescriptionItem().getDisplayValue().equals("")
						&& !MapGeneratorSaveWindowPresenter.this.page.getDifficultySelector().getValue().equals("")) {

					MapGeneratorSaveWindowPresenter.this.presenter.setSaveName(MapGeneratorSaveWindowPresenter.this.page
							.getNameItem().getDisplayValue());
					MapGeneratorSaveWindowPresenter.this.presenter.setSaveDescription(MapGeneratorSaveWindowPresenter.this.page
							.getDescriptionItem().getDisplayValue());
					MapGeneratorSaveWindowPresenter.this.presenter
							.setSaveDifficulty((String) MapGeneratorSaveWindowPresenter.this.page.getDifficultySelector()
									.getValue());

					MapGeneratorSaveWindowPresenter.this.page.destroy();

					final UnknownProgressWindow ukpw = new UnknownProgressWindow(Page.props
							.mapGeneratorPage_progressWindow_upload());

					/*
					 * hier wird das Playingboard erstellt
					 */
					new Timer() {
						@Override
						public void run() {
							MapGeneratorSaveWindowPresenter.this.mapGeneratorService.savePlayingBoard(new PlayingBoard(
									MapGeneratorSaveWindowPresenter.this.fields, MapGeneratorSaveWindowPresenter.this.page
											.getNameItem().getDisplayValue(), MapGeneratorSaveWindowPresenter.this.page
											.getDescriptionItem().getDisplayValue(),
									(String) MapGeneratorSaveWindowPresenter.this.page.getDifficultySelector().getValue(),
									-1), UserPresenter.getInstance().getUser().getId(), UserPresenter.getInstance().isAdmin(), new AsyncCallback<Boolean>() {
								@Override
								public void onFailure(Throwable caught) {

									/*
									 * zerstören
									 */
									ukpw.destroy();

									SC.warn(Page.props.global_title_error(), caught.getMessage(), new BooleanCallback() {
										@Override
										public void execute(Boolean value) {
										}
									}, new Dialog());
								}

								@Override
								public void onSuccess(Boolean result) {

									/*
									 * zerstören
									 */
									ukpw.destroy();

									SC.say(Page.props.mapGeneratorSaveWindow_success());

									/*
									 * Das spielfeld ist jetzt nicht verändert
									 */
									MapGeneratorSaveWindowPresenter.this.presenter.setChanged(false);
								}
							});

						}

					}.schedule(700);
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
}
