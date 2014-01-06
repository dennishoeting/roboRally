package prototyp.client.util;

import prototyp.shared.round.RoundInfo;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Hilfsklasse für die Anzeige der Spiele in der Lobby
 * 
 * @author Andreas Rehfeldt
 * @version 1.0
 */
public class GameRecord extends ListGridRecord {
	/** Round */
	private RoundInfo round;

	public GameRecord(RoundInfo round) {
		this.round = round;
		setAttribute("gameName", round.getRoundOption().getGameName());
		setAttribute("mapName", round.getPlayingboard().getName());
		setAttribute("playerCount", round.getRoundOption().getPlayersSlots()
				- round.getFreePlayerSlots() + "/"
				+ round.getRoundOption().getPlayersSlots());
		setAttribute("watcherCount", round.getRoundOption().getWatchersSlots()
				- round.getFreeWatcherSlots() + "/"
				+ round.getRoundOption().getWatchersSlots());
		setAttribute("difficulty", round.getPlayingboard().getDifficulty());
		if (round.isStarted()) {
			this.setAttribute("timeElapsed", "icons/roundStarted.png");
		} else {
			this.setAttribute("timeElapsed", "icons/roundWaiting.png");
		}
	}

	/**
	 * Beinhaltet Spielnamen, Mapnamen, Spieleranzahl, Beobachteranzahl und
	 * Schwierigkeit
	 * 
	 * @return round
	 */
	public RoundInfo getRound() {
		return this.round;
	}
}
