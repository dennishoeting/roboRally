package prototyp.client.util;

import prototyp.client.view.Page;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HStack;

/**
 * Erweiterung von HStack
 * 
 * @author Dennis
 * @version 1.0
 * @see HStack
 * @see VerticalStack
 */
public class HorizontalStack extends HStack {
	/**
	 * Konstruktor, vorerst leer
	 */
	public HorizontalStack() {

	}

	/**
	 * Konstruktor
	 * 
	 * @param margin
	 *            Margin des Stacks
	 * @param padding
	 *            Padding des Stacks
	 */
	public HorizontalStack(int margin, int padding) {
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
	public HorizontalStack(int margin, int padding, String title) {
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
	public HorizontalStack(String title) {
		this(Integer.valueOf(Page.props.global_marginInStackAreas()), Integer
				.valueOf(Page.props.global_paddingInStackAreas()));

		setGroupTitle(title);
		setIsGroup(true);
	}

	/**
	 * Editiert zu einem HorizontalStack X Canvas als Child
	 * 
	 * @param children
	 */
	public void addChildren(Canvas... children) {
		for (Canvas c : children) {
			this.addChild(c);
		}
	}

	/**
	 * Editiert zu einem HorizontalStack X Canvas als Member
	 * 
	 * @param members
	 */
	public void addMembers(Canvas... members) {
		for (Canvas c : members) {
			this.addMember(c);
		}
	}
}
