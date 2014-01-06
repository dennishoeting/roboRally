package prototyp.shared.util.events.chat;

import prototyp.client.view.Page;

/**
 * ChatEvent, wenn das PostGame betreten wird.
 * 
 * @author Andreas
 * @version 1.0
 */
public class ChatEventEnterPostGame extends ChatEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = 571604123095353168L;

	/** Nickname */
	private String nickname;

	/** Zeitangabe */
	private String time;

	/** Farbe */
	private String color;

	/**
	 * Default
	 */
	public ChatEventEnterPostGame() {

	}

	/**
	 * Normaler Konstruktor
	 * 
	 * @param nickname
	 *            Nickname
	 * @param time
	 *            Zeit
	 * @param color
	 *            Farbe
	 * @param sound
	 *            Dateiname
	 */
	public ChatEventEnterPostGame(String nickname, String time, String color, String sound) {
		super.sound = sound;
		this.nickname = nickname;
		this.time = time;
		this.color = color;
	}

	/**
	 * getMessage() überschreiben, um die Props zu nutzen
	 * 
	 * @return Chatnachricht
	 */
	@Override
	public String getMessage() {
		return "<strong><font color=" + this.color + ">" + this.time + " " + this.nickname + " "
				+ Page.props.chatPresenterEnterPostGame() + " </font></strong>";
	}
}
