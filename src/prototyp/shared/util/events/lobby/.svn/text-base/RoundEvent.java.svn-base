package prototyp.shared.util.events.lobby;

import java.util.Map;

import prototyp.shared.round.RoundInfo;
import de.novanic.eventservice.client.event.Event;

/**
 * Dieses Event wird geschmissen wenn eine neue Round erstellt wird und ist für
 * die Lobby sinnvoll. Einhält alle Rounds
 * 
 * @author timo
 * @version 1.0
 * @versipn 1.1
 * 
 */
public class RoundEvent implements Event {
	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 9211083058335988898L;

	/** Liste mit Runden */
	private Map<Integer, RoundInfo> rounds;

	/** Default-Konstruktor */
	public RoundEvent() {

	}

	/**
	 * Konstruktor
	 * 
	 * @param rounds
	 *            Runden
	 */
	public RoundEvent(Map<Integer, RoundInfo> rounds) {
		this.rounds = rounds;
	}

	/**
	 * Einhält alle aktuellen Rounden.
	 * 
	 * @return Map<Integer, Round>
	 */
	public Map<Integer, RoundInfo> getRounds() {
		return this.rounds;
	}
}
