package prototyp.client.view.mapgenerator;

import prototyp.client.util.Button;
import prototyp.client.view.Page;

import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * Window, das Buttons für das Transformieren eines Spielbretts hat
 * @author Marcus
 *
 */
public class MapGeneratorTransformWindow extends Window {

	private final Button buttonChangeSize = new Button(35, 35);
	
	private final Button buttonFlipX = new Button(35, 35);
	
	private final Button buttonFlipY = new Button(35, 35);
	
	private final Button buttonRotateRight = new Button(35, 35);
	
	private final Button buttonRotateLeft = new Button(35, 35);
	
	private final Button buttonRotateFully = new Button(35, 35);
	
	/**
	 * Konstruktor
	 */
	public MapGeneratorTransformWindow() {

		this.setIsModal(true);
		this.setShowModalMask(true);
		
		final VStack vStack = new VStack(20);
		vStack.setTop(40);
		vStack.setLeft(20);
		
		final HStack hStack1 = new HStack(20);
		final HStack hStack2 = new HStack(20);
		
		hStack1.setHeight(35);
		hStack2.setHeight(35);
		
		hStack1.setMembers(this.buttonChangeSize, this.buttonFlipX, this.buttonFlipY);
		hStack2.setMembers(this.buttonRotateRight, this.buttonRotateLeft, this.buttonRotateFully);
		
		vStack.setMembers(hStack1, hStack2);
		
		
		this.addChild(vStack);
		
		this.setTitle(Page.props.mapGenerator_transformWindow_title());
		
		
		this.buttonChangeSize.addChild(new Img("mapgenerator/trans_change_size.png", 35, 35));
		this.buttonFlipX.addChild(new Img("mapgenerator/trans_spiegeln_horizontal.png", 35, 35));
		this.buttonFlipY.addChild(new Img("mapgenerator/trans_spiegeln_vertikal.png", 35, 35));
		this.buttonRotateRight.addChild(new Img("mapgenerator/trans_90_rechts.png", 35, 35));
		this.buttonRotateLeft.addChild(new Img("mapgenerator/trans_90_links.png", 35, 35));
		this.buttonRotateFully.addChild(new Img("mapgenerator/trans_180.png", 35, 35));
		
		this.buttonChangeSize.setTooltip(Page.props.mapGenerator_transformWindow_buttonChangeSize_tooltip());
		this.buttonFlipX.setTooltip(Page.props.mapGenerator_transformWindow_buttonFlipX_tooltip());
		this.buttonFlipY.setTooltip(Page.props.mapGenerator_transformWindow_buttonFlipY_tooltip());
		this.buttonRotateRight.setTooltip(Page.props.mapGenerator_transformWindow_buttonRotateRight_tooltip());
		this.buttonRotateLeft.setTooltip(Page.props.mapGenerator_transformWindow_buttonRotateLeft_tooltip());
		this.buttonRotateFully.setTooltip(Page.props.mapGenerator_transformWindow_buttonRotateFully_tooltip());

		this.setCanDrag(false);
		this.setCanDragReposition(false);
		
		this.setWidth(197);
		this.setHeight(150);

		centerInPage();

		show();
	}


	public Button getButtonChangeSize() {
		return buttonChangeSize;
	}


	public Button getButtonFlipX() {
		return buttonFlipX;
	}


	public Button getButtonFlipY() {
		return buttonFlipY;
	}


	public Button getButtonRotateRight() {
		return buttonRotateRight;
	}


	public Button getButtonRotateLeft() {
		return buttonRotateLeft;
	}


	public Button getButtonRotateFully() {
		return buttonRotateFully;
	}
}
