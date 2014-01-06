package prototyp.client.view.round;

import org.vaadin.gwtgraphics.client.DrawingArea;

import prototyp.client.view.Page;

/**
 * Abstrakte Oberklasse der RoundPages
 * 
 * @author Dennis
 * @version 1.0
 */
public abstract class RoundPage extends Page {

	/** die drawingArea */
	protected DrawingArea drawingArea;

	protected RoundPage(String title) {
		super(title);
	}

	/**
	 * Liefert die Drawing-Area
	 * 
	 * @return drawingArea
	 */
	public DrawingArea getDrawingArea() {
		return this.drawingArea;
	}
	
	public abstract RobotStatusArea getRobotStatusArea();
	
	abstract void buildDrawingArea();
}
