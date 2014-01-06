package prototyp.shared.util.events.chat;

import prototyp.client.view.Page;

/**
 * Wird ausgelöst, wenn der Countdown (PreGame) gestartet wird. Als extra Klasse, um die props benutzen zu können.
 * 
 * @author timo
 * @version 1.0
 */
public class ChatEventCountDownStart extends ChatEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 7429052685917279667L;

	/** Zeit */
	private String time;

	/** Farbe */
	private String color;

	/**
	 * Default
	 */
	public ChatEventCountDownStart() {
		super();
	}

	/**
	 * Konstruktor
	 * 
	 * @param color
	 *            Farbe
	 * @param time
	 *            Zeit
	 * @param sound
	 *            Dateiname der Sounddatei
	 */
	public ChatEventCountDownStart(String color, String time, String sound) {
		super.sound = sound;
		super.message = null;
		this.time = time;
		this.color = color;
	}

	/**
	 * Liefert die ChatNachricht
	 * 
	 * @retrun Nachricht
	 */
	@Override
	public String getMessage() {
		return "<strong>" + this.time + ": " + "<font color=" + this.color + ">" + Page.props.chatPresenterCountDownStart()
				+ "</font></strong>";
	}

}
