package prototyp.client.presenter.mapgenerator;

import prototyp.client.view.Page;
import prototyp.client.view.mapgenerator.MapGeneratorLoadWindow;
import prototyp.shared.round.PlayingBoard;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * Presenter für das MapGeneratorLoadingPopUp Fenster
 * 
 * @author Timo, Dennis (Verantwortlicher)
 * @version 1.0
 * 
 */
public class MapGeneratorLoadWindowPresenter extends MapGeneratorAbstractLoadDeleteWindowPresenter {

	public MapGeneratorLoadWindowPresenter(MapGeneratorPresenter mapGeneratorPresenter) {
		super(mapGeneratorPresenter);

		this.page = new MapGeneratorLoadWindow();
		this.isDeleteRequest = false;
		super.getPlayingBoardDataAndFillGrid();
		this.addListeners();
	}

	@Override
	protected boolean addListeners() {

		// Ok Button
		this.page.getButtonAction().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MapGeneratorLoadWindowPresenter.this.page.getButtonAction().setSelected(true);

				if (MapGeneratorLoadWindowPresenter.this.page.getListGrid().getSelectedRecord().getAttributeAsString("name")
						.equals("")
						|| MapGeneratorLoadWindowPresenter.this.page.getListGrid().getSelectedRecord() == null) {
					SC.say(Page.props.mapGeneratorLoadingPopUp_selection_error());
					MapGeneratorLoadWindowPresenter.this.page.getButtonAction().setSelected(false);
				} else {
					MapGeneratorLoadWindowPresenter.this.mapGeneratorService.loadPlayingBoard(
							MapGeneratorLoadWindowPresenter.this.page.getListGrid().getSelectedRecord()
									.getAttributeAsString("name"), new AsyncCallback<PlayingBoard>() {

								@Override
								public void onFailure(Throwable caught) {
									SC.say(caught.getMessage());
									MapGeneratorLoadWindowPresenter.this.page.getButtonAction().setSelected(false);
								}

								@Override
								public void onSuccess(PlayingBoard result) {
									if (result == null) {
										SC.say(Page.props.global_title_error());
										MapGeneratorLoadWindowPresenter.this.page.getButtonAction().setSelected(false);
									} else {
										MapGeneratorLoadWindowPresenter.this.mapGeneratorPresenter.transmitPlayingBoard(result);
									}
								}

							});
				}

				MapGeneratorLoadWindowPresenter.this.page.clear();
			}

		});

		return super.addListeners();

	}
}
