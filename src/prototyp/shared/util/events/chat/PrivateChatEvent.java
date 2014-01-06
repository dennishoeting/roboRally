package prototyp.shared.util.events.chat;

import prototyp.client.view.Page;
import de.novanic.eventservice.client.event.Event;

/**
 * Privates Chat Event (fürs Flüstern)
 * 
 * @author timo
 * @version 1.0
 * 
 */
public class PrivateChatEvent implements Event {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -4283394072948506085L;

	/**
	 * Nachricht
	 */
	private String message;

	/** Absender */
	private String from;

	/** Empfänger */
	private String to;

	/** Zeitangabe */
	private String time;

	/**
	 * Default
	 */
	public PrivateChatEvent() {

	}

	/**
	 * Konstruktor
	 * 
	 * @param message
	 *            Nachricht
	 * @param from
	 *            Absender
	 * @param to
	 *            Empfänger
	 * @param time
	 *            Zeitangabe
	 */
	public PrivateChatEvent(String message, String from, String to, String time) {
		this.message = message;
		this.from = from;
		this.to = to;
		this.time = time;
	}

	/**
	 * Liefert den Absender
	 * 
	 * @return Sender
	 */
	public String getFrom() {
		return this.from;
	}

	/**
	 * Liefert die Nachricht
	 * 
	 * @return Nachricht
	 */
	public String getMessage() {
		return "<font color='red'><strong>" + this.time + " " + this.from + " " + Page.props.chatPresenterWhisper()
				+ ":</strong></font>" + this.message;
	}

	/**
	 * Liefert den Empfänger
	 * 
	 * @return Empfänger
	 */
	public String getTo() {
		return this.to;
	}

}
