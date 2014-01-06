package prototyp.client.presenter;

import com.smartgwt.client.widgets.Canvas;

/**
 * Interface für PagePresenter
 * 
 * @author Robert
 * @version 1.0
 */
public interface PagePresenter {
	/**
	 * Übergibt die dem Presenter zugehörige Page(-View)
	 * 
	 * @return Page
	 */
	public Canvas getPage();

}
