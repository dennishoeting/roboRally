package prototyp.shared.util.events.chat;

import prototyp.client.view.Page;

/**
 * Chatnachricht, wenn das Spiel betreten wird. Als extra Klasse, wegen den Properties..
 * 
 * @author timo
 * @version 1.0
 */
public class ChatEventEnterGame extends ChatEvent {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -3441173855703248086L;

	/** Nickname */
	private String nickname;

	/** Farbe */
	private String color;

	/** Zeitangabe */
	private String time;

	/**
	 * Default
	 */
	public ChatEventEnterGame() {
		super();
		this.nickname = null;
		this.color = null;
		this.time = null;
	}

	/**
	 * Konstruktor
	 * 
	 * @param nickname
	 *            Nickname
	 * @param color
	 *            Farbe
	 * @param time
	 *            Zeitangabe
	 * @param sound
	 *            Sounddatei
	 */
	public ChatEventEnterGame(String nickname, String color, String time, String sound) {
		super.sound = sound;
		this.nickname = nickname;
		this.time = time;
		this.color = color;
	}

	/**
	 * Liefert die Chatnachricht
	 * 
	 * @return Nachricht
	 */
	@Override
	public String getMessage() {
		return "<strong><font color=" + this.color + ">" + this.time + " " + this.nickname + " "
				+ Page.props.chatPresenterEnterGame() + "</font></strong>";
	}

}
