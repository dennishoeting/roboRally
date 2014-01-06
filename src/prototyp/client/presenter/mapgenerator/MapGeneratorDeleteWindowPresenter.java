package prototyp.client.presenter.mapgenerator;

import prototyp.client.view.Page;
import prototyp.client.view.mapgenerator.MapGeneratorDeleteWindow;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * Presenter, um Spielbretter zu löschen
 * 
 * @author Marcus
 * @version 1.0
 */
public class MapGeneratorDeleteWindowPresenter extends MapGeneratorAbstractLoadDeleteWindowPresenter {

	/**
	 * Konstruktor
	 * 
	 * @param mapGeneratorPresenter
	 *            ParentPresenter
	 */
	public MapGeneratorDeleteWindowPresenter(MapGeneratorPresenter mapGeneratorPresenter) {
		super(mapGeneratorPresenter);

		this.page = new MapGeneratorDeleteWindow();
		this.isDeleteRequest = true;
		super.getPlayingBoardDataAndFillGrid();
		this.addListeners();
	}

	/**
	 * Fügt die Listener hinzu
	 * 
	 * @return true, wenn alles geklappt hat
	 */
	@Override
	protected boolean addListeners() {

		// Ok Button
		this.page.getButtonAction().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SC.ask(Page.props.mapGeneratorDeletePopUp_deleteQuestionText(), new BooleanCallback() {

					@Override
					public void execute(Boolean value) {
						if (value) {
							mapGeneratorService.deletePlayingBoard(
									page.getListGrid().getSelectedRecord().getAttributeAsString("name"), page.getListGrid()
											.getSelectedRecord().getAttributeAsInt("playingBoardID"),
									new AsyncCallback<Boolean>() {

										@Override
										public void onSuccess(Boolean result) {
											MapGeneratorDeleteWindowPresenter.this.page.getButtonAction().setDisabled(true);
											MapGeneratorDeleteWindowPresenter.this.page.getPreviewPicture().setSrc(
													"ui/default_map_image.png");
											page.getListGrid().removeData(page.getListGrid().getSelectedRecord());
											page.getListGrid().redraw();

										}

										@Override
										public void onFailure(Throwable caught) {
											SC.say(caught.getMessage());
										}
									});
						}
					}
				});
			}
		});

		return super.addListeners();
	}

}
