package prototyp.client.view.round;

import org.vaadin.gwtgraphics.client.DrawingArea;

import prototyp.client.presenter.round.RoundWatcherPagePresenter;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.VerticalStack;

import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;

/**
 * Definiert alles, was für die visuelle Darstellung der RoundWatcherPage wichtig ist.
 * 
 * @author Dennis, Marcus (Verantwortlicher)
 */
public class RoundWatcherPage extends RoundPage {

	/** Watcher Area (auf der rechten Seite) */
	private WatcherStatusArea robotStatusArea;

	/** der mainStack */
	private HorizontalStack mainStack;

	/** die ScrollArea für die drawingArea */
	/** die ScrollArea für die drawingArea */

	/** die ScrollArea für die drawingArea */
	private VerticalStack scrollArea, bgArea, moveCursorArea;


	/** Konstanten */
	private static final int DRAWING_AREA_WIDTH = 610, DRAWING_AREA_HEIGHT = 560;

	/**
	 * Konstruktor
	 */
	public RoundWatcherPage(RoundWatcherPagePresenter presenter) {
		super(presenter.getRoundInfo().getRoundOption().getGameName());
		setShowHeadline(false);

		this.mainStack = new HorizontalStack(0, 0);
		this.mainStack.setMembersMargin(0);
		this.mainStack.setHeight100();
		this.scrollArea = new VerticalStack();
		this.scrollArea.setWidth(RoundWatcherPage.DRAWING_AREA_WIDTH);
		this.scrollArea.setHeight(RoundWatcherPage.DRAWING_AREA_HEIGHT);
		this.scrollArea.setOverflow(Overflow.AUTO);

		// scrollArea.setBorder("3px solid black");
		this.drawingArea = new DrawingArea(1, 1);
		this.robotStatusArea = new WatcherStatusArea(presenter);

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
		
		if(bgArea.getWidth() < RoundWatcherPage.DRAWING_AREA_WIDTH) {
			bgArea.setLeft((RoundWatcherPage.DRAWING_AREA_WIDTH - bgArea.getWidth()) / 2
					- (bgArea.getHeight() > RoundWatcherPage.DRAWING_AREA_HEIGHT ? 10 : 0));
		}
		
		if(bgArea.getHeight() < RoundWatcherPage.DRAWING_AREA_HEIGHT) {
			bgArea.setTop((RoundWatcherPage.DRAWING_AREA_HEIGHT - bgArea.getHeight()) / 2
					- (bgArea.getWidth() > RoundWatcherPage.DRAWING_AREA_WIDTH ? 10 : 0));
		}
		
		this.bgArea.addChild(this.drawingArea);
		
		this.scrollArea.addChild(this.bgArea);
		
		this.moveCursorArea = new VerticalStack();
		this.moveCursorArea.setWidth(this.drawingArea.getWidth());
		this.moveCursorArea.setHeight(this.drawingArea.getHeight());
		this.moveCursorArea.setCursor(Cursor.MOVE);
		
		this.bgArea.addChild(this.moveCursorArea);
	}


	public VerticalStack getScrollArea() {
		return this.scrollArea;
	}

	/**
	 * Liefert die Area auf der rechten Seite
	 * 
	 * @return watcherStatusArea
	 */
	public WatcherStatusArea getWatcherStatusArea() {
		return this.robotStatusArea;
	}

	/**
	 * Liefert die RobotStatusArea
	 * 
	 * @return RobotStatusArea
	 */
	@Override
	public RobotStatusArea getRobotStatusArea() {
		return this.robotStatusArea;
	}
	
	public VerticalStack getMoveCursorArea() {
		return moveCursorArea;
	}
}
