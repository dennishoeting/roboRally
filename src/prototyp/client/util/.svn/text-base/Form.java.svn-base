package prototyp.client.util;

import prototyp.client.properties.PropertiesDe;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;

/**
 * Erweiterung der DynamicForm, zum Standard erhobenes Margin sowie Padding
 * werden automatisch eingefügt
 * 
 * @author Dennis Höting
 * @version 1.0
 * @see DynamicForm
 */
public class Form extends DynamicForm {
	private PropertiesDe props = GWT.create(PropertiesDe.class);

	/**
	 * Konstroktur, kein GroupTitle
	 */
	public Form() {
		super();
		setMargin(Integer.valueOf(this.props.global_marginInStackAreas()));
		setPadding(Integer.valueOf(this.props.global_paddingInStackAreas()));
	}

	/*
	 * 
	 */
	public Form(FormItem... items) {
		this();
		setFields(items);
	}

	/**
	 * Konstruktor mit GroupTitle
	 * 
	 * @param title
	 *            Beschriftung der Gruppe
	 */
	public Form(String title) {
		this();
		setGroupTitle(title);
		setIsGroup(true);
	}

	/**
	 * 
	 */
	public Form(String title, FormItem... items) {
		this(title);
		setFields(items);
	}
}
