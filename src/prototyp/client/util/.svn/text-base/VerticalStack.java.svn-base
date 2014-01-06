package prototyp.client.util;

import prototyp.client.view.Page;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * Erweiterung von VStack
 * 
 * @author Dennis Höting
 * @version 1.0
 * @see VStack
 * @see HorizontalStack
 */
public class VerticalStack extends VStack {
	/**
	 * Konstruktor, vorerst leer
	 */
	public VerticalStack() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param margin
	 *            Margin des Stacks
	 * @param padding
	 *            Padding des Stacks
	 */
	public VerticalStack(int margin, int padding) {
		this();
		setMembersMargin(margin);
		setPadding(padding);
	}

	/**
	 * Konstruktor
	 * 
	 * @param margin
	 *            Margin
	 * @param padding
	 *            Padding
	 * @param title
	 *            Titel
	 */
	public VerticalStack(int margin, int padding, String title) {
		this(margin, padding);

		setGroupTitle(title);
		setIsGroup(true);
	}

	/**
	 * Konstruktor
	 * 
	 * @param title
	 *            Titel der Gruppe
	 */
	public VerticalStack(String title) {
		this(Integer.valueOf(Page.props.global_marginInStackAreas()), Integer
				.valueOf(Page.props.global_paddingInStackAreas()));

		setGroupTitle(title);
		setIsGroup(true);
	}

	/**
	 * Editiert zu einem VerticalStack X Canvas als Child
	 * 
	 * @param children
	 */
	public void addChildren(Canvas... children) {
		for (Canvas c : children) {
			this.addChild(c);
		}
	}

	/**
	 * Editiert zu einem VerticalStack X Canvas als Member
	 * 
	 * @param members
	 */
	public void addMembers(Canvas... members) {
		for (Canvas c : members) {
			this.addMember(c);
		}
	}
}
