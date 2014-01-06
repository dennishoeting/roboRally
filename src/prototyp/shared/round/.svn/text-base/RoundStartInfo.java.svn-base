package prototyp.shared.round;

import java.io.Serializable;

/**
 * Wird als Event geschickt
 * 
 * @author Marcus
 * @version 1.0
 */
public class RoundStartInfo implements Serializable {

	/**
	 * Seriennumer
	 */
	private static final long serialVersionUID = 1L;

	/** das Playingboard */
	private PlayingBoard playingBoard;


	/**
	 * Default-Konstruktor für Serializable-Interface
	 */
	public RoundStartInfo() {
	}

	/**
	 * Konstruktor
	 * 
	 * @param playingBoard
	 *            Spielbrett
	 * @param logicManager
	 *            LogicManager
	 */
	public RoundStartInfo(PlayingBoard playingBoard) {
		this.playingBoard = playingBoard;
	}



	/**
	 * @return the playingBoard
	 */
	public PlayingBoard getPlayingBoard() {
		return this.playingBoard;
	}


	/**
	 * @param playingBoard
	 *            the playingBoard to set
	 */
	public void setPlayingBoard(PlayingBoard playingBoard) {
		this.playingBoard = playingBoard;
	}

}
