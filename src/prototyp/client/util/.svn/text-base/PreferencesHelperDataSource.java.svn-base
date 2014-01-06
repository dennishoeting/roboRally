package prototyp.client.util;

import prototyp.client.view.Page;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

/**
 * Hilfsklasse für die Preview-ListGrids. Muss ich erstellen, sonst erscheinen
 * wieder Exceptions. Das DataSource-Objekt wird aber sonst nicht benutzt (nur
 * für die Namen der Spalten).
 * 
 * @author Andreas
 * @version 1.0
 * 
 */
public class PreferencesHelperDataSource extends DataSource {

	/**
	 * Konstruktor
	 */
	public PreferencesHelperDataSource() {
		DataSourceTextField optionTitle = new DataSourceTextField(
				"optionTitle", Page.props.global_title_preference());
		optionTitle.setRequired(true);
		optionTitle.setPrimaryKey(true);

		DataSourceTextField optionDescription = new DataSourceTextField(
				"optionDescription", Page.props.global_title_description());
		optionDescription.setRequired(true);

		DataSourceTextField optionType = new DataSourceTextField("optionType",
				Page.props.global_title_type());
		optionType.setDetail(true);

		setFields(optionTitle, optionDescription, optionType);
	}

}
