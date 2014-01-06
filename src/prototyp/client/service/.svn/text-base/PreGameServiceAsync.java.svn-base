package prototyp.client.service;

import java.util.Map;

import prototyp.client.presenter.pregame.RefereePagePresenter;
import prototyp.shared.exception.pregame.NoMapFoundException;
import prototyp.shared.round.PlayingBoard;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Die zugehörige Async-Klasse zu {@link PreGameService}.
 * 
 * @author Andreas
 * @version 1.0
 */
public interface PreGameServiceAsync {

	/**
	 * Benutzt vom {@link RefereePagePresenter}.
	 * 
	 * Liefert alle {@link PlayingBoard}-Objekte aus der Datenbank. Es wird eine
	 * HashMap erstellt, die als Key die playingBoardID des zugehörigen
	 * Spielbrettes verwendet.
	 * 
	 * @return HashMap<Integer, PlayingBoard>
	 * @throws NoMapFoundException
	 */
	void getPlayingBoards(AsyncCallback<Map<Integer, PlayingBoard>> callback);

}
