package prototyp.client.service;

import java.util.Map;

import prototyp.client.presenter.pregame.RefereePagePresenter;
import prototyp.server.view.PreGameImpl;
import prototyp.shared.exception.pregame.NoMapFoundException;
import prototyp.shared.round.PlayingBoard;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * PreGame, clientseitige Sicht. Hier befinden sich alle Methoden für die
 * Presenter aus dem PreGame-Package.
 * 
 * @author Andreas
 * @version 1.0
 * 
 * @see Serverseite: {@link PreGameImpl}
 */
@RemoteServiceRelativePath("pregame")
public interface PreGameService extends RemoteService {

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
	Map<Integer, PlayingBoard> getPlayingBoards() throws NoMapFoundException;
}
