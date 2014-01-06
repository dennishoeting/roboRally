package prototyp.shared.useradministration;

import java.io.Serializable;

/**
 * Statistic eines Nutzers
 * 
 * @author Andreas
 * @version 1.0
 * @version 1.1 points in Rang geändert. Timo
 * 
 * @see {@link User}, {@link AccountData}
 */
public class Statistic implements Serializable {

	/** Seriennnummer */
	private static final long serialVersionUID = -8782660838869425929L;

	/** Anzahl der abgebrochenden Spiele */
	private int abortedGames;

	/** Rank des Users */
	private int highScoreRang;

	/** Anzahl der gespielten Spiele */
	private int playedGames;

	/** Erworbene Punkte */
	private int points;

	/** Spielerlevel */
	private String userRang;

	/** Anzahl der gewonnenden Spiele */
	private int wins;

	/**
	 * Default-Konstruktor
	 */
	public Statistic() {
		this.wins = 0;
		this.highScoreRang = 0;
		this.playedGames = 0;
		this.abortedGames = 0;
		this.points = 0;
		this.userRang = null;
	}

	/**
	 * Konstruktor
	 * 
	 * @param highScoreRang
	 *            Rang im HighScore
	 * @param wins
	 *            Anzahl der gewonnenden Spiele
	 * @param playedGames
	 *            Anzahl der gespielten Spiele
	 * @param abortedGames
	 *            Anzahl der abgebrochenen Spiele
	 * @param userRang
	 *            Rang in der DB
	 */
	public Statistic(int wins, int highScoreRang, int playedGames, int abortedGames, int points, String userRang) {
		this.wins = wins;
		this.highScoreRang = highScoreRang;
		this.playedGames = playedGames;
		this.abortedGames = abortedGames;
		this.points = points;
		this.userRang = userRang;
	}

	/**
	 * Liefert die Anzahl der abgebrochenen Spiele
	 * 
	 * @return Anzahl der abgebrochenen Spiele
	 */
	public int getAbortedGames() {
		return this.abortedGames;
	}

	/**
	 * Liefert den HighScore-Rang
	 * 
	 * @return HighScore-Rang
	 */
	public int getHighScoreRang() {
		return this.highScoreRang;
	}

	/**
	 * Liefert die Anzahl der verlorenen Spiele
	 * 
	 * @return Anzahl der verlorenen Spiele
	 */
	public int getLostGames() {
		return this.playedGames - this.abortedGames - this.wins;
	}

	/**
	 * Liefert die Anzahl der gespielten Spiele
	 * 
	 * @return Anzahl der gespielten Spiele
	 */
	public int getPlayedGames() {
		return this.playedGames;
	}

	/**
	 * Liefert die Punkte
	 * 
	 * @return Punkte
	 */
	public int getPoints() {
		return this.points;
	}

	/**
	 * Liefert den Userrang
	 * 
	 * @return Userrang
	 */
	public String getUserRang() {
		return this.userRang;
	}

	/**
	 * Liefert die gewonnenden Spiele
	 * 
	 * @return wins
	 */
	public int getWins() {
		return this.wins;
	}

	/**
	 * Erhöht die Anzahl der abgebrochenen Spiele um eins
	 * 
	 * @return true, falls es geklappt hat
	 */
	public boolean incAbortedGames() {
		++this.abortedGames;
		return true;
	}

	/**
	 * Erhöht die Anzahl der gespielten Spiele um eins
	 * 
	 * @return true, falls es geklappt hat
	 */
	public boolean incPlayedGames() {
		++this.playedGames;
		return true;
	}

	/**
	 * Erhöht die Punkte um einen bestimmten Wert
	 * 
	 * @return true, falls es geklappt hat
	 */
	public boolean incPoints(int bonus) {
		this.points += bonus;
		return true;
	}

	/**
	 * Erhöht die Anzahl der gewonnenden Spiele um eins
	 * 
	 * @return true, falls es geklappt hat
	 */
	public boolean incWins() {
		++this.wins;
		return true;
	}

	/**
	 * Setzt die abgebrochenen Spiele
	 * 
	 * @param abortedGames
	 *            Anzahl der Spiele
	 */
	public void setAbortedGames(int abortedGames) {
		this.abortedGames = abortedGames;
	}

	/**
	 * Setzt die Anzahl der gespielten Spiele
	 * 
	 * @param playedGames
	 *            Anzahl der gespielten Spiele
	 */
	public void setPlayedGames(int playedGames) {
		this.playedGames = playedGames;
	}

	/**
	 * Setzt eine neue Anzahl an gewonnenden Spiele
	 * 
	 * @param wins
	 *            Anzahl an gewonnenden Spiele
	 */
	public void setWins(int wins) {
		this.wins = wins;
	}
}
