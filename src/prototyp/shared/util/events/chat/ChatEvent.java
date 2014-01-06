package prototyp.shared.util.events.chat;

import de.novanic.eventservice.client.event.Event;

/**
 * Dieses Event soll bei neuen Nachrichten geschmissen werden.
 * 
 * @author Timo, Andreas
 * @version 1.0
 * @version 1.1 Sound hinzugekommen
 */
public class ChatEvent implements Event {

	/** Seriennummer */
	private static final long serialVersionUID = 2037188548577100479L;

	/** Message */
	protected String message;

	/** Sounddatei */
	protected String sound;

	/**
	 * Default-Konstruktor
	 */
	public ChatEvent() {
		this.message = null;
		this.sound = null;
	}

	/**
	 * Konstruktor
	 * 
	 * @param message
	 *            Nachricht
	 * @param sound
	 *            Dateiname der Sounddatei, die beim Schreiben ausgegeben werden
	 *            soll
	 */
	public ChatEvent(String message, String sound) {
		this.message = message;
		this.sound = sound;
	}

	/**
	 * Liefert die Nachricht
	 * 
	 * @return String
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Liefert den Dateinamen der Sounddatei
	 * 
	 * @return sound oder null
	 */
	public String getSound() {
		return this.sound;
	}

	/**
	 * Setzt eine neue Nachricht
	 * 
	 * @param message
	 *            Nachricht
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Setzt den Dateinamen der Sounddatei
	 * 
	 * @param sound
	 *            String des Dateinamen
	 */
	public void setSound(String sound) {
		this.sound = sound;
	}
}
