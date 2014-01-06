package prototyp.client.view.mapgenerator;

import java.util.LinkedHashMap;

import org.vaadin.gwtgraphics.client.Image;

import prototyp.client.util.Button;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.Minimap;
import prototyp.client.util.PlayingboardArea;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;

import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * MapGeneratorPage. Hier befindet sich alles, was für die visuelle Darstellung
 * wichtig ist.
 * 
 * @author Dennis (Verantwortlicher)
 * @version 1.0
 */
public class MapGeneratorPage extends Page {
	// Attribute
	private PlayingboardArea playingboardArea;
	private Minimap minimap;
	private VerticalStack drawingAreaArea, drawingAreaAreaInner =  new VerticalStack();
	private final MapGeneratorEditWindow editingWindow;
	private Image[][] images;
	private final HorizontalStack mainArea;

	/**
	 * Konstruktor
	 */
	public MapGeneratorPage() {
		super(Page.props.mapGeneratorPage_title());

		this.mainArea = new HorizontalStack(Integer.valueOf(Page.props
				.global_marginBetweenStackAreas()), 0);
		this.mainArea.setTop(40);
		
		this.editingWindow = new MapGeneratorEditWindow();
		this.editingWindow.setLeft(716);
		this.editingWindow.setTop(0);
		this.addChild(this.editingWindow);

		this.addChild(this.mainArea);
	}

	/**
	 * Liefert den Create-Button
	 * 
	 * @return createButton
	 */
	public Button getButtonCreate() {
		return this.editingWindow.getButtonCreate();
	}

	/**
	 * Liefert den Load-Button
	 * 
	 * @return loadButton
	 */
	public Button getButtonLoad() {
		return this.editingWindow.getButtonLoad();
	}
	
	/**
	 * Liefert den Unload-Button
	 * 
	 * @return loadButton
	 */
	public Button getButtonUnload() {
		return this.editingWindow.getButtonUnload();
	}
	
	/**
	 * Liefert den Delete-Button
	 * 
	 * @return loadButton
	 */
	public Button getButtonDelete() {
		return this.editingWindow.getButtonDelete();
	}

	/**
	 * Liefert den Save-Button
	 * 
	 * @return saveButton
	 */
	public Button getButtonSave() {
		return this.editingWindow.getButtonSave();
	}


	public Button getButtonTransform() {
		return this.editingWindow.getButtonTransform();
	}
	
	/**
	 * Liefert die Drawing-Area
	 * 
	 * @return the drawingArea
	 */
	public PlayingboardArea getPlayingboardArea() {
		return this.playingboardArea;
	}

	public VerticalStack getDrawingAreaArea() {
		return this.drawingAreaArea;
	}

	/**
	 * Liefert das Editing-Window
	 * 
	 * @return editingWindow
	 */
	public MapGeneratorEditWindow getEditingWindow() {
		return this.editingWindow;
	}

	/**
	 * Liefert die FieldTypeBox
	 * 
	 * @return fieldTypeBox
	 */
	public FormItem getFieldTypeBox() {
		return this.editingWindow.getFieldTypeBox();
	}

	/**
	 * Liefert die HashMap
	 * 
	 * @return linkedHashMap
	 */
	public LinkedHashMap<String, String> gethMap() {
		return this.editingWindow.gethMap();
	}

	/**
	 * Liefert die Bilder
	 * 
	 * @return the images
	 */
	public Image[][] getImages() {
		return this.images;
	}

	/**
	 * Liefert die Minimap
	 * 
	 * @return the minimap
	 */
	public Minimap getMinimap() {
		return this.minimap;
	}
	
	
	
	
	public VerticalStack getDrawingAreaAreaInner() {
		return drawingAreaAreaInner;
	}

	/**
	 * Liefert die HauptArea
	 * @return
	 */
	public HorizontalStack getMainArea() {
		return mainArea;
	}

	/**
	 * Stellt die DrawingArea dar
	 * 
	 * @param playingboardArea
	 *            the drawingArea to set
	 */
	public void setDrawingArea(final int width, final int height) {
		if (this.playingboardArea != null) {
			this.playingboardArea.clear();
			this.playingboardArea.removeFromParent();
			this.minimap.clear();
			this.minimap.removeFromParent();
			if(this.mainArea.contains(this.drawingAreaArea)) {
				this.mainArea.removeChild(this.drawingAreaArea);
			}
		}
		
		this.drawingAreaArea = new VerticalStack();
		this.drawingAreaArea.setWidth(715);
		this.drawingAreaArea.setHeight(485);
		this.drawingAreaArea.setOverflow(Overflow.AUTO);
		
		this.drawingAreaAreaInner = new VerticalStack();
		this.drawingAreaAreaInner.setWidth(width);
		this.drawingAreaAreaInner.setHeight(height);
		this.drawingAreaAreaInner.setBackgroundColor("#636363");
		this.drawingAreaAreaInner.setBorder("2px solid yellow");
		
		if(this.drawingAreaAreaInner.getWidth() < 715) {
			this.drawingAreaAreaInner.setLeft((715 - this.drawingAreaAreaInner.getWidth()) / 2 
					- (this.drawingAreaAreaInner.getHeight() > 485 ? 10 : 0));
		}
		
		if(this.drawingAreaAreaInner.getHeight() < 485) {
			this.drawingAreaAreaInner.setTop((485 - this.drawingAreaAreaInner.getHeight()) / 2
					- (this.drawingAreaAreaInner.getWidth() > 715 ? 10 : 0));
		}
		
		this.drawingAreaArea.addChild(this.drawingAreaAreaInner);
		
		this.mainArea.addChild(this.drawingAreaArea);
		
		this.playingboardArea = new PlayingboardArea(width, height,
				this.drawingAreaArea);
		
		this.drawingAreaAreaInner.addChild(this.playingboardArea);
		this.drawingAreaArea.scrollTo(0, 0);

		final VStack handCursorLayout = new VStack();
		handCursorLayout.setWidth(width / 5);
		handCursorLayout.setHeight(height / 5);
		handCursorLayout.setCursor(Cursor.MOVE);
		
		this.minimap = new Minimap(width / 5, height / 5, handCursorLayout);
		this.editingWindow.setMiniMap(this.minimap, handCursorLayout);

		this.playingboardArea.setMinimap(this.minimap);
		this.minimap.setPlayingboardArea(this.playingboardArea);
	}
	
	/**
	 * Setzt die Bilder
	 * 
	 * @param images
	 *            the images to set
	 */
	public void setImages(Image[][] images) {
		this.images = images;
	}
}
