package prototyp.client.presenter.mapgenerator;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.view.Page;
import prototyp.client.view.mapgenerator.MapGeneratorCreateWindow;
import prototyp.shared.exception.mapGenerator.AspectRatioException;
import prototyp.shared.exception.mapGenerator.MapSizeException;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyDownEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyDownHandler;

/**
 * Presenter für die Transformationen
 * 
 * @author Marcus
 * @version 1.0
 * 
 */
public class MapGeneratorChangeMapSizeWindowPresenter implements PagePresenter {
	/** Speichern des mapGeneratorPresenter */
	private MapGeneratorPresenter mapGeneratorPresenter;

	/** Speichern des PopUps */
	private MapGeneratorCreateWindow page;

	/**
	 * Konstruktor
	 * 
	 * @param mapGeneratorPresenter
	 *            mapGeneratorPresenter
	 */
	public MapGeneratorChangeMapSizeWindowPresenter(MapGeneratorPresenter mapGeneratorPresenter) {
		this.page = new MapGeneratorCreateWindow(Page.props.mapGeneratorChangeSizeWindow_title());
		this.mapGeneratorPresenter = mapGeneratorPresenter;

		this.page.getWidthItem().setValue(this.mapGeneratorPresenter.getFields()[0].length);
		this.page.getHeightItem().setValue(this.mapGeneratorPresenter.getFields().length);

		addListeners();
	}

	/**
	 * Fügt die Listener hinzu
	 * 
	 * @return
	 */
	private boolean addListeners() {
		/*
		 * beim Klicken auf "Abbruch"
		 */
		this.page.getButtonAbort().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MapGeneratorChangeMapSizeWindowPresenter.this.page.clear();
			}
		});

		/*
		 * Beim Klicken auf "OK"
		 */
		this.page.getButtonOk().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!MapGeneratorChangeMapSizeWindowPresenter.this.page.getWidthItem().getDisplayValue().equals("")
						|| !MapGeneratorChangeMapSizeWindowPresenter.this.page.getHeightItem().getDisplayValue().equals("")) {

					changeSizeInMapGeneratorPresenter();
				}
			}
		});

		/*
		 * Beim Drücken von Enter, wenn in "Breite"
		 */
		this.page.getWidthItem().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getKeyName().equals("Enter")
						&& (!MapGeneratorChangeMapSizeWindowPresenter.this.page.getWidthItem().getDisplayValue().equals("") || !MapGeneratorChangeMapSizeWindowPresenter.this.page
								.getHeightItem().getDisplayValue().equals(""))) {
					changeSizeInMapGeneratorPresenter();
				}
			}

		});

		/**
		 * Beim klicken von Enter, wenn in "Höhe"
		 */
		this.page.getHeightItem().addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getKeyName().equals("Enter")
						&& (!MapGeneratorChangeMapSizeWindowPresenter.this.page.getWidthItem().getDisplayValue().equals("") || !MapGeneratorChangeMapSizeWindowPresenter.this.page
								.getHeightItem().getDisplayValue().equals(""))) {
					changeSizeInMapGeneratorPresenter();
				}
			}
		});

		return true;
	}

	/**
	 * Erstellt die neue Map
	 */
	@SuppressWarnings("finally")
	private void changeSizeInMapGeneratorPresenter() {

		try {

			/**
			 * Speichern von Breite und Höhe
			 */
			final int width = Integer.valueOf(this.page.getWidthItem().getDisplayValue());
			final int height = Integer.valueOf(this.page.getHeightItem().getDisplayValue());

			/*
			 * Abfrage diverser Standardfehlerfälle
			 */
			if (width > 24 || height > 24) {
				throw new MapSizeException(MapSizeException.TOO_LARGE);
			}

			if (width < 5 || height < 5) {
				throw new MapSizeException(MapSizeException.TOO_SMALL);
			}

			if (Math.max((double) width, (double) height) / Math.min((double) width, (double) height) > 2) {
				throw new AspectRatioException();
			}

			/*
			 * neue Karte erstellen
			 */
			this.mapGeneratorPresenter.changePlayingBoardSize(width, height);

			/*
			 * Fenster weg
			 */
			this.page.destroy();

		} catch (NumberFormatException numberFormatException) {
			SC.say(Page.props.mapGeneratorPage_title(), Page.props.mapGeneratorPopUp_numberFormatException());
		} catch (Throwable e) {
			SC.say(Page.props.mapGeneratorPage_title(), e.getMessage());
		} finally {
			return;
		}
	}

	@Override
	/**
	 * Liefert die Page
	 */
	public Canvas getPage() {
		return this.page;
	}

}
