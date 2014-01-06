package prototyp.client.view.round;

import org.vaadin.gwtgraphics.client.DrawingArea;

import prototyp.client.presenter.round.RoundPlayerPagePresenter;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.VerticalStack;

import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;

/**
 * Definiert alles, was für die visuelle Darstellung der RoundPlayerPage wichtig
 * ist.
 * 
 * @author Dennis, Marcus (Verantwortlicher)
 * @version 1.1 Properties hinzugefügt (Marina)
 */
public class RoundPlayerPage extends RoundPage {

	/** die rechte Area */
	private final RobotStatusArea robotStatusArea;

	/** der mainStack */
	private final HorizontalStack mainStack;

	/** die ScrollArea für die drawingArea */
	private VerticalStack scrollArea, bgArea, moveCursorArea;

	/** Konstanten */
	public static final int DRAWING_AREA_WIDTH = 610,
			DRAWING_AREA_HEIGHT = 560;

	/**
	 * Konstruktor
	 */
	public RoundPlayerPage(RoundPlayerPagePresenter presenter) {
		super(presenter.getRoundInfo().getRoundOption().getGameName());
		setShowHeadline(false);

		this.mainStack = new HorizontalStack(10, 0);
		this.mainStack.setMembersMargin(0);
		this.mainStack.setHeight100();
		this.scrollArea = new VerticalStack();
		this.scrollArea.setWidth(RoundPlayerPage.DRAWING_AREA_WIDTH);
		this.scrollArea.setHeight(RoundPlayerPage.DRAWING_AREA_HEIGHT);
		this.scrollArea.setOverflow(Overflow.AUTO);

		this.drawingArea = new DrawingArea(1, 1); // Alles, ohne robotStatusArea
		this.robotStatusArea = new PlayerStatusArea(presenter);

		this.mainStack.addMembers(this.scrollArea, this.robotStatusArea);
		setMainStack(this.mainStack);

		setCanAcceptDrop(true);
	}
	
	@Override
	public void buildDrawingArea() {
		this.bgArea = new VerticalStack();
		this.bgArea.setWidth(this.drawingArea.getWidth());
		this.bgArea.setHeight(this.drawingArea.getHeight());
		this.bgArea.setBackgroundColor("#636363");
		this.bgArea.setBorder("2px solid yellow");
		
		if(bgArea.getWidth() < RoundPlayerPage.DRAWING_AREA_WIDTH) {
			bgArea.setLeft((RoundPlayerPage.DRAWING_AREA_WIDTH - bgArea.getWidth()) / 2
					- (bgArea.getHeight() > RoundPlayerPage.DRAWING_AREA_HEIGHT ? 10 : 0));
		}
		
		if(bgArea.getHeight() < RoundPlayerPage.DRAWING_AREA_HEIGHT) {
			bgArea.setTop((RoundPlayerPage.DRAWING_AREA_HEIGHT - bgArea.getHeight()) / 2
					- (bgArea.getWidth() > RoundPlayerPage.DRAWING_AREA_WIDTH ? 10 : 0));
		}
		
		this.bgArea.addChild(this.drawingArea);
		
		this.scrollArea.addChild(this.bgArea);
		
		this.moveCursorArea = new VerticalStack();
		this.moveCursorArea.setWidth(this.drawingArea.getWidth());
		this.moveCursorArea.setHeight(this.drawingArea.getHeight());
		this.moveCursorArea.setCursor(Cursor.MOVE);
		
		this.bgArea.addChild(this.moveCursorArea);
	}

	/**
	 * Liefert die Robot-Status-Area
	 * 
	 * @return robotStatusArea
	 */
	public RobotStatusArea getRobotStatusArea() {
		return this.robotStatusArea;
	}

	public VerticalStack getScrollArea() {
		return this.scrollArea;
	}

	public VerticalStack getMoveCursorArea() {
		return moveCursorArea;
	}
	
}
