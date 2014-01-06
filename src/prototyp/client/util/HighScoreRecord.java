package prototyp.client.util;

import prototyp.client.view.lobby.HighScorePage;
import prototyp.shared.useradministration.Statistic;
import prototyp.shared.useradministration.User;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Record für das HighScoreListGrid
 * 
 * @author Timo
 * @version 1.0
 * @version 1.1 Rank hinzugefügt.
 * 
 * @see {@link HighScorePage}
 */
public class HighScoreRecord extends ListGridRecord {

	/**
	 * Konstruktor
	 * 
	 * @param user
	 *            User-Objekt
	 */
	public HighScoreRecord(User user) {
		Statistic stats = user.getAccountData().getStatistic();
		setAttribute("userID", user.getId());
		setAttribute("rank", stats.getHighScoreRang());
		setAttribute("nickname", user.getAccountData().getNickname());
		setAttribute("wins", stats.getWins());
		setAttribute("abortedGames", stats.getAbortedGames());
		setAttribute("playedGames", stats.getPlayedGames());
		// losts = PlayedRounds - aborted - wins
		int losts = stats.getPlayedGames() - stats.getAbortedGames() - stats.getWins();
		setAttribute("losts", losts);
		setAttribute("points", stats.getPoints());
	}
}