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
 * Presenter für das PopUp Fenster um beim Map Generator die Größe auszuwählen
 * 
 * @author Timo, Dennis
 */
public class MapGeneratorNewMapWindowPresenter implements PagePresenter {
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
	public MapGeneratorNewMapWindowPresenter(MapGeneratorPresenter mapGeneratorPresenter) {
		this.page = new MapGeneratorCreateWindow(Page.props.mapGeneratorPopUp_title());
		this.mapGeneratorPresenter = mapGeneratorPresenter;
		
		this.page.getWidthItem().setValue(10);
		this.page.getHeightItem().setValue(10);
		
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
				MapGeneratorNewMapWindowPresenter.this.page.clear();
			}
		});

		/*
		 * Beim Klicken auf "OK"
		 */
		this.page.getButtonOk().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!MapGeneratorNewMapWindowPresenter.this.page.getWidthItem().getDisplayValue().equals("")
						|| !MapGeneratorNewMapWindowPresenter.this.page.getHeightItem().getDisplayValue().equals("")) {
					createInMapGeneratorPresenter();
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
						&& (!MapGeneratorNewMapWindowPresenter.this.page.getWidthItem().getDisplayValue().equals("") || !MapGeneratorNewMapWindowPresenter.this.page
								.getHeightItem().getDisplayValue().equals(""))) {
					createInMapGeneratorPresenter();
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
						&& (!MapGeneratorNewMapWindowPresenter.this.page.getWidthItem().getDisplayValue().equals("") || !MapGeneratorNewMapWindowPresenter.this.page
								.getHeightItem().getDisplayValue().equals(""))) {
					createInMapGeneratorPresenter();
				}
			}
		});

		return true;
	}

	/**
	 * Erstellt die neue Map
	 */
	@SuppressWarnings("finally")
	private void createInMapGeneratorPresenter() {

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
			this.mapGeneratorPresenter.createNew(width, height);

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
