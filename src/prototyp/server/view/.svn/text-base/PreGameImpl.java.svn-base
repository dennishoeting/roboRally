package prototyp.server.view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import prototyp.client.presenter.pregame.RefereePagePresenter;
import prototyp.client.service.PreGameService;
import prototyp.server.lib.DBConnection;
import prototyp.server.lib.DBStatements;
import prototyp.shared.exception.pregame.NoMapFoundException;
import prototyp.shared.round.PlayingBoard;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * PreGame, serverseitige Sicht. Hier befinden sich alle Methoden für die
 * Presenter aus dem PreGame-Package.
 * 
 * @author Andreas, Mischa
 * @version 1.0
 * @version 1.1 Exception-Handling
 * 
 * @see Clientseite: {@link PreGameService}
 */
public class PreGameImpl extends RemoteServiceServlet implements PreGameService {

	/** Seriennummer */
	private static final long serialVersionUID = -6469908289678552903L;

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
	@Override
	public Map<Integer, PlayingBoard> getPlayingBoards()
			throws NoMapFoundException {
		final Map<Integer, PlayingBoard> tmpMap = new HashMap<Integer, PlayingBoard>();
		try {
			// PreparedStatement vorbereiten
			final PreparedStatement playingBoardsStmt = DBConnection
					.getPstmt(DBStatements.SELECT_PLAYINGBOARDS.getStmt());

			// Statement ausführen
			final ResultSet stmtResult = playingBoardsStmt.executeQuery();

			while (stmtResult.next()) {
				final PlayingBoard tmpPlayingBoard = new PlayingBoard();
				tmpPlayingBoard.setID(stmtResult.getInt("playingBoardID"));
				tmpPlayingBoard.setDescription(stmtResult
						.getString("description"));
				tmpPlayingBoard.setName(stmtResult.getString("name"));
				tmpPlayingBoard.setWidth(stmtResult.getInt("width"));
				tmpPlayingBoard.setHeight(stmtResult.getInt("height"));
				tmpPlayingBoard.setNumberOfCheckpoints(stmtResult
						.getInt("numberOfCheckpoints"));
				tmpPlayingBoard.setMaxPlayers(stmtResult.getInt("maxPlayers"));
				tmpPlayingBoard.setImageFileName(stmtResult
						.getString("imageFileName"));
				tmpPlayingBoard.setDifficulty(stmtResult
						.getString("difficulty"));
				tmpPlayingBoard.setNumberOfConveyorBeltFields(stmtResult.getInt("numberOfConveyorBelts"));
				tmpPlayingBoard.setNumberOfCompactorFields(stmtResult.getInt("numberOfCompactors"));
				tmpPlayingBoard.setNumberOfPusherFields(stmtResult.getInt("numberOfPushers"));
				tmpPlayingBoard.setNumberOfGearFields(stmtResult.getInt("numberOfGears"));
				tmpPlayingBoard.setNumberOfLaserCannonFields(stmtResult.getInt("numberOfLaserCannons"));
				tmpPlayingBoard.setNickname(stmtResult
						.getString("nickname"));

				tmpMap.put(tmpPlayingBoard.getID(), tmpPlayingBoard);
			}

		} catch (Exception e) {
			throw new NoMapFoundException();
		}

		return tmpMap;
	}
}
