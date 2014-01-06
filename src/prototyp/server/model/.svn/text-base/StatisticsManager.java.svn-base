package prototyp.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import prototyp.shared.useradministration.Statistic;

/**
 * StatisticsManager
 * 
 * @author Andreas
 * @version 1.0
 * 
 * @see {@link Round}
 */
public class StatisticsManager implements Serializable {

	/** Seriennummer */
	private static final long serialVersionUID = -1142480363396240669L;

	/** Gibt die Statistiken an */
	private List<Statistic> statistic;

	/**
	 * Default-Konstruktor
	 */
	public StatisticsManager() {
		this.statistic = new ArrayList<Statistic>();
	}

	/**
	 * Konstruktor
	 * 
	 * @param statistic
	 *            Liste mit Statistic
	 */
	public StatisticsManager(List<Statistic> statistic) {
		this.statistic = statistic;
	}

	/**
	 * Liefert die List mit Statistic-Objekten
	 * 
	 * @return statistic
	 */
	public List<Statistic> getStatistic() {
		return this.statistic;
	}

	/**
	 * Setzt eine neue Liste mit Statistiken
	 * 
	 * @param statistic
	 *            List mit Statistic
	 */
	public void setStatistic(List<Statistic> statistic) {
		this.statistic = statistic;
	}

}
