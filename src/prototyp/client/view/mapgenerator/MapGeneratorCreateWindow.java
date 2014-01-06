package prototyp.client.view.mapgenerator;

import prototyp.client.util.Button;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

/**
 * PopUp Window für den MapGenerator
 * 
 * @author Timo, Dennis (Verantwortlicher)
 * @version 1.0
 */
public class MapGeneratorCreateWindow extends Window {

	// Attribute
	private final HorizontalStack buttonStack;

	// Forms
	private final DynamicForm form;

	// Layout
	private final VerticalStack mainStack;

	// Buttons
	private final Button buttonOk, buttonAbort;

	// Felder
	private final SpinnerItem width, height;

	/**
	 * Konstruktor
	 */
	public MapGeneratorCreateWindow(final String title) {
		setIsModal(true);
		setShowModalMask(true);
		setTitle(title);

		this.mainStack = new VerticalStack(Integer.valueOf(Page.props
				.global_marginBetweenStackAreas()), 0);
		this.mainStack.setTop(20);
		this.mainStack.setWidth(145);
		this.mainStack.setHeight(145);

		this.buttonStack = new HorizontalStack(Integer.valueOf(Page.props.global_marginInStackAreas()), 0);
		this.buttonStack.setHeight(20);
		this.buttonStack.setAlign(Alignment.CENTER);
		this.buttonStack.setTop(30);

		this.buttonOk = new Button(Page.props.mapGeneratorPopUp_okButton());
		this.buttonOk.setAlign(Alignment.CENTER);
		this.buttonAbort = new Button(
				Page.props.mapGeneratorPopUp_abortButton());
		this.buttonAbort.setAlign(Alignment.CENTER);

		this.width = new SpinnerItem();
		this.width.setTitle(Page.props.mapGeneratorPopUp_button_title_width());
		this.width.setWidth(60);
		this.width.setMin(5);
		this.width.setMax(24);
		this.height = new SpinnerItem();
		this.height
				.setTitle(Page.props.mapGeneratorPopUp_button_title_height());
		this.height.setWidth(60);
		this.height.setMin(5);
		this.height.setMax(24);

		this.form = new DynamicForm();
		this.form.setItems(this.width, this.height);
		this.form.setAutoWidth();
		this.form.setMargin(20);

		this.buttonStack.addMembers(this.buttonOk, this.buttonAbort);
		this.mainStack.addMembers(this.form, this.buttonStack);
		this.addChild(this.mainStack);

		this.setWidth(150);
		this.setHeight(175);

		centerInPage();
		
		setCanDrag(false);
		setCanDragReposition(false);

		show();
	}

	/**
	 * Liefert den Abort-Button
	 * 
	 * @return abortButton
	 */
	public Button getButtonAbort() {
		return this.buttonAbort;
	}

	/**
	 * Liefert den OK-Button
	 * 
	 * @return okButton
	 */
	public Button getButtonOk() {
		return this.buttonOk;
	}

	/**
	 * Liefert das TextItem
	 * 
	 * @return height
	 */
	public TextItem getHeightItem() {
		return this.height;
	}

	/**
	 * Liefert das WidthItem
	 * 
	 * @return width
	 */
	public TextItem getWidthItem() {
		return this.width;
	}

}
