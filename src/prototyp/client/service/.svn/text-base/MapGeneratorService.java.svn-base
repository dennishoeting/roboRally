package prototyp.client.service;

import java.util.Map;

import prototyp.client.presenter.mapgenerator.MapGeneratorPresenter;
import prototyp.client.presenter.pregame.RefereePagePresenter;
import prototyp.shared.exception.mapGenerator.CantLoadPlayingBoardException;
import prototyp.shared.exception.mapGenerator.CantSavePlayingBoardException;
import prototyp.shared.exception.mapGenerator.PlayingBoardIsLockedException;
import prototyp.shared.exception.playingboard.PlayingboardException;
import prototyp.shared.round.PlayingBoard;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Service für den MapGenerator
 * 
 * @version 1.0
 * @version 1.1 (22.10.10) Mittels savePlayingBoard können jetzt eigene
 *          PlayingBoards überschrieben werden.
 * @author timo
 * 
 */
@RemoteServiceRelativePath("mapgenerator")
public interface MapGeneratorService extends RemoteService {

	/**
	 * Benutzt vom {@link RefereePagePresenter}, {@link MapGeneratorPresenter}.
	 * 
	 * Liefert alle {@link PlayingBoard}-Objekte aus der Datenbank. Es wird eine
	 * HashMap erstellt, die als Key die playingBoardID des zugehörigen
	 * Spielbrettes verwendet. ACHTUNG: liefert kein komplettes PlayingBoard mit
	 * seinen Fields! Dazu loadPlayingBoards(String playingBoardName) benutzen!
	 * 
	 * @return HashMap<Integer, PlayingBoard>
	 */
	public Map<Integer, PlayingBoard> getPlayingBoards(int userId, boolean isAdmin, boolean isDeleteRequest);

	/**
	 * Läd ein Playingboard anhand seines Namen aus einer XML.
	 * 
	 * @param playingBoardName
	 * @return
	 */
	public PlayingBoard loadPlayingBoard(String playingBoardName)
			throws CantLoadPlayingBoardException;

	/**
	 * Speichert ein PlayingBoard persistent auf den Server. Gespeicherte
	 * PlayingBoards können nur durch den Ersteller überschrieben werden.
	 * 
	 * @param playingboard
	 * @return
	 * @throws PlayingboardException
	 * @throws CantLoadPlayingBoardException 
	 * @throws CantSavePlayingBoardException 
	 * @throws PlayingBoardIsLockedException 
	 */
	public boolean savePlayingBoard(PlayingBoard playingboard, int userId, boolean isAdmin)
			throws PlayingboardException, CantLoadPlayingBoardException, CantSavePlayingBoardException, PlayingBoardIsLockedException;

	/**
	 * Löscht ein Spielbrett anhand des Namen
	 * @param playingBoardName
	 * 		das zu löschende Spielbrett
	 * @return
	 * 		immer true
	 */
	boolean deletePlayingBoard(String playingBoardName, int playingboardId);
}
