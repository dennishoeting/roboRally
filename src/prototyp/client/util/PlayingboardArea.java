package prototyp.client.util;

import java.util.HashMap;
import java.util.Map;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import prototyp.shared.util.DrawingInfo;

import com.smartgwt.client.widgets.layout.Layout;

/**
 * Stellt die Area für das Playingboard
 * 
 * @author Marcus
 * @version 1.0
 */
public class PlayingboardArea extends DrawingArea {

	/** Minimap */
	private Minimap minimap;

	/** Parent */
	private Layout father;

	/** Spielbrett */
	private final Map<VectorObject, VectorObject> vectorMap = new HashMap<VectorObject, VectorObject>();

	/**
	 * Konstruktor.
	 * 
	 * @param width
	 *            Breite
	 * @param height
	 *            Höhe
	 * @param father
	 *            Parent
	 */
	public PlayingboardArea(int width, int height, Layout father) {
		super(width, height);
		this.father = father;
	}

	/**
	 * Fügt ein neues Objekt hinzu
	 * 
	 * @param vo
	 *            Neues {@link VectorObject}
	 * 
	 */
	@Override
	public VectorObject add(VectorObject vo) {
		final VectorObject re = super.add(vo);
		if (vo instanceof Image) {
			final Image img = (Image) vo;
			final Image copy;
			this.minimap.add(copy = new Image(img.getX() / 5, img.getY() / 5, img.getWidth() / 5, img.getHeight() / 5, img
					.getHref()));

			this.vectorMap.put(vo, copy);

		} else if (vo instanceof Rectangle) {
			final Rectangle old = (Rectangle) vo;

			final Rectangle copy = new Rectangle(old.getX() / 5, old.getY() / 5, old.getWidth() / 5, old.getHeight() / 5);
			copy.setStrokeColor(old.getStrokeColor());
			copy.setStrokeWidth(1);
			copy.setFillOpacity(old.getFillOpacity());

			this.minimap.setRectangleChoose(copy);

			this.minimap.add(copy);

			this.vectorMap.put(vo, copy);

		} else if (vo instanceof Line) {
			final Line old = (Line) vo;

			final Line copy = new Line(old.getX1() / 5, old.getY1() / 5, old.getX2() / 5, old.getY2() / 5);
			copy.setStrokeColor(old.getStrokeColor());
			copy.setStrokeWidth(1);

			this.minimap.add(copy);

			this.vectorMap.put(vo, copy);

		} else if (vo instanceof Circle) {
			final Circle old = (Circle) vo;

			final Circle copy = new Circle(old.getX() / 5, old.getY() / 5, 1);
			copy.setStrokeColor(old.getStrokeColor());

			this.minimap.add(copy);

			this.vectorMap.put(vo, copy);
		}

		return re;
	}

	/**
	 * Fügt ein Bild hinzu
	 * 
	 * @param info
	 *            DrawingInfo
	 * @param x
	 *            X-Koordinate
	 * @param y
	 *            Y-Koordinate
	 * @return true, falls alles geklappt hat
	 */
	public Image add(final DrawingInfo info, final int x, final int y) {
		final Image img = new Image(x + info.getX(), y + info.getY(), info.getWidth(), info.getHeight(), info.getImageFileName());
		final Image copy;
		this.minimap
				.add(copy = new Image(img.getX() / 5, img.getY() / 5, img.getWidth() / 5, img.getHeight() / 5, img.getHref()));

		this.vectorMap.put(img, copy);

		return (Image) super.add(img);
	}

	/**
	 * Liefert den Parent
	 * 
	 * @return the father
	 */
	public Layout getFather() {
		return this.father;
	}

	/**
	 * Liefert die Minimap
	 * 
	 * @return the minimap
	 */
	public Minimap getMinimap() {
		return this.minimap;
	}

	/**
	 * Löscht ein Objekt
	 * 
	 * @param vo
	 *            {@link VectorObject}
	 */
	@Override
	public VectorObject remove(VectorObject vo) {
		if (vo instanceof Image || vo instanceof Rectangle || vo instanceof Line || vo instanceof Circle) {
			final VectorObject remove = this.vectorMap.remove(vo);
			if (remove != null) {
				this.minimap.remove(remove);
			}
		}
		return super.remove(vo);
	}

	/**
	 * Setzt eine neue Minimap
	 * 
	 * @param minimap
	 *            the minimap to set
	 */
	public void setMinimap(Minimap minimap) {
		this.minimap = minimap;
	}
	
	/**
	 * Liefert das Minimap-Vectorobject für das Vectorobject der PlayingboardArea
	 * @param key
	 * 		das VectorObject der PlayingBoardArea
	 * @return
	 * 		das zugehörige Vekotobject auf der Minimap
	 */
	public VectorObject getMinimapVecorObject(VectorObject key) {
		return this.vectorMap.get(key);
	}

}
