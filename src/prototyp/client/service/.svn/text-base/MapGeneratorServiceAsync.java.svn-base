package prototyp.client.service;

import java.util.Map;

import prototyp.client.presenter.mapgenerator.MapGeneratorPresenter;
import prototyp.client.presenter.pregame.RefereePagePresenter;
import prototyp.shared.exception.playingboard.PlayingboardException;
import prototyp.shared.round.PlayingBoard;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MapGeneratorServiceAsync {

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
	void getPlayingBoards(int userId, boolean isAdmin, boolean isDeleteRequest, final AsyncCallback<Map<Integer, PlayingBoard>> callback);

	/**
	 * Läd ein Playingboard anhand seines Namen aus einer XML.
	 * 
	 * @param playingBoardName
	 * @return
	 */
	void loadPlayingBoard(String playingBoardName,
			AsyncCallback<PlayingBoard> callback);

	/**
	 * Speichert ein PlayingBoard persistent auf den Server. Gespeicherte
	 * PlayingBoards können nur durch den Ersteller überschrieben werden.
	 * 
	 * @param playingboard
	 * @return
	 * @throws PlayingboardException
	 */
	void savePlayingBoard(PlayingBoard playingboard, int userId, boolean isAdmin,
			AsyncCallback<Boolean> callback);

	/**
	 * Löscht ein Spielbrett anhand des Namen
	 * @param playingBoardName
	 * 		das zu löschende Spielbrett
	 * @param callback
	 */
	void deletePlayingBoard(String playingBoardName, int playingboardId,
			AsyncCallback<Boolean> callback);
}
