package prototyp.client.util;

import prototyp.shared.round.PlayingBoard;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Hilfsklasse für das MapGeneratorLoadingPopUp
 * 
 * @author Timo, Andreas
 * @version 1.1
 */
public class MapGeneratorLoadingRecord extends ListGridRecord {

	/** Name des Spielbrettes */
	private final String playingboardName;

	/** Name des Vorschaubildes */
	private final String playingboardImageName;

	/** Breite des Spielbrettes */
	private final int playingboardWidth;

	/** Höhe des Spielbrettes */
	private final int playingboardHeight;

	/**
	 * Konstruktor
	 * 
	 * @param playingBoard
	 *            Spielbrett
	 */
	public MapGeneratorLoadingRecord(final PlayingBoard playingBoard) {

		setAttribute("playingBoardID", playingBoard.getID());
		setAttribute("name", playingBoard.getName());
		setAttribute("size", playingBoard.getWidth() + " * " + playingBoard.getHeight());
		setAttribute("height", playingBoard.getHeight());
		setAttribute("width", playingBoard.getWidth());
		setAttribute("numberOfCheckpoints", playingBoard.getNumberOfCheckpoints());
		setAttribute("maxPlayers", playingBoard.getMaxPlayers());
		setAttribute("imageFileName", playingBoard.getImageFileName());
		setAttribute("nickname", playingBoard.getNickname());

		// Attribute speichern
		this.playingboardName = playingBoard.getName();
		this.playingboardImageName = playingBoard.getImageFileName();
		this.playingboardWidth = playingBoard.getWidth();
		this.playingboardHeight = playingBoard.getHeight();
	}

	/**
	 * Liefert den Namen des Vorschaubildes
	 * 
	 * @return playingboardImageName
	 */
	public String getPlayingboardImageName() {
		return this.playingboardImageName;
	}

	/**
	 * Liefert den Namen des Spielbrettes
	 * 
	 * @return playingboardName
	 */
	public String getPlayingboardName() {
		return this.playingboardName;
	}

	/**
	 * Liefert die Breite des Spielbrettes
	 * 
	 * @return playingboardWidth
	 */
	public int getPlayingboardWidth() {
		return playingboardWidth;
	}

	/**
	 * Liefert die Höhe des Spielbrettes
	 * 
	 * @return playingboardHeight
	 */
	public int getPlayingboardHeight() {
		return playingboardHeight;
	}
}