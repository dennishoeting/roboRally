package prototyp.client.presenter.mapgenerator;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.view.Page;
import prototyp.client.view.mapgenerator.MapGeneratorTransformWindow;
import prototyp.shared.field.Field;
import prototyp.shared.field.FieldTransformation;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * Presenter für das TransformWindow
 * 
 * @author Marcus
 * @version 1.0
 */
public class MapGeneratorTransformWindowPresenter implements PagePresenter {

	/** der MapGeneratorPresenter */
	private final MapGeneratorPresenter presenter;

	/** die zugehörige Page */
	private final MapGeneratorTransformWindow page;

	/**
	 * Konstruktor
	 * 
	 * @param presenter
	 *            der MapGeneratorPresenter
	 */
	public MapGeneratorTransformWindowPresenter(final MapGeneratorPresenter presenter) {
		this.presenter = presenter;
		this.page = new MapGeneratorTransformWindow();
		this.addListeners();
	}

	/**
	 * Hinzufügen von Eventhandlern
	 * 
	 * @return
	 */
	private boolean addListeners() {

		/*
		 * Ereignisbehandlung für Button "Größe ändern"
		 */
		this.page.getButtonChangeSize().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new MapGeneratorChangeMapSizeWindowPresenter(presenter);
				page.destroy();
			}
		});

		/*
		 * Ereignisbehandlung für Button "X-Achse spiegeln"
		 */
		this.page.getButtonFlipX().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				flipX();
			}
		});

		/*
		 * Ereignisbehandlung für Button "Y-Achse spiegeln"
		 */
		this.page.getButtonFlipY().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				flipY();
			}
		});

		/*
		 * Ereignisbehandlung für Button "90 grad rechts rotieren"
		 */
		this.page.getButtonRotateRight().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				rotateRight();
			}
		});

		/*
		 * Ereignisbehandlung für Button "90 grad links rotieren"
		 */
		this.page.getButtonRotateLeft().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				rotateLeft();
			}
		});

		/*
		 * Ereignisbehandlung für Button "180 grad rotieren"
		 */
		this.page.getButtonRotateFully().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				rotateAround();
			}
		});

		return true;
	}

	/**
	 * Spiegelt das Spielbrett an der X-Achse
	 */
	private void flipX() {
		final Field[][] oldFields = presenter.getFields();

		for (int i = 0; i < oldFields.length / 2; i++) {
			for (int j = 0; j < oldFields[i].length; j++) {
				final Field curr = oldFields[i][j];

				oldFields[i][j] = oldFields[oldFields.length - i - 1][j].transform(FieldTransformation.X_AXIS);
				oldFields[oldFields.length - i - 1][j] = curr.transform(FieldTransformation.X_AXIS);

			}
		}

		/*
		 * Falls Höhe des Bretts ungerade wird die mittlere Reihe transformiert
		 */
		if (oldFields.length % 2 == 1) {
			final int last = oldFields.length / 2;
			for (int j = 0; j < oldFields[last].length; j++) {
				oldFields[last][j] = oldFields[last][j].transform(FieldTransformation.X_AXIS);
			}
		}

		presenter.transformFields(oldFields, Page.props.mapGeneratorPage_progressWindow_flipX());

		this.page.destroy();
	}

	/**
	 * Spiegelt das Spielbrett an der Y-Achse
	 */
	private void flipY() {
		final Field[][] oldFields = presenter.getFields();

		for (int i = 0; i < oldFields.length; i++) {
			for (int j = 0; j < oldFields[i].length / 2; j++) {
				final Field curr = oldFields[i][j];

				oldFields[i][j] = oldFields[i][oldFields[i].length - j - 1].transform(FieldTransformation.Y_AXIS);
				oldFields[i][oldFields[i].length - j - 1] = curr.transform(FieldTransformation.Y_AXIS);

			}
		}

		/*
		 * Falls Breite des Bretts ungerade ist, wird die mittlere Reihe transformiert
		 */
		if (oldFields[0].length % 2 == 1) {
			final int last = oldFields[0].length / 2;
			for (int i = 0; i < oldFields.length; i++) {
				oldFields[i][last] = oldFields[i][last].transform(FieldTransformation.Y_AXIS);
			}
		}

		presenter.transformFields(oldFields, Page.props.mapGeneratorPage_progressWindow_flipY());

		this.page.destroy();
	}

	/**
	 * dreht ein Spielbrett um 90 grad nach rechts
	 */
	private void rotateRight() {
		final Field[][] oldFields = presenter.getFields();
		final Field[][] transformedFields = new Field[oldFields[0].length][oldFields.length];

		for (int i = 0; i < oldFields.length; i++) {
			for (int j = 0; j < oldFields[i].length; j++) {

				transformedFields[j][oldFields.length - i - 1] = oldFields[i][j].transform(FieldTransformation.QUARTER_RIGHT);

			}
		}

		presenter.transformFields(transformedFields, Page.props.mapGeneratorPage_progressWindow_rotateRight());

		this.page.destroy();
	}

	/**
	 * dreht ein Spielbrett um 90 grad nach links
	 */
	private void rotateLeft() {
		final Field[][] oldFields = presenter.getFields();
		final Field[][] transformedFields = new Field[oldFields[0].length][oldFields.length];

		for (int i = 0; i < oldFields.length; i++) {
			for (int j = 0; j < oldFields[i].length; j++) {

				transformedFields[oldFields[0].length - j - 1][i] = oldFields[i][j].transform(FieldTransformation.QUARTER_LEFT);

			}
		}

		presenter.transformFields(transformedFields, Page.props.mapGeneratorPage_progressWindow_rotateLeft());

		this.page.destroy();
	}

	/**
	 * dreht ein Spielbrett um 180 grad
	 */
	private void rotateAround() {

		final Field[][] oldFields = presenter.getFields();
		final Field[][] transformedFields = new Field[oldFields.length][oldFields[0].length];

		for (int i = 0; i < oldFields.length; i++) {
			for (int j = 0; j < oldFields[i].length; j++) {

				transformedFields[oldFields.length - i - 1][oldFields[0].length - j - 1] = oldFields[i][j]
						.transform(FieldTransformation.HALF_RIGHT);

			}
		}

		presenter.transformFields(transformedFields, Page.props.mapGeneratorPage_progressWindow_rotate());

		this.page.destroy();
	}

	@Override
	public Canvas getPage() {
		return this.page;
	}

}
