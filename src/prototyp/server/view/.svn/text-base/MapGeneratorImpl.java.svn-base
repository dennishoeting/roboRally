package prototyp.server.view;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import prototyp.client.presenter.mapgenerator.MapGeneratorPresenter;
import prototyp.client.presenter.pregame.RefereePagePresenter;
import prototyp.client.service.MapGeneratorService;
import prototyp.server.lib.DBConnection;
import prototyp.server.lib.DBStatements;
import prototyp.shared.exception.mapGenerator.CantLoadPlayingBoardException;
import prototyp.shared.exception.mapGenerator.CantSavePlayingBoardException;
import prototyp.shared.exception.mapGenerator.PlayingBoardIsLockedException;
import prototyp.shared.exception.playingboard.PlayingboardException;
import prototyp.shared.round.PlayingBoard;
import prototyp.shared.util.DrawingInfo;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Service für den MapGenerator
 * 
 * @author Timo, Andreas
 * @version 1.0
 * @version 1.1 Gespeicherte PlayingBoards können jetzt durch den Ersteller oder einen Admin geändert werden.
 * @version 1.2 einige Exceptions hinzugefügt -Timo
 */
public class MapGeneratorImpl extends RemoteServiceServlet implements MapGeneratorService {

	/** Seriennummer */
	private static final long serialVersionUID = 7592980941256759470L;

	/**
	 * Dateinamen erstellen (ersetzt alles komische durch "_" )
	 * 
	 * @param text
	 *            String, der überprüft werden soll
	 * @return neuer String
	 */
	private String createFileName(String text) {
		Pattern pattern = Pattern.compile("([^a-zA-Z0-9.-_])");
		Matcher matcher = pattern.matcher(text);
		return matcher.replaceAll("_");
	}

	/**
	 * Liefert Playingboard Name und Path, aber nicht RealPath
	 * 
	 * @param playingBoardName
	 * @return Name und Path
	 */
	private String getPlayingBoardFileNameAndPath(String playingBoardName) {
		return "/playingboard/" + playingBoardName.toLowerCase().replaceAll(" ", "") + ".plb";
	}

	/**
	 * Liefert den (möglichen) PlayingBoard Datei Namen und seinen RealPath. Geht nur wenn man dies als Servlet aufruft
	 * 
	 * @param playingBoardName
	 * @return
	 */
	private String getPlayingBoardFileNameAndRealPath(String playingBoardName) {
		return getServletContext().getRealPath("/") + getPlayingBoardFileNameAndPath(playingBoardName);
	}

	/**
	 * Benutzt vom {@link RefereePagePresenter}, {@link MapGeneratorPresenter}.
	 * 
	 * Liefert alle {@link PlayingBoard}-Objekte aus der Datenbank. Es wird eine HashMap erstellt, die als Key die playingBoardID
	 * des zugehörigen Spielbrettes verwendet. ACHTUNG: liefert kein komplettes PlayingBoard mit seinen Fields! Dazu
	 * loadPlayingBoards(String playingBoardName) benutzen!
	 * 
	 * @return HashMap<Integer, PlayingBoard>
	 */
	@Override
	public Map<Integer, PlayingBoard> getPlayingBoards(final int userId, final boolean isAdmin, final boolean isDeleteRequest) {
		final Map<Integer, PlayingBoard> tmpMap = new HashMap<Integer, PlayingBoard>();
		ResultSet stmtResult = null;

		try {
			// PreparedStatement vorbereiten
			final PreparedStatement playingBoardsStmt;

			if (isAdmin || !isDeleteRequest) {
				playingBoardsStmt = DBConnection.getPstmt(DBStatements.SELECT_PLAYINGBOARDS.getStmt());
			} else {
				playingBoardsStmt = DBConnection.getPstmt(DBStatements.SELECT_MY_PLAYINGBOARDS.getStmt());
				playingBoardsStmt.setInt(1, userId);
			}

			// Statement ausführen
			stmtResult = playingBoardsStmt.executeQuery();

			while (stmtResult.next()) {
				final PlayingBoard tmpPlayingBoard = new PlayingBoard();
				tmpPlayingBoard.setID(stmtResult.getInt("playingBoardID"));
				tmpPlayingBoard.setDescription(stmtResult.getString("description"));
				tmpPlayingBoard.setName(stmtResult.getString("name"));
				tmpPlayingBoard.setWidth(stmtResult.getInt("width"));
				tmpPlayingBoard.setHeight(stmtResult.getInt("height"));
				tmpPlayingBoard.setNumberOfCheckpoints(stmtResult.getInt("numberOfCheckpoints"));
				tmpPlayingBoard.setMaxPlayers(stmtResult.getInt("maxPlayers"));
				tmpPlayingBoard.setImageFileName(stmtResult.getString("imageFileName"));
				tmpPlayingBoard.setDifficulty(stmtResult.getString("difficulty"));
				tmpPlayingBoard.setNickname(stmtResult.getString("nickname"));

				tmpMap.put(tmpPlayingBoard.getID(), tmpPlayingBoard);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmtResult != null) {
				try {
					stmtResult.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

		return tmpMap;
	}

	/**
	 * Löscht das Spielbrett
	 * 
	 * @param playingBoardName
	 *            Name des Spielbrettes
	 * @param playingboardId
	 *            ID des Spielbrettes in der Datenbank
	 * @return true
	 */
	@Override
	public boolean deletePlayingBoard(String playingBoardName, final int playingboardId) {
		// Spielbrettname erstellen
		playingBoardName = createFileName(playingBoardName.toLowerCase());

		/*
		 * Die Spielbrett-Datei löschen
		 */
		new File(getPlayingBoardFileNameAndRealPath(playingBoardName)).delete();

		/*
		 * Das Spielbrett-Bild löschen
		 */
		new File(getServletContext().getRealPath("/") + "/images/maps/" + playingBoardName + ".png").delete();

		/*
		 * Datenbankeintrag löschen
		 */
		final PreparedStatement deleteStmt = DBConnection.getPstmt(DBStatements.DELETE_PLAYINGBOARD.getStmt());

		try {
			deleteStmt.setInt(1, playingboardId);
			deleteStmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} 

		return true;
	}

	/**
	 * Lädt ein Playingboard anhand seines Namen aus der jeweiligen Datei
	 * 
	 * @param playingBoardName
	 *            Spielbrettname
	 * @return PlayingBoard
	 */
	@Override
	public PlayingBoard loadPlayingBoard(String playingBoardName) throws CantLoadPlayingBoardException {

		ResultSet stmtResult = null;
		PlayingBoard tmpPlayingBoard = null;
		ObjectInputStream o = null;

		// Spielbrettname erstellen
		playingBoardName = createFileName(playingBoardName.toLowerCase());

		try {
			if (!new File(getPlayingBoardFileNameAndRealPath(playingBoardName)).exists()) {
				throw new CantLoadPlayingBoardException();
			}

			o = new ObjectInputStream(getServletContext().getResourceAsStream(getPlayingBoardFileNameAndPath(playingBoardName)));
			tmpPlayingBoard = (PlayingBoard) o.readObject();

			// Nickname aus der DB holen:

			// PreparedStatement vorbereiten
			PreparedStatement playingBoardsStmt = DBConnection.getPstmt(DBStatements.SELECT_PLAYINGBOARD.getStmt());

			// Variablen binden
			playingBoardsStmt.setString(1, playingBoardName);

			// Statement ausführen
			stmtResult = playingBoardsStmt.executeQuery();

			while (stmtResult.next()) {
				tmpPlayingBoard.setNickname(stmtResult.getString("nickname"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (o != null) {
					o.close();
				}
				if (stmtResult != null) {
					stmtResult.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return tmpPlayingBoard;
	}

	/**
	 * Speichert ein PlayingBoard als *.plb auf den Server und trägt es in die DB ein. Wenn ein PlayingBoard existiert darf es nur
	 * durch den Ersteller oder einen Admin geändert werden.
	 * 
	 * @param playingboard
	 *            userID
	 * @return true
	 * @throws CantSavePlayingBoardException
	 *             Wenn das Spielbrett nicht gespeichert werden kann
	 * @throws CantLoadPlayingBoardException
	 * @throws PlayingBoardIsLockedException
	 */
	@Override
	public boolean savePlayingBoard(final PlayingBoard playingboard, final int userId, final boolean isAdmin)
			throws PlayingboardException, CantSavePlayingBoardException, CantLoadPlayingBoardException,
			PlayingBoardIsLockedException {

		/*
		 * Playingboardfelder verlinken
		 */
		playingboard.linkFieldsAndCheckSense();

		/*
		 * Pfad war/playingboards/*
		 */
		final String mapname = createFileName(playingboard.getName().toLowerCase());
		final String fileName = getPlayingBoardFileNameAndRealPath(mapname);

		// ImageFileName ändern
		playingboard.setImageFileName(mapname + ".png");

		/*
		 * PlayingBoard muss erst in die DB eingetragen werden um eine eindeutige ID zu bekommen.
		 */
		int playingBoardID = -1;

		/*
		 * Alles was im finally Block geschlossen wird
		 */
		ResultSet stmtResult = null;
		OutputStream fos = null;

		try {

			// Nur in die DB eintragen, wenn die Datei neu ist
			if (!new File(fileName).exists()) {

				/*
				 * UserId fürs Playingboard setzen
				 */
				playingboard.setUserID(userId);

				// PreparedStatement vorbereiten
				final PreparedStatement playingBoardStmt = DBConnection.getPstmt(DBStatements.INSERT_NEW_PLAYINGBOARD.getStmt());

				// Variablen binden
				playingBoardStmt.setString(1, playingboard.getName());
				playingBoardStmt.setString(2, playingboard.getDescription());
				playingBoardStmt.setInt(3, playingboard.getWidth());
				playingBoardStmt.setInt(4, playingboard.getHeight());
				playingBoardStmt.setInt(5, playingboard.getNumberOfCheckpoints());
				playingBoardStmt.setInt(6, playingboard.getMaxPlayers());
				playingBoardStmt.setString(7, playingboard.getImageFileName());
				playingBoardStmt.setInt(8, playingboard.getUserID());
				playingBoardStmt.setString(9, playingboard.getDifficulty());
				playingBoardStmt.setInt(10, playingboard.getConveyorBeltFieldRangeOneList().size()
						+ playingboard.getConveyorBeltFieldRangeTwoList().size());
				playingBoardStmt.setInt(11, playingboard.getCompactorFieldList().size());
				playingBoardStmt.setInt(12, playingboard.getPusherFieldList().size());
				playingBoardStmt.setInt(13, playingboard.getGearFieldList().size());
				playingBoardStmt.setInt(14, playingboard.getLaserCannonFieldList().size());

				// Statement ausführen
				playingBoardStmt.executeUpdate();

			} else { // Ansonsten DB einträge Updaten

				/*
				 * UserId des Erstellers herausfinden
				 */
				final PreparedStatement userIdStmt = DBConnection.getPstmt(DBStatements.SELECT_PLAYINGBOARD.getStmt());
				userIdStmt.setString(1, playingboard.getName());

				/*
				 * ErstellerId setzen
				 */
				final ResultSet ownerId = userIdStmt.executeQuery();
				ownerId.next();
				playingboard.setUserID(ownerId.getInt("userID"));
				
				
				/*
				 * Hier rausfinden, ob das PlayingBoard geändert werden darf:
				 */
				if (!isAdmin && playingboard.getUserID() != userId) {
					// PlayingBoards dürfen nur durch Admins oder Ersteller überschrieben werden:
					throw new CantSavePlayingBoardException();
				}


				// PreparedStatement vorbereiten
				final PreparedStatement playingBoardStmt = DBConnection.getPstmt(DBStatements.UPDATE_PLAYINGBOARD.getStmt());

				// Variablen binden
				playingBoardStmt.setString(1, playingboard.getDescription());
				playingBoardStmt.setInt(2, playingboard.getWidth());
				playingBoardStmt.setInt(3, playingboard.getHeight());
				playingBoardStmt.setInt(4, playingboard.getNumberOfCheckpoints());
				playingBoardStmt.setInt(5, playingboard.getMaxPlayers());
				playingBoardStmt.setString(6, playingboard.getImageFileName());
				playingBoardStmt.setInt(7, userId);
				playingBoardStmt.setString(8, playingboard.getDifficulty());
				playingBoardStmt.setInt(9, playingboard.getConveyorBeltFieldRangeOneList().size()
						+ playingboard.getConveyorBeltFieldRangeTwoList().size());
				playingBoardStmt.setInt(10, playingboard.getCompactorFieldList().size());
				playingBoardStmt.setInt(11, playingboard.getPusherFieldList().size());
				playingBoardStmt.setInt(12, playingboard.getGearFieldList().size());
				playingBoardStmt.setInt(13, playingboard.getLaserCannonFieldList().size());

				playingBoardStmt.setString(14, playingboard.getName());

				// Statement ausführen
				playingBoardStmt.executeUpdate();

			}

			// Jetzt die MAP holen (wegen der ID)
			final PreparedStatement playingBoardStmt2 = DBConnection.getPstmt(DBStatements.SELECT_PLAYINGBOARD.getStmt());

			// Variablen binden
			playingBoardStmt2.setString(1, playingboard.getName());

			// Statement ausführen
			stmtResult = playingBoardStmt2.executeQuery();

			// Result durchlaufen
			while (stmtResult.next()) {
				playingBoardID = stmtResult.getInt("playingBoardID");
			}

			// ID setzen
			playingboard.setID(playingBoardID);

			/*
			 * Hier wird die Datei geschrieben
			 */

			fos = new FileOutputStream(fileName);
			ObjectOutputStream o = new ObjectOutputStream(fos);
			o.writeObject(playingboard);

		} catch (IOException e) {
			e.printStackTrace();
			throw new CantSavePlayingBoardException();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CantSavePlayingBoardException();
		} finally {
			try {
				if (stmtResult != null) {
					stmtResult.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/*
		 * Das Vorschaubild für das Spielfeld erstellen
		 */
		final BufferedImage bImg = new BufferedImage(playingboard.getWidth() * 50, playingboard.getHeight() * 50,
				BufferedImage.TYPE_INT_RGB);

		/*
		 * Hinteergrund des Vorschaubilds grau zeichnen
		 */
		bImg.createGraphics().setBackground(Color.GRAY);

		try {

			for (int i = 0; i < playingboard.getHeight(); i++) {
				for (int j = 0; j < playingboard.getWidth(); j++) {
					for (DrawingInfo info : playingboard.getFields()[i][j].getImagePathList()) {

						/*
						 * Spielfeldbild aufs Vorschaubild zeichnen
						 */
						bImg.createGraphics().drawImage(
								ImageIO.read(new File(getServletContext().getRealPath("/") + "/" + info.getImageFileName())),
								j * 50 + info.getX(), i * 50 + info.getY(), info.getWidth(), info.getHeight(), null);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * Das Prieview-Bild wird in eine Bild-Datei geschrieben.
		 */
		try {
			ImageIO.write(bImg, playingboard.getImageFormat(), new File(getServletContext().getRealPath("/") + "/images/maps/"
					+ playingboard.getImageFileName()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

}
