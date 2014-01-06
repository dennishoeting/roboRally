package prototyp.client.presenter.round;

import java.util.Map;

import org.vaadin.gwtgraphics.client.Image;

import prototyp.client.presenter.PagePresenter;
import prototyp.client.presenter.chat.ChatPresenter;
import prototyp.client.util.OthersReadyRecord;
import prototyp.client.util.OthersStateRecord;
import prototyp.client.view.round.RoundPage;
import prototyp.shared.round.Robot;

/**
 * Interface für RoundPlayerPagePresenter und RoundWatcherPagePresenter
 * 
 * @author Timo
 * @version 1.0
 */
public interface RoundPresenterInterface extends PagePresenter {

	/**
	 * Liefert den Manager
	 * 
	 * @return Manager
	 */
	public ManagerInterface getManager();

	/**
	 * Setzt den AreaStatus (für die rechte Seite)
	 * 
	 * @param state
	 *            Angabe des Status
	 */
	public void setAreaState(int state);

	/**
	 * Setzt den AreaState zurück
	 */
	public void resetAreaState();

	/**
	 * Liefert das OthersReadyRecord
	 * 
	 * @return OthersReadyRecord
	 */
	public Map<Integer, OthersReadyRecord> getOthersReadyRecords();

	/**
	 * Liefert das OthersStateRecord
	 * 
	 * @return OthersStateRecord
	 */
	public Map<Integer, OthersStateRecord> getOthersStateRecords();

	/**
	 * Liefert die Seite
	 * 
	 * @return RoundPage
	 */
	public RoundPage getPage();

	/**
	 * Liefert die Map mit den Roboterbildern
	 * 
	 * @return Map<Robot, Image>
	 */
	public Map<Robot, Image> getRobotsImageList();

	/**
	 * Liefert den ChatPresenter
	 * 
	 * @return ChatPresenter
	 */
	ChatPresenter getChatPresenter();
}
