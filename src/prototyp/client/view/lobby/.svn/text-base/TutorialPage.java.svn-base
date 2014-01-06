package prototyp.client.view.lobby;

import prototyp.client.presenter.lobby.TutorialPagePresenter;
import prototyp.client.util.HorizontalStack;
import prototyp.client.view.Page;

import com.smartgwt.client.widgets.HTMLPane;

/**
 * TutorialPage (View). Zeigt einen YouTube-Player, wobei das Tutorial gezeigt wird.
 * 
 * @author Andreas (Verantwortlicher)
 * @version 1.0
 * @version 1.1 Youtube Link zum Tutorial (Tim)
 * 
 * @see TutorialPagePresenter
 */
public class TutorialPage extends Page {

	// Attribute
	private HorizontalStack mainArea;
	private HTMLPane playerArea;

	/**
	 * Konstruktor
	 */
	public TutorialPage() {
		super(Page.props.tutorialPage_title());

		// Areas
		this.mainArea = new HorizontalStack(Integer.valueOf(Page.props.global_marginBetweenStackAreas()), 0);
		HorizontalStack area = new HorizontalStack();
		area.setWidth100();
		area.setHeight100();
		
		// Fields
		int width = 425;
		int height = 355;

		// YouTube-Player
		this.playerArea = new HTMLPane();
		this.playerArea
				.setContents("<object width='"
						+ width
						+ "' height='"
						+ height
						+ "'><param name='movie' value='http://www.youtube.com/v/w4dPWxCvUKY?rel=1&color1=0x2b405b&color2=0x6b8ab6&border=1&fs=1'></param><param name='allowFullScreen' value='true'></param><param name='allowScriptAccess' value='always'></param><embed src='http://www.youtube.com/v/w4dPWxCvUKY?rel=1&color1=0x2b405b&color2=0x6b8ab6&border=1&fs=1' type='application/x-shockwave-flash' allowscriptaccess='always' width='425' height='355' allowfullscreen='true'></embed></object>");
		this.playerArea.setWidth(width + 25);
		this.playerArea.setHeight(height + 25);
		this.playerArea.setLeft((Integer.valueOf(Page.props.prototyp_childTabManager_width()) / 2) - (width / 2));
		this.playerArea.setTop(50);

		// Areas zusammenfügen
		area.addChild(this.playerArea);
		this.mainArea.addMembers(area);

		setMainStack(this.mainArea);
	}
}