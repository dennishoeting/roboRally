package prototyp.client.util;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import com.smartgwt.client.widgets.events.MouseDownEvent;
import com.smartgwt.client.widgets.events.MouseDownHandler;
import com.smartgwt.client.widgets.events.MouseMoveEvent;
import com.smartgwt.client.widgets.events.MouseMoveHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseUpEvent;
import com.smartgwt.client.widgets.events.MouseUpHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * 
 * @author Marcus
 * @version 1.0, 1.1 (JDoc, Robert)
 * 
 */
public class Minimap extends DrawingArea {

	private PlayingboardArea playingboardArea;

	/** gibt an, ob die Maus auf der Minimap gedrückt ist */
	private boolean mousePressedOnMinimap = false;
	
	private Rectangle rectangle;
	private Rectangle rectangleChoose;
	private int draggedX = -1;
	private int draggedY = -1;
	private Layout father;

	final VStack handCursorLayout;
	/**
	 * Konstruktor. Erstellt eine Minimap als Übersicht und mit Navigationshilfe
	 * (Mouselistenern) für das Original
	 * 
	 * @param width
	 * @param height
	 */
	public Minimap(final int width, final int height, final VStack handCursorLayout) {
		super(width, height);
		
		this.handCursorLayout = handCursorLayout;

		this.handCursorLayout.addMouseMoveHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if (Minimap.this.draggedX != -1) {
					
					Minimap.this.draggedX = event.getX() - Minimap.this.handCursorLayout.getAbsoluteLeft();
					Minimap.this.draggedY = event.getY() - Minimap.this.handCursorLayout.getAbsoluteTop();

					int extraX = 0;
					int extraY = 0;
					
					if (Minimap.this.draggedX
							+ Minimap.this.rectangle.getWidth() / 2 > getWidth()) {
						Minimap.this.draggedX = getWidth()
								- Minimap.this.rectangle.getWidth() / 2;
						extraX = 15;
					} else if (Minimap.this.draggedX
							- Minimap.this.rectangle.getWidth() / 2 <= 0) {
						Minimap.this.draggedX = Minimap.this.rectangle
								.getWidth() / 2;
					}

					if (Minimap.this.draggedY
							+ Minimap.this.rectangle.getHeight() / 2 > getHeight()) {
						Minimap.this.draggedY = getHeight()
								- Minimap.this.rectangle.getHeight() / 2;
						extraY = 15;
					} else if (Minimap.this.draggedY
							- Minimap.this.rectangle.getHeight() / 2 <= 0) {
						Minimap.this.draggedY = Minimap.this.rectangle
								.getHeight() / 2;
					}

					Minimap.this.rectangle.setX(Minimap.this.draggedX
							- Minimap.this.rectangle.getWidth() / 2);
					Minimap.this.rectangle.setY(Minimap.this.draggedY
							- Minimap.this.rectangle.getHeight() / 2);

					Minimap.this.father.scrollTo(
							(Minimap.this.draggedX - Minimap.this.rectangle
									.getWidth() / 2) * 5 + extraX,
							(Minimap.this.draggedY - Minimap.this.rectangle
									.getHeight() / 2) * 5 + extraY);
				}
			}
		});

		this.handCursorLayout.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				
				mousePressedOnMinimap = true;
				
				Minimap.this.draggedX = event.getX() - Minimap.this.handCursorLayout.getAbsoluteLeft();
				Minimap.this.draggedY = event.getY() - Minimap.this.handCursorLayout.getAbsoluteTop();
				
				int extraX = 0;
				int extraY = 0;
				
				if (Minimap.this.draggedX + Minimap.this.rectangle.getWidth()
						/ 2 > getWidth()) {
					Minimap.this.draggedX = getWidth()
							- Minimap.this.rectangle.getWidth() / 2;
					extraX = 15;
				} else if (Minimap.this.draggedX
						- Minimap.this.rectangle.getWidth() / 2 <= 0) {
					Minimap.this.draggedX = Minimap.this.rectangle.getWidth() / 2;
				}

				if (Minimap.this.draggedY + Minimap.this.rectangle.getHeight()
						/ 2 > getHeight()) {
					Minimap.this.draggedY = getHeight()
							- Minimap.this.rectangle.getHeight() / 2;
					extraY = 15;
				} else if (Minimap.this.draggedY
						- Minimap.this.rectangle.getHeight() / 2 <= 0) {
					Minimap.this.draggedY = Minimap.this.rectangle.getHeight() / 2;
				}

				Minimap.this.rectangle.setX(Minimap.this.draggedX
						- Minimap.this.rectangle.getWidth() / 2);
				Minimap.this.rectangle.setY(Minimap.this.draggedY
						- Minimap.this.rectangle.getHeight() / 2);

				Minimap.this.father.scrollTo(
						(Minimap.this.draggedX - Minimap.this.rectangle
								.getWidth() / 2) * 5 + extraX,
						(Minimap.this.draggedY - Minimap.this.rectangle
								.getHeight() / 2) * 5 + extraY);
			}
		});

		this.handCursorLayout.addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				
				mousePressedOnMinimap = false;
				
				if (Minimap.this.draggedX != -1) {
					Minimap.this.draggedX = -1;
				}
			}
		});

		this.handCursorLayout.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				
				mousePressedOnMinimap = false;
				Minimap.this.draggedX = -1;
			}
		});
	}

	public void addRectangle(Rectangle rect) {
		add(rect);
		this.rectangle = rect;
	}

	/**
	 * @return the playingboardArea
	 */
	public PlayingboardArea getPlayingboardArea() {
		return this.playingboardArea;
	}

	/**
	 * @return the rectangle
	 */
	public Rectangle getRectangle() {
		return this.rectangle;
	}

	/**
	 * @param playingboardArea
	 *            the playingboardArea to set
	 */
	public void setPlayingboardArea(PlayingboardArea playingboardArea) {
		this.playingboardArea = playingboardArea;
		this.father = this.playingboardArea.getFather();
	}

	public boolean isMousePressedOnMinimap() {
		return mousePressedOnMinimap;
	}

	public Rectangle getRectangleChoose() {
		return rectangleChoose;
	}

	public void setRectangleChoose(Rectangle rectangleChoose) {
		this.rectangleChoose = rectangleChoose;
	}
	
	
}
