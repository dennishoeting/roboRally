package prototyp.client.view.mapgenerator;

import java.util.LinkedHashMap;

import prototyp.client.util.Button;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

/**
 * MapGeneratorSaveWindow. Hier befindet sich alles, was für die visuelle
 * Darstellung wichtig ist.
 * 
 * @author Dennis (Verantwortlicher)
 * @version 1.0
 */
public class MapGeneratorSaveWindow extends Window {

	private HorizontalStack buttonStack;

	// Textfeld für die Beschreibung
	private final TextAreaItem description;

	// Forms
	private final DynamicForm form;

	// Layout
	private final VerticalStack mainStack;

	// Felder
	private final TextItem name;

	// Buttons
	private final Button buttonOk, buttonAbort;

	private LinkedHashMap<String, String> difficultyMap;
	private final SelectItem difficultySelector;

	/**
	 * Konstruktor
	 */
	public MapGeneratorSaveWindow() {
		setIsModal(true);
		setShowModalMask(true);

		this.mainStack = new VerticalStack(Integer.valueOf(Page.props
				.global_marginBetweenStackAreas()), 0);
		this.mainStack.setTop(40);
		this.buttonStack = new HorizontalStack(Integer.valueOf(Page.props
				.global_marginInStackAreas()), 0);

		this.buttonOk = new Button(Page.props.mapGeneratorPopUp_okButton());
		this.buttonOk.setAlign(Alignment.CENTER);
		this.buttonAbort = new Button(
				Page.props.mapGeneratorPopUp_abortButton());
		this.buttonAbort.setAlign(Alignment.CENTER);
		this.buttonStack.addMember(this.buttonOk);
		this.buttonStack.addMember(this.buttonAbort);
		this.buttonStack.setWidth(230);
		this.buttonStack.setAlign(Alignment.CENTER);

		this.name = new TextItem();
		this.name.setTitle(Page.props.global_title_name());
		this.name.setLength(30);
		this.name.setRequired(true);
		this.name.setRequiredMessage(Page.props.global_title_requiredMessage());
		
		this.description = new TextAreaItem();

		this.description.setTitle(Page.props.global_title_description());
		this.description.setRequired(true);
		this.description.setRequiredMessage(Page.props
				.global_title_requiredMessage());
		this.description.setCellStyle("textItemField");
		this.difficultyMap = new LinkedHashMap<String, String>();
		this.difficultyMap.put(
				Page.props.mapGeneratorSaveWindow_sehrLeicht_title(),
				Page.props.mapGeneratorSaveWindow_sehrLeicht_title());
		this.difficultyMap.put(
				Page.props.mapGeneratorSaveWindow_leicht_title(),
				Page.props.mapGeneratorSaveWindow_leicht_title());
		this.difficultyMap.put(
				Page.props.mapGeneratorSaveWindow_normal_title(),
				Page.props.mapGeneratorSaveWindow_normal_title());
		this.difficultyMap.put(
				Page.props.mapGeneratorSaveWindow_schwer_title(),
				Page.props.mapGeneratorSaveWindow_schwer_title());
		this.difficultyMap.put(
				Page.props.mapGeneratorSaveWindow_sehrSchwer_title(),
				Page.props.mapGeneratorSaveWindow_sehrSchwer_title());

		this.difficultySelector = new SelectItem();
		this.difficultySelector.setTitle(Page.props
				.mapGeneratorSaveWindow_difficulty());
		this.difficultySelector.setValueMap(this.difficultyMap);
		this.difficultySelector.setRequired(true);
		this.difficultySelector.setRequiredMessage(Page.props
				.global_title_requiredMessage());

		this.form = new DynamicForm();
		this.form
				.setItems(this.name, this.description, this.difficultySelector);

		this.mainStack.addMember(this.form);
		this.mainStack.addMember(this.buttonStack);

		this.addChild(this.mainStack);

		setTitle(Page.props.mapGeneratorPage_button_saveMap());

		this.setWidth(245);
		this.setHeight(275);

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
	public Button getAbortButton() {
		return this.buttonAbort;
	}

	/**
	 * Liefert die Beschreibung
	 * 
	 * @return description
	 */
	public TextAreaItem getDescriptionItem() {
		return this.description;
	}

	/**
	 * Liefert den Schwierigkeits-Auswähler
	 * 
	 * @return difficultySelector
	 */
	public SelectItem getDifficultySelector() {
		return this.difficultySelector;
	}

	/**
	 * Liefert den Namen
	 * 
	 * @return name
	 */
	public TextItem getNameItem() {
		return this.name;
	}

	/**
	 * Liefert den OK-Button
	 * 
	 * @return okButton
	 */
	public Button getOkButton() {
		return this.buttonOk;
	}

	/**
	 * Validiert, ob alle Eingaben getätigt worden sind.
	 * 
	 * @return true, wenn alles okay ist.
	 */
	public boolean validate() {
		return this.form.validate();
	}
}
